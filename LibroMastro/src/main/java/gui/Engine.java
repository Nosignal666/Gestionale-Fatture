package gui;

import java.awt.EventQueue;

public class Engine {


	public static void main(String[] args) {
		
		AppModel appModel=new AppModel();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView loginView = new LoginView(appModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

}

