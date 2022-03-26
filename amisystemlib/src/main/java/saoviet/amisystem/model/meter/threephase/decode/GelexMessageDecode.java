/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: GelexMessageDecode.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-06-06 11:47:06
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
import saoviet.amisystem.model.meter.threephase.entity.MT3MeterAlertCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3OperationEntity;
import saoviet.amisystem.model.meter.threephase.packet.GelexPacketStructure;
import saoviet.amisystem.model.meter.threephase.packet.ThreePhasePacketData;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.SaveMessageUlti;
import saoviet.amisystem.ulti.StringUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class GelexMessageDecode implements IThreePhaseMessageDecode {

	private DataCollection mepdvPacketList;
	private DataCollection historicalPacketList;
	private DataCollection operationPacketList;
	private DataCollection loadProfilePacketList;
	private DataCollection meterEventPacketList;
	private DataCollection dcuWarningPacketList;
	private IMT3Business iMT3Business;
	private LogUlti logUlti = new LogUlti(GelexMessageDecode.class);
	private SaveMessageUlti save = new SaveMessageUlti();
	private SystemEventCallback systemEventCallback;

	@Override
	public void setSystemEventCallBack(SystemEventCallback systemEventCallback) {
		// TODO Auto-generated method stub
		this.systemEventCallback = systemEventCallback;
		this.iMT3Business.setSystemEventCallback(this.systemEventCallback);
	}

	public GelexMessageDecode(IMT3Business inIMT3Business) {
		this.mepdvPacketList = GelexPacketStructure.getMEPDVCollection();

		this.dcuWarningPacketList = GelexPacketStructure.getMeterAlertCollection();

		this.historicalPacketList = GelexPacketStructure.getHistoricalCollection();

		this.operationPacketList = GelexPacketStructure.getOperationCollection();

		this.loadProfilePacketList = GelexPacketStructure.getLoadProfileCollection();

		this.meterEventPacketList = GelexPacketStructure.getEventCollection();

		this.iMT3Business = inIMT3Business;
	}

	@Override
	public synchronized void decodeMessage(MessageBase messagebase) {
		try {
			// Convert byte to string
			String commandLine = null;

			switch (messagebase.getMessageType()) {
			case Constant.SAO_VIET_MESSAGE_TYPE_MEPDV:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeMeasurementPointConfigPacket(messagebase.getDcuCode(), messagebase.getMeterCode(),
						Constant.METER_TYPE_GELEX, commandLine, this.mepdvPacketList.copy(), this.iMT3Business);
				break;
			case Constant.SAO_VIET_MESSAGE_TYPE_DCU_WARNING:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeMeasurementPointAlertPacket(messagebase.getDcuCode(), messagebase.getMeterCode(),
						commandLine, this.dcuWarningPacketList.copy(), this.iMT3Business);
				break;
			case Constant.SAO_VIET_MESSAGE_TYPE_HISTORICAL:
				commandLine = ConvertUlti.toHexString(messagebase.getData());
				this.decodeHistoricalPacket(messagebase.getPreTopic(),messagebase.getDcuCode(), messagebase.getMeterCode(), commandLine,
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
			
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeMessage", ex);
		}
	}

	@Override
	public synchronized void decodeMeasurementPointConfigPacket(String dcuCode, String meterId, String meterType, String commandLine,
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
			System.out.println("MEPDV_GELEX ERROR: " + mpstatus + " - meterType:" + meterType + " - DCU:" + dcuCode
					+ " -METER:" + meterId + " - DATAERROR:" + ex);
			logUlti.writeLog(LogType.ERROR, "MePDV decode fail", ex);
		}
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
			mpAlertConfList = null;
			alertValueList = null;
			
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeMeasurementPointAlertConfig", ex);
		}
	}

	@Override
	public  void decodeDcuinfo(String dcuCode, String meterId, DataCollection dataList, IMT3Business iMT3Business) {
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
	public  void decodeMeasurementPointAlertPacket(String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		try {
			MT3MeterAlertCollection meterAlertList = new MT3MeterAlertCollection();
			ThreePhasePacketData.setMeasurementPointDefaultConfig(commandLine, dataCollection);
			if (dataCollection.getdataList().isEmpty())
			{
				logUlti.writeLog(LogType.INFO,"GELEX - DCU:"+dcuCode+" - METER:"+meterId+
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
			valueAlert = null;
			statusValue = null;
			
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeMeasurementPointAlertPacket - DCU:"+dcuCode+" - METER: "+meterId+" - DATA: "+commandLine, ex);	
		}
	}

	@Override
	public  void decodeEventPacket(String dcuCode, String meterId, String commandLine, DataCollection dataCollection,
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
			eventValueFullList = dataCollection.getDataFieldByObisName("EventPowerFailureTimeStart");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventPowerFailureFirst(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventPowerFailureSecond(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventPowerFailureThird(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventPowerFailureFourth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventPowerFailureFifth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			eventValueFullList = dataCollection.getDataFieldByObisName("EventPowerFailureTimeEnd");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventPowerRecoverFirst(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventPowerRecoverSecond(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventPowerRecoverThird(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventPowerRecoverFourth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventPowerRecoverFifth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			if (event.getEventPowerFailureFirst() != null)
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTPOWERFAILURE,
						event.getEventPowerFailureCount(), null, event.getEventPowerFailureFirst(),
						event.getEventPowerFailureSecond(), event.getEventPowerFailureThird(),
						event.getEventPowerFailureFourth(), event.getEventPowerFailureFifth(),
						event.getEventPowerRecoverFirst(), event.getEventPowerRecoverSecond(),
						event.getEventPowerRecoverThird(), event.getEventPowerRecoverFourth(),
						event.getEventPowerRecoverFifth(), "3P");

			eventValueFullList = dataCollection.getDataFieldByObisName("EventProgrammingTime");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventProgrammingFirst(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventProgrammingSecond(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventProgrammingThird(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventProgrammingFourth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventProgrammingFifth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			if (event.getEventProgrammingFirst() != null)
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTPROGRAMMING,
						event.getEventProgrammingCount(), null, event.getEventProgrammingFirst(),
						event.getEventProgrammingSecond(), event.getEventProgrammingThird(),
						event.getEventProgrammingFourth(), event.getEventProgrammingFifth(), null, null, null, null,
						null, "3P");

			eventValueFullList = dataCollection.getDataFieldByObisName("EventCoverOpenTime");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventCoverOpenTimeFirst(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventCoverOpenTimeSecond(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventCoverOpenTimeThird(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventCoverOpenTimeFourth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventCoverOpenTimeFifth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			eventValueFullList = dataCollection.getDataFieldByObisName("EventCoverCloseTime");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventCoverCloseTimeFirst(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventCoverCloseTimeSecond(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventCoverCloseTimeThird(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventCoverCloseTimeFourth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventCoverCloseTimeFifth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			if (event.getEventCoverOpenTimeFirst() != null)
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTCOVER, null, null,
						event.getEventCoverOpenTimeFirst(), event.getEventCoverOpenTimeSecond(),
						event.getEventCoverOpenTimeThird(), event.getEventCoverOpenTimeFourth(),
						event.getEventCoverOpenTimeFifth(), event.getEventCoverCloseTimeFirst(),
						event.getEventCoverCloseTimeSecond(), event.getEventCoverCloseTimeThird(),
						event.getEventCoverCloseTimeFourth(), event.getEventCoverCloseTimeFifth(), "3P");

			eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseCurrentPhaseAStart");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventReverseRunPhaseAFirstStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventReverseRunPhaseASecondStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventReverseRunPhaseAThirdStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventReverseRunPhaseAFourthStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventReverseRunPhaseAFifthStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseCurrentPhaseAEnd");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventReverseRunPhaseAFirstEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventReverseRunPhaseASecondEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventReverseRunPhaseAThirdEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventReverseRunPhaseAFourthEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventReverseRunPhaseAFifthEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			if (event.getEventReverseRunPhaseAFirstStart() != null)
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTREVERSERUNPHASEA, null, null,
						event.getEventReverseRunPhaseAFirstStart(), event.getEventReverseRunPhaseASecondStart(),
						event.getEventReverseRunPhaseAThirdStart(), event.getEventReverseRunPhaseAFourthStart(),
						event.getEventReverseRunPhaseAFifthStart(), event.getEventReverseRunPhaseAFirstEnd(),
						event.getEventReverseRunPhaseASecondEnd(), event.getEventReverseRunPhaseAThirdEnd(),
						event.getEventReverseRunPhaseAFourthEnd(), event.getEventReverseRunPhaseAFifthEnd(), "3P");

			eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseCurrentPhaseBStart");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventReverseRunPhaseBFirstStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventReverseRunPhaseBSecondStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventReverseRunPhaseBThirdStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventReverseRunPhaseBFourthStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventReverseRunPhaseBFifthStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseCurrentPhaseBEnd");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventReverseRunPhaseBFirstEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventReverseRunPhaseBSecondEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventReverseRunPhaseBThirdEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventReverseRunPhaseBFourthEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventReverseRunPhaseBFifthEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			if (event.getEventReverseRunPhaseBFirstStart() != null)
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTREVERSERUNPHASEB, null, null,
						event.getEventReverseRunPhaseBFirstStart(), event.getEventReverseRunPhaseBSecondStart(),
						event.getEventReverseRunPhaseBThirdStart(), event.getEventReverseRunPhaseBFourthStart(),
						event.getEventReverseRunPhaseBFifthStart(), event.getEventReverseRunPhaseBFirstEnd(),
						event.getEventReverseRunPhaseBSecondEnd(), event.getEventReverseRunPhaseBThirdEnd(),
						event.getEventReverseRunPhaseBFourthEnd(), event.getEventReverseRunPhaseBFifthEnd(), "3P");

			eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseCurrentPhaseCStart");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventReverseRunPhaseCFirstStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventReverseRunPhaseCSecondStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventReverseRunPhaseCThirdStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventReverseRunPhaseCFourthStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventReverseRunPhaseCFifthStart(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			eventValueFullList = dataCollection.getDataFieldByObisName("EventReverseCurrentPhaseCEnd");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventReverseRunPhaseCFirstEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventReverseRunPhaseCSecondEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventReverseRunPhaseCThirdEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventReverseRunPhaseCFourthEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventReverseRunPhaseCFifthEnd(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			if (event.getEventReverseRunPhaseCFirstStart() != null)
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTREVERSERUNPHASEC, null, null,
						event.getEventReverseRunPhaseCFirstStart(), event.getEventReverseRunPhaseCSecondStart(),
						event.getEventReverseRunPhaseCThirdStart(), event.getEventReverseRunPhaseCFourthStart(),
						event.getEventReverseRunPhaseCFifthStart(), event.getEventReverseRunPhaseCFirstEnd(),
						event.getEventReverseRunPhaseCSecondEnd(), event.getEventReverseRunPhaseCThirdEnd(),
						event.getEventReverseRunPhaseCFourthEnd(), event.getEventReverseRunPhaseCFifthEnd(), "3P");

			eventValueFullList = dataCollection.getDataFieldByObisName("EventImbalancePowerStart");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventImbalancePowerStartFirst(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventImbalancePowerStartSecond(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventImbalancePowerStartThird(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventImbalancePowerStartFourth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventImbalancePowerStartFifth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			eventValueFullList = dataCollection.getDataFieldByObisName("EventImbalancePowerEnd");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventImbalancePowerEndFirst(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventImbalancePowerEndSecond(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventImbalancePowerEndThird(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventImbalancePowerEndFourth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventImbalancePowerEndFifth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			if (event.getEventImbalancePowerStartFirst() != null)
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTIMBALANCEPOWER, null, null,
						event.getEventImbalancePowerStartFirst(), event.getEventImbalancePowerStartSecond(),
						event.getEventImbalancePowerStartThird(), event.getEventImbalancePowerStartFourth(),
						event.getEventImbalancePowerStartFifth(), event.getEventImbalancePowerEndFirst(),
						event.getEventImbalancePowerEndSecond(), event.getEventImbalancePowerEndThird(),
						event.getEventImbalancePowerEndFourth(), event.getEventImbalancePowerEndFifth(), "3P");

			eventValueFullList = dataCollection.getDataFieldByObisName("EventReversePowerStart");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventReversePowerStartFirst(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventReversePowerStartSecond(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventReversePowerStartThird(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventReversePowerStartFourth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventReversePowerStartFifth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			eventValueFullList = dataCollection.getDataFieldByObisName("EventReversePowerEnd");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventReversePowerEndFirst(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventReversePowerEndSecond(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventReversePowerEndThird(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventReversePowerEndFourth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventReversePowerEndFifth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			if (event.getEventReversePowerStartFirst() != null)
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTREVERSEPOWER, null, null,
						event.getEventReversePowerStartFirst(), event.getEventReversePowerStartSecond(),
						event.getEventReversePowerStartThird(), event.getEventReversePowerStartFourth(),
						event.getEventReversePowerStartFifth(), event.getEventReversePowerEndFirst(),
						event.getEventReversePowerEndSecond(), event.getEventReversePowerEndThird(),
						event.getEventReversePowerEndFourth(), event.getEventReversePowerEndFifth(), "3P");

			eventValueFullList = dataCollection.getDataFieldByObisName("EventStealElectricity");
			if (eventValueFullList.getEventList().size() > 0) {
				event.setEventStealElectricityFirst(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 1), eventValueFullList)));
				event.setEventStealElectricitySecond(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 2), eventValueFullList)));
				event.setEventStealElectricityThird(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 3), eventValueFullList)));
				event.setEventStealElectricityFourth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 4), eventValueFullList)));
				event.setEventStealElectricityFifth(this.convertMeterTime(this.getEventValue(
						Integer.toString(eventValueFullList.getEventList().size() - 5), eventValueFullList)));
			}
			if (event.getEventStealElectricityFirst() != null)
				eventMeterList.add(dcuCode, meterId, event.getServerTime(), Constant.EVENTSTEALELECTRICITY, null, null,
						event.getEventStealElectricityFirst(), event.getEventStealElectricitySecond(),
						event.getEventStealElectricityThird(), event.getEventStealElectricityFourth(),
						event.getEventStealElectricityFifth(), null, null, null, null, null, "3P");

			if (eventMeterList.getEventMeterList().size() > 0) {
				iMT3Business.insertEvent(eventMeterList);
			}

			eventMeterList = null;
			event = null;
			eventValueFullList = null;
			
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeEventPacket", ex);
		}

	}

	public  void decodeOperationPacket(String topic, String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		MT3OperationEntity op = new MT3OperationEntity();
		DataCollection scaleList = new DataCollection();
		try {
			iMT3Business.getMeterObisScale(meterId, null, scaleList);
			if (scaleList.getdataList().isEmpty())
			{
				// 2 Not in DB
				this.save.UpdateMessageLog(topic, 2);
				logUlti.writeLog(LogType.INFO,"GELEX - DCU:"+dcuCode+" - METER:"+meterId+
						" - decodeOperationPacket: RETURN - scaleList.getdataList().isEmpty()");
				return;
			}
			op.setTopic(topic);
			// Get Operation data
			ThreePhasePacketData.setMeasurementPointDefaultConfig(commandLine, dataCollection);
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

			op.setEnergyPlusArate1(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergyPlusArate1"),
					scaleList, "EnergyPlusArate1"));
			op.setEnergyPlusArate2(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergyPlusArate2"),
					scaleList, "EnergyPlusArate2"));
			op.setEnergyPlusArate3(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergyPlusArate3"),
					scaleList, "EnergyPlusArate3"));
			op.setEnergySubArate1(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergySubArate1"),
					scaleList, "EnergySubArate1"));
			op.setEnergySubArate2(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergySubArate2"),
					scaleList, "EnergySubArate2"));
			op.setEnergySubArate3(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergySubArate3"),
					scaleList, "EnergySubArate3"));
			op.setExportWh(
					this.calculatorConvertToFloat(dataCollection.getDatabyName("ExportWh"), scaleList, "ExportWh"));
			op.setImportWh(
					this.calculatorConvertToFloat(dataCollection.getDatabyName("ImportWh"), scaleList, "ImportWh"));
			op.setQ1(this.calculatorConvertToFloat(dataCollection.getDatabyName("Q1"), scaleList, "Q1"));
			op.setQ3(this.calculatorConvertToFloat(dataCollection.getDatabyName("Q3"), scaleList, "Q3"));
			op.setMaxDemandPlusArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate1"), scaleList, "MaxDemandPlusArate1"));
			op.setMaxDemandPlusArate1Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate1Time")));
			op.setMaxDemandSubArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate1"), scaleList, "MaxDemandSubArate1"));
			op.setMaxDemandSubArate1Time(this.convertMeterTime(dataCollection.getDatabyName("MaxDemandSubArate1Time")));
			op.setMaxDemandPlusArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate2"), scaleList, "MaxDemandPlusArate2"));
			op.setMaxDemandPlusArate2Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate2Time")));
			op.setMaxDemandSubArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate2"), scaleList, "MaxDemandSubArate2"));
			op.setMaxDemandSubArate2Time(this.convertMeterTime(dataCollection.getDatabyName("MaxDemandSubArate2Time")));
			op.setMaxDemandPlusArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate3"), scaleList, "MaxDemandPlusArate3"));
			op.setMaxDemandPlusArate3Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate3Time")));
			op.setMaxDemandSubArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate3"), scaleList, "MaxDemandSubArate3"));
			op.setMaxDemandSubArate3Time(this.convertMeterTime(dataCollection.getDatabyName("MaxDemandSubArate3Time")));
			op.setTiT(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("Ti")));
			op.setTiM(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("TiM")));
			if (op.getTiM() != null)
				op.setTiM(op.getTiM().divide(BigDecimal.valueOf(10)));
			op.setTi(op.getTiM() == BigDecimal.valueOf(0) && (op.getTiM() == null) ? op.getTiT()
					: op.getTiT().divide(op.getTiM(), 2, RoundingMode.CEILING));

			op.setTuT(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("Tu")));
			op.setTuM(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("TuM")));
			if (op.getTuM() != null)
				op.setTuM(op.getTuM().divide(BigDecimal.valueOf(10)));
			op.setTu(op.getTuM() == BigDecimal.valueOf(0) && (op.getTuM() == null) ? op.getTuT()
					: op.getTuT().divide(op.getTuM(), 2, RoundingMode.CEILING));
			op.setReactiveEnergyPlusArate1(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate1"), scaleList, "ReactiveEnergyPlusArate1"));
			op.setReactiveEnergyPlusArate2(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate2"), scaleList, "ReactiveEnergyPlusArate2"));
			op.setReactiveEnergyPlusArate3(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate3"), scaleList, "ReactiveEnergyPlusArate3"));
			op.setReactiveEnergySubArate1(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergySubArate1"), scaleList, "ReactiveEnergySubArate1"));
			op.setReactiveEnergySubArate2(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergySubArate2"), scaleList, "ReactiveEnergySubArate2"));
			op.setReactiveEnergySubArate3(this.calculatorConvertToFloat(
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

	public  void decodeInstantaneousPacket(String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		MT3OperationEntity ins = new MT3OperationEntity();
		DataCollection scaleList = new DataCollection();
		try {
			iMT3Business.getMeterObisScale(meterId, null, scaleList);
			if (scaleList.getdataList().isEmpty())
				{
				logUlti.writeLog(LogType.INFO,"GELEX - DCU:"+dcuCode+" - METER:"+meterId+
						" - decodeInstantaneousPacket: RETURN - scaleList.getdataList().isEmpty()");
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

			ins.setEnergyPlusArate1(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergyPlusArate1"),
					scaleList, "EnergyPlusArate1"));
			ins.setEnergyPlusArate2(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergyPlusArate2"),
					scaleList, "EnergyPlusArate2"));
			ins.setEnergyPlusArate3(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergyPlusArate3"),
					scaleList, "EnergyPlusArate3"));
			ins.setEnergySubArate1(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergySubArate1"),
					scaleList, "EnergySubArate1"));
			ins.setEnergySubArate2(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergySubArate2"),
					scaleList, "EnergySubArate2"));
			ins.setEnergySubArate3(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergySubArate3"),
					scaleList, "EnergySubArate3"));
			ins.setExportWh(
					this.calculatorConvertToFloat(dataCollection.getDatabyName("ExportWh"), scaleList, "ExportWh"));
			ins.setImportWh(
					this.calculatorConvertToFloat(dataCollection.getDatabyName("ImportWh"), scaleList, "ImportWh"));
			ins.setQ1(this.calculatorConvertToFloat(dataCollection.getDatabyName("Q1"), scaleList, "Q1"));
			ins.setQ3(this.calculatorConvertToFloat(dataCollection.getDatabyName("Q3"), scaleList, "Q3"));
			ins.setMaxDemandPlusArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate1"), scaleList, "MaxDemandPlusArate1"));
			ins.setMaxDemandPlusArate1Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate1Time")));
			ins.setMaxDemandSubArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate1"), scaleList, "MaxDemandSubArate1"));
			ins.setMaxDemandSubArate1Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandSubArate1Time")));
			ins.setMaxDemandPlusArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate2"), scaleList, "MaxDemandPlusArate2"));
			ins.setMaxDemandPlusArate2Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate2Time")));
			ins.setMaxDemandSubArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate2"), scaleList, "MaxDemandSubArate2"));
			ins.setMaxDemandSubArate2Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandSubArate2Time")));
			ins.setMaxDemandPlusArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate3"), scaleList, "MaxDemandPlusArate3"));
			ins.setMaxDemandPlusArate3Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate3Time")));
			ins.setMaxDemandSubArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandSubArate3"), scaleList, "MaxDemandSubArate3"));
			ins.setMaxDemandSubArate3Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandSubArate3Time")));
			ins.setTiT(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("Ti")));
			ins.setTiM(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("TiM")));
			if (ins.getTiM() != null)
				ins.setTiM(ins.getTiM().divide(BigDecimal.valueOf(10)));
			ins.setTi(ins.getTiM() == BigDecimal.valueOf(0) && (ins.getTiM() == null) ? ins.getTiT()
					: ins.getTiT().divide(ins.getTiM(), 2, RoundingMode.CEILING));

			ins.setTuT(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("Tu")));
			ins.setTuM(ConvertUlti.convertHexToDecimal(dataCollection.getDatabyName("TuM")));
			if (ins.getTuM() != null)
				ins.setTuM(ins.getTuM().divide(BigDecimal.valueOf(10)));
			ins.setTu(ins.getTuM() == BigDecimal.valueOf(0) && (ins.getTuM() == null) ? ins.getTuT()
					: ins.getTuT().divide(ins.getTuM(), 2, RoundingMode.CEILING));
			ins.setReactiveEnergyPlusArate1(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate1"), scaleList, "ReactiveEnergyPlusArate1"));
			ins.setReactiveEnergyPlusArate2(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate2"), scaleList, "ReactiveEnergyPlusArate2"));
			ins.setReactiveEnergyPlusArate3(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate3"), scaleList, "ReactiveEnergyPlusArate3"));
			ins.setReactiveEnergySubArate1(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergySubArate1"), scaleList, "ReactiveEnergySubArate1"));
			ins.setReactiveEnergySubArate2(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergySubArate2"), scaleList, "ReactiveEnergySubArate2"));
			ins.setReactiveEnergySubArate3(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergySubArate3"), scaleList, "ReactiveEnergySubArate3"));

			ins.setApparentPowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("ApparentPowerPhaseTotal"), scaleList, "ApparentPowerPhaseTotal"));

			iMT3Business.insertIntantaneous(ins);

			ins = null;
			scaleList = null;		
			
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeInstantaneousPacket", ex);
		}
	}

	@Override
	public  void decodeHistoricalPacket(String topic, String dcuCode, String meterId, String commandLine,
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

			hi.setBeginHistoricalTime(this.convertMeterTime(dataCollection.getDatabyName("BeginHistoricalTime")));

			if (hi.getBeginHistoricalTime() == null)
			{
				logUlti.writeLog(LogType.INFO,"GELEX - DCU:"+dcuCode+" - METER:"+meterId+
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

			hi.setImportWh(
					this.calculatorConvertToFloat(dataCollection.getDatabyName("ImportWh"), scaleList, "ImportWh"));

			hi.setExportWh(
					this.calculatorConvertToFloat(dataCollection.getDatabyName("ExportWh"), scaleList, "ExportWh"));

			hi.setEnergyPlusARate1(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergyPlusArate1"),
					scaleList, "EnergyPlusArate1"));

			hi.setEnergyPlusARate2(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergyPlusArate2"),
					scaleList, "EnergyPlusArate2"));

			hi.setEnergyPlusARate3(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergyPlusArate3"),
					scaleList, "EnergyPlusArate3"));

			hi.setEnergySubARate1(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergySubArate1"),
					scaleList, "EnergySubArate1"));

			hi.setEnergySubARate2(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergySubArate2"),
					scaleList, "EnergySubArate2"));

			hi.setEnergySubARate3(this.calculatorConvertToFloat(dataCollection.getDatabyName("EnergySubArate3"),
					scaleList, "EnergySubArate3"));

			hi.setQ1(this.calculatorConvertToFloat(dataCollection.getDatabyName("Q1"), scaleList, "Q1"));
			hi.setQ3(this.calculatorConvertToFloat(dataCollection.getDatabyName("Q3"), scaleList, "Q3"));

			hi.setMaxDemandPlusArate1(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate1"), scaleList, "MaxDemandPlusArate1"));
			hi.setMaxDemandPlusArate1Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate1Time")));

			hi.setMaxDemandPlusArate2(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate2"), scaleList, "MaxDemandPlusArate2"));
			hi.setMaxDemandPlusArate2Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate2Time")));

			hi.setMaxDemandPlusArate3(this.calculatorConvertHexToIntIsNull(
					dataCollection.getDatabyName("MaxDemandPlusArate3"), scaleList, "MaxDemandPlusArate3"));
			hi.setMaxDemandPlusArate3Time(
					this.convertMeterTime(dataCollection.getDatabyName("MaxDemandPlusArate3Time")));

			hi.setReactiveEnergyPlusArate1(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate1"), scaleList, "ReactiveEnergyPlusArate1"));
			hi.setReactiveEnergyPlusArate2(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate2"), scaleList, "ReactiveEnergyPlusArate2"));
			hi.setReactiveEnergyPlusArate3(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergyPlusArate3"), scaleList, "ReactiveEnergyPlusArate3"));
			hi.setReactiveEnergySubArate1(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergySubArate1"), scaleList, "ReactiveEnergySubArate1"));
			hi.setReactiveEnergySubArate2(this.calculatorConvertToFloat(
					dataCollection.getDatabyName("ReactiveEnergySubArate2"), scaleList, "ReactiveEnergySubArate2"));
			hi.setReactiveEnergySubArate3(this.calculatorConvertToFloat(
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
	public  void decodeMeasurementPointScale(String dcuCode, String meterId, String meterType,
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

			scaleList = null;
			scale = null;
			
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeMeasurementPointScale", ex);
		}
	}

	public  void decodeLoadProfilePacket(String dcuCode, String meterId, String commandLine,
			DataCollection dataCollection, IMT3Business iMT3Business) {
		MT3LoadProfile3PMessageEntity load = new MT3LoadProfile3PMessageEntity();
		MT3LoadProfilePacketList loadProfileList = new MT3LoadProfilePacketList();
		try {

			DataCollection scaleList = new DataCollection();
			iMT3Business.getMeterObisScale(meterId, null, scaleList);
			if (scaleList.getdataList().isEmpty())
			{
				logUlti.writeLog(LogType.INFO,"GELEX - DCU:"+dcuCode+" - METER:"+meterId+
						" - decodeLoadProfilePacket: RETURN - scaleList.getdataList().isEmpty()");
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
				load.setActivePowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ActivePowerPhaseTotal"), scaleList, "ActivePowerPhaseTotal"));
				load.setActivePowerPhaseA(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ActivePowerPhaseA"), scaleList, "ActivePowerPhaseA"));
				load.setActivePowerPhaseB(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ActivePowerPhaseB"), scaleList, "ActivePowerPhaseB"));
				load.setActivePowerPhaseC(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ActivePowerPhaseC"), scaleList, "ActivePowerPhaseC"));
				load.setPhaseAnglePhaseA(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("PhaseAnglePhaseA"), scaleList, "PhaseAnglePhaseA"));
				load.setPhaseAnglePhaseB(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("PhaseAnglePhaseB"), scaleList, "PhaseAnglePhaseB"));
				load.setPhaseAnglePhaseC(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("PhaseAnglePhaseC"), scaleList, "PhaseAnglePhaseC"));
				load.setApparentPowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ApparentPowerPhaseTotal"), scaleList, "ApparentPowerPhaseTotal"));
				load.setApparentPowerPhaseA(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ApparentPowerPhaseA"), scaleList, "ApparentPowerPhaseA"));
				load.setApparentPowerPhaseB(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ApparentPowerPhaseB"), scaleList, "ApparentPowerPhaseB"));
				load.setApparentPowerPhaseC(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ApparentPowerPhaseC"), scaleList, "ApparentPowerPhaseC"));
				load.setPowerFactorPhaseTotal(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("PowerFactorPhaseTotal"), scaleList, "PowerFactorPhaseTotal"));
				load.setPowerFactorPhaseA(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("PowerFactorPhaseA"), scaleList, "PowerFactorPhaseA"));
				load.setPowerFactorPhaseB(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("PowerFactorPhaseB"), scaleList, "PowerFactorPhaseB"));
				load.setPowerFactorPhaseC(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("PowerFactorPhaseC"), scaleList, "PowerFactorPhaseC"));
				load.setReactivePowerPhaseTotal(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ReactivePowerPhaseTotal"), scaleList, "ReactivePowerPhaseTotal"));
				load.setReactivePowerPhaseA(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ReactivePowerPhaseA"), scaleList, "ReactivePowerPhaseA"));
				load.setReactivePowerPhaseB(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ReactivePowerPhaseB"), scaleList, "ReactivePowerPhaseB"));
				load.setReactivePowerPhaseC(this.calculatorConvertHexToIntIsNull(
						profileList.getDatabyName("ReactivePowerPhaseC"), scaleList, "ReactivePowerPhaseC"));

				load.setDcuCode(dcuCode);
				load.setMeterCode(meterId);

				iMT3Business.insertLoadProfilePacket(load);

				load = null;
				loadProfileList = null;
				scaleList = null;
				
			}

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "decodeLoadProfilePacket", ex);
		}

	}

	private  BigDecimal getAlertValue(String dataByObisname, int value) {
		try {
			return ConvertUlti.convertHexToDecimal(dataByObisname).divide(BigDecimal.valueOf(value));
		} catch (Exception ex) {
		}
		return null;
	}

	private  BigDecimal getValueBy10PercentOver(BigDecimal freqOver) {
		if (freqOver != null)
			return freqOver.multiply(BigDecimal.valueOf(0.91)); // 10/11
		return null;
	}

	private  String convertSingal(String singalHex) {
		String result = null;
		try {
			result = ("-").concat(
					ConvertUlti.convertHexToDecimal(singalHex.substring(0, 2)) + " ," + singalHex.substring(2, 2));

		} catch (Exception ex) {
		}
		return result;
	}

	public  Timestamp convertMeterTime(String hexString) {
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

	public  Timestamp convertDateTime(String hexString) {

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

	private  BigDecimal calculatorConvertHexToIntIsNull(String hexString, DataCollection scaleList, String filename) {
		try {
			return BigDecimal.valueOf(ConvertUlti.hex2Int(hexString)).multiply((scaleList.getScalebyName(filename)));
		} catch (Exception ex) {
			return null;
		}
	}

	private  BigDecimal calculatorConvertToFloat(String hexString, DataCollection scaleList, String filename) {
		try {
			Long i = Long.parseLong(hexString, 16);
			Float dataFloat = Float.intBitsToFloat(i.intValue());
			return new BigDecimal(dataFloat).multiply((scaleList.getScalebyName(filename)));
		} catch (Exception ex) {
			return null;
		}
	}

	private  BigDecimal getScaleFromFirstMessage(String hexString) {
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
	private  String getEventValue(String fieldCode, DataField eventValueFullList) {
		// Auto-generated method stub
		DataField item = new DataField();
		int index = eventValueFullList.getEventList().indexOf(new DataField(StringUlti.PadLeft(fieldCode, 1, '0')));
		if (index > -1) {
			item = eventValueFullList.getEventList().get(index);
			return item.getData();
		}
		return null;
	}

	private  BigDecimal dataDivideValue(BigDecimal data, int value) {
		if (data == null)
			return null;
		return data.divide(BigDecimal.valueOf(value));
	}

}
