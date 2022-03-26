/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: SaveMessageUlti.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.ulti;

import java.sql.SQLException;

import saoviet.amisystem.model.datacollection.DataTable;
import saoviet.amisystem.sqlhelper.DatabaseConnection;
import saoviet.amisystem.sqlhelper.SqlHelper;

public class SaveMessageUlti implements Runnable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.saveMessageLog(this.topics, this.data);
		//System.out.println("Message: " + topics + "-" + data);
	}

	private String topics = "";
	private String data = "";

	public SaveMessageUlti(String topics, String data) {
		this.topics = topics;
		this.data = data;
	}

	public SaveMessageUlti() {
	}

	public DataTable getMessageLog() throws SQLException {

		String sql = "SELECT * FROM [MESSAGES] WHERE [STATUS] IS NULL AND (TOPICS LIKE '%Opera%' OR TOPICS LIKE '%current%')";

		DataTable rst = null;

		rst = SqlHelper.SqliteExecuteDataset(DatabaseConnection.getMessageLogConnection(), sql);

		return rst;
	}

	public void UpdateMessageLog(String topics, int status) {
		String sql = "UPDATE MESSAGES set Status = " + status + " WHERE Topics = '" + topics + "';";
		try {
			SqlHelper.SqliteExecuteQuery(DatabaseConnection.getMessageLogConnection(), sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveMessageLog(String topics, String data) {
		try {
			// this.createDatabase();
			String sql = "INSERT INTO [MESSAGES] ( Topics, Content, CreatedDate) values ('" + topics + "','" + data
					+ "','" + StringUlti.getCurrentTimeStamp() + "');";
			SqlHelper.SqliteExecuteQuery(DatabaseConnection.getMessageLogConnection(), sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
