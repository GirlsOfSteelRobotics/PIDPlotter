/**
 * PID Plotter
 * 
 * An application to help tune PID parameters for a FIRST FRC robot.
 */

package org.usfirst.frc.team3504;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PIDPlotter {
	private enum ConnStatus {
		Disconnected, Connecting, Connected
	}

	private JFrame frame;
	private JTextField teamNum;
	private JTextField txtStatus;
	private JFreeChart chart;
	private JFileChooser fileChooser;
	private XYSeriesCollection dataset;
	private ConnStatus status;

	// Labels for the two series of plotted data
	private final static String velocityKey = "Velocity";
	private final static String positionKey = "Position";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					PIDPlotter plotter = new PIDPlotter();
					plotter.frame.setVisible(true);

					ActionListener listener = new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							plotter.dataset.getSeries(velocityKey).add(0.9, 0.0);
							plotter.dataset.getSeries(positionKey).add(0.9, 1.7);
						}
					};
					Timer tim = new Timer(5000, listener);
					tim.setRepeats(false);
					tim.start();

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
		initializeUI();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeUI() {
		/*
		 * Create a file chooser used for saving or opening files. The location
		 * defaults to the platform-specific documents directory. Re-using this
		 * same instance for all dialogs causes it to remember the last
		 * directory selected.
		 */
		fileChooser = new JFileChooser();
		// setPanelFont(fileChooser.getComponents(), font);

		/*
		 * Set the initial connection status
		 */
		status = ConnStatus.Disconnected;

		/*
		 * Create a worker for handling the potentially time-consuming
		 * connection process
		 */
		SwingWorker<String, Void> connector = new SwingWorker<String, Void>() {
			@Override
			public String doInBackground() {
				boolean exists = false;
				while (!exists) {
						File file = new File("/tmp/exists");
						exists = file.exists();
				}
				return "Connected to roborio-3504-frc.local";
			}

			@Override
			public void done() {
				String msg = null;
				try {
					msg = get(10, TimeUnit.MILLISECONDS);
					status = ConnStatus.Connected;
				} catch (InterruptedException ignore) {
				} catch (TimeoutException ignore) {
				} catch (java.util.concurrent.ExecutionException e) {
					Throwable cause = e.getCause();
					msg = "Failed to connect: " + ((cause != null) ? cause.getMessage() : e.getMessage());
					status = ConnStatus.Disconnected;
					//btnConnect.setEnabled(true);
				}
				txtStatus.setText(msg);
			}
		};

		/*
		 * Create the main application window. This code was mostly
		 * auto-generated using the WindowBuilder plug-in for Eclipse, installed
		 * by adding this site in the Help > Install New Software dialog:
		 * http://download.eclipse.org/windowbuilder/WB/integration/4.7/
		 */
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
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnConnect.setEnabled(false);
				status = ConnStatus.Connecting;
				txtStatus.setText("Connecting...");
				connector.execute();
			}
		});
		topPanel.add(btnConnect);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		topPanel.add(horizontalStrut);

		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setForeground(Color.DARK_GRAY);
		topPanel.add(lblStatus);

		txtStatus = new JTextField();
		txtStatus.setDisabledTextColor(Color.DARK_GRAY);
		txtStatus.setEnabled(false);
		txtStatus.setEditable(false);
		txtStatus.setBackground(UIManager.getColor("Panel.background"));
		txtStatus.setBorder(BorderFactory.createEmptyBorder());
		txtStatus.setText("Disconnected");
		txtStatus.setColumns(20);
		topPanel.add(txtStatus);

		JPanel bottomPanel = new JPanel();
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new SaveButtonHandler());
		bottomPanel.add(btnSave);

		JButton btnExport = new JButton("Export PNG");
		btnExport.addActionListener(new ExportButtonHandler());
		bottomPanel.add(btnExport);

		JButton btnOpen = new JButton("Open");
		bottomPanel.add(btnOpen);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		dataset = createDataset();
		tabbedPane.add("New", new ChartPanel(createChart(dataset)));
		tabbedPane.setTabComponentAt(0, new ButtonTabComponent(tabbedPane));
	}

	/**
	 * Creates an XY plot with all of the labels and legends
	 *
	 * @return The chart.
	 */
	private JFreeChart createChart(XYDataset dataset) {
		// Create the chart, saving a reference in the class variable
		chart = ChartFactory.createXYLineChart("Velocity/Position over Time", // chart
				// title
				velocityKey, // domain axis label
				"Time", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips?
				false // URL generator? Not required...
		);

		return chart;
	}

	/**
	 * Creates a sample dataset.
	 *
	 * @return The dataset.
	 */
	private XYSeriesCollection createDataset() {
		// Plot several series of data on the same chart
		XYSeries vel = new XYSeries(velocityKey);
		vel.add(0.0, 0.0);
		vel.add(0.1, 0.3);
		vel.add(0.2, 0.5);
		vel.add(0.3, 0.7);
		vel.add(0.4, 0.7);
		vel.add(0.5, 0.7);
		vel.add(0.6, 0.5);
		vel.add(0.7, 0.3);
		vel.add(0.8, 0.2);

		XYSeries pos = new XYSeries(positionKey);
		pos.add(0.0, 0.0);
		pos.add(0.1, 0.1);
		pos.add(0.2, 0.3);
		pos.add(0.3, 0.6);
		pos.add(0.4, 0.9);
		pos.add(0.5, 1.2);
		pos.add(0.6, 1.4);
		pos.add(0.7, 1.5);
		pos.add(0.8, 1.6);

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(vel);
		dataset.addSeries(pos);

		return dataset;
	}

	/*
	 * Export a chart as a PNG image to a location and filename selected with a
	 * save dialog. Remembers the selected location for any future exports.
	 */
	private class ExportButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int returnVal = fileChooser.showSaveDialog(fileChooser);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File path = fileChooser.getSelectedFile();
				int width = 640;
				int height = 480;

				try {
					ChartUtilities.saveChartAsPNG(path, chart, width, height);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/*
	 * Save a chart as a data file to a location and filename selected with a
	 * save dialog. Remembers the selected location for any future exports.
	 */
	private class SaveButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int returnVal = fileChooser.showSaveDialog(fileChooser);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File path = fileChooser.getSelectedFile();
				int width = 640;
				int height = 480;

				try {
					ChartUtilities.saveChartAsJPEG(path, chart, width, height);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
