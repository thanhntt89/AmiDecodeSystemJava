package saoviet;

import java.util.Scanner;
import saoviet.amisystem.connectionmanagement.ConnectionManagement;
import saoviet.amisystem.connectionmanagement.IConnectionManagement;


/**
 * Hello world!
 *
 */


public class App {

	// create a scanner so we can read the command-line input
	static Scanner scanner = new Scanner(System.in);
	static IConnectionManagement iactive;
	static byte[] value;
	public static void main(String[] args) {
//		  Thread brokerThread = new Thread(new ActivemqTest());	     
//	        brokerThread.start();

//		value = ConvertUlti.toByteArray("FFFFFF97");
//		for(int i = 0; i< value.length;i++)
//			value[i] = (byte) ~value[i];
//		int dataa = ConvertUlti.byteArrayToInt(value);
//		dataa = ~dataa;
//		String hexbyte = ConvertUlti.byteArrayToHex(value);
//		System.out.print(hexbyte);
		
		// String key = "2b7e151628aed2a6abf7158809cf4f3c";
		// String data =
		// "7649ABAC8119B246CEE98E9B12E9197D5086CB9B507219EE95DB113A917678B273BED6B8E3C1743B7116E69E222295163FF1CAA1681FAC09120ECA307586E1A7";

		// String checkSum =
		// "220EE6B4C500E6F99C7558EEC9BD326CB396C99B3DEDC991E1F0503238610CDE52EFEB717B0D8D2A6105F3157B5F74496FDCF516C2511A8E29BC4FC133B40EC4F2D199C74D7D25F1CECB5DC5C69748C78335CC3AE93706A8E573BCB016A375198A2827ECDEC09BE24A101C44FFC0127B92ABECBB071D3764DE40E07252F7DE10A4274E711B90CFFE9A213280C79BBE2CA0962620520C308B67967A0BDD1A82F6CE5CF1B326CFE7A1CE5F4FF11C95E1E3F5";//

		// ConvertUlti.ValidateByte(ConvertUlti.fromHexToBytes(checkSum));
		// checkSum = SecurityUlti.aesDecrypt(checkSum, key);
		// String dataLength = checkSum.substring(0, 4);
		//
		// int length = ConvertUlti.hex2Int(dataLength) + 4;
		//
		// // Remove 2bytes header
		// checkSum = checkSum.substring(4, checkSum.length() - length);

		// byte[] out = new byte[];
		// System.out.println(SecurityUlti.aesDecrypt(data, key));//
		// ConvertUlti.toHexString(SecurityUlti.aesEncrypt(data))

		// System.out.println(SecurityUlti.aesDecrypt(data, key));//
		// ConvertUlti.toHexString(SecurityUlti.aesEncrypt(data))
		//System.out.print("SQL CONNECTION POOL IS CREATING...\n");
		

		try {
//
//			if (!DatabaseConnection.CheckSqlConnectionPool()) {
//				IConnectionManagement iactive = new ConnectionViettelDcuManagement();
//				iactive.connect();
//				System.out.println("Connection fail");
//				return;
//			}
			// LogUlti.writeLog(LogType.INFO, "TEST");
			// Object[] obj = new Object[13];
			//
			// obj[0] = "misv";
			// obj[1] = "SVMI";
			//
			// //ResultSet rst =
			// SqlHelper.ExecueDataset(DatabaseConnection.getSqlConnection(),
			// "get_all_user");
			//
			// // SqlHelper.ExecuteNoneQuery(con, "SYSTEMUSER_tapi.ins", obj);//
			// // GET_ALL_USERS
			// // GET_USER_BY_USER_NAME
			// while (rst.next()) {
			//
			// // Retrieve by column name
			// int id = rst.getInt("USER_ID");
			// String first = rst.getString("USER_NAME");
			// String last = rst.getString("FULL_NAME");
			// // Display values
			// System.out.print("ID: " + id);
			// System.out.print(", First: " + first);
			// System.out.println(", Last: " + last);
			// LogUlti.writeLog(LogType.INFO, "id:" + id);
			// LogUlti.writeLog(LogType.INFO, "First:" + first);
			// LogUlti.writeLog(LogType.INFO, "Last:" + last);
			// }
			// rst.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("CREATE POOL ERROR: " + e.getMessage() + "\n");
			System.out.print("SYSTEM STOPED\n");
			System.exit(0);
		}


		// IOpenwireManagement open = new OpenwireManagement();
		// open.connect();

//		IConnectionManagement iactive = new ConnectionManagement();
//		iactive.connect();
		// if (iactive.Properties().getconnectedStatus())
		// System.out.println("Connected!");
		// else
		// System.out.println("Connect fail");

		// IOpenwireManagement open = new OpenwireManagement();
		// open.connect();
		//
		// DataTable dt = new DataTable();
		//
		// dt.addRow(new Object[] { "aa", "bbb" });
		// dt.addRow(new Object[] { "AA", "BB" });
		//
		// String da = dt.getValue(1, 0);

		// prompt for the user's name
//		System.out.print("<<===========================(^_^)========================>>\n");
//		System.out.print("1. Start: start\n");
//		System.out.print("2. Stop: stop\n");
//		System.out.print("3. Exit: stop\n");
//		System.out.print("<<===========================(^_^)========================>>\n");
//
////		while (true) {
//			while (true) {
//				// get their input as a String
//				String type = scanner.next();
//
//				if (type.equals("start")) {
//					iactive = new ConnectionViettelDcuManagement();
//					iactive.setConnectionStatusEventCallback(new ConnectionStatusEventCallback() {
//						@Override
//						public void ConnectionStatus(boolean status, String message) {
//							// TODO Auto-generated method stub
//							System.out.print("HES: " + message);
//						}
//					});
//
//					iactive.connect();
//
//					break;
//				} else {
//					System.out.print("COMMAND ERROR\n");
//					System.out.print("\n");
//				}
//			}
//
//			while (true) {
//				// get their input as a String
//				String type = scanner.next();
//
//				if (type.equals("stop")) {
//					iactive.disconnect();
//					break;
//				} else {
//					if (type.equals("start")) {
//						System.out.print("Program is started\n");
//					} else
//						System.out.print("COMMAND ERROR\n");
//					
//					System.out.print("COMMAND ERROR\n");
//					System.out.print("\n");
//				}
//			}
//
//			while (true) {
//				// get their input as a String
//				String type = scanner.next();
//				if (type.equals("exit")) {
//					iactive.existThreadDecode();
//					System.exit(0);
//				} else {
//					if (type.equals("stop")) {
//
//					} else if (type.equals("start")) {
//						System.out.print("Program is started\n");
//						break;
//					} else
//						System.out.print("COMMAND ERROR\n");
//					System.out.print("\n");
//				}
//			}
//		}
	}
}
