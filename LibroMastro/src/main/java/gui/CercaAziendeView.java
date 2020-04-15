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
import javax.swing.JPanel;

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
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

public class CercaAziendeView implements Observer{

	private JFrame frmGestionaleFatture;
	private JTable tabellaAziende;
	private AppModel appModel;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JButton btnNewButton;
	private DatePicker dataInizioPicker;
	private DatePicker dataFinePicker;
	

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
		frmGestionaleFatture.setBounds(100, 100, 533, 610);
		frmGestionaleFatture.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		JPanel panel=new JPanel();
		JScrollPane scrollPaneMain=new JScrollPane(panel);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(88dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(174dlu;default)"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:max(220dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(8dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(8dlu;default)"),}));
		frmGestionaleFatture.getContentPane().add(scrollPaneMain);
		
		tabellaAziende=new JTable();
		tabellaAziende.setModel(new TabellaAziendeModel(appModel.getAziende()));
		JScrollPane scrollPane = new JScrollPane(tabellaAziende);
		panel.add(scrollPane, "2, 2, 3, 1, fill, fill");
		
		lblNewLabel = new JLabel("Data inizio:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel, "2, 4, left, default");
		
		DatePickerSettings settings1=new DatePickerSettings();
		settings1.setAllowEmptyDates(false);
		settings1.setAllowKeyboardEditing(false);
		dataInizioPicker = new DatePicker(settings1);
		panel.add(dataInizioPicker, "4, 4, fill, fill");
		
		lblNewLabel_1 = new JLabel("Data fine:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel_1, "2, 6, left, default");
		
		btnNewButton = new JButton("Genera estratto Conto");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					String nomeAzienda=(String)tabellaAziende.getValueAt(tabellaAziende.getSelectedRow(),0);
					String partitaIva=(String)tabellaAziende.getValueAt(tabellaAziende.getSelectedRow(),1);
					appModel.scriviEstrattoConto(nomeAzienda, partitaIva,
							dataInizioPicker.getDate(),
							dataFinePicker.getDate());
					dataFinePicker.clear();
					dataInizioPicker.clear();
				} catch (Exception e) {
					new LogView(e);
					e.printStackTrace();
				}
			}
		});
		
		DatePickerSettings settings2=new DatePickerSettings();
		settings2.setAllowEmptyDates(false);
		settings2.setAllowKeyboardEditing(false);
		dataFinePicker = new DatePicker(settings2);
		panel.add(dataFinePicker, "4, 6, fill, fill");
		btnNewButton.setFont(new Font("Tahoma", Font.ITALIC, 15));
		panel.add(btnNewButton, "2, 8");
	}

	@Override
	public void notificaCambiamento() {
		frmGestionaleFatture.setVisible(true);
		TabellaAziendeModel tam=(TabellaAziendeModel)tabellaAziende.getModel();
		tam.fireTableDataChanged();
		
	}

}
