package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import strutturaDati.Azienda;
import strutturaDati.MalformedDataException;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class InserisciAziendaView {

	private JFrame frmGestionaleFatture;
	private AppModel appModel;
	private JTextField campoNomeAzienda;
	private JTextField campoPartitaIva;

	/**
	 * Create the application.
	 */
	public InserisciAziendaView(AppModel appModel) {
		this.appModel=appModel;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGestionaleFatture = new JFrame();
		frmGestionaleFatture.setTitle("Gestionale Fatture - Aggiungi azienda");
		frmGestionaleFatture.setBounds(100, 100, 420, 182);
		frmGestionaleFatture.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmGestionaleFatture.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(87dlu;default)"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel("Nome azienda:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel, "4, 4, right, default");
		
		campoNomeAzienda = new JTextField();
		frmGestionaleFatture.getContentPane().add(campoNomeAzienda, "6, 4, fill, default");
		campoNomeAzienda.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Partita Iva:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFatture.getContentPane().add(lblNewLabel_1, "4, 6, left, default");
		
		campoPartitaIva = new JTextField();
		frmGestionaleFatture.getContentPane().add(campoPartitaIva, "6, 6, fill, default");
		campoPartitaIva.setColumns(10);
		
		JButton bottoneInserisci = new JButton("Aggiungi");
		bottoneInserisci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Azienda azienda=new Azienda(campoNomeAzienda.getText(),campoPartitaIva.getText());
					appModel.inserisciAzienda(azienda);
					appModel.refetch();
					campoNomeAzienda.setText("");
					campoPartitaIva.setText("");
				} catch (MalformedDataException | SQLException e1) {
					new LogView(e1);
					e1.printStackTrace();
				}
			}
		});
		bottoneInserisci.setFont(new Font("Tahoma", Font.ITALIC, 15));
		frmGestionaleFatture.getContentPane().add(bottoneInserisci, "4, 8");
		
		frmGestionaleFatture.setVisible(true);
	}

}
