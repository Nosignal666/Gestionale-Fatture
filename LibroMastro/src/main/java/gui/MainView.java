package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import storage.DataBaseStorageManager;
import strutturaDati.Fattura;
import strutturaDati.MalformedDataException;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.BoxLayout;
import java.awt.Color;
import java.awt.Desktop;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;


import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class MainView implements Observer {
	
	ArrayList<Fattura> fattureTrovate;
    private AppModel appModel;
	private JFrame frmGestionaleFatture;
	private JTextField campoPartitaIva;
	private JTextField campoNomeAzienda;
	private JTable tabellaFatture;
	private CercaAziendeView cav=null;


	/**
	 * Create the application.
	 */
	public MainView(AppModel appModel) {
		this.appModel=appModel;
		appModel.aggiungiObserver(this);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGestionaleFatture = new JFrame();
		frmGestionaleFatture.setTitle("Gestionale Fatture");
		frmGestionaleFatture.setBounds(100, 100, 1348, 774);
		frmGestionaleFatture.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGestionaleFatture.getContentPane().setLayout(new BoxLayout(frmGestionaleFatture.getContentPane(), BoxLayout.X_AXIS));
		
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JScrollPane scrollPane=new JScrollPane(tabbedPane);
		frmGestionaleFatture.getContentPane().add(scrollPane);
		
		JPanel CercaFatturePanel = new JPanel();
		tabbedPane.addTab("Panello Principale", null, CercaFatturePanel, null);
		CercaFatturePanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(88dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(63dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(90dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(80dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(98dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(60dlu;default):grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		
		
		JLabel lblNewLabel_8 = new JLabel("Partita Iva:");
		lblNewLabel_8.setFont(new Font("Tahoma", Font.BOLD, 15));
		CercaFatturePanel.add(lblNewLabel_8, "2, 2");
		
		campoPartitaIva = new JTextField();
		CercaFatturePanel.add(campoPartitaIva, "4, 2, fill, fill");
		campoPartitaIva.setColumns(10);
		
		JLabel lblNewLabel_9 = new JLabel("Nome Azienda:");
		lblNewLabel_9.setFont(new Font("Tahoma", Font.BOLD, 15));
		CercaFatturePanel.add(lblNewLabel_9, "2, 4, fill, fill");
		
		campoNomeAzienda = new JTextField();
		CercaFatturePanel.add(campoNomeAzienda, "4, 4, fill, fill");
		campoNomeAzienda.setColumns(10);
		
		JPopupMenu popupMenu=new JPopupMenu();
		JMenuItem getPdfFileMenuItem=new JMenuItem("Salva pdf");
		getPdfFileMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileOutputStream fos=null;
				byte[] pdfFileContent;
				try {
					String codiceUnivoco=(String)tabellaFatture.getValueAt(tabellaFatture.getSelectedRow(),0);
					String partitaIva=(String)tabellaFatture.getValueAt(tabellaFatture.getSelectedRow(),2);
					pdfFileContent=appModel.getPdfFileContent(codiceUnivoco, partitaIva);					
					JFileChooser fc=new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fc.setFileHidingEnabled(true);
					fc.showSaveDialog(CercaFatturePanel);
					if(fc.getSelectedFile()!=null) {
						File pdfFile=new File(fc.getSelectedFile().getAbsolutePath()+"\\Fattura"+codiceUnivoco+".pdf");
						fos=new FileOutputStream(pdfFile);
						fos.write(pdfFileContent);
						Desktop.getDesktop().open(pdfFile);
					}
					
				} catch (SQLException |IOException  e1) {
					new LogView(e1);
					e1.printStackTrace();
				}finally {
					if(fos!=null)
						try {
							fos.close();
						} catch (IOException e1) {}
				}
				
				
			}
		});JMenuItem modificaFatturaMenuItem=new JMenuItem("Modifica");
		modificaFatturaMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String codiceUnivoco=(String)tabellaFatture.getValueAt(tabellaFatture.getSelectedRow(), 0);
				String partitaIva=(String)tabellaFatture.getValueAt(tabellaFatture.getSelectedRow(), 2);
				Fattura fatturaOriginale=appModel.getFattura(codiceUnivoco, partitaIva);
				new ModificaFatturaView(appModel, fatturaOriginale);
				
				
			}
		});
		popupMenu.add(modificaFatturaMenuItem);
		popupMenu.add(getPdfFileMenuItem);
		tabellaFatture = new JTable();
		tabellaFatture.setComponentPopupMenu(popupMenu);
		tabellaFatture.setModel(new TabellaFatturaModel(appModel.getFatture()));
		JScrollPane scrollPane2 = new JScrollPane(tabellaFatture);
		CercaFatturePanel.add(scrollPane2, "2, 12, 13, 1, fill, fill");
			
			JButton bottoneInserisciAzienda = new JButton("Aggiungi azienda");
			bottoneInserisciAzienda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new InserisciAziendaView(appModel);
				}
			});
			
			JButton bottoneInserisciFattura = new JButton("Aggiungi fattura");
			bottoneInserisciFattura.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new InserisciFatturaView(appModel);
				}
			});
			
				JButton bottoneCercaPerPartitaIva = new JButton("Cerca ");
				bottoneCercaPerPartitaIva.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(cav==null) cav=new CercaAziendeView(appModel);
						appModel.fetch(campoNomeAzienda.getText(),campoPartitaIva.getText());
					}
				});
				
				
				bottoneCercaPerPartitaIva.setFont(new Font("Tahoma", Font.ITALIC, 15));
				bottoneCercaPerPartitaIva.setFocusable(false);
				CercaFatturePanel.add(bottoneCercaPerPartitaIva, "4, 8, fill, fill");
			bottoneInserisciFattura.setFont(new Font("Tahoma", Font.ITALIC, 15));
			CercaFatturePanel.add(bottoneInserisciFattura, "6, 8, fill, fill");
			bottoneInserisciAzienda.setFont(new Font("Tahoma", Font.ITALIC, 15));
			CercaFatturePanel.add(bottoneInserisciAzienda, "8, 8, fill, fill");
		tabellaFatture.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				int hoveredRow=tabellaFatture.rowAtPoint(e.getPoint());
				int hoveredCol=tabellaFatture.columnAtPoint(e.getPoint());
				if(hoveredRow!=tabellaFatture.getSelectedRow() | hoveredCol!=tabellaFatture.getSelectedColumn()) tabellaFatture.changeSelection(hoveredRow, hoveredCol, false, false);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
		tabellaFatture.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1 & e.getClickCount()==2) {
					TabellaFatturaModel tfm=(TabellaFatturaModel)tabellaFatture.getModel();
					String codiceUnivoco=(String) tfm.getValueAt(tabellaFatture.getSelectedRow(),0);
					String partitaIva=(String) tfm.getValueAt(tabellaFatture.getSelectedRow(), 2);
					new DettagliFatturaView(appModel,appModel.getFattura(codiceUnivoco, partitaIva));
				}
				
			}
		});

		frmGestionaleFatture.setVisible(true);
	}

	@Override
	public void notificaCambiamento() {
		TabellaFatturaModel tfm=(TabellaFatturaModel)tabellaFatture.getModel();
		tfm.fireTableDataChanged();
	}
	
}
