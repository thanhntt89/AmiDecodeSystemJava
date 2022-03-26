/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: IThreePhaseMessageDecode.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.model.meter.threephase.decode;

import saoviet.amisystem.business.IMT3Business;
import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.datacollection.DataCollection;

public interface IThreePhaseMessageDecode {

	void setSystemEventCallBack(SystemEventCallback systemEventCallback);

	void decodeMessage(MessageBase messagebase);

	void decodeMeasurementPointConfigPacket(String dcuCode, String meterId, String meterType, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business);

	void decodeMeasurementPointAlertConfig(String dcuCode, String meterId, DataCollection dataList,
			IMT3Business iMT3Business);

	/**
	 * Hàm cập nhật giá trị Scale cho điểm đo trogn DB
	 * 
	 * @param dcuCode
	 * @param meterId
	 * @param meterType
	 * @param dataList
	 * @param iMT3Business
	 */
	void decodeMeasurementPointScale(String dcuCode, String meterId, String meterType, DataCollection dataList,
			IMT3Business iMT3Business);

	void decodeDcuinfo(String dcuCode, String meterId, DataCollection dataList, IMT3Business iMT3Business);

	void decodeMeasurementPointAlertPacket(String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business);

	void decodeEventPacket(String dcuCode, String meterId, String commandLine, DataCollection dataCollection,
			IMT3Business iMT3Business);

	void decodeOperationPacket(String topic, String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business);

	void decodeInstantaneousPacket(String dcuCode, String meterId, String commandLine, DataCollection dataCollection,
			IMT3Business iMT3Business);

	void decodeHistoricalPacket(String topic, String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business);

	void decodeLoadProfilePacket(String dcuCode, String meterId, String commandLine, DataCollection dataCollection,
			IMT3Business iMT3Business);

}
