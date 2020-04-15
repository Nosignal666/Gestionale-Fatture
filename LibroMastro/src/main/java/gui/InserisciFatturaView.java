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
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

public class InserisciFatturaView {

	private JFrame frmGestionaleFatture;
	private AppModel appModel;
	private JTextField campoCodiceUnivoco;
	private JTextField campoNomeAzienda;
	private JTextField campoImporto;
	private JTextField campoPartitaIva;
	JTextArea areaNote;
	private DatePicker dataEmissionePicker;
	private DatePicker dataScadenzaPicker;

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
		frmGestionaleFatture.setBounds(100, 100, 390, 528);
		frmGestionaleFatture.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panel=new JPanel();
		JScrollPane scrollPane=new JScrollPane(panel);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(8dlu;default)"),
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(122dlu;default)"),
				ColumnSpec.decode("max(6dlu;default)"),},
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
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(115dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:max(19dlu;default)"),
				RowSpec.decode("top:max(8dlu;default)"),}));
		frmGestionaleFatture.getContentPane().add(scrollPane);
		
		
		JLabel lblNewLabel = new JLabel("Codice univoco:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel, "2, 4, right, default");
		
		campoCodiceUnivoco = new JTextField();
		panel.add(campoCodiceUnivoco, "4, 4, fill, default");
		campoCodiceUnivoco.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Nome azienda: ");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_1, "2, 6, left, default");
		
		campoNomeAzienda = new JTextField();
		campoNomeAzienda.setText("");
		panel.add(campoNomeAzienda, "4, 6, fill, default");
		campoNomeAzienda.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("Partita Iva:");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_6, "2, 8, left, default");
		
		campoPartitaIva = new JTextField();
		panel.add(campoPartitaIva, "4, 8, fill, default");
		campoPartitaIva.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Data emissione:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_2, "2, 10, left, default");
		
		DatePickerSettings settings1=new DatePickerSettings();
		settings1.setAllowEmptyDates(false);
		settings1.setAllowKeyboardEditing(false);
		dataEmissionePicker = new DatePicker(settings1);
		panel.add(dataEmissionePicker, "4, 10, fill, fill");
		
		JLabel lblNewLabel_3 = new JLabel("Data scadenza:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_3, "2, 12, left, default");
		
		DatePickerSettings settings2=new DatePickerSettings();
		settings2.setAllowEmptyDates(false);
		settings2.setAllowKeyboardEditing(false);
		dataScadenzaPicker = new DatePicker(settings2);
		panel.add(dataScadenzaPicker, "4, 12, fill, fill");
		
		
		
		JLabel lblNewLabel_4 = new JLabel("Importo:");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_4, "2, 14, left, default");
		
		JButton bottoneInserisciFattura = new JButton("Aggiungi");
		bottoneInserisciFattura.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Fattura fattura=new Fattura(campoPartitaIva.getText(),campoNomeAzienda.getText(),campoCodiceUnivoco.getText(), 
							dataEmissionePicker.getDate(),
							dataScadenzaPicker.getDate(),
							Money.parse("EUR "+campoImporto.getText()),areaNote.getText());
					appModel.inserisciFattura(fattura);
					appModel.refetch();
					campoCodiceUnivoco.setText("");
					dataEmissionePicker.clear();
					dataScadenzaPicker.clear();
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
		panel.add(campoImporto, "4, 14, fill, default");
		campoImporto.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Note:");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_5, "2, 16");
		
		areaNote = new JTextArea();
		areaNote.setLineWrap(true);
		areaNote.setText("");
		panel.add(areaNote, "4, 16, fill, fill");
		bottoneInserisciFattura.setFont(new Font("Tahoma", Font.ITALIC, 15));
		panel.add(bottoneInserisciFattura, "2, 18");
		frmGestionaleFatture.setVisible(true);
	}
	
	

}
