/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: EventPacketViettelDecode.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-06-06 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.vietteldcudecode;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import saoviet.amisystem.business.MT3Business;
import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.DataField;
import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.datacollection.DataCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3EventEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3EventMeterCollection;
import saoviet.amisystem.model.meter.threephase.packetvietteldcu.MessagePacketStructureViettelDcu;
import saoviet.amisystem.model.meter.threephase.packetvietteldcu.ThreePhaseViettelDcuStructData;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class EventPacketViettelDecode {
	private LogUlti logUlti = new LogUlti(EventPacketViettelDecode.class);
	
	private DataCollection eventListDefault;
	private MT3Business imt3Business;
	private SystemEventCallback systemEventCallback;

	public EventPacketViettelDecode() {
		eventListDefault = MessagePacketStructureViettelDcu.getEventCollection();
		imt3Business = new MT3Business();
	}

	public void eventPacketDecode(MessageBase messageBase) {
		try {
			String dcuCode = messageBase.getDcuCode();
			String meterType = null;
			String commandLine = ConvertUlti.toHexString(messageBase.getData());
			DataCollection scaleList = new DataCollection();
			// Get meter scale form database
			this.imt3Business.getMeterObisScale(null, dcuCode, scaleList);
			// Lay type meter tu DataField dau tien trong list
			meterType = scaleList.getdataList().get(0).getType();
			// khong tim thay scale thi return
			if (meterType == null)
				return;
			DataCollection eventList = this.eventListDefault.copy();
			ThreePhaseViettelDcuStructData.setDataForObisCode(commandLine, eventList);

			// Decode Event
			MT3EventMeterCollection eventMeterList = new MT3EventMeterCollection();
			DataField eventValueFullList = new DataField();
			MT3EventEntity ev = new MT3EventEntity();

			// EventProgramming
			List<String> valueList = new ArrayList<String>();
			valueList = eventList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				ev.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = eventList.getValueListbyName("EventProgramming");
			if (valueList != null && valueList.size() > 0) {
				ev.setEventProgrammingCount(this.convertValue(valueList.get(0)));
				if (ev.getEventProgrammingCount() != null
						&& ev.getEventProgrammingCount().compareTo(BigDecimal.valueOf(0)) == 1) {
					ev.setEventProgrammingFirst(
							this.convertDateTime(valueList != null && valueList.size() > 1 ? valueList.get(1) : null));
					ev.setEventProgrammingSecond(
							this.convertDateTime(valueList != null && valueList.size() > 2 ? valueList.get(2) : null));
					ev.setEventProgrammingThird(
							this.convertDateTime(valueList != null && valueList.size() > 3 ? valueList.get(3) : null));
					ev.setEventProgrammingFourth(
							this.convertDateTime(valueList != null && valueList.size() > 4 ? valueList.get(4) : null));
					ev.setEventProgrammingFifth(
							this.convertDateTime(valueList != null && valueList.size() > 5 ? valueList.get(5) : null));
					eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTPROGRAMMING,
							ev.getEventProgrammingCount(), null, ev.getEventProgrammingFirst(),
							ev.getEventProgrammingSecond(), ev.getEventProgrammingThird(),
							ev.getEventProgrammingFourth(), ev.getEventProgrammingFifth(), null, null, null, null, null,
							"3P");
				}
			}

			// EventPasswordChange
			valueList = eventList.getValueListbyName("EventPasswordChange");
			if (valueList != null && valueList.size() > 0) {
				ev.setEventPasswordChangeCount(this.convertValue(valueList.get(0)));

				if (ev.getEventPasswordChangeCount() != null
						&& ev.getEventPasswordChangeCount().compareTo(BigDecimal.valueOf(0)) == 1) {
					ev.setEventPasswordChangeFirst(
							this.convertDateTime(valueList != null && valueList.size() > 1 ? valueList.get(1) : null));
					ev.setEventPasswordChangeSecond(
							this.convertDateTime(valueList != null && valueList.size() > 2 ? valueList.get(2) : null));
					ev.setEventPasswordChangeThird(
							this.convertDateTime(valueList != null && valueList.size() > 3 ? valueList.get(3) : null));
					ev.setEventPasswordChangeFourth(
							this.convertDateTime(valueList != null && valueList.size() > 4 ? valueList.get(4) : null));
					ev.setEventPasswordChangeFifth(
							this.convertDateTime(valueList != null && valueList.size() > 5 ? valueList.get(5) : null));
					eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTPASSWORDCHANGE,
							ev.getEventPasswordChangeCount(), null, ev.getEventPasswordChangeFirst(),
							ev.getEventPasswordChangeSecond(), ev.getEventProgrammingThird(),
							ev.getEventPasswordChangeFourth(), ev.getEventPasswordChangeFifth(), null, null, null, null,
							null, "3P");
				}
			}

			// EventVoltageImbalance
			valueList = eventList.getValueListbyName("EventVoltageImbalance");
			if (valueList != null && valueList.size() > 0) {
				ev.setEventVoltageImbalanceCount(this.convertValue(valueList.get(0)));
				if (ev.getEventVoltageImbalanceCount() != null
						&& ev.getEventVoltageImbalanceCount().compareTo(BigDecimal.valueOf(0)) == 1) {
					ev.setEventVoltageImbalanceFirst(
							this.convertDateTime(valueList != null && valueList.size() > 1 ? valueList.get(1) : null));
					ev.setEventVoltageImbalanceSecond(
							this.convertDateTime(valueList != null && valueList.size() > 2 ? valueList.get(2) : null));
					ev.setEventVoltageImbalanceThird(
							this.convertDateTime(valueList != null && valueList.size() > 3 ? valueList.get(3) : null));
					ev.setEventVoltageImbalanceFourth(
							this.convertDateTime(valueList != null && valueList.size() > 4 ? valueList.get(4) : null));
					ev.setEventVoltageImbalanceFifth(
							this.convertDateTime(valueList != null && valueList.size() > 5 ? valueList.get(5) : null));
					eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTVOLTAGEIMBALANCE,
							ev.getEventVoltageImbalanceCount(), null, ev.getEventVoltageImbalanceFirst(),
							ev.getEventVoltageImbalanceSecond(), ev.getEventVoltageImbalanceThird(),
							ev.getEventVoltageImbalanceFourth(), ev.getEventVoltageImbalanceFifth(), null, null, null,
							null, null, "3P");
				}
			}

			// EventPowerFailure

			eventValueFullList = eventList.getDataFieldByObisName("EventPowerFailure");
			ev.setEventPowerFailureCount(this.convertValue(this.getEventValue("02", eventValueFullList)));

			if (ev.getEventPowerFailureCount() != null
					&& ev.getEventPowerFailureCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				if ((meterType.equals(Constant.METER_TYPE_STAR)) || (meterType.equals(Constant.METER_TYPE_GELEX))) {
					eventValueFullList = eventList.getDataFieldByObisName("EventPowerFailureStart");
					ev.setEventPowerFailureFirst(this.convertDateTime(this.getEventValue("02", eventValueFullList)));
					ev.setEventPowerFailureSecond(this.convertDateTime(this.getEventValue("03", eventValueFullList)));
					ev.setEventPowerFailureThird(this.convertDateTime(this.getEventValue("04", eventValueFullList)));
					ev.setEventPowerFailureFourth(this.convertDateTime(this.getEventValue("05", eventValueFullList)));
					ev.setEventPowerFailureFifth(this.convertDateTime(this.getEventValue("06", eventValueFullList)));

					eventValueFullList = eventList.getDataFieldByObisName("EventPowerFailureEnd");
					ev.setEventPowerRecoverFirst(this.convertDateTime(this.getEventValue("02", eventValueFullList)));
					ev.setEventPowerRecoverSecond(this.convertDateTime(this.getEventValue("03", eventValueFullList)));
					ev.setEventPowerRecoverThird(this.convertDateTime(this.getEventValue("04", eventValueFullList)));
					ev.setEventPowerRecoverFourth(this.convertDateTime(this.getEventValue("05", eventValueFullList)));
					ev.setEventPowerRecoverFifth(this.convertDateTime(this.getEventValue("06", eventValueFullList)));

					eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTPOWERFAILURE,
							ev.getEventPowerFailureCount(), null, ev.getEventPowerFailureFirst(),
							ev.getEventPowerFailureSecond(), ev.getEventPowerFailureThird(),
							ev.getEventPowerFailureFourth(), ev.getEventPowerFailureFifth(),
							ev.getEventPowerRecoverFirst(), ev.getEventPowerRecoverSecond(),
							ev.getEventPowerRecoverThird(), ev.getEventPowerRecoverFourth(),
							ev.getEventPowerRecoverFifth(), "3P");

				} else {
					ev.setEventPowerFailureFirst(this.convertDateTime(this.getEventValue("03", eventValueFullList)));
					ev.setEventPowerFailureSecond(this.convertDateTime(this.getEventValue("04", eventValueFullList)));
					ev.setEventPowerFailureThird(this.convertDateTime(this.getEventValue("05", eventValueFullList)));
					ev.setEventPowerFailureFourth(this.convertDateTime(this.getEventValue("06", eventValueFullList)));
					ev.setEventPowerFailureFifth(this.convertDateTime(this.getEventValue("07", eventValueFullList)));
					ev.setEventPowerRecoverFirst(this.convertDateTime(this.getEventValue("08", eventValueFullList)));
					ev.setEventPowerRecoverSecond(this.convertDateTime(this.getEventValue("09", eventValueFullList)));
					ev.setEventPowerRecoverThird(this.convertDateTime(this.getEventValue("0A", eventValueFullList)));
					ev.setEventPowerRecoverFourth(this.convertDateTime(this.getEventValue("0B", eventValueFullList)));
					ev.setEventPowerRecoverFifth(this.convertDateTime(this.getEventValue("0C", eventValueFullList)));

					eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTPOWERFAILURE,
							ev.getEventPowerFailureCount(), null, ev.getEventPowerFailureFirst(),
							ev.getEventPowerFailureSecond(), ev.getEventPowerFailureThird(),
							ev.getEventPowerFailureFourth(), ev.getEventPowerFailureFifth(),
							ev.getEventPowerRecoverFirst(), ev.getEventPowerRecoverSecond(),
							ev.getEventPowerRecoverThird(), ev.getEventPowerRecoverFourth(),
							ev.getEventPowerRecoverFifth(), "3P");
				}
			}

			// EventTimeDateChange
			eventValueFullList = eventList.getDataFieldByObisName("EventTimeDateChange");
			ev.setEventTimeDateChangeCount(this.convertValue(this.getEventValue("02", eventValueFullList)));

			if (ev.getEventTimeDateChangeCount() != null
					&& ev.getEventTimeDateChangeCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				ev.setEventTimeDateChangeFirstStart(this.convertDateTime(this.getEventValue("03", eventValueFullList)));
				ev.setEventTimeDateChangeSecondStart(
						this.convertDateTime(this.getEventValue("04", eventValueFullList)));
				ev.setEventTimeDateChangeThirdStart(this.convertDateTime(this.getEventValue("05", eventValueFullList)));
				ev.setEventTimeDateChangeFourthStart(
						this.convertDateTime(this.getEventValue("06", eventValueFullList)));
				ev.setEventTimeDateChangeFifthStart(this.convertDateTime(this.getEventValue("07", eventValueFullList)));
				ev.setEventTimeDateChangeFirstEnd(this.convertDateTime(this.getEventValue("08", eventValueFullList)));
				ev.setEventTimeDateChangeSecondEnd(this.convertDateTime(this.getEventValue("09", eventValueFullList)));
				ev.setEventTimeDateChangeThirdEnd(this.convertDateTime(this.getEventValue("0A", eventValueFullList)));
				ev.setEventTimeDateChangeFourthEnd(this.convertDateTime(this.getEventValue("0B", eventValueFullList)));
				ev.setEventTimeDateChangeFifthEnd(this.convertDateTime(this.getEventValue("0C", eventValueFullList)));

				eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTTIMEDATECHANGE,
						ev.getEventTimeDateChangeCount(), null, ev.getEventTimeDateChangeFirstStart(),
						ev.getEventTimeDateChangeSecondStart(), ev.getEventTimeDateChangeThirdStart(),
						ev.getEventTimeDateChangeFourthStart(), ev.getEventTimeDateChangeFifthStart(),
						ev.getEventTimeDateChangeFirstEnd(), ev.getEventTimeDateChangeSecondEnd(),
						ev.getEventTimeDateChangeThirdEnd(), ev.getEventTimeDateChangeFourthEnd(),
						ev.getEventTimeDateChangeFifthEnd(), "3P");

			}

			// EventFailurePhaseA
			eventValueFullList = eventList.getDataFieldByObisName("EventFailurePhaseA");
			ev.setEventFailurePhaseACount(this.convertValue(this.getEventValue("02", eventValueFullList)));

			if (ev.getEventFailurePhaseACount() != null
					&& ev.getEventFailurePhaseACount().compareTo(BigDecimal.valueOf(0)) == 1) {
				ev.setEventFailurePhaseAFirst(this.convertDateTime(this.getEventValue("03", eventValueFullList)));
				ev.setEventFailurePhaseASecond(this.convertDateTime(this.getEventValue("04", eventValueFullList)));
				ev.setEventFailurePhaseAThird(this.convertDateTime(this.getEventValue("05", eventValueFullList)));
				ev.setEventFailurePhaseAFourth(this.convertDateTime(this.getEventValue("06", eventValueFullList)));
				ev.setEventFailurePhaseAFifth(this.convertDateTime(this.getEventValue("07", eventValueFullList)));
				ev.setEventPowerOnPhaseAFirst(this.convertDateTime(this.getEventValue("08", eventValueFullList)));
				ev.setEventPowerOnPhaseASecond(this.convertDateTime(this.getEventValue("09", eventValueFullList)));
				ev.setEventPowerOnPhaseAThird(this.convertDateTime(this.getEventValue("0A", eventValueFullList)));
				ev.setEventPowerOnPhaseAFourth(this.convertDateTime(this.getEventValue("0B", eventValueFullList)));
				ev.setEventPowerOnPhaseAFifth(this.convertDateTime(this.getEventValue("0C", eventValueFullList)));

				eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTFAILUREPHASEA,
						ev.getEventFailurePhaseACount(), null, ev.getEventFailurePhaseAFirst(),
						ev.getEventFailurePhaseASecond(), ev.getEventFailurePhaseAThird(),
						ev.getEventFailurePhaseAFourth(), ev.getEventFailurePhaseAFifth(), null, null, null, null, null,
						"3P");
			}

			// EventFailurePhaseB
			eventValueFullList = eventList.getDataFieldByObisName("EventFailurePhaseB");
			ev.setEventFailurePhaseBCount(this.convertValue(this.getEventValue("02", eventValueFullList)));

			if (ev.getEventFailurePhaseBCount() != null
					&& ev.getEventFailurePhaseBCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				ev.setEventFailurePhaseBFirst(this.convertDateTime(this.getEventValue("03", eventValueFullList)));
				ev.setEventFailurePhaseBSecond(this.convertDateTime(this.getEventValue("04", eventValueFullList)));
				ev.setEventFailurePhaseBThird(this.convertDateTime(this.getEventValue("05", eventValueFullList)));
				ev.setEventFailurePhaseBFourth(this.convertDateTime(this.getEventValue("06", eventValueFullList)));
				ev.setEventFailurePhaseBFifth(this.convertDateTime(this.getEventValue("07", eventValueFullList)));
				ev.setEventPowerOnPhaseBFirst(this.convertDateTime(this.getEventValue("08", eventValueFullList)));
				ev.setEventPowerOnPhaseBSecond(this.convertDateTime(this.getEventValue("09", eventValueFullList)));
				ev.setEventPowerOnPhaseBThird(this.convertDateTime(this.getEventValue("0A", eventValueFullList)));
				ev.setEventPowerOnPhaseBFourth(this.convertDateTime(this.getEventValue("0B", eventValueFullList)));
				ev.setEventPowerOnPhaseBFifth(this.convertDateTime(this.getEventValue("0C", eventValueFullList)));

				eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTFAILUREPHASEB,
						ev.getEventFailurePhaseBCount(), null, ev.getEventFailurePhaseBFirst(),
						ev.getEventFailurePhaseBSecond(), ev.getEventFailurePhaseBThird(),
						ev.getEventFailurePhaseBFourth(), ev.getEventFailurePhaseBFifth(), null, null, null, null, null,
						"3P");

			}

			// EventFailurePhaseC
			eventValueFullList = eventList.getDataFieldByObisName("EventFailurePhasec");
			ev.setEventFailurePhaseCCount(this.convertValue(this.getEventValue("02", eventValueFullList)));

			if (ev.getEventFailurePhaseCCount() != null
					&& ev.getEventFailurePhaseCCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				ev.setEventFailurePhaseCFirst(this.convertDateTime(this.getEventValue("03", eventValueFullList)));
				ev.setEventFailurePhaseCSecond(this.convertDateTime(this.getEventValue("04", eventValueFullList)));
				ev.setEventFailurePhaseCThird(this.convertDateTime(this.getEventValue("05", eventValueFullList)));
				ev.setEventFailurePhaseCFourth(this.convertDateTime(this.getEventValue("06", eventValueFullList)));
				ev.setEventFailurePhaseCFifth(this.convertDateTime(this.getEventValue("07", eventValueFullList)));
				ev.setEventPowerOnPhaseCFirst(this.convertDateTime(this.getEventValue("08", eventValueFullList)));
				ev.setEventPowerOnPhaseCSecond(this.convertDateTime(this.getEventValue("09", eventValueFullList)));
				ev.setEventPowerOnPhaseCThird(this.convertDateTime(this.getEventValue("0A", eventValueFullList)));
				ev.setEventPowerOnPhaseCFourth(this.convertDateTime(this.getEventValue("0B", eventValueFullList)));
				ev.setEventPowerOnPhaseCFifth(this.convertDateTime(this.getEventValue("0C", eventValueFullList)));

				eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTFAILUREPHASEC,
						ev.getEventFailurePhaseCCount(), null, ev.getEventFailurePhaseCFirst(),
						ev.getEventFailurePhaseCSecond(), ev.getEventFailurePhaseCThird(),
						ev.getEventFailurePhaseCFourth(), ev.getEventFailurePhaseCFifth(), null, null, null, null, null,
						"3P");

			}

			// EventReverseRunPhaseA
			eventValueFullList = eventList.getDataFieldByObisName("EventReverseRunPhaseA");
			ev.setEventReverseRunPhaseACount(this.convertValue(this.getEventValue("02", eventValueFullList)));

			if (ev.getEventReverseRunPhaseACount() != null
					&& ev.getEventReverseRunPhaseACount().compareTo(BigDecimal.valueOf(0)) == 1) {
				ev.setEventReverseRunPhaseAFirstStart(
						this.convertDateTime(this.getEventValue("03", eventValueFullList)));
				ev.setEventReverseRunPhaseASecondStart(
						this.convertDateTime(this.getEventValue("04", eventValueFullList)));
				ev.setEventReverseRunPhaseAThirdStart(
						this.convertDateTime(this.getEventValue("05", eventValueFullList)));
				ev.setEventReverseRunPhaseAFourthStart(
						this.convertDateTime(this.getEventValue("06", eventValueFullList)));
				ev.setEventReverseRunPhaseAFifthStart(
						this.convertDateTime(this.getEventValue("07", eventValueFullList)));
				ev.setEventReverseRunPhaseAFirstEnd(this.convertDateTime(this.getEventValue("08", eventValueFullList)));
				ev.setEventReverseRunPhaseASecondEnd(
						this.convertDateTime(this.getEventValue("09", eventValueFullList)));
				ev.setEventReverseRunPhaseAThirdEnd(this.convertDateTime(this.getEventValue("0A", eventValueFullList)));
				ev.setEventReverseRunPhaseAFourthEnd(
						this.convertDateTime(this.getEventValue("0B", eventValueFullList)));
				ev.setEventReverseRunPhaseAFifthEnd(this.convertDateTime(this.getEventValue("0C", eventValueFullList)));

				eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTREVERSERUNPHASEA,
						ev.getEventReverseRunPhaseACount(), null, ev.getEventReverseRunPhaseAFirstStart(),
						ev.getEventReverseRunPhaseASecondStart(), ev.getEventReverseRunPhaseAThirdStart(),
						ev.getEventReverseRunPhaseAFourthStart(), ev.getEventReverseRunPhaseAFifthStart(),
						ev.getEventReverseRunPhaseAFirstEnd(), ev.getEventReverseRunPhaseASecondEnd(),
						ev.getEventReverseRunPhaseAThirdEnd(), ev.getEventReverseRunPhaseAFourthEnd(),
						ev.getEventReverseRunPhaseAFifthEnd(), "3P");

			}

			// EventReverseRunPhaseB
			eventValueFullList = eventList.getDataFieldByObisName("EventReverseRunPhaseB");
			ev.setEventReverseRunPhaseBCount(this.convertValue(this.getEventValue("02", eventValueFullList)));

			if (ev.getEventReverseRunPhaseBCount() != null
					&& ev.getEventReverseRunPhaseBCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				ev.setEventReverseRunPhaseBFirstStart(
						this.convertDateTime(this.getEventValue("03", eventValueFullList)));
				ev.setEventReverseRunPhaseBSecondStart(
						this.convertDateTime(this.getEventValue("04", eventValueFullList)));
				ev.setEventReverseRunPhaseBThirdStart(
						this.convertDateTime(this.getEventValue("05", eventValueFullList)));
				ev.setEventReverseRunPhaseBFourthStart(
						this.convertDateTime(this.getEventValue("06", eventValueFullList)));
				ev.setEventReverseRunPhaseBFifthStart(
						this.convertDateTime(this.getEventValue("07", eventValueFullList)));
				ev.setEventReverseRunPhaseBFirstEnd(this.convertDateTime(this.getEventValue("08", eventValueFullList)));
				ev.setEventReverseRunPhaseBSecondEnd(
						this.convertDateTime(this.getEventValue("09", eventValueFullList)));
				ev.setEventReverseRunPhaseBThirdEnd(this.convertDateTime(this.getEventValue("0A", eventValueFullList)));
				ev.setEventReverseRunPhaseBFourthEnd(
						this.convertDateTime(this.getEventValue("0B", eventValueFullList)));
				ev.setEventReverseRunPhaseBFifthEnd(this.convertDateTime(this.getEventValue("0C", eventValueFullList)));

				eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTREVERSERUNPHASEB,
						ev.getEventReverseRunPhaseBCount(), null, ev.getEventReverseRunPhaseBFirstStart(),
						ev.getEventReverseRunPhaseBSecondStart(), ev.getEventReverseRunPhaseBThirdStart(),
						ev.getEventReverseRunPhaseBFourthStart(), ev.getEventReverseRunPhaseBFifthStart(),
						ev.getEventReverseRunPhaseBFirstEnd(), ev.getEventReverseRunPhaseBSecondEnd(),
						ev.getEventReverseRunPhaseBThirdEnd(), ev.getEventReverseRunPhaseBFourthEnd(),
						ev.getEventReverseRunPhaseBFifthEnd(), "3P");

			}

			// EventReverseRunPhaseC
			eventValueFullList = eventList.getDataFieldByObisName("EventReverseRunPhaseC");
			ev.setEventReverseRunPhaseCCount(this.convertValue(this.getEventValue("02", eventValueFullList)));

			if (ev.getEventReverseRunPhaseCCount() != null
					&& ev.getEventReverseRunPhaseCCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				ev.setEventReverseRunPhaseCFirstStart(
						this.convertDateTime(this.getEventValue("03", eventValueFullList)));
				ev.setEventReverseRunPhaseCSecondStart(
						this.convertDateTime(this.getEventValue("04", eventValueFullList)));
				ev.setEventReverseRunPhaseCThirdStart(
						this.convertDateTime(this.getEventValue("05", eventValueFullList)));
				ev.setEventReverseRunPhaseCFourthStart(
						this.convertDateTime(this.getEventValue("06", eventValueFullList)));
				ev.setEventReverseRunPhaseCFifthStart(
						this.convertDateTime(this.getEventValue("07", eventValueFullList)));
				ev.setEventReverseRunPhaseCFirstEnd(this.convertDateTime(this.getEventValue("08", eventValueFullList)));
				ev.setEventReverseRunPhaseCSecondEnd(
						this.convertDateTime(this.getEventValue("09", eventValueFullList)));
				ev.setEventReverseRunPhaseCThirdEnd(this.convertDateTime(this.getEventValue("0A", eventValueFullList)));
				ev.setEventReverseRunPhaseCFourthEnd(
						this.convertDateTime(this.getEventValue("0B", eventValueFullList)));
				ev.setEventReverseRunPhaseCFifthEnd(this.convertDateTime(this.getEventValue("0C", eventValueFullList)));

				eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTREVERSERUNPHASEC,
						ev.getEventReverseRunPhaseCCount(), null, ev.getEventReverseRunPhaseCFirstStart(),
						ev.getEventReverseRunPhaseCSecondStart(), ev.getEventReverseRunPhaseCThirdStart(),
						ev.getEventReverseRunPhaseCFourthStart(), ev.getEventReverseRunPhaseCFifthStart(),
						ev.getEventReverseRunPhaseCFirstEnd(), ev.getEventReverseRunPhaseCSecondEnd(),
						ev.getEventReverseRunPhaseCThirdEnd(), ev.getEventReverseRunPhaseCFourthEnd(),
						ev.getEventReverseRunPhaseCFifthEnd(), "3P");

			}

			// EventReverseRunThreePhase

			eventValueFullList = eventList.getDataFieldByObisName("EventReverseRunThreePhase");
			ev.setEventReverseRunThreePhaseCount(this.convertValue(this.getEventValue("02", eventValueFullList)));

			if (ev.getEventReverseRunThreePhaseCount() != null
					&& ev.getEventReverseRunThreePhaseCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				ev.setEventReverseRunThreePhaseFirstStart(
						this.convertDateTime(this.getEventValue("03", eventValueFullList)));
				ev.setEventReverseRunThreePhaseSecondStart(
						this.convertDateTime(this.getEventValue("04", eventValueFullList)));
				ev.setEventReverseRunThreePhaseThirdStart(
						this.convertDateTime(this.getEventValue("05", eventValueFullList)));
				ev.setEventReverseRunThreePhaseFourthStart(
						this.convertDateTime(this.getEventValue("06", eventValueFullList)));
				ev.setEventReverseRunThreePhaseFifthStart(
						this.convertDateTime(this.getEventValue("07", eventValueFullList)));
				ev.setEventReverseRunThreePhaseFirstEnd(
						this.convertDateTime(this.getEventValue("08", eventValueFullList)));
				ev.setEventReverseRunThreePhaseSecondEnd(
						this.convertDateTime(this.getEventValue("09", eventValueFullList)));
				ev.setEventReverseRunThreePhaseThirdEnd(
						this.convertDateTime(this.getEventValue("0A", eventValueFullList)));
				ev.setEventReverseRunThreePhaseFourthEnd(
						this.convertDateTime(this.getEventValue("0B", eventValueFullList)));
				ev.setEventReverseRunThreePhaseFifthEnd(
						this.convertDateTime(this.getEventValue("0C", eventValueFullList)));

				eventMeterList.add(dcuCode, null, ev.getServerTime(), Constant.EVENTREVERSERUNTHREEEPHASE,
						ev.getEventReverseRunThreePhaseCount(), null, ev.getEventReverseRunThreePhaseFirstStart(),
						ev.getEventReverseRunThreePhaseSecondStart(), ev.getEventReverseRunThreePhaseThirdStart(),
						ev.getEventReverseRunThreePhaseFourthStart(), ev.getEventReverseRunThreePhaseFifthStart(),
						ev.getEventReverseRunThreePhaseFirstEnd(), ev.getEventReverseRunThreePhaseSecondEnd(),
						ev.getEventReverseRunThreePhaseThirdEnd(), ev.getEventReverseRunThreePhaseFourthEnd(),
						ev.getEventReverseRunThreePhaseFifthEnd(), "3P");

			}

			if (eventMeterList.getEventMeterList().size() > 0) {
				imt3Business.insertEvent(eventMeterList);
			}

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "eventPacketDecode", ex);
		}

	}

	private Timestamp convertDateTime(String hexString) {

		try {
			int year = (ConvertUlti.convertHexToDecimal(hexString.substring(0, 4))).intValue() - 1900;
			int mm = ConvertUlti.convertHexToDecimal(hexString.substring(4, 6)).intValue() - 1; //// month
			//// 0-11
			int dd = ConvertUlti.convertHexToDecimal(hexString.substring(6, 8)).intValue();

			int hh = ConvertUlti.convertHexToDecimal(hexString.substring(8, 10)).intValue();
			int min = ConvertUlti.convertHexToDecimal(hexString.substring(10, 12)).intValue();
			int sc = ConvertUlti.convertHexToDecimal(hexString.substring(12, 14)).intValue();

			@SuppressWarnings("deprecation")
			Timestamp date = new Timestamp(year, mm, dd, hh, min, sc, 0);
			// String datetime = datetimeformat.format(date);
			return date;
		} catch (Exception ex) {
			return null;
		}
	}

	private BigDecimal convertValue(String hexString) {
		try {
			return BigDecimal.valueOf(ConvertUlti.hex2Int(hexString));
		} catch (Exception ex) {
			return null;
		}
	}

	private String getEventValue(String fieldCode, DataField eventValueFullList) {
		// Auto-generated method stub
		DataField item = new DataField();
		int index = eventValueFullList.getObisValueFullList().indexOf(new DataField(fieldCode));
		if (index > -1) {
			item = eventValueFullList.getObisValueFullList().get(index);
			return item.getData();
		}
		return null;
	}

	public void setSystemEventCallBack(SystemEventCallback systemEventCallback) {
		this.systemEventCallback = systemEventCallback;
		imt3Business.setSystemEventCallback(this.systemEventCallback);
	}

}
