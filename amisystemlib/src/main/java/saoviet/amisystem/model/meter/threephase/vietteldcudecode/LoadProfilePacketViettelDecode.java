/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: LoadProfilePacketViettelDecode.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-06-06 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.vietteldcudecode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import saoviet.amisystem.business.MT3Business;
import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.datacollection.DataCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3LoadProfile3PMessageEntity;
import saoviet.amisystem.model.meter.threephase.packetvietteldcu.MessagePacketStructureViettelDcu;
import saoviet.amisystem.model.meter.threephase.packetvietteldcu.ThreePhaseViettelDcuStructData;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class LoadProfilePacketViettelDecode {
	private LogUlti logUlti = new LogUlti(LoadProfilePacketViettelDecode.class);
	private MT3Business iMt3Bussiness;
	private DataCollection loadProfileDefaultList;
	private SystemEventCallback systemEventCallback;

	public LoadProfilePacketViettelDecode() {
		iMt3Bussiness = new MT3Business();
		loadProfileDefaultList = MessagePacketStructureViettelDcu.getLoadProfileCollection();
	}

	public void loadProfilePacketDecode(MessageBase messageBase) {
		try {
			String dcuCode = messageBase.getDcuCode();
			String meterType = null;
			String commandLine = ConvertUlti.toHexString(messageBase.getData());
			DataCollection scaleList = new DataCollection();
			// Get meter scale form database
			this.iMt3Bussiness.getMeterObisScale(null, dcuCode, scaleList);
			// Lay type meter tu DataField dau tien trong list
			meterType = scaleList.getdataList().get(0).getType();
			// khong tim thay scale thi return
			if (meterType == null)
				return;
			DataCollection loadProfileList = this.loadProfileDefaultList.copy();
			ThreePhaseViettelDcuStructData.setDataForObisCode(commandLine, loadProfileList);
			switch (meterType) {
			case Constant.METER_TYPE_GELEX:
				this.loadprofileGelexPacketDeocode(dcuCode, loadProfileList, scaleList, this.iMt3Bussiness);
				break;
			case Constant.METER_TYPE_ELSTER:
				this.loadprofileElsterPacketDeocode(dcuCode, loadProfileList, scaleList, this.iMt3Bussiness);
				break;
			case Constant.METER_TYPE_GENIUS:
				this.loadprofileGeniusPacketDeocode(dcuCode, loadProfileList, scaleList, this.iMt3Bussiness);
				break;
			case Constant.METER_TYPE_STAR:
				this.loadprofileStarPacketDeocode(dcuCode, loadProfileList, scaleList, this.iMt3Bussiness);
				break;
			case Constant.METER_TYPE_LANDIS:
				this.loadprofileLandisPacketDeocode(dcuCode, loadProfileList, scaleList, this.iMt3Bussiness);
				break;
			default:
				break;
			}

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "loadprofilePacketDecode", ex);
		}

	}

	private void loadprofileLandisPacketDeocode(String dcuCode, DataCollection loadProfileList,
			DataCollection scaleList, MT3Business iMt3Bussiness) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3LoadProfile3PMessageEntity load = new MT3LoadProfile3PMessageEntity();

			valueList = loadProfileList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				load.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = loadProfileList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				load.setMeterTimeFrom(this.convertDateTime(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				load.setMeterTimeTo(this.convertDateTime(valueList.get(1)));

			valueList = loadProfileList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				load.setVoltagePhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = loadProfileList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				load.setVoltagePhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = loadProfileList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				load.setVoltagePhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = loadProfileList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				load.setCurrentPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = loadProfileList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				load.setCurrentPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = loadProfileList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				load.setCurrentPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));
			valueList = loadProfileList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				load.setQ1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList),
						1000));
			valueList = loadProfileList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				load.setQ3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList),
						1000));
			valueList = loadProfileList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				load.setImportWh(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList), 1000));

			valueList = loadProfileList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				load.setExportWh(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList), 1000));

			load.setDcuCode(dcuCode);

			iMt3Bussiness.insertLoadProfilePacket(load);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "loadProfileLandisPacketDecode", ex);
		}
	}

	private void loadprofileStarPacketDeocode(String dcuCode, DataCollection loadProfileList, DataCollection scaleList,
			MT3Business iMt3Bussiness) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3LoadProfile3PMessageEntity load = new MT3LoadProfile3PMessageEntity();

			valueList = loadProfileList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				load.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = loadProfileList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				load.setMeterTimeFrom(this.convertDateTime(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				load.setMeterTimeTo(this.convertDateTime(valueList.get(1)));

			valueList = loadProfileList.getValueListbyName("PowerAvgPos");
			if (valueList != null && valueList.size() > 0)
				load.setPowerAvgPos(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("PowerAvgNeg");
			if (valueList != null && valueList.size() > 0)
				load.setPowerAvgNeg(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("ReactivePowerAvgPos");
			if (valueList != null && valueList.size() > 0)
				load.setReactivePowerAvgPos(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("ReactivePowerAvgNeg");
			if (valueList != null && valueList.size() > 0)
				load.setReactivePowerAvgNeg(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("LastAvgPowerFactor");
			if (valueList != null && valueList.size() > 0)
				load.setLastAvgPowerFactor(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));


			load.setDcuCode(dcuCode);

			iMt3Bussiness.insertLoadProfilePacket(load);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "loadProfileStarPacketDecode", ex);
		}
	}

	private void loadprofileGeniusPacketDeocode(String dcuCode, DataCollection loadProfileList,
			DataCollection scaleList, MT3Business iMt3Bussiness) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3LoadProfile3PMessageEntity load = new MT3LoadProfile3PMessageEntity();

			valueList = loadProfileList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				load.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = loadProfileList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				load.setMeterTimeFrom(this.convertDateTime(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				load.setMeterTimeTo(this.convertDateTime(valueList.get(1)));

			valueList = loadProfileList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				load.setVoltagePhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = loadProfileList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				load.setVoltagePhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = loadProfileList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				load.setVoltagePhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = loadProfileList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				load.setCurrentPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = loadProfileList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				load.setCurrentPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = loadProfileList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				load.setCurrentPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));
			valueList = loadProfileList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				load.setQ1(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList));
			valueList = loadProfileList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				load.setQ3(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList));
			valueList = loadProfileList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				load.setImportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList));

			valueList = loadProfileList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				load.setExportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList));
			valueList = loadProfileList.getValueListbyName("PowerAvgPos");
			if (valueList != null && valueList.size() > 0)
				load.setPowerAvgPos(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("PowerAvgNeg");
			if (valueList != null && valueList.size() > 0)
				load.setPowerAvgNeg(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("ReactivePowerAvgPos");
			if (valueList != null && valueList.size() > 0)
				load.setReactivePowerAvgPos(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("ReactivePowerAvgNeg");
			if (valueList != null && valueList.size() > 0)
				load.setReactivePowerAvgNeg(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("LastAvgPowerFactor");
			if (valueList != null && valueList.size() > 0)
				load.setLastAvgPowerFactor(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));


			load.setDcuCode(dcuCode);

			iMt3Bussiness.insertLoadProfilePacket(load);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "loadProfileGeniusPacketDecode", ex);
		}
	}

	private void loadprofileElsterPacketDeocode(String dcuCode, DataCollection loadProfileList,
			DataCollection scaleList, MT3Business iMt3Bussiness) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3LoadProfile3PMessageEntity load = new MT3LoadProfile3PMessageEntity();

			valueList = loadProfileList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				load.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = loadProfileList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				load.setMeterTimeFrom(this.convertDateTime(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				load.setMeterTimeTo(this.convertDateTime(valueList.get(1)));

			valueList = loadProfileList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				load.setVoltagePhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = loadProfileList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				load.setVoltagePhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = loadProfileList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				load.setVoltagePhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = loadProfileList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				load.setCurrentPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = loadProfileList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				load.setCurrentPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = loadProfileList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				load.setCurrentPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));
			
			valueList = loadProfileList.getValueListbyName("PowerFactorPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				load.setPowerFactorPhaseTotal(
						this.convertNegativeValue(valueList.get(0), "PowerFactorPhaseTotal", scaleList));
			
			valueList = loadProfileList.getValueListbyName("PowerAvgPos");
			if (valueList != null && valueList.size() > 0)
				load.setPowerAvgPos(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("PowerAvgNeg");
			if (valueList != null && valueList.size() > 0)
				load.setPowerAvgNeg(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("ReactivePowerAvgPos");
			if (valueList != null && valueList.size() > 0)
				load.setReactivePowerAvgPos(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("ReactivePowerAvgNeg");
			if (valueList != null && valueList.size() > 0)
				load.setReactivePowerAvgNeg(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("LastAvgPowerFactor");
			if (valueList != null && valueList.size() > 0)
				load.setLastAvgPowerFactor(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));

			load.setDcuCode(dcuCode);

			iMt3Bussiness.insertLoadProfilePacket(load);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "loadProfileElsterPacketDecode", ex);
		}
	}

	private void loadprofileGelexPacketDeocode(String dcuCode, DataCollection loadProfileList, DataCollection scaleList,
			MT3Business iMt3Bussiness) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3LoadProfile3PMessageEntity load = new MT3LoadProfile3PMessageEntity();

			valueList = loadProfileList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				load.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = loadProfileList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				load.setMeterTimeFrom(this.convertDateTime(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				load.setMeterTimeTo(this.convertDateTime(valueList.get(1)));

			valueList = loadProfileList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				load.setVoltagePhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = loadProfileList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				load.setVoltagePhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = loadProfileList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				load.setVoltagePhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = loadProfileList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				load.setCurrentPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = loadProfileList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				load.setCurrentPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = loadProfileList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				load.setCurrentPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));

			valueList = loadProfileList.getValueListbyName("PowerFactorPhaseA");
			if (valueList != null && valueList.size() > 0)
				load.setPowerFactorPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseA", scaleList));
			if (load.getPowerFactorPhaseA() != null && load.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(1)) <= 0
					&& load.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(0)) >= 0)
				load.setPhaseAnglePhaseA(
						new BigDecimal(Math.acos(load.getPowerFactorPhaseA().doubleValue()) * (180 / Math.PI))
								.setScale(3, RoundingMode.CEILING));

			valueList = loadProfileList.getValueListbyName("PowerFactorPhaseB");
			if (valueList != null && valueList.size() > 0)
				load.setPowerFactorPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseB", scaleList));
			if (load.getPowerFactorPhaseB() != null && load.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(1)) <= 0
					&& load.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(0)) >= 0)
				load.setPhaseAnglePhaseB(
						new BigDecimal(Math.acos(load.getPowerFactorPhaseB().doubleValue()) * (180 / Math.PI))
								.setScale(3, RoundingMode.CEILING));

			valueList = loadProfileList.getValueListbyName("PowerFactorPhaseC");
			if (valueList != null && valueList.size() > 0)
				load.setPowerFactorPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseC", scaleList));
			if (load.getPowerFactorPhaseC() != null && load.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(1)) <= 0
					&& load.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(0)) >= 0)
				load.setPhaseAnglePhaseC(
						new BigDecimal(Math.acos(load.getPowerFactorPhaseC().doubleValue()) * (180 / Math.PI))
								.setScale(3, RoundingMode.CEILING));

			valueList = loadProfileList.getValueListbyName("PowerFactorPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				load.setPowerFactorPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));

			valueList = loadProfileList.getValueListbyName("ActivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				load.setActivePowerPhaseA(this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseA", scaleList));

			valueList = loadProfileList.getValueListbyName("ActivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				load.setActivePowerPhaseB(this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseB", scaleList));

			valueList = loadProfileList.getValueListbyName("ActivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				load.setActivePowerPhaseC(this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseC", scaleList));

			valueList = loadProfileList.getValueListbyName("ActivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				load.setActivePowerPhaseTotal(
						this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseTotal", scaleList));

			valueList = loadProfileList.getValueListbyName("ReactivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				load.setReactivePowerPhaseA(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseA", scaleList));

			valueList = loadProfileList.getValueListbyName("ReactivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				load.setReactivePowerPhaseB(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseB", scaleList));

			valueList = loadProfileList.getValueListbyName("ReactivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				load.setReactivePowerPhaseC(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseC", scaleList));

			valueList = loadProfileList.getValueListbyName("ReactivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				load.setReactivePowerPhaseTotal(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));

			valueList = loadProfileList.getValueListbyName("ApparentPowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				load.setApparentPowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseA", scaleList));
			if (load.getActivePowerPhaseA() == null && load.getReactivePowerPhaseA() == null)
				load.setApparentPowerPhaseA(null);

			valueList = loadProfileList.getValueListbyName("ApparentPowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				load.setApparentPowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseB", scaleList));
			if (load.getActivePowerPhaseB() == null && load.getReactivePowerPhaseB() == null)
				load.setApparentPowerPhaseB(null);

			valueList = loadProfileList.getValueListbyName("ApparentPowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				load.setApparentPowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseC", scaleList));
			if (load.getActivePowerPhaseC() == null && load.getReactivePowerPhaseC() == null)
				load.setApparentPowerPhaseC(null);

			valueList = loadProfileList.getValueListbyName("ApparentPowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				load.setApparentPowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseTotal", scaleList));

			valueList = loadProfileList.getValueListbyName("EnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				load.setEnergyPlusARate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate1", scaleList));

			valueList = loadProfileList.getValueListbyName("EnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				load.setEnergyPlusARate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate2", scaleList));

			valueList = loadProfileList.getValueListbyName("EnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				load.setEnergyPlusARate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate3", scaleList));

			valueList = loadProfileList.getValueListbyName("EnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				load.setEnergySubARate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate1", scaleList));

			valueList = loadProfileList.getValueListbyName("EnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				load.setEnergySubARate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate2", scaleList));

			valueList = loadProfileList.getValueListbyName("EnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				load.setEnergySubARate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate3", scaleList));
			valueList = loadProfileList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				load.setQ1(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList));
			valueList = loadProfileList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				load.setQ3(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList));
			valueList = loadProfileList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				load.setImportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList));

			valueList = loadProfileList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				load.setExportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList));

			valueList = loadProfileList.getValueListbyName("PowerAvgPos");
			if (valueList != null && valueList.size() > 0)
				load.setPowerAvgPos(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("PowerAvgNeg");
			if (valueList != null && valueList.size() > 0)
				load.setPowerAvgNeg(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("ReactivePowerAvgPos");
			if (valueList != null && valueList.size() > 0)
				load.setReactivePowerAvgPos(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("ReactivePowerAvgNeg");
			if (valueList != null && valueList.size() > 0)
				load.setReactivePowerAvgNeg(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));
			valueList = loadProfileList.getValueListbyName("LastAvgPowerFactor");
			if (valueList != null && valueList.size() > 0)
				load.setLastAvgPowerFactor(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));

			load.setDcuCode(dcuCode);

			iMt3Bussiness.insertLoadProfilePacket(load);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "loadProfileGelexPacketDecode", ex);
		}
	}

	private BigDecimal convertNegativeValue(String hexString, String filename, DataCollection scaleList) {
		try {
			byte[] value;
			if (hexString.substring(0, 2).equals("FF")) {
				value = ConvertUlti.toByteArray(hexString);
				for (int i = 0; i < value.length; i++)
					value[i] = (byte) ~value[i];
				long dataConvert = ConvertUlti.byteToLong(value);
				return BigDecimal.valueOf(~dataConvert).multiply((scaleList.getScalebyName(filename)));
			}
			return BigDecimal.valueOf(Long.parseLong(hexString, 16)).multiply((scaleList.getScalebyName(filename)));
		} catch (Exception ex) {
			return null;
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

	private BigDecimal calculatorConvertHexToIntIsNull(String hexString, String filename, DataCollection scaleList) {
		try {
			return BigDecimal.valueOf(ConvertUlti.hex2Long(hexString)).multiply((scaleList.getScalebyName(filename)));
		} catch (Exception ex) {
			return null;
		}
	}

	private BigDecimal dataDivideValue(BigDecimal data, int value) {
		if (data == null)
			return null;
		return data.divide(BigDecimal.valueOf(value));
	}
	

	public void setSystemEventCallBack(SystemEventCallback systemEventCallback) {
		this.systemEventCallback = systemEventCallback;
		iMt3Bussiness.setSystemEventCallback(this.systemEventCallback);
	}

}
