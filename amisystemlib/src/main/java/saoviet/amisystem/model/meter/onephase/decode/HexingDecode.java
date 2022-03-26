/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: HexingDecode.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.model.meter.onephase.decode;

import saoviet.amisystem.business.MT1Bussiness;
import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.DataField;
import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.datacollection.DataCollection;
import saoviet.amisystem.model.meter.onephase.packet.HexingPacketStructure;
import saoviet.amisystem.model.meter.onephase.packet.OphasePacketData;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;

public class HexingDecode implements IHexingDecode {

	private SystemEventCallback systemEventCallback;

	@Override
	public void setSystemEventCallBack(SystemEventCallback systemEventCallback) {
		this.systemEventCallback = systemEventCallback;
		// TODO Auto-generated method stub
		this.mt1Business.setSystemEventCallback(this.systemEventCallback);
	}

	private DataCollection mepdvPacketList;
	private DataCollection dcuEventPacketList;
	private DataCollection historicalPacketList;
	private DataCollection operationPacketList;
	private DataCollection loadProfilePacketList;
	private DataCollection meterEventPacketList;
	private DataCollection dcuWarningPacketList;

	private MT1Bussiness mt1Business;

	public HexingDecode() {
		mepdvPacketList = HexingPacketStructure.getMEPDVCollection();
		System.out.println("This is init hexingdecode");
		this.mt1Business = new MT1Bussiness();
	}

	public void decodeMessage(MessageBase messagebase) {

		messagebase.setMeterType(Constant.METER_TYPE_HEXING);

		// Convert byte to string
		String commandLine = ConvertUlti.toHexString(messagebase.getData());

		// System.out.println("Dequeue data: " + commandLine);

		switch (messagebase.getMessageType()) {
		case Constant.SAO_VIET_MESSAGE_TYPE_MEPDV:
			this.decodeMeasurementPointConfigPacket(messagebase.getDcuCode(), messagebase.getMeterCode(),
					messagebase.getMeterType(), commandLine, mepdvPacketList);
			break;
		case Constant.SAO_VIET_MESSAGE_TYPE_DCU_WARNING:
			this.decodeMeasurementPointConfigPacket(messagebase.getDcuCode(), messagebase.getMeterCode(),
					messagebase.getMeterType(), commandLine, dcuWarningPacketList);
			break;
		case Constant.SAO_VIET_MESSAGE_TYPE_HISTORICAL:
			this.decodeMeasurementPointConfigPacket(messagebase.getDcuCode(), messagebase.getMeterCode(),
					messagebase.getMeterType(), commandLine, historicalPacketList);
			break;
		case Constant.SAO_VIET_MESSAGE_TYPE_INTANTANEOUS:
			this.decodeMeasurementPointConfigPacket(messagebase.getDcuCode(), messagebase.getMeterCode(),
					messagebase.getMeterType(), commandLine, operationPacketList);
			break;
		case Constant.SAO_VIET_MESSAGE_TYPE_LOAPROFILE:
			this.decodeMeasurementPointConfigPacket(messagebase.getDcuCode(), messagebase.getMeterCode(),
					messagebase.getMeterType(), commandLine, loadProfilePacketList);
			break;
		case Constant.SAO_VIET_MESSAGE_TYPE_METER_EVENT:
			this.decodeMeasurementPointConfigPacket(messagebase.getDcuCode(), messagebase.getMeterCode(),
					messagebase.getMeterType(), commandLine, meterEventPacketList);
			break;
		case Constant.SAO_VIET_MESSAGE_TYPE_OPERATION:
			this.decodeOperationPacket(messagebase.getDcuCode(), messagebase.getMeterCode(), operationPacketList);
			break;
		case Constant.SAO_VIET_MESSAGE_TYPE_MEASUREMENT_POINT_ALERT_CONFIG:
			break;
		default:
			break;
		}
	}

	public void decodeMeasurementPointConfigPacket(String dcuCode, String meterId, String meterType, String commandLine,
			DataCollection dataCollection) {
		// TODO Auto-generated method stub
		DataCollection mepdvList = new DataCollection();
		// Create copy list
		mepdvList.setDataList(dataCollection.getdataList());

		OphasePacketData.setMeasurementPointDefaultConfig(commandLine, mepdvList);

		//MT1OperationEntity operationEntity = new MT1OperationEntity();

		// try {

		// System.out.println("MeterId: " + meterId);
		// update data for datacollection
		OphasePacketData.setMeasurementPointDefaultConfig(commandLine, mepdvList);

		for (DataField item : dataCollection.getdataList()) {
			System.out.println(Thread.currentThread().getName() + "-MeterId: " + meterId + "-Obis: "
					+ item.getFieldCode() + "-" + item.getName() + "-" + item.getData());

		}

		// } catch (Exception ex) {
		//
		// }

		String temp = "Thank you. I've got it!!!";
		byte[] CDRIVES = ConvertUlti.fromStringToBytes(temp);
		if (this.systemEventCallback != null)
			this.systemEventCallback.sendData("rep/view", CDRIVES);
	}

	public void decodeMeasurementPointAlertConfig(String dcuCode, String meterId, DataCollection dataList) {
		// TODO Auto-generated method stub

	}

	public void decodeMeasurementPointScale(String dcuCode, String meterId, String meterType, DataCollection dataList) {
		// TODO Auto-generated method stub

	}

	public void decodeDcuinfo(String dcuCode, String meterId, DataCollection dataList) {
		// TODO Auto-generated method stub
	}

	public void decodeMeasurementPointAlertPacket(String dcuCode, String meterId, DataCollection dataCollection) {
		// TODO Auto-generated method stub

	}

	public void decodeEventPacket(String dcuCode, String meterId, DataCollection dataCollection) {
		// TODO Auto-generated method stub

	}

	public void decodeOperationPacket(String dcuCode, String meterId, DataCollection dataCollection) {
		// TODO Auto-generated method stub

	}

	public void decodeInstantaneousPacket(String dcuCode, String meterId, DataCollection dataCollection) {
		// TODO Auto-generated method stub

	}

	public void decodeHistoricalPacket(String dcuCode, String meterId, DataCollection dataCollection) {
		// TODO Auto-generated method stub

	}	
}
