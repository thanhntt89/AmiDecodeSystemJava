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

public class MT3MeterAlert3PMessageEntity {
	private Timestamp ServerTime ;
    private  String MeterId ;
    private BigDecimal Tu ;
    private BigDecimal Ti ;
     //Event change Meter
    private  String MeterIdReal ;
    private  String DcuCode ;
    private Timestamp MeterTime ;
    private BigDecimal VoltA ;
    private BigDecimal VoltB ;
    private BigDecimal VoltC ;
    private BigDecimal CurrentA ;
    private BigDecimal CurrentB ;
    private BigDecimal CurrentC ;
    private BigDecimal PowerFactor ;
    private BigDecimal Frequency ;
    private int MeterAlert ;
     // Dcu Viettel
    private BigDecimal VoltOverPhaseA ;
    private BigDecimal VoltOverPhaseB ;
    private BigDecimal VoltOverPhaseC ;
    private BigDecimal FreqOver ;
    private BigDecimal FreqLower ;
    private BigDecimal CurrentOverPhaseA ;
    private BigDecimal CurrentOverPhaseB ;
    private BigDecimal CurrentOverPhaseC ;
    private BigDecimal VoltFailurePhaseA ;
    private BigDecimal VoltFailurePhaseB ;
    private BigDecimal VoltFailurePhaseC ;
    private BigDecimal VoltLowerPhaseA ;
    private BigDecimal VoltLowerPhaseB ;
    private BigDecimal VoltLowerPhaseC ;
    private BigDecimal LostPowerPhaseAll ;
    private BigDecimal LostPowerPhaseA ;
    private BigDecimal LostPowerPhaseB ;
    private BigDecimal LostPowerPhaseC ;
    private BigDecimal PhasesDeviation ;
    private BigDecimal CurrentAFail ;
    private BigDecimal CurrentBFail ;
    private BigDecimal CurrentCFail ;
    private BigDecimal PowerFactorA ;
    private BigDecimal PowerFactorB ;
    private BigDecimal PowerFactorC ;
    private BigDecimal TimeDeviation ;
    
    public Timestamp getServerTime() {
		return ServerTime;
	}
	public void setServerTime(Timestamp serverTime) {
		ServerTime = serverTime;
	}
	public String getMeterId() {
		return MeterId;
	}
	public void setMeterId(String meterId) {
		MeterId = meterId;
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
	public String getMeterIdReal() {
		return MeterIdReal;
	}
	public void setMeterIdReal(String meterIdReal) {
		MeterIdReal = meterIdReal;
	}
	public String getDcuCode() {
		return DcuCode;
	}
	public void setDcuCode(String dcuCode) {
		DcuCode = dcuCode;
	}
	public Timestamp getMeterTime() {
		return MeterTime;
	}
	public void setMeterTime(Timestamp meterTime) {
		MeterTime = meterTime;
	}
	public BigDecimal getVoltA() {
		return VoltA;
	}
	public void setVoltA(BigDecimal voltA) {
		VoltA = voltA;
	}
	public BigDecimal getVoltB() {
		return VoltB;
	}
	public void setVoltB(BigDecimal voltB) {
		VoltB = voltB;
	}
	public BigDecimal getVoltC() {
		return VoltC;
	}
	public void setVoltC(BigDecimal voltC) {
		VoltC = voltC;
	}
	public BigDecimal getCurrentA() {
		return CurrentA;
	}
	public void setCurrentA(BigDecimal currentA) {
		CurrentA = currentA;
	}
	public BigDecimal getCurrentB() {
		return CurrentB;
	}
	public void setCurrentB(BigDecimal currentB) {
		CurrentB = currentB;
	}
	public BigDecimal getCurrentC() {
		return CurrentC;
	}
	public void setCurrentC(BigDecimal currentC) {
		CurrentC = currentC;
	}
	public BigDecimal getPowerFactor() {
		return PowerFactor;
	}
	public void setPowerFactor(BigDecimal powerFactor) {
		PowerFactor = powerFactor;
	}
	public BigDecimal getFrequency() {
		return Frequency;
	}
	public void setFrequency(BigDecimal frequency) {
		Frequency = frequency;
	}
	public int getMeterAlert() {
		return MeterAlert;
	}
	public void setMeterAlert(int meterAlert) {
		MeterAlert = meterAlert;
	}
	public BigDecimal getVoltOverPhaseA() {
		return VoltOverPhaseA;
	}
	public void setVoltOverPhaseA(BigDecimal voltOverPhaseA) {
		VoltOverPhaseA = voltOverPhaseA;
	}
	public BigDecimal getVoltOverPhaseB() {
		return VoltOverPhaseB;
	}
	public void setVoltOverPhaseB(BigDecimal voltOverPhaseB) {
		VoltOverPhaseB = voltOverPhaseB;
	}
	public BigDecimal getVoltOverPhaseC() {
		return VoltOverPhaseC;
	}
	public void setVoltOverPhaseC(BigDecimal voltOverPhaseC) {
		VoltOverPhaseC = voltOverPhaseC;
	}
	public BigDecimal getFreqOver() {
		return FreqOver;
	}
	public void setFreqOver(BigDecimal freqOver) {
		FreqOver = freqOver;
	}
	public BigDecimal getFreqLower() {
		return FreqLower;
	}
	public void setFreqLower(BigDecimal freqLower) {
		FreqLower = freqLower;
	}
	public BigDecimal getCurrentOverPhaseA() {
		return CurrentOverPhaseA;
	}
	public void setCurrentOverPhaseA(BigDecimal currentOverPhaseA) {
		CurrentOverPhaseA = currentOverPhaseA;
	}
	public BigDecimal getCurrentOverPhaseB() {
		return CurrentOverPhaseB;
	}
	public void setCurrentOverPhaseB(BigDecimal currentOverPhaseB) {
		CurrentOverPhaseB = currentOverPhaseB;
	}
	public BigDecimal getCurrentOverPhaseC() {
		return CurrentOverPhaseC;
	}
	public void setCurrentOverPhaseC(BigDecimal currentOverPhaseC) {
		CurrentOverPhaseC = currentOverPhaseC;
	}
	public BigDecimal getVoltFailurePhaseA() {
		return VoltFailurePhaseA;
	}
	public void setVoltFailurePhaseA(BigDecimal voltFailurePhaseA) {
		VoltFailurePhaseA = voltFailurePhaseA;
	}
	public BigDecimal getVoltFailurePhaseB() {
		return VoltFailurePhaseB;
	}
	public void setVoltFailurePhaseB(BigDecimal voltFailurePhaseB) {
		VoltFailurePhaseB = voltFailurePhaseB;
	}
	public BigDecimal getVoltFailurePhaseC() {
		return VoltFailurePhaseC;
	}
	public void setVoltFailurePhaseC(BigDecimal voltFailurePhaseC) {
		VoltFailurePhaseC = voltFailurePhaseC;
	}
	public BigDecimal getVoltLowerPhaseA() {
		return VoltLowerPhaseA;
	}
	public void setVoltLowerPhaseA(BigDecimal voltLowerPhaseA) {
		VoltLowerPhaseA = voltLowerPhaseA;
	}
	public BigDecimal getVoltLowerPhaseB() {
		return VoltLowerPhaseB;
	}
	public void setVoltLowerPhaseB(BigDecimal voltLowerPhaseB) {
		VoltLowerPhaseB = voltLowerPhaseB;
	}
	public BigDecimal getVoltLowerPhaseC() {
		return VoltLowerPhaseC;
	}
	public void setVoltLowerPhaseC(BigDecimal voltLowerPhaseC) {
		VoltLowerPhaseC = voltLowerPhaseC;
	}
	public BigDecimal getLostPowerPhaseAll() {
		return LostPowerPhaseAll;
	}
	public void setLostPowerPhaseAll(BigDecimal lostPowerPhaseAll) {
		LostPowerPhaseAll = lostPowerPhaseAll;
	}
	public BigDecimal getLostPowerPhaseA() {
		return LostPowerPhaseA;
	}
	public void setLostPowerPhaseA(BigDecimal lostPowerPhaseA) {
		LostPowerPhaseA = lostPowerPhaseA;
	}
	public BigDecimal getLostPowerPhaseB() {
		return LostPowerPhaseB;
	}
	public void setLostPowerPhaseB(BigDecimal lostPowerPhaseB) {
		LostPowerPhaseB = lostPowerPhaseB;
	}
	public BigDecimal getLostPowerPhaseC() {
		return LostPowerPhaseC;
	}
	public void setLostPowerPhaseC(BigDecimal lostPowerPhaseC) {
		LostPowerPhaseC = lostPowerPhaseC;
	}
	public BigDecimal getPhasesDeviation() {
		return PhasesDeviation;
	}
	public void setPhasesDeviation(BigDecimal phasesDeviation) {
		PhasesDeviation = phasesDeviation;
	}
	public BigDecimal getCurrentAFail() {
		return CurrentAFail;
	}
	public void setCurrentAFail(BigDecimal currentAFail) {
		CurrentAFail = currentAFail;
	}
	public BigDecimal getCurrentBFail() {
		return CurrentBFail;
	}
	public void setCurrentBFail(BigDecimal currentBFail) {
		CurrentBFail = currentBFail;
	}
	public BigDecimal getCurrentCFail() {
		return CurrentCFail;
	}
	public void setCurrentCFail(BigDecimal currentCFail) {
		CurrentCFail = currentCFail;
	}
	public BigDecimal getPowerFactorA() {
		return PowerFactorA;
	}
	public void setPowerFactorA(BigDecimal powerFactorA) {
		PowerFactorA = powerFactorA;
	}
	public BigDecimal getPowerFactorB() {
		return PowerFactorB;
	}
	public void setPowerFactorB(BigDecimal powerFactorB) {
		PowerFactorB = powerFactorB;
	}
	public BigDecimal getPowerFactorC() {
		return PowerFactorC;
	}
	public void setPowerFactorC(BigDecimal powerFactorC) {
		PowerFactorC = powerFactorC;
	}
	public BigDecimal getTimeDeviation() {
		return TimeDeviation;
	}
	public void setTimeDeviation(BigDecimal timeDeviation) {
		TimeDeviation = timeDeviation;
	}
	

}
