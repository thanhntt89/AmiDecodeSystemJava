/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: ThreePhasePacketData.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.packet;

import java.util.Date;

import saoviet.amisystem.model.DataField;
import saoviet.amisystem.model.datacollection.DataCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3LoadProfilePacketList;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class ThreePhasePacketData {
	private static LogUlti logUlti = new LogUlti(ThreePhasePacketData.class);

	// Methode set data for MePDV first message
	public static void setMeasurementPointDefaultConfig(String commandLine, DataCollection dataCollection) {
		// String dataLength = commandLine.substring(0, 4);
		// int length = ConvertUlti.hex2Int(dataLength);
		// Remove 2bytes header
		// commandLine = commandLine.substring(4, commandLine.length() -
		// length);

		// Remove 2bytes header
		commandLine = commandLine.substring(4);
		int obiDataLength = 0;

		String obisTag = "";
		String obisData = "";
		String obisLength = "";

		while (commandLine.length() > 0) {
			try {
				obisTag = commandLine.substring(0, 2);
				obisLength = commandLine.substring(2, 4);
				obiDataLength = ConvertUlti.hex2Int(obisLength) * 2;
				obisData = commandLine.substring(4, obiDataLength + 4);
				// Remove first data field
				commandLine = commandLine.substring(4 + obiDataLength);
			} catch (Exception ex) {
				logUlti.writeLog(LogType.INFO, (new Date()) + "ERROR:" + ThreePhasePacketData.class.getName()
						+ " - setMeasurementPointDefaultConfig:" + commandLine + " - Ex:" + ex.getMessage());
			}
			// FE: DCU info - FA: Scale - FB: Alert config
			if (obisTag.equals(Constant.DCUINFO_TAG) || obisTag.equals(Constant.SCALE_TAG)
					|| obisTag.equals(Constant.ALERT_TAG)) {
				setMeasurementPointDefaultConfigSub(obisTag, obisData, dataCollection);
			} else {
				// Get obis server time
				dataCollection.updateDataByObisCodeAndTagId(Constant.SERVER_TIME_TAG, obisTag, obisData);
			}			

		}
		
		obisLength = null;
		obisTag = null;
		obisData = null;
	}

	private static void setMeasurementPointDefaultConfigSub(String tagId, String commandLine,
			DataCollection dataCollection) {
		String obisTag = "";
		String obisData = "";
		int dataLength = 0;

		if (commandLine == "")
			return;

		while (commandLine.length() > 0) {
			try {
				// Get obis code 1byte = 2 length
				obisTag = commandLine.substring(0, 2);

				// Get data field String Length = byte.Length*2
				dataLength = ConvertUlti.hex2Int(commandLine.substring(2, 4)) * 2;

				obisData = commandLine.substring(4, dataLength + 4);

				// Update data when obis exist
				dataCollection.updateDataByObisCodeAndTagId(tagId, obisTag, obisData);

				// Get data of obis remove 2 length obis + 2 length data = 4
				// Remove obis and length
				// Remove first data field
				commandLine = commandLine.substring(4 + dataLength);
			} catch (Exception ex) {
				logUlti.writeLog(LogType.INFO, (new Date()) + "ERROR:" + ThreePhasePacketData.class.getName()
						+ " - setMeasurementPointDefaultConfig:" + commandLine + " - Ex:" + ex.getMessage());
			}
			try {
				 Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		obisTag = null;
		obisData = null;
	}

	// Methode set data for Event meter
	public static void setEventPacketData(String commandLine, DataCollection dataCollection) {
		// Remove 2 byte frist
		commandLine = commandLine.substring(4);
		int dataLength = 0;
		String obisTag = "";
		String obisData = "";
		String length = "";

		while (commandLine.length() > 0) {
			try {
				obisTag = commandLine.substring(0, 2);
				length = commandLine.substring(2, 4);
				dataLength = ConvertUlti.hex2Int(length) * 2;
				obisData = commandLine.substring(4, dataLength + 4);
				dataCollection.addEvent(obisTag, obisTag, obisData);
				// Remove first data field
				commandLine = commandLine.substring(4 + dataLength);

			} catch (Exception ex) {
				logUlti.writeLog(LogType.INFO, "ERROR - setMeasurementPointDefaultConfig:" + commandLine + " - Ex:",
						ex);
			}
		}

		obisTag = null;
		obisData = null;
		length = null;		
	}

	public static MT3LoadProfilePacketList GetLoadProfileObisByName(String commandLine, DataCollection messageList) {
		MT3LoadProfilePacketList list = new MT3LoadProfilePacketList();

		// String dataLengthValue = null;
		String serverTime = null;

		String data = null;
		int dataLength = 0, subLength = 0;

		String obisTag = null;
		String obisData = null;

		// Remove 2byte lenght at first
		commandLine = commandLine.substring(4);

		// Get ServerTime obis 1byte + 1byteleth=4 char, data 6byte =12 length
		// char
		serverTime = commandLine.substring(4, 16);
		// remove servertime
		commandLine = commandLine.substring(16);

		list.setServerTimeValue(serverTime);

		DataCollection dataList = null;

		while (commandLine.length() > 0) {
			dataList = new DataCollection();

			// Get data first String Lenght = byte.Lenght*2
			dataLength = (int) ConvertUlti.hex2Int(commandLine.substring(0, 2)) * 2;

			// Get data 2length=1byte(length)*2(char)
			data = commandLine.substring(2, dataLength + 2);

			// Get obis value
			while (data.length() > 0) {
				// Get obis code 1byte = 2 lenght
				obisTag = data.substring(0, 2);

				// Get data field String Lenght = byte.Lenght*2
				subLength = (int) ConvertUlti.hex2Int(data.substring(2, 4)) * 2;

				// Get data of obistag
				obisData = data.substring(4, subLength + 4);

				// Get data of obis remove 2 lenght obis + 2 lenght data = 4
				// Remove obis and lenght
				data = data.substring(subLength + 4);

				// Check obis code in packet structure
				DataField obisExist = messageList.getDataByFieldCode(obisTag);
				// Nếu không null set data cho list
				if (obisExist != null) {
					obisExist.setData(obisData);
					dataList.add(obisExist); // add list vừa có data vào
												// list<DataField>
				}

			}

			list.addLoadProfileList(dataList);

			// Get data of obis remove: 1(byte)*2(1byte=2char) = 2
			// Remove obis and lenght
			commandLine = commandLine.substring(dataLength + 2);

		}

		serverTime = null;
		obisTag = null;
		obisData = null;
		dataList = null;
		
		return list;
	}
}
