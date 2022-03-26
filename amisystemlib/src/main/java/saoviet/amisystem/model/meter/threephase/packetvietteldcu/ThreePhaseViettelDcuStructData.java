/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: ThreePhaseViettelDcuStructData.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-06-06 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.packetvietteldcu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import saoviet.amisystem.model.DataList;
import saoviet.amisystem.model.datacollection.DataCollection;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class ThreePhaseViettelDcuStructData {
	private static LogUlti logUlti = new LogUlti(ThreePhaseViettelDcuStructData.class);

	public static void setDataForObisCode(String commandLine, DataCollection dataCollection) {
		DataList listDataType = null;
		String command = null;
		String value = null;

		// Get value list from message
		while (commandLine.length() > 0) {
			// Get CommandCode
			command = commandLine.substring(0, 2);

			// Remove 1byte CommandCode
			commandLine = commandLine.substring(2);
			int dataLength = 0;

			// Get command value
			listDataType = getValue(commandLine);
			value = listDataType.getDataString();
			dataLength = listDataType.getDataInt();
			// Remove command
			commandLine = commandLine.substring(dataLength);

			// Get server time
			if (command.equals("06")) {
				List<String> obisValueList = new ArrayList<String>();
				obisValueList.add(value);
				dataCollection.updateValueListByObisCodeAndTagId(command, command, obisValueList);
			}
			// Command Data Data tags except scale tag|| command.Equals("28")
			else if (command.equals("24") || command.equals("27")) {
				// Get obis in sequence
				getObisInSequence(command, commandLine, value, dataCollection);
			}
		}

		command = null;
		value = null;
		listDataType = null;
	}

	private static void getObisInSequence(String tagId, String commandLine, String sequeceLengthValue,
			DataCollection dataCollection) {
		// Count Length DataTag
		int tagDataLength = ConvertUlti.convertHexToDecimal(sequeceLengthValue).intValue() * 2;

		List<String> obisValueList = null;
		DataCollection obisValueFullList = null;
		DataList listData1 = null;
		String obisCode = null;
		int sqTagLength = 0;
		int prTagLength = 0;
		int sqDataLength = 0;
		DataList listData = null;
		String lengthValue = null;
		DataList listDataIn = null;
		String obisValue = null;

		try {
			// Get data foreach sequence
			while (tagDataLength > 0) {
				sqTagLength = 0;
				prTagLength = 0;
				sqDataLength = 0;
				obisValueList = new ArrayList<String>();
				obisValueFullList = new DataCollection();

				// Remove 1byte sqTagCode
				commandLine = commandLine.substring(2);
				// Get sequece Tag
				listData = getValue(commandLine);
				lengthValue = listData.getDataString();
				sqTagLength = listData.getDataInt();

				sqDataLength = ConvertUlti.convertHexToDecimal(lengthValue).intValue() * 2;
				// Remove sqTag
				commandLine = commandLine.substring(sqTagLength);

				// Remove 1byte prTagCode
				commandLine = commandLine.substring(2);
				// Get property
				listData1 = getValue(commandLine);
				prTagLength = listData1.getDataInt();
				obisCode = listData1.getDataString();

				// Remove prTag
				commandLine = commandLine.substring(prTagLength);

				// Remove sequence after decode 2 length= 1 byte obis sqTagCode
				tagDataLength -= (2 + sqTagLength + sqDataLength);

				// 2length = 1 byte obis prTagCode
				sqDataLength -= (prTagLength + 2);

				// Get value foreach Property
				while (sqDataLength > 0) {
					int valueLength = 0;
					String subObisTag = commandLine.substring(0, 2);
					// Remove 1byte ValueTagCode
					commandLine = commandLine.substring(2);
					// Get ValueTag
					listDataIn = getValue(commandLine);
					valueLength = listDataIn.getDataInt();
					obisValue = listDataIn.getDataString();
					// Remove ValueTag
					commandLine = commandLine.substring(valueLength);

					// Add value
					obisValueList.add(obisValue);

					// Add value to list full
					obisValueFullList.add(subObisTag, obisValue);
					// 2 length = 1 byte obis ValueTagCode
					sqDataLength -= (valueLength + 2);
				}

				dataCollection.updateListDataByObisCodeAndTagId(tagId, obisCode, obisValueList,
						obisValueFullList.getdataList());
			}
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "getObisInSequence", ex);
		} finally {
			obisValueList = null;
			obisValueFullList = null;
			listData1 = null;
			obisCode = null;
			sqTagLength = 0;
			prTagLength = 0;
			sqDataLength = 0;
			listData = null;
			lengthValue = null;
			listDataIn = null;
			obisValue = null;
		}
	}

	private static DataList getValue(String tag) {
		DataList dt = new DataList();
		String value = null;
		// Get Length of Sequence
		String length = tag.substring(0, 2);

		// length value
		int lengthValue = ConvertUlti.convertHexToDecimal(length).intValue() * 2;
		// Remove length
		tag = tag.substring(2);
		value = tag.substring(0, lengthValue);
		dt.setDataString(value);

		// Set data length include sequece length
		dt.setDataInt(lengthValue + 2);
		return dt;
	}

	public static DataCollection getObisScale(String commandLine) {
		DataCollection operationListScale = MessagePacketStructureViettelDcu.getOperationCollection();
		// Get value list from message
		while (commandLine.length() > 0) {
			// Get CommandCode
			String command = commandLine.substring(0, 2);

			// Remove 1byte CommandCode
			commandLine = commandLine.substring(2);
			int dataLength = 0;

			// Get command value
			DataList listDataType = getValue(commandLine);
			String value = listDataType.getDataString();
			dataLength = listDataType.getDataInt();
			// Remove command
			commandLine = commandLine.substring(dataLength);
			// Command Data Data tags except scale tag|| command.Equals("28")
			if (command.equals("28")) {
				// Get obis in sequence
				GetObisInSequenceScale("24", commandLine, value, operationListScale);
			}
		}
		return operationListScale;
	}

	private static void GetObisInSequenceScale(String tagId, String commandLine, String sequeceLengthValue,
			DataCollection dataCollection) {
		// Count Length DataTag
		int tagDataLength = ConvertUlti.convertHexToDecimal(sequeceLengthValue).intValue() * 2;
		try {
			// Get data foreach sequence
			while (tagDataLength > 0) {
				int sqTagLength = 0;
				int prTagLength = 0;
				int sqDataLength = 0;

				List<String> obisValueList = new ArrayList<String>();
				// Remove 1byte sqTagCode
				commandLine = commandLine.substring(2);
				// Get sequece Tag
				DataList listData = getValue(commandLine);
				String lengthValue = listData.getDataString();
				sqTagLength = listData.getDataInt();

				sqDataLength = ConvertUlti.convertHexToDecimal(lengthValue).intValue() * 2;
				// Remove sqTag
				commandLine = commandLine.substring(sqTagLength);

				// Remove 1byte prTagCode
				commandLine = commandLine.substring(2);
				// Get property
				DataList listData1 = getValue(commandLine);
				prTagLength = listData1.getDataInt();
				String obisCode = listData1.getDataString();
				// Remove prTag
				commandLine = commandLine.substring(prTagLength);

				// Remove sequence after decode 2 length= 1 byte obis sqTagCode
				tagDataLength -= (2 + sqTagLength + sqDataLength);

				// 2length = 1 byte obis prTagCode
				sqDataLength -= (prTagLength + 2);

				BigDecimal scaleValue = null;
				// Get value foreach Property
				while (sqDataLength > 0) {
					int valueLength = 0;
					// Remove 1byte ValueTagCode
					commandLine = commandLine.substring(2);
					// Get ValueTag
					DataList listDataIn = getValue(commandLine);
					valueLength = listDataIn.getDataInt();
					String obisValue = listDataIn.getDataString();
					// Remove ValueTag
					commandLine = commandLine.substring(valueLength);
					// Add value
					obisValueList.add(obisValue);
					// 2 length = 1 byte obis ValueTagCode
					sqDataLength -= (valueLength + 2);
				}

				scaleValue = getObisScaleValue(obisValueList.get(0));
				dataCollection.updateScaleByObisCode(obisCode, scaleValue);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static BigDecimal getObisScaleValue(String obisValue) {

		BigDecimal scaleValue = null;
		switch (obisValue) {
		case "00":
			scaleValue = BigDecimal.valueOf(1);
			break;
		case "FF":
			scaleValue = BigDecimal.valueOf(0.1);
			break;
		case "FE":
			scaleValue = BigDecimal.valueOf(0.01);
			break;
		case "FD":
			scaleValue = BigDecimal.valueOf(0.001);
			break;
		case "FC":
			scaleValue = BigDecimal.valueOf(0.0001);
			break;
		default:
			return null;
		}
		return scaleValue;
	}
}
