package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import storage.DataBaseStorageManager;

import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;

public class LoginView implements Observer{
	
	private AppModel appModel;
	private JFrame frmGestionalefatture;
	private JTextField campoUsername;
	private JPasswordField campoPassword;
	private JTextField campoIndirizzo;

	public LoginView(AppModel appModel) {
		this.appModel=appModel;
		appModel.aggiungiObserver(this);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGestionalefatture = new JFrame();
		frmGestionalefatture.setTitle("Gestionale Fatture - Pannello autenticazione");
		frmGestionalefatture.setBounds(100, 100, 504, 313);
		frmGestionalefatture.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGestionalefatture.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("49px"),
				ColumnSpec.decode("124px"),
				ColumnSpec.decode("27px"),
				ColumnSpec.decode("212px"),},
			new RowSpec[] {
				RowSpec.decode("39px"),
				RowSpec.decode("25px"),
				FormSpecs.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("27px"),
				RowSpec.decode("22px"),
				RowSpec.decode("25px"),
				RowSpec.decode("31px"),
				RowSpec.decode("45px"),}));
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 20));
		frmGestionalefatture.getContentPane().add(lblNewLabel, "2, 4, fill, top");
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 20));
		frmGestionalefatture.getContentPane().add(lblNewLabel_1, "2, 6, fill, top");
		
		campoUsername = new JTextField();
		campoUsername.setFont(new Font("Arial", Font.PLAIN, 15));
		frmGestionalefatture.getContentPane().add(campoUsername, "4, 4, fill, fill");
		campoUsername.setColumns(10);
		
		campoPassword = new JPasswordField();
		campoPassword.setFont(new Font("Arial", Font.PLAIN, 15));
		frmGestionalefatture.getContentPane().add(campoPassword, "4, 6, fill, fill");
		
		JLabel lblNewLabel_2 = new JLabel("Indirizzo DB:");
		lblNewLabel_2.setFont(new Font("Arial", Font.BOLD, 20));
		frmGestionalefatture.getContentPane().add(lblNewLabel_2, "2, 2, left, top");
		
		campoIndirizzo = new JTextField();
		campoIndirizzo.setFont(new Font("Arial", Font.PLAIN, 15));
		frmGestionalefatture.getContentPane().add(campoIndirizzo, "4, 2, fill, fill");
		campoIndirizzo.setColumns(10);
		campoIndirizzo.setText("localhost:5432/Fatture");
		
		JButton bottoneLogin = new JButton("Autenticati");
		bottoneLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String user=campoUsername.getText();
					char[] passwordArray=campoPassword.getPassword();
					String password=String.copyValueOf(passwordArray);
					String indirizzo="jdbc:postgresql://"+campoIndirizzo.getText();
				
					appModel.initStorage(user,password,indirizzo);
					new MainView(appModel);
					frmGestionalefatture.setVisible(false);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
					new LogView(e);
				}
			}
		});
		bottoneLogin.setFocusable(false);
		bottoneLogin.setFont(new Font("Arial", Font.ITALIC, 20));
		frmGestionalefatture.getContentPane().add(bottoneLogin, "4, 8, default, fill");
		
		frmGestionalefatture.setVisible(true);
	}

	@Override
	public void notificaCambiamento() {
		// TODO Auto-generated method stub
		
	}
}
