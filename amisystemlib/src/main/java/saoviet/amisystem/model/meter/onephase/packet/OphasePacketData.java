/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: HexingPacketData.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.model.meter.onephase.packet;

//import java.util.logging.Logger;

import saoviet.amisystem.connectionmanagement.ConnectionManagement;
import saoviet.amisystem.model.datacollection.DataCollection;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;

public class OphasePacketData {

	static final Class<?> cclass = ConnectionManagement.class;
	//private static final String className = cclass.getName();
	//private static final Logger log = Logger.getLogger(className);

	public static void setMeasurementPointDefaultConfig(String commandLine, DataCollection dataCollection) {

		String dataLength = commandLine.substring(0, 4);

		int length = ConvertUlti.hex2Int(dataLength);

		// Remove 2bytes header
		commandLine = commandLine.substring(4, commandLine.length() - length);

		int obisDataLength = 0;
		String obisTag = "";
		String obisData = "";
		String obisLength = "";

		while (commandLine.length() > 0) {
			obisTag = commandLine.substring(0, 2);
			obisLength = commandLine.substring(2, 4);
			obisDataLength = ConvertUlti.hex2Int(obisLength) * 2;
			obisData = commandLine.substring(4, obisDataLength + 4);

			// FE: DCU info - FA: Scale - FB: Alert config
			if (obisTag.equals(Constant.DCUINFO_TAG) || obisTag.equals(Constant.SCALE_TAG)
					|| obisTag.equals(Constant.ALERT_TAG)) {
				setMeasurementPointDefaultConfigSub(obisTag, obisData, dataCollection);
			} else {
				// Get obis server time
				dataCollection.updateDataByObisCodeAndTagId(Constant.SERVER_TIME_TAG, obisTag, obisData);
			}

			// Remove first data field
			commandLine = commandLine.substring(4 + obisDataLength);
		}
	}

	private static void setMeasurementPointDefaultConfigSub(String tagId, String commandLine,
			DataCollection dataCollection) {
		String obisTag = "";
		String obisData = "";
		int dataLength = 0;

		if (commandLine == "")
			return;

		while (commandLine.length() > 0) {
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
		}
	}

	public static void setPacketData(String commandLine, DataCollection dataCollection) {
		// Remove 2bytes header
		commandLine = commandLine.substring(4);
		int dataLength = 0;

		String obisTag = "";
		String obisData = "";
		String length = "";

		while (commandLine.length() > 0) {
			obisTag = commandLine.substring(0, 2);
			length = commandLine.substring(2, 4);
			dataLength = ConvertUlti.hex2Int(length) * 2;
			obisData = commandLine.substring(4, dataLength + 4);
			dataCollection.updateDataByObisCode(obisTag, obisData);
			// Remove first data field
			commandLine = commandLine.substring(4 + dataLength);
		}
	}

}
