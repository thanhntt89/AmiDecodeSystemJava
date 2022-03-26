/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: ElsterMessageDecode.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.decode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Calendar;
import saoviet.amisystem.business.IMT3Business;
import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.DataField;
import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.datacollection.DataCollection;
import saoviet.amisystem.model.dcu.DcuInforEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3EventEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3EventMeterCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3HistoricalEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3LoadProfile3PMessageEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3LoadProfilePacketList;
import saoviet.amisystem.model.meter.threephase.entity.MT3MeasurementPointAlertConfigCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3OperationEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3MeterAlertCollection;
import saoviet.amisystem.model.meter.threephase.packet.ElsterPacketStructure;
import saoviet.amisystem.model.meter.threephase.packet.ThreePhasePacketData;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.SaveMessageUlti;
import saoviet.amisystem.ulti.StringUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class ElsterMessageDecode implements IThreePhaseMessageDecode {

	private SystemEventCallback systemEventCallback;
	private LogUlti logUlti = new LogUlti(ElsterMessageDecode.class);
	private SaveMessageUlti save = new SaveMessageUlti();

	@Override
	public void setSystemEventCallBack(SystemEventCallback systemEventCallback) {
		// TODO Auto-generated method stub
		this.systemEventCallback = systemEventCallback;
		this.iMT3Business.setSystemEventCallback(this.systemEventCallback);
	}

	private DataCollection mepdvPacketList;
	private DataCollection historicalPacketList;
	private DataCollection operationPacketList;
	private DataCollection loadProfilePacketList;
	private DataCollection meterEventPacketList;
	private DataCollection dcuWarningPacketList;
	private IMT3Business iMT3Business;

	public ElsterMessageDecode(IMT3Business inIMT3Business) {

		this.mepdvPacketList = ElsterPacketStructure.getMEPDVCollection();

		this.historicalPacketList = ElsterPacketStructure.getHistoricalCollection();

		this.operationPacketList = ElsterPacketStructure.getOperationCollection();

		this.loadProfilePacketList = ElsterPacketStructure.getLoadProfileCollection();

		this.meterEventPacketList = ElsterPacketStructure.getEventCollection();

		this.dcuWarningPacketList = ElsterPacketStructure.getMeterAlertCollection();

		this.iMT3Business = inIMT3Business;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.model.meter.threephase.decode.IThreePhaseMessageDecode#
	 * decodeMessage(saoviet.amisystem.model.MessageBase)
	 */
	@Override
	public void decodeMessage(MessageBase messagebase) {
		try {
			// Convert byte to string
			String commandLine = null;

			switch (messagebase.getMessageType()) {
			case Constant.SAO_VIET_MESSAGE_TYPE_MEPDV:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeMeasurementPointConfigPacket(messagebase.getDcuCode(), messagebase.getMeterCode(),
						Constant.METER_TYPE_ELSTER, commandLine, this.mepdvPacketList.copy(), this.iMT3Business);
				break;
			case Constant.SAO_VIET_MESSAGE_TYPE_DCU_WARNING:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeMeasurementPointAlertPacket(messagebase.getDcuCode(), messagebase.getMeterCode(),
						commandLine, this.dcuWarningPacketList.copy(), this.iMT3Business);
				break;
			case Constant.SAO_VIET_MESSAGE_TYPE_HISTORICAL:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeHistoricalPacket(messagebase.getPreTopic(), messagebase.getDcuCode(),
						messagebase.getMeterCode(), commandLine, this.historicalPacketList.copy(), this.iMT3Business);
				break;
			case Constant.SAO_VIET_MESSAGE_TYPE_INTANTANEOUS:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeInstantaneousPacket(messagebase.getDcuCode(), messagebase.getMeterCode(), commandLine,
						this.operationPacketList.copy(), this.iMT3Business);
				break;
			case Constant.SAO_VIET_MESSAGE_TYPE_LOAPROFILE:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeLoadProfilePacket(messagebase.getDcuCode(), messagebase.getMeterCode(), commandLine,
						this.loadProfilePacketList.copy(), this.iMT3Business);
				break;
			case Constant.SAO_VIET_MESSAGE_TYPE_METER_EVENT:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeEventPacket(messagebase.getDcuCode(), messagebase.getMeterCode(), commandLine,
						this.meterEventPacketList.copy(), this.iMT3Business);
				break;
			case Constant.SAO_VIET_MESSAGE_TYPE_OPERATION:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeOperationPacket(messagebase.getPreTopic(), messagebase.getDcuCode(),
						messagebase.getMeterCode(), commandLine, this.operationPacketList.copy(), this.iMT3Business);
				break;
			default:
				break;
			}

			commandLine = null;

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeMessage", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.model.meter.threephase.decode.IThreePhaseMessageDecode#
	 * decodeMeasurementPointConfigPacket(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String,
	 * saoviet.amisystem.model.datacollection.DataCollection,
	 * saoviet.amisystem.business.IMT3Business)
	 */
	@Override
	public void decodeMeasurementPointConfigPacket(String dcuCode, String meterId, String meterType, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {

		// Biến cho biết trạng thái của điểm đo từ bản tin MePDV:
		// Giá trị = 1 - CHANG_DCU
		// Giá trị = 2 - CHANGE_METER
		// Giá trị = 3 - MP_NEW
		int mpstatus = 0; // Giá trị khởi tạo mạc định

		try {

			// Get MePDV data
			ThreePhasePacketData.setMeasurementPointDefaultConfig(commandLine, dataCollection);

			// Update DcuId for Measurement point
			mpstatus = iMT3Business.UpdateDcuIdForMeasurementPointByDcuCodeAndMeterId(dcuCode, meterId, meterType);

			if (mpstatus != -2) { // KHONG PHAI GIA TRI MAC DINH
				// Update obiscale for meter if change meter
				this.decodeMeasurementPointScale(dcuCode, meterId, meterType, dataCollection, iMT3Business);
				// Default alert config info
				this.decodeMeasurementPointAlertConfig(dcuCode, meterId, dataCollection, iMT3Business);
			}

			// DCU info
			this.decodeDcuinfo(dcuCode, meterId, dataCollection, iMT3Business);

			if (this.systemEventCallback != null) {
				byte[] ResponseDCU = ConvertUlti.fromStringToBytes(Constant.PAYLOAD_RESPONSE_DCU);
				this.systemEventCallback.sendData(
						String.format(Constant.TOPIC_RESPONSE_FOR_DCU_OF_FIRST_MESSAGE, dcuCode), ResponseDCU);
				ResponseDCU = null;
			}
		} catch (Exception ex) {
			System.out.println("MEPDV_START ERROR: " + mpstatus + " - meterType:" + meterType + " - DCU:" + dcuCode
					+ " -METER:" + meterId + " - DATAERROR:" + ex);
			logUlti.writeLog(LogType.ERROR, "MePDV decode fail", ex);
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.model.meter.threephase.decode.IThreePhaseMessageDecode#
	 * decodeMeasurementPointScale(java.lang.String, java.lang.String,
	 * java.lang.String, saoviet.amisystem.model.datacollection.DataCollection,
	 * saoviet.amisystem.business.IMT3Business)
	 */
	@Override
	public void decodeMeasurementPointScale(String dcuCode, String meterId, String meterType,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		try {
			// Get Scale list
			DataCollection scaleList = dataCollection.getDataCollectionByTagId(Constant.SCALE_TAG);
			BigDecimal scale = null;

			for (DataField item : scaleList.getdataList()) {
				try {
					scale = ConvertUlti.convertHexToDecimal(item.getData()) == null ? null
							: BigDecimal.valueOf(1).divide(ConvertUlti.convertHexToDecimal(item.getData()));
					if (scale != null) {
						item.setScale(scale);
						item.setType(meterType);
					}
				} catch (Exception ex) {
					logUlti.writeLog(LogType.ERROR, "decodeMeasurementPointScale", ex);
				}
			}

			iMT3Business.UpdateMeterObisScale(meterId, scaleList, false);

			scaleList = null;
			scale = null;
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeMeasurementPointScale", ex);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.model.meter.threephase.decode.IThreePhaseMessageDecode#
	 * decodeMeasurementPointAlertConfig(java.lang.String, java.lang.String,
	 * saoviet.amisystem.model.datacollection.DataCollection,
	 * saoviet.amisystem.business.IMT3Business)
	 */
	@Override
	public void decodeMeasurementPointAlertConfig(String dcuCode, String meterId, DataCollection dataList,
			IMT3Business iMT3Business) {
		try {
			MT3MeasurementPointAlertConfigCollection alertValueList = new MT3MeasurementPointAlertConfigCollection();

			BigDecimal alertValue = null;
			// Get Measurement Point Alert config info from packetMePDV
			DataCollection mpAlertConfList = dataList.getDataCollectionByTagId(Constant.ALERT_TAG);

			// Update alert config
			alertValue = this.getAlertValue(mpAlertConfList.getDatabyName("FreqOver"), 100);
			alertValueList.add(dcuCode, meterId, Constant.FREQUENCY_3P, "AlertConfig", alertValue);

			alertValue = this.getValueBy10PercentOver(alertValue);
			alertValueList.add(dcuCode, meterId, Constant.FREQUENCY_H_3P, "AlertConfig", alertValue);

			alertValue = this.getAlertValue(mpAlertConfList.getDatabyName("FreqUnder"), 100);
			alertValueList.add(dcuCode, meterId, Constant.FREQUENCY_L_3P, "AlertConfig", alertValue);

			alertValue = this.getAlertValue(mpAlertConfList.getDatabyName("PowerFactorUnder"), 1000);
			alertValueList.add(dcuCode, meterId, Constant.COS_3P, "AlertConfig", alertValue);

			alertValue = this.getAlertValue(mpAlertConfList.getDatabyName("VotPhaseAOver"), 100);
			alertValueList.add(dcuCode, meterId, Constant.VOLTAGE_A_H_3P, "AlertConfig", alertValue);

			alertValue = this.getValueBy10PercentOver(alertValue);
			alertValueList.add(dcuCode, meterId, Constant.VOLTAGE_A_3P, "AlertConfig", alertValue);

			alertValue = this.getAlertValue(mpAlertConfList.getDatabyName("VotPhaseAUnder"), 100);
			alertValueList.add(dcuCode, meterId, Constant.VOLTAGE_A_L_3P, "AlertConfig", alertValue);

			alertValue = this.getAlertValue(mpAlertConfList.getDatabyName("VotPhaseBOver"), 100);
			alertValueList.add(dcuCode, meterId, Constant.VOLTAGE_B_H_3P, "AlertConfig", alertValue);

			alertValue = this.getValueBy10PercentOver(alertValue);
			alertValueList.add(dcuCode, meterId, Constant.VOLTAGE_B_3P, "AlertConfig", alertValue);

			alertValue = this.getAlertValue(mpAlertConfList.getDatabyName("VotPhaseBUnder"), 100);
			alertValueList.add(dcuCode, meterId, Constant.VOLTAGE_B_L_3P, "AlertConfig", alertValue);

			alertValue = this.getAlertValue(mpAlertConfList.getDatabyName("VotPhaseCOver"), 100);
			alertValueList.add(dcuCode, meterId, Constant.VOLTAGE_C_H_3P, "AlertConfig", alertValue);

			alertValue = this.getValueBy10PercentOver(alertValue);
			alertValueList.add(dcuCode, meterId, Constant.VOLTAGE_C_3P, "AlertConfig", alertValue);

			alertValue = this.getAlertValue(mpAlertConfList.getDatabyName("VotPhaseCUnder"), 100);
			alertValueList.add(dcuCode, meterId, Constant.VOLTAGE_C_L_3P, "AlertConfig", alertValue);

			alertValue = this.getAlertValue(mpAlertConfList.getDatabyName("Cur1PhaseOverRated"), 100);
			alertValueList.add(dcuCode, meterId, Constant.CURRENT_A_H_3P, "AlertConfig", alertValue);
			alertValueList.add(dcuCode, meterId, Constant.CURRENT_B_H_3P, "AlertConfig", alertValue);
			alertValueList.add(dcuCode, meterId, Constant.CURRENT_C_H_3P, "AlertConfig", alertValue);

			alertValue = this.getAlertValue(mpAlertConfList.getDatabyName("Cur1PhaseUnderAverage"), 100);
			alertValueList.add(dcuCode, meterId, Constant.CURRENT_A_L_3P, "AlertConfig", alertValue);
			alertValueList.add(dcuCode, meterId, Constant.CURRENT_B_L_3P, "AlertConfig", alertValue);
			alertValueList.add(dcuCode, meterId, Constant.CURRENT_C_L_3P, "AlertConfig", alertValue);

			iMT3Business.UpdateMeasurementPointAlertConfig(alertValueList);

			alertValueList = null;
			alertValue = null;
			mpAlertConfList = null;
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeMeasurementPointAlertConfig", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.model.meter.threephase.decode.IThreePhaseMessageDecode#
	 * decodeDcuinfo(java.lang.String, java.lang.String,
	 * saoviet.amisystem.model.datacollection.DataCollection,
	 * saoviet.amisystem.business.IMT3Business)
	 */
	@Override
	public void decodeDcuinfo(String dcuCode, String meterId, DataCollection dataList, IMT3Business iMT3Business) {
		try {
			DcuInforEntity dcuInfo = new DcuInforEntity();
			DataCollection dcuInforList = dataList.getDataCollectionByTagId(Constant.DCUINFO_TAG);

			dcuInfo.setDcuCode(dcuCode);
			dcuInfo.setDcuVerSion(new String(ConvertUlti.toByteArray(dcuInforList.getDatabyName("SvnVersion"))));
			dcuInfo.setCellIdConnect(new String(ConvertUlti.toByteArray(dcuInforList.getDatabyName("CellIdConnect"))));
			dcuInfo.setMcuTemperature(this.dataDivideValue(
					ConvertUlti.convertHexToDecimal(dcuInforList.getDatabyName("McuTemperature")), 10));
			dcuInfo.setRtcPinVotage(this
					.dataDivideValue(ConvertUlti.convertHexToDecimal(dcuInforList.getDatabyName("RtcPinVotage")), 10));
			dcuInfo.setSimTemperature(ConvertUlti.convertHexToDecimal(dcuInforList.getDatabyName("SimTemperature")));
			dcuInfo.setSingal(this.convertSingal(dcuInforList.getDatabyName("Singal")));
			dcuInfo.setSimSerial(new String(ConvertUlti.toByteArray(dcuInforList.getDatabyName("SimSerial"))));

			iMT3Business.UpdateDcuInfo(dcuInfo);

			dcuInfo = null;
			dcuInforList = null;

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeDcuinfo", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.model.meter.threephase.decode.IThreePhaseMessageDecode#
	 * decodeMeasurementPointAlertPacket(java.lang.String, java.lang.String,
	 * java.lang.String, saoviet.amisystem.model.datacollection.DataCollection,
	 * saoviet.amisystem.business.IMT3Business)
	 */
	@Override
	public void decodeMeasurementPointAlertPacket(String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		try {
			MT3MeterAlertCollection meterAlertList = new MT3MeterAlertCollection();
			ThreePhasePacketData.setMeasurementPointDefaultConfig(commandLine, dataCollection);

			if (dataCollection.getdataList().isEmpty()) {
				logUlti.writeLog(LogType.INFO, "ELSTER - DCU:" + dcuCode + " - METER:" + meterId
						+ " - decodeMeasurementPointAlertPacket: RETURN - dataCollection.getdataList().isEmpty()");
				return;
			}
			// Begin decode
			BigDecimal statusValue = null;
			BigDecimal valueAlert = null;
			meterAlertList.setServerTime(this.convertDateTime(dataCollection.getDatabyName("ServerTime")));
			meterAlertList.setMeterCode(meterId);
			meterAlertList.setDcuCode(dcuCode);

			// Volt A lower
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertVotPhaseAUnder"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("VotPhaseA"), 100);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_A_LOWER.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_A_LOWER.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}

			// Volt A Over
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertVotPhaseAOver"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("VotPhaseA"), 100);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_A_OVER.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_A_OVER.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}
			// CurrentAFail
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertCurrPhaseAFail"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("CurPhaseA"), 100);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.CURRENT_A_FAIL.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.CURRENT_A_FAIL.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}

			// IsAlertPowerFactorAUnder
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertPowerFactorAUnder"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("PowerFactorA"), 1000);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.POWER_FACTOR_A.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.POWER_FACTOR_A.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}

			// Volt B lower
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertVotPhaseBUnder"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("VotPhaseB"), 100);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_B_LOWER.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_B_LOWER.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}

			// Volt B Over
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertVotPhaseBOver"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("VotPhaseB"), 100);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_B_OVER.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_B_OVER.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}
			// CurrentBFail
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertCurrPhaseBFail"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("CurPhaseB"), 100);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.CURRENT_B_FAIL.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.CURRENT_B_FAIL.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}
			// IsAlertPowerFactorBUnder
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertPowerFactorBUnder"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("PowerFactorB"), 1000);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.POWER_FACTOR_B.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.POWER_FACTOR_B.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}

			// Volt C lower
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertVotPhaseCUnder"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("VotPhaseC"), 100);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_C_LOWER.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_C_LOWER.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}
			// Volt C Over
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertVotPhaseCOver"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("VotPhaseC"), 100);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_C_OVER.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_C_OVER.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}
			// CurrentCFail
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertCurrPhaseCFail"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("CurPhaseC"), 100);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.CURRENT_C_FAIL.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.CURRENT_C_FAIL.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}
			// IsAlertPowerFactorCUnder
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertPowerFactorCUnder"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("PowerFactorC"), 1000);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.POWER_FACTOR_C.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.POWER_FACTOR_C.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}

			// IsAlertFreqUnder
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertFreqUnder"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("Freq"), 100);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.FREQ_LOWER.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.FREQ_LOWER.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}
			// IsAlertFreqUnder
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertFreqOver"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("Freq"), 100);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.FREQ_OVER.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.FREQ_OVER.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}
			// IsAlertFreqUnder
			statusValue = this.getAlertValue(dataCollection.getDatabyName("IsAlertPowerFactorUnder"), 1);
			if (statusValue != null) {
				valueAlert = this.getAlertValue(dataCollection.getDatabyName("PowerFactor"), 1000);
				// Has a alert
				if (statusValue.intValue() == Constant.HAS_ALERT) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.POWER_FACTOR_LOWER.getValue(),
							valueAlert.toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.POWER_FACTOR_LOWER.getValue(),
							valueAlert.toString(), Constant.MP_END_ALERT);
				}
			}
			// End decode
			iMT3Business.insertMeterAlert(meterAlertList);

			meterAlertList = null;
			valueAlert = null;
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeMeasurementPointAlertPacket - DCU:" + dcuCode + " - METER: "
					+ meterId + " - DATA: " + commandLine, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.model.meter.threephase.decode.IThreePhaseMessageDecode#
	 * decodeEventPacket(java.lang.String, java.lang.String, java.lang.String,
	 * saoviet.amisystem.model.datacollection.DataCollection,
	 * saoviet.amisystem.business.IMT3Business)
	 */
	@Override
	public void decodeEventPacket(String dcuCode, String meterId, String commandLine, DataCollection dataCollection,
			IMT3Business iMT3Business) {
		MT3EventEntity event = new MT3EventEntity();
		try {
			MT3EventMeterCollection eventMeterList = new MT3EventMeterCollection();
			// Get event data
			ThreePhasePacketData.setEventPacketData(commandLine, dataCollection);
			event.setDcuCode(dcuCode);

			DataField eventValueFullList = new DataField();

			eventValueFullList = dataCollection.getDataFieldByObisName("ServerTime");
			event.setServerTime(this.convertDateTime(this.getEventValue("0", eventValueFullList)));
			eventValueFullList = dataCollection.getDataFieldByObisName("EventPowerFailureCount");
			event.setEventPowerFailureCount(
					ConvertUlti.convertHexToDecimal(this.getEventValue("0", eventValueFullList)));
			if (event.getEventPowerFailureCount() != null
					&& event.getEventPowerFailureCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventValueFullList = dataCollection.getDataFieldByObisName("EventPowerFailureTimeStart");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventPowerFailureFirst(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventPowerFailureSecond(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventPowerFailureThird(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventPowerFailureFourth(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventPowerFailureFifth(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventValueFullList = dataCollection.getDataFieldByObisName("EventPowerFailureTimeEnd");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventPowerRecoverFirst(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventPowerRecoverSecond(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventPowerRecoverThird(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventPowerRecoverFourth(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventPowerRecoverFifth(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTPOWERFAILURE,
						event.getEventPowerFailureCount(), null, event.getEventPowerFailureFirst(),
						event.getEventPowerFailureSecond(), event.getEventPowerFailureThird(),
						event.getEventPowerFailureFourth(), event.getEventPowerFailureFifth(),
						event.getEventPowerRecoverFirst(), event.getEventPowerRecoverSecond(),
						event.getEventPowerRecoverThird(), event.getEventPowerRecoverFourth(),
						event.getEventPowerRecoverFifth(), "3P");
			}

			eventValueFullList = dataCollection.getDataFieldByObisName("EventProgrammingCount");
			event.setEventProgrammingCount(
					ConvertUlti.convertHexToDecimal(this.getEventValue("0", eventValueFullList)));
			if (event.getEventProgrammingCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventValueFullList = dataCollection.getDataFieldByObisName("EventProgrammingTime");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventProgrammingFirst(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventProgrammingSecond(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventProgrammingThird(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventProgrammingFourth(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventProgrammingFifth(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTPROGRAMMING,
						event.getEventProgrammingCount(), null, event.getEventProgrammingFirst(),
						event.getEventProgrammingSecond(), event.getEventProgrammingThird(),
						event.getEventProgrammingFourth(), event.getEventProgrammingFifth(), null, null, null, null,
						null, "3P");
			}

			eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseRunCountPhaseA");
			event.setEventReverseRunPhaseACount(
					ConvertUlti.convertHexToDecimal(this.getEventValue("0", eventValueFullList)));
			if (event.getEventReverseRunPhaseACount() != null
					&& event.getEventReverseRunPhaseACount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseRunTimeAStart");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventReverseRunPhaseAFirstStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventReverseRunPhaseASecondStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventReverseRunPhaseAThirdStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventReverseRunPhaseAFourthStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventReverseRunPhaseAFifthStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseRunTimeAEnd");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventReverseRunPhaseAFirstEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventReverseRunPhaseASecondEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventReverseRunPhaseAThirdEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventReverseRunPhaseAFourthEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventReverseRunPhaseAFifthEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}

				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTREVERSERUNPHASEA,
						event.getEventReverseRunPhaseACount(), null, event.getEventReverseRunPhaseAFirstStart(),
						event.getEventReverseRunPhaseASecondStart(), event.getEventReverseRunPhaseAThirdStart(),
						event.getEventReverseRunPhaseAFourthStart(), event.getEventReverseRunPhaseAFifthStart(),
						event.getEventReverseRunPhaseAFirstEnd(), event.getEventReverseRunPhaseASecondEnd(),
						event.getEventReverseRunPhaseAThirdEnd(), event.getEventReverseRunPhaseAFourthEnd(),
						event.getEventReverseRunPhaseAFifthEnd(), "3P");
			}
			eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseRunCountPhaseB");
			event.setEventReverseRunPhaseBCount(
					ConvertUlti.convertHexToDecimal(this.getEventValue("0", eventValueFullList)));
			if (event.getEventReverseRunPhaseBCount() != null
					&& event.getEventReverseRunPhaseBCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseRunTimeBStart");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventReverseRunPhaseBFirstStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventReverseRunPhaseBSecondStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventReverseRunPhaseBThirdStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventReverseRunPhaseBFourthStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventReverseRunPhaseBFifthStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseRunTimeBEnd");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventReverseRunPhaseBFirstEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventReverseRunPhaseBSecondEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventReverseRunPhaseBThirdEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventReverseRunPhaseBFourthEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventReverseRunPhaseBFifthEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}

				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTREVERSERUNPHASEB,
						event.getEventReverseRunPhaseBCount(), null, event.getEventReverseRunPhaseBFirstStart(),
						event.getEventReverseRunPhaseBSecondStart(), event.getEventReverseRunPhaseBThirdStart(),
						event.getEventReverseRunPhaseBFourthStart(), event.getEventReverseRunPhaseBFifthStart(),
						event.getEventReverseRunPhaseBFirstEnd(), event.getEventReverseRunPhaseBSecondEnd(),
						event.getEventReverseRunPhaseBThirdEnd(), event.getEventReverseRunPhaseBFourthEnd(),
						event.getEventReverseRunPhaseBFifthEnd(), "3P");
			}
			eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseRunCountPhaseC");
			event.setEventReverseRunPhaseCCount(
					ConvertUlti.convertHexToDecimal(this.getEventValue("0", eventValueFullList)));
			if (event.getEventReverseRunPhaseCCount() != null
					&& event.getEventReverseRunPhaseCCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseRunTimeCStart");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventReverseRunPhaseCFirstStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventReverseRunPhaseCSecondStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventReverseRunPhaseCThirdStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventReverseRunPhaseCFourthStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventReverseRunPhaseCFifthStart(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseRunTimeCEnd");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventReverseRunPhaseCFirstEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventReverseRunPhaseCSecondEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventReverseRunPhaseCThirdEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventReverseRunPhaseCFourthEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventReverseRunPhaseCFifthEnd(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTREVERSERUNPHASEC,
						event.getEventReverseRunPhaseCCount(), null, event.getEventReverseRunPhaseCFirstStart(),
						event.getEventReverseRunPhaseCSecondStart(), event.getEventReverseRunPhaseCThirdStart(),
						event.getEventReverseRunPhaseCFourthStart(), event.getEventReverseRunPhaseCFifthStart(),
						event.getEventReverseRunPhaseCFirstEnd(), event.getEventReverseRunPhaseCSecondEnd(),
						event.getEventReverseRunPhaseCThirdEnd(), event.getEventReverseRunPhaseCFourthEnd(),
						event.getEventReverseRunPhaseCFifthEnd(), "3P");
			}
			eventValueFullList = dataCollection.getDataFieldByObisName("EventPhaseFailureCountPhaseA");
			event.setEventFailurePhaseACount(
					ConvertUlti.convertHexToDecimal(this.getEventValue("0", eventValueFullList)));
			if (event.getEventFailurePhaseACount() != null
					&& event.getEventFailurePhaseACount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventValueFullList = dataCollection.getDataFieldByObisName("EventPhaseAFailureTimeStart");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventFailurePhaseAFirst(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventFailurePhaseASecond(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventFailurePhaseAThird(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventFailurePhaseAFourth(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventFailurePhaseAFifth(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTFAILUREPHASEA,
						event.getEventFailurePhaseACount(), null, event.getEventFailurePhaseAFirst(),
						event.getEventFailurePhaseASecond(), event.getEventFailurePhaseAThird(),
						event.getEventFailurePhaseAFourth(), event.getEventFailurePhaseAFifth(), null, null, null, null,
						null, "3P");
			}
			eventValueFullList = dataCollection.getDataFieldByObisName("EventPhaseFailureCountPhaseB");
			event.setEventFailurePhaseBCount(
					ConvertUlti.convertHexToDecimal(this.getEventValue("0", eventValueFullList)));
			if (event.getEventFailurePhaseBCount() != null
					&& event.getEventFailurePhaseBCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventValueFullList = dataCollection.getDataFieldByObisName("EventPhaseAFailureTimeStart");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventFailurePhaseBFirst(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventFailurePhaseBSecond(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventFailurePhaseBThird(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventFailurePhaseBFourth(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventFailurePhaseBFifth(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTFAILUREPHASEB,
						event.getEventFailurePhaseBCount(), null, event.getEventFailurePhaseBFirst(),
						event.getEventFailurePhaseBSecond(), event.getEventFailurePhaseBThird(),
						event.getEventFailurePhaseBFourth(), event.getEventFailurePhaseBFifth(), null, null, null, null,
						null, "3P");
			}

			eventValueFullList = dataCollection.getDataFieldByObisName("EventPhaseFailureCountPhaseC");
			event.setEventFailurePhaseCCount(
					ConvertUlti.convertHexToDecimal(this.getEventValue("0", eventValueFullList)));

			if (event.getEventFailurePhaseCCount() != null
					&& event.getEventFailurePhaseCCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventValueFullList = dataCollection.getDataFieldByObisName("EventPhaseCFailureTimeStart");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventFailurePhaseCFirst(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventFailurePhaseCSecond(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventFailurePhaseCThird(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventFailurePhaseCFourth(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventFailurePhaseCFifth(
							this.convertTimeMaxDemand(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTFAILUREPHASEC,
						event.getEventFailurePhaseCCount(), null, event.getEventFailurePhaseCFirst(),
						event.getEventFailurePhaseCSecond(), event.getEventFailurePhaseCThird(),
						event.getEventFailurePhaseCFourth(), event.getEventFailurePhaseCFifth(), null, null, null, null,
						null, "3P");
			}
			if (eventMeterList.getEventMeterList().size() > 0) {
				iMT3Business.insertEvent(eventMeterList);
			}

			eventMeterList = null;
			eventValueFullList = null;
			event = null;

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeEventPacket", ex);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.model.meter.threephase.decode.IThreePhaseMessageDecode#
	 * decodeOperationPacket(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String,
	 * saoviet.amisystem.model.datacollection.DataCollection,
	 * saoviet.amisystem.business.IMT3Business)
	 */
	public void decodeOperationPacket(String topic, String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		MT3OperationEntity op = new MT3OperationEntity();
		DataCollection scaleList = new DataCollection();
		try {
			op.setTopic(topic);
			iMT3Business.getMeterObisScale(meterId, null, scaleList);
			if (scaleList.getdataList().isEmpty()) {
				// 2 Not in DB
				this.save.UpdateMessageLog(topic, 2);
				logUlti.writeLog(LogType.INFO, "ELSTER - DCU:" + dcuCode + " - METER:" + meterId
						+ " - decodeOperationPacket: RETURN - scaleList.getdataList().isEmpty()");
				return;
			}
			// Get Operation data
			ThreePhasePacketData.setMeasurementPointDefaultConfig(commandLine, dataCollection);
			op.setMeterId(meterId);
			op.setDcuCode(dcuCode);
			op.setServerTime(this.convertDateTime(dataCollection.getDatabyName("ServerTime")));
			op.setMeterTime(this.convertMeterTime(dataCollection.getDatabyName("MeterTime")));
			op.setVoltagePhaseA(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("VoltagePhaseA"),
					scaleList, "VoltagePhaseA"));
			op.setVoltagePhaseB(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("VoltagePhaseB"),
					scaleList, "VoltagePhaseB"));
			op.setVoltagePhaseC(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("VoltagePhaseC"),
					scaleList, "VoltagePhaseC"));
			op.setCurrentPhaseA(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("CurrentPhaseA"),
					scaleList, "CurrentPhaseA"));
			op.setCurrentPhaseB(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("CurrentPhaseB"),
					scaleList, "CurrentPhaseB"));
			op.setCurrentPhaseC(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("CurrentPhaseC"),
					scaleList, "CurrentPhaseC"));

			BigDecimal freqA = this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("FrequencyPhaseA"),
					scaleList, "FrequencyPhaseA");
			BigDecimal freqB = this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("FrequencyPhaseB"),
					scaleList, "FrequencyPhaseB");
			BigDecimal freqC = this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("FrequencyPhaseC"),
					scaleList, "FrequencyPhaseC");
			BigDecimal avg = BigDecimal.valueOf(1);

			if (freqA != null && freqB != null && freqC != null) {
				avg = BigDecimal.valueOf(3);
				op.setFrequency((freqA.add(freqB).add(freqC)).divide(avg));
			} else if (freqA != null && freqB != null) {
				avg = BigDecimal.valueOf(2);
				op.setFrequency((freqA.add(freqB)).divide(avg));
			} else if (freqB != null && freqC != null) {
				avg = BigDecimal.valueOf(2);
				op.setFrequency((freqC.add(freqB)).divide(avg));
			} else if (freqA != null && freqC != null) {
				avg = BigDecimal.valueOf(2);
				op.setFrequency((freqA.add(freqC)).divide(avg));
			} else if (freqA != null)
				op.setFrequency((freqA).divide(avg));
			op.setActivePowerPhaseA(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ActivePowerPhaseA"), scaleList, "ActivePowerPhaseA"));
			op.setActivePowerPhaseB(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ActivePowerPhaseB"), scaleList, "ActivePowerPhaseB"));
			op.setActivePowerPhaseC(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ActivePowerPhaseC"), scaleList, "ActivePowerPhaseC"));
			op.setActivePowerPhaseTotal(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ActivePowerPhaseTotal"), scaleList, "ActivePowerPhaseTotal"));
			op.setReactivePowerPhaseA(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseA"), scaleList, "ReactivePowerPhaseA"));
			op.setReactivePowerPhaseB(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseB"), scaleList, "ReactivePowerPhaseB"));
			op.setReactivePowerPhaseC(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseC"), scaleList, "ReactivePowerPhaseC"));
			op.setReactivePowerPhaseTotal(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseTotal"), scaleList, "ReactivePowerPhaseTotal"));
			op.setPowerFactorPhaseA(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("PowerFactorPhaseA"), scaleList, "PowerFactorPhaseA"));
			if (op.getPowerFactorPhaseA() != null && op.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseA(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseA().doubleValue()) * (180 / Math.PI)));
			op.setPowerFactorPhaseB(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("PowerFactorPhaseB"), scaleList, "PowerFactorPhaseB"));
			if (op.getPowerFactorPhaseB() != null && op.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseB(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseB().doubleValue()) * (180 / Math.PI)));

			op.setPowerFactorPhaseC(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("PowerFactorPhaseC"), scaleList, "PowerFactorPhaseC"));
			if (op.getPowerFactorPhaseC() != null && op.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseC(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseC().doubleValue()) * (180 / Math.PI)));

			op.setPowerFactorPhaseTotal(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("PowerFactorPhaseTotal"), scaleList, "PowerFactorPhaseTotal"));

			op.setEnergyPlusArate1(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergyPlusArate1"), scaleList, "EnergyPlusArate1"), 1000));
			op.setEnergyPlusArate2(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergyPlusArate2"), scaleList, "EnergyPlusArate2"), 1000));
			op.setEnergyPlusArate3(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergyPlusArate3"), scaleList, "EnergyPlusArate3"), 1000));
			op.setEnergySubArate1(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergySubArate1"), scaleList, "EnergySubArate1"), 1000));
			op.setEnergySubArate2(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergySubArate2"), scaleList, "EnergySubArate2"), 1000));
			op.setEnergySubArate3(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergySubArate3"), scaleList, "EnergySubArate3"), 1000));
			op.setExportWh(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ExportWh"), scaleList, "ExportWh"), 1000));
			op.setImportWh(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ImportWh"), scaleList, "ImportWh"), 1000));
			op.setQ1(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("Q1"), scaleList, "Q1"));
			op.setQ2(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("Q2"), scaleList, "Q2"));
			if (op.getQ2() != null && op.getQ1() != null)
				op.setQ1((op.getQ1().add(op.getQ2())).divide(BigDecimal.valueOf(1000)));
			else if (op.getQ1() != null)
				op.setQ1(op.getQ1().divide(BigDecimal.valueOf(1000)));
			op.setQ3(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("Q3"), scaleList, "Q3"));
			op.setQ4(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("Q4"), scaleList, "Q4"));
			if (op.getQ4() != null && op.getQ3() != null)
				op.setQ3((op.getQ3().add(op.getQ4())).divide(BigDecimal.valueOf(1000)));
			else if (op.getQ4() != null)
				op.setQ4(op.getQ4().divide(BigDecimal.valueOf(1000)));
			op.setMaxDemandPlusArate1(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate1"), scaleList, "MaxDemandPlusArate1"));
			op.setMaxDemandPlusArate1Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandPlusArate1Time")));
			op.setMaxDemandSubArate1(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandSubArate1"), scaleList, "MaxDemandSubArate1"));
			op.setMaxDemandSubArate1Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandSubArate1Time")));
			op.setMaxDemandPlusArate2(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate2"), scaleList, "MaxDemandPlusArate2"));
			op.setMaxDemandPlusArate2Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandPlusArate2Time")));
			op.setMaxDemandSubArate2(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandSubArate2"), scaleList, "MaxDemandSubArate2"));
			op.setMaxDemandSubArate2Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandSubArate2Time")));
			op.setMaxDemandPlusArate3(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate3"), scaleList, "MaxDemandPlusArate3"));
			op.setMaxDemandPlusArate3Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandPlusArate3Time")));
			op.setMaxDemandSubArate3(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandSubArate3"), scaleList, "MaxDemandSubArate3"));
			op.setMaxDemandSubArate3Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandSubArate3Time")));
			op.setTiT(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("Ti")));
			op.setTiM(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("TiM")));
			op.setTi(op.getTiM() == BigDecimal.valueOf(0) && (op.getTiM() == null) ? op.getTiT()
					: op.getTiT().divide(op.getTiM(), 2, RoundingMode.CEILING));

			op.setTuT(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("Tu")));
			op.setTuM(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("TuM")));
			op.setTu(op.getTuM() == BigDecimal.valueOf(0) && (op.getTuM() == null) ? op.getTuT()
					: op.getTuT().divide(op.getTuM(), 2, RoundingMode.CEILING));
			op.setReactiveEnergyPlusArate1(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate1"), scaleList, "ReactiveEnergyPlusArate1"));
			op.setReactiveEnergyPlusArate2(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate2"), scaleList, "ReactiveEnergyPlusArate2"));
			op.setReactiveEnergyPlusArate3(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate3"), scaleList, "ReactiveEnergyPlusArate3"));
			op.setReactiveEnergySubArate1(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate1"), scaleList, "ReactiveEnergySubArate1"));
			op.setReactiveEnergySubArate2(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate2"), scaleList, "ReactiveEnergySubArate2"));
			op.setReactiveEnergySubArate3(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate3"), scaleList, "ReactiveEnergySubArate3"));

			op.setApparentPowerPhaseTotal(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ApparentPowerPhaseTotal"), scaleList, "ApparentPowerPhaseTotal"));

			iMT3Business.addMessageOperation(op);

			scaleList = null;
			op = null;

		} catch (Exception ex) {
			// -1 error decode
			this.save.UpdateMessageLog(topic, -1);
			logUlti.writeLog(LogType.ERROR, "decodeOpearationPacket", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.model.meter.threephase.decode.IThreePhaseMessageDecode#
	 * decodeInstantaneousPacket(java.lang.String, java.lang.String,
	 * java.lang.String, saoviet.amisystem.model.datacollection.DataCollection,
	 * saoviet.amisystem.business.IMT3Business)
	 */
	public void decodeInstantaneousPacket(String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		MT3OperationEntity ins = new MT3OperationEntity();
		try {
			// Get Operation data
			ThreePhasePacketData.setMeasurementPointDefaultConfig(commandLine, dataCollection);

			DataCollection scaleList = new DataCollection();
			iMT3Business.getMeterObisScale(meterId, null, scaleList);
			if (scaleList.getdataList().isEmpty()) {
				logUlti.writeLog(LogType.INFO, "ELSTER - DCU:" + dcuCode + " - METER:" + meterId
						+ " - decodeInstantaneousPacket: RETURN - scaleList.getdataList().isEmpty()");
				return;

			}
			ins.setMeterId(meterId);
			ins.setDcuCode(dcuCode);
			ins.setServerTime(this.convertDateTime(dataCollection.getDatabyName("ServerTime")));
			ins.setMeterTime(this.convertMeterTime(dataCollection.getDatabyName("MeterTime")));
			ins.setVoltagePhaseA(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("VoltagePhaseA"),
					scaleList, "VoltagePhaseA"));
			ins.setVoltagePhaseB(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("VoltagePhaseB"),
					scaleList, "VoltagePhaseB"));
			ins.setVoltagePhaseC(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("VoltagePhaseC"),
					scaleList, "VoltagePhaseC"));
			ins.setCurrentPhaseA(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("CurrentPhaseA"),
					scaleList, "CurrentPhaseA"));
			ins.setCurrentPhaseB(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("CurrentPhaseB"),
					scaleList, "CurrentPhaseB"));
			ins.setCurrentPhaseC(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("CurrentPhaseC"),
					scaleList, "CurrentPhaseC"));

			BigDecimal freqA = this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("FrequencyPhaseA"),
					scaleList, "FrequencyPhaseA");
			BigDecimal freqB = this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("FrequencyPhaseB"),
					scaleList, "FrequencyPhaseB");
			BigDecimal freqC = this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("FrequencyPhaseC"),
					scaleList, "FrequencyPhaseC");
			BigDecimal avg = BigDecimal.valueOf(1);

			if (freqA != null && freqB != null && freqC != null) {
				avg = BigDecimal.valueOf(3);
				ins.setFrequency((freqA.add(freqB).add(freqC)).divide(avg));
			} else if (freqA != null && freqB != null) {
				avg = BigDecimal.valueOf(2);
				ins.setFrequency((freqA.add(freqB)).divide(avg));
			} else if (freqB != null && freqC != null) {

				avg = BigDecimal.valueOf(2);
				ins.setFrequency((freqC.add(freqB)).divide(avg));
			} else if (freqA != null && freqC != null) {
				avg = BigDecimal.valueOf(2);
				ins.setFrequency((freqA.add(freqC)).divide(avg));
			} else if (freqA != null)
				ins.setFrequency((freqA).divide(avg));
			ins.setActivePowerPhaseA(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ActivePowerPhaseA"), scaleList, "ActivePowerPhaseA"));
			ins.setActivePowerPhaseB(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ActivePowerPhaseB"), scaleList, "ActivePowerPhaseB"));
			ins.setActivePowerPhaseC(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ActivePowerPhaseC"), scaleList, "ActivePowerPhaseC"));
			ins.setActivePowerPhaseTotal(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ActivePowerPhaseTotal"), scaleList, "ActivePowerPhaseTotal"));
			ins.setReactivePowerPhaseA(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseA"), scaleList, "ReactivePowerPhaseA"));
			ins.setReactivePowerPhaseB(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseB"), scaleList, "ReactivePowerPhaseB"));
			ins.setReactivePowerPhaseC(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseC"), scaleList, "ReactivePowerPhaseC"));
			ins.setReactivePowerPhaseTotal(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseTotal"), scaleList, "ReactivePowerPhaseTotal"));
			ins.setPowerFactorPhaseA(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("PowerFactorPhaseA"), scaleList, "PowerFactorPhaseA"));
			if (ins.getPowerFactorPhaseA() != null && ins.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseA(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseA().doubleValue()) * (180 / Math.PI)));
			ins.setPowerFactorPhaseB(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("PowerFactorPhaseB"), scaleList, "PowerFactorPhaseB"));
			if (ins.getPowerFactorPhaseB() != null && ins.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseB(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseB().doubleValue()) * (180 / Math.PI)));

			ins.setPowerFactorPhaseC(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("PowerFactorPhaseC"), scaleList, "PowerFactorPhaseC"));
			if (ins.getPowerFactorPhaseC() != null && ins.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseC(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseC().doubleValue()) * (180 / Math.PI)));

			ins.setPowerFactorPhaseTotal(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("PowerFactorPhaseTotal"), scaleList, "PowerFactorPhaseTotal"));

			ins.setEnergyPlusArate1(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergyPlusArate1"), scaleList, "EnergyPlusArate1"), 1000));
			ins.setEnergyPlusArate2(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergyPlusArate2"), scaleList, "EnergyPlusArate2"), 1000));
			ins.setEnergyPlusArate3(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergyPlusArate3"), scaleList, "EnergyPlusArate3"), 1000));
			ins.setEnergySubArate1(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergySubArate1"), scaleList, "EnergySubArate1"), 1000));
			ins.setEnergySubArate2(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergySubArate2"), scaleList, "EnergySubArate2"), 1000));
			ins.setEnergySubArate3(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergySubArate3"), scaleList, "EnergySubArate3"), 1000));
			ins.setExportWh(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ExportWh"), scaleList, "ExportWh"), 1000));
			ins.setImportWh(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ImportWh"), scaleList, "ImportWh"), 1000));
			ins.setQ1(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("Q1"), scaleList, "Q1"));
			ins.setQ2(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("Q2"), scaleList, "Q2"));
			ins.setQ1((ins.getQ1().add(ins.getQ2())).divide(BigDecimal.valueOf(1000)));
			ins.setQ3(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("Q3"), scaleList, "Q3"));
			ins.setQ4(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("Q4"), scaleList, "Q4"));
			ins.setQ3((ins.getQ3().add(ins.getQ4())).divide(BigDecimal.valueOf(1000)));
			ins.setMaxDemandPlusArate1(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate1"), scaleList, "MaxDemandPlusArate1"));
			ins.setMaxDemandPlusArate1Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandPlusArate1Time")));
			ins.setMaxDemandSubArate1(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandSubArate1"), scaleList, "MaxDemandSubArate1"));
			ins.setMaxDemandSubArate1Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandSubArate1Time")));
			ins.setMaxDemandPlusArate2(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate2"), scaleList, "MaxDemandPlusArate2"));
			ins.setMaxDemandPlusArate2Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandPlusArate2Time")));
			ins.setMaxDemandSubArate2(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandSubArate2"), scaleList, "MaxDemandSubArate2"));
			ins.setMaxDemandSubArate2Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandSubArate2Time")));
			ins.setMaxDemandPlusArate3(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate3"), scaleList, "MaxDemandPlusArate3"));
			ins.setMaxDemandPlusArate3Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandPlusArate3Time")));
			ins.setMaxDemandSubArate3(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandSubArate3"), scaleList, "MaxDemandSubArate3"));
			ins.setMaxDemandSubArate3Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandSubArate3Time")));
			ins.setTiT(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("Ti")));
			ins.setTiM(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("TiM")));
			ins.setTi(ins.getTiM() == BigDecimal.valueOf(0) && (ins.getTiM() == null) ? ins.getTiT()
					: ins.getTiT().divide(ins.getTiM(), 2, RoundingMode.CEILING)); // làm
																					// tròn
																					// 2chữ
																					// số
																					// sau
																					// dấu
																					// phẩy
			ins.setTuT(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("Tu")));
			ins.setTuM(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("TuM")));
			ins.setTu(ins.getTuM() == BigDecimal.valueOf(0) && (ins.getTuM() == null) ? ins.getTuT()
					: ins.getTuT().divide(ins.getTuM(), 2, RoundingMode.CEILING));// làm
																					// tròn
																					// 2chữ
																					// số
																					// sau
																					// dấu
																					// phẩy
			ins.setReactiveEnergyPlusArate1(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate1"), scaleList, "ReactiveEnergyPlusArate1"));
			ins.setReactiveEnergyPlusArate2(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate2"), scaleList, "ReactiveEnergyPlusArate2"));
			ins.setReactiveEnergyPlusArate3(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate3"), scaleList, "ReactiveEnergyPlusArate3"));
			ins.setReactiveEnergySubArate1(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate1"), scaleList, "ReactiveEnergySubArate1"));
			ins.setReactiveEnergySubArate2(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate2"), scaleList, "ReactiveEnergySubArate2"));
			ins.setReactiveEnergySubArate3(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate3"), scaleList, "ReactiveEnergySubArate3"));

			ins.setApparentPowerPhaseTotal(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ApparentPowerPhaseTotal"), scaleList, "ApparentPowerPhaseTotal"));

			iMT3Business.insertIntantaneous(ins);
			ins = null;
			scaleList = null;

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeInstantaneousPacket", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.model.meter.threephase.decode.IThreePhaseMessageDecode#
	 * decodeHistoricalPacket(java.lang.String, java.lang.String,
	 * java.lang.String, saoviet.amisystem.model.datacollection.DataCollection,
	 * saoviet.amisystem.business.IMT3Business)
	 */
	@Override
	public void decodeHistoricalPacket(String topic, String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		// Auto-generated method stub
		MT3HistoricalEntity hi = new MT3HistoricalEntity();

		DataCollection scaleList = new DataCollection();

		try {
			iMT3Business.getMeterObisScale(meterId, null, scaleList);
			// Get historical data
			ThreePhasePacketData.setMeasurementPointDefaultConfig(commandLine, dataCollection);
			// Begin decode
			hi.setServerTime(this.convertDateTime(dataCollection.getDatabyName("ServerTime")));

			hi.setMeterTime(this.convertMeterTime(dataCollection.getDatabyName("MeterTime")));

			hi.setMeterId(meterId);
			hi.setDcuCode(dcuCode);

			hi.setBeginHistoricalTime(this.convertTimeMaxDemand(dataCollection.getDatabyName("BeginHistoricalTime")));

			if (hi.getBeginHistoricalTime() == null) {
				logUlti.writeLog(LogType.INFO, "ELSTER - DCU:" + dcuCode + " - METER:" + meterId
						+ " - decodeHistoricalPacket: RETURN - hi.getBeginHistoricalTime() == null");
				return;
			}

			if (hi.getHistoricalTime() == null) {
				Timestamp dt = hi.getBeginHistoricalTime();
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(dt.getTime());
				cal.add(Calendar.MONTH, 1);
				hi.setHistoricalTime(new Timestamp(cal.getTime().getTime()));
				hi.setEndHistoricalTime(hi.getHistoricalTime());
			}

			hi.setImportWh(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ImportWh"), scaleList, "ImportWh"), 1000));

			hi.setExportWh(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ExportWh"), scaleList, "ExportWh"), 1000));

			hi.setEnergyPlusARate1(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergyPlusArate1"), scaleList, "EnergyPlusArate1"), 1000));

			hi.setEnergyPlusARate2(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergyPlusArate2"), scaleList, "EnergyPlusArate2"), 1000));

			hi.setEnergyPlusARate3(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergyPlusArate3"), scaleList, "EnergyPlusArate3"), 1000));

			hi.setEnergySubARate1(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergySubArate1"), scaleList, "EnergySubArate1"), 1000));

			hi.setEnergySubARate2(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergySubArate2"), scaleList, "EnergySubArate2"), 1000));

			hi.setEnergySubARate3(this.dataDivideValue(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("EnergySubArate3"), scaleList, "EnergySubArate3"), 1000));

			hi.setQ1(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("Q1"), scaleList, "Q1"));
			hi.setQ2(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("Q2"), scaleList, "Q2"));
			hi.setQ1(hi.getQ1().add(hi.getQ2()));
			hi.setQ3(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("Q3"), scaleList, "Q3"));
			hi.setQ4(this.calculatorConvertHexToInt64IsNull(dataCollection.getDatabyName("Q4"), scaleList, "Q4"));
			hi.setQ3(hi.getQ3().add(hi.getQ4()));

			hi.setMaxDemandPlusArate1(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate1"), scaleList, "MaxDemandPlusArate1"));
			hi.setMaxDemandPlusArate1Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandPlusArate1Time")));

			hi.setMaxDemandPlusArate2(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate2"), scaleList, "MaxDemandPlusArate2"));
			hi.setMaxDemandPlusArate2Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandPlusArate2Time")));

			hi.setMaxDemandPlusArate3(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate3"), scaleList, "MaxDemandPlusArate3"));
			hi.setMaxDemandPlusArate3Time(
					this.convertTimeMaxDemand(dataCollection.getDatabyName("MaxDemandPlusArate3Time")));

			hi.setReactiveEnergyPlusArate1(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate1"), scaleList, "ReactiveEnergyPlusArate1"));
			hi.setReactiveEnergyPlusArate2(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate2"), scaleList, "ReactiveEnergyPlusArate2"));
			hi.setReactiveEnergyPlusArate3(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate3"), scaleList, "ReactiveEnergyPlusArate3"));
			hi.setReactiveEnergySubArate1(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate1"), scaleList, "ReactiveEnergySubArate1"));
			hi.setReactiveEnergySubArate2(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate2"), scaleList, "ReactiveEnergySubArate2"));
			hi.setReactiveEnergySubArate3(this.calculatorConvertHexToInt64IsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate3"), scaleList, "ReactiveEnergySubArate3"));
			// System.out.println("HISTORICAL");
			// End decode

			hi.setTopic(topic);

			if (hi.getHistoricalTime() != null)
				iMT3Business.insertHistorical(hi);

			scaleList = null;
			hi = null;

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeHistoricalPacket", ex);
		}
	}

	public void decodeLoadProfilePacket(String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {

		MT3LoadProfile3PMessageEntity load = new MT3LoadProfile3PMessageEntity();
		MT3LoadProfilePacketList loadProfileList = new MT3LoadProfilePacketList();
		try {

			DataCollection scaleList = new DataCollection();
			iMT3Business.getMeterObisScale(meterId, null, scaleList);
			if (scaleList.getdataList().isEmpty()) {
				logUlti.writeLog(LogType.INFO, "ELSTER - DCU:" + dcuCode + " - METER:" + meterId
						+ " - decodeLoadProfilePacket: RETURN - scaleList.getdataList().isEmpty()");
				return;

			}
			loadProfileList = ThreePhasePacketData.GetLoadProfileObisByName(commandLine, dataCollection);

			for (DataCollection profileList : loadProfileList.getLoadProfileList()) {
				load.setServerTime(this.convertDateTime(loadProfileList.getServerTimeValue()));
				load.setMeterTimeFrom(this.convertDateTimeNotSec(profileList.getDatabyName("MeterTime")));
				load.setPowerAvgPos(this.calculatorConvertHexToInt64IsNull(profileList.getDatabyName("PowerAvgPos"),
						scaleList, "ActivePowerPhaseA"));
				load.setPowerAvgNeg(this.calculatorConvertHexToInt64IsNull(profileList.getDatabyName("PowerAvgNeg"),
						scaleList, "ActivePowerPhaseA"));
				load.setReactivePowerAvgPos(this.calculatorConvertHexToInt64IsNull(
						profileList.getDatabyName("ReactivePowerAvgPos"), scaleList, "ActivePowerPhaseA"));
				load.setReactivePowerAvgNeg(this.calculatorConvertHexToInt64IsNull(
						profileList.getDatabyName("ReactivePowerAvgNeg"), scaleList, "ActivePowerPhaseA"));

				load.setDcuCode(dcuCode);
				load.setMeterCode(meterId);

				iMT3Business.insertLoadProfilePacket(load);

				loadProfileList = null;
				load = null;
			}

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeLoadProfilePacket", ex);
		}
	}

	/**
	 * fieldCode = 0: Vị trí đầu tiên của eventValueFullList fieldCode = 1: Vị
	 * trí thứ 2 của eventValueFullList fieldCode = 2: Vị trí thứ 3 của
	 * eventValueFullList fieldCode = 3: Vị trí thứ 4 của eventValueFullList
	 * fieldCode = 4: Vị trí thứ 5 của eventValueFullList
	 * 
	 * @param fieldCode
	 * @param eventValueFullList
	 * @return
	 */
	private String getEventValue(String fieldCode, DataField eventValueFullList) {
		// Auto-generated method stub
		DataField item = new DataField();
		int index = eventValueFullList.getEventList().indexOf(new DataField(StringUlti.PadLeft(fieldCode, 1, '0')));
		if (index > -1) {
			item = eventValueFullList.getEventList().get(index);
			return item.getData();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.model.meter.threephase.decode.IThreePhaseMessageDecode#
	 * ConvertUlti.convertHexToDecimal(java.lang.String)
	 */
	private BigDecimal getAlertValue(String dataByObisname, int value) {
		try {
			return ConvertUlti.convertHexToDecimal(dataByObisname).divide(BigDecimal.valueOf(value));
		} catch (Exception ex) {
		}
		return null;
	}

	private BigDecimal getValueBy10PercentOver(BigDecimal freqOver) {
		if (freqOver != null)
			return freqOver.multiply(BigDecimal.valueOf(0.91)); // 10/11
		return null;
	}

	private String convertSingal(String singalHex) {
		String result = null;
		try {
			result = ("-").concat(
					ConvertUlti.convertHexToDecimal(singalHex.substring(0, 2)) + " ," + singalHex.substring(2, 2));

		} catch (Exception ex) {
		}
		return result;
	}

	public BigDecimal calculatorConvertHexToInt64IsNull(String hexString, DataCollection scaleList, String filename) {
		BigDecimal value = BigDecimal.valueOf(1);

		try {
			// neu byte 2 la FF return 0
			if (hexString.substring(2, 4).equals("FF"))
				return BigDecimal.valueOf(0);
			if (hexString.substring(0, 2).equals("80"))
				value = BigDecimal.valueOf(-1);
			// bo 1 byte dau
			hexString = hexString.substring(2);

			// byte dau = 80 thi lay gia tri am, con k giai ma bt
			value = value.multiply(new BigDecimal(hexString)).multiply((scaleList.getScalebyName(filename)));
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR,
					"RETURN NULL - ELS1 - calculatorConvertHexToInt64IsNull: " + filename + " - value: " + hexString);
			return null;
		}

		return value;
	}

	private Timestamp convertDateTimeNotSec(String hexString) {
		try {
			int year = 100 + (ConvertUlti.convertHexToDecimal(hexString.substring(0, 2))).intValue();// 1900+100=2000
			int mm = ConvertUlti.convertHexToDecimal(hexString.substring(2, 4)).intValue() - 1; //// month
			//// 0-11
			int dd = ConvertUlti.convertHexToDecimal(hexString.substring(4, 6)).intValue();

			int hh = ConvertUlti.convertHexToDecimal(hexString.substring(6, 8)).intValue();
			int min = ConvertUlti.convertHexToDecimal(hexString.substring(8, 10)).intValue();
			int sc = 0;

			@SuppressWarnings("deprecation")
			Timestamp date = new Timestamp(year, mm, dd, hh, min, sc, 0);
			// String datetime = datetimeformat.format(date);
			return date;
		} catch (Exception ex) {
			return null;
		}
	}

	private Timestamp convertTimeMaxDemand(String sec) {
		try {
			if (sec == null || Integer.parseInt(sec, 16) == 0)
				return null;
			@SuppressWarnings("deprecation")
			Timestamp datetime = new Timestamp(70, 0, 1, 0, 0, 0, 0); // mốc
																		// 01/01/1970
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(datetime.getTime());
			cal.add(Calendar.SECOND, Integer.parseInt(sec, 16));
			datetime = null;
			return new Timestamp(cal.getTime().getTime());
		} catch (Exception ex) {
			return null;
		}
	}

	public Timestamp convertMeterTime(String hexString) {
		try {
			int year = 100 + Integer.parseInt(hexString.substring(0, 4));// 1900
																			// +
																			// 100
																			// =2000
			int mm = Integer.parseInt(hexString.substring(4, 6)) - 1; // month
																		// 0- 11
			int dd = Integer.parseInt(hexString.substring(6, 8));

			int hh = Integer.parseInt(hexString.substring(8, 10));
			int min = Integer.parseInt(hexString.substring(10, 12));
			int sc = Integer.parseInt(hexString.substring(12, 14));
			@SuppressWarnings("deprecation")
			Timestamp date = new Timestamp(year, mm, dd, hh, min, sc, 0);
			return date;
		} catch (Exception ex) {
			return null;
		}
	}

	public Timestamp convertDateTime(String hexString) {

		try {
			int year = 100 + (ConvertUlti.convertHexToDecimal(hexString.substring(0, 2))).intValue();// 1900+100=2000
			int mm = ConvertUlti.convertHexToDecimal(hexString.substring(2, 4)).intValue() - 1; //// month
			//// 0-11
			int dd = ConvertUlti.convertHexToDecimal(hexString.substring(4, 6)).intValue();

			int hh = ConvertUlti.convertHexToDecimal(hexString.substring(6, 8)).intValue();
			int min = ConvertUlti.convertHexToDecimal(hexString.substring(8, 10)).intValue();
			int sc = ConvertUlti.convertHexToDecimal(hexString.substring(10, 12)).intValue();

			@SuppressWarnings("deprecation")
			Timestamp date = new Timestamp(year, mm, dd, hh, min, sc, 0);
			// String datetime = datetimeformat.format(date);
			return date;
		} catch (Exception ex) {
			return null;
		}
	}

	private BigDecimal dataDivideValue(BigDecimal data, int value) {
		if (data == null)
			return null;
		return data.divide(BigDecimal.valueOf(value));
	}

}
