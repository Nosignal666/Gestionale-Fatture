package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import strutturaDati.MalformedDataException;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;

public class CercaAziendeView implements Observer{

	private JFrame frmGestionaleFatture;
	private JTable tabellaAziende;
	private AppModel appModel;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JTextField campoDataInizio;
	private JTextField campoDatafine;
	private JButton btnNewButton;
	

	/**
	 * Create the application.
	 */
	public CercaAziendeView(AppModel appModel) {
		this.appModel=appModel;
		appModel.aggiungiObserver(this);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGestionaleFatture = new JFrame();
		frmGestionaleFatture.setTitle("Gestionale Fatture - Aziende");
		frmGestionaleFatture.setBounds(100, 100, 420, 564);
		frmGestionaleFatture.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmGestionaleFatture.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(82dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(67dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(44dlu;default)"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:max(220dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		tabellaAziende=new JTable();
		tabellaAziende.setModel(new TabellaAziendeModel(appModel.getAziende()));
		JScrollPane scrollPane = new JScrollPane(tabellaAziende);
		frmGestionaleFatture.getContentPane().add(scrollPane, "2, 2, 5, 1, fill, fill");
		
		lblNewLabel = new JLabel("Data inizio:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel, "2, 4, left, default");
		
		campoDataInizio = new JTextField();
		frmGestionaleFatture.getContentPane().add(campoDataInizio, "4, 4, fill, default");
		campoDataInizio.setColumns(10);
		campoDataInizio.setText("dd/MM/yyyy");
		campoDataInizio.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(campoDataInizio.getText().equals("")) campoDataInizio.setText("dd/MM/yyyy");
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(campoDataInizio.getText().equals("dd/MM/yyyy")) campoDataInizio.setText("");
				
			}
		});
		
		lblNewLabel_1 = new JLabel("Data fine:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_1, "2, 6, left, default");
		
		campoDatafine = new JTextField();
		frmGestionaleFatture.getContentPane().add(campoDatafine, "4, 6, fill, default");
		campoDatafine.setColumns(10);
		campoDatafine.setText("dd/MM/yyyy");
		campoDatafine.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(campoDatafine.getText().equals("")) campoDatafine.setText("dd/MM/yyyy");
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(campoDatafine.getText().equals("dd/MM/yyyy")) campoDatafine.setText("");
				
			}
		});
		
		btnNewButton = new JButton("Genera estratto Conto");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					String nomeAzienda=(String)tabellaAziende.getValueAt(tabellaAziende.getSelectedRow(),0);
					String partitaIva=(String)tabellaAziende.getValueAt(tabellaAziende.getSelectedRow(),1);
					appModel.scriviEstrattoConto(nomeAzienda, partitaIva,
							LocalDate.parse(campoDataInizio.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
							LocalDate.parse(campoDatafine.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				} catch (Exception e) {
					System.out.println("ci sono");
			
					new LogView(e);
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.ITALIC, 15));
		frmGestionaleFatture.getContentPane().add(btnNewButton, "2, 8");
	}

	@Override
	public void notificaCambiamento() {
		frmGestionaleFatture.setVisible(true);
		TabellaAziendeModel tam=(TabellaAziendeModel)tabellaAziende.getModel();
		tam.fireTableDataChanged();
		
	}

}
