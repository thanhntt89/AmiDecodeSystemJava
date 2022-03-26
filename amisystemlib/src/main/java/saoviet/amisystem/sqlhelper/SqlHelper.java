/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: SqlHelper.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.sqlhelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleTypes;
import saoviet.amisystem.model.datacollection.DataTable;

public class SqlHelper {

	/**
	 * Execute sql
	 * 
	 * @param con
	 * @param sqlcommand
	 * @throws SQLException
	 * @throws SQLException
	 */
	public static void SqliteExecuteQuery(Connection con, String sqlcommand) throws SQLException {
		Statement stmt = null;
		try {
			con.setAutoCommit(false);
			stmt = con.createStatement();
			synchronized (stmt) {
				stmt.executeUpdate(sqlcommand);
			}
			con.commit();
		} catch (Exception ex) {
		} finally {

			if (stmt != null)
				stmt.close();
			stmt = null;
		}
	}

	public static int ExecuteNoneQuery(Connection con, String procedureName, Object[] parameterIn) throws Exception {
		int rs = -1;
		StringBuilder par = new StringBuilder();

		StringBuilder sql = new StringBuilder();

		par.append("(");

		int parCount = parameterIn.length;
		// Set paramenter
		for (int i = 0; i < parCount - 1; i++) {
			par.append("?,");
		}
		par.append("?)");

		sql.append("{call ");
		sql.append(procedureName);
		sql.append(par);
		sql.append("}");
		CallableStatement callableStatement = null;
		try {
			callableStatement = con.prepareCall(sql.toString());
			// Fill paramenter
			for (int i = 0; i < parCount; i++) {
				callableStatement.setObject(i + 1, parameterIn[i]);
			}

			callableStatement.execute();

		} catch (Exception ex) {
		} finally {
			if (con != null)
				con.close();
			if (callableStatement != null)
				callableStatement.close();
			par = null;
			sql = null;
			callableStatement = null;
		}
		return rs;
	}

	public static DataTable SqliteExecuteDataset(Connection con, String sqlCommand) throws SQLException {
		DataTable map = null;
		ResultSet result = null;
		Statement stmt = null;
		try {
			con.setAutoCommit(false);
			stmt = con.createStatement();
			result = stmt.executeQuery(sqlCommand);
			map = new DataTable(result);
		} catch (Exception ex) {
		} finally {
			if (stmt != null)
				stmt.close();
			if (result != null)
				result.close();
			stmt = null;
			result = null;
		}

		return map;
	}

	/**
	 * 
	 * @param con
	 * @param procedureName
	 * @return
	 * @throws SQLException
	 */
	public static DataTable ExecueDataset(Connection con, String procedureName) throws Exception {
		DataTable result = null;
		CallableStatement callableStatement = null;
		StringBuilder sql = new StringBuilder();
		sql.append("{call ");
		sql.append(procedureName);
		sql.append("}");
		ResultSet rst = null;
		try {
			callableStatement = con.prepareCall(sql.toString());
			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			callableStatement.execute();
			rst = (ResultSet) callableStatement.getObject(1);
			result = new DataTable(rst);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out
					.println("ExecueDataset ERROR - procedureName: " + procedureName + " - error: " + ex.getMessage());

		} finally {
			if (con != null)
				con.close();
			if (rst != null)
				rst.close();
			if (callableStatement != null)
				callableStatement.close();
			sql = null;
			rst = null;
			callableStatement = null;
		}
		return result;
	}

	/**
	 * Return ResultSet parameter objects list
	 * 
	 * @param con
	 * @param procedureName
	 * @param parameterIn
	 * @return
	 * @throws SQLException
	 */
	public static DataTable ExecueDataset(Connection con, String procedureName, Object[] parameterIn) throws Exception {

		DataTable result = null;

		StringBuilder par = new StringBuilder();

		StringBuilder sql = new StringBuilder();

		ResultSet rst = null;
		CallableStatement callableStatement = null;

		par.append("(");

		int parCount = parameterIn.length;

		// Set paramenter
		for (int i = 0; i < parCount; i++) {
			par.append("?,");
		}
		par.append("?)");

		sql.append("{call ");
		sql.append(procedureName);
		sql.append(par);
		sql.append("}");

		try {
			callableStatement = con.prepareCall(sql.toString());
			// Fill paramenter
			for (int i = 0; i < parCount; i++) {
				callableStatement.setObject(i + 1, parameterIn[i]);
			}

			callableStatement.registerOutParameter(parCount + 1, OracleTypes.CURSOR);
			callableStatement.execute();
			rst = (ResultSet) callableStatement.getObject(parCount + 1);

			result = new DataTable(rst);

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ExecueDataset Has parammeter ERROR - procedureName: " + procedureName + " - error: "
					+ ex.getMessage());
		} finally {
			if (rst != null)
				rst.close();
			sql = null;
			par = null;
			if (callableStatement != null)
				callableStatement.close();
			callableStatement = null;
			if (con != null)
				con.close();
			rst = null;
		}
		return result;
	}

	public static int[] InsertBatch(Connection con, String procedureName, DataTable dataTable) throws Exception {
		// TODO Auto-generated method stub

		int[] rs = null;

		StringBuilder par = new StringBuilder();

		StringBuilder sql = new StringBuilder();

		CallableStatement callableStatement = null;

		par.append("(");

		int parCount = dataTable.columnCount();
		int rowCount = dataTable.rowCount();

		// Set paramenter
		for (int i = 0; i < parCount - 1; i++) {
			par.append("?,");
		}

		par.append("?)");
		sql.append("{call ");
		sql.append(procedureName);
		sql.append(par);
		sql.append("}");

		try {
			callableStatement = con.prepareCall(sql.toString());
			// Fill paramenter
			for (int j = 0; j < rowCount; j++) {
				for (int i = 0; i < parCount; i++) {
					callableStatement.setObject(i + 1, dataTable.getObjectValue(j, i));
				}
				callableStatement.addBatch();
			}

			rs = callableStatement.executeBatch();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
					"InsertBatch Has a ERROR - procedureName: " + procedureName + " - error: " + ex.getMessage());
		} finally {
			if (callableStatement != null)
				callableStatement.close();
			if (con != null)
				con.close();
			sql = null;
			par = null;
			callableStatement = null;
		}
		return rs;
	}
}
