/*
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

public class MT3MeasurementPointAlertConfigEntity implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		// TODO Auto-generated constructor stub
		private BigDecimal Volt;
        public BigDecimal getVolt() {
			return Volt;
		}
		public void setVolt(BigDecimal volt) {
			Volt = volt;
		}
		public BigDecimal getVotPhaseUnder() {
			return VotPhaseUnder;
		}
		public void setVotPhaseUnder(BigDecimal votPhaseUnder) {
			VotPhaseUnder = votPhaseUnder;
		}
		public BigDecimal getVotPhaseOver() {
			return VotPhaseOver;
		}
		public void setVotPhaseOver(BigDecimal votPhaseOver) {
			VotPhaseOver = votPhaseOver;
		}
		public BigDecimal getVotPhaseAUnder() {
			return VotPhaseAUnder;
		}
		public void setVotPhaseAUnder(BigDecimal votPhaseAUnder) {
			VotPhaseAUnder = votPhaseAUnder;
		}
		public BigDecimal getVotPhaseAOver() {
			return VotPhaseAOver;
		}
		public void setVotPhaseAOver(BigDecimal votPhaseAOver) {
			VotPhaseAOver = votPhaseAOver;
		}
		public BigDecimal getVotPhaseBUnder() {
			return VotPhaseBUnder;
		}
		public void setVotPhaseBUnder(BigDecimal votPhaseBUnder) {
			VotPhaseBUnder = votPhaseBUnder;
		}
		public BigDecimal getVotPhaseBOver() {
			return VotPhaseBOver;
		}
		public void setVotPhaseBOver(BigDecimal votPhaseBOver) {
			VotPhaseBOver = votPhaseBOver;
		}
		public BigDecimal getVotPhaseCUnder() {
			return VotPhaseCUnder;
		}
		public void setVotPhaseCUnder(BigDecimal votPhaseCUnder) {
			VotPhaseCUnder = votPhaseCUnder;
		}
		public BigDecimal getVotPhaseCOver() {
			return VotPhaseCOver;
		}
		public void setVotPhaseCOver(BigDecimal votPhaseCOver) {
			VotPhaseCOver = votPhaseCOver;
		}
		public BigDecimal getFreq() {
			return Freq;
		}
		public void setFreq(BigDecimal freq) {
			Freq = freq;
		}
		public BigDecimal getFreqUnder() {
			return FreqUnder;
		}
		public void setFreqUnder(BigDecimal freqUnder) {
			FreqUnder = freqUnder;
		}
		public BigDecimal getFreqOver() {
			return FreqOver;
		}
		public void setFreqOver(BigDecimal freqOver) {
			FreqOver = freqOver;
		}
		public BigDecimal getPowerFactorUnder() {
			return PowerFactorUnder;
		}
		public void setPowerFactorUnder(BigDecimal powerFactorUnder) {
			PowerFactorUnder = powerFactorUnder;
		}
		public BigDecimal getCurrent() {
			return Current;
		}
		public void setCurrent(BigDecimal current) {
			Current = current;
		}
		public BigDecimal getCur1PhaseOverRated() {
			return Cur1PhaseOverRated;
		}
		public void setCur1PhaseOverRated(BigDecimal cur1PhaseOverRated) {
			Cur1PhaseOverRated = cur1PhaseOverRated;
		}
		public BigDecimal getCur2PhaseOverRated() {
			return Cur2PhaseOverRated;
		}
		public void setCur2PhaseOverRated(BigDecimal cur2PhaseOverRated) {
			Cur2PhaseOverRated = cur2PhaseOverRated;
		}
		public BigDecimal getCur1PhaseUnderAverage() {
			return Cur1PhaseUnderAverage;
		}
		public void setCur1PhaseUnderAverage(BigDecimal cur1PhaseUnderAverage) {
			Cur1PhaseUnderAverage = cur1PhaseUnderAverage;
		}
		private BigDecimal VotPhaseUnder;
        private BigDecimal VotPhaseOver;
        private BigDecimal VotPhaseAUnder;
        private BigDecimal VotPhaseAOver;
        private BigDecimal VotPhaseBUnder;
        private BigDecimal VotPhaseBOver;
        private BigDecimal VotPhaseCUnder;
        private BigDecimal VotPhaseCOver;
        private BigDecimal Freq;
        private BigDecimal FreqUnder;
        private BigDecimal FreqOver;
        private BigDecimal PowerFactorUnder;
        private BigDecimal Current;
        private BigDecimal Cur1PhaseOverRated;
        private BigDecimal Cur2PhaseOverRated;
        private BigDecimal Cur1PhaseUnderAverage;

}
