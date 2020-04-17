package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.joda.money.Money;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import strutturaDati.Fattura;

public class ModificaFatturaView {

	private JFrame frmGestionaleFatture;
	private AppModel appModel;
	private JTextField campoCodiceUnivoco;
	private JTextField campoNomeAzienda;
	private JTextField campoImporto;
	private JTextField campoPartitaIva;
	JTextArea areaNote;
	private DatePicker dataEmissionePicker;
	private DatePicker dataScadenzaPicker;
	JFileChooser fc=null;
	JTextArea areaFeedbackFile;
	JTextArea alertArea;
	private Boolean isApproved=false;
	Fattura fatturaOriginale;

	/**
	 * Create the application.
	 */
	public ModificaFatturaView(AppModel appModel,Fattura fatturaOriginale) {
		this.appModel=appModel;
		this.fatturaOriginale=fatturaOriginale;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frmGestionaleFatture = new JFrame();
		frmGestionaleFatture.setTitle("Gestionale Fatture - Aggiungi fattura");
		frmGestionaleFatture.setBounds(100, 100, 420, 544);
		frmGestionaleFatture.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panel=new JPanel();
		JScrollPane scrollPane=new JScrollPane(panel);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(8dlu;default)"),
				ColumnSpec.decode("max(59dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(27dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(29dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(14dlu;default)"),},
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
				RowSpec.decode("top:max(19dlu;default):grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:max(19dlu;default):grow"),
				RowSpec.decode("top:max(8dlu;default)"),}));
		frmGestionaleFatture.getContentPane().add(scrollPane);
		
		
		JLabel lblNewLabel = new JLabel("Codice univoco:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel, "2, 4, right, default");
		
		campoCodiceUnivoco = new JTextField();
		panel.add(campoCodiceUnivoco, "4, 4, 3, 1, fill, default");
		campoCodiceUnivoco.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Nome azienda: ");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_1, "2, 6, left, default");
		
		campoNomeAzienda = new JTextField();
		campoNomeAzienda.setText("");
		panel.add(campoNomeAzienda, "4, 6, 3, 1, fill, default");
		campoNomeAzienda.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("Partita Iva:");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_6, "2, 8, left, default");
		
		campoPartitaIva = new JTextField();
		panel.add(campoPartitaIva, "4, 8, 3, 1, fill, default");
		campoPartitaIva.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Data emissione:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_2, "2, 10, left, default");
		
		DatePickerSettings settings1=new DatePickerSettings();
		settings1.setAllowEmptyDates(false);
		settings1.setAllowKeyboardEditing(false);
		dataEmissionePicker = new DatePicker(settings1);
		panel.add(dataEmissionePicker, "4, 10, 3, 1, fill, fill");
		
		JLabel lblNewLabel_3 = new JLabel("Data scadenza:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_3, "2, 12, left, default");
		
		DatePickerSettings settings2=new DatePickerSettings();
		settings2.setAllowEmptyDates(false);
		settings2.setAllowKeyboardEditing(false);
		dataScadenzaPicker = new DatePicker(settings2);
		panel.add(dataScadenzaPicker, "4, 12, 3, 1, fill, fill");
		
		
		
		JLabel lblNewLabel_4 = new JLabel("Importo:");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_4, "2, 14, left, default");
		
		JButton bottoneInserisciFattura = new JButton("Modifica");
		bottoneInserisciFattura.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Fattura fatturaModificata=new Fattura(campoPartitaIva.getText(),campoNomeAzienda.getText(),campoCodiceUnivoco.getText(), 
							dataEmissionePicker.getDate(),
							dataScadenzaPicker.getDate(),
							Money.parse("EUR "+campoImporto.getText()),areaNote.getText());
					if(!isApproved & fc==null) {
						alertArea.setText("Attenzione! nessun file selezionato, cliccare di nuovo per confermare.");
						isApproved=true;
					}
					else {
						if(fc!=null) {
							fatturaModificata.setPdfFile(fc.getSelectedFile());
						}
						appModel.modificaFattura(fatturaOriginale,fatturaModificata);
						appModel.refetch();
						campoCodiceUnivoco.setText("");
						dataEmissionePicker.setDateToToday();
						dataScadenzaPicker.setDateToToday();
						campoNomeAzienda.setText("");
						campoPartitaIva.setText("");
						campoImporto.setText("");
						areaNote.setText("");
					}
				} catch (Exception e1) {
					new LogView(e1);
					e1.printStackTrace();
				}
			}
		});
		
		campoImporto = new JTextField();
		panel.add(campoImporto, "4, 14, 3, 1, fill, default");
		campoImporto.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Note:");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_5, "2, 16");
		
		areaNote = new JTextArea();
		areaNote.setLineWrap(true);
		areaNote.setText("");
		panel.add(areaNote, "4, 16, 3, 1, fill, fill");
		bottoneInserisciFattura.setFont(new Font("Tahoma", Font.ITALIC, 15));
		panel.add(bottoneInserisciFattura, "2, 18");
		
		JButton bottoneAggiungiPdf = new JButton("Seleziona file");
		bottoneAggiungiPdf.setFont(new Font("Tahoma", Font.ITALIC, 15));
		bottoneAggiungiPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				areaFeedbackFile.setText("");
				fc=new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setFileHidingEnabled(true);
				fc.setApproveButtonText("Seleziona");
				if(fc.showOpenDialog(panel)==JFileChooser.APPROVE_OPTION) areaFeedbackFile.setText("OK");
			}
		});
		panel.add(bottoneAggiungiPdf, "4, 18");
		
		areaFeedbackFile = new JTextArea();
		areaFeedbackFile.setLineWrap(true);
		areaFeedbackFile.setFont(new Font("Monospaced", Font.BOLD, 15));
		areaFeedbackFile.setForeground(Color.BLUE);
		areaFeedbackFile.setBackground(frmGestionaleFatture.getBackground());
		panel.add(areaFeedbackFile, "6, 18, left, fill");
		
		alertArea = new JTextArea();
		alertArea.setForeground(Color.RED);
		alertArea.setBackground(frmGestionaleFatture.getBackground());
		alertArea.setLineWrap(true);
		panel.add(alertArea, "2, 20, 7, 1, fill, fill");
		
		riempiCampi(fatturaOriginale);
		frmGestionaleFatture.setVisible(true);
		
	}
	
	public void riempiCampi(Fattura fattura) {
		campoCodiceUnivoco.setText(fattura.getCodiceUnivoco());
		campoNomeAzienda.setText(fattura.getNomeAzienda());
		campoPartitaIva.setText(fattura.getPartitaIva());
		campoImporto.setText(fattura.getImporto().getAmount().toString());
		dataEmissionePicker.setDate(fattura.getDataEmissione());
		dataScadenzaPicker.setDate(fattura.getDataScadenza());
		areaNote.setText(fattura.getNote());
	}

}
