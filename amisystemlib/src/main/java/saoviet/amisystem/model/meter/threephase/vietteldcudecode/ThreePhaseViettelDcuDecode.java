/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: ThreePhaseViettelDcuDecode.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-06-06 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.vietteldcudecode;

import saoviet.amisystem.event.SystemEventCallback;

public class ThreePhaseViettelDcuDecode implements IThreePhaseViettelDcuDecode {
	private OperationPacketViettelDecode operationPacketViettelDecode;

	private HistoricalPacketViettelDecode historicalPacketViettelDecode;

	private EventPacketViettelDecode eventPacketViettelDecode;

	// private WarningPacketViettelDecode warningPacketViettelDecode;

	private LoadProfilePacketViettelDecode loadProfilePacketViettelDecode;

	public ThreePhaseViettelDcuDecode() {
		this.operationPacketViettelDecode = new OperationPacketViettelDecode();
		this.historicalPacketViettelDecode = new HistoricalPacketViettelDecode();
		this.eventPacketViettelDecode = new EventPacketViettelDecode();
		// this.warningPacketViettelDecode = new WarningPacketViettelDecode();
		this.loadProfilePacketViettelDecode = new LoadProfilePacketViettelDecode();
	}

	public void setSystemEventCallBack(SystemEventCallback systemEventCallback) {

		this.operationPacketViettelDecode.setSystemEventCallBack(systemEventCallback);
		this.historicalPacketViettelDecode.setSystemEventCallBack(systemEventCallback);
		this.loadProfilePacketViettelDecode.setSystemEventCallBack(systemEventCallback);
		this.eventPacketViettelDecode.setSystemEventCallBack(systemEventCallback);
	}

	public OperationPacketViettelDecode getMessageOperationDecode() {
		// TODO Auto-generated method stub
		return this.operationPacketViettelDecode;
	}

	public HistoricalPacketViettelDecode getMessageHistoricalDecode() {
		// TODO Auto-generated method stub
		return this.historicalPacketViettelDecode;
	}

	public EventPacketViettelDecode getMessageEventDecode() {
		// TODO Auto-generated method stub
		return this.eventPacketViettelDecode;
	}

	// public WarningPacketViettelDecode getMessageWarningDecode() {
	// // TODO Auto-generated method stub
	// return this.warningPacketViettelDecode;
	// }
	public LoadProfilePacketViettelDecode getMessageLoadProfileDecode() {
		// TODO Auto-generated method stub
		return this.loadProfilePacketViettelDecode;
	}

}
