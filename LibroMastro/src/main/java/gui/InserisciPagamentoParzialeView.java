package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import strutturaDati.Fattura;
import strutturaDati.MalformedDataException;
import strutturaDati.PagamentoParziale;
import strutturaDati.TipoPagamento;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JTable;

import java.awt.Font;
import javax.swing.JTextField;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import java.awt.Color;

public class InserisciPagamentoParzialeView {
	
	private String codiceUnivoco;
	private String partitaIva;
	private AppModel appModel;
	private JFrame frmGestionaleFattura;
	private JTextField campoNrFattura;
	private JTextField campoImporto;
	private JLabel lblNewLabel_2;
	private JTextField campoDataPagamento;
	private JLabel lblNewLabel_3;
	private JButton bottoneInserisci;
	private JComboBox selectorTipoPagamento;
	private Boolean goOn=false;
	private JTextArea alertArea;

	
	/**
	 * Create the application.
	 */
	public InserisciPagamentoParzialeView(AppModel appModel,String codiceUnivoco,String partitaiva){
		this.appModel=appModel;
		this.codiceUnivoco=codiceUnivoco;
		this.partitaIva=partitaiva;
		initialize();
	}
	
	private void initialize() {
		frmGestionaleFattura = new JFrame();
		frmGestionaleFattura.setTitle("Gestionale Fattura - Nuovo PP");
		frmGestionaleFattura.setBounds(100, 100, 501, 278);
		frmGestionaleFattura.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmGestionaleFattura.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
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
				RowSpec.decode("max(17dlu;default)"),}));
		
		JLabel lblNewLabel = new JLabel("Nuovo pagamento parziale per fattura ");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFattura.getContentPane().add(lblNewLabel, "4, 4, left, default");
		
		campoNrFattura = new JTextField();
		campoNrFattura.setEditable(false);
		frmGestionaleFattura.getContentPane().add(campoNrFattura, "6, 4, fill, default");
		campoNrFattura.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Importo:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFattura.getContentPane().add(lblNewLabel_1, "4, 6, left, default");
		
		campoImporto = new JTextField();
		campoImporto.setEditable(true);
		campoImporto.setText("");
		frmGestionaleFattura.getContentPane().add(campoImporto, "6, 6, fill, default");
		campoImporto.setColumns(10);
		
		lblNewLabel_2 = new JLabel("Data Pagamento:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFattura.getContentPane().add(lblNewLabel_2, "4, 8, left, default");
		
		campoDataPagamento = new JTextField();
		campoDataPagamento.setText("dd/MM/yyyy");
		frmGestionaleFattura.getContentPane().add(campoDataPagamento, "6, 8, fill, default");
		campoDataPagamento.setColumns(10);
		campoDataPagamento.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(campoDataPagamento.getText().equals("")) campoDataPagamento.setText("dd/MM/yyyy");
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(campoDataPagamento.getText().equals("dd/MM/yyyy")) campoDataPagamento.setText("");
				
			}
		});
		
		
		lblNewLabel_3 = new JLabel("Tipo pagamento:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmGestionaleFattura.getContentPane().add(lblNewLabel_3, "4, 10, left, default");
		
		
		bottoneInserisci = new JButton("Inserisci");
		bottoneInserisci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					LocalDate dataPagamento=LocalDate.parse(campoDataPagamento.getText(),DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					Money importo=Money.parse("EUR"+ campoImporto.getText());
					if(!goOn & appModel.checkifAlreadyExist(dataPagamento, importo)) {
						goOn=true;
						alertArea.setText("Un pagamento parziale con stessa data e stesso importo è già presente, per confermare premi di nuovo");
						return;
					}
					PagamentoParziale pp=new PagamentoParziale(codiceUnivoco,importo,
							dataPagamento,
							TipoPagamento.valueOf(selectorTipoPagamento.getSelectedItem().toString()),partitaIva);
					appModel.inserisciPagamentoParziale(pp);
					campoDataPagamento.setText("");
					campoImporto.setText("");
					alertArea.setText("");
					appModel.refetch();
					
				} catch (Exception e1) {
					System.out.println("canedio");
					new LogView(e1);
					e1.printStackTrace();
				}
				
			}
		});
		
		
		selectorTipoPagamento = new JComboBox();
		selectorTipoPagamento.setModel(new DefaultComboBoxModel(new String[] {"Bonifico", "Contanti"}));
		frmGestionaleFattura.getContentPane().add(selectorTipoPagamento, "6, 10, fill, default");
		bottoneInserisci.setFont(new Font("Tahoma", Font.ITALIC, 15));
		bottoneInserisci.setFocusable(false);
		frmGestionaleFattura.getContentPane().add(bottoneInserisci, "4, 12");
		
		alertArea = new JTextArea();
		alertArea.setLineWrap(true);
		alertArea.setForeground(Color.RED);
		alertArea.setBackground(frmGestionaleFattura.getBackground());
		frmGestionaleFattura.getContentPane().add(alertArea, "4, 14, fill, fill");
		
		
		fillview();
		frmGestionaleFattura.setVisible(true);
	}
	
	public void fillview() {
		campoNrFattura.setText(codiceUnivoco);
	}	
}
