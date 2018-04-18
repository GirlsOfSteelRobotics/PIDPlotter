/**
 * PID Plotter
 * 
 * An application to help tune PID parameters for a FIRST FRC robot.
 * 
 * To edit the interface using the Eclipse WindowBuilder plug-in, 
 * add this site in the Help > Install New Software dialog:
 * http://download.eclipse.org/windowbuilder/WB/integration/4.7/
 */

package org.usfirst.frc.team3504;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;

public class PIDPlotter {

	private JFrame frame;
	private JTextField teamNum;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PIDPlotter window = new PIDPlotter();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PIDPlotter() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 850, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel topPanel = new JPanel();
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);

		JLabel lblTeam = new JLabel("Team #");
		topPanel.add(lblTeam);

		teamNum = new JTextField();
		lblTeam.setLabelFor(teamNum);
		topPanel.add(teamNum);
		teamNum.setColumns(5);

		JButton btnConnect = new JButton("Connect");
		topPanel.add(btnConnect);

		JPanel bottomPanel = new JPanel();
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		JButton btnSave = new JButton("Save");
		bottomPanel.add(btnSave);
		
		JButton btnExportPng = new JButton("Export PNG");
		bottomPanel.add(btnExportPng);
		
		JButton btnOpen = new JButton("Open");
		bottomPanel.add(btnOpen);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		for (int i = 0; i < 5; i++) {
			String tabTitle = "New " + i;
			tabbedPane.add(tabTitle, new JLabel(tabTitle));
			tabbedPane.setTabComponentAt(i, new ButtonTabComponent(tabbedPane));
		}
	}

}
