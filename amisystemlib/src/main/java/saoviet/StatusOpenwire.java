package saoviet;

import java.util.Date;
import java.util.Scanner;
import saoviet.amisystem.connectionmanagement.IOpenwireManagement;
import saoviet.amisystem.connectionmanagement.OpenwireManagement;
import saoviet.amisystem.event.ConnectionStatusEventCallback;
import saoviet.amisystem.model.SystemConfig;
import saoviet.amisystem.sqlhelper.DatabaseConnection;
import saoviet.amisystem.ulti.FileUlti;
import saoviet.amisystem.ulti.SessionEntity;

/**
 * Hello world!
 *
 */
public class StatusOpenwire {

	// create a scanner so we can read the command-line input
	static Scanner scanner = new Scanner(System.in);
	static IOpenwireManagement iactive;
	static boolean isRunning = false;

	public static void main(String[] args) {
		SystemConfig sys = FileUlti.getSystemConfig();
		// prompt for the user's name
		System.out.print("<==========================System Status========================>>\n");
		System.out.print("Application to get DCU status from gateway using openwire protocol\n");
		System.out.print("Copyright: Sao Viet JSC\n");
		System.out.print("Created by: Nguyen Tat Thanh - Phung Van Dong\n");
		System.out.print("Contact us: Nguyen Tat Thanh - Mobile: 098 664 8910\n");
		System.out.print("Or: Phung Van Dong - Mobile: 0983 643 739\n");
		System.out.print("Ha Noi " + new Date() + "\n");
		System.out.print("_____________________________CLIENT INFO__________________________\n");
		System.out.print("Client: " + sys.getClientId());
		System.out.print("\nServer: " + sys.getMqttGatwayAddress());
		System.out.print("\n------------------------------------------------------------------\n");
		System.out.print("Command line system\n");
		System.out.print("1. Run application: start\n");
		System.out.print("2. Stop application: stop\n");
		System.out.print("3. Exit application: exit\n");
		System.out.print("<<======================HTTP://JIMMIINGUYEYN.INFO=================>>\n");

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
					iactive = new OpenwireManagement();
					iactive.setConnectionStatusEventCallback(new ConnectionStatusEventCallback() {
						@Override
						public void ConnectionStatus(boolean status, String message) {
							// TODO Auto-generated method stub
							System.out.print("STATUS: " + message);
							isRunning = status;
						}
					});
				}
				if (!SessionEntity.isSqlDisconnect()) {
					iactive.connect();
				}

			} else if (type.equals("stop")) {
				if (!isRunning)
					System.out.print("\nTHREAD IS NOT START");
				else {
					iactive.disconnect();
					isRunning = false;
					System.out.print("\nAPPLICATION IS DISCONNECTED");
				}

			} else if (type.equals("exit")) {
				if (!isRunning)
					isRunning = false;
				else
					iactive.disconnect();
				System.out.print("\nAPPLICATION IS STOPPED");
				System.exit(1);

			} else {
				System.out.print("\nWRONG COMMAND");
			}
		}
	}

}
