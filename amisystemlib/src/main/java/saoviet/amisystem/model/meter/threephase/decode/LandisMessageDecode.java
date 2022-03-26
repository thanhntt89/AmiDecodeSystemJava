/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: LandisMessageDecode.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.decode;

import java.math.BigDecimal;
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
import saoviet.amisystem.model.meter.threephase.entity.MT3MeterAlertCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3OperationEntity;
import saoviet.amisystem.model.meter.threephase.packet.LandisPacketStructure;
import saoviet.amisystem.model.meter.threephase.packet.ThreePhasePacketData;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.SaveMessageUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class LandisMessageDecode implements IThreePhaseMessageDecode {

	private DataCollection mepdvPacketList;
	private DataCollection historicalPacketList;
	private DataCollection operationPacketList;
	private DataCollection loadProfilePacketList;
	private DataCollection meterEventPacketList;
	private DataCollection dcuWarningPacketList;
	private IMT3Business iMT3Business;
	private LogUlti logUlti = new LogUlti(LandisMessageDecode.class);
	private SaveMessageUlti save = new SaveMessageUlti();
	private SystemEventCallback systemEventCallback;

	@Override
	public void setSystemEventCallBack(SystemEventCallback systemEventCallback) {
		// TODO Auto-generated method stub
		this.systemEventCallback = systemEventCallback;
		this.iMT3Business.setSystemEventCallback(this.systemEventCallback);
	}

	public LandisMessageDecode(IMT3Business inIMT3Business) {
		this.mepdvPacketList = LandisPacketStructure.getMEPDVCollection();

		this.dcuWarningPacketList = LandisPacketStructure.getMeterAlertCollection();

		this.historicalPacketList = LandisPacketStructure.getHistoricalCollection();

		this.operationPacketList = LandisPacketStructure.getOperationCollection();

		this.loadProfilePacketList = LandisPacketStructure.getLoadProfileCollection();

		this.meterEventPacketList = LandisPacketStructure.getEventCollection();

		this.iMT3Business = inIMT3Business;
	}

	@Override
	public void decodeMessage(MessageBase messagebase) {
		try {
			// Convert byte to string
			String commandLine = null;

			switch (messagebase.getMessageType()) {
			case Constant.SAO_VIET_MESSAGE_TYPE_MEPDV:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeMeasurementPointConfigPacket(messagebase.getDcuCode(), messagebase.getMeterCode(),
						Constant.METER_TYPE_LANDIS, commandLine, this.mepdvPacketList.copy(), this.iMT3Business);
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
			System.out.println("MEPDV_LANDIS ERROR: " + mpstatus + " - meterType:" + meterType + " - DCU:" + dcuCode
					+ " -METER:" + meterId + " - DATAERROR:" + ex);
			logUlti.writeLog(LogType.ERROR, "MePDV decode fail", ex);
		}
		// System.out.println("MEPDV_LANDIS STATUS: " + mpstatus + " -
		// meterType:" + meterType + " - DCU:" + dcuCode
		// + " -METER:" + meterId);
	}

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

			alertValue = null;
			alertValueList = null;
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeMeasurementPointAlertConfig", ex);
		}
	}

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

			dcuInforList = null;
			dcuInfo = null;
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeDcuinfo", ex);
		}
	}

	@Override
	public void decodeMeasurementPointAlertPacket(String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		try {
			MT3MeterAlertCollection meterAlertList = new MT3MeterAlertCollection();
			ThreePhasePacketData.setMeasurementPointDefaultConfig(commandLine, dataCollection);
			if (dataCollection.getdataList().isEmpty()) {
				logUlti.writeLog(LogType.INFO, "LAND - DCU:" + dcuCode + " - METER:" + meterId
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

			statusValue = null;
			valueAlert = null;
			meterAlertList = null;
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeMeasurementPointAlertPacket", ex);
		}
	}

	@Override
	public void decodeEventPacket(String dcuCode, String meterId, String commandLine, DataCollection dataCollection,
			IMT3Business iMT3Business) {
		MT3EventEntity event = new MT3EventEntity();
		try {
			MT3EventMeterCollection eventMeterList = new MT3EventMeterCollection();
			// Get event data
			ThreePhasePacketData.setMeasurementPointDefaultConfig(commandLine, dataCollection);
			event.setDcuCode(dcuCode);

			event.setServerTime(this.convertDateTime(dataCollection.getDatabyName("ServerTime")));
			event.setEventProgrammingCount(
					ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("EventProgrammingCount")));
			if (event.getEventProgrammingCount() != null
					&& event.getEventProgrammingCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTPROGRAMMING,
						event.getEventProgrammingCount(), null, null, null, null, null, null, null, null, null, null,
						null, "3P");
			}

			event.setEventPowerFailureCount(ConvertUlti.convertHexToDecimal(
					dataCollection.getDatabyName("EventNumberOfPowerFailuresObjectsInAllThreePhases")));
			if (event.getEventPowerFailureCount() != null
					&& event.getEventPowerFailureCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTPOWERFAILURE,
						event.getEventPowerFailureCount(), null, null, null, null, null, null, null, null, null, null,
						null, "3P");
			}
			event.setEventFailurePhaseACount(ConvertUlti
					.convertHexToDecimal(dataCollection.getDatabyName("NumberOfPowerFailuresObjectsInPhaseL1")));
			if (event.getEventFailurePhaseACount() != null
					&& event.getEventFailurePhaseACount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTFAILUREPHASEA,
						event.getEventFailurePhaseACount(), null, null, null, null, null, null, null, null, null, null,
						null, "3P");
			}

			event.setEventFailurePhaseBCount(ConvertUlti
					.convertHexToDecimal(dataCollection.getDatabyName("NumberOfPowerFailuresObjectsInPhaseL2")));
			if (event.getEventFailurePhaseBCount() != null
					&& event.getEventFailurePhaseBCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTFAILUREPHASEB,
						event.getEventFailurePhaseBCount(), null, null, null, null, null, null, null, null, null, null,
						null, "3P");
			}
			event.setEventFailurePhaseCCount(ConvertUlti
					.convertHexToDecimal(dataCollection.getDatabyName("NumberOfPowerFailuresObjectsInPhaseL3")));
			if (event.getEventFailurePhaseCCount() != null
					&& event.getEventFailurePhaseCCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTFAILUREPHASEA,
						event.getEventFailurePhaseCCount(), null, null, null, null, null, null, null, null, null, null,
						null, "3P");
			}
			if (eventMeterList.getEventMeterList().size() > 0) {
				iMT3Business.insertEvent(eventMeterList);
			}

			event = null;
			eventMeterList = null;
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeEventPacket", ex);
		}
	}

	public void decodeOperationPacket(String topic, String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		MT3OperationEntity op = new MT3OperationEntity();
		DataCollection scaleList = new DataCollection();
		try {
			iMT3Business.getMeterObisScale(meterId, null, scaleList);
			if (scaleList.getdataList().isEmpty()) {
				// 2 Not in DB
				this.save.UpdateMessageLog(topic, 2);
				logUlti.writeLog(LogType.INFO, "LAND - DCU:" + dcuCode + " - METER:" + meterId
						+ " - decodeOperationPacket: RETURN - scaleList.getdataList().isEmpty()");
				return;
			}
			// Get Operation data
			ThreePhasePacketData.setMeasurementPointDefaultConfig(commandLine, dataCollection);
			op.setTopic(topic);
			op.setMeterId(meterId);
			op.setDcuCode(dcuCode);
			op.setServerTime(this.convertDateTime(dataCollection.getDatabyName("ServerTime")));
			op.setMeterTime(this.convertMeterTime(dataCollection.getDatabyName("MeterTime")));
			op.setVoltagePhaseA(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("VoltagePhaseA"),
					scaleList, "VoltagePhaseA"));
			op.setVoltagePhaseB(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("VoltagePhaseB"),
					scaleList, "VoltagePhaseB"));
			op.setVoltagePhaseC(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("VoltagePhaseC"),
					scaleList, "VoltagePhaseC"));
			op.setCurrentPhaseA(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("CurrentPhaseA"),
					scaleList, "CurrentPhaseA"));
			op.setCurrentPhaseB(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("CurrentPhaseB"),
					scaleList, "CurrentPhaseB"));
			op.setCurrentPhaseC(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("CurrentPhaseC"),
					scaleList, "CurrentPhaseC"));
			op.setFrequency(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Frequency"), scaleList,
					"Frequency"));

			op.setPowerFactorPhaseA(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("PowerFactorPhaseA"), scaleList, "PowerFactorPhaseA"));
			if (op.getPowerFactorPhaseA() != null && op.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseA(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseA().doubleValue()) * (180 / Math.PI)));
			op.setPowerFactorPhaseB(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("PowerFactorPhaseB"), scaleList, "PowerFactorPhaseB"));
			if (op.getPowerFactorPhaseB() != null && op.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseB(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseB().doubleValue()) * (180 / Math.PI)));
			op.setPowerFactorPhaseC(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("PowerFactorPhaseC"), scaleList, "PowerFactorPhaseC"));
			if (op.getPowerFactorPhaseC() != null && op.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseC(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseC().doubleValue()) * (180 / Math.PI)));

			op.setActivePowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ActivePowerPhaseTotal"), scaleList, "ActivePowerPhaseTotal"));
			op.setActivePowerPhaseA(this.dataDivideValue(
					op.getVoltagePhaseA().multiply(op.getCurrentPhaseA().multiply(op.getPowerFactorPhaseA())), 1000));
			op.setActivePowerPhaseB(this.dataDivideValue(
					op.getVoltagePhaseB().multiply(op.getCurrentPhaseB().multiply(op.getPowerFactorPhaseB())), 1000));
			op.setActivePowerPhaseC(this.dataDivideValue(
					op.getVoltagePhaseC().multiply(op.getCurrentPhaseC().multiply(op.getPowerFactorPhaseC())), 1000));

			if (op.getPhaseAnglePhaseA() != null)
				op.setReactivePowerPhaseA(
						this.dataDivideValue(
								op.getVoltagePhaseA()
										.multiply(op.getCurrentPhaseA()
												.multiply(BigDecimal
														.valueOf(Math.sin(op.getPhaseAnglePhaseA().doubleValue())))),
								1000));
			if (op.getPhaseAnglePhaseB() != null)
				op.setReactivePowerPhaseB(
						this.dataDivideValue(
								op.getVoltagePhaseB()
										.multiply(op.getCurrentPhaseB()
												.multiply(BigDecimal
														.valueOf(Math.sin(op.getPhaseAnglePhaseB().doubleValue())))),
								1000));
			if (op.getPhaseAnglePhaseC() != null)
				op.setReactivePowerPhaseC(
						this.dataDivideValue(
								op.getVoltagePhaseC()
										.multiply(op.getCurrentPhaseC()
												.multiply(BigDecimal
														.valueOf(Math.sin(op.getPhaseAnglePhaseC().doubleValue())))),
								1000));

			op.setReactivePowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseTotal"), scaleList, "ReactivePowerPhaseTotal"));
			op.setPowerFactorPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("PowerFactorPhaseTotal"), scaleList, "PowerFactorPhaseTotal"));

			op.setEnergyPlusArate1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate1"), scaleList, "EnergyPlusArate1"), 1000));
			op.setEnergyPlusArate2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate2"), scaleList, "EnergyPlusArate2"), 1000));
			op.setEnergyPlusArate3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate3"), scaleList, "EnergyPlusArate3"), 1000));
			op.setEnergySubArate1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergySubArate1"), scaleList, "EnergySubArate1"), 1000));
			op.setEnergySubArate2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergySubArate2"), scaleList, "EnergySubArate2"), 1000));
			op.setEnergySubArate3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergySubArate3"), scaleList, "EnergySubArate3"), 1000));
			op.setExportWh(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ExportWh"), scaleList, "ExportWh"), 1000));
			op.setImportWh(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ImportWh"), scaleList, "ImportWh"), 1000));
			op.setQ1(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Q1"), scaleList, "Q1"));
			op.setQ3(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Q3"), scaleList, "Q3"));
			op.setMaxDemandPlusArate1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate1"), scaleList, "MaxDemandPlusArate1"), 1000));
			op.setMaxDemandPlusArate1Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate1Time")));
			op.setMaxDemandSubArate1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate1"), scaleList, "MaxDemandSubArate1"), 1000));
			op.setMaxDemandSubArate1Time(this.convertMeterTime(dataCollection.getDatabyName("MaxDemandSubArate1Time")));
			op.setMaxDemandPlusArate2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate2"), scaleList, "MaxDemandPlusArate2"), 1000));
			op.setMaxDemandPlusArate2Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate2Time")));
			op.setMaxDemandSubArate2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate2"), scaleList, "MaxDemandSubArate2"), 1000));
			op.setMaxDemandSubArate2Time(this.convertMeterTime(dataCollection.getDatabyName("MaxDemandSubArate2Time")));
			op.setMaxDemandPlusArate3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate3"), scaleList, "MaxDemandPlusArate3"), 1000));
			op.setMaxDemandPlusArate3Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate3Time")));
			op.setMaxDemandSubArate3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate3"), scaleList, "MaxDemandSubArate3"), 1000));
			op.setMaxDemandSubArate3Time(this.convertMeterTime(dataCollection.getDatabyName("MaxDemandSubArate3Time")));
			op.setTi(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("Ti")));
			op.setTu(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("Tu")));
			op.setReactiveEnergyPlusArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate1"), scaleList, "ReactiveEnergyPlusArate1"));
			op.setReactiveEnergyPlusArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate2"), scaleList, "ReactiveEnergyPlusArate2"));
			op.setReactiveEnergyPlusArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate3"), scaleList, "ReactiveEnergyPlusArate3"));
			op.setReactiveEnergySubArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate1"), scaleList, "ReactiveEnergySubArate1"));
			op.setReactiveEnergySubArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate2"), scaleList, "ReactiveEnergySubArate2"));
			op.setReactiveEnergySubArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate3"), scaleList, "ReactiveEnergySubArate3"));

			op.setApparentPowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
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

	public void decodeInstantaneousPacket(String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		MT3OperationEntity ins = new MT3OperationEntity();
		DataCollection scaleList = new DataCollection();
		try {
			iMT3Business.getMeterObisScale(meterId, null, scaleList);
			if (scaleList.getdataList().isEmpty()) {
				logUlti.writeLog(LogType.INFO, "LAND - DCU:" + dcuCode + " - METER:" + meterId
						+ " - decodeInstantaneousPacket: RETURN - scaleList.getdataList().isEmpty()");
				return;

			}
			// Get Operation data
			ThreePhasePacketData.setMeasurementPointDefaultConfig(commandLine, dataCollection);
			ins.setMeterId(meterId);
			ins.setDcuCode(dcuCode);
			ins.setServerTime(this.convertDateTime(dataCollection.getDatabyName("ServerTime")));
			ins.setMeterTime(this.convertMeterTime(dataCollection.getDatabyName("MeterTime")));
			ins.setVoltagePhaseA(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("VoltagePhaseA"),
					scaleList, "VoltagePhaseA"));
			ins.setVoltagePhaseB(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("VoltagePhaseB"),
					scaleList, "VoltagePhaseB"));
			ins.setVoltagePhaseC(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("VoltagePhaseC"),
					scaleList, "VoltagePhaseC"));
			ins.setCurrentPhaseA(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("CurrentPhaseA"),
					scaleList, "CurrentPhaseA"));
			ins.setCurrentPhaseB(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("CurrentPhaseB"),
					scaleList, "CurrentPhaseB"));
			ins.setCurrentPhaseC(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("CurrentPhaseC"),
					scaleList, "CurrentPhaseC"));
			ins.setFrequency(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Frequency"), scaleList,
					"Frequency"));

			ins.setPowerFactorPhaseA(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("PowerFactorPhaseA"), scaleList, "PowerFactorPhaseA"));
			if (ins.getPowerFactorPhaseA() != null && ins.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseA(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseA().doubleValue()) * (180 / Math.PI)));
			ins.setPowerFactorPhaseB(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("PowerFactorPhaseB"), scaleList, "PowerFactorPhaseB"));
			if (ins.getPowerFactorPhaseB() != null && ins.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseB(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseB().doubleValue()) * (180 / Math.PI)));
			ins.setPowerFactorPhaseC(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("PowerFactorPhaseC"), scaleList, "PowerFactorPhaseC"));
			if (ins.getPowerFactorPhaseC() != null && ins.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseC(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseC().doubleValue()) * (180 / Math.PI)));

			ins.setActivePowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ActivePowerPhaseTotal"), scaleList, "ActivePowerPhaseTotal"));
			ins.setActivePowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ActivePowerPhaseTotal"), scaleList, "ActivePowerPhaseTotal"));
			ins.setActivePowerPhaseA(this.dataDivideValue(
					ins.getVoltagePhaseA().multiply(ins.getCurrentPhaseA().multiply(ins.getPowerFactorPhaseA())),
					1000));
			ins.setActivePowerPhaseB(this.dataDivideValue(
					ins.getVoltagePhaseB().multiply(ins.getCurrentPhaseB().multiply(ins.getPowerFactorPhaseB())),
					1000));
			ins.setActivePowerPhaseC(this.dataDivideValue(
					ins.getVoltagePhaseC().multiply(ins.getCurrentPhaseC().multiply(ins.getPowerFactorPhaseC())),
					1000));

			if (ins.getPhaseAnglePhaseA() != null)
				ins.setReactivePowerPhaseA(
						this.dataDivideValue(
								ins.getVoltagePhaseA()
										.multiply(ins.getCurrentPhaseA()
												.multiply(BigDecimal
														.valueOf(Math.sin(ins.getPhaseAnglePhaseA().doubleValue())))),
								1000));
			if (ins.getPhaseAnglePhaseB() != null)
				ins.setReactivePowerPhaseB(
						this.dataDivideValue(
								ins.getVoltagePhaseB()
										.multiply(ins.getCurrentPhaseB()
												.multiply(BigDecimal
														.valueOf(Math.sin(ins.getPhaseAnglePhaseB().doubleValue())))),
								1000));
			if (ins.getPhaseAnglePhaseC() != null)
				ins.setReactivePowerPhaseC(
						this.dataDivideValue(
								ins.getVoltagePhaseC()
										.multiply(ins.getCurrentPhaseC()
												.multiply(BigDecimal
														.valueOf(Math.sin(ins.getPhaseAnglePhaseC().doubleValue())))),
								1000));

			ins.setReactivePowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseTotal"), scaleList, "ReactivePowerPhaseTotal"));
			ins.setPowerFactorPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("PowerFactorPhaseTotal"), scaleList, "PowerFactorPhaseTotal"));

			ins.setEnergyPlusArate1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate1"), scaleList, "EnergyPlusArate1"), 1000));
			ins.setEnergyPlusArate2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate2"), scaleList, "EnergyPlusArate2"), 1000));
			ins.setEnergyPlusArate3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate3"), scaleList, "EnergyPlusArate3"), 1000));
			ins.setEnergySubArate1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergySubArate1"), scaleList, "EnergySubArate1"), 1000));
			ins.setEnergySubArate2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergySubArate2"), scaleList, "EnergySubArate2"), 1000));
			ins.setEnergySubArate3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergySubArate3"), scaleList, "EnergySubArate3"), 1000));
			ins.setExportWh(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ExportWh"), scaleList, "ExportWh"), 1000));
			ins.setImportWh(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ImportWh"), scaleList, "ImportWh"), 1000));
			ins.setQ1(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Q1"), scaleList, "Q1"));
			ins.setQ3(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Q3"), scaleList, "Q3"));
			ins.setMaxDemandPlusArate1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate1"), scaleList, "MaxDemandPlusArate1"), 1000));
			ins.setMaxDemandPlusArate1Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate1Time")));
			ins.setMaxDemandSubArate1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate1"), scaleList, "MaxDemandSubArate1"), 10));
			ins.setMaxDemandSubArate1Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandSubArate1Time")));
			ins.setMaxDemandPlusArate2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate2"), scaleList, "MaxDemandPlusArate2"), 1000));
			ins.setMaxDemandPlusArate2Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate2Time")));
			ins.setMaxDemandSubArate2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate2"), scaleList, "MaxDemandSubArate2"), 1000));
			ins.setMaxDemandSubArate2Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandSubArate2Time")));
			ins.setMaxDemandPlusArate3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate3"), scaleList, "MaxDemandPlusArate3"), 1000));
			ins.setMaxDemandPlusArate3Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate3Time")));
			ins.setMaxDemandSubArate3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate3"), scaleList, "MaxDemandSubArate3"), 1000));
			ins.setMaxDemandSubArate3Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandSubArate3Time")));
			ins.setTi(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("Ti")));
			ins.setTu(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("Tu")));
			ins.setReactiveEnergyPlusArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate1"), scaleList, "ReactiveEnergyPlusArate1"));
			ins.setReactiveEnergyPlusArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate2"), scaleList, "ReactiveEnergyPlusArate2"));
			ins.setReactiveEnergyPlusArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate3"), scaleList, "ReactiveEnergyPlusArate3"));
			ins.setReactiveEnergySubArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate1"), scaleList, "ReactiveEnergySubArate1"));
			ins.setReactiveEnergySubArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate2"), scaleList, "ReactiveEnergySubArate2"));
			ins.setReactiveEnergySubArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate3"), scaleList, "ReactiveEnergySubArate3"));

			ins.setApparentPowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ApparentPowerPhaseTotal"), scaleList, "ApparentPowerPhaseTotal"));

			iMT3Business.insertIntantaneous(ins);

			scaleList = null;
			ins = null;
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeInstantaneousPacket", ex);
		}
	}

	@Override
	public void decodeHistoricalPacket(String topic, String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		MT3HistoricalEntity hi = new MT3HistoricalEntity();

		DataCollection scaleList = new DataCollection();

		try {
			iMT3Business.getMeterObisScale(meterId, null, scaleList);
			// Get historical data
			ThreePhasePacketData.setMeasurementPointDefaultConfig(commandLine, dataCollection);
			
			hi.setTopic(topic);
			
			// Begin decode
			hi.setServerTime(this.convertDateTime(dataCollection.getDatabyName("ServerTime")));

			hi.setMeterTime(this.convertMeterTime(dataCollection.getDatabyName("MeterTime")));

			hi.setMeterId(meterId);
			hi.setDcuCode(dcuCode);

			hi.setEndHistoricalTime(this.convertMeterTime(dataCollection.getDatabyName("MeterTime")));

			if (hi.getEndHistoricalTime() == null) {
				logUlti.writeLog(LogType.INFO, "LAND - DCU:" + dcuCode + " - METER:" + meterId
						+ " - decodeHistoricalPacket: RETURN - hi.getEndHistoricalTime() == null");
				return;

			}

			Timestamp dt = hi.getEndHistoricalTime();
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(dt.getTime());
			cal.add(Calendar.MONTH, -1);
			hi.setHistoricalTime(new Timestamp(cal.getTime().getTime()));
			hi.setBeginHistoricalTime(hi.getHistoricalTime());

			hi.setImportWh(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ImportWh"), scaleList, "ImportWh"), 1000));

			hi.setExportWh(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ExportWh"), scaleList, "ExportWh"), 1000));

			hi.setEnergyPlusARate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate1"), scaleList, "EnergyPlusArate1"));

			hi.setEnergyPlusARate2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate2"), scaleList, "EnergyPlusArate2"), 1000));

			hi.setEnergyPlusARate3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate3"), scaleList, "EnergyPlusArate3"), 1000));

			hi.setEnergySubARate1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergySubArate1"), scaleList, "EnergySubArate1"), 1000));

			hi.setEnergySubARate2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergySubArate2"), scaleList, "EnergySubArate2"), 1000));

			hi.setEnergySubARate3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergySubArate3"), scaleList, "EnergySubArate3"), 1000));

			hi.setQ1(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Q1"), scaleList, "Q1"));

			hi.setQ3(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Q3"), scaleList, "Q3"));

			hi.setMaxDemandPlusArate1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate1"), scaleList, "MaxDemandPlusArate1"), 1000));
			hi.setMaxDemandPlusArate1Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate1Time")));

			hi.setMaxDemandPlusArate2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate2"), scaleList, "MaxDemandPlusArate2"), 1000));
			hi.setMaxDemandPlusArate2Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate2Time")));

			hi.setMaxDemandPlusArate3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate3"), scaleList, "MaxDemandPlusArate3"), 1000));
			hi.setMaxDemandPlusArate3Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate3Time")));

			hi.setReactiveEnergyPlusArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate1"), scaleList, "ReactiveEnergyPlusArate1"));
			hi.setReactiveEnergyPlusArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate2"), scaleList, "ReactiveEnergyPlusArate2"));
			hi.setReactiveEnergyPlusArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate3"), scaleList, "ReactiveEnergyPlusArate3"));
			hi.setReactiveEnergySubArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate1"), scaleList, "ReactiveEnergySubArate1"));
			hi.setReactiveEnergySubArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate2"), scaleList, "ReactiveEnergySubArate2"));
			hi.setReactiveEnergySubArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactiveEnergySubArate3"), scaleList, "ReactiveEnergySubArate3"));
			// End decode

			if (hi.getHistoricalTime() != null)
				iMT3Business.insertHistorical(hi);

			hi = null;
			scaleList = null;
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeHistoricalPacket", ex);
		}
	}

	@Override
	public void decodeMeasurementPointScale(String dcuCode, String meterId, String meterType,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		try {
			// Get Scale list
			DataCollection scaleList = dataCollection.getDataCollectionByTagId(Constant.SCALE_TAG);
			BigDecimal scale = null;
			for (DataField item : scaleList.getdataList()) {
				try {
					scale = getScaleFromFirstMessage(item.getData());
					if (scale != null) {
						item.setScale(scale);
						item.setType(meterType);
					}
				} catch (Exception ex) {
					logUlti.writeLog(LogType.ERROR, "decodeMeasurementPointScale", ex);
				}
			}

			iMT3Business.UpdateMeterObisScale(meterId, scaleList, false);

			scale = null;
			scaleList = null;
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeMeasurementPointScale", ex);
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
				logUlti.writeLog(LogType.INFO, "LAND - DCU:" + dcuCode + " - METER:" + meterId
						+ " - decodeLoadProfilePacket: RETURN - scaleList.getdataList().isEmpty()");
				return;

			}
			loadProfileList = ThreePhasePacketData.GetLoadProfileObisByName(commandLine, dataCollection);

			for (DataCollection profileList : loadProfileList.getLoadProfileList()) {
				load.setServerTime(this.convertDateTime(loadProfileList.getServerTimeValue()));
				load.setMeterTimeFrom(this.convertMeterTime(profileList.getDatabyName("MeterTime")));

				load.setVoltagePhaseA(this.calculatorConvertHexToIntIsNull(profileList.getDatabyName("VoltagePhaseA"),
						scaleList, "VoltagePhaseA"));
				load.setVoltagePhaseB(this.calculatorConvertHexToIntIsNull(profileList.getDatabyName("VoltagePhaseB"),
						scaleList, "VoltagePhaseB"));
				load.setVoltagePhaseC(this.calculatorConvertHexToIntIsNull(profileList.getDatabyName("VoltagePhaseC"),
						scaleList, "VoltagePhaseC"));
				load.setCurrentPhaseA(this.calculatorConvertHexToIntIsNull(profileList.getDatabyName("CurrentPhaseA"),
						scaleList, "CurrentPhaseA"));
				load.setCurrentPhaseB(this.calculatorConvertHexToIntIsNull(profileList.getDatabyName("CurrentPhaseB"),
						scaleList, "CurrentPhaseB"));
				load.setCurrentPhaseC(this.calculatorConvertHexToIntIsNull(profileList.getDatabyName("CurrentPhaseC"),
						scaleList, "CurrentPhaseC"));
				load.setImportWh(this.calculatorConvertHexToIntIsNull(profileList.getDatabyName("ImportWh"), scaleList,
						"ImportWh"));
				load.setExportWh(this.calculatorConvertHexToIntIsNull(profileList.getDatabyName("ExportWh"), scaleList,
						"ExportWh"));
				load.setEnergyPlusARate1(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("EnergyPlusARate1"), scaleList, "EnergyPlusARate1"));
				load.setEnergyPlusARate2(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("EnergyPlusARate2"), scaleList, "EnergyPlusARate2"));
				load.setEnergyPlusARate3(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("EnergyPlusARate3"), scaleList, "EnergyPlusARate3"));
				load.setEnergySubARate1(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("EnergySubARate1"), scaleList, "EnergySubARate1"));
				load.setEnergySubARate2(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("EnergySubARate2"), scaleList, "EnergySubARate2"));
				load.setEnergySubARate3(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("EnergySubARate3"), scaleList, "EnergySubARate3"));

				load.setQ1(this.calculatorConvertHexToIntIsNull(profileList.getDatabyName("Q1"), scaleList, "Q1"));
				load.setQ3(this.calculatorConvertHexToIntIsNull(profileList.getDatabyName("Q3"), scaleList, "Q3"));

				load.setDcuCode(dcuCode);
				load.setMeterCode(meterId);

				iMT3Business.insertLoadProfilePacket(load);

				scaleList = null;
				profileList = null;
				load = null;
			}

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeLoadProfilePacket", ex);
		}

	}

	private BigDecimal getScaleFromFirstMessage(String hexString) {
		try {
			BigDecimal scaleValue = null;
			if (hexString.equals("01"))
				scaleValue = BigDecimal.valueOf(0.1);
			else if (hexString.equals("00"))
				scaleValue = BigDecimal.valueOf(1000);
			else if (hexString.equals("FF"))
				scaleValue = BigDecimal.valueOf(10);
			else if (hexString.equals("FE"))
				scaleValue = BigDecimal.valueOf(100);
			else if (hexString.equals("FD"))
				scaleValue = BigDecimal.valueOf(1000);
			else if (hexString.equals("FC"))
				scaleValue = BigDecimal.valueOf(10000);

			return BigDecimal.valueOf(1).divide(scaleValue);
		} catch (Exception ex) {
			return null;
		}
	}

	private BigDecimal getAlertValue(String dataByObisname, int value) {
		try {
			return ConvertUlti.convertHexToDecimal(dataByObisname).divide(BigDecimal.valueOf(value));
		} catch (Exception ex) {
		}
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

	private BigDecimal getValueBy10PercentOver(BigDecimal freqOver) {
		if (freqOver != null)
			return freqOver.multiply(BigDecimal.valueOf(0.91)); // 10/11
		return null;
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

	public Timestamp convertMeterTime(String hexString) {
		try {
			int year = ConvertUlti.hex2Int(hexString.substring(0, 4)) - 1900; // Timestamp
																				// +
																				// 1900
																				// (year)
			int mm = ConvertUlti.hex2Int(hexString.substring(4, 6)) - 1; // month
																			// 0-
																			// 11
			int dd = ConvertUlti.hex2Int(hexString.substring(6, 8));

			int hh = ConvertUlti.hex2Int(hexString.substring(10, 12));
			int min = ConvertUlti.hex2Int(hexString.substring(12, 14));
			int sc = ConvertUlti.hex2Int(hexString.substring(14, 16));
			@SuppressWarnings("deprecation")
			Timestamp date = new Timestamp(year, mm, dd, hh, min, sc, 0);
			return date;
		} catch (Exception ex) {
			return null;
		}
	}

	private BigDecimal calculatorConvertHexToIntIsNull(String hexString, DataCollection scaleList, String filename) {
		try {
			return BigDecimal.valueOf(ConvertUlti.hex2Int(hexString)).multiply((scaleList.getScalebyName(filename)));
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
