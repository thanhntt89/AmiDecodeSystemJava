package saoviet.amigui.systemstatus;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextPane;

import saoviet.amisystem.connectionmanagement.IOpenwireManagement;
import saoviet.amisystem.connectionmanagement.OpenwireManagement;
import saoviet.amisystem.event.ConnectionStatusEventCallback;
import saoviet.amisystem.sqlhelper.DatabaseConnection;

import javax.swing.JLabel;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class SystemStatus {

	public JFrame frmSystemStatus = new JFrame();
	// Biến sử dụng
	private JButton btnStart = new JButton("");
	private JButton btnStop = new JButton("");
	private JButton btnExit = new JButton("");
	private JLabel lblMessageInfo = new JLabel("Message info");
	private IOpenwireManagement iOpenwireManagement = null;
	private JTextPane txtMessageInfo = new JTextPane();

	/**
	 * Create the application.
	 */
	public SystemStatus() {
		this.iOpenwireManagement = new OpenwireManagement();
		this.iOpenwireManagement.setConnectionStatusEventCallback(new ConnectionStatusEventCallback() {

			public void ConnectionStatus(boolean status, String message) {
				// TODO Auto-generated method stub
				btnStart.setEnabled(!status);
				btnStop.setEnabled(status);
				txtMessageInfo.setText(message);
			}
		});
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmSystemStatus.setIconImage(
				Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") +"\\image\\updates.gif"));
		frmSystemStatus.setTitle("SYSTEM STATUS");
		frmSystemStatus.setResizable(false);
		frmSystemStatus.setBounds(100, 100, 320, 248);
		frmSystemStatus.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmSystemStatus.getContentPane().setLayout(null);
		frmSystemStatus.setLocationRelativeTo(null);
		btnStart.setActionCommand("");
		btnStart.setIcon(new ImageIcon(System.getProperty("user.dir") +"\\image\\start.gif"));

		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (DatabaseConnection.CheckSqlConnectionPool())
					iOpenwireManagement.connect();
			}
		});
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnStart.setBounds(10, 11, 51, 49);
		frmSystemStatus.getContentPane().add(btnStart);
		btnStop.setIcon(new ImageIcon(System.getProperty("user.dir") +"\\image\\stop.gif"));
		btnStop.setEnabled(false);

		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				iOpenwireManagement.disconnect();
			}
		});
		btnStop.setBounds(253, 11, 51, 49);
		frmSystemStatus.getContentPane().add(btnStop);
		btnExit.setIcon(new ImageIcon(System.getProperty("user.dir") +"\\image\\exitimg.gif"));

		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(253, 179, 51, 30);
		frmSystemStatus.getContentPane().add(btnExit);

		txtMessageInfo.setEditable(false);
		txtMessageInfo.setBounds(10, 89, 294, 79);
		frmSystemStatus.getContentPane().add(txtMessageInfo);

		lblMessageInfo.setBounds(10, 71, 96, 14);
		frmSystemStatus.getContentPane().add(lblMessageInfo);
	}
}
