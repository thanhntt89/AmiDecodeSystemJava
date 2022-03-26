/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: MessagePacketStructureViettelDcu.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.packetvietteldcu;

import java.math.BigDecimal;

import saoviet.amisystem.model.datacollection.DataCollection;

public class MessagePacketStructureViettelDcu {

	public static DataCollection getLoadProfileCollection() {
		DataCollection loadProfileCollection = new DataCollection();
		loadProfileCollection.add("06", "06", "ServerTime", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "10", "ImportWh", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "20", "ExportWh", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "30", "Q1", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "40", "Q3", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "11", "EnergyPlusARate1", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "12", "EnergyPlusARate2", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "13", "EnergyPlusARate3", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "21", "EnergySubARate1", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "22", "EnergySubARate2", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "23", "EnergySubARate3", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "41", "ReactiveEnergyPlusARate1", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "42", "ReactiveEnergyPlusARate2", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "44", "ReactiveEnergyPlusARate3", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "31", "ReactiveEnergySubARate1", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "32", "ReactiveEnergySubARate2", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "33", "ReactiveEnergySubARate3", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "AF", "VoltagePhaseA", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "B0", "VoltagePhaseB", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "B1", "VoltagePhaseC", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "B3", "CurrentPhaseA", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "B4", "CurrentPhaseB", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "B5", "CurrentPhaseC", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "AB", "ActivePowerPhaseA", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "AC", "ActivePowerPhaseB", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "AD", "ActivePowerPhaseC", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "AA", "ActivePowerPhaseTotal", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "BD", "ReactivePowerPhaseA", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "BE", "ReactivePowerPhaseB", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "BF", "ReactivePowerPhaseC", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "BC", "ReactivePowerPhaseTotal", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "7B", "ApparentPowerPhaseA", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "7C", "ApparentPowerPhaseB", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "7D", "ApparentPowerPhaseC", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "7E", "ApparentPowerPhaseTotal", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "A1", "PowerFactorPhaseA", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "A2", "PowerFactorPhaseB", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "A3", "PowerFactorPhaseC", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "A0", "PowerFactorPhaseTotal", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "A4", "PhaseAnglePhaseA", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "A7", "PhaseAnglePhaseB", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "A8", "PhaseAnglePhaseC", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "02", "MeterTime", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "85", "PowerAvgPos", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "86", "PowerAvgNeg", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "87", "ReactivePowerAvgPos", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "88", "ReactivePowerAvgNeg", 0, null, null, null, null, null);
		loadProfileCollection.add("24", "DA", "LastAvgPowerFactor", 0, null, null, null, null, null);

		return loadProfileCollection;
	}

	public static DataCollection getEventCollection() {
		DataCollection eventCollection = new DataCollection();
		eventCollection.add("06", "06", "ServerTime", 0, null, null, null, null, null);
		eventCollection.add("24", "80", "EventProgramming", 0, null, null, null, null, null);
		eventCollection.add("24", "8B", "EventPasswordChange", 0, null, null, null, null, null);
		eventCollection.add("24", "1A", "EventSystem", 0, null, null, null, null, null);
		eventCollection.add("24", "2E", "EventVoltageImbalance", 0, null, null, null, null, null);
		eventCollection.add("24", "E0", "EventPowerFailure", 0, null, null, null, null, null);
		eventCollection.add("24", "4A", "EventTimeDateChange", 0, null, null, null, null, null);
		eventCollection.add("24", "E1", "EventFailurePhaseA", 0, null, null, null, null, null);
		eventCollection.add("24", "E2", "EventFailurePhaseB", 0, null, null, null, null, null);
		eventCollection.add("24", "E3", "EventFailurePhaseC", 0, null, null, null, null, null);
		eventCollection.add("24", "9B", "EventReverseRunPhaseA", 0, null, null, null, null, null);
		eventCollection.add("24", "9C", "EventReverseRunPhaseB", 0, null, null, null, null, null);
		eventCollection.add("24", "9D", "EventReverseRunPhaseC", 0, null, null, null, null, null);
		eventCollection.add("24", "83", "EventPowerOn", 0, null, null, null, null, null);
		eventCollection.add("24", "E9", "EventResetCounter", 0, null, null, null, null, null);
		eventCollection.add("24", "4B", "EventReverseRunThreePhase", 0, null, null, null, null, null);
		eventCollection.add("24", "8C", "EventBatteryError", 0, null, null, null, null, null);
		eventCollection.add("24", "84", "EventPowerFailureEnd", 0, null, null, null, null, null);
		eventCollection.add("24", "99", "EventPowerFailureStart", 0, null, null, null, null, null);

		return eventCollection;
	}

	public static DataCollection getOperationCollection() {
		DataCollection operationCollection = new DataCollection();
		operationCollection.add("06", "06", "ServerTime", 0, null, null, null, null, null);
		operationCollection.add("27", "01", "MeterId", 0, null, null, null, null, null);
		operationCollection.add("24", "10", "ImportWh", 0, null, null, null, null, null);
		operationCollection.add("24", "20", "ExportWh", 0, null, null, null, null, null);
		operationCollection.add("24", "90", "EnergyPlusVA", 0, null, null, null, null, null);
		operationCollection.add("24", "30", "Q1", 0, null, null, null, null, null);
		operationCollection.add("24", "40", "Q3", 0, null, null, null, null, null);
		operationCollection.add("24", "11", "EnergyPlusARate1", 0, null, null, null, null, null);
		operationCollection.add("24", "12", "EnergyPlusARate2", 0, null, null, null, null, null);
		operationCollection.add("24", "13", "EnergyPlusARate3", 0, null, null, null, null, null);
		operationCollection.add("24", "21", "EnergySubARate1", 0, null, null, null, null, null);
		operationCollection.add("24", "22", "EnergySubARate2", 0, null, null, null, null, null);
		operationCollection.add("24", "23", "EnergySubARate3", 0, null, null, null, null, null);
		operationCollection.add("24", "C2", "MaxDemandPlusARate1", 0, null, null, null, null, null);
		operationCollection.add("24", "C3", "MaxDemandPlusARate2", 0, null, null, null, null, null);
		operationCollection.add("24", "C4", "MaxDemandPlusARate3", 0, null, null, null, null, null);
		operationCollection.add("24", "DC", "MaxDemandPlusARate4", 0, null, null, null, null, null);
		operationCollection.add("24", "C5", "MaxDemandSubARate1 ", 0, null, null, null, null, null);
		operationCollection.add("24", "C6", "MaxDemandSubARate2", 0, null, null, null, null, null);
		operationCollection.add("24", "C7", "MaxDemandSubARate3", 0, null, null, null, null, null);
		operationCollection.add("24", "43", "MaxDemandSubARate4", 0, null, null, null, null, null);
		operationCollection.add("24", "AF", "VoltagePhaseA", 0, null, null, null, null, null);
		operationCollection.add("24", "B0", "VoltagePhaseB", 0, null, null, null, null, null);
		operationCollection.add("24", "B1", "VoltagePhaseC", 0, null, null, null, null, null);
		operationCollection.add("24", "B3", "CurrentPhaseA", 0, null, null, null, null, null);
		operationCollection.add("24", "B4", "CurrentPhaseB", 0, null, null, null, null, null);
		operationCollection.add("24", "B5", "CurrentPhaseC", 0, null, null, null, null, null);
		operationCollection.add("24", "AB", "ActivePowerPhaseA", 0, null, null, null, null, null);
		operationCollection.add("24", "AC", "ActivePowerPhaseB", 0, null, null, null, null, null);
		operationCollection.add("24", "AD", "ActivePowerPhaseC", 0, null, null, null, null, null);
		operationCollection.add("24", "AA", "ActivePowerPhaseTotal", 0, null, null, null, null, null);
		operationCollection.add("24", "BD", "ReactivePowerPhaseA", 0, null, null, null, null, null);
		operationCollection.add("24", "BE", "ReactivePowerPhaseB", 0, null, null, null, null, null);
		operationCollection.add("24", "BF", "ReactivePowerPhaseC", 0, null, null, null, null, null);
		operationCollection.add("24", "BC", "ReactivePowerPhaseTotal", 0, null, null, null, null, null);
		operationCollection.add("24", "7B", "ApparentPowerPhaseA", 0, null, null, null, null, null);
		operationCollection.add("24", "7C", "ApparentPowerPhaseB", 0, null, null, null, null, null);
		operationCollection.add("24", "7D", "ApparentPowerPhaseC", 0, null, null, null, null, null);
		operationCollection.add("24", "7E", "ApparentPowerPhaseTotal", 0, null, null, null, null, null);
		operationCollection.add("24", "A1", "PowerFactorPhaseA", 0, null, null, null, null, null);
		operationCollection.add("24", "A2", "PowerFactorPhaseB", 0, null, null, null, null, null);
		operationCollection.add("24", "A3", "PowerFactorPhaseC", 0, null, null, null, null, null);
		operationCollection.add("24", "A0", "PowerFactorPhaseTotal", 0, null, null, null, null, null);
		operationCollection.add("24", "A9", "FreqA", 0, null, null, null, null, null);
		operationCollection.add("24", "73", "FreqB", 0, null, null, null, null, null);
		operationCollection.add("24", "98", "FreqC", 0, null, null, null, null, null);
		operationCollection.add("24", "A4", "PhaseAnglePhaseA", 0, null, null, null, null, null);
		operationCollection.add("24", "A7", "PhaseAnglePhaseB", 0, null, null, null, null, null);
		operationCollection.add("24", "A8", "PhaseAnglePhaseC", 0, null, null, null, null, null);
		operationCollection.add("24", "8D", "PhaseRotation", 0, null, null, null, null, null);
		operationCollection.add("24", "C9", "Tu", 0, null, null, null, null, null);
		operationCollection.add("24", "C8", "Ti", 0, null, null, null, null, null);
		operationCollection.add("24", "8E", "Signal", 0, null, null, null, null, null);
		operationCollection.add("24", "02", "MeterTime", 0, null, null, null, null, null);
		operationCollection.add("27", "02", "SvnVersion", 0, null, null, null, null, null);
		operationCollection.add("27", "03", "ProtocolVersion", 0, null, null, null, null, null);
		operationCollection.add("24", "41", "ReactiveEnergyPlusARate1", 0, null, null, null, null, null);
		operationCollection.add("24", "42", "ReactiveEnergyPlusARate2", 0, null, null, null, null, null);
		operationCollection.add("24", "44", "ReactiveEnergyPlusARate3", 0, null, null, null, null, null);
		operationCollection.add("24", "31", "ReactiveEnergySubARate1", 0, null, null, null, null, null);
		operationCollection.add("24", "32", "ReactiveEnergySubARate2", 0, null, null, null, null, null);
		operationCollection.add("24", "33", "ReactiveEnergySubARate3", 0, null, null, null, null, null);

		return operationCollection;
	}

	public static DataCollection getHistoricalCollection() {
		DataCollection historicalCollection = new DataCollection();
		historicalCollection.add("06", "06", "ServerTime", 0, null, null, null, null, null);
		historicalCollection.add("24", "02", "HistoricalTime", 0, null, null, null, null, null);
		historicalCollection.add("24", "10", "ImportWh", 0, null, null, null, null, null);
		historicalCollection.add("24", "20", "ExportWh", 0, null, null, null, null, null);
		historicalCollection.add("24", "30", "Q1", 0, null, null, null, null, null);
		historicalCollection.add("24", "60", "Q2", 0, null, null, null, null, null);
		historicalCollection.add("24", "40", "Q3", 0, null, null, null, null, null);
		historicalCollection.add("24", "8A", "Q4", 0, null, null, null, null, null);
		historicalCollection.add("24", "11", "EnergyPlusARate1", 0, null, null, null, null, null);
		historicalCollection.add("24", "12", "EnergyPlusARate2", 0, null, null, null, null, null);
		historicalCollection.add("24", "13", "EnergyPlusARate3", 0, null, null, null, null, null);
		historicalCollection.add("24", "21", "EnergySubARate1", 0, null, null, null, null, null);
		historicalCollection.add("24", "22", "EnergySubARate2", 0, null, null, null, null, null);
		historicalCollection.add("24", "23", "EnergySubARate3", 0, null, null, null, null, null);
		historicalCollection.add("24", "90", "EnergyPlusVA", 0, null, null, null, null, null);
		historicalCollection.add("24", "C2", "MaxDemandPlusARate1", 0, null, null, null, null, null);
		historicalCollection.add("24", "C3", "MaxDemandPlusARate2", 0, null, null, null, null, null);
		historicalCollection.add("24", "C4", "MaxDemandPlusARate3", 0, null, null, null, null, null);
		historicalCollection.add("24", "DC", "MaxDemandPlusARate4", 0, null, null, null, null, null);
		historicalCollection.add("24", "41", "ReactiveEnergyPlusARate1", 0, null, null, null, null, null);
		historicalCollection.add("24", "42", "ReactiveEnergyPlusARate2", 0, null, null, null, null, null);
		historicalCollection.add("24", "44", "ReactiveEnergyPlusARate3", 0, null, null, null, null, null);
		historicalCollection.add("24", "31", "ReactiveEnergySubARate1", 0, null, null, null, null, null);
		historicalCollection.add("24", "32", "ReactiveEnergySubARate2", 0, null, null, null, null, null);
		historicalCollection.add("24", "33", "ReactiveEnergySubARate3", 0, null, null, null, null, null);

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

	public static DataCollection getElsterObisScaleList() {
		
		DataCollection operationCollection = new DataCollection();
		operationCollection.add(null, "10", "ImportWh", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "20", "ExportWh", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "90", "EnergyPlusVA", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "30", "Q1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "40", "Q3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "11", "EnergyPlusARate1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "12", "EnergyPlusARate2", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "13", "EnergyPlusARate3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "21", "EnergySubARate1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "22", "EnergySubARate2", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "23", "EnergySubARate3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "C2", "MaxDemandPlusARate1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "C3", "MaxDemandPlusARate2", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "C4", "MaxDemandPlusARate3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "DC", "MaxDemandPlusARate4", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "C5", "MaxDemandSubARate1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "C6", "MaxDemandSubARate2", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "C7", "MaxDemandSubARate3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "43", "MaxDemandSubARate4", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "A1", "PowerFactorPhaseA", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add(null, "A2", "PowerFactorPhaseB", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add(null, "A3", "PowerFactorPhaseC", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add(null, "A0", "PowerFactorPhaseTotal", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add(null, "A9", "FreqA", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add(null, "73", "FreqB", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add(null, "98", "FreqC", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add(null, "A4", "PhaseAnglePhaseA", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add(null, "A7", "PhaseAnglePhaseB", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add(null, "A8", "PhaseAnglePhaseC", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add(null, "8D", "PhaseRotation", 0, null, null, BigDecimal.valueOf(1), null, null);
		operationCollection.add(null, "C9", "Tu", 0, null, null, BigDecimal.valueOf(1), null, null);
		operationCollection.add(null, "C8", "Ti", 0, null, null, BigDecimal.valueOf(1), null, null);
		operationCollection.add(null, "8E", "Signal", 0, null, null, BigDecimal.valueOf(1), null, null);
		operationCollection.add(null, "41", "ReactiveEnergyPlusARate1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "42", "ReactiveEnergyPlusARate2", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "44", "ReactiveEnergyPlusARate3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "31", "ReactiveEnergySubARate1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "32", "ReactiveEnergySubARate2", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add(null, "33", "ReactiveEnergySubARate3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);

		return operationCollection;
	}

	public static DataCollection getStarObisScaleList() {
		
		DataCollection operationCollection = new DataCollection();
		operationCollection.add (null , "10", "ImportWh", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "20", "ExportWh", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "90", "EnergyPlusVA", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "30", "Q1", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "40", "Q3", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "11", "EnergyPlusARate1", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "12", "EnergyPlusARate2", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "13", "EnergyPlusARate3", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "21", "EnergySubARate1", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "22", "EnergySubARate2", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "23", "EnergySubARate3", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "C2", "MaxDemandPlusARate1", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "C3", "MaxDemandPlusARate2", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "C4", "MaxDemandPlusARate3", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "DC", "MaxDemandPlusARate4", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "C5", "MaxDemandSubARate1", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "C6", "MaxDemandSubARate2", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "C7", "MaxDemandSubARate3", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "43", "MaxDemandSubARate4", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "AF", "VoltagePhaseA", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "B0", "VoltagePhaseB", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "B1", "VoltagePhaseC", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "B3", "CurrentPhaseA", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "B4", "CurrentPhaseB", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "B5", "CurrentPhaseC", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "AB", "ActivePowerPhaseA", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "AC", "ActivePowerPhaseB", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "AD", "ActivePowerPhaseC", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "AA", "ActivePowerPhaseTotal", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "BD", "ReactivePowerPhaseA", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "BE", "ReactivePowerPhaseB", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "BF", "ReactivePowerPhaseC", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "BC", "ReactivePowerPhaseTotal", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "A1", "PowerFactorPhaseA", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "A2", "PowerFactorPhaseB", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "A3", "PowerFactorPhaseC", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "A0", "PowerFactorPhaseTotal", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "A9", "FreqA", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "73", "FreqB", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "98", "FreqC", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "C9", "Tu", 0, null, null, BigDecimal.valueOf(1), null, null);
		operationCollection.add (null , "C8", "Ti", 0, null, null, BigDecimal.valueOf(1), null, null);
		operationCollection.add (null , "8E", "Signal", 0, null, null, BigDecimal.valueOf(1), null, null);
		operationCollection.add (null , "41", "ReactiveEnergyPlusARate1", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "42", "ReactiveEnergyPlusARate2", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "44", "ReactiveEnergyPlusARate3", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "31", "ReactiveEnergySubARate1", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "32", "ReactiveEnergySubARate2", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		operationCollection.add (null , "33", "ReactiveEnergySubARate3", 0, null, null, BigDecimal.valueOf(0.01), null, null);
		return operationCollection;
	}

	public static DataCollection getGelexObisScaleList() {
		
		DataCollection operationCollection = new DataCollection();
		operationCollection.add (null , "10", "ImportWh", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "20", "ExportWh", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "30", "Q1", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "40", "Q3", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "11", "EnergyPlusARate1", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "12", "EnergyPlusARate2", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "13", "EnergyPlusARate3", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "21", "EnergySubARate1", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "22", "EnergySubARate2", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "23", "EnergySubARate3", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "C2", "MaxDemandPlusARate1", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "C3", "MaxDemandPlusARate2", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "C4", "MaxDemandPlusARate3", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "DC", "MaxDemandPlusARate4", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "C5", "MaxDemandSubARate1", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "C6", "MaxDemandSubARate2", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "C7", "MaxDemandSubARate3", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "43", "MaxDemandSubARate4", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "AF", "VoltagePhaseA", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.01), null, null);
		operationCollection.add (null , "B0", "VoltagePhaseB", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.01), null, null);
		operationCollection.add (null , "B1", "VoltagePhaseC", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.01), null, null);
		operationCollection.add (null , "B3", "CurrentPhaseA", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.01), null, null);
		operationCollection.add (null , "B4", "CurrentPhaseB", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.01), null, null);
		operationCollection.add (null , "B5", "CurrentPhaseC", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.01), null, null);
		operationCollection.add (null , "AB", "ActivePowerPhaseA", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "AC", "ActivePowerPhaseB", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "AD", "ActivePowerPhaseC", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "AA", "ActivePowerPhaseTotal", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "BD", "ReactivePowerPhaseA", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "BE", "ReactivePowerPhaseB", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "BF", "ReactivePowerPhaseC", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "BC", "ReactivePowerPhaseTotal", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "7B", "ApparentPowerPhaseA", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "7C", "ApparentPowerPhaseB", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "7D", "ApparentPowerPhaseC", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "7E", "ApparentPowerPhaseTotal", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.01), null, null);
		operationCollection.add (null , "A1", "PowerFactorPhaseA", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "A2", "PowerFactorPhaseB", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "A3", "PowerFactorPhaseC", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "A0", "PowerFactorPhaseTotal", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "A9", "Frequency", 0, null, BigDecimal.valueOf(0.01), BigDecimal.valueOf( 0.01), null, null);
		operationCollection.add (null , "C9", "Tu", 0, null, BigDecimal.valueOf(1), BigDecimal.valueOf( 1), null, null);
		operationCollection.add (null , "C8", "Ti", 0, null, BigDecimal.valueOf(1), BigDecimal.valueOf( 1), null, null);
		operationCollection.add (null , "8E", "Signal", 0, null, BigDecimal.valueOf(1), BigDecimal.valueOf( 1), null, null);
		operationCollection.add (null , "17", "Maxdemand Value", 0, null, BigDecimal.valueOf(0.001), BigDecimal.valueOf( 0.001), null, null);
		operationCollection.add (null , "41", "ReactiveEnergyPlusARate1", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "42", "ReactiveEnergyPlusARate2", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "44", "ReactiveEnergyPlusARate3", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "31", "ReactiveEnergySubARate1", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "32", "ReactiveEnergySubARate2", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		operationCollection.add (null , "33", "ReactiveEnergySubARate3", 0, null, BigDecimal.valueOf(0.000000001), BigDecimal.valueOf( 0.000000001), null, null);
		return operationCollection;
	}

	public static DataCollection getLandisObisScaleList() {
		
		DataCollection operationCollection = new DataCollection();
		operationCollection.add (null , "C2", "MaxDemandPlusARate1", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "C3", "MaxDemandPlusARate2", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "C4", "MaxDemandPlusARate3", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "DC", "MaxDemandPlusARate4", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "C5", "MaxDemandSubARate1", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "C6", "MaxDemandSubARate2", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "C7", "MaxDemandSubARate3", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		operationCollection.add (null , "43", "MaxDemandSubARate4", 0, null, null, BigDecimal.valueOf(0.0001), null, null);
		return operationCollection;
	}

	public static DataCollection getGeniusObisScaleList() {
		DataCollection operationCollection = new DataCollection();
		operationCollection.add (null , "10", "ImportWh", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "20", "ExportWh", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "90", "EnergyPlusVA", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "30", "Q1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "40", "Q3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "11", "EnergyPlusARate1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "12", "EnergyPlusARate2", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "13", "EnergyPlusARate3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "21", "EnergySubARate1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "22", "EnergySubARate2", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "23", "EnergySubARate3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "C2", "MaxDemandPlusARate1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "C3", "MaxDemandPlusARate2", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "C4", "MaxDemandPlusARate3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "DC", "MaxDemandPlusARate4", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "C5", "MaxDemandSubARate1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "C6", "MaxDemandSubARate2", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "C7", "MaxDemandSubARate3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "43", "MaxDemandSubARate4", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "AF", "VoltagePhaseA", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "B0", "VoltagePhaseB", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "B1", "VoltagePhaseC", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "B3", "CurrentPhaseA", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "B4", "CurrentPhaseB", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "B5", "CurrentPhaseC", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "AB", "ActivePowerPhaseA", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "AC", "ActivePowerPhaseB", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "AD", "ActivePowerPhaseC", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "AA", "ActivePowerPhaseTotal", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "BD", "ReactivePowerPhaseA", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "BE", "ReactivePowerPhaseB", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "BF", "ReactivePowerPhaseC", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "BC", "ReactivePowerPhaseTotal", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "7B", "ApparentPowerPhaseA", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "7C", "ApparentPowerPhaseB", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "7D", "ApparentPowerPhaseC", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "7E", "ApparentPowerPhaseTotal", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "A0", "PowerFactorPhaseTotal", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "A9", "FreqA", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "73", "FreqB", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "98", "FreqC", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "A4", "PhaseAnglePhaseA", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "A7", "PhaseAnglePhaseB", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "A8", "PhaseAnglePhaseC", 0, null, null, BigDecimal.valueOf(0.001), null, null);
		operationCollection.add (null , "C9", "Tu", 0, null, null, BigDecimal.valueOf(1), null, null);
		operationCollection.add (null , "C8", "Ti", 0, null, null, BigDecimal.valueOf(1), null, null);
		operationCollection.add (null , "8E", "Signal", 0, null, null, BigDecimal.valueOf(1), null, null);
		operationCollection.add (null , "41", "ReactiveEnergyPlusARate1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "42", "ReactiveEnergyPlusARate2", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "44", "ReactiveEnergyPlusARate3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "31", "ReactiveEnergySubARate1", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "32", "ReactiveEnergySubARate2", 0, null, null, BigDecimal.valueOf(0.000001), null, null);
		operationCollection.add (null , "33", "ReactiveEnergySubARate3", 0, null, null, BigDecimal.valueOf(0.000001), null, null);

		return operationCollection;
	}

}
