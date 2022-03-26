package saoviet.amigui.systemservertime;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;


import saoviet.amisystem.connectionmanagement.ConnectionServerTimeManagement;
import saoviet.amisystem.connectionmanagement.IConnectionServerTimeManagement;
import saoviet.amisystem.event.ConnectionStatusEventCallback;
//import saoviet.amisystem.event.ConnectionStatusEventCallback;
import saoviet.amisystem.sqlhelper.DatabaseConnection;

public class SystemTime {
	private IConnectionServerTimeManagement rtcmanager = null;
	private ConnectionServerTimeManagement systime;
	public JFrame frmRealtimeSystem;
	JLabel lblConnectstatus = new JLabel("Disconnect");
	JButton btnClose = new JButton("EXIT");
	JButton btnConnect = new JButton("START");
	JButton btnDisconnect = new JButton("STOP");

	/**
	 * Create the application.
	 */
	public SystemTime() {
		LoadForm();
		initialize();
		
	}

	private void LoadForm() {
		this.rtcmanager = new ConnectionServerTimeManagement();
		this.rtcmanager.setConnectionStatusEventCallback( new ConnectionStatusEventCallback() {
			
			public void ConnectionStatus(boolean status, String message) {
					// TODO Auto-generated method stub			
				btnDisconnect.setEnabled(status);
				btnConnect.setEnabled(!status);
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRealtimeSystem = new JFrame();
		frmRealtimeSystem.setResizable(false);
		frmRealtimeSystem.setTitle("REALTIME SYSTEM");
		frmRealtimeSystem.getContentPane().setForeground(Color.RED);
		frmRealtimeSystem.setBounds(100, 100, 323, 211);
		frmRealtimeSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		lblConnectstatus.setBounds(10, 152, 77, 14);
		lblConnectstatus.setForeground(Color.RED);
		lblConnectstatus.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 12));
		frmRealtimeSystem.getContentPane().add(lblConnectstatus);
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 27));
		
		
		btnConnect.setBounds(10, 11, 141, 73);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rtcmanager != null) {
					if (DatabaseConnection.CheckSqlConnectionPool())
					{
						rtcmanager.connect();
						lblConnectstatus.setText("Connectted");
						lblConnectstatus.setForeground(Color.GREEN);
					}				
					
				}
			}
		});
		frmRealtimeSystem.getContentPane().setLayout(null);
		frmRealtimeSystem.getContentPane().add(btnConnect);
		btnDisconnect.setEnabled(false);
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 27));
		
		
		btnDisconnect.setBounds(161, 11, 141, 73);
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rtcmanager != null) {
					rtcmanager.disconnect();
					lblConnectstatus.setText("Disconnect");
					lblConnectstatus.setForeground(Color.RED);
				}
			}
		});
		frmRealtimeSystem.getContentPane().add(btnDisconnect);
		
		JLabel lblRealtimeSystem = new JLabel("");
		lblRealtimeSystem.setBounds(115, 11, 205, 50);
		lblRealtimeSystem.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		frmRealtimeSystem.getContentPane().add(lblRealtimeSystem);
		
		JButton btnSendall = new JButton("SendAll");
		btnSendall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systime.sendTimeAll();
			}
		});
		btnSendall.setBounds(97, 149, 77, 23);
		frmRealtimeSystem.getContentPane().add(btnSendall);
		
		btnClose.setToolTipText("Close application");

		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnDisconnect.isEnabled()) {					
					return;
				} else if (rtcmanager != null ) {
					System.exit(0);
				} 
				
				DatabaseConnection.closeAllConnection();
			}
		});

		btnClose.setBounds(231, 148, 66, 25);
		frmRealtimeSystem.getContentPane().add(btnClose);
	}
}
