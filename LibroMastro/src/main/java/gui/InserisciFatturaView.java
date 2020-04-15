package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import strutturaDati.Fattura;
import strutturaDati.MalformedDataException;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

import org.joda.money.Money;

import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;

public class InserisciFatturaView {

	private JFrame frmGestionaleFatture;
	private AppModel appModel;
	private JTextField campoCodiceUnivoco;
	private JTextField campoNomeAzienda;
	private JTextField campoDataEmissione;
	private JTextField campoDataScadenza;
	private JTextField campoImporto;
	private JTextField campoPartitaIva;
	JTextArea areaNote;

	/**
	 * Create the application.
	 */
	public InserisciFatturaView(AppModel appModel) {
		this.appModel=appModel;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGestionaleFatture = new JFrame();
		frmGestionaleFatture.setTitle("Gestionale Fatture - Aggiungi fattura");
		frmGestionaleFatture.setBounds(100, 100, 521, 518);
		frmGestionaleFatture.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmGestionaleFatture.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(102dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(64dlu;default)"),},
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
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(129dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		
		JLabel lblNewLabel = new JLabel("Codice univoco:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel, "4, 4, right, default");
		
		campoCodiceUnivoco = new JTextField();
		frmGestionaleFatture.getContentPane().add(campoCodiceUnivoco, "6, 4, fill, default");
		campoCodiceUnivoco.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Nome azienda: ");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_1, "4, 6, left, default");
		
		campoNomeAzienda = new JTextField();
		campoNomeAzienda.setText("");
		frmGestionaleFatture.getContentPane().add(campoNomeAzienda, "6, 6, fill, default");
		campoNomeAzienda.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("Partita Iva:");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_6, "4, 8, left, default");
		
		campoPartitaIva = new JTextField();
		frmGestionaleFatture.getContentPane().add(campoPartitaIva, "6, 8, fill, default");
		campoPartitaIva.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Data emissione:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_2, "4, 10, left, default");
		
		campoDataEmissione = new JTextField();
		campoDataEmissione.setText("dd/MM/yyyy");
		frmGestionaleFatture.getContentPane().add(campoDataEmissione, "6, 10, fill, default");
		campoDataEmissione.setColumns(10);
		campoDataEmissione.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(campoDataEmissione.getText().equals("")) campoDataEmissione.setText("dd/MM/yyyy");

				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(campoDataEmissione.getText().equals("dd/MM/yyyy")) campoDataEmissione.setText("");
			}
		});
		
		JLabel lblNewLabel_3 = new JLabel("Data scadenza:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_3, "4, 12, left, default");
		
		campoDataScadenza = new JTextField();
		campoDataScadenza.setText("dd/MM/yyyy");
		frmGestionaleFatture.getContentPane().add(campoDataScadenza, "6, 12, fill, default");
		campoDataScadenza.setColumns(10);
		campoDataScadenza.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(campoDataScadenza.getText().equals("")) campoDataScadenza.setText("dd/MM/yyyy");
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(campoDataScadenza.getText().equals("dd/MM/yyyy")) campoDataScadenza.setText("");
				
			}
		});
		
		
		JLabel lblNewLabel_4 = new JLabel("Importo:");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_4, "4, 14, left, default");
		
		JButton bottoneInserisciFattura = new JButton("Aggiungi");
		bottoneInserisciFattura.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Fattura fattura=new Fattura(campoPartitaIva.getText(),campoNomeAzienda.getText(),campoCodiceUnivoco.getText(), 
							LocalDate.parse(campoDataEmissione.getText(),DateTimeFormatter.ofPattern("dd/MM/yyyy")),
							LocalDate.parse(campoDataScadenza.getText(),DateTimeFormatter.ofPattern("dd/MM/yyyy")),
							Money.parse("EUR "+campoImporto.getText()),areaNote.getText());
					appModel.inserisciFattura(fattura);
					appModel.refetch();
					campoCodiceUnivoco.setText("");
					campoDataEmissione.setText("");
					campoDataScadenza.setText("");
					campoNomeAzienda.setText("");
					campoPartitaIva.setText("");
					campoImporto.setText("");
					areaNote.setText("");
				} catch (MalformedDataException | SQLException e1) {
					new LogView(e1);
					e1.printStackTrace();
				}
			}
		});
		
		campoImporto = new JTextField();
		frmGestionaleFatture.getContentPane().add(campoImporto, "6, 14, fill, default");
		campoImporto.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Note:");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_5, "4, 16");
		
		areaNote = new JTextArea();
		areaNote.setLineWrap(true);
		areaNote.setText("");
		frmGestionaleFatture.getContentPane().add(areaNote, "6, 16, 3, 1, fill, fill");
		bottoneInserisciFattura.setFont(new Font("Tahoma", Font.ITALIC, 15));
		frmGestionaleFatture.getContentPane().add(bottoneInserisciFattura, "4, 18");
		frmGestionaleFatture.setVisible(true);
	}
	
	

}
