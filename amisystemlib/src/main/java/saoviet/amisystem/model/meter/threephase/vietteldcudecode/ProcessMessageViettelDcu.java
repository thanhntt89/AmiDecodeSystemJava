/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: ProcessMessageViettelDcu.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-06-06 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.vietteldcudecode;

import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class ProcessMessageViettelDcu implements Runnable {
	
	 private LogUlti logUlti = new LogUlti(ProcessMessageViettelDcu.class);
	private MessageBase mesageProcess;
	private IThreePhaseViettelDcuDecode iThreePhaseViettelDcuDecode;

	public ProcessMessageViettelDcu(IThreePhaseViettelDcuDecode idecode, MessageBase mesage) {
		this.mesageProcess = mesage;
		this.iThreePhaseViettelDcuDecode = idecode;
	}

	@Override
	public void run() {
		String[] topic = this.mesageProcess.getPreTopic().split("/");

		if (topic.length < 6 || this.mesageProcess.getData() == null) {
			System.out.println("Error topics: " + this.mesageProcess.getPreTopic());
			logUlti.writeLog(LogType.ERROR, "TOPIC WRONG: " + this.mesageProcess.getPreTopic());
			return;
		}

		this.mesageProcess.setMeterType(topic[1]);
		this.mesageProcess.setMessageType(topic[2]);
		this.mesageProcess.setDcuCode(topic[4]);

		if (this.iThreePhaseViettelDcuDecode != null)
			this.processThreePhaseMessage(this.iThreePhaseViettelDcuDecode, this.mesageProcess);
	}

	public void processThreePhaseMessage(IThreePhaseViettelDcuDecode iThreePhaseViettelDcuDecode,
			MessageBase messageBase) {
		switch (messageBase.getMessageType()) {
		case Constant.OPERATIONVIETTELDCU:
			iThreePhaseViettelDcuDecode.getMessageOperationDecode().operationPacketDecode(messageBase);
			break;
		case Constant.HITSTORICALVIETTELDCU:
			iThreePhaseViettelDcuDecode.getMessageHistoricalDecode().historicalPacketDecode(messageBase);
			break;
		case Constant.EVENTVIETTELDCU:
			iThreePhaseViettelDcuDecode.getMessageEventDecode().eventPacketDecode(messageBase);
			break;
			//Tạm thời bỏ luồng Warning
//		case Constant.WARNINGVIETTELDCU:
//			ThreePhaseViettelDcuDecode.getMessageWarningDecode().warningPacketDecode(messageBase);
//			break;
		case Constant.LOADPROFILEVIETTELDCU:
			iThreePhaseViettelDcuDecode.getMessageLoadProfileDecode().loadProfilePacketDecode(messageBase);
			break;
		default:
			break;

		}
	}

}
