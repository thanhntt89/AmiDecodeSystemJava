/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: MT3Business.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class MT3OperationEntity implements Serializable {
	private String Topic;
	private Timestamp ServerTime;
	private Timestamp MeterTime;
	private BigDecimal VoltagePhaseA;
	private BigDecimal VoltagePhaseB;
	private BigDecimal VoltagePhaseC;
	private BigDecimal CurrentPhaseA;
	private BigDecimal CurrentPhaseB;
	private BigDecimal CurrentPhaseC;
	private BigDecimal PhaseAnglePhaseA;
	private BigDecimal PhaseAnglePhaseB;
	private BigDecimal PhaseAnglePhaseC;
	private BigDecimal AngleofUL2subUL1;
	private BigDecimal AngleofUL1subUL3;
	private BigDecimal Frequency;
	private BigDecimal NeutralCurrent;
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
	private BigDecimal EnergyPlusArate1;
	private BigDecimal EnergyPlusArate2;
	private BigDecimal EnergyPlusArate3;
	private BigDecimal EnergyPlusArate4;
	private BigDecimal EnergySubArate1;
	private BigDecimal EnergySubArate2;
	private BigDecimal EnergySubArate3;
	private BigDecimal EnergySubArate4;
	private BigDecimal Q1;
	private BigDecimal Q2;
	private BigDecimal Q3;
	private BigDecimal Q4;
	private BigDecimal ImportWh;
	private BigDecimal ExportWh;
	private BigDecimal EnergyPlusVA;
	private BigDecimal MaxDemandPlusA;
	private BigDecimal MaxDemandSubA;
	private BigDecimal MaxDemandPlusArate1;
	private Timestamp MaxDemandPlusArate1Time;
	private BigDecimal MaxDemandSubArate1;
	private Timestamp MaxDemandSubArate1Time;
	private BigDecimal MaxDemandPlusArate2;
	private Timestamp MaxDemandPlusArate2Time;
	private BigDecimal MaxDemandSubArate2;
	private Timestamp MaxDemandSubArate2Time;
	private BigDecimal MaxDemandPlusArate3;
	private Timestamp MaxDemandPlusArate3Time;
	private BigDecimal MaxDemandSubArate3;
	private Timestamp MaxDemandSubArate3Time;
	private BigDecimal MaxDemandPlusArate4;
	private Timestamp MaxDemandPlusArate4Time;
	private BigDecimal MaxDemandSubArate4;
	private Timestamp MaxDemandSubArate4Time;
	private String PhaseRotation;
	private BigDecimal Tu;
	private BigDecimal Ti;
	private BigDecimal TuT;
	private BigDecimal TiT;
	private BigDecimal TuM;
	private BigDecimal TiM;
	private String MeterId;
	private String DcuCode;
	private String DcuType;
	private BigDecimal Signal;
	private String ProtocolVersion;
	private String SvnVersion;
	private BigDecimal ReactiveEnergyPlusArate1;
	private BigDecimal ReactiveEnergyPlusArate2;
	private BigDecimal ReactiveEnergyPlusArate3;
	private BigDecimal ReactiveEnergySubArate1;
	private BigDecimal ReactiveEnergySubArate2;
	private BigDecimal ReactiveEnergySubArate3;
	
	public String getTopic() {
		return Topic;
	}

	public void setTopic(String topic) {
		Topic = topic;
	}
	
	public Timestamp getServerTime() {
		return ServerTime;
	}

	public void setServerTime(Timestamp serverTime) {
		ServerTime = serverTime;
	}

	public Timestamp getMeterTime() {
		return MeterTime;
	}

	public void setMeterTime(Timestamp meterTime) {
		MeterTime = meterTime;
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

	public BigDecimal getAngleofUL2subUL1() {
		return AngleofUL2subUL1;
	}

	public void setAngleofUL2subUL1(BigDecimal angleofUL2subUL1) {
		AngleofUL2subUL1 = angleofUL2subUL1;
	}

	public BigDecimal getAngleofUL1subUL3() {
		return AngleofUL1subUL3;
	}

	public void setAngleofUL1subUL3(BigDecimal angleofUL1subUL3) {
		AngleofUL1subUL3 = angleofUL1subUL3;
	}

	public BigDecimal getFrequency() {
		return Frequency;
	}

	public void setFrequency(BigDecimal frequency) {
		Frequency = frequency;
	}

	public BigDecimal getNeutralCurrent() {
		return NeutralCurrent;
	}

	public void setNeutralCurrent(BigDecimal neutralCurrent) {
		NeutralCurrent = neutralCurrent;
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

	public BigDecimal getEnergyPlusArate1() {
		return EnergyPlusArate1;
	}

	public void setEnergyPlusArate1(BigDecimal energyPlusArate1) {
		EnergyPlusArate1 = energyPlusArate1;
	}

	public BigDecimal getEnergyPlusArate2() {
		return EnergyPlusArate2;
	}

	public void setEnergyPlusArate2(BigDecimal energyPlusArate2) {
		EnergyPlusArate2 = energyPlusArate2;
	}

	public BigDecimal getEnergyPlusArate3() {
		return EnergyPlusArate3;
	}

	public void setEnergyPlusArate3(BigDecimal energyPlusArate3) {
		EnergyPlusArate3 = energyPlusArate3;
	}

	public BigDecimal getEnergyPlusArate4() {
		return EnergyPlusArate4;
	}

	public void setEnergyPlusArate4(BigDecimal energyPlusArate4) {
		EnergyPlusArate4 = energyPlusArate4;
	}

	public BigDecimal getEnergySubArate1() {
		return EnergySubArate1;
	}

	public void setEnergySubArate1(BigDecimal energySubArate1) {
		EnergySubArate1 = energySubArate1;
	}

	public BigDecimal getEnergySubArate2() {
		return EnergySubArate2;
	}

	public void setEnergySubArate2(BigDecimal energySubArate2) {
		EnergySubArate2 = energySubArate2;
	}

	public BigDecimal getEnergySubArate3() {
		return EnergySubArate3;
	}

	public void setEnergySubArate3(BigDecimal energySubArate3) {
		EnergySubArate3 = energySubArate3;
	}

	public BigDecimal getEnergySubArate4() {
		return EnergySubArate4;
	}

	public void setEnergySubArate4(BigDecimal energySubArate4) {
		EnergySubArate4 = energySubArate4;
	}

	public BigDecimal getQ1() {
		return Q1;
	}

	public void setQ1(BigDecimal q1) {
		Q1 = q1;
	}

	public BigDecimal getQ2() {
		return Q2;
	}

	public void setQ2(BigDecimal q2) {
		Q2 = q2;
	}

	public BigDecimal getQ3() {
		return Q3;
	}

	public void setQ3(BigDecimal q3) {
		Q3 = q3;
	}

	public BigDecimal getQ4() {
		return Q4;
	}

	public void setQ4(BigDecimal q4) {
		Q4 = q4;
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

	public BigDecimal getEnergyPlusVA() {
		return EnergyPlusVA;
	}

	public void setEnergyPlusVA(BigDecimal energyPlusVA) {
		EnergyPlusVA = energyPlusVA;
	}

	public BigDecimal getMaxDemandPlusA() {
		return MaxDemandPlusA;
	}

	public void setMaxDemandPlusA(BigDecimal maxDemandPlusA) {
		MaxDemandPlusA = maxDemandPlusA;
	}

	public BigDecimal getMaxDemandSubA() {
		return MaxDemandSubA;
	}

	public void setMaxDemandSubA(BigDecimal maxDemandSubA) {
		MaxDemandSubA = maxDemandSubA;
	}

	public BigDecimal getMaxDemandPlusArate1() {
		return MaxDemandPlusArate1;
	}

	public void setMaxDemandPlusArate1(BigDecimal maxDemandPlusArate1) {
		MaxDemandPlusArate1 = maxDemandPlusArate1;
	}

	public Timestamp getMaxDemandPlusArate1Time() {
		return MaxDemandPlusArate1Time;
	}

	public void setMaxDemandPlusArate1Time(Timestamp maxDemandPlusArate1Time) {
		MaxDemandPlusArate1Time = maxDemandPlusArate1Time;
	}

	public BigDecimal getMaxDemandSubArate1() {
		return MaxDemandSubArate1;
	}

	public void setMaxDemandSubArate1(BigDecimal maxDemandSubArate1) {
		MaxDemandSubArate1 = maxDemandSubArate1;
	}

	public Timestamp getMaxDemandSubArate1Time() {
		return MaxDemandSubArate1Time;
	}

	public void setMaxDemandSubArate1Time(Timestamp maxDemandSubArate1Time) {
		MaxDemandSubArate1Time = maxDemandSubArate1Time;
	}

	public BigDecimal getMaxDemandPlusArate2() {
		return MaxDemandPlusArate2;
	}

	public void setMaxDemandPlusArate2(BigDecimal maxDemandPlusArate2) {
		MaxDemandPlusArate2 = maxDemandPlusArate2;
	}

	public Timestamp getMaxDemandPlusArate2Time() {
		return MaxDemandPlusArate2Time;
	}

	public void setMaxDemandPlusArate2Time(Timestamp maxDemandPlusArate2Time) {
		MaxDemandPlusArate2Time = maxDemandPlusArate2Time;
	}

	public BigDecimal getMaxDemandSubArate2() {
		return MaxDemandSubArate2;
	}

	public void setMaxDemandSubArate2(BigDecimal maxDemandSubArate2) {
		MaxDemandSubArate2 = maxDemandSubArate2;
	}

	public Timestamp getMaxDemandSubArate2Time() {
		return MaxDemandSubArate2Time;
	}

	public void setMaxDemandSubArate2Time(Timestamp maxDemandSubArate2Time) {
		MaxDemandSubArate2Time = maxDemandSubArate2Time;
	}

	public BigDecimal getMaxDemandPlusArate3() {
		return MaxDemandPlusArate3;
	}

	public void setMaxDemandPlusArate3(BigDecimal maxDemandPlusArate3) {
		MaxDemandPlusArate3 = maxDemandPlusArate3;
	}

	public Timestamp getMaxDemandPlusArate3Time() {
		return MaxDemandPlusArate3Time;
	}

	public void setMaxDemandPlusArate3Time(Timestamp maxDemandPlusArate3Time) {
		MaxDemandPlusArate3Time = maxDemandPlusArate3Time;
	}

	public BigDecimal getMaxDemandSubArate3() {
		return MaxDemandSubArate3;
	}

	public void setMaxDemandSubArate3(BigDecimal maxDemandSubArate3) {
		MaxDemandSubArate3 = maxDemandSubArate3;
	}

	public Timestamp getMaxDemandSubArate3Time() {
		return MaxDemandSubArate3Time;
	}

	public void setMaxDemandSubArate3Time(Timestamp maxDemandSubArate3Time) {
		MaxDemandSubArate3Time = maxDemandSubArate3Time;
	}

	public BigDecimal getMaxDemandPlusArate4() {
		return MaxDemandPlusArate4;
	}

	public void setMaxDemandPlusArate4(BigDecimal maxDemandPlusArate4) {
		MaxDemandPlusArate4 = maxDemandPlusArate4;
	}

	public Timestamp getMaxDemandPlusArate4Time() {
		return MaxDemandPlusArate4Time;
	}

	public void setMaxDemandPlusArate4Time(Timestamp maxDemandPlusArate4Time) {
		MaxDemandPlusArate4Time = maxDemandPlusArate4Time;
	}

	public BigDecimal getMaxDemandSubArate4() {
		return MaxDemandSubArate4;
	}

	public void setMaxDemandSubArate4(BigDecimal maxDemandSubArate4) {
		MaxDemandSubArate4 = maxDemandSubArate4;
	}

	public Timestamp getMaxDemandSubArate4Time() {
		return MaxDemandSubArate4Time;
	}

	public void setMaxDemandSubArate4Time(Timestamp maxDemandSubArate4Time) {
		MaxDemandSubArate4Time = maxDemandSubArate4Time;
	}

	public String getPhaseRotation() {
		return PhaseRotation;
	}

	public void setPhaseRotation(String phaseRotation) {
		PhaseRotation = phaseRotation;
	}

	public BigDecimal getTu() {
		return Tu;
	}

	public void setTu(BigDecimal tu) {
		Tu = tu;
	}

	public BigDecimal getTi() {
		return Ti;
	}

	public void setTi(BigDecimal ti) {
		Ti = ti;
	}

	public BigDecimal getTuT() {
		return TuT;
	}

	public void setTuT(BigDecimal tuT) {
		TuT = tuT;
	}

	public BigDecimal getTiT() {
		return TiT;
	}

	public void setTiT(BigDecimal tiT) {
		TiT = tiT;
	}

	public BigDecimal getTuM() {
		return TuM;
	}

	public void setTuM(BigDecimal tuM) {
		TuM = tuM;
	}

	public BigDecimal getTiM() {
		return TiM;
	}

	public void setTiM(BigDecimal tiM) {
		TiM = tiM;
	}

	public String getMeterId() {
		return MeterId;
	}

	public void setMeterId(String meterId) {
		MeterId = meterId;
	}

	public String getDcuCode() {
		return DcuCode;
	}

	public void setDcuCode(String dcuCode) {
		DcuCode = dcuCode;
	}

	public String getDcuType() {
		return DcuType;
	}

	public void setDcuType(String dcuType) {
		DcuType = dcuType;
	}

	public BigDecimal getSignal() {
		return Signal;
	}

	public void setSignal(BigDecimal signal) {
		Signal = signal;
	}

	public String getProtocolVersion() {
		return ProtocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		ProtocolVersion = protocolVersion;
	}

	public String getSvnVersion() {
		return SvnVersion;
	}

	public void setSvnVersion(String svnVersion) {
		SvnVersion = svnVersion;
	}

	public BigDecimal getReactiveEnergyPlusArate1() {
		return ReactiveEnergyPlusArate1;
	}

	public void setReactiveEnergyPlusArate1(BigDecimal reactiveEnergyPlusArate1) {
		ReactiveEnergyPlusArate1 = reactiveEnergyPlusArate1;
	}

	public BigDecimal getReactiveEnergyPlusArate2() {
		return ReactiveEnergyPlusArate2;
	}

	public void setReactiveEnergyPlusArate2(BigDecimal reactiveEnergyPlusArate2) {
		ReactiveEnergyPlusArate2 = reactiveEnergyPlusArate2;
	}

	public BigDecimal getReactiveEnergyPlusArate3() {
		return ReactiveEnergyPlusArate3;
	}

	public void setReactiveEnergyPlusArate3(BigDecimal reactiveEnergyPlusArate3) {
		ReactiveEnergyPlusArate3 = reactiveEnergyPlusArate3;
	}

	public BigDecimal getReactiveEnergySubArate1() {
		return ReactiveEnergySubArate1;
	}

	public void setReactiveEnergySubArate1(BigDecimal reactiveEnergySubArate1) {
		ReactiveEnergySubArate1 = reactiveEnergySubArate1;
	}

	public BigDecimal getReactiveEnergySubArate2() {
		return ReactiveEnergySubArate2;
	}

	public void setReactiveEnergySubArate2(BigDecimal reactiveEnergySubArate2) {
		ReactiveEnergySubArate2 = reactiveEnergySubArate2;
	}

	public BigDecimal getReactiveEnergySubArate3() {
		return ReactiveEnergySubArate3;
	}

	public void setReactiveEnergySubArate3(BigDecimal reactiveEnergySubArate3) {
		ReactiveEnergySubArate3 = reactiveEnergySubArate3;
	}
}
