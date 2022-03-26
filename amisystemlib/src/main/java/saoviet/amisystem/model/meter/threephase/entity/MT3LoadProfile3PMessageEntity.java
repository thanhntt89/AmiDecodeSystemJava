/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: MT3Business.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class MT3LoadProfile3PMessageEntity {

	public String getDcuCode() {
		return DcuCode;
	}

	public void setDcuCode(String dcuCode) {
		DcuCode = dcuCode;
	}

	public String getMeterCode() {
		return MeterCode;
	}

	public void setMeterCode(String meterCode) {
		MeterCode = meterCode;
	}

	public Timestamp getServerTime() {
		return ServerTime;
	}

	public void setServerTime(Timestamp serverTime) {
		ServerTime = serverTime;
	}

	public Timestamp getMeterTimeFrom() {
		return MeterTimeFrom;
	}

	public void setMeterTimeFrom(Timestamp meterTimeFrom) {
		MeterTimeFrom = meterTimeFrom;
	}

	public Timestamp getMeterTimeTo() {
		return MeterTimeTo;
	}

	public void setMeterTimeTo(Timestamp meterTimeTo) {
		MeterTimeTo = meterTimeTo;
	}

	public BigDecimal getVoltagePhaseA() {
		return VoltagePhaseA;
	}

	public void setVoltagePhaseA(BigDecimal voltagePhaseA) {
		VoltagePhaseA = voltagePhaseA;
	}

	public BigDecimal getVoltagePhaseB() {
		return VoltagePhaseB;
	}

	public void setVoltagePhaseB(BigDecimal voltagePhaseB) {
		VoltagePhaseB = voltagePhaseB;
	}

	public BigDecimal getVoltagePhaseC() {
		return VoltagePhaseC;
	}

	public void setVoltagePhaseC(BigDecimal voltagePhaseC) {
		VoltagePhaseC = voltagePhaseC;
	}

	public BigDecimal getCurrentPhaseA() {
		return CurrentPhaseA;
	}

	public void setCurrentPhaseA(BigDecimal currentPhaseA) {
		CurrentPhaseA = currentPhaseA;
	}

	public BigDecimal getCurrentPhaseB() {
		return CurrentPhaseB;
	}

	public void setCurrentPhaseB(BigDecimal currentPhaseB) {
		CurrentPhaseB = currentPhaseB;
	}

	public BigDecimal getCurrentPhaseC() {
		return CurrentPhaseC;
	}

	public void setCurrentPhaseC(BigDecimal currentPhaseC) {
		CurrentPhaseC = currentPhaseC;
	}

	public BigDecimal getPhaseAnglePhaseA() {
		return PhaseAnglePhaseA;
	}

	public void setPhaseAnglePhaseA(BigDecimal phaseAnglePhaseA) {
		PhaseAnglePhaseA = phaseAnglePhaseA;
	}

	public BigDecimal getPhaseAnglePhaseB() {
		return PhaseAnglePhaseB;
	}

	public void setPhaseAnglePhaseB(BigDecimal phaseAnglePhaseB) {
		PhaseAnglePhaseB = phaseAnglePhaseB;
	}

	public BigDecimal getPhaseAnglePhaseC() {
		return PhaseAnglePhaseC;
	}

	public void setPhaseAnglePhaseC(BigDecimal phaseAnglePhaseC) {
		PhaseAnglePhaseC = phaseAnglePhaseC;
	}

	public BigDecimal getActivePowerPhaseA() {
		return ActivePowerPhaseA;
	}

	public void setActivePowerPhaseA(BigDecimal activePowerPhaseA) {
		ActivePowerPhaseA = activePowerPhaseA;
	}

	public BigDecimal getActivePowerPhaseB() {
		return ActivePowerPhaseB;
	}

	public void setActivePowerPhaseB(BigDecimal activePowerPhaseB) {
		ActivePowerPhaseB = activePowerPhaseB;
	}

	public BigDecimal getActivePowerPhaseC() {
		return ActivePowerPhaseC;
	}

	public void setActivePowerPhaseC(BigDecimal activePowerPhaseC) {
		ActivePowerPhaseC = activePowerPhaseC;
	}

	public BigDecimal getActivePowerPhaseTotal() {
		return ActivePowerPhaseTotal;
	}

	public void setActivePowerPhaseTotal(BigDecimal activePowerPhaseTotal) {
		ActivePowerPhaseTotal = activePowerPhaseTotal;
	}

	public BigDecimal getReactivePowerPhaseA() {
		return ReactivePowerPhaseA;
	}

	public void setReactivePowerPhaseA(BigDecimal reactivePowerPhaseA) {
		ReactivePowerPhaseA = reactivePowerPhaseA;
	}

	public BigDecimal getReactivePowerPhaseB() {
		return ReactivePowerPhaseB;
	}

	public void setReactivePowerPhaseB(BigDecimal reactivePowerPhaseB) {
		ReactivePowerPhaseB = reactivePowerPhaseB;
	}

	public BigDecimal getReactivePowerPhaseC() {
		return ReactivePowerPhaseC;
	}

	public void setReactivePowerPhaseC(BigDecimal reactivePowerPhaseC) {
		ReactivePowerPhaseC = reactivePowerPhaseC;
	}

	public BigDecimal getReactivePowerPhaseTotal() {
		return ReactivePowerPhaseTotal;
	}

	public void setReactivePowerPhaseTotal(BigDecimal reactivePowerPhaseTotal) {
		ReactivePowerPhaseTotal = reactivePowerPhaseTotal;
	}

	public BigDecimal getApparentPowerPhaseA() {
		return ApparentPowerPhaseA;
	}

	public void setApparentPowerPhaseA(BigDecimal apparentPowerPhaseA) {
		ApparentPowerPhaseA = apparentPowerPhaseA;
	}

	public BigDecimal getApparentPowerPhaseB() {
		return ApparentPowerPhaseB;
	}

	public void setApparentPowerPhaseB(BigDecimal apparentPowerPhaseB) {
		ApparentPowerPhaseB = apparentPowerPhaseB;
	}

	public BigDecimal getApparentPowerPhaseC() {
		return ApparentPowerPhaseC;
	}

	public void setApparentPowerPhaseC(BigDecimal apparentPowerPhaseC) {
		ApparentPowerPhaseC = apparentPowerPhaseC;
	}

	public BigDecimal getApparentPowerPhaseTotal() {
		return ApparentPowerPhaseTotal;
	}

	public void setApparentPowerPhaseTotal(BigDecimal apparentPowerPhaseTotal) {
		ApparentPowerPhaseTotal = apparentPowerPhaseTotal;
	}

	public BigDecimal getPowerFactorPhaseA() {
		return PowerFactorPhaseA;
	}

	public void setPowerFactorPhaseA(BigDecimal powerFactorPhaseA) {
		PowerFactorPhaseA = powerFactorPhaseA;
	}

	public BigDecimal getPowerFactorPhaseB() {
		return PowerFactorPhaseB;
	}

	public void setPowerFactorPhaseB(BigDecimal powerFactorPhaseB) {
		PowerFactorPhaseB = powerFactorPhaseB;
	}

	public BigDecimal getPowerFactorPhaseC() {
		return PowerFactorPhaseC;
	}

	public void setPowerFactorPhaseC(BigDecimal powerFactorPhaseC) {
		PowerFactorPhaseC = powerFactorPhaseC;
	}

	public BigDecimal getPowerFactorPhaseTotal() {
		return PowerFactorPhaseTotal;
	}

	public void setPowerFactorPhaseTotal(BigDecimal powerFactorPhaseTotal) {
		PowerFactorPhaseTotal = powerFactorPhaseTotal;
	}

	public BigDecimal getEnergyPlusARate1() {
		return EnergyPlusARate1;
	}

	public void setEnergyPlusARate1(BigDecimal energyPlusARate1) {
		EnergyPlusARate1 = energyPlusARate1;
	}

	public BigDecimal getEnergyPlusARate2() {
		return EnergyPlusARate2;
	}

	public void setEnergyPlusARate2(BigDecimal energyPlusARate2) {
		EnergyPlusARate2 = energyPlusARate2;
	}

	public BigDecimal getEnergyPlusARate3() {
		return EnergyPlusARate3;
	}

	public void setEnergyPlusARate3(BigDecimal energyPlusARate3) {
		EnergyPlusARate3 = energyPlusARate3;
	}

	public BigDecimal getEnergySubARate1() {
		return EnergySubARate1;
	}

	public void setEnergySubARate1(BigDecimal energySubARate1) {
		EnergySubARate1 = energySubARate1;
	}

	public BigDecimal getEnergySubARate2() {
		return EnergySubARate2;
	}

	public void setEnergySubARate2(BigDecimal energySubARate2) {
		EnergySubARate2 = energySubARate2;
	}

	public BigDecimal getEnergySubARate3() {
		return EnergySubARate3;
	}

	public void setEnergySubARate3(BigDecimal energySubARate3) {
		EnergySubARate3 = energySubARate3;
	}

	public BigDecimal getQ1() {
		return Q1;
	}

	public void setQ1(BigDecimal q1) {
		Q1 = q1;
	}

	public BigDecimal getQ3() {
		return Q3;
	}

	public void setQ3(BigDecimal q3) {
		Q3 = q3;
	}

	public BigDecimal getImportWh() {
		return ImportWh;
	}

	public void setImportWh(BigDecimal importWh) {
		ImportWh = importWh;
	}

	public BigDecimal getExportWh() {
		return ExportWh;
	}

	public void setExportWh(BigDecimal exportWh) {
		ExportWh = exportWh;
	}

	public BigDecimal getReactiveEnergyPlusARate1() {
		return ReactiveEnergyPlusARate1;
	}

	public void setReactiveEnergyPlusARate1(BigDecimal reactiveEnergyPlusARate1) {
		ReactiveEnergyPlusARate1 = reactiveEnergyPlusARate1;
	}

	public BigDecimal getReactiveEnergyPlusARate2() {
		return ReactiveEnergyPlusARate2;
	}

	public void setReactiveEnergyPlusARate2(BigDecimal reactiveEnergyPlusARate2) {
		ReactiveEnergyPlusARate2 = reactiveEnergyPlusARate2;
	}

	public BigDecimal getReactiveEnergyPlusARate3() {
		return ReactiveEnergyPlusARate3;
	}

	public void setReactiveEnergyPlusARate3(BigDecimal reactiveEnergyPlusARate3) {
		ReactiveEnergyPlusARate3 = reactiveEnergyPlusARate3;
	}

	public BigDecimal getReactiveEnergySubARate1() {
		return ReactiveEnergySubARate1;
	}

	public void setReactiveEnergySubARate1(BigDecimal reactiveEnergySubARate1) {
		ReactiveEnergySubARate1 = reactiveEnergySubARate1;
	}

	public BigDecimal getReactiveEnergySubARate2() {
		return ReactiveEnergySubARate2;
	}

	public void setReactiveEnergySubARate2(BigDecimal reactiveEnergySubARate2) {
		ReactiveEnergySubARate2 = reactiveEnergySubARate2;
	}

	public BigDecimal getReactiveEnergySubARate3() {
		return ReactiveEnergySubARate3;
	}

	public void setReactiveEnergySubARate3(BigDecimal reactiveEnergySubARate3) {
		ReactiveEnergySubARate3 = reactiveEnergySubARate3;
	}

	public BigDecimal getPowerAvgPos() {
		return PowerAvgPos;
	}

	public void setPowerAvgPos(BigDecimal powerAvgPos) {
		PowerAvgPos = powerAvgPos;
	}

	public BigDecimal getPowerAvgNeg() {
		return PowerAvgNeg;
	}

	public void setPowerAvgNeg(BigDecimal powerAvgNeg) {
		PowerAvgNeg = powerAvgNeg;
	}

	public BigDecimal getReactivePowerAvgPos() {
		return ReactivePowerAvgPos;
	}

	public void setReactivePowerAvgPos(BigDecimal reactivePowerAvgPos) {
		ReactivePowerAvgPos = reactivePowerAvgPos;
	}

	public BigDecimal getReactivePowerAvgNeg() {
		return ReactivePowerAvgNeg;
	}

	public void setReactivePowerAvgNeg(BigDecimal reactivePowerAvgNeg) {
		ReactivePowerAvgNeg = reactivePowerAvgNeg;
	}

	public BigDecimal getLastAvgPowerFactor() {
		return LastAvgPowerFactor;
	}

	public void setLastAvgPowerFactor(BigDecimal lastAvgPowerFactor) {
		LastAvgPowerFactor = lastAvgPowerFactor;
	}

	private String DcuCode;
	private String MeterCode;
	private Timestamp ServerTime;
	private Timestamp MeterTimeFrom;
	private Timestamp MeterTimeTo;
	private BigDecimal VoltagePhaseA;
	private BigDecimal VoltagePhaseB;
	private BigDecimal VoltagePhaseC;
	private BigDecimal CurrentPhaseA;
	private BigDecimal CurrentPhaseB;
	private BigDecimal CurrentPhaseC;
	private BigDecimal PhaseAnglePhaseA;
	private BigDecimal PhaseAnglePhaseB;
	private BigDecimal PhaseAnglePhaseC;
	private BigDecimal ActivePowerPhaseA;
	private BigDecimal ActivePowerPhaseB;
	private BigDecimal ActivePowerPhaseC;
	private BigDecimal ActivePowerPhaseTotal;
	private BigDecimal ReactivePowerPhaseA;
	private BigDecimal ReactivePowerPhaseB;
	private BigDecimal ReactivePowerPhaseC;
	private BigDecimal ReactivePowerPhaseTotal;
	private BigDecimal ApparentPowerPhaseA;
	private BigDecimal ApparentPowerPhaseB;
	private BigDecimal ApparentPowerPhaseC;
	private BigDecimal ApparentPowerPhaseTotal;
	private BigDecimal PowerFactorPhaseA;
	private BigDecimal PowerFactorPhaseB;
	private BigDecimal PowerFactorPhaseC;
	private BigDecimal PowerFactorPhaseTotal;
	private BigDecimal EnergyPlusARate1;
	private BigDecimal EnergyPlusARate2;
	private BigDecimal EnergyPlusARate3;
	private BigDecimal EnergySubARate1;
	private BigDecimal EnergySubARate2;
	private BigDecimal EnergySubARate3;
	private BigDecimal Q1;
	private BigDecimal Q3;
	private BigDecimal ImportWh;
	private BigDecimal ExportWh;
	private BigDecimal ReactiveEnergyPlusARate1;
	private BigDecimal ReactiveEnergyPlusARate2;
	private BigDecimal ReactiveEnergyPlusARate3;
	private BigDecimal ReactiveEnergySubARate1;
	private BigDecimal ReactiveEnergySubARate2;
	private BigDecimal ReactiveEnergySubARate3;
	private BigDecimal PowerAvgPos;
	private BigDecimal PowerAvgNeg;
	private BigDecimal ReactivePowerAvgPos;
	private BigDecimal ReactivePowerAvgNeg;
	private BigDecimal LastAvgPowerFactor;

}
