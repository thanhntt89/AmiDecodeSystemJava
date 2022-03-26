/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: LandisPacketStructure.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.packet;

import saoviet.amisystem.model.datacollection.DataCollection;

public class LandisPacketStructure {

	public static DataCollection getLoadProfileCollection() {
		DataCollection loadProfileCollection = new DataCollection();
		loadProfileCollection.add(null, "00", "ServerTime", 0, null, null, null, null, null);
		loadProfileCollection.add(null, "01", "MeterTime", 0, null, null, null, null, null);
		loadProfileCollection.add(null, "84", "PowerAvgPos", 0, null, null, null, null, null);
		loadProfileCollection.add(null, "85", "PowerAvgNeg", 0, null, null, null, null, null);
		loadProfileCollection.add(null, "86", "ReactivePowerAvgPos", 0, null, null, null, null, null);
		loadProfileCollection.add(null, "87", "ReactivePowerAvgNeg", 0, null, null, null, null, null);
		return loadProfileCollection;
	}

	public static DataCollection getMEPDVCollection() {
		DataCollection mepdvCollection = new DataCollection();
		mepdvCollection.add("F0", "01", "ServerTime", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "-", "DCU_INFO", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "FE", "DCU_INFO", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "01", "SvnVersion", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "0A", "Singal", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "0B", "SimTemperature", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "0C", "McuTemperature", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "0D", "RtcPinVotage", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "0E", "CellIdConnect", 0, null, null, null, null, null);
		mepdvCollection.add("FE", "0F", "SimSerial", 0, null, null, null, null, null);	
		mepdvCollection.add("FA", "02", "VoltagePhaseA", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "03", "VoltagePhaseB", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "04", "VoltagePhaseC", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "05", "CurrentPhaseA", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "06", "CurrentPhaseB", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "07", "CurrentPhaseC", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "0C", "PhaseAnglePhaseA", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "0D", "PhaseAnglePhaseB", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "0E", "PhaseAnglePhaseC", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "0F", "ActivePowerPhaseA", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "10", "ActivePowerPhaseB", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "11", "ActivePowerPhaseC", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "12", "ActivePowerPhaseTotal", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "13", "ReactivePowerPhaseA", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "14", "ReactivePowerPhaseB", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "15", "ReactivePowerPhaseC", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "16", "ReactivePowerPhaseTotal", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "17", "ApparentPowerPhaseA", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "18", "ApparentPowerPhaseB", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "19", "ApparentPowerPhaseC", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "1A", "ApparentPowerPhaseTotal", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "1B", "PowerFactorPhaseTotal", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "1C", "PowerFactorPhaseA", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "1D", "PowerFactorPhaseB", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "1E", "PowerFactorPhaseC", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "1F", "EnergyPlusArate1", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "20", "EnergyPlusArate2", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "21", "EnergyPlusArate3", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "23", "EnergySubArate1", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "24", "EnergySubArate2", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "25", "EnergySubArate3", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "28", "ExportWh", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "27", "ImportWh", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "29", "Q1", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "2B", "Q3", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "2A", "Q2", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "2C", "Q4", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "2D", "EnergyPlusVA", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "2E", "MaxDemandPlusArate", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "31", "MaxDemandPlusArate1", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "35", "MaxDemandPlusArate2", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "39", "MaxDemandPlusArate3", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "41", "Ti", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "42", "Tu", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "43", "PhaseRotation", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "09", "Frequency", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "64", "TiM", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "65", "TuM", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "49", "ReactiveEnergyPlusArate1", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "4A", "ReactiveEnergyPlusArate2", 0, null, null, null, null, null);
		mepdvCollection.add("FA", "4B", "ReactiveEnergyPlusArate3", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "-", "SCALE_ALERT_DEFAULT", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "00", "VotPhaseAUnder", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "01", "VotPhaseAOver", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "02", "VotPhaseBUnder", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "03", "VotPhaseBOver", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "04", "VotPhaseCUnder", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "05", "VotPhaseCOver", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "06", "FreqUnder", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "07", "FreqOver", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "08", "PowerFactorUnder", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "09", "Cur1PhaseOverRated", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "0A", "Cur2PhaseOverRated", 0, null, null, null, null, null);
		mepdvCollection.add("FB", "0B", "Cur1PhaseUnderAverage", 0, null, null, null, null, null);
		return mepdvCollection;
	}

	public static DataCollection getEventCollection() {
		DataCollection eventCollection = new DataCollection();
		eventCollection.add(null, "00", "ServerTime", 0, null, null, null, null, null);
		eventCollection.add(null, "01", "EventNumberOfPowerFailuresObjectsInPhaseL1", 0, null, null, null, null, null);
		eventCollection.add(null, "02", "EventNumberOfPowerFailuresObjectsInPhaseL2", 0, null, null, null, null, null);
		eventCollection.add(null, "03", "EventNumberOfPowerFailuresObjectsInPhaseL3", 0, null, null, null, null, null);
		eventCollection.add(null, "04", "EventNumberOfPowerFailuresObjectsInAllThreePhases", 0, null, null, null, null, null);
		eventCollection.add(null, "17", "EventProgrammingCount", 0, null, null, null, null, null);
		eventCollection.add(null, "18", "EventProgrammingTime", 0, null, null, null, null, null);
		return eventCollection;
	}

	public static DataCollection getOperationCollection() {
		DataCollection operationCollection = new DataCollection();
		operationCollection.add(null, "00", "ServerTime", 0, null, null, null, null, null);
		operationCollection.add(null, "01", "MeterTime", 0, null, null, null, null, null);
		operationCollection.add(null, "02", "VoltagePhaseA", 0, null, null, null, null, null);
		operationCollection.add(null, "03", "VoltagePhaseB", 0, null, null, null, null, null);
		operationCollection.add(null, "04", "VoltagePhaseC", 0, null, null, null, null, null);
		operationCollection.add(null, "05", "CurrentPhaseA", 0, null, null, null, null, null);
		operationCollection.add(null, "06", "CurrentPhaseB", 0, null, null, null, null, null);
		operationCollection.add(null, "07", "CurrentPhaseC", 0, null, null, null, null, null);
		operationCollection.add(null, "09", "Frequency", 0, null, null, null, null, null);
		operationCollection.add(null, "0F", "ActivePowerPhaseA", 0, null, null, null, null, null);
		operationCollection.add(null, "10", "ActivePowerPhaseB", 0, null, null, null, null, null);
		operationCollection.add(null, "11", "ActivePowerPhaseC", 0, null, null, null, null, null);
		operationCollection.add(null, "12", "ActivePowerPhaseTotal", 0, null, null, null, null, null);
		operationCollection.add(null, "13", "ReactivePowerPhaseA", 0, null, null, null, null, null);
		operationCollection.add(null, "14", "ReactivePowerPhaseB", 0, null, null, null, null, null);
		operationCollection.add(null, "15", "ReactivePowerPhaseC", 0, null, null, null, null, null);
		operationCollection.add(null, "16", "ReactivePowerPhaseTotal", 0, null, null, null, null, null);
		operationCollection.add(null, "1C", "PowerFactorPhaseA", 0, null, null, null, null, null);
		operationCollection.add(null, "1D", "PowerFactorPhaseB", 0, null, null, null, null, null);
		operationCollection.add(null, "1E", "PowerFactorPhaseC", 0, null, null, null, null, null);
		operationCollection.add(null, "1B", "PowerFactorPhaseTotal", 0, null, null, null, null, null);
		operationCollection.add(null, "1F", "EnergyPlusArate1", 0, null, null, null, null, null);
		operationCollection.add(null, "20", "EnergyPlusArate2", 0, null, null, null, null, null);
		operationCollection.add(null, "21", "EnergyPlusArate3", 0, null, null, null, null, null);
		operationCollection.add(null, "23", "EnergySubArate1", 0, null, null, null, null, null);
		operationCollection.add(null, "24", "EnergySubArate2", 0, null, null, null, null, null);
		operationCollection.add(null, "25", "EnergySubArate3", 0, null, null, null, null, null);
		operationCollection.add(null, "27", "ImportWh", 0, null, null, null, null, null);
		operationCollection.add(null, "28", "ExportWh", 0, null, null, null, null, null);
		operationCollection.add(null, "29", "Q1", 0, null, null, null, null, null);
		operationCollection.add(null, "2B", "Q3", 0, null, null, null, null, null);
		operationCollection.add(null, "2A", "Q2", 0, null, null, null, null, null);
		operationCollection.add(null, "2C", "Q4", 0, null, null, null, null, null);
		operationCollection.add(null, "17", "ApparentPowerPhaseA", 0, null, null, null, null, null);
		operationCollection.add(null, "18", "ApparentPowerPhaseB", 0, null, null, null, null, null);
		operationCollection.add(null, "19", "ApparentPowerPhaseC", 0, null, null, null, null, null);
		operationCollection.add(null, "31", "MaxDemandPlusArate1", 0, null, null, null, null, null);
		operationCollection.add(null, "32", "MaxDemandPlusArate1Time", 0, null, null, null, null, null);
		operationCollection.add(null, "35", "MaxDemandPlusArate2", 0, null, null, null, null, null);
		operationCollection.add(null, "36", "MaxDemandPlusArate2Time", 0, null, null, null, null, null);
		operationCollection.add(null, "39", "MaxDemandPlusArate3", 0, null, null, null, null, null);
		operationCollection.add(null, "3A", "MaxDemandPlusArate3Time", 0, null, null, null, null, null);
		operationCollection.add(null, "2E", "MaxDemandPlusArate", 0, null, null, null, null, null);
		operationCollection.add(null, "2F", "MaxDemandPlusArateTime", 0, null, null, null, null, null);
		operationCollection.add(null, "30", "MaxDemandSubArate", 0, null, null, null, null, null);
		operationCollection.add(null, "45", "MaxDemandSubArateTime", 0, null, null, null, null, null);
		operationCollection.add(null, "33", "MaxDemandSubArate1", 0, null, null, null, null, null);
		operationCollection.add(null, "34", "MaxDemandSubArate1Time", 0, null, null, null, null, null);
		operationCollection.add(null, "37", "MaxDemandSubArate2", 0, null, null, null, null, null);
		operationCollection.add(null, "38", "MaxDemandSubArate2Time", 0, null, null, null, null, null);
		operationCollection.add(null, "3B", "MaxDemandSubArate3", 0, null, null, null, null, null);
		operationCollection.add(null, "3C", "MaxDemandSubArate3Time", 0, null, null, null, null, null);
		operationCollection.add(null, "41", "Ti", 0, null, null, null, null, null);
		operationCollection.add(null, "64", "TiM", 0, null, null, null, null, null);
		operationCollection.add(null, "42", "Tu", 0, null, null, null, null, null);
		operationCollection.add(null, "65", "TuM", 0, null, null, null, null, null);
		operationCollection.add(null, "49", "ReactiveEnergyPlusArate1", 0, null, null, null, null, null);
		operationCollection.add(null, "4A", "ReactiveEnergyPlusArate2", 0, null, null, null, null, null);
		operationCollection.add(null, "4B", "ReactiveEnergyPlusArate3", 0, null, null, null, null, null);
		operationCollection.add(null, "4D", "ReactiveEnergySubArate1", 0, null, null, null, null, null);
		operationCollection.add(null, "4E", "ReactiveEnergySubArate2", 0, null, null, null, null, null);
		operationCollection.add(null, "4F", "ReactiveEnergySubArate3", 0, null, null, null, null, null);
		operationCollection.add(null, "1A", "ApparentPowerPhaseTotal", 0, null, null, null, null, null);

		return operationCollection;
	}

	public static DataCollection getHistoricalCollection() {
		DataCollection historicalCollection = new DataCollection();
		historicalCollection.add(null, "00", "ServerTime", 0, null, null, null, null, null);
		historicalCollection.add(null, "01", "MeterTime", 0, null, null, null, null, null);
		historicalCollection.add(null, "41", "Ti", 0, null, null, null, null, null);
		historicalCollection.add(null, "64", "TiM", 0, null, null, null, null, null);
		historicalCollection.add(null, "42", "Tu", 0, null, null, null, null, null);
		historicalCollection.add(null, "65", "TuM", 0, null, null, null, null, null);
		historicalCollection.add(null, "27", "ImportWh", 0, null, null, null, null, null);
		historicalCollection.add(null, "1F", "EnergyPlusArate1", 0, null, null, null, null, null);
		historicalCollection.add(null, "20", "EnergyPlusArate2", 0, null, null, null, null, null);
		historicalCollection.add(null, "21", "EnergyPlusArate3", 0, null, null, null, null, null);
		historicalCollection.add(null, "28", "ExportWh", 0, null, null, null, null, null);
		historicalCollection.add(null, "23", "EnergySubArate1", 0, null, null, null, null, null);
		historicalCollection.add(null, "24", "EnergySubArate2", 0, null, null, null, null, null);
		historicalCollection.add(null, "25", "EnergySubArate3", 0, null, null, null, null, null);
		historicalCollection.add(null, "29", "Q1", 0, null, null, null, null, null);
		historicalCollection.add(null, "2A", "Q2", 0, null, null, null, null, null);
		historicalCollection.add(null, "49", "ReactiveEnergyPlusArate1", 0, null, null, null, null, null);
		historicalCollection.add(null, "4A", "ReactiveEnergyPlusArate2", 0, null, null, null, null, null);
		historicalCollection.add(null, "4B", "ReactiveEnergyPlusArate3", 0, null, null, null, null, null);
		historicalCollection.add(null, "2B", "Q3", 0, null, null, null, null, null);
		historicalCollection.add(null, "2C", "Q4", 0, null, null, null, null, null);
		historicalCollection.add(null, "4D", "ReactiveEnergySubArate1", 0, null, null, null, null, null);
		historicalCollection.add(null, "2E", "MaxDemandPlusArate", 0, null, null, null, null, null);
		historicalCollection.add(null, "31", "MaxDemandPlusArate1", 0, null, null, null, null, null);
		historicalCollection.add(null, "35", "MaxDemandPlusArate2", 0, null, null, null, null, null);
		historicalCollection.add(null, "39", "MaxDemandPlusArate3", 0, null, null, null, null, null);
		historicalCollection.add(null, "2F", "MaxDemandPlusArateTime", 0, null, null, null, null, null);
		historicalCollection.add(null, "32", "MaxDemandPlusArate1Time", 0, null, null, null, null, null);
		historicalCollection.add(null, "36", "MaxDemandPlusArate2Time", 0, null, null, null, null, null);
		historicalCollection.add(null, "3A", "MaxDemandPlusArate3Time", 0, null, null, null, null, null);
		historicalCollection.add(null, "5D", "NumberResetBilling", 0, null, null, null, null, null);
		historicalCollection.add(null, "5B", "BeginHistoricalTime", 0, null, null, null, null, null);
		return historicalCollection;
	}

	public static DataCollection getMeterAlertCollection() {
		DataCollection meterAlertCollection = new DataCollection();
		meterAlertCollection.add(null, "00", "ServerTime", 0, null, null, null, null, null);

		meterAlertCollection.add(null, "01", "IsAlertVotPhaseAUnder", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "03", "IsAlertVotPhaseBUnder", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "05", "IsAlertVotPhaseCUnder", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "02", "IsAlertVotPhaseAOver", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "04", "IsAlertVotPhaseBOver", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "06", "IsAlertVotPhaseCOver", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "07", "IsAlertFreqUnder", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "0B", "IsAlertFreqOver", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "0F", "IsAlertPowerFactorUnder", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "10", "IsAlertPowerFactorAUnder", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "11", "IsAlertPowerFactorBUnder", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "12", "IsAlertPowerFactorCUnder", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "13", "IsAlertCurPhaseAOverRated", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "14", "IsAlertCurPhaseBOverRated", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "15", "IsAlertCurPhaseCOverRated", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "16", "IsAlertCurPhaseABOverRated", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "17", "IsAlertCurPhaseBCOverRated", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "18", "IsAlertCurPhaseACOverRated", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "19", "IsAlertCurPhaseAUnderAverageCurrent", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "1A", "IsAlertCurPhaseBUnderAverageCurrent", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "1B", "IsAlertCurPhaseCUnderAverageCurrent", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "1E", "IsAlertVotPhaseAFail", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "1F", "IsAlertVotPhaseBFail", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "20", "IsAlertVotPhaseCFail", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "21", "IsAlertCurrPhaseAFail", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "22", "IsAlertCurrPhaseBFail", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "23", "IsAlertCurrPhaseCFail", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "70", "VotPhaseA", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "71", "VotPhaseB", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "72", "VotPhaseC", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "73", "CurPhaseA", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "74", "CurPhaseB", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "75", "CurPhaseC", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "76", "Freq", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "77", "PowerFactor", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "78", "PowerFactorA", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "79", "PowerFactorB", 0, null, null, null, null, null);
		meterAlertCollection.add(null, "7A", "PowerFactorC", 0, null, null, null, null, null);

		return meterAlertCollection;
	}
}
