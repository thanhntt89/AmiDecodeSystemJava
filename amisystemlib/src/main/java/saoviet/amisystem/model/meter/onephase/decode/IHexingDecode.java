/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: IHexingDecode.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.model.meter.onephase.decode;

import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.datacollection.DataCollection;

public interface IHexingDecode {

	void setSystemEventCallBack(SystemEventCallback systemEventCallback);

	void decodeMessage(MessageBase messagebase);

	void decodeMeasurementPointConfigPacket(String dcuCode, String meterId, String meterType, String commandLine,
			DataCollection dataCollection);

	void decodeMeasurementPointAlertConfig(String dcuCode, String meterId, DataCollection dataList);

	void decodeMeasurementPointScale(String dcuCode, String meterId, String meterType, DataCollection dataList);

	void decodeDcuinfo(String dcuCode, String meterId, DataCollection dataList);

	void decodeMeasurementPointAlertPacket(String dcuCode, String meterId, DataCollection dataCollection);

	void decodeEventPacket(String dcuCode, String meterId, DataCollection dataCollection);

	void decodeOperationPacket(String dcuCode, String meterId, DataCollection dataCollection);

	void decodeInstantaneousPacket(String dcuCode, String meterId, DataCollection dataCollection);

	void decodeHistoricalPacket(String dcuCode, String meterId, DataCollection dataCollection);
}
