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

public class MT3HistoricalEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String Topic;
	private Timestamp ServerTime;
	private Timestamp MeterTime;
	private Timestamp HistoricalTime;
	private Timestamp BeginHistoricalTime;
	private Timestamp EndHistoricalTime;
	private BigDecimal EnergyPlusARate1;
	private BigDecimal EnergyPlusARate2;
	private BigDecimal EnergyPlusARate3;
	private BigDecimal EnergyPlusARate4;
	private BigDecimal EnergySubARate1;
	private BigDecimal EnergySubARate2;
	private BigDecimal EnergySubARate3;
	private BigDecimal EnergySubARate4;
	private BigDecimal ImportWh;
	private BigDecimal ExportWh;
	private BigDecimal EnergyPlusVA;
	private BigDecimal Q1;
	private BigDecimal Q2;
	private BigDecimal Q3;
	private BigDecimal Q4;
	private BigDecimal ActivePowerQ1Q4Q2Q3;
	private BigDecimal ReactivePowerQ1Q2Q3Q4;
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
	private BigDecimal TotalReactiveEnergyPositve;
	private BigDecimal TotalReactiveEnergyNegative;
	private BigDecimal ReactiveEnergyPlusArate1;
	private BigDecimal ReactiveEnergyPlusArate2;
	private BigDecimal ReactiveEnergyPlusArate3;
	private BigDecimal ReactiveEnergySubArate1;
	private BigDecimal ReactiveEnergySubArate2;
	private BigDecimal ReactiveEnergySubArate3;
	private String MeterId;
	private String DcuCode;
	
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

	public Timestamp getHistoricalTime() {
		return HistoricalTime;
	}

	public void setHistoricalTime(Timestamp historicalTime) {
		HistoricalTime = historicalTime;
	}

	public Timestamp getBeginHistoricalTime() {
		return BeginHistoricalTime;
	}

	public void setBeginHistoricalTime(Timestamp beginHistoricalTime) {
		BeginHistoricalTime = beginHistoricalTime;
	}

	public Timestamp getEndHistoricalTime() {
		return EndHistoricalTime;
	}

	public void setEndHistoricalTime(Timestamp endHistoricalTime) {
		EndHistoricalTime = endHistoricalTime;
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

	public BigDecimal getEnergyPlusARate4() {
		return EnergyPlusARate4;
	}

	public void setEnergyPlusARate4(BigDecimal energyPlusARate4) {
		EnergyPlusARate4 = energyPlusARate4;
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

	public BigDecimal getEnergySubARate4() {
		return EnergySubARate4;
	}

	public void setEnergySubARate4(BigDecimal energySubARate4) {
		EnergySubARate4 = energySubARate4;
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

	public BigDecimal getActivePowerQ1Q4Q2Q3() {
		return ActivePowerQ1Q4Q2Q3;
	}

	public void setActivePowerQ1Q4Q2Q3(BigDecimal activePowerQ1Q4Q2Q3) {
		ActivePowerQ1Q4Q2Q3 = activePowerQ1Q4Q2Q3;
	}

	public BigDecimal getReactivePowerQ1Q2Q3Q4() {
		return ReactivePowerQ1Q2Q3Q4;
	}

	public void setReactivePowerQ1Q2Q3Q4(BigDecimal reactivePowerQ1Q2Q3Q4) {
		ReactivePowerQ1Q2Q3Q4 = reactivePowerQ1Q2Q3Q4;
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

	public BigDecimal getTotalReactiveEnergyPositve() {
		return TotalReactiveEnergyPositve;
	}

	public void setTotalReactiveEnergyPositve(BigDecimal totalReactiveEnergyPositve) {
		TotalReactiveEnergyPositve = totalReactiveEnergyPositve;
	}

	public BigDecimal getTotalReactiveEnergyNegative() {
		return TotalReactiveEnergyNegative;
	}

	public void setTotalReactiveEnergyNegative(BigDecimal totalReactiveEnergyNegative) {
		TotalReactiveEnergyNegative = totalReactiveEnergyNegative;
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

}
