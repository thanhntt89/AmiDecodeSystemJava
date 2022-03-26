/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: MT1MEPDVEntity.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.model.meter.onephase.entity;

import java.math.BigDecimal;
import java.util.Date;

public class MT1MePDVEntity {
	
	public Date getServerTime() {
		return ServerTime;
	}
	public void setServerTime(Date serverTime) {
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
	public Date getMeterTime() {
		return MeterTime;
	}
	public void setMeterTime(Date meterTime) {
		MeterTime = meterTime;
	}
	public BigDecimal getVolt() {
		return Volt;
	}
	public void setVolt(BigDecimal volt) {
		Volt = volt;
	}
	public BigDecimal getCurrent() {
		return Current;
	}
	public void setCurrent(BigDecimal current) {
		Current = current;
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
	public BigDecimal getVoltOverPhase() {
		return VoltOverPhase;
	}
	public void setVoltOverPhase(BigDecimal voltOverPhase) {
		VoltOverPhase = voltOverPhase;
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
	public BigDecimal getCurrentOverPhase() {
		return CurrentOverPhase;
	}
	public void setCurrentOverPhase(BigDecimal currentOverPhase) {
		CurrentOverPhase = currentOverPhase;
	}
	public BigDecimal getVoltFailurePhase() {
		return VoltFailurePhase;
	}
	public void setVoltFailurePhase(BigDecimal voltFailurePhase) {
		VoltFailurePhase = voltFailurePhase;
	}
	public BigDecimal getVoltLowerPhase() {
		return VoltLowerPhase;
	}
	public void setVoltLowerPhase(BigDecimal voltLowerPhase) {
		VoltLowerPhase = voltLowerPhase;
	}
	public BigDecimal getPhasesDeviation() {
		return PhasesDeviation;
	}
	public void setPhasesDeviation(BigDecimal phasesDeviation) {
		PhasesDeviation = phasesDeviation;
	}
	public BigDecimal getCurrentFail() {
		return CurrentFail;
	}
	public void setCurrentFail(BigDecimal currentFail) {
		CurrentFail = currentFail;
	}
	
	private Date ServerTime;
	private Date MeterTime;
	private String MeterId;
	private BigDecimal Tu;
	private BigDecimal Ti;
	// Event change Meter
	private String MeterIdReal;
	private String DcuCode;	
	private BigDecimal Volt;
	private BigDecimal Current;
	private BigDecimal PowerFactor;
	private BigDecimal Frequency;
	private int MeterAlert;
	// Dcu Viettel
	private BigDecimal VoltOverPhase;
	private BigDecimal FreqOver;
	private BigDecimal FreqLower;
	private BigDecimal CurrentOverPhase;
	private BigDecimal VoltFailurePhase;
	private BigDecimal VoltLowerPhase;
	private BigDecimal PhasesDeviation;
	private BigDecimal CurrentFail;
}
