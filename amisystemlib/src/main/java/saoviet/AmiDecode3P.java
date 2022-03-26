package saoviet;

import java.util.Date;
import java.util.Scanner;

import saoviet.amisystem.connectionmanagement.ConnectionManagement;
import saoviet.amisystem.connectionmanagement.IConnectionManagement;
import saoviet.amisystem.event.ConnectionStatusEventCallback;
import saoviet.amisystem.model.SystemConfig;
import saoviet.amisystem.sqlhelper.DatabaseConnection;
import saoviet.amisystem.ulti.FileUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;
import saoviet.amisystem.ulti.SessionEntity;

/**
 * Hello world!
 *
 */
public class AmiDecode3P {

	// create a scanner so we can read the command-line input
	static Scanner scanner = new Scanner(System.in);
	static IConnectionManagement iactive;
	static boolean isRunning = false;
	static LogUlti logUlti = new LogUlti(AmiDecode3P.class);

	public static void main(String[] args) {
		logUlti.writeLog(LogType.INFO, "Start Thread AmiDecode3P");
		SystemConfig sys = FileUlti.getSystemConfig();
		
		
		System.out.print("\n          *****    *****    *****    *           * *  ********  *******");
		System.out.print("\n         *     *  *     *  *     *    *         *  *  *            *");
		System.out.print("\n         *        *     *  *     *     *       *   *  *            *");
		System.out.print("\n          *****   *******  *     *      *     *    *  ********     *");
		System.out.print("\n               *  *     *  *     *       *   *     *  *            *");
		System.out.print("\n         *     *  *     *  *     *        * *      *  *            *");
		System.out.print("\n          *****   *     *   *****          *       *  ********     *");
			
		System.out.print("\n ");
		
		// prompt for the user's name
		System.out.print("<<=============================|AMI DECODE 3P|===========================>>\n");
		System.out.print("Application AmiDecode message meter 3 phase\n");
		System.out.print("Copyright: Sao Viet JSC\n");
		System.out.print("Created by: Nguyen Tat Thanh - Phung Van Dong\n");
		System.out.print("Contact us: Nguyen Tat Thanh - Mobile: 098 664 8910\n");
		System.out.print("        Or: Phung Van Dong   - Mobile: 0983 643 739\n");
		System.out.print("Started Time: " + new Date() + "\n");
		System.out.print("Folder path: " + System.getProperty("user.dir") + "\n");
		System.out.print("--------------------------------|CLIENT INFO|------------------------------\n");
		System.out.print("Client: " + sys.getClientId());
		System.out.print("\nServer: " + sys.getMqttGatwayAddress());
		System.out.println("\nSubcribe Topics: " + sys.getMqttTopics());
		System.out.println("\nIs Load backup message: " +sys.isLoadMessage());
		System.out.print("--------------------------------------------------------------------------\n");
		System.out.print("Command system:\n");
		System.out.print("1. Run application: start\n");
		System.out.print("2. Stop application: stop\n");
		System.out.print("3. Exit application: exit\n");
		System.out.print("<<========================|HTTP://JIMMIINGUYEYN.INFO|====================>>\n");		
		System.out.print(">>");
		String type = null;

		while (true) {
			type = scanner.next();
			if (type.equals("start")) {
				if (isRunning) {
					System.out.print("Application is started.\n");
					continue;
				}
				if (iactive == null) {
					DatabaseConnection.CheckSqlConnectionPool();
					iactive = new ConnectionManagement();
					iactive.setConnectionStatusEventCallback(new ConnectionStatusEventCallback() {
						@Override
						public void ConnectionStatus(boolean status, String message) {
							// TODO Auto-generated method stub
							System.out.print("HES: " + message);
						}
					});
				}
				if (!SessionEntity.isSqlDisconnect()) {
					iactive.connect();
				} else {
					System.out.print("\nSQL CAN'T CREATE CONNECTION");
				}
				if (iactive.properties().getconnectedStatus()) {
					System.out.print("\nAPPLICATION IS STARTED\n");
					isRunning = true;
				}
			} else if (type.equals("stop")) {
				if (!isRunning) {
					System.out.print("\nTHREAD IS NOT START");
				} else {
					iactive.disconnect();
					isRunning = false;
					System.out.print("\nAPPLICATION IS DISCONNECTED");
				}
			} else if (type.equals("exit")) {
				if (!isRunning) {
					logUlti.writeLog(LogType.INFO, "Exit thread collection3P");
					System.exit(0);
				} else if (iactive.existThreadDecode()) {
					logUlti.writeLog(LogType.INFO, "Stop thread collection3P");
					iactive = null;
					isRunning = false;
					System.out.print("\nAPPLICATION IS STOPPED");
					System.exit(0);
				} else {
					System.out.print("\nDEQUEUING....");
				}
			} else {
				System.out.print("\nWRONG COMMAND");
			}
		}
	}
}
