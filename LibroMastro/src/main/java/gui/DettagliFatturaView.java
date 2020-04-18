package gui;
import javax.swing.JFrame;
import strutturaDati.Fattura;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;


public class DettagliFatturaView implements Observer {
	
	private Fattura fattura;
	private AppModel appModel;
    private JFrame frmGestionaleFatture;
	private JTextField campoUtente;
	private JTextField campoNrFattura;
	private JTextArea areaNote;
	private JLabel lblNewLabel_3;
	private JScrollPane scrollPane;
	private JTable tabellaPagamentiParziali;
	private JButton bottoneInserisciPagamentoParziale;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JTextField campoStatoPagamento;
	private JTextField campoImportoRimanente;


	/**
	 * Create the application.
	 */
	public DettagliFatturaView(AppModel appModel,Fattura fattura) {
		this.appModel=appModel;
		appModel.aggiungiObserver(this);
		this.fattura=fattura;
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */	
	private void initialize() {
		
		frmGestionaleFatture = new JFrame();
		frmGestionaleFatture.setTitle("Gestionale Fatture - Dettagli");
		frmGestionaleFatture.setBounds(100, 100, 1086, 812);
		frmGestionaleFatture.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmGestionaleFatture.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(184dlu;default):grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(123dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(52dlu;default)"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(126dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(239dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel_2 = new JLabel("Fattura ");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_2, "4, 4, left, default");
		
		campoNrFattura = new JTextField();
		campoNrFattura.setEditable(false);
		frmGestionaleFatture.getContentPane().add(campoNrFattura, "6, 4, 3, 1, left, default");
		campoNrFattura.setColumns(10);
		
		lblNewLabel_4 = new JLabel("Stato pagamento: ");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_4, "10, 4, right, default");
		
		campoStatoPagamento = new JTextField();
		campoStatoPagamento.setEditable(false);
		frmGestionaleFatture.getContentPane().add(campoStatoPagamento, "12, 4, left, default");
		campoStatoPagamento.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Ultima modifica:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel, "4, 6, left, default");
		
		campoUtente = new JTextField();
		campoUtente.setEditable(false);
		frmGestionaleFatture.getContentPane().add(campoUtente, "6, 6, 3, 1, left, default");
		campoUtente.setColumns(10);
		
		lblNewLabel_5 = new JLabel("Importo rimanente:");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_5, "10, 6, right, default");
		
		campoImportoRimanente = new JTextField();
		campoImportoRimanente.setEditable(false);
		frmGestionaleFatture.getContentPane().add(campoImportoRimanente, "12, 6, fill, default");
		campoImportoRimanente.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Note:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_1, "4, 8");
		
		areaNote = new JTextArea();
		areaNote.setLineWrap(true);
		areaNote.setEditable(false);
		frmGestionaleFatture.getContentPane().add(areaNote, "6, 8, fill, fill");
		
		lblNewLabel_3 = new JLabel("Pagamenti Parziali:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_3, "4, 10");
		
		JPopupMenu popupMenu=new JPopupMenu();
		JMenuItem ModificaPagamentoParzialemenuItem=new JMenuItem("Modifica");
		ModificaPagamentoParzialemenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int indicePagamento=(int)tabellaPagamentiParziali.getValueAt(tabellaPagamentiParziali.getSelectedRow(), 0);
				new ModificaPagamentoParzialeView(appModel, fattura, indicePagamento);
			}
		});
		popupMenu.add(ModificaPagamentoParzialemenuItem);
		
		tabellaPagamentiParziali = new JTable();
		tabellaPagamentiParziali.setModel(new TabellaPPModel());
		tabellaPagamentiParziali.setComponentPopupMenu(popupMenu);
		scrollPane = new JScrollPane(tabellaPagamentiParziali);
		frmGestionaleFatture.getContentPane().add(scrollPane, "6, 10, 3, 1, fill, fill");
		
		
		bottoneInserisciPagamentoParziale = new JButton("Nuovo Pagamento");
		bottoneInserisciPagamentoParziale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new InserisciPagamentoParzialeView(appModel,fattura);
			}
		});
		
		bottoneInserisciPagamentoParziale.setFont(new Font("Tahoma", Font.ITALIC, 15));
		frmGestionaleFatture.getContentPane().add(bottoneInserisciPagamentoParziale, "4, 12");
		tabellaPagamentiParziali.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				int hoveredRow=tabellaPagamentiParziali.rowAtPoint(e.getPoint());
				if(hoveredRow!=tabellaPagamentiParziali.getSelectedRow()) tabellaPagamentiParziali.changeSelection(hoveredRow, 0, false, false);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		try {
			fillView();
		} catch (SQLException e1) {
			e1.printStackTrace();
			new LogView(e1);
		}
		frmGestionaleFatture.setVisible(true);
	}
	
	private void fillView() throws SQLException {
		fattura=appModel.getFattura(fattura.getCodiceUnivoco(),fattura.getPartitaIva());
		if(fattura==null) {
			frmGestionaleFatture.dispose();
			throw new SQLException("identificatori fattura modificati,la finestra Dettagli Fattura è stata chiusa;");
		}
		TabellaPPModel tppm=(TabellaPPModel)tabellaPagamentiParziali.getModel();
		tppm.setPagamentiParziali(fattura.getPagamentiParziali());
		campoNrFattura.setText(fattura.getCodiceUnivoco());
		campoUtente.setText(fattura.getUser());
		areaNote.setText(fattura.getNote());
		campoImportoRimanente.setText(fattura.getImportoRimanente().toString());
		campoStatoPagamento.setText(fattura.getStato());
		tppm.fireTableDataChanged();
	}
	
	@Override
	public void notificaCambiamento() {
		try {
			fillView();
		}catch(Exception e) {
			e.printStackTrace();
			new LogView(e);
		}
		
	}
	
}
