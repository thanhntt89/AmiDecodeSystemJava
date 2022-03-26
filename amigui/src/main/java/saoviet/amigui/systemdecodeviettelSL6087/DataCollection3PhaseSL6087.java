package saoviet.amigui.systemdecodeviettelSL6087;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import saoviet.amisystem.connectionmanagement.ConnectionViettelDcuManagement;
import saoviet.amisystem.connectionmanagement.IConnectionManagement;
import saoviet.amisystem.event.ConnectionStatusEventCallback;
import saoviet.amisystem.sqlhelper.DatabaseConnection;
import java.awt.Font;
import java.awt.Toolkit;

public class DataCollection3PhaseSL6087 {

	private IConnectionManagement iactive = null;
	JButton btnConnect = new JButton("START");
	JLabel lblConnectioInfo = new JLabel("Message info");
	JButton btnDisconnect = new JButton("STOP");
	JButton btnClose = new JButton("EXIT");
	JTextArea txtContent = new JTextArea();

	public JFrame frmDataCollection;

	/**
	 * Create the application.
	 */
	public DataCollection3PhaseSL6087() {
		LoadForm();
		initialize();
	}

	public void LoadForm() {
		this.iactive = new ConnectionViettelDcuManagement ();
		this.iactive.setConnectionStatusEventCallback(new ConnectionStatusEventCallback() {
			public void ConnectionStatus(boolean status, String message) {
				// TODO Auto-generated method stub
				txtContent.setText(message);
				btnDisconnect.setEnabled(status);
				btnConnect.setEnabled(!status);
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDataCollection = new JFrame();
		frmDataCollection
				.setIconImage(Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + "\\image\\3.gif"));
		frmDataCollection.setTitle("DATA COLLECTION SL6087 DECODE");
		frmDataCollection.setResizable(false);
		frmDataCollection.setBounds(100, 100, 320, 248);
		frmDataCollection.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmDataCollection.getContentPane().setLayout(null);
		frmDataCollection.setLocationRelativeTo(null);
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnConnect.setToolTipText("Start system");
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnDisconnect.setEnabled(false);

		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (iactive != null) {
					txtContent.setText("Creating sql connection pool...");
					if (DatabaseConnection.CheckSqlConnectionPool())
						iactive.connect();
					else
						txtContent.setText("Can't create sql connection pool.");
				}
			}
		});

		btnConnect.setBounds(10, 7, 104, 49);
		frmDataCollection.getContentPane().add(btnConnect);
		lblConnectioInfo.setBounds(10, 58, 89, 14);
		frmDataCollection.getContentPane().add(lblConnectioInfo);
		txtContent.setEditable(false);
		txtContent.setBounds(10, 83, 294, 96);
		frmDataCollection.getContentPane().add(txtContent);
		btnDisconnect.setToolTipText("Stop system");

		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (iactive != null) {
					iactive.disconnect();
				}
			}
		});

		btnDisconnect.setSelectedIcon(new ImageIcon(DataCollection3PhaseSL6087.class.getResource("/junit/runner/logo.gif")));
		btnDisconnect.setBounds(200, 7, 104, 49);
		frmDataCollection.getContentPane().add(btnDisconnect);
		btnClose.setToolTipText("Close application");

		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnDisconnect.isEnabled()) {
					txtContent.setText("You have to press STOP system before closing ...");
					return;
				} else if (iactive != null || iactive.existThreadDecode()) {
					System.exit(0);
				} else
					txtContent.setText("Data is dequeuing.");

				DatabaseConnection.closeAllConnection();
			}
		});

		btnClose.setBounds(238, 190, 66, 25);
		frmDataCollection.getContentPane().add(btnClose);
	}
}
