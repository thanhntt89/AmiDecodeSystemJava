/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: OperationPacketViettelDecode.java
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

import saoviet.amisystem.business.IMT3Business;
import saoviet.amisystem.business.MT3Business;
import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.DataField;
import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.datacollection.DataCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3OperationEntity;
import saoviet.amisystem.model.meter.threephase.packetvietteldcu.MessagePacketStructureViettelDcu;
import saoviet.amisystem.model.meter.threephase.packetvietteldcu.ThreePhaseViettelDcuStructData;
import saoviet.amisystem.ulti.ByteUltil;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.SaveMessageUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class OperationPacketViettelDecode {
	private LogUlti logUlti = new LogUlti(OperationPacketViettelDecode.class);
	private DataCollection operationListDefault = new DataCollection();
	private IMT3Business iMT3Business;
	private DataCollection elsterObisScaleList;
	private DataCollection starObisScaleList;
	private DataCollection gelexObisScaleList;
	private DataCollection landisObisScaleList;
	private DataCollection geniusObisScaleList;
	private SaveMessageUlti save = new SaveMessageUlti();
	private SystemEventCallback systemEventCallback;

	public void setSystemEventCallBack(SystemEventCallback systemEventCallback) {
		this.systemEventCallback = systemEventCallback;
		iMT3Business.setSystemEventCallback(this.systemEventCallback);
	}

	public OperationPacketViettelDecode() {
		this.operationListDefault = MessagePacketStructureViettelDcu.getOperationCollection();
		this.iMT3Business = new MT3Business();
		elsterObisScaleList = MessagePacketStructureViettelDcu.getElsterObisScaleList();
		starObisScaleList = MessagePacketStructureViettelDcu.getStarObisScaleList();
		gelexObisScaleList = MessagePacketStructureViettelDcu.getGelexObisScaleList();
		landisObisScaleList = MessagePacketStructureViettelDcu.getLandisObisScaleList();
		geniusObisScaleList = MessagePacketStructureViettelDcu.getGeniusObisScaleList();
	}

	public void operationPacketDecode(MessageBase messageBase) {
		try {
			String dcuCode = messageBase.getDcuCode();
			String commandLine = ConvertUlti.toHexString(messageBase.getData());
			String commandLineTemp = new String(commandLine);
			DataCollection operationList = this.operationListDefault.copy();
			ThreePhaseViettelDcuStructData.setDataForObisCode(commandLine, operationList);
			
			if (operationList.getValueListbyName("MeterId") != null) {
				this.opearationFirstDecode(dcuCode, commandLineTemp, operationList, this.iMT3Business);
				return;
			}

			String meterType = null;
			DataCollection scaleList = new DataCollection();
			// Get meter scale form database
			this.iMT3Business.getMeterObisScale(null, dcuCode, scaleList);
			if(scaleList.getdataList().isEmpty()) return;
			// Lay type meter tu DataField dau tien trong list
			meterType = scaleList.getdataList().get(0).getType();
			switch (meterType) {
			case Constant.METER_TYPE_GELEX:
				this.operationGelexPacketDeocode(messageBase.getPreTopic(), dcuCode, operationList, scaleList,
						iMT3Business);
				break;
			case Constant.METER_TYPE_ELSTER:
				this.operationElsterPacketDeocode(messageBase.getPreTopic(), dcuCode, operationList, scaleList,
						iMT3Business);
				break;
			case Constant.METER_TYPE_GENIUS:
				this.operationGeniusPacketDeocode(messageBase.getPreTopic(), dcuCode, operationList, scaleList,
						iMT3Business);
				break;
			case Constant.METER_TYPE_STAR:
				this.operationStarPacketDeocode(messageBase.getPreTopic(), dcuCode, operationList, scaleList,
						iMT3Business);
				break;
			case Constant.METER_TYPE_LANDIS:
				this.operationLandisPacketDeocode(messageBase.getPreTopic(), dcuCode, operationList, scaleList,
						iMT3Business);
				break;
			default:
				break;
			}

		} catch (Exception ex) {
			// 2 not mp
			this.save.UpdateMessageLog(messageBase.getPreTopic(), 2);
			logUlti.writeLog(LogType.ERROR, "operationPacketDecode", ex);
		}
	}

	private void operationLandisPacketDeocode(String topic, String dcuCode, DataCollection operationList,
			DataCollection scaleList, IMT3Business iMt3Business) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3OperationEntity op = new MT3OperationEntity();
			op.setTopic(topic);
			valueList = operationList.getValueListbyName("Tu");
			if (valueList != null && valueList.size() > 0)
				op.setTu(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				op.setTuT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				op.setTuM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("Ti");
			if (valueList != null && valueList.size() > 0)
				op.setTi(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				op.setTiT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				op.setTiM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				op.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				op.setMeterTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseB(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseC(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseB(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseC(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseA", scaleList));
			if (op.getPowerFactorPhaseA() != null && op.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseA(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseA().doubleValue()) * (180 / Math.PI)).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseB", scaleList));
			if (op.getPowerFactorPhaseB() != null && op.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseB(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseB().doubleValue()) * (180 / Math.PI)).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseC", scaleList));
			if (op.getPowerFactorPhaseC() != null && op.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseC(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseC().doubleValue()) * (180 / Math.PI)).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL2subUL1");
			if (valueList != null && valueList.size() > 0)
				op.setAngleofUL2subUL1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL2subUL1", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL1subUL3");
			if (valueList != null && valueList.size() > 0)
				op.setAngleofUL1subUL3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL1subUL3", scaleList));
			BigDecimal freqA = null, freqB = null, freqC = null;
			valueList = operationList.getValueListbyName("FreqA");
			if (valueList != null && valueList.size() > 0)
				freqA = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqA", scaleList);
			valueList = operationList.getValueListbyName("FreqB");
			if (valueList != null && valueList.size() > 0)
				freqB = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqB", scaleList);
			valueList = operationList.getValueListbyName("FreqC");
			if (valueList != null && valueList.size() > 0)
				freqC = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqC", scaleList);
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
			if (op.getPowerFactorPhaseA() != null)
				op.setActivePowerPhaseA(this.dataDivideValue(
						op.getVoltagePhaseA().multiply(op.getCurrentPhaseA().multiply(op.getPowerFactorPhaseA())),
						1000));
			if (op.getPowerFactorPhaseB() != null)
				op.setActivePowerPhaseB(this.dataDivideValue(
						op.getVoltagePhaseB().multiply(op.getCurrentPhaseB().multiply(op.getPowerFactorPhaseB())),
						1000));
			if (op.getPowerFactorPhaseC() != null)
				op.setActivePowerPhaseC(this.dataDivideValue(
						op.getVoltagePhaseC().multiply(op.getCurrentPhaseC().multiply(op.getPowerFactorPhaseC())),
						1000));
			if (op.getActivePowerPhaseA() != null && op.getActivePowerPhaseB() != null
					&& op.getActivePowerPhaseC() != null)
				op.setActivePowerPhaseTotal(
						op.getActivePowerPhaseA().add(op.getActivePowerPhaseB().add(op.getActivePowerPhaseC())));
			if (op.getPowerFactorPhaseA() != null)
				op.setReactivePowerPhaseA(
						this.dataDivideValue(
								op.getVoltagePhaseA()
										.multiply(op.getCurrentPhaseA()).multiply(new BigDecimal(Math.sqrt(BigDecimal
												.valueOf(1).subtract(op.getPowerFactorPhaseA().pow(2)).doubleValue()))),
								1000));
			if (op.getPowerFactorPhaseB() != null)
				op.setReactivePowerPhaseB(
						this.dataDivideValue(
								op.getVoltagePhaseB()
										.multiply(op.getCurrentPhaseB()).multiply(new BigDecimal(Math.sqrt(BigDecimal
												.valueOf(1).subtract(op.getPowerFactorPhaseB().pow(2)).doubleValue()))),
								1000));
			if (op.getPowerFactorPhaseC() != null)
				op.setReactivePowerPhaseC(
						this.dataDivideValue(
								op.getVoltagePhaseC()
										.multiply(op.getCurrentPhaseC()).multiply(new BigDecimal(Math.sqrt(BigDecimal
												.valueOf(1).subtract(op.getPowerFactorPhaseC().pow(2)).doubleValue()))),
								1000));
			if (op.getReactivePowerPhaseA() != null && op.getReactivePowerPhaseB() != null
					&& op.getReactivePowerPhaseC() != null)
				op.setReactivePowerPhaseTotal(
						op.getReactivePowerPhaseA().add(op.getReactivePowerPhaseB().add(op.getReactivePowerPhaseC())));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseA", scaleList));
			if (op.getApparentPowerPhaseA() == null && op.getActivePowerPhaseA() != null
					&& op.getReactivePowerPhaseA() != null)
				op.setApparentPowerPhaseA(new BigDecimal(Math
						.sqrt(op.getActivePowerPhaseA().pow(2).add(op.getReactivePowerPhaseA().pow(2)).doubleValue()))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseB", scaleList));
			if (op.getApparentPowerPhaseB() == null && op.getActivePowerPhaseB() != null
					&& op.getReactivePowerPhaseB() != null)
				op.setApparentPowerPhaseB(new BigDecimal(Math
						.sqrt(op.getActivePowerPhaseB().pow(2).add(op.getReactivePowerPhaseB().pow(2)).doubleValue()))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseC", scaleList));
			if (op.getApparentPowerPhaseC() == null && op.getActivePowerPhaseC() != null
					&& op.getReactivePowerPhaseC() != null)
				op.setApparentPowerPhaseC(new BigDecimal(Math
						.sqrt(op.getActivePowerPhaseC().pow(2).add(op.getReactivePowerPhaseC().pow(2)).doubleValue()))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseTotal", scaleList));
			if (op.getApparentPowerPhaseTotal() == null && op.getApparentPowerPhaseA() != null
					&& op.getApparentPowerPhaseB() != null && op.getApparentPowerPhaseC() != null)
				op.setApparentPowerPhaseTotal(
						op.getApparentPowerPhaseA().add(op.getApparentPowerPhaseB().add(op.getApparentPowerPhaseC())));

			valueList = operationList.getValueListbyName("EnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate1(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate1", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate2(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate2", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate3(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate3", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergyPlusARate4");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate4(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate4", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate1(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate1", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate2(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate2", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate3(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate3", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergySubARate4");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate4(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate4", scaleList), 1000));

			valueList = operationList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				op.setQ1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList),
						1000));

			valueList = operationList.getValueListbyName("Q2");
			if (valueList != null && valueList.size() > 0)
				op.setQ2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q2", scaleList),
						1000));

			valueList = operationList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				op.setQ3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList),
						1000));

			valueList = operationList.getValueListbyName("Q4");
			if (valueList != null && valueList.size() > 0)
				op.setQ4(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q4", scaleList),
						1000));

			valueList = operationList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				op.setImportWh(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList), 1000));

			valueList = operationList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				op.setExportWh(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergyPlusVA");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusVA(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusVA", scaleList), 1000));

			valueList = operationList.getValueListbyName("MaxDemandPlusA");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusA(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusA", scaleList), 1000));

			valueList = operationList.getValueListbyName("MaxDemandSubA");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubA(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubA", scaleList), 1000));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate1(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate1", scaleList),
						1000));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate1(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate1", scaleList), 1000));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate2(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate2", scaleList),
						1000));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate2(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate2", scaleList), 1000));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate3(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate3", scaleList),
						1000));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate3(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate3", scaleList), 1000));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate4");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate4(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate4", scaleList),
						1000));

			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate4");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate4(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate4", scaleList), 1000));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("PhaseRotation");
			if (valueList != null && valueList.size() > 0)
				op.setPhaseRotation(ConvertUlti.convertHexToDecimalString(valueList.get(0)));

			valueList = operationList.getValueListbyName("Signal");
			if (valueList != null && valueList.size() > 0)
				op.setSignal(ConvertUlti.convertNegativeValue(valueList.get(0)));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate3", scaleList));

			op.setDcuCode(dcuCode);
			op.setDcuType(Constant.DCU_TYPE_VIETTEL);

			iMt3Business.addMessageOperation(op);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "operationLandisPacketDecode", ex);
		}
	}

	/**
	 *  
	 * @param topic
	 * @param dcuCode
	 * @param operationList
	 * @param scaleList
	 * @param iMt3Business
	 */
	private void operationStarPacketDeocode(String topic, String dcuCode, DataCollection operationList,
			DataCollection scaleList, IMT3Business iMt3Business) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3OperationEntity op = new MT3OperationEntity();
			op.setTopic(topic);
			valueList = operationList.getValueListbyName("Tu");
			if (valueList != null && valueList.size() > 0)
				op.setTu(this.convertValue(valueList.get(0)));

			valueList = operationList.getValueListbyName("Ti");
			if (valueList != null && valueList.size() > 0)
				op.setTi(this.convertValue(valueList.get(0)));

			valueList = operationList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				op.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				op.setMeterTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseB(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseC(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseB(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseC(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseA", scaleList));
			if (op.getPowerFactorPhaseA() != null && op.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseA(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseA().doubleValue()) * (180 / Math.PI)).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseB", scaleList));
			if (op.getPowerFactorPhaseB() != null && op.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseB(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseB().doubleValue()) * (180 / Math.PI)).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseC", scaleList));
			if (op.getPowerFactorPhaseC() != null && op.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseC(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseC().doubleValue()) * (180 / Math.PI)).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL2subUL1");
			if (valueList != null && valueList.size() > 0)
				op.setAngleofUL2subUL1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL2subUL1", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL1subUL3");
			if (valueList != null && valueList.size() > 0)
				op.setAngleofUL1subUL3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL1subUL3", scaleList));
			BigDecimal freqA = null, freqB = null, freqC = null;
			valueList = operationList.getValueListbyName("FreqA");
			if (valueList != null && valueList.size() > 0)
				freqA = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqA", scaleList);
			valueList = operationList.getValueListbyName("FreqB");
			if (valueList != null && valueList.size() > 0)
				freqB = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqB", scaleList);
			valueList = operationList.getValueListbyName("FreqC");
			if (valueList != null && valueList.size() > 0)
				freqC = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqC", scaleList);
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

			valueList = operationList.getValueListbyName("ActivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));
			if (op.getReactivePowerPhaseTotal() == null && op.getReactivePowerPhaseA() != null
					&& op.getReactivePowerPhaseB() != null && op.getReactivePowerPhaseC() != null)
				op.setReactivePowerPhaseTotal(
						op.getReactivePowerPhaseA().add(op.getReactivePowerPhaseB().add(op.getReactivePowerPhaseC())));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseA", scaleList));
			if (op.getApparentPowerPhaseA() == null && op.getActivePowerPhaseA() != null
					&& op.getReactivePowerPhaseA() != null)
				op.setApparentPowerPhaseA(new BigDecimal(Math
						.sqrt(op.getActivePowerPhaseA().pow(2).add(op.getReactivePowerPhaseA().pow(2)).doubleValue()))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseB", scaleList));
			if (op.getApparentPowerPhaseB() == null && op.getActivePowerPhaseB() != null
					&& op.getReactivePowerPhaseB() != null)
				op.setApparentPowerPhaseB(new BigDecimal(Math
						.sqrt(op.getActivePowerPhaseB().pow(2).add(op.getReactivePowerPhaseB().pow(2)).doubleValue()))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseC", scaleList));
			if (op.getApparentPowerPhaseC() == null && op.getActivePowerPhaseC() != null
					&& op.getReactivePowerPhaseC() != null)
				op.setApparentPowerPhaseC(new BigDecimal(Math
						.sqrt(op.getActivePowerPhaseC().pow(2).add(op.getReactivePowerPhaseC().pow(2)).doubleValue()))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseTotal", scaleList));
			if (op.getApparentPowerPhaseTotal() == null && op.getApparentPowerPhaseA() != null
					&& op.getApparentPowerPhaseB() != null && op.getApparentPowerPhaseC() != null)
				op.setApparentPowerPhaseTotal(
						op.getApparentPowerPhaseA().add(op.getApparentPowerPhaseB().add(op.getApparentPowerPhaseC())));

			valueList = operationList.getValueListbyName("EnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate4");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate4", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate4");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate4", scaleList));

			valueList = operationList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				op.setQ1(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList));

			valueList = operationList.getValueListbyName("Q2");
			if (valueList != null && valueList.size() > 0)
				op.setQ2(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q2", scaleList));

			valueList = operationList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				op.setQ3(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList));

			valueList = operationList.getValueListbyName("Q4");
			if (valueList != null && valueList.size() > 0)
				op.setQ4(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q4", scaleList));

			valueList = operationList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				op.setImportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList));

			valueList = operationList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				op.setExportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusVA");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusVA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusVA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusA");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandSubA");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate4");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate4", scaleList));

			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate4");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate4", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("PhaseRotation");
			if (valueList != null && valueList.size() > 0)
				op.setPhaseRotation(ConvertUlti.convertHexToDecimalString(valueList.get(0)));

			valueList = operationList.getValueListbyName("Signal");
			if (valueList != null && valueList.size() > 0)
				op.setSignal(ConvertUlti.convertNegativeValue(valueList.get(0)));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate3", scaleList));

			op.setDcuCode(dcuCode);
			op.setDcuType(Constant.DCU_TYPE_VIETTEL);

			iMt3Business.addMessageOperation(op);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "operationStarPacketDecode", ex);
		}

	}

	private void operationGeniusPacketDeocode(String topic, String dcuCode, DataCollection operationList,
			DataCollection scaleList, IMT3Business iMt3Business) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3OperationEntity op = new MT3OperationEntity();
			op.setTopic(topic);
			valueList = operationList.getValueListbyName("Tu");
			if (valueList != null && valueList.size() > 0)
				op.setTu(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				op.setTuT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				op.setTuM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("Ti");
			if (valueList != null && valueList.size() > 0)
				op.setTi(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				op.setTiT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				op.setTiM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				op.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				op.setMeterTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseB(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseC(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseB(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseC(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));

			valueList = operationList.getValueListbyName("PhaseAnglePhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setPhaseAnglePhaseA(this.convertNegativeValue(valueList.get(0), "PhaseAnglePhaseA", scaleList));
			if (op.getPhaseAnglePhaseA() != null)
				op.setPowerFactorPhaseA(
						new BigDecimal(Math.cos(op.getPhaseAnglePhaseA().doubleValue() * (Math.PI / 180))).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PhaseAnglePhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setPhaseAnglePhaseB(this.convertNegativeValue(valueList.get(0), "PhaseAnglePhaseB", scaleList));
			if (op.getPhaseAnglePhaseB() != null)
				op.setPowerFactorPhaseB(
						new BigDecimal(Math.cos(op.getPhaseAnglePhaseB().doubleValue() * (Math.PI / 180))).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PhaseAnglePhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setPhaseAnglePhaseC(this.convertNegativeValue(valueList.get(0), "PhaseAnglePhaseC", scaleList));
			if (op.getPhaseAnglePhaseC() != null)
				op.setPowerFactorPhaseC(
						new BigDecimal(Math.cos(op.getPhaseAnglePhaseC().doubleValue() * (Math.PI / 180))).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL2subUL1");
			if (valueList != null && valueList.size() > 0)
				op.setAngleofUL2subUL1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL2subUL1", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL1subUL3");
			if (valueList != null && valueList.size() > 0)
				op.setAngleofUL1subUL3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL1subUL3", scaleList));
			BigDecimal freqA = null, freqB = null, freqC = null;
			valueList = operationList.getValueListbyName("FreqA");
			if (valueList != null && valueList.size() > 0)
				freqA = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqA", scaleList);
			valueList = operationList.getValueListbyName("FreqB");
			if (valueList != null && valueList.size() > 0)
				freqB = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqB", scaleList);
			valueList = operationList.getValueListbyName("FreqC");
			if (valueList != null && valueList.size() > 0)
				freqC = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqC", scaleList);
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

			valueList = operationList.getValueListbyName("ActivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseA", scaleList));
			if (op.getActivePowerPhaseA() == null && op.getReactivePowerPhaseA() == null)
				op.setApparentPowerPhaseA(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseB", scaleList));
			if (op.getActivePowerPhaseB() == null && op.getReactivePowerPhaseB() == null)
				op.setApparentPowerPhaseB(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseC", scaleList));
			if (op.getActivePowerPhaseC() == null && op.getReactivePowerPhaseC() == null)
				op.setApparentPowerPhaseC(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate4");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate4", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate4");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate4", scaleList));

			valueList = operationList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				op.setQ1(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList));

			valueList = operationList.getValueListbyName("Q2");
			if (valueList != null && valueList.size() > 0)
				op.setQ2(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q2", scaleList));

			valueList = operationList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				op.setQ3(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList));

			valueList = operationList.getValueListbyName("Q4");
			if (valueList != null && valueList.size() > 0)
				op.setQ4(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q4", scaleList));

			valueList = operationList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				op.setImportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList));

			valueList = operationList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				op.setExportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusVA");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusVA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusVA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusA");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandSubA");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate4");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate4", scaleList));

			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate4");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate4", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("PhaseRotation");
			if (valueList != null && valueList.size() > 0)
				op.setPhaseRotation(ConvertUlti.convertHexToDecimalString(valueList.get(0)));

			valueList = operationList.getValueListbyName("Signal");
			if (valueList != null && valueList.size() > 0)
				op.setSignal(ConvertUlti.convertNegativeValue(valueList.get(0)));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate3", scaleList));

			op.setDcuCode(dcuCode);
			op.setDcuType(Constant.DCU_TYPE_VIETTEL);

			iMt3Business.addMessageOperation(op);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "operationGeniusPacketDecode", ex);
		}

	}

	private void operationElsterPacketDeocode(String topic, String dcuCode, DataCollection operationList,
			DataCollection scaleList, IMT3Business iMt3Business) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3OperationEntity op = new MT3OperationEntity();
			op.setTopic(topic);
			valueList = operationList.getValueListbyName("Tu");
			if (valueList != null && valueList.size() > 0)
				op.setTu(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				op.setTuT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				op.setTuM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("Ti");
			if (valueList != null && valueList.size() > 0)
				op.setTi(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				op.setTiT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				op.setTiM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				op.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				op.setMeterTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseB(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseC(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseB(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseC(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseA", scaleList));
			valueList = operationList.getValueListbyName("PhaseAnglePhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setPhaseAnglePhaseA(this.convertNegativeValue(valueList.get(0), "PhaseAnglePhaseA", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseB", scaleList));
			valueList = operationList.getValueListbyName("PhaseAnglePhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setPhaseAnglePhaseB(this.convertNegativeValue(valueList.get(0), "PhaseAnglePhaseB", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseC", scaleList));
			valueList = operationList.getValueListbyName("PhaseAnglePhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setPhaseAnglePhaseC(this.convertNegativeValue(valueList.get(0), "PhaseAnglePhaseC", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL2subUL1");
			if (valueList != null && valueList.size() > 0)
				op.setAngleofUL2subUL1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL2subUL1", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL1subUL3");
			if (valueList != null && valueList.size() > 0)
				op.setAngleofUL1subUL3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL1subUL3", scaleList));
			BigDecimal freqA = null, freqB = null, freqC = null;
			valueList = operationList.getValueListbyName("FreqA");
			if (valueList != null && valueList.size() > 0)
				freqA = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqA", scaleList);
			valueList = operationList.getValueListbyName("FreqB");
			if (valueList != null && valueList.size() > 0)
				freqB = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqB", scaleList);
			valueList = operationList.getValueListbyName("FreqC");
			if (valueList != null && valueList.size() > 0)
				freqC = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqC", scaleList);
			BigDecimal avg = BigDecimal.valueOf(1);

			if (freqA != null && freqB != null && freqC != null) {
				avg = BigDecimal.valueOf(3);
				// ban tin optan Elster Freq chia them cho 100
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

			valueList = operationList.getValueListbyName("ActivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseA(this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseB(this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseC(this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseTotal(
						this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseA(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseB(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseC(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseTotal(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseA", scaleList));
			if (op.getActivePowerPhaseA() == null && op.getReactivePowerPhaseA() == null)
				op.setApparentPowerPhaseA(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseB", scaleList));
			if (op.getActivePowerPhaseB() == null && op.getReactivePowerPhaseB() == null)
				op.setApparentPowerPhaseB(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseC", scaleList));
			if (op.getActivePowerPhaseC() == null && op.getReactivePowerPhaseC() == null)
				op.setApparentPowerPhaseC(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate4");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate4", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate4");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate4", scaleList));

			valueList = operationList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				op.setQ1(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList));

			valueList = operationList.getValueListbyName("Q2");
			if (valueList != null && valueList.size() > 0)
				op.setQ2(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q2", scaleList));

			valueList = operationList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				op.setQ3(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList));

			valueList = operationList.getValueListbyName("Q4");
			if (valueList != null && valueList.size() > 0)
				op.setQ4(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q4", scaleList));

			valueList = operationList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				op.setImportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList));

			valueList = operationList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				op.setExportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusVA");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusVA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusVA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusA");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandSubA");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate4");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate4", scaleList));

			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate4");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate4", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("PhaseRotation");
			if (valueList != null && valueList.size() > 0)
				op.setPhaseRotation(ConvertUlti.convertHexToDecimalString(valueList.get(0)));

			valueList = operationList.getValueListbyName("Signal");
			if (valueList != null && valueList.size() > 0)
				op.setSignal(ConvertUlti.convertNegativeValue(valueList.get(0)));

			op.setDcuCode(dcuCode);
			op.setDcuType(Constant.DCU_TYPE_VIETTEL);

			iMt3Business.addMessageOperation(op);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "operationElsterPacketDecode", ex);
		}

	}

	private void operationGelexPacketDeocode(String topic, String dcuCode, DataCollection operationList,
			DataCollection scaleList, IMT3Business iMt3Business) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3OperationEntity op = new MT3OperationEntity();
			op.setTopic(topic);
			valueList = operationList.getValueListbyName("Tu");
			if (valueList != null && valueList.size() > 0)
				op.setTu(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				op.setTuT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				op.setTuM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("Ti");
			if (valueList != null && valueList.size() > 0)
				op.setTi(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				op.setTiT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				op.setTiM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				op.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				op.setMeterTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseB(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setVoltagePhaseC(this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseB(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setCurrentPhaseC(this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseA", scaleList));
			if (op.getPowerFactorPhaseA() != null && op.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseA(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseA().doubleValue()) * (180 / Math.PI)).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseB", scaleList));
			if (op.getPowerFactorPhaseB() != null && op.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseB(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseB().doubleValue()) * (180 / Math.PI)).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseC", scaleList));
			if (op.getPowerFactorPhaseC() != null && op.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(1)) <= 0
					&& op.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(0)) >= 0)
				op.setPhaseAnglePhaseC(
						new BigDecimal(Math.acos(op.getPowerFactorPhaseC().doubleValue()) * (180 / Math.PI)).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setPowerFactorPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL2subUL1");
			if (valueList != null && valueList.size() > 0)
				op.setAngleofUL2subUL1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL2subUL1", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL1subUL3");
			if (valueList != null && valueList.size() > 0)
				op.setAngleofUL1subUL3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL1subUL3", scaleList));
			BigDecimal freqA = null, freqB = null, freqC = null;
			valueList = operationList.getValueListbyName("FreqA");
			if (valueList != null && valueList.size() > 0)
				freqA = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqA", scaleList);
			valueList = operationList.getValueListbyName("FreqB");
			if (valueList != null && valueList.size() > 0)
				freqB = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqB", scaleList);
			valueList = operationList.getValueListbyName("FreqC");
			if (valueList != null && valueList.size() > 0)
				freqC = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqC", scaleList);
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

			valueList = operationList.getValueListbyName("ActivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseA(this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseB(this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseC(this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setActivePowerPhaseTotal(
						this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseA(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseB(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseC(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setReactivePowerPhaseTotal(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseA", scaleList));
			if (op.getActivePowerPhaseA() == null && op.getReactivePowerPhaseA() == null)
				op.setApparentPowerPhaseA(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseB", scaleList));
			if (op.getActivePowerPhaseB() == null && op.getReactivePowerPhaseB() == null)
				op.setApparentPowerPhaseB(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseC", scaleList));
			if (op.getActivePowerPhaseC() == null && op.getReactivePowerPhaseC() == null)
				op.setApparentPowerPhaseC(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				op.setApparentPowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate4");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate4", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate4");
			if (valueList != null && valueList.size() > 0)
				op.setEnergySubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate4", scaleList));

			valueList = operationList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				op.setQ1(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList));

			valueList = operationList.getValueListbyName("Q2");
			if (valueList != null && valueList.size() > 0)
				op.setQ2(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q2", scaleList));

			valueList = operationList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				op.setQ3(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList));

			valueList = operationList.getValueListbyName("Q4");
			if (valueList != null && valueList.size() > 0)
				op.setQ4(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q4", scaleList));

			valueList = operationList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				op.setImportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList));

			valueList = operationList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				op.setExportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusVA");
			if (valueList != null && valueList.size() > 0)
				op.setEnergyPlusVA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusVA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusA");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandSubA");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate4");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate4", scaleList));

			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandPlusArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate4");
			if (valueList != null && valueList.size() > 0)
				op.setMaxDemandSubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate4", scaleList));
			if (valueList != null && valueList.size() > 1)
				op.setMaxDemandSubArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("PhaseRotation");
			if (valueList != null && valueList.size() > 0)
				op.setPhaseRotation(ConvertUlti.convertHexToDecimalString(valueList.get(0)));

			valueList = operationList.getValueListbyName("Signal");
			if (valueList != null && valueList.size() > 0)
				op.setSignal(ConvertUlti.convertNegativeValue(valueList.get(0)));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				op.setReactiveEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate3", scaleList));

			op.setDcuCode(dcuCode);
			op.setDcuType(Constant.DCU_TYPE_VIETTEL);

			iMt3Business.addMessageOperation(op);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "operationGelexPacketDecode", ex);
		}

	}

	private void opearationFirstDecode(String dcuCode, String commandLineTemp, DataCollection operationList,
			IMT3Business iMt3Business) {
		DataCollection scaleList = new DataCollection();
		try {
			scaleList = ThreePhaseViettelDcuStructData.getObisScale(commandLineTemp);
			String meterId = ConvertUlti.convertHexaToString(operationList.getValueListbyName("MeterId").get(2));
			String meterType = operationList.getValueListbyName("MeterId").get(0);
			for (DataField item : scaleList.getdataList()) {
				switch (meterType) {
				case Constant.METER_TYPE_LANDIS:
					DataField listDataLandis = landisObisScaleList.getDataByFieldCode(item.getFieldCode());
					if (listDataLandis.getScale() != null)
						item.setScale(listDataLandis.getScale());
					item.setType(Constant.METER_TYPE_LANDIS);
					break;
				case Constant.METER_TYPE_ELSTER:
					DataField listDataElster = elsterObisScaleList.getDataByFieldCode(item.getFieldCode());
					if (listDataElster.getScale() != null)
						item.setScale(listDataElster.getScale());
					item.setType(Constant.METER_TYPE_ELSTER);
					break;
				case Constant.METER_TYPE_GENIUS:
					DataField listDataGenius = geniusObisScaleList.getDataByFieldCode(item.getFieldCode());
					if (listDataGenius.getScale() != null)
						item.setScale(listDataGenius.getScale());
					item.setType(Constant.METER_TYPE_GENIUS);
					break;
				case Constant.METER_TYPE_STAR:
					DataField listDataStar = starObisScaleList.getDataByFieldCode(item.getFieldCode());
					if (listDataStar.getScale() != null)
						item.setScale(listDataStar.getScale());
					item.setType(Constant.METER_TYPE_STAR);
					break;
				case Constant.METER_TYPE_GELEX:
					DataField listDataGelex = gelexObisScaleList.getDataByFieldCode(item.getFieldCode());
					if (listDataGelex.getScale() != null) {
						item.setScale(listDataGelex.getScale());
						item.setDefaultScale(listDataGelex.getDefaultScale());
					}
					item.setType(Constant.METER_TYPE_GELEX);
					break;
				default:
					break;
				}
			}
			Boolean IsUsedScaleDefault = false;
			List<String> valueList = null;
			valueList = operationList.getValueListbyName("Tu");
			BigDecimal tu = ConvertUlti.convertHexToDecimal(valueList.get(0));
			valueList = operationList.getValueListbyName("Ti");
			BigDecimal ti = ConvertUlti.convertHexToDecimal(valueList.get(0));
			if ((meterType == Constant.METER_TYPE_GELEX) && (tu == BigDecimal.valueOf(1))
					&& (ti == BigDecimal.valueOf(1)))
				IsUsedScaleDefault = true;
			int mpstatus = -1; // Giá trị khởi tạo mạc định

			// Update DcuId for Measurement point
			mpstatus = iMt3Business.UpdateDcuIdForMeasurementPointByDcuCodeAndMeterId(dcuCode, meterId, meterType);

			if (mpstatus != -1) { // KHONG PHAI GIA TRI MAC DINH
				// Update obiscale for meter if change meter
				iMt3Business.UpdateMeterObisScale(meterId, scaleList, IsUsedScaleDefault);
			}

			this.instantaneousPacketDecode(meterType, meterId, dcuCode, scaleList, operationList, iMt3Business);

			// Send Feedback Dcu
			if (this.systemEventCallback != null) {
				byte[] ResponseDCU = this.getPacketPayloadForAckDcu();
				String topic = "ami/002/" + dcuCode;
				this.systemEventCallback.sendData(topic, ResponseDCU);
			}
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "opearationFirstDecode", ex);
		}
	}

	private byte[] getPacketPayloadForAckDcu() {
		try {
			// Create message return ACK
			// CommandId
			byte[] temp = ConvertUlti.asnEncode("02", "01");
			// CommandType
			temp = ByteUltil.Combine(temp, ConvertUlti.asnEncode("01", "01"));
			// Command Action
			temp = ByteUltil.Combine(temp, ConvertUlti.asnEncode("03", "17"));
			// Data
			temp = ByteUltil.Combine(temp, ConvertUlti.asnEncode("24", "00"));
			return temp;
		} catch (Exception ex) {
			return null;
		}
	}

	private void instantaneousPacketDecode(String meterType, String meterId, String dcuCode, DataCollection scaleList,
			DataCollection operationList, IMT3Business iMt3Business) {

		try {

			switch (meterType) {
			case Constant.METER_TYPE_GELEX:
				this.instantaneousGelexPacketDecode(meterId, dcuCode, operationList, scaleList, iMt3Business);
				break;
			case Constant.METER_TYPE_ELSTER:
				this.instantaneousElsterPacketDecode(meterId, dcuCode, operationList, scaleList, iMt3Business);
				break;
			case Constant.METER_TYPE_GENIUS:
				this.instantaneousGeniusPacketDecode(meterId, dcuCode, operationList, scaleList, iMt3Business);
				break;
			case Constant.METER_TYPE_STAR:
				this.instantaneousStarPacketDecode(meterId, dcuCode, operationList, scaleList, iMt3Business);
				break;
			case Constant.METER_TYPE_LANDIS:
				this.instantaneousLandisPacketDecode(meterId, dcuCode, operationList, scaleList, iMt3Business);
				break;
			default:
				break;
			}
		} catch (Exception ex) {
		}

	}

	private void instantaneousLandisPacketDecode(String meterId, String dcuCode, DataCollection operationList,
			DataCollection scaleList, IMT3Business iMt3Business) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3OperationEntity ins = new MT3OperationEntity();

			ins.setMeterId(meterId);
			valueList = operationList.getValueListbyName("SvnVersion");
			if (valueList != null && valueList.size() > 0)
				ins.setSvnVersion(new String(ConvertUlti.toByteArray(valueList.get(0))));

			valueList = operationList.getValueListbyName("ProtocolVersion");
			if (valueList != null && valueList.size() > 0)
				ins.setProtocolVersion(new String(ConvertUlti.toByteArray(valueList.get(0))));

			valueList = operationList.getValueListbyName("Tu");
			if (valueList != null && valueList.size() > 0)
				ins.setTu(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				ins.setTuT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				ins.setTuM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("Ti");
			if (valueList != null && valueList.size() > 0)
				ins.setTi(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				ins.setTiT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				ins.setTiM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				ins.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				ins.setMeterTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseA", scaleList));
			if (ins.getPowerFactorPhaseA() != null && ins.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseA(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseA().doubleValue()) * (180 / Math.PI))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseB", scaleList));
			if (ins.getPowerFactorPhaseB() != null && ins.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseB(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseB().doubleValue()) * (180 / Math.PI))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseC", scaleList));
			if (ins.getPowerFactorPhaseC() != null && ins.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseC(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseC().doubleValue()) * (180 / Math.PI))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL2subUL1");
			if (valueList != null && valueList.size() > 0)
				ins.setAngleofUL2subUL1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL2subUL1", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL1subUL3");
			if (valueList != null && valueList.size() > 0)
				ins.setAngleofUL1subUL3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL1subUL3", scaleList));
			BigDecimal freqA = null, freqB = null, freqC = null;
			valueList = operationList.getValueListbyName("FreqA");
			if (valueList != null && valueList.size() > 0)
				freqA = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqA", scaleList);
			valueList = operationList.getValueListbyName("FreqB");
			if (valueList != null && valueList.size() > 0)
				freqB = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqB", scaleList);
			valueList = operationList.getValueListbyName("FreqC");
			if (valueList != null && valueList.size() > 0)
				freqC = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqC", scaleList);
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
			if (ins.getPowerFactorPhaseA() != null)
				ins.setActivePowerPhaseA(this.dataDivideValue(
						ins.getVoltagePhaseA().multiply(ins.getCurrentPhaseA().multiply(ins.getPowerFactorPhaseA())),
						1000));
			if (ins.getPowerFactorPhaseB() != null)
				ins.setActivePowerPhaseB(this.dataDivideValue(
						ins.getVoltagePhaseB().multiply(ins.getCurrentPhaseB().multiply(ins.getPowerFactorPhaseB())),
						1000));
			if (ins.getPowerFactorPhaseC() != null)
				ins.setActivePowerPhaseC(this.dataDivideValue(
						ins.getVoltagePhaseC().multiply(ins.getCurrentPhaseC().multiply(ins.getPowerFactorPhaseC())),
						1000));
			if (ins.getActivePowerPhaseA() != null && ins.getActivePowerPhaseB() != null
					&& ins.getActivePowerPhaseC() != null)
				ins.setActivePowerPhaseTotal(
						ins.getActivePowerPhaseA().add(ins.getActivePowerPhaseB().add(ins.getActivePowerPhaseC())));
			if (ins.getPowerFactorPhaseA() != null)
				ins.setReactivePowerPhaseA(this.dataDivideValue(
						ins.getVoltagePhaseA()
								.multiply(ins.getCurrentPhaseA()).multiply(new BigDecimal(Math.sqrt(BigDecimal
										.valueOf(1).subtract(ins.getPowerFactorPhaseA().pow(2)).doubleValue()))),
						1000));
			if (ins.getPowerFactorPhaseB() != null)
				ins.setReactivePowerPhaseB(this.dataDivideValue(
						ins.getVoltagePhaseB()
								.multiply(ins.getCurrentPhaseB()).multiply(new BigDecimal(Math.sqrt(BigDecimal
										.valueOf(1).subtract(ins.getPowerFactorPhaseB().pow(2)).doubleValue()))),
						1000));
			if (ins.getPowerFactorPhaseC() != null)
				ins.setReactivePowerPhaseC(this.dataDivideValue(
						ins.getVoltagePhaseC()
								.multiply(ins.getCurrentPhaseC()).multiply(new BigDecimal(Math.sqrt(BigDecimal
										.valueOf(1).subtract(ins.getPowerFactorPhaseC().pow(2)).doubleValue()))),
						1000));
			if (ins.getReactivePowerPhaseA() != null && ins.getReactivePowerPhaseB() != null
					&& ins.getReactivePowerPhaseC() != null)
				ins.setReactivePowerPhaseTotal(ins.getReactivePowerPhaseA()
						.add(ins.getReactivePowerPhaseB().add(ins.getReactivePowerPhaseC())));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseA", scaleList));
			if (ins.getApparentPowerPhaseA() == null && ins.getActivePowerPhaseA() != null
					&& ins.getReactivePowerPhaseA() != null)
				ins.setApparentPowerPhaseA(new BigDecimal(Math
						.sqrt(ins.getActivePowerPhaseA().pow(2).add(ins.getReactivePowerPhaseA().pow(2)).doubleValue()))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseB", scaleList));
			if (ins.getApparentPowerPhaseB() == null && ins.getActivePowerPhaseB() != null
					&& ins.getReactivePowerPhaseB() != null)
				ins.setApparentPowerPhaseB(new BigDecimal(Math
						.sqrt(ins.getActivePowerPhaseB().pow(2).add(ins.getReactivePowerPhaseB().pow(2)).doubleValue()))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseC", scaleList));
			if (ins.getApparentPowerPhaseC() == null && ins.getActivePowerPhaseC() != null
					&& ins.getReactivePowerPhaseC() != null)
				ins.setApparentPowerPhaseC(new BigDecimal(Math
						.sqrt(ins.getActivePowerPhaseC().pow(2).add(ins.getReactivePowerPhaseC().pow(2)).doubleValue()))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseTotal", scaleList));
			if (ins.getApparentPowerPhaseTotal() == null && ins.getApparentPowerPhaseA() != null
					&& ins.getApparentPowerPhaseB() != null && ins.getApparentPowerPhaseC() != null)
				ins.setApparentPowerPhaseTotal(ins.getApparentPowerPhaseA()
						.add(ins.getApparentPowerPhaseB().add(ins.getApparentPowerPhaseC())));

			valueList = operationList.getValueListbyName("EnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate1(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate1", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate2(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate2", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate3(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate3", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergyPlusARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate4(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate4", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate1(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate1", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate2(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate2", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate3(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate3", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergySubARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate4(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate4", scaleList), 1000));

			valueList = operationList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				ins.setQ1(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList),
						1000));

			valueList = operationList.getValueListbyName("Q2");
			if (valueList != null && valueList.size() > 0)
				ins.setQ2(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q2", scaleList),
						1000));

			valueList = operationList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				ins.setQ3(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList),
						1000));

			valueList = operationList.getValueListbyName("Q4");
			if (valueList != null && valueList.size() > 0)
				ins.setQ4(this.dataDivideValue(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q4", scaleList),
						1000));

			valueList = operationList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				ins.setImportWh(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList), 1000));

			valueList = operationList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				ins.setExportWh(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList), 1000));

			valueList = operationList.getValueListbyName("EnergyPlusVA");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusVA(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusVA", scaleList), 1000));

			valueList = operationList.getValueListbyName("MaxDemandPlusA");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusA(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusA", scaleList), 1000));

			valueList = operationList.getValueListbyName("MaxDemandSubA");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubA(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubA", scaleList), 1000));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate1(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate1", scaleList),
						1000));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate1(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate1", scaleList), 1000));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate2(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate2", scaleList),
						1000));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate2(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate2", scaleList), 1000));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate3(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate3", scaleList),
						1000));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate3(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate3", scaleList), 1000));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate4(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate4", scaleList),
						1000));

			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate4(this.dataDivideValue(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate4", scaleList), 1000));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("PhaseRotation");
			if (valueList != null && valueList.size() > 0)
				ins.setPhaseRotation(ConvertUlti.convertHexToDecimalString(valueList.get(0)));

			valueList = operationList.getValueListbyName("Signal");
			if (valueList != null && valueList.size() > 0)
				ins.setSignal(ConvertUlti.convertNegativeValue(valueList.get(0)));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate3", scaleList));

			ins.setDcuCode(dcuCode);
			ins.setDcuType(Constant.DCU_TYPE_VIETTEL);

			iMt3Business.insertIntantaneous(ins);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "instantaneousLandisPacketDecode", ex);
		}
	}

	private void instantaneousStarPacketDecode(String meterId, String dcuCode, DataCollection operationList,
			DataCollection scaleList, IMT3Business iMt3Business) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3OperationEntity ins = new MT3OperationEntity();

			ins.setMeterId(meterId);
			valueList = operationList.getValueListbyName("SvnVersion");
			if (valueList != null && valueList.size() > 0)
				ins.setSvnVersion(new String(ConvertUlti.toByteArray(valueList.get(0))));

			valueList = operationList.getValueListbyName("ProtocolVersion");
			if (valueList != null && valueList.size() > 0)
				ins.setProtocolVersion(new String(ConvertUlti.toByteArray(valueList.get(0))));

			valueList = operationList.getValueListbyName("Tu");
			if (valueList != null && valueList.size() > 0)
				ins.setTu(this.convertValue(valueList.get(0)));

			valueList = operationList.getValueListbyName("Ti");
			if (valueList != null && valueList.size() > 0)
				ins.setTi(this.convertValue(valueList.get(0)));

			valueList = operationList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				ins.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				ins.setMeterTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseA", scaleList));
			if (ins.getPowerFactorPhaseA() != null && ins.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseA(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseA().doubleValue()) * (180 / Math.PI))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseB", scaleList));
			if (ins.getPowerFactorPhaseB() != null && ins.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseB(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseB().doubleValue()) * (180 / Math.PI))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseC", scaleList));
			if (ins.getPowerFactorPhaseC() != null && ins.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseC(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseC().doubleValue()) * (180 / Math.PI))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL2subUL1");
			if (valueList != null && valueList.size() > 0)
				ins.setAngleofUL2subUL1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL2subUL1", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL1subUL3");
			if (valueList != null && valueList.size() > 0)
				ins.setAngleofUL1subUL3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL1subUL3", scaleList));
			BigDecimal freqA = null, freqB = null, freqC = null;
			valueList = operationList.getValueListbyName("FreqA");
			if (valueList != null && valueList.size() > 0)
				freqA = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqA", scaleList);
			valueList = operationList.getValueListbyName("FreqB");
			if (valueList != null && valueList.size() > 0)
				freqB = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqB", scaleList);
			valueList = operationList.getValueListbyName("FreqC");
			if (valueList != null && valueList.size() > 0)
				freqC = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqC", scaleList);
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

			valueList = operationList.getValueListbyName("ActivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));
			if (ins.getReactivePowerPhaseTotal() == null && ins.getReactivePowerPhaseA() != null
					&& ins.getReactivePowerPhaseB() != null && ins.getReactivePowerPhaseC() != null)
				ins.setReactivePowerPhaseTotal(ins.getReactivePowerPhaseA()
						.add(ins.getReactivePowerPhaseB().add(ins.getReactivePowerPhaseC())));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseA", scaleList));
			if (ins.getApparentPowerPhaseA() == null && ins.getActivePowerPhaseA() != null
					&& ins.getReactivePowerPhaseA() != null)
				ins.setApparentPowerPhaseA(new BigDecimal(Math
						.sqrt(ins.getActivePowerPhaseA().pow(2).add(ins.getReactivePowerPhaseA().pow(2)).doubleValue()))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseB", scaleList));
			if (ins.getApparentPowerPhaseB() == null && ins.getActivePowerPhaseB() != null
					&& ins.getReactivePowerPhaseB() != null)
				ins.setApparentPowerPhaseB(new BigDecimal(Math
						.sqrt(ins.getActivePowerPhaseB().pow(2).add(ins.getReactivePowerPhaseB().pow(2)).doubleValue()))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseC", scaleList));
			if (ins.getApparentPowerPhaseC() == null && ins.getActivePowerPhaseC() != null
					&& ins.getReactivePowerPhaseC() != null)
				ins.setApparentPowerPhaseC(new BigDecimal(Math
						.sqrt(ins.getActivePowerPhaseC().pow(2).add(ins.getReactivePowerPhaseC().pow(2)).doubleValue()))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseTotal", scaleList));
			if (ins.getApparentPowerPhaseTotal() == null && ins.getApparentPowerPhaseA() != null
					&& ins.getApparentPowerPhaseB() != null && ins.getApparentPowerPhaseC() != null)
				ins.setApparentPowerPhaseTotal(ins.getApparentPowerPhaseA()
						.add(ins.getApparentPowerPhaseB().add(ins.getApparentPowerPhaseC())));

			valueList = operationList.getValueListbyName("EnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate4", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate4", scaleList));

			valueList = operationList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				ins.setQ1(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList));

			valueList = operationList.getValueListbyName("Q2");
			if (valueList != null && valueList.size() > 0)
				ins.setQ2(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q2", scaleList));

			valueList = operationList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				ins.setQ3(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList));

			valueList = operationList.getValueListbyName("Q4");
			if (valueList != null && valueList.size() > 0)
				ins.setQ4(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q4", scaleList));

			valueList = operationList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				ins.setImportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList));

			valueList = operationList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				ins.setExportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusVA");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusVA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusVA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusA");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandSubA");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate4", scaleList));

			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate4", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("PhaseRotation");
			if (valueList != null && valueList.size() > 0)
				ins.setPhaseRotation(ConvertUlti.convertHexToDecimalString(valueList.get(0)));

			valueList = operationList.getValueListbyName("Signal");
			if (valueList != null && valueList.size() > 0)
				ins.setSignal(ConvertUlti.convertNegativeValue(valueList.get(0)));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate3", scaleList));

			ins.setDcuCode(dcuCode);
			ins.setDcuType(Constant.DCU_TYPE_VIETTEL);

			iMt3Business.insertIntantaneous(ins);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "instantaneousStarPacketDecode", ex);
		}
	}

	private void instantaneousGeniusPacketDecode(String meterId, String dcuCode, DataCollection operationList,
			DataCollection scaleList, IMT3Business iMt3Business) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3OperationEntity ins = new MT3OperationEntity();

			ins.setMeterId(meterId);
			valueList = operationList.getValueListbyName("SvnVersion");
			if (valueList != null && valueList.size() > 0)
				ins.setSvnVersion(new String(ConvertUlti.toByteArray(valueList.get(0))));

			valueList = operationList.getValueListbyName("ProtocolVersion");
			if (valueList != null && valueList.size() > 0)
				ins.setProtocolVersion(new String(ConvertUlti.toByteArray(valueList.get(0))));

			valueList = operationList.getValueListbyName("Tu");
			if (valueList != null && valueList.size() > 0)
				ins.setTu(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				ins.setTuT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				ins.setTuM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("Ti");
			if (valueList != null && valueList.size() > 0)
				ins.setTi(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				ins.setTiT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				ins.setTiM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				ins.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				ins.setMeterTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));

			valueList = operationList.getValueListbyName("PhaseAnglePhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setPhaseAnglePhaseA(this.convertNegativeValue(valueList.get(0), "PhaseAnglePhaseA", scaleList));
			if (ins.getPhaseAnglePhaseA() != null)
				ins.setPowerFactorPhaseA(
						new BigDecimal(Math.cos(ins.getPhaseAnglePhaseA().doubleValue() * (Math.PI / 180))).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PhaseAnglePhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setPhaseAnglePhaseB(this.convertNegativeValue(valueList.get(0), "PhaseAnglePhaseB", scaleList));
			if (ins.getPhaseAnglePhaseB() != null)
				ins.setPowerFactorPhaseB(
						new BigDecimal(Math.cos(ins.getPhaseAnglePhaseB().doubleValue() * (Math.PI / 180))).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PhaseAnglePhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setPhaseAnglePhaseC(this.convertNegativeValue(valueList.get(0), "PhaseAnglePhaseC", scaleList));
			if (ins.getPhaseAnglePhaseC() != null)
				ins.setPowerFactorPhaseC(
						new BigDecimal(Math.cos(ins.getPhaseAnglePhaseC().doubleValue() * (Math.PI / 180))).setScale(3,
								RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL2subUL1");
			if (valueList != null && valueList.size() > 0)
				ins.setAngleofUL2subUL1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL2subUL1", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL1subUL3");
			if (valueList != null && valueList.size() > 0)
				ins.setAngleofUL1subUL3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL1subUL3", scaleList));
			BigDecimal freqA = null, freqB = null, freqC = null;
			valueList = operationList.getValueListbyName("FreqA");
			if (valueList != null && valueList.size() > 0)
				freqA = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqA", scaleList);
			valueList = operationList.getValueListbyName("FreqB");
			if (valueList != null && valueList.size() > 0)
				freqB = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqB", scaleList);
			valueList = operationList.getValueListbyName("FreqC");
			if (valueList != null && valueList.size() > 0)
				freqC = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqC", scaleList);
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

			valueList = operationList.getValueListbyName("ActivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ActivePowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseA", scaleList));
			if (ins.getActivePowerPhaseA() == null && ins.getReactivePowerPhaseA() == null)
				ins.setApparentPowerPhaseA(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseB", scaleList));
			if (ins.getActivePowerPhaseB() == null && ins.getReactivePowerPhaseB() == null)
				ins.setApparentPowerPhaseB(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseC", scaleList));
			if (ins.getActivePowerPhaseC() == null && ins.getReactivePowerPhaseC() == null)
				ins.setApparentPowerPhaseC(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate4", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate4", scaleList));

			valueList = operationList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				ins.setQ1(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList));

			valueList = operationList.getValueListbyName("Q2");
			if (valueList != null && valueList.size() > 0)
				ins.setQ2(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q2", scaleList));

			valueList = operationList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				ins.setQ3(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList));

			valueList = operationList.getValueListbyName("Q4");
			if (valueList != null && valueList.size() > 0)
				ins.setQ4(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q4", scaleList));

			valueList = operationList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				ins.setImportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList));

			valueList = operationList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				ins.setExportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusVA");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusVA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusVA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusA");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandSubA");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate4", scaleList));

			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate4", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("PhaseRotation");
			if (valueList != null && valueList.size() > 0)
				ins.setPhaseRotation(ConvertUlti.convertHexToDecimalString(valueList.get(0)));

			valueList = operationList.getValueListbyName("Signal");
			if (valueList != null && valueList.size() > 0)
				ins.setSignal(ConvertUlti.convertNegativeValue(valueList.get(0)));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate3", scaleList));

			ins.setDcuCode(dcuCode);
			ins.setDcuType(Constant.DCU_TYPE_VIETTEL);

			iMt3Business.insertIntantaneous(ins);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "instantaneousGeniusPacketDecode", ex);
		}
	}

	private void instantaneousElsterPacketDecode(String meterId, String dcuCode, DataCollection operationList,
			DataCollection scaleList, IMT3Business iMt3Business) {
		List<String> valueList = new ArrayList<String>();

		try {
			MT3OperationEntity ins = new MT3OperationEntity();

			ins.setMeterId(meterId);
			valueList = operationList.getValueListbyName("SvnVersion");
			if (valueList != null && valueList.size() > 0)
				ins.setSvnVersion(new String(ConvertUlti.toByteArray(valueList.get(0))));

			valueList = operationList.getValueListbyName("ProtocolVersion");
			if (valueList != null && valueList.size() > 0)
				ins.setProtocolVersion(new String(ConvertUlti.toByteArray(valueList.get(0))));

			valueList = operationList.getValueListbyName("Tu");
			if (valueList != null && valueList.size() > 0)
				ins.setTu(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				ins.setTuT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				ins.setTuM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("Ti");
			if (valueList != null && valueList.size() > 0)
				ins.setTi(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				ins.setTiT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				ins.setTiM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				ins.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				ins.setMeterTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseA", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseB", scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "VoltagePhaseC", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseA", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseB", scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "CurrentPhaseC", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseA", scaleList));
			valueList = operationList.getValueListbyName("PhaseAnglePhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setPhaseAnglePhaseA(this.convertNegativeValue(valueList.get(0), "PhaseAnglePhaseA", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseB", scaleList));
			valueList = operationList.getValueListbyName("PhaseAnglePhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setPhaseAnglePhaseB(this.convertNegativeValue(valueList.get(0), "PhaseAnglePhaseB", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseC", scaleList));
			valueList = operationList.getValueListbyName("PhaseAnglePhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setPhaseAnglePhaseC(this.convertNegativeValue(valueList.get(0), "PhaseAnglePhaseC", scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "PowerFactorPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL2subUL1");
			if (valueList != null && valueList.size() > 0)
				ins.setAngleofUL2subUL1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL2subUL1", scaleList));

			valueList = operationList.getValueListbyName("AngleofUL1subUL3");
			if (valueList != null && valueList.size() > 0)
				ins.setAngleofUL1subUL3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "AngleofUL1subUL3", scaleList));
			BigDecimal freqA = null, freqB = null, freqC = null;
			valueList = operationList.getValueListbyName("FreqA");
			if (valueList != null && valueList.size() > 0)
				freqA = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqA", scaleList);
			valueList = operationList.getValueListbyName("FreqB");
			if (valueList != null && valueList.size() > 0)
				freqB = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqB", scaleList);
			valueList = operationList.getValueListbyName("FreqC");
			if (valueList != null && valueList.size() > 0)
				freqC = this.calculatorConvertHexToIntIsNull(valueList.get(0), "FreqC", scaleList);
			BigDecimal avg = BigDecimal.valueOf(1);

			if (freqA != null && freqB != null && freqC != null) {
				avg = BigDecimal.valueOf(3);
				// ban tin instan Elster Freq chia them cho 100
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

			valueList = operationList.getValueListbyName("ActivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseA(this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseB(this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseC(this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseTotal(
						this.convertNegativeValue(valueList.get(0), "ActivePowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseA(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseA", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseB(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseB", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseC(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseC", scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseTotal(
						this.convertNegativeValue(valueList.get(0), "ReactivePowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseA", scaleList));
			if (ins.getActivePowerPhaseA() == null && ins.getReactivePowerPhaseA() == null)
				ins.setApparentPowerPhaseA(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseB(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseB", scaleList));
			if (ins.getActivePowerPhaseB() == null && ins.getReactivePowerPhaseB() == null)
				ins.setApparentPowerPhaseB(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseC(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseC", scaleList));
			if (ins.getActivePowerPhaseC() == null && ins.getReactivePowerPhaseC() == null)
				ins.setApparentPowerPhaseC(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseTotal(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "ApparentPowerPhaseTotal", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate4", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate1", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate2", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate3", scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate4", scaleList));

			valueList = operationList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				ins.setQ1(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList));

			valueList = operationList.getValueListbyName("Q2");
			if (valueList != null && valueList.size() > 0)
				ins.setQ2(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q2", scaleList));

			valueList = operationList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				ins.setQ3(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList));

			valueList = operationList.getValueListbyName("Q4");
			if (valueList != null && valueList.size() > 0)
				ins.setQ4(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q4", scaleList));

			valueList = operationList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				ins.setImportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList));

			valueList = operationList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				ins.setExportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusVA");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusVA(this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusVA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusA");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandSubA");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubA(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubA", scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate1(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate2(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate3(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate4", scaleList));

			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate4(
						this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandSubARate4", scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("PhaseRotation");
			if (valueList != null && valueList.size() > 0)
				ins.setPhaseRotation(ConvertUlti.convertHexToDecimalString(valueList.get(0)));

			valueList = operationList.getValueListbyName("Signal");
			if (valueList != null && valueList.size() > 0)
				ins.setSignal(ConvertUlti.convertNegativeValue(valueList.get(0)));

			ins.setDcuCode(dcuCode);
			ins.setDcuType(Constant.DCU_TYPE_VIETTEL);

			iMt3Business.insertIntantaneous(ins);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "instantaneousElsterPacketDecode", ex);
		}
	}

	private void instantaneousGelexPacketDecode(String meterId, String dcuCode, DataCollection operationList,
			DataCollection scaleList, IMT3Business iMt3Business) {

		List<String> valueList = new ArrayList<String>();

		try {
			MT3OperationEntity ins = new MT3OperationEntity();

			ins.setMeterId(meterId);
			valueList = operationList.getValueListbyName("SvnVersion");
			if (valueList != null && valueList.size() > 0)
				ins.setSvnVersion(new String(ConvertUlti.toByteArray(valueList.get(0))));

			valueList = operationList.getValueListbyName("ProtocolVersion");
			if (valueList != null && valueList.size() > 0)
				ins.setProtocolVersion(new String(ConvertUlti.toByteArray(valueList.get(0))));

			valueList = operationList.getValueListbyName("Tu");
			if (valueList != null && valueList.size() > 0)
				ins.setTu(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				ins.setTuT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				ins.setTuM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("Ti");
			if (valueList != null && valueList.size() > 0)
				ins.setTi(this.convertValue(valueList.get(0)));
			if (valueList != null && valueList.size() > 1)
				ins.setTiT(this.convertValue(valueList.get(1)));
			if (valueList != null && valueList.size() > 2)
				ins.setTiM(this.convertValue(valueList.get(2)));

			valueList = operationList.getValueListbyName("ServerTime");
			if (valueList != null && valueList.size() > 0)
				ins.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("MeterTime");
			if (valueList != null && valueList.size() > 0)
				ins.setMeterTime(this.convertDateTime(valueList.get(0)));

			valueList = operationList.getValueListbyName("VoltagePhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseA(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "VoltagePhaseA",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseB(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "VoltagePhaseB",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("VoltagePhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setVoltagePhaseC(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "VoltagePhaseC",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseA(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "CurrentPhaseA",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseB(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "CurrentPhaseB",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("CurrentPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setCurrentPhaseC(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "CurrentPhaseC",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("PowerFactorPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseA(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"PowerFactorPhaseA", ins.getTu(), ins.getTi(), scaleList));
			if (ins.getPowerFactorPhaseA() != null && ins.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseA().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseA(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseA().doubleValue()) * (180 / Math.PI))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseB(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"PowerFactorPhaseB", ins.getTu(), ins.getTi(), scaleList));
			if (ins.getPowerFactorPhaseB() != null && ins.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseB().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseB(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseB().doubleValue()) * (180 / Math.PI))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseC(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"PowerFactorPhaseC", ins.getTu(), ins.getTi(), scaleList));
			if (ins.getPowerFactorPhaseC() != null && ins.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(1)) <= 0
					&& ins.getPowerFactorPhaseC().compareTo(BigDecimal.valueOf(0)) >= 0)
				ins.setPhaseAnglePhaseC(
						new BigDecimal(Math.acos(ins.getPowerFactorPhaseC().doubleValue()) * (180 / Math.PI))
								.setScale(3, RoundingMode.CEILING));

			valueList = operationList.getValueListbyName("PowerFactorPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setPowerFactorPhaseTotal(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"PowerFactorPhaseTotal", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("AngleofUL2subUL1");
			if (valueList != null && valueList.size() > 0)
				ins.setAngleofUL2subUL1(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"AngleofUL2subUL1", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("AngleofUL1subUL3");
			if (valueList != null && valueList.size() > 0)
				ins.setAngleofUL1subUL3(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"AngleofUL1subUL3", ins.getTu(), ins.getTi(), scaleList));
			BigDecimal freqA = null, freqB = null, freqC = null;
			valueList = operationList.getValueListbyName("FreqA");
			if (valueList != null && valueList.size() > 0)
				freqA = this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "FreqA", ins.getTu(),
						ins.getTi(), scaleList);
			valueList = operationList.getValueListbyName("FreqB");
			if (valueList != null && valueList.size() > 0)
				freqB = this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "FreqB", ins.getTu(),
						ins.getTi(), scaleList);
			valueList = operationList.getValueListbyName("FreqC");
			if (valueList != null && valueList.size() > 0)
				freqC = this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "FreqC", ins.getTu(),
						ins.getTi(), scaleList);
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

			valueList = operationList.getValueListbyName("ActivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseA(this.convertNegativeValueFirstGelex(valueList.get(0), "ActivePowerPhaseA",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseB(this.convertNegativeValueFirstGelex(valueList.get(0), "ActivePowerPhaseB",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseC(this.convertNegativeValueFirstGelex(valueList.get(0), "ActivePowerPhaseC",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ActivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setActivePowerPhaseTotal(this.convertNegativeValueFirstGelex(valueList.get(0),
						"ActivePowerPhaseTotal", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseA(this.convertNegativeValueFirstGelex(valueList.get(0), "ReactivePowerPhaseA",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseB(this.convertNegativeValueFirstGelex(valueList.get(0), "ReactivePowerPhaseB",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseC(this.convertNegativeValueFirstGelex(valueList.get(0), "ReactivePowerPhaseC",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ReactivePowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setReactivePowerPhaseTotal(this.convertNegativeValueFirstGelex(valueList.get(0),
						"ReactivePowerPhaseTotal", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ApparentPowerPhaseA");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseA(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"ApparentPowerPhaseA", ins.getTu(), ins.getTi(), scaleList));
			if (ins.getActivePowerPhaseA() == null && ins.getReactivePowerPhaseA() == null)
				ins.setApparentPowerPhaseA(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseB");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseB(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"ApparentPowerPhaseB", ins.getTu(), ins.getTi(), scaleList));
			if (ins.getActivePowerPhaseB() == null && ins.getReactivePowerPhaseB() == null)
				ins.setApparentPowerPhaseB(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseC");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseC(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"ApparentPowerPhaseC", ins.getTu(), ins.getTi(), scaleList));
			if (ins.getActivePowerPhaseC() == null && ins.getReactivePowerPhaseC() == null)
				ins.setApparentPowerPhaseC(null);

			valueList = operationList.getValueListbyName("ApparentPowerPhaseTotal");
			if (valueList != null && valueList.size() > 0)
				ins.setApparentPowerPhaseTotal(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"ApparentPowerPhaseTotal", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate1(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"EnergyPlusARate1", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate2(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"EnergyPlusARate2", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate3(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"EnergyPlusARate3", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusArate4(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"EnergyPlusARate4", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate1(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"EnergySubARate1", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate2(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"EnergySubARate2", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate3(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"EnergySubARate3", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("EnergySubARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergySubArate4(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"EnergySubARate4", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("Q1");
			if (valueList != null && valueList.size() > 0)
				ins.setQ1(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "Q1", ins.getTu(),
						ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("Q2");
			if (valueList != null && valueList.size() > 0)
				ins.setQ2(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "Q2", ins.getTu(),
						ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("Q3");
			if (valueList != null && valueList.size() > 0)
				ins.setQ3(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "Q3", ins.getTu(),
						ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("Q4");
			if (valueList != null && valueList.size() > 0)
				ins.setQ4(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "Q4", ins.getTu(),
						ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
				ins.setImportWh(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "ImportWh",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ExportWh");
			if (valueList != null && valueList.size() > 0)
				ins.setExportWh(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "ExportWh",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("EnergyPlusVA");
			if (valueList != null && valueList.size() > 0)
				ins.setEnergyPlusVA(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "EnergyPlusVA",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusA");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusA(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "MaxDemandPlusA",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("MaxDemandSubA");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubA(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0), "MaxDemandSubA",
						ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate1(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"MaxDemandPlusARate1", ins.getTu(), ins.getTi(), scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate1(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"MaxDemandSubARate1", ins.getTu(), ins.getTi(), scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate1Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate2(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"MaxDemandPlusARate2", ins.getTu(), ins.getTi(), scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate2(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"MaxDemandSubARate2", ins.getTu(), ins.getTi(), scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate3(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"MaxDemandPlusARate3", ins.getTu(), ins.getTi(), scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate3(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"MaxDemandSubARate3", ins.getTu(), ins.getTi(), scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandPlusARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandPlusArate4(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"MaxDemandPlusARate4", ins.getTu(), ins.getTi(), scaleList));

			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandPlusArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("MaxDemandSubARate4");
			if (valueList != null && valueList.size() > 0)
				ins.setMaxDemandSubArate4(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"MaxDemandSubARate4", ins.getTu(), ins.getTi(), scaleList));
			if (valueList != null && valueList.size() > 1)
				ins.setMaxDemandSubArate4Time(this.convertDateTime(valueList.get(1)));

			valueList = operationList.getValueListbyName("PhaseRotation");
			if (valueList != null && valueList.size() > 0)
				ins.setPhaseRotation(ConvertUlti.convertHexToDecimalString(valueList.get(0)));

			valueList = operationList.getValueListbyName("Signal");
			if (valueList != null && valueList.size() > 0)
				ins.setSignal(ConvertUlti.convertNegativeValue(valueList.get(0)));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergyPlusArate1(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"ReactiveEnergyPlusARate1", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergyPlusArate2(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"ReactiveEnergyPlusARate2", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergyPlusARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergyPlusArate3(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"ReactiveEnergyPlusARate3", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate1");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergySubArate1(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"ReactiveEnergySubARate1", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate2");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergySubArate2(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"ReactiveEnergySubARate2", ins.getTu(), ins.getTi(), scaleList));

			valueList = operationList.getValueListbyName("ReactiveEnergySubARate3");
			if (valueList != null && valueList.size() > 0)
				ins.setReactiveEnergySubArate3(this.calculatorConvertHexToIntIsNullFirstGelex(valueList.get(0),
						"ReactiveEnergySubARate3", ins.getTu(), ins.getTi(), scaleList));

			ins.setDcuCode(dcuCode);
			ins.setDcuType(Constant.DCU_TYPE_VIETTEL);

			iMt3Business.insertIntantaneous(ins);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "instantaneousGelexPacketDecode", ex);
		}
	}

	private BigDecimal convertNegativeValueFirstGelex(String hexString, String filename, BigDecimal tu, BigDecimal ti,
			DataCollection scaleList) {
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

	private BigDecimal calculatorConvertHexToIntIsNullFirstGelex(String hexString, String filename, BigDecimal tu,
			BigDecimal ti, DataCollection scaleList) {
		try {
			return BigDecimal.valueOf(ConvertUlti.hex2Long(hexString)).multiply((scaleList.getScalebyName(filename)));
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

	private BigDecimal dataDivideValue(BigDecimal data, int value) {
		if (data == null)
			return null;
		return data.divide(BigDecimal.valueOf(value));
	}

}
