package saoviet.amigui;

import java.awt.EventQueue;

import saoviet.amigui.systemdecode.DataCollection3Phase;
import saoviet.amigui.systemdecodeviettelSL6087.DataCollection3PhaseSL6087;
import saoviet.amisystem.ulti.SingleInstanceLock;

/**
 * Hello world!
 *
 */
public class App {
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
	        if(!SingleInstanceLock.lock()) {	        	        		
//	            System.out.println("The program is already running");
//	            System.exit(0);
	        }
	    } catch (Exception e) {
	        System.err.println("Couldn't create lock file or w/e");
	        System.exit(1);
	    }
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DataCollection3PhaseSL6087 window = new DataCollection3PhaseSL6087();
					window.frmDataCollection.setVisible(true);
//					SystemStatus window = new SystemStatus();
//					window.frmSystemStatus.setVisible(true);
					
//					SystemTime window = new SystemTime();
//					window.frmRealtimeSystem.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	
}
