package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Color;

public class LogView {

	private JFrame frmGestionaleFatture;
	JTextArea logArea;
	Exception e;
	/**
	 * Create the application.
	 */
	public LogView(Exception e) {
		this.e=e;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGestionaleFatture = new JFrame();
		frmGestionaleFatture.setTitle("Gestionale Fatture - Log");
		frmGestionaleFatture.setBounds(100, 100, 457, 149);
		frmGestionaleFatture.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmGestionaleFatture.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(37dlu;pref)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(199dlu;pref)"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(35dlu;default)"),}));
		
		logArea = new JTextArea();
		logArea.setLineWrap(true);
		logArea.setForeground(Color.RED);
		frmGestionaleFatture.getContentPane().add(logArea, "2, 2, 3, 3, fill, fill");
		logArea.setBackground(frmGestionaleFatture.getBackground());
		
		logArea.append(e.getClass().toString()+": "+e.getMessage());
		frmGestionaleFatture.setVisible(true);
	}

}
