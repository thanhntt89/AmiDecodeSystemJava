/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: DatabaseConnection.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.sqlhelper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.SynchronousMode;

import saoviet.amisystem.model.SystemConfig;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.FileUlti;
import saoviet.amisystem.ulti.SessionEntity;

public class DatabaseConnection {
	private static SystemConfig sys;
	private static boolean isValide = false;
	private static BasicDataSource dbPool;

	// Default config
	private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static String DB_URL = "jdbc:oracle:thin:@124.158.5.154:1521:orcl";

	// Database credentials
	private static String USER = "thanhnt";
	private static String PASS = "thanhnt";

	static {
		sys = FileUlti.getSystemConfig();
	}

	private static Connection sqlConnection;

	private static Connection sqliteConnection;

	public static Connection getMessageLogConnection() {
		return sqliteConnection;
	}

	public static Connection getSqlConnection() {
		Connection conn = null;
		try {
			conn = dbPool.getConnection();
		} catch (SQLException e) {
			System.out.println("get direction connection");
			conn = getConnection();
		}
		return conn;
	}

	public static void closeAllConnection() {
		try {
			sqliteConnection.close();
			dbPool.close();
			dbPool = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get oracle connection *
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		DB_URL = sys.getDbConnectionString();
		USER = sys.getSqlUserName();
		PASS = sys.getSqlPassword();
		try {
			Class.forName(JDBC_DRIVER);
			sqlConnection = DriverManager.getConnection(DB_URL, USER, PASS);
			SessionEntity.setSqlDisconnect(false);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			SessionEntity.setSqlDisconnect(true);
		}
		return sqlConnection;
	}

	/**
	 * Get ORACLE connection
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void getConnectionPool() throws SQLException {
		isValide = false;
		dbPool = new BasicDataSource();
		dbPool.setDriverClassName(JDBC_DRIVER);// "oracle.jdbc.driver.OracleDriver"
		dbPool.setUrl(sys.getDbConnectionString());
		dbPool.setUsername(sys.getSqlUserName());
		dbPool.setPassword(sys.getSqlPassword());
		dbPool.setInitialSize(sys.getSqlPoolInitSize());
		dbPool.setMaxIdle(sys.getSqlMaxIdle());
		dbPool.setMinIdle(sys.getSqlMinIdl());
		dbPool.setMaxActive(sys.getSqlMaxActive());
		dbPool.setDefaultAutoCommit(sys.isSqlDefaultAutoCommit());
		dbPool.setAccessToUnderlyingConnectionAllowed(true);
		dbPool.setPoolPreparedStatements(true);
		// System.out.println("setLoginTimeout:" + sys.getSqlLoginTimeout());
		// dbPool.setLoginTimeout(sys.getSqlLoginTimeout());
		dbPool.setValidationQueryTimeout(sys.getSqlQueryTimeOut());
		// dbPool.setLogWriter(new PrintWriter(System.out)); 2 thang nay neu
		// dung no toan bao loi anh phai bo no di moi chay duoc
		new Thread(new Runnable() {
			@Override
			public void run() {
				int count = 0;
				while (true) {
					checkConnection();
					try {
						if (SessionEntity.isSqlDisconnect()) {							
							count++;
							System.out.println("RECONNECT SQL COUNTS: "+count);
							SessionEntity.setLostCount(count);
							Thread.sleep(60000);							
						} else {
							Thread.sleep(3000);
							count = 0;
							SessionEntity.setLostCount(count);
						}
						if (SessionEntity.getLostCount() == SessionEntity.getMaxLostCount())
							break;
					} catch (InterruptedException e) {
					}
				}
				System.out.println("STOP THREAD CHECK SQL CONNECTION");
			}

		}).start();
		System.out.println("SQL CONNECTION POOL IS CREATED");
		System.out.println("POOL SIZE: " + sys.getSqlPoolInitSize());
	}

	/**
	 * Check connection
	 */
	private static void checkConnection() {
		Connection conn = null;
		try {
			getConnection();
//			conn = dbPool.getConnection();
//			isValide = conn.isValid(dbPool.getValidationQueryTimeout());
		} catch (Exception e) {
			System.out.println("CONNECTION FAIL: " + e.getMessage());

		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean CheckSqlConnectionPool() {
		System.out.print("SQL CONNECTION POOL IS CREATING...\n");
		try {
			getSqliteConnection();
			getConnectionPool();
		} catch (Exception e) {
			System.out.print("FAIL: SQL CONNECTION POOL ...\n");
		}
		return !isValide;
	}

	private static void getSqliteConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			File f = new File(Constant.MESSAGE_DATABASE_FILE_PATH);
			SQLiteConfig config = new SQLiteConfig();
			config.setSynchronous(SynchronousMode.OFF);

			if (f.exists()) {

				// Get filesize sqlite MB
				long byteSize = f.length() / 1000000;

				if (byteSize > 200) {

					File dest = new File(Constant.MESSAGE_DATABASE_FILE_PATH_RENAME);

					// Rename old database
					f.renameTo(dest);
					// Create new database
					createSqliteFile();
					return;
				}
				sqliteConnection = DriverManager.getConnection("jdbc:sqlite:" + Constant.MESSAGE_DATABASE_FILE_PATH,
						config.toProperties());
				// do something
				return;
			}

			createSqliteFile();

		} catch (Exception e) {
			System.out.println("Connected sqlite database error...");
		}
	}

	private static void createSqliteFile() {
		try {
			Class.forName("org.sqlite.JDBC");
			SQLiteConfig config = new SQLiteConfig();
			config.setSynchronous(SynchronousMode.OFF);

			sqliteConnection = DriverManager.getConnection("jdbc:sqlite:" + Constant.MESSAGE_DATABASE_FILE_PATH,
					config.toProperties());
			Statement stmt = null;
			try {
				stmt = DatabaseConnection.getMessageLogConnection().createStatement();
				String sql = "CREATE TABLE [MESSAGES] (No INTEGER PRIMARY KEY   AUTOINCREMENT,Topics text,Content text,CreatedDate datetime,Status integer)";
				stmt.executeUpdate(sql);
				stmt.close();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				System.exit(0);
			}

			stmt = null;
			System.out.println("Connected sqlite database successfully...");
		} catch (Exception e) {
			System.out.println("Connected sqlite database error...");
		}
	}

}
