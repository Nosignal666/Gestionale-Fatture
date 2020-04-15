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
		frmGestionalefatture.setBounds(100, 100, 529, 369);
		frmGestionalefatture.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGestionalefatture.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 20));
		lblNewLabel.setBounds(49, 78, 108, 25);
		frmGestionalefatture.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 20));
		lblNewLabel_1.setBounds(49, 129, 108, 16);
		frmGestionalefatture.getContentPane().add(lblNewLabel_1);
		
		campoUsername = new JTextField();
		campoUsername.setFont(new Font("Arial", Font.PLAIN, 19));
		campoUsername.setBounds(184, 80, 194, 25);
		frmGestionalefatture.getContentPane().add(campoUsername);
		campoUsername.setColumns(10);
		
		JButton bottoneLogin = new JButton("Autenticati");
		bottoneLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					appModel.initStorage("postgres","academy2020","jdbc:postgresql://localhost:5432/Fatture");
					new MainView(appModel);
					frmGestionalefatture.setVisible(false);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
		});
		bottoneLogin.setFocusable(false);
		bottoneLogin.setFont(new Font("Arial", Font.ITALIC, 20));
		bottoneLogin.setBounds(154, 183, 150, 45);
		frmGestionalefatture.getContentPane().add(bottoneLogin);
		
		campoPassword = new JPasswordField();
		campoPassword.setFont(new Font("Arial", Font.PLAIN, 19));
		campoPassword.setBounds(184, 127, 194, 25);
		frmGestionalefatture.getContentPane().add(campoPassword);
		
		JLabel lblNewLabel_2 = new JLabel("Indirizzo DB:");
		lblNewLabel_2.setFont(new Font("Arial", Font.BOLD, 20));
		lblNewLabel_2.setBounds(49, 41, 188, 16);
		frmGestionalefatture.getContentPane().add(lblNewLabel_2);
		
		campoIndirizzo = new JTextField();
		campoIndirizzo.setFont(new Font("Arial", Font.PLAIN, 19));
		campoIndirizzo.setBounds(184, 39, 194, 25);
		frmGestionalefatture.getContentPane().add(campoIndirizzo);
		campoIndirizzo.setColumns(10);
		
		frmGestionalefatture.setVisible(true);
	}

	@Override
	public void notificaCambiamento() {
		// TODO Auto-generated method stub
		
	}
}
