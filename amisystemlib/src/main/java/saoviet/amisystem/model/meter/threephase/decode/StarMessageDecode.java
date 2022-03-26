/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: StarMessageDecode.java
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
import saoviet.amisystem.model.meter.threephase.packet.StarPacketStructure;
import saoviet.amisystem.model.meter.threephase.packet.ThreePhasePacketData;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.SaveMessageUlti;
import saoviet.amisystem.ulti.StringUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class StarMessageDecode implements IThreePhaseMessageDecode {

	private LogUlti logUlti = new LogUlti(StarMessageDecode.class);
	private DataCollection mepdvPacketList;
	private DataCollection historicalPacketList;
	private DataCollection operationPacketList;
	private DataCollection loadProfilePacketList;
	private DataCollection meterEventPacketList;
	private DataCollection dcuWarningPacketList;
	private IMT3Business iMT3Business;
	private SaveMessageUlti save = new SaveMessageUlti();
	private SystemEventCallback systemEventCallback;

	@Override
	public void setSystemEventCallBack(SystemEventCallback systemEventCallback) {
		// TODO Auto-generated method stub
		this.systemEventCallback = systemEventCallback;
		this.iMT3Business.setSystemEventCallback(this.systemEventCallback);
	}

	public StarMessageDecode(IMT3Business inIMT3Business) {
		this.mepdvPacketList = StarPacketStructure.getMEPDVCollection();

		this.dcuWarningPacketList = StarPacketStructure.getMeterAlertCollection();

		this.historicalPacketList = StarPacketStructure.getHistoricalCollection();

		this.operationPacketList = StarPacketStructure.getOperationCollection();

		this.loadProfilePacketList = StarPacketStructure.getLoadProfileCollection();

		this.meterEventPacketList = StarPacketStructure.getEventCollection();

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
						Constant.METER_TYPE_STAR, commandLine, this.mepdvPacketList.copy(), this.iMT3Business);
				break;
			case Constant.SAO_VIET_MESSAGE_TYPE_DCU_WARNING:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeMeasurementPointAlertPacket(messagebase.getDcuCode(), messagebase.getMeterCode(),
						commandLine, this.dcuWarningPacketList.copy(), this.iMT3Business);
				break;
			case Constant.SAO_VIET_MESSAGE_TYPE_HISTORICAL:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeHistoricalPacket(messagebase.getPreTopic(), messagebase.getDcuCode(), messagebase.getMeterCode(), commandLine,
						this.historicalPacketList.copy(), this.iMT3Business);
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
			Runtime.getRuntime().gc();
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
				Runtime.getRuntime().gc();
			}
		} catch (Exception ex) {
			System.out.println("MEPDV_START ERROR: " + mpstatus + " - meterType:" + meterType + " - DCU:" + dcuCode
					+ " -METER:" + meterId + " - DATAERROR:" + ex);
			logUlti.writeLog(LogType.ERROR, "MePDV decode fail", ex);
		}
		// System.out.println("MEPDV_START STATUS: " + mpstatus + " -
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
			
			alertValueList = null;
			alertValue = null;
			mpAlertConfList = null;

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

			dcuInfo = null;
			dcuInforList = null;
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
				logUlti.writeLog(LogType.INFO,"STAR - DCU:"+dcuCode+" - METER:"+meterId+
						" - decodeMeasurementPointAlertPacket: RETURN - dataCollection.getdataList().isEmpty()");
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
			statusValue = null;
			valueAlert = null;
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeMeasurementPointAlertPacket - DCU:"+dcuCode+" - METER: "+meterId+" - DATA: "+commandLine, ex);
		}
	}

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
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventPowerFailureSecond(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventPowerFailureThird(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventPowerFailureFourth(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventPowerFailureFifth(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventValueFullList = dataCollection.getDataFieldByObisName("EventPowerFailureTimeEnd");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventPowerRecoverFirst(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventPowerRecoverSecond(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventPowerRecoverThird(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventPowerRecoverFourth(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventPowerRecoverFifth(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(4), eventValueFullList)));
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
			if (event.getEventProgrammingCount() != null
					&& event.getEventProgrammingCount().compareTo(BigDecimal.valueOf(0)) == 1) {
				eventValueFullList = dataCollection.getDataFieldByObisName("EventProgrammingTime");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventProgrammingFirst(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventProgrammingSecond(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventProgrammingThird(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventProgrammingFourth(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventProgrammingFifth(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(4), eventValueFullList)));
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
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventReverseRunPhaseASecondStart(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventReverseRunPhaseAThirdStart(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventReverseRunPhaseAFourthStart(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventReverseRunPhaseAFifthStart(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseRunTimeAEnd");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventReverseRunPhaseAFirstEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventReverseRunPhaseASecondEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventReverseRunPhaseAThirdEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventReverseRunPhaseAFourthEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventReverseRunPhaseAFifthEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(4), eventValueFullList)));
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
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventReverseRunPhaseBSecondStart(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventReverseRunPhaseBThirdStart(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventReverseRunPhaseBFourthStart(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventReverseRunPhaseBFifthStart(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseRunTimeBEnd");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventReverseRunPhaseBFirstEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventReverseRunPhaseBSecondEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventReverseRunPhaseBThirdEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventReverseRunPhaseBFourthEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventReverseRunPhaseBFifthEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(4), eventValueFullList)));
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
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventReverseRunPhaseCSecondStart(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventReverseRunPhaseCThirdStart(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventReverseRunPhaseCFourthStart(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventReverseRunPhaseCFifthStart(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(4), eventValueFullList)));
				}
				eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseRunTimeCEnd");
				if (eventValueFullList.getEventList().size() > 0) {
					event.setEventReverseRunPhaseCFirstEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventReverseRunPhaseCSecondEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventReverseRunPhaseCThirdEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventReverseRunPhaseCFourthEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventReverseRunPhaseCFifthEnd(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(4), eventValueFullList)));
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
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventFailurePhaseASecond(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventFailurePhaseAThird(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventFailurePhaseAFourth(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventFailurePhaseAFifth(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(4), eventValueFullList)));
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
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventFailurePhaseBSecond(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventFailurePhaseBThird(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventFailurePhaseBFourth(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventFailurePhaseBFifth(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(4), eventValueFullList)));
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
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(0), eventValueFullList)));
					event.setEventFailurePhaseCSecond(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(1), eventValueFullList)));
					event.setEventFailurePhaseCThird(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(2), eventValueFullList)));
					event.setEventFailurePhaseCFourth(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(3), eventValueFullList)));
					event.setEventFailurePhaseCFifth(
							this.convertDateTimeNotSec(this.getEventValue(Integer.toString(4), eventValueFullList)));
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

			event = null;
			eventValueFullList = null;
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
				logUlti.writeLog(LogType.INFO,"STAR - DCU:"+dcuCode+" - METER:"+meterId+
						" - decodeOperationPacket: RETURN - scaleList.getdataList().isEmpty()");
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
			op.setActivePowerPhaseA(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ActivePowerPhaseA"), scaleList, "ActivePowerPhaseA"));
			op.setActivePowerPhaseB(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ActivePowerPhaseB"), scaleList, "ActivePowerPhaseB"));
			op.setActivePowerPhaseC(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ActivePowerPhaseC"), scaleList, "ActivePowerPhaseC"));
			op.setActivePowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ActivePowerPhaseTotal"), scaleList, "ActivePowerPhaseTotal"));
			op.setReactivePowerPhaseA(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseA"), scaleList, "ReactivePowerPhaseA"));
			op.setReactivePowerPhaseB(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseB"), scaleList, "ReactivePowerPhaseB"));
			op.setReactivePowerPhaseC(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseC"), scaleList, "ReactivePowerPhaseC"));
			op.setReactivePowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseTotal"), scaleList, "ReactivePowerPhaseTotal"));
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

			op.setPowerFactorPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("PowerFactorPhaseTotal"), scaleList, "PowerFactorPhaseTotal"));

			op.setEnergyPlusArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate1"), scaleList, "EnergyPlusArate1"));
			op.setEnergyPlusArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate2"), scaleList, "EnergyPlusArate2"));
			op.setEnergyPlusArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate3"), scaleList, "EnergyPlusArate3"));
			op.setEnergySubArate1(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("EnergySubArate1"),
					scaleList, "EnergySubArate1"));
			op.setEnergySubArate2(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("EnergySubArate2"),
					scaleList, "EnergySubArate2"));
			op.setEnergySubArate3(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("EnergySubArate3"),
					scaleList, "EnergySubArate3"));
			op.setExportWh(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("ExportWh"), scaleList,
					"ExportWh"));
			op.setImportWh(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("ImportWh"), scaleList,
					"ImportWh"));
			op.setQ1(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Q1"), scaleList, "Q1"));
			op.setQ3(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Q3"), scaleList, "Q3"));
			op.setMaxDemandPlusArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate1"), scaleList, "MaxDemandPlusArate1"));
			op.setMaxDemandPlusArate1Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandPlusArate1Time")));
			op.setMaxDemandSubArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate1"), scaleList, "MaxDemandSubArate1"));
			op.setMaxDemandSubArate1Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandSubArate1Time")));
			op.setMaxDemandPlusArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate2"), scaleList, "MaxDemandPlusArate2"));
			op.setMaxDemandPlusArate2Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandPlusArate2Time")));
			op.setMaxDemandSubArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate2"), scaleList, "MaxDemandSubArate2"));
			op.setMaxDemandSubArate2Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandSubArate2Time")));
			op.setMaxDemandPlusArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate3"), scaleList, "MaxDemandPlusArate3"));
			op.setMaxDemandPlusArate3Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandPlusArate3Time")));
			op.setMaxDemandSubArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate3"), scaleList, "MaxDemandSubArate3"));
			op.setMaxDemandSubArate3Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandSubArate3Time")));
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
				logUlti.writeLog(LogType.INFO,"STAR - DCU:"+dcuCode+" - METER:"+meterId+
						" - decodeOperationPacket: RETURN - scaleList.getdataList().isEmpty()");
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
			ins.setActivePowerPhaseA(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ActivePowerPhaseA"), scaleList, "ActivePowerPhaseA"));
			ins.setActivePowerPhaseB(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ActivePowerPhaseB"), scaleList, "ActivePowerPhaseB"));
			ins.setActivePowerPhaseC(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ActivePowerPhaseC"), scaleList, "ActivePowerPhaseC"));
			ins.setActivePowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ActivePowerPhaseTotal"), scaleList, "ActivePowerPhaseTotal"));
			ins.setReactivePowerPhaseA(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseA"), scaleList, "ReactivePowerPhaseA"));
			ins.setReactivePowerPhaseB(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseB"), scaleList, "ReactivePowerPhaseB"));
			ins.setReactivePowerPhaseC(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseC"), scaleList, "ReactivePowerPhaseC"));
			ins.setReactivePowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ReactivePowerPhaseTotal"), scaleList, "ReactivePowerPhaseTotal"));
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

			ins.setPowerFactorPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("PowerFactorPhaseTotal"), scaleList, "PowerFactorPhaseTotal"));

			ins.setEnergyPlusArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate1"), scaleList, "EnergyPlusArate1"));
			ins.setEnergyPlusArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate2"), scaleList, "EnergyPlusArate2"));
			ins.setEnergyPlusArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate3"), scaleList, "EnergyPlusArate3"));
			ins.setEnergySubArate1(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("EnergySubArate1"),
					scaleList, "EnergySubArate1"));
			ins.setEnergySubArate2(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("EnergySubArate2"),
					scaleList, "EnergySubArate2"));
			ins.setEnergySubArate3(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("EnergySubArate3"),
					scaleList, "EnergySubArate3"));
			ins.setExportWh(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("ExportWh"), scaleList,
					"ExportWh"));
			ins.setImportWh(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("ImportWh"), scaleList,
					"ImportWh"));
			ins.setQ1(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Q1"), scaleList, "Q1"));
			ins.setQ3(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Q3"), scaleList, "Q3"));
			ins.setMaxDemandPlusArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate1"), scaleList, "MaxDemandPlusArate1"));
			ins.setMaxDemandPlusArate1Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandPlusArate1Time")));
			ins.setMaxDemandSubArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate1"), scaleList, "MaxDemandSubArate1"));
			ins.setMaxDemandSubArate1Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandSubArate1Time")));
			ins.setMaxDemandPlusArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate2"), scaleList, "MaxDemandPlusArate2"));
			ins.setMaxDemandPlusArate2Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandPlusArate2Time")));
			ins.setMaxDemandSubArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate2"), scaleList, "MaxDemandSubArate2"));
			ins.setMaxDemandSubArate2Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandSubArate2Time")));
			ins.setMaxDemandPlusArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate3"), scaleList, "MaxDemandPlusArate3"));
			ins.setMaxDemandPlusArate3Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandPlusArate3Time")));
			ins.setMaxDemandSubArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate3"), scaleList, "MaxDemandSubArate3"));
			ins.setMaxDemandSubArate3Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandSubArate3Time")));
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

			hi.setBeginHistoricalTime(this.convertDateTimeNotSec(dataCollection.getDatabyName("BeginHistoricalTime")));

			if (hi.getBeginHistoricalTime() == null)
			{	
				logUlti.writeLog(LogType.INFO,"STAR - DCU:"+dcuCode+" - METER:"+meterId+
						" - decodeHistoricalPacket: RETURN - hi.getBeginHistoricalTime() == null");
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

			hi.setImportWh(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("ImportWh"), scaleList,
					"ImportWh"));

			hi.setExportWh(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("ExportWh"), scaleList,
					"ExportWh"));

			hi.setEnergyPlusARate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate1"), scaleList, "EnergyPlusArate1"));

			hi.setEnergyPlusARate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate2"), scaleList, "EnergyPlusArate2"));

			hi.setEnergyPlusARate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("EnergyPlusArate3"), scaleList, "EnergyPlusArate3"));

			hi.setEnergySubARate1(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("EnergySubArate1"),
					scaleList, "EnergySubArate1"));

			hi.setEnergySubARate2(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("EnergySubArate2"),
					scaleList, "EnergySubArate2"));

			hi.setEnergySubARate3(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("EnergySubArate3"),
					scaleList, "EnergySubArate3"));

			hi.setQ1(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Q1"), scaleList, "Q1"));

			hi.setQ3(this.calculatorConvertHexToIntIsNull(dataCollection.getDatabyName("Q3"), scaleList, "Q3"));

			hi.setMaxDemandPlusArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate1"), scaleList, "MaxDemandPlusArate1"));
			hi.setMaxDemandPlusArate1Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandPlusArate1Time")));

			hi.setMaxDemandPlusArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate2"), scaleList, "MaxDemandPlusArate2"));
			hi.setMaxDemandPlusArate2Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandPlusArate2Time")));

			hi.setMaxDemandPlusArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate3"), scaleList, "MaxDemandPlusArate3"));
			hi.setMaxDemandPlusArate3Time(
					this.convertDateTimeNotSec(dataCollection.getDatabyName("MaxDemandPlusArate3Time")));

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
			// System.out.println("HISTORICAL");
			// End decode

			if (hi.getHistoricalTime() != null)
				iMT3Business.insertHistorical(hi);

			scaleList = null;
			hi = null;

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

	public void decodeLoadProfilePacket(String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		MT3LoadProfile3PMessageEntity load = new MT3LoadProfile3PMessageEntity();
		MT3LoadProfilePacketList loadProfileList = new MT3LoadProfilePacketList();
		try {

			DataCollection scaleList = new DataCollection();
			iMT3Business.getMeterObisScale(meterId, null, scaleList);
			if (scaleList.getdataList().isEmpty())
			{	
				logUlti.writeLog(LogType.INFO,"STAR - DCU:"+dcuCode+" - METER:"+meterId+
					" - decodeLoadProfilePacket: RETURN - scaleList.getdataList().isEmpty()");
				return;
			
			}
			loadProfileList = ThreePhasePacketData.GetLoadProfileObisByName(commandLine, dataCollection);

			for (DataCollection profileList : loadProfileList.getLoadProfileList()) {
				load.setServerTime(this.convertDateTime(loadProfileList.getServerTimeValue()));
				load.setMeterTimeFrom(this.convertDateTimeNotSec(profileList.getDatabyName("MeterTime")));
				load.setPowerAvgPos(this.calculatorConvertHexToIntIsNull(profileList.getDatabyName("PowerAvgPos"),
						scaleList, "EnergyPlusArate1"));
				load.setPowerAvgNeg(this.calculatorConvertHexToIntIsNull(profileList.getDatabyName("PowerAvgNeg"),
						scaleList, "EnergyPlusArate1"));
				load.setReactivePowerAvgPos(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ReactivePowerAvgPos"), scaleList, "EnergyPlusArate1"));
				load.setReactivePowerAvgNeg(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ReactivePowerAvgNeg"), scaleList, "EnergyPlusArate1"));

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

	public Timestamp convertMeterTime(String hexString) {
		try {
			int year = 100 + (Integer.parseInt(hexString.substring(0, 2)));// 1900+100=2000
			if (year == 100)
				return null; // giá trị 0
			int mm = Integer.parseInt(hexString.substring(2, 4)) - 1; //// month
			//// 0-11
			int dd = Integer.parseInt(hexString.substring(4, 6));

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

	private BigDecimal calculatorConvertHexToIntIsNull(String hexString, DataCollection scaleList, String filename) {
		try {
			return new BigDecimal(hexString).multiply((scaleList.getScalebyName(filename)));
		} catch (Exception ex) {
			return null;
		}
	}

	private Timestamp convertDateTimeNotSec(String hexString) {
		try {
			int year = 100 + (Integer.parseInt(hexString.substring(0, 2)));// 1900+100=2000
			if (year == 100)
				return null; // giá trị 0
			int mm = Integer.parseInt(hexString.substring(2, 4)) - 1; //// month
			//// 0-11
			int dd = Integer.parseInt(hexString.substring(4, 6));

			int hh = Integer.parseInt(hexString.substring(6, 8));
			int min = Integer.parseInt(hexString.substring(8, 10));
			int sc = 0;
			@SuppressWarnings("deprecation")
			Timestamp date = new Timestamp(year, mm, dd, hh, min, sc, 0);
			// String datetime = datetimeformat.format(date);
			return date;
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

	private synchronized BigDecimal getValueBy10PercentOver(BigDecimal freqOver) {
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

	private BigDecimal dataDivideValue(BigDecimal data, int value) {
		if (data == null)
			return null;
		return data.divide(BigDecimal.valueOf(value));
	}

}
