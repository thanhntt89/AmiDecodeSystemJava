/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: DataTable.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.model.datacollection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DataTable {

	public TableModel dataTable;

	public TableModel getDataTable() {
		return dataTable;
	}

	private Vector<String> columnNames;
	private Vector<Vector<Object>> rows = new Vector<Vector<Object>>();

	private TableModel resultSetToTableModel(ResultSet rs) {
		try {
			int numberOfColumns = 0;
			if (rs == null)
				return null;
			ResultSetMetaData metaData = rs.getMetaData();
			if (metaData != null)
				numberOfColumns = metaData.getColumnCount();

			// Get the column names
			for (int column = 0; column < numberOfColumns; column++) {
				this.columnNames.addElement(metaData.getColumnLabel(column + 1));
			}

			// Get all rows.
			while (rs.next()) {
				Vector<Object> newRow = new Vector<Object>();
				for (int i = 1; i <= numberOfColumns; i++) {
					newRow.addElement(rs.getObject(i));
				}
				this.rows.addElement(newRow);
				newRow = null;
			}
			rs.close();
			metaData = null;
			return new DefaultTableModel(rows, this.columnNames);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("resultSetToTableModel error:" + e.getMessage());
			return null;
		}
	}

	public DataTable() {
	}

	public DataTable(ResultSet result) throws SQLException {
		this.columnNames = new Vector<String>();
		this.dataTable = this.resultSetToTableModel(result);
	}

	public String getValue(int rowIndex, int columnIndex) {
		return this.dataTable.getValueAt(rowIndex, columnIndex).toString();
	}

	public Object getObjectValue(int rowIndex, int columnIndex) {
		return this.dataTable.getValueAt(rowIndex, columnIndex);
	}

	public String getValue(int rowIndex, String columnName) {
		return this.getValue(rowIndex, this.columnNames.indexOf(columnName));
	}

	public void addRow(Object[] obj) {

		Vector<Object> newRow = new Vector<Object>();
		if (this.columnNames == null) {
			this.columnNames = new Vector<String>();
			this.columnNames.setSize(obj.length);
		}

		for (int i = 0; i < obj.length; i++) {
			newRow.addElement(obj[i]);
		}
		
		this.rows.addElement(newRow);
		this.dataTable = new DefaultTableModel(this.rows, this.columnNames);		
		newRow = null;
	}

	public int rowCount() {
		return this.rows.size();
	}

	public int columnCount() {
		return this.dataTable.getColumnCount();
	}

	public void rowClear() {
		this.rows.clear();
	}
}
