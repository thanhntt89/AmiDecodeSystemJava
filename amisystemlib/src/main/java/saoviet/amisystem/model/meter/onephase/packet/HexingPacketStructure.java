/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: HexingPacketStructure.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.model.meter.onephase.packet;

import java.math.BigDecimal;

import saoviet.amisystem.model.datacollection.DataCollection;

public class HexingPacketStructure {
	
	public static DataCollection getMEPDVCollection() {
		DataCollection mepdvCollection = new DataCollection();
		mepdvCollection.add("F0", "01", "ServerTime", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "-", "DCU_INFO", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "01", "SvnVersion", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "0A", "Singal", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "0B", "SimTemperature", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "0C", "McuTemperature", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "0D", "RtcPinVotage", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "0E", "CellIdConnect", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "-", "SCALE_OBIS_DATA_CONFIG_DEFAULT", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "02", "VoltagePhase", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "05", "CurrentPhase", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "09", "Frequency", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "0F", "ActivePowerPhase", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "1C", "PowerFactorPhaseA", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "1F", "EnergyPlusArate1", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "20", "EnergyPlusArate2", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "21", "EnergyPlusArate3", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "22", "EnergyPlusArate4", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "23", "EnergySubArate1", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "24", "EnergySubArate2", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "25", "EnergySubArate3", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "26", "EnergySubArate4", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "27", "ExportWh", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "28", "ImportWh", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "29", "Q1", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "2B", "Q3", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "2E", "MaxDemandPlusArate", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "31", "MaxDemandPlusArate1", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "35", "MaxDemandPlusArate2", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "39", "MaxDemandPlusArate3", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "3D", "MaxDemandPlusArate4", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "41", "Ti", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "42", "Tu", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "-", "SCALE_ALERT_DEFAULT", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "01", "VoltPhaseLower", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "02", "VoltPhaseOver", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "03", "FreqLower", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "04", "FreqOver", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "05", "PowerFactorLower", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "06", "CurOver", 0, null, null, null, null, null);

		return mepdvCollection;
	}

	public static DataCollection getScaleCollection() {	
		 DataCollection scaleCollection = new DataCollection();
		 
		scaleCollection.add(null, "02", "VoltagePhase", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "05", "CurrentPhase", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "09", "Frequency", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "0F", "ActivePowerPhase", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "1C", "PowerFactorPhaseA", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "1F", "EnergyPlusArate1", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "20", "EnergyPlusArate2", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "21", "EnergyPlusArate3", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "23", "EnergySubArate1", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "24", "EnergySubArate2", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "25", "EnergySubArate3", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "27", "ExportWh", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "28", "ImportWh", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "2E", "MaxDemandPlusArate", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "31", "MaxDemandPlusArate1", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "35", "MaxDemandPlusArate2", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "39", "MaxDemandPlusArate3", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		scaleCollection.add(null, "41", "Ti", 0, null, null, BigDecimal.valueOf(1), null, null);
		scaleCollection.add(null, "42", "Tu", 0, null, null, BigDecimal.valueOf(1), null, null);
		return scaleCollection;
	}

	
}
