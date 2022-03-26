/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: ProcessMessage.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.messagemanagement;

import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.meter.onephase.decode.IOnePhaseDecode;
import saoviet.amisystem.model.meter.threephase.decode.IThreePhaseDecode;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;

public class ProcessMessage implements Runnable, IProcessMessage {

	private MessageBase mesageProcess;
	private IOnePhaseDecode iOnePhaseDecode;
	private IThreePhaseDecode iThreePhaseDecode;

	public ProcessMessage(IOnePhaseDecode idecode, MessageBase mesage) {
		this.mesageProcess = mesage;
		this.iOnePhaseDecode = idecode;
	}

	public ProcessMessage(IThreePhaseDecode idecode, MessageBase mesage) {
		this.mesageProcess = mesage;
		this.iThreePhaseDecode = idecode;
	}

	@Override
	public void run() {
		this.mesageProcess.setData(ConvertUlti.RemovedCheckSum(this.mesageProcess.getData()));
		if (this.iOnePhaseDecode != null)
			this.processOnePhaseMessage(this.iOnePhaseDecode, this.mesageProcess);
		else if (this.iThreePhaseDecode != null)
			this.processThreePhaseMessage(this.iThreePhaseDecode, this.mesageProcess);

		this.iOnePhaseDecode = null;
		this.iThreePhaseDecode = null;
		this.mesageProcess = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see saoviet.amisystem.messagemanagement#processOnePhaseMessage(
	 * IOnePhaseDecode iOnePhaseDecode,MessageBase messageBase)
	 */
	public void processOnePhaseMessage(IOnePhaseDecode iOnePhaseDecode, MessageBase messageBase) {
		switch (messageBase.getMeterType()) {
		case Constant.TOPIC_TYPE_ELSTER:
			System.out.println("Topics: METER_TYPE_ELSTER");
			break;
		case Constant.TOPIC_TYPE_GELEX:
			System.out.println("Topics: METER_TYPE_GELEX");
			break;
		case Constant.TOPIC_TYPE_GENIUS:
			System.out.println("Topics: METER_TYPE_GENIUS");
			break;
		case Constant.TOPIC_TYPE_HEXING:
			iOnePhaseDecode.getIHexingDecode().decodeMessage(messageBase);
			break;
		case Constant.TOPIC_TYPE_LANDIS:
			System.out.println("Topics: METER_TYPE_LANDIS");
			break;
		case Constant.TOPIC_TYPE_STAR:
			System.out.println("Topics: METER_TYPE_STAR");
			break;
		default:
			System.out.println("Topics default: " + messageBase.getPreTopic());
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see saoviet.amisystem.messagemanagement#processThreePhaseMessage(
	 * IThreePhaseDecode iThreePhaseDecode,MessageBase messageBase)nh
	 */
	public void processThreePhaseMessage(IThreePhaseDecode iThreePhaseDecode, MessageBase messageBase) {
		switch (messageBase.getMeterType()) {
		case Constant.TOPIC_TYPE_ELSTER:
			this.iThreePhaseDecode.getIElsterDecode().decodeMessage(messageBase);
			break;
		case Constant.TOPIC_TYPE_GELEX:
			this.iThreePhaseDecode.getIGelexDecode().decodeMessage(messageBase);
			break;
		case Constant.TOPIC_TYPE_GENIUS:
			System.out.println("Topics: METER_TYPE_GENIUS");
			break;
		case Constant.TOPIC_TYPE_HEXING:
			break;
		case Constant.TOPIC_TYPE_LANDIS:
			this.iThreePhaseDecode.getILandisDecode().decodeMessage(messageBase);
			break;
		case Constant.TOPIC_TYPE_STAR:
			this.iThreePhaseDecode.getIStarDecode().decodeMessage(messageBase);
			break;
		default:
			System.out.println("Topics default: " + messageBase.getPreTopic());
			break;
		}
	}
}
