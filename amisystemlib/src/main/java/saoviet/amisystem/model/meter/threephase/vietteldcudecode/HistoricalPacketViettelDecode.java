/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: HistoricalPacketViettelDecode.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-06-06 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.vietteldcudecode;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import saoviet.amisystem.business.IMT3Business;
import saoviet.amisystem.business.MT3Business;
import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.datacollection.DataCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3HistoricalEntity;
import saoviet.amisystem.model.meter.threephase.packetvietteldcu.MessagePacketStructureViettelDcu;
import saoviet.amisystem.model.meter.threephase.packetvietteldcu.ThreePhaseViettelDcuStructData;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class HistoricalPacketViettelDecode {
	private LogUlti logUlti = new LogUlti(HistoricalPacketViettelDecode.class);
	private SystemEventCallback systemEventCallback;
	private IMT3Business iMT3Business;
	private DataCollection historicalDefaultList = new DataCollection();

	public HistoricalPacketViettelDecode() {
		this.historicalDefaultList = MessagePacketStructureViettelDcu.getHistoricalCollection();
		this.iMT3Business = new MT3Business();
	}

	public void historicalPacketDecode(MessageBase messageBase) {
		try {
			String dcuCode = messageBase.getDcuCode();
			String meterType = null;
			String commandLine = ConvertUlti.toHexString(messageBase.getData());
			DataCollection scaleList = new DataCollection();
			// Get meter scale form database
			this.iMT3Business.getMeterObisScale(null, dcuCode, scaleList);
			// Lay type meter tu DataField dau tien trong list
			meterType = scaleList.getdataList().get(0).getType();
			// khong tim thay scale thi return
			if (meterType == null)
				return;
			DataCollection historicalList = this.historicalDefaultList.copy();
			ThreePhaseViettelDcuStructData.setDataForObisCode(commandLine, historicalList);

			// Decode His
			List<String> valueList = new ArrayList<String>();
			MT3HistoricalEntity his = new MT3HistoricalEntity();

			his.setDcuCode(dcuCode);
			valueList = historicalList.getValueListbyName("ServerTime");
			his.setServerTime(this.convertDateTime(valueList.get(0)));

			valueList = historicalList.getValueListbyName("HistoricalTime");
			if (meterType.equals(Constant.METER_TYPE_LANDIS) || meterType.equals(Constant.METER_TYPE_GELEX)
					|| meterType.equals(Constant.METER_TYPE_GENIUS)) {
				his.setEndHistoricalTime(this.convertDateTime(valueList.get(0)));
				his.setHistoricalTime(his.getEndHistoricalTime());

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(his.getEndHistoricalTime().getTime());
				cal.add(Calendar.MONTH, -1);
				his.setBeginHistoricalTime(new Timestamp(cal.getTime().getTime()));

			} else {
				his.setBeginHistoricalTime(this.convertDateTime(valueList.get(0)));

				if (his.getBeginHistoricalTime() == null)
					return;

				if (valueList != null && valueList.size() > 1)
					his.setEndHistoricalTime(this.convertDateTime(valueList.get(1)));

				if (valueList != null && valueList.size() > 2)
					his.setHistoricalTime(this.convertDateTime(valueList.get(2)));

				if (his.getHistoricalTime() == null) {
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(his.getBeginHistoricalTime().getTime());
					cal.add(Calendar.MONTH, 1);
					his.setHistoricalTime(new Timestamp(cal.getTime().getTime()));
					his.setEndHistoricalTime(his.getHistoricalTime());
				}
			}
			valueList = historicalList.getValueListbyName("ImportWh");
			if (valueList != null && valueList.size() > 0)
			his.setImportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ImportWh", scaleList));
			if (meterType.equals(Constant.METER_TYPE_LANDIS))
				his.setImportWh(this.dataDivideValue(his.getImportWh(), 1000));
			valueList = historicalList.getValueListbyName("ExportWh");
			if(valueList != null && valueList.size() > 0)
			his.setExportWh(this.calculatorConvertHexToIntIsNull(valueList.get(0), "ExportWh", scaleList));
			if (meterType.equals(Constant.METER_TYPE_LANDIS))
				his.setExportWh(this.dataDivideValue(his.getExportWh(), 1000));
			valueList = historicalList.getValueListbyName("EnergyPlusARate1");
			if(valueList != null && valueList.size() > 0)
			his.setEnergyPlusARate1(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate1", scaleList));
			if (meterType.equals(Constant.METER_TYPE_LANDIS))
				his.setEnergyPlusARate1(this.dataDivideValue(his.getEnergyPlusARate1(), 1000));
			valueList = historicalList.getValueListbyName("EnergyPlusARate2");
			if(valueList != null && valueList.size() > 0)
			his.setEnergyPlusARate2(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate2", scaleList));
			if (meterType.equals(Constant.METER_TYPE_LANDIS))
				his.setEnergyPlusARate2(this.dataDivideValue(his.getEnergyPlusARate2(), 1000));
			valueList = historicalList.getValueListbyName("EnergyPlusARate3");
			if(valueList != null && valueList.size() > 0)
			his.setEnergyPlusARate3(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergyPlusARate3", scaleList));
			if (meterType.equals(Constant.METER_TYPE_LANDIS))
				his.setEnergyPlusARate3(this.dataDivideValue(his.getEnergyPlusARate3(), 1000));
			valueList = historicalList.getValueListbyName("EnergySubARate1");
			if(valueList != null && valueList.size() > 0)
			his.setEnergySubARate1(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate1", scaleList));
			if (meterType.equals(Constant.METER_TYPE_LANDIS))
				his.setEnergySubARate1(this.dataDivideValue(his.getEnergySubARate1(), 1000));
			valueList = historicalList.getValueListbyName("EnergySubARate2");
			if(valueList != null && valueList.size() > 0)
			his.setEnergySubARate2(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate2", scaleList));
			if (meterType.equals(Constant.METER_TYPE_LANDIS))
				his.setEnergySubARate2(this.dataDivideValue(his.getEnergySubARate2(), 1000));
			valueList = historicalList.getValueListbyName("EnergySubARate3");
			if(valueList != null && valueList.size() > 0)
			his.setEnergySubARate3(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "EnergySubARate3", scaleList));
			if (meterType.equals(Constant.METER_TYPE_LANDIS))
				his.setEnergySubARate3(this.dataDivideValue(his.getEnergySubARate3(), 1000));
			valueList = historicalList.getValueListbyName("Q1");
			if(valueList != null && valueList.size() > 0)
			his.setQ1(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q1", scaleList));
			his.setTotalReactiveEnergyPositve(his.getQ1());
			if (meterType.equals(Constant.METER_TYPE_LANDIS))
				his.setQ1(this.dataDivideValue(his.getQ1(), 1000));
			valueList = historicalList.getValueListbyName("Q3");
			if(valueList != null && valueList.size() > 0)
			his.setQ3(this.calculatorConvertHexToIntIsNull(valueList.get(0), "Q3", scaleList));
			his.setTotalReactiveEnergyNegative(his.getQ3());
			if (meterType.equals(Constant.METER_TYPE_LANDIS))
				his.setQ3(this.dataDivideValue(his.getQ3(), 1000));

			valueList = historicalList.getValueListbyName("MaxDemandPlusARate1");
			if(valueList != null && valueList.size() > 0)
			his.setMaxDemandPlusArate1(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate1", scaleList));
			if (valueList != null && valueList.size() > 1)
				his.setMaxDemandPlusArate1Time(this.convertDateTime(valueList.get(1)));
			if(valueList != null && valueList.size() > 0)

			valueList = historicalList.getValueListbyName("MaxDemandPlusARate2");
			if(valueList != null && valueList.size() > 0)
			his.setMaxDemandPlusArate2(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate2", scaleList));
			if (valueList != null && valueList.size() > 1)
				his.setMaxDemandPlusArate2Time(this.convertDateTime(valueList.get(1)));

			valueList = historicalList.getValueListbyName("MaxDemandPlusARate3");
			if(valueList != null && valueList.size() > 0)
			his.setMaxDemandPlusArate3(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate3", scaleList));
			if (valueList != null && valueList.size() > 1)
				his.setMaxDemandPlusArate3Time(this.convertDateTime(valueList.get(1)));

			valueList = historicalList.getValueListbyName("MaxDemandPlusARate4");
			if(valueList != null && valueList.size() > 0)
			his.setMaxDemandPlusArate4(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "MaxDemandPlusARate4", scaleList));
			if (valueList != null && valueList.size() > 1)
				his.setMaxDemandPlusArate4Time(this.convertDateTime(valueList.get(1)));
			valueList = historicalList.getValueListbyName("ReactiveEnergyPlusARate1");
			if(valueList != null && valueList.size() > 0)
			his.setReactiveEnergyPlusArate1(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate1", scaleList));
			valueList = historicalList.getValueListbyName("ReactiveEnergyPlusARate2");
			if(valueList != null && valueList.size() > 0)
			his.setReactiveEnergyPlusArate2(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate2", scaleList));
			valueList = historicalList.getValueListbyName("ReactiveEnergyPlusARate3");
			if(valueList != null && valueList.size() > 0)
			his.setReactiveEnergyPlusArate3(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergyPlusARate3", scaleList));
			valueList = historicalList.getValueListbyName("ReactiveEnergySubARate1");
			if(valueList != null && valueList.size() > 0)
			his.setReactiveEnergySubArate1(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate1", scaleList));
			valueList = historicalList.getValueListbyName("ReactiveEnergySubARate2");
			if(valueList != null && valueList.size() > 0)
			his.setReactiveEnergySubArate2(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate2", scaleList));
			valueList = historicalList.getValueListbyName("ReactiveEnergySubARate3");
			if(valueList != null && valueList.size() > 0)
			his.setReactiveEnergySubArate3(
					this.calculatorConvertHexToIntIsNull(valueList.get(0), "ReactiveEnergySubARate3", scaleList));
			if (his.getHistoricalTime() != null)
				iMT3Business.insertHistorical(his);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "historicalPacketDecode", ex);
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
		iMT3Business.setSystemEventCallback(this.systemEventCallback);
	}

}
