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

public class MT3EventEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getDcuCode() {
		return DcuCode;
	}

	public void setDcuCode(String dcuCode) {
		DcuCode = dcuCode;
	}

	public BigDecimal getEventProgrammingCount() {
		return EventProgrammingCount;
	}

	public void setEventProgrammingCount(BigDecimal eventProgrammingCount) {
		EventProgrammingCount = eventProgrammingCount;
	}

	public Timestamp getEventProgrammingFirst() {
		return EventProgrammingFirst;
	}

	public void setEventProgrammingFirst(Timestamp eventProgrammingFirst) {
		EventProgrammingFirst = eventProgrammingFirst;
	}

	public Timestamp getEventProgrammingSecond() {
		return EventProgrammingSecond;
	}

	public void setEventProgrammingSecond(Timestamp eventProgrammingSecond) {
		EventProgrammingSecond = eventProgrammingSecond;
	}

	public Timestamp getEventProgrammingThird() {
		return EventProgrammingThird;
	}

	public void setEventProgrammingThird(Timestamp eventProgrammingThird) {
		EventProgrammingThird = eventProgrammingThird;
	}

	public Timestamp getEventProgrammingFourth() {
		return EventProgrammingFourth;
	}

	public void setEventProgrammingFourth(Timestamp eventProgrammingFourth) {
		EventProgrammingFourth = eventProgrammingFourth;
	}

	public Timestamp getEventProgrammingFifth() {
		return EventProgrammingFifth;
	}

	public void setEventProgrammingFifth(Timestamp eventProgrammingFifth) {
		EventProgrammingFifth = eventProgrammingFifth;
	}

	public BigDecimal getEventPasswordChangeCount() {
		return EventPasswordChangeCount;
	}

	public void setEventPasswordChangeCount(BigDecimal eventPasswordChangeCount) {
		EventPasswordChangeCount = eventPasswordChangeCount;
	}

	public Timestamp getEventPasswordChangeFirst() {
		return EventPasswordChangeFirst;
	}

	public void setEventPasswordChangeFirst(Timestamp eventPasswordChangeFirst) {
		EventPasswordChangeFirst = eventPasswordChangeFirst;
	}

	public Timestamp getEventPasswordChangeSecond() {
		return EventPasswordChangeSecond;
	}

	public void setEventPasswordChangeSecond(Timestamp eventPasswordChangeSecond) {
		EventPasswordChangeSecond = eventPasswordChangeSecond;
	}

	public Timestamp getEventPasswordChangeThird() {
		return EventPasswordChangeThird;
	}

	public void setEventPasswordChangeThird(Timestamp eventPasswordChangeThird) {
		EventPasswordChangeThird = eventPasswordChangeThird;
	}

	public Timestamp getEventPasswordChangeFourth() {
		return EventPasswordChangeFourth;
	}

	public void setEventPasswordChangeFourth(Timestamp eventPasswordChangeFourth) {
		EventPasswordChangeFourth = eventPasswordChangeFourth;
	}

	public Timestamp getEventPasswordChangeFifth() {
		return EventPasswordChangeFifth;
	}

	public void setEventPasswordChangeFifth(Timestamp eventPasswordChangeFifth) {
		EventPasswordChangeFifth = eventPasswordChangeFifth;
	}

	public BigDecimal getEventVoltageImbalanceCount() {
		return EventVoltageImbalanceCount;
	}

	public void setEventVoltageImbalanceCount(BigDecimal eventVoltageImbalanceCount) {
		EventVoltageImbalanceCount = eventVoltageImbalanceCount;
	}

	public Timestamp getEventVoltageImbalanceFirst() {
		return EventVoltageImbalanceFirst;
	}

	public void setEventVoltageImbalanceFirst(Timestamp eventVoltageImbalanceFirst) {
		EventVoltageImbalanceFirst = eventVoltageImbalanceFirst;
	}

	public Timestamp getEventVoltageImbalanceSecond() {
		return EventVoltageImbalanceSecond;
	}

	public void setEventVoltageImbalanceSecond(Timestamp eventVoltageImbalanceSecond) {
		EventVoltageImbalanceSecond = eventVoltageImbalanceSecond;
	}

	public Timestamp getEventVoltageImbalanceThird() {
		return EventVoltageImbalanceThird;
	}

	public void setEventVoltageImbalanceThird(Timestamp eventVoltageImbalanceThird) {
		EventVoltageImbalanceThird = eventVoltageImbalanceThird;
	}

	public Timestamp getEventVoltageImbalanceFourth() {
		return EventVoltageImbalanceFourth;
	}

	public void setEventVoltageImbalanceFourth(Timestamp eventVoltageImbalanceFourth) {
		EventVoltageImbalanceFourth = eventVoltageImbalanceFourth;
	}

	public Timestamp getEventVoltageImbalanceFifth() {
		return EventVoltageImbalanceFifth;
	}

	public void setEventVoltageImbalanceFifth(Timestamp eventVoltageImbalanceFifth) {
		EventVoltageImbalanceFifth = eventVoltageImbalanceFifth;
	}

	public BigDecimal getEventPowerFailureCount() {
		return EventPowerFailureCount;
	}

	public void setEventPowerFailureCount(BigDecimal eventPowerFailureCount) {
		EventPowerFailureCount = eventPowerFailureCount;
	}

	public Timestamp getEventPowerFailureFirst() {
		return EventPowerFailureFirst;
	}

	public void setEventPowerFailureFirst(Timestamp eventPowerFailureFirst) {
		EventPowerFailureFirst = eventPowerFailureFirst;
	}

	public Timestamp getEventPowerFailureSecond() {
		return EventPowerFailureSecond;
	}

	public void setEventPowerFailureSecond(Timestamp eventPowerFailureSecond) {
		EventPowerFailureSecond = eventPowerFailureSecond;
	}

	public Timestamp getEventPowerFailureThird() {
		return EventPowerFailureThird;
	}

	public void setEventPowerFailureThird(Timestamp eventPowerFailureThird) {
		EventPowerFailureThird = eventPowerFailureThird;
	}

	public Timestamp getEventPowerFailureFourth() {
		return EventPowerFailureFourth;
	}

	public void setEventPowerFailureFourth(Timestamp eventPowerFailureFourth) {
		EventPowerFailureFourth = eventPowerFailureFourth;
	}

	public Timestamp getEventPowerFailureFifth() {
		return EventPowerFailureFifth;
	}

	public void setEventPowerFailureFifth(Timestamp eventPowerFailureFifth) {
		EventPowerFailureFifth = eventPowerFailureFifth;
	}

	public Timestamp getEventPowerRecoverFirst() {
		return EventPowerRecoverFirst;
	}

	public void setEventPowerRecoverFirst(Timestamp eventPowerRecoverFirst) {
		EventPowerRecoverFirst = eventPowerRecoverFirst;
	}

	public Timestamp getEventPowerRecoverSecond() {
		return EventPowerRecoverSecond;
	}

	public void setEventPowerRecoverSecond(Timestamp eventPowerRecoverSecond) {
		EventPowerRecoverSecond = eventPowerRecoverSecond;
	}

	public Timestamp getEventPowerRecoverThird() {
		return EventPowerRecoverThird;
	}

	public void setEventPowerRecoverThird(Timestamp eventPowerRecoverThird) {
		EventPowerRecoverThird = eventPowerRecoverThird;
	}

	public Timestamp getEventPowerRecoverFourth() {
		return EventPowerRecoverFourth;
	}

	public void setEventPowerRecoverFourth(Timestamp eventPowerRecoverFourth) {
		EventPowerRecoverFourth = eventPowerRecoverFourth;
	}

	public Timestamp getEventPowerRecoverFifth() {
		return EventPowerRecoverFifth;
	}

	public void setEventPowerRecoverFifth(Timestamp eventPowerRecoverFifth) {
		EventPowerRecoverFifth = eventPowerRecoverFifth;
	}

	public BigDecimal getEventTimeDateChangeCount() {
		return EventTimeDateChangeCount;
	}

	public void setEventTimeDateChangeCount(BigDecimal eventTimeDateChangeCount) {
		EventTimeDateChangeCount = eventTimeDateChangeCount;
	}

	public Timestamp getEventTimeDateChangeFirstStart() {
		return EventTimeDateChangeFirstStart;
	}

	public void setEventTimeDateChangeFirstStart(Timestamp eventTimeDateChangeFirstStart) {
		EventTimeDateChangeFirstStart = eventTimeDateChangeFirstStart;
	}

	public Timestamp getEventTimeDateChangeSecondStart() {
		return EventTimeDateChangeSecondStart;
	}

	public void setEventTimeDateChangeSecondStart(Timestamp eventTimeDateChangeSecondStart) {
		EventTimeDateChangeSecondStart = eventTimeDateChangeSecondStart;
	}

	public Timestamp getEventTimeDateChangeThirdStart() {
		return EventTimeDateChangeThirdStart;
	}

	public void setEventTimeDateChangeThirdStart(Timestamp eventTimeDateChangeThirdStart) {
		EventTimeDateChangeThirdStart = eventTimeDateChangeThirdStart;
	}

	public Timestamp getEventTimeDateChangeFourthStart() {
		return EventTimeDateChangeFourthStart;
	}

	public void setEventTimeDateChangeFourthStart(Timestamp eventTimeDateChangeFourthStart) {
		EventTimeDateChangeFourthStart = eventTimeDateChangeFourthStart;
	}

	public Timestamp getEventTimeDateChangeFifthStart() {
		return EventTimeDateChangeFifthStart;
	}

	public void setEventTimeDateChangeFifthStart(Timestamp eventTimeDateChangeFifthStart) {
		EventTimeDateChangeFifthStart = eventTimeDateChangeFifthStart;
	}

	public Timestamp getEventTimeDateChangeFirstEnd() {
		return EventTimeDateChangeFirstEnd;
	}

	public void setEventTimeDateChangeFirstEnd(Timestamp eventTimeDateChangeFirstEnd) {
		EventTimeDateChangeFirstEnd = eventTimeDateChangeFirstEnd;
	}

	public Timestamp getEventTimeDateChangeSecondEnd() {
		return EventTimeDateChangeSecondEnd;
	}

	public void setEventTimeDateChangeSecondEnd(Timestamp eventTimeDateChangeSecondEnd) {
		EventTimeDateChangeSecondEnd = eventTimeDateChangeSecondEnd;
	}

	public Timestamp getEventTimeDateChangeThirdEnd() {
		return EventTimeDateChangeThirdEnd;
	}

	public void setEventTimeDateChangeThirdEnd(Timestamp eventTimeDateChangeThirdEnd) {
		EventTimeDateChangeThirdEnd = eventTimeDateChangeThirdEnd;
	}

	public Timestamp getEventTimeDateChangeFourthEnd() {
		return EventTimeDateChangeFourthEnd;
	}

	public void setEventTimeDateChangeFourthEnd(Timestamp eventTimeDateChangeFourthEnd) {
		EventTimeDateChangeFourthEnd = eventTimeDateChangeFourthEnd;
	}

	public Timestamp getEventTimeDateChangeFifthEnd() {
		return EventTimeDateChangeFifthEnd;
	}

	public void setEventTimeDateChangeFifthEnd(Timestamp eventTimeDateChangeFifthEnd) {
		EventTimeDateChangeFifthEnd = eventTimeDateChangeFifthEnd;
	}

	public BigDecimal getEventFailurePhaseACount() {
		return EventFailurePhaseACount;
	}

	public void setEventFailurePhaseACount(BigDecimal eventFailurePhaseACount) {
		EventFailurePhaseACount = eventFailurePhaseACount;
	}

	public Timestamp getEventFailurePhaseAFirst() {
		return EventFailurePhaseAFirst;
	}

	public void setEventFailurePhaseAFirst(Timestamp eventFailurePhaseAFirst) {
		EventFailurePhaseAFirst = eventFailurePhaseAFirst;
	}

	public Timestamp getEventFailurePhaseASecond() {
		return EventFailurePhaseASecond;
	}

	public void setEventFailurePhaseASecond(Timestamp eventFailurePhaseASecond) {
		EventFailurePhaseASecond = eventFailurePhaseASecond;
	}

	public Timestamp getEventFailurePhaseAThird() {
		return EventFailurePhaseAThird;
	}

	public void setEventFailurePhaseAThird(Timestamp eventFailurePhaseAThird) {
		EventFailurePhaseAThird = eventFailurePhaseAThird;
	}

	public Timestamp getEventFailurePhaseAFourth() {
		return EventFailurePhaseAFourth;
	}

	public void setEventFailurePhaseAFourth(Timestamp eventFailurePhaseAFourth) {
		EventFailurePhaseAFourth = eventFailurePhaseAFourth;
	}

	public Timestamp getEventFailurePhaseAFifth() {
		return EventFailurePhaseAFifth;
	}

	public void setEventFailurePhaseAFifth(Timestamp eventFailurePhaseAFifth) {
		EventFailurePhaseAFifth = eventFailurePhaseAFifth;
	}

	public Timestamp getEventPowerOnPhaseAFirst() {
		return EventPowerOnPhaseAFirst;
	}

	public void setEventPowerOnPhaseAFirst(Timestamp eventPowerOnPhaseAFirst) {
		EventPowerOnPhaseAFirst = eventPowerOnPhaseAFirst;
	}

	public Timestamp getEventPowerOnPhaseASecond() {
		return EventPowerOnPhaseASecond;
	}

	public void setEventPowerOnPhaseASecond(Timestamp eventPowerOnPhaseASecond) {
		EventPowerOnPhaseASecond = eventPowerOnPhaseASecond;
	}

	public Timestamp getEventPowerOnPhaseAThird() {
		return EventPowerOnPhaseAThird;
	}

	public void setEventPowerOnPhaseAThird(Timestamp eventPowerOnPhaseAThird) {
		EventPowerOnPhaseAThird = eventPowerOnPhaseAThird;
	}

	public Timestamp getEventPowerOnPhaseAFourth() {
		return EventPowerOnPhaseAFourth;
	}

	public void setEventPowerOnPhaseAFourth(Timestamp eventPowerOnPhaseAFourth) {
		EventPowerOnPhaseAFourth = eventPowerOnPhaseAFourth;
	}

	public Timestamp getEventPowerOnPhaseAFifth() {
		return EventPowerOnPhaseAFifth;
	}

	public void setEventPowerOnPhaseAFifth(Timestamp eventPowerOnPhaseAFifth) {
		EventPowerOnPhaseAFifth = eventPowerOnPhaseAFifth;
	}

	public BigDecimal getEventFailurePhaseBCount() {
		return EventFailurePhaseBCount;
	}

	public void setEventFailurePhaseBCount(BigDecimal eventFailurePhaseBCount) {
		EventFailurePhaseBCount = eventFailurePhaseBCount;
	}

	public Timestamp getEventFailurePhaseBFirst() {
		return EventFailurePhaseBFirst;
	}

	public void setEventFailurePhaseBFirst(Timestamp eventFailurePhaseBFirst) {
		EventFailurePhaseBFirst = eventFailurePhaseBFirst;
	}

	public Timestamp getEventFailurePhaseBSecond() {
		return EventFailurePhaseBSecond;
	}

	public void setEventFailurePhaseBSecond(Timestamp eventFailurePhaseBSecond) {
		EventFailurePhaseBSecond = eventFailurePhaseBSecond;
	}

	public Timestamp getEventFailurePhaseBThird() {
		return EventFailurePhaseBThird;
	}

	public void setEventFailurePhaseBThird(Timestamp eventFailurePhaseBThird) {
		EventFailurePhaseBThird = eventFailurePhaseBThird;
	}

	public Timestamp getEventFailurePhaseBFourth() {
		return EventFailurePhaseBFourth;
	}

	public void setEventFailurePhaseBFourth(Timestamp eventFailurePhaseBFourth) {
		EventFailurePhaseBFourth = eventFailurePhaseBFourth;
	}

	public Timestamp getEventFailurePhaseBFifth() {
		return EventFailurePhaseBFifth;
	}

	public void setEventFailurePhaseBFifth(Timestamp eventFailurePhaseBFifth) {
		EventFailurePhaseBFifth = eventFailurePhaseBFifth;
	}

	public Timestamp getEventPowerOnPhaseBFirst() {
		return EventPowerOnPhaseBFirst;
	}

	public void setEventPowerOnPhaseBFirst(Timestamp eventPowerOnPhaseBFirst) {
		EventPowerOnPhaseBFirst = eventPowerOnPhaseBFirst;
	}

	public Timestamp getEventPowerOnPhaseBSecond() {
		return EventPowerOnPhaseBSecond;
	}

	public void setEventPowerOnPhaseBSecond(Timestamp eventPowerOnPhaseBSecond) {
		EventPowerOnPhaseBSecond = eventPowerOnPhaseBSecond;
	}

	public Timestamp getEventPowerOnPhaseBThird() {
		return EventPowerOnPhaseBThird;
	}

	public void setEventPowerOnPhaseBThird(Timestamp eventPowerOnPhaseBThird) {
		EventPowerOnPhaseBThird = eventPowerOnPhaseBThird;
	}

	public Timestamp getEventPowerOnPhaseBFourth() {
		return EventPowerOnPhaseBFourth;
	}

	public void setEventPowerOnPhaseBFourth(Timestamp eventPowerOnPhaseBFourth) {
		EventPowerOnPhaseBFourth = eventPowerOnPhaseBFourth;
	}

	public Timestamp getEventPowerOnPhaseBFifth() {
		return EventPowerOnPhaseBFifth;
	}

	public void setEventPowerOnPhaseBFifth(Timestamp eventPowerOnPhaseBFifth) {
		EventPowerOnPhaseBFifth = eventPowerOnPhaseBFifth;
	}

	public BigDecimal getEventFailurePhaseCCount() {
		return EventFailurePhaseCCount;
	}

	public void setEventFailurePhaseCCount(BigDecimal eventFailurePhaseCCount) {
		EventFailurePhaseCCount = eventFailurePhaseCCount;
	}

	public Timestamp getEventFailurePhaseCFirst() {
		return EventFailurePhaseCFirst;
	}

	public void setEventFailurePhaseCFirst(Timestamp eventFailurePhaseCFirst) {
		EventFailurePhaseCFirst = eventFailurePhaseCFirst;
	}

	public Timestamp getEventFailurePhaseCSecond() {
		return EventFailurePhaseCSecond;
	}

	public void setEventFailurePhaseCSecond(Timestamp eventFailurePhaseCSecond) {
		EventFailurePhaseCSecond = eventFailurePhaseCSecond;
	}

	public Timestamp getEventFailurePhaseCThird() {
		return EventFailurePhaseCThird;
	}

	public void setEventFailurePhaseCThird(Timestamp eventFailurePhaseCThird) {
		EventFailurePhaseCThird = eventFailurePhaseCThird;
	}

	public Timestamp getEventFailurePhaseCFourth() {
		return EventFailurePhaseCFourth;
	}

	public void setEventFailurePhaseCFourth(Timestamp eventFailurePhaseCFourth) {
		EventFailurePhaseCFourth = eventFailurePhaseCFourth;
	}

	public Timestamp getEventFailurePhaseCFifth() {
		return EventFailurePhaseCFifth;
	}

	public void setEventFailurePhaseCFifth(Timestamp eventFailurePhaseCFifth) {
		EventFailurePhaseCFifth = eventFailurePhaseCFifth;
	}

	public Timestamp getEventPowerOnPhaseCFirst() {
		return EventPowerOnPhaseCFirst;
	}

	public void setEventPowerOnPhaseCFirst(Timestamp eventPowerOnPhaseCFirst) {
		EventPowerOnPhaseCFirst = eventPowerOnPhaseCFirst;
	}

	public Timestamp getEventPowerOnPhaseCSecond() {
		return EventPowerOnPhaseCSecond;
	}

	public void setEventPowerOnPhaseCSecond(Timestamp eventPowerOnPhaseCSecond) {
		EventPowerOnPhaseCSecond = eventPowerOnPhaseCSecond;
	}

	public Timestamp getEventPowerOnPhaseCThird() {
		return EventPowerOnPhaseCThird;
	}

	public void setEventPowerOnPhaseCThird(Timestamp eventPowerOnPhaseCThird) {
		EventPowerOnPhaseCThird = eventPowerOnPhaseCThird;
	}

	public Timestamp getEventPowerOnPhaseCFourth() {
		return EventPowerOnPhaseCFourth;
	}

	public void setEventPowerOnPhaseCFourth(Timestamp eventPowerOnPhaseCFourth) {
		EventPowerOnPhaseCFourth = eventPowerOnPhaseCFourth;
	}

	public Timestamp getEventPowerOnPhaseCFifth() {
		return EventPowerOnPhaseCFifth;
	}

	public void setEventPowerOnPhaseCFifth(Timestamp eventPowerOnPhaseCFifth) {
		EventPowerOnPhaseCFifth = eventPowerOnPhaseCFifth;
	}

	public BigDecimal getEventReverseRunPhaseACount() {
		return EventReverseRunPhaseACount;
	}

	public void setEventReverseRunPhaseACount(BigDecimal eventReverseRunPhaseACount) {
		EventReverseRunPhaseACount = eventReverseRunPhaseACount;
	}

	public Timestamp getEventReverseRunPhaseAFirstStart() {
		return EventReverseRunPhaseAFirstStart;
	}

	public void setEventReverseRunPhaseAFirstStart(Timestamp eventReverseRunPhaseAFirstStart) {
		EventReverseRunPhaseAFirstStart = eventReverseRunPhaseAFirstStart;
	}

	public Timestamp getEventReverseRunPhaseASecondStart() {
		return EventReverseRunPhaseASecondStart;
	}

	public void setEventReverseRunPhaseASecondStart(Timestamp eventReverseRunPhaseASecondStart) {
		EventReverseRunPhaseASecondStart = eventReverseRunPhaseASecondStart;
	}

	public Timestamp getEventReverseRunPhaseAThirdStart() {
		return EventReverseRunPhaseAThirdStart;
	}

	public void setEventReverseRunPhaseAThirdStart(Timestamp eventReverseRunPhaseAThirdStart) {
		EventReverseRunPhaseAThirdStart = eventReverseRunPhaseAThirdStart;
	}

	public Timestamp getEventReverseRunPhaseAFourthStart() {
		return EventReverseRunPhaseAFourthStart;
	}

	public void setEventReverseRunPhaseAFourthStart(Timestamp eventReverseRunPhaseAFourthStart) {
		EventReverseRunPhaseAFourthStart = eventReverseRunPhaseAFourthStart;
	}

	public Timestamp getEventReverseRunPhaseAFifthStart() {
		return EventReverseRunPhaseAFifthStart;
	}

	public void setEventReverseRunPhaseAFifthStart(Timestamp eventReverseRunPhaseAFifthStart) {
		EventReverseRunPhaseAFifthStart = eventReverseRunPhaseAFifthStart;
	}

	public Timestamp getEventReverseRunPhaseAFirstEnd() {
		return EventReverseRunPhaseAFirstEnd;
	}

	public void setEventReverseRunPhaseAFirstEnd(Timestamp eventReverseRunPhaseAFirstEnd) {
		EventReverseRunPhaseAFirstEnd = eventReverseRunPhaseAFirstEnd;
	}

	public Timestamp getEventReverseRunPhaseASecondEnd() {
		return EventReverseRunPhaseASecondEnd;
	}

	public void setEventReverseRunPhaseASecondEnd(Timestamp eventReverseRunPhaseASecondEnd) {
		EventReverseRunPhaseASecondEnd = eventReverseRunPhaseASecondEnd;
	}

	public Timestamp getEventReverseRunPhaseAThirdEnd() {
		return EventReverseRunPhaseAThirdEnd;
	}

	public void setEventReverseRunPhaseAThirdEnd(Timestamp eventReverseRunPhaseAThirdEnd) {
		EventReverseRunPhaseAThirdEnd = eventReverseRunPhaseAThirdEnd;
	}

	public Timestamp getEventReverseRunPhaseAFourthEnd() {
		return EventReverseRunPhaseAFourthEnd;
	}

	public void setEventReverseRunPhaseAFourthEnd(Timestamp eventReverseRunPhaseAFourthEnd) {
		EventReverseRunPhaseAFourthEnd = eventReverseRunPhaseAFourthEnd;
	}

	public Timestamp getEventReverseRunPhaseAFifthEnd() {
		return EventReverseRunPhaseAFifthEnd;
	}

	public void setEventReverseRunPhaseAFifthEnd(Timestamp eventReverseRunPhaseAFifthEnd) {
		EventReverseRunPhaseAFifthEnd = eventReverseRunPhaseAFifthEnd;
	}

	public BigDecimal getEventReverseRunPhaseBCount() {
		return EventReverseRunPhaseBCount;
	}

	public void setEventReverseRunPhaseBCount(BigDecimal eventReverseRunPhaseBCount) {
		EventReverseRunPhaseBCount = eventReverseRunPhaseBCount;
	}

	public Timestamp getEventReverseRunPhaseBFirstStart() {
		return EventReverseRunPhaseBFirstStart;
	}

	public void setEventReverseRunPhaseBFirstStart(Timestamp eventReverseRunPhaseBFirstStart) {
		EventReverseRunPhaseBFirstStart = eventReverseRunPhaseBFirstStart;
	}

	public Timestamp getEventReverseRunPhaseBSecondStart() {
		return EventReverseRunPhaseBSecondStart;
	}

	public void setEventReverseRunPhaseBSecondStart(Timestamp eventReverseRunPhaseBSecondStart) {
		EventReverseRunPhaseBSecondStart = eventReverseRunPhaseBSecondStart;
	}

	public Timestamp getEventReverseRunPhaseBThirdStart() {
		return EventReverseRunPhaseBThirdStart;
	}

	public void setEventReverseRunPhaseBThirdStart(Timestamp eventReverseRunPhaseBThirdStart) {
		EventReverseRunPhaseBThirdStart = eventReverseRunPhaseBThirdStart;
	}

	public Timestamp getEventReverseRunPhaseBFourthStart() {
		return EventReverseRunPhaseBFourthStart;
	}

	public void setEventReverseRunPhaseBFourthStart(Timestamp eventReverseRunPhaseBFourthStart) {
		EventReverseRunPhaseBFourthStart = eventReverseRunPhaseBFourthStart;
	}

	public Timestamp getEventReverseRunPhaseBFifthStart() {
		return EventReverseRunPhaseBFifthStart;
	}

	public void setEventReverseRunPhaseBFifthStart(Timestamp eventReverseRunPhaseBFifthStart) {
		EventReverseRunPhaseBFifthStart = eventReverseRunPhaseBFifthStart;
	}

	public Timestamp getEventReverseRunPhaseBFirstEnd() {
		return EventReverseRunPhaseBFirstEnd;
	}

	public void setEventReverseRunPhaseBFirstEnd(Timestamp eventReverseRunPhaseBFirstEnd) {
		EventReverseRunPhaseBFirstEnd = eventReverseRunPhaseBFirstEnd;
	}

	public Timestamp getEventReverseRunPhaseBSecondEnd() {
		return EventReverseRunPhaseBSecondEnd;
	}

	public void setEventReverseRunPhaseBSecondEnd(Timestamp eventReverseRunPhaseBSecondEnd) {
		EventReverseRunPhaseBSecondEnd = eventReverseRunPhaseBSecondEnd;
	}

	public Timestamp getEventReverseRunPhaseBThirdEnd() {
		return EventReverseRunPhaseBThirdEnd;
	}

	public void setEventReverseRunPhaseBThirdEnd(Timestamp eventReverseRunPhaseBThirdEnd) {
		EventReverseRunPhaseBThirdEnd = eventReverseRunPhaseBThirdEnd;
	}

	public Timestamp getEventReverseRunPhaseBFourthEnd() {
		return EventReverseRunPhaseBFourthEnd;
	}

	public void setEventReverseRunPhaseBFourthEnd(Timestamp eventReverseRunPhaseBFourthEnd) {
		EventReverseRunPhaseBFourthEnd = eventReverseRunPhaseBFourthEnd;
	}

	public Timestamp getEventReverseRunPhaseBFifthEnd() {
		return EventReverseRunPhaseBFifthEnd;
	}

	public void setEventReverseRunPhaseBFifthEnd(Timestamp eventReverseRunPhaseBFifthEnd) {
		EventReverseRunPhaseBFifthEnd = eventReverseRunPhaseBFifthEnd;
	}

	public BigDecimal getEventReverseRunPhaseCCount() {
		return EventReverseRunPhaseCCount;
	}

	public void setEventReverseRunPhaseCCount(BigDecimal eventReverseRunPhaseCCount) {
		EventReverseRunPhaseCCount = eventReverseRunPhaseCCount;
	}

	public Timestamp getEventReverseRunPhaseCFirstStart() {
		return EventReverseRunPhaseCFirstStart;
	}

	public void setEventReverseRunPhaseCFirstStart(Timestamp eventReverseRunPhaseCFirstStart) {
		EventReverseRunPhaseCFirstStart = eventReverseRunPhaseCFirstStart;
	}

	public Timestamp getEventReverseRunPhaseCSecondStart() {
		return EventReverseRunPhaseCSecondStart;
	}

	public void setEventReverseRunPhaseCSecondStart(Timestamp eventReverseRunPhaseCSecondStart) {
		EventReverseRunPhaseCSecondStart = eventReverseRunPhaseCSecondStart;
	}

	public Timestamp getEventReverseRunPhaseCThirdStart() {
		return EventReverseRunPhaseCThirdStart;
	}

	public void setEventReverseRunPhaseCThirdStart(Timestamp eventReverseRunPhaseCThirdStart) {
		EventReverseRunPhaseCThirdStart = eventReverseRunPhaseCThirdStart;
	}

	public Timestamp getEventReverseRunPhaseCFourthStart() {
		return EventReverseRunPhaseCFourthStart;
	}

	public void setEventReverseRunPhaseCFourthStart(Timestamp eventReverseRunPhaseCFourthStart) {
		EventReverseRunPhaseCFourthStart = eventReverseRunPhaseCFourthStart;
	}

	public Timestamp getEventReverseRunPhaseCFifthStart() {
		return EventReverseRunPhaseCFifthStart;
	}

	public void setEventReverseRunPhaseCFifthStart(Timestamp eventReverseRunPhaseCFifthStart) {
		EventReverseRunPhaseCFifthStart = eventReverseRunPhaseCFifthStart;
	}

	public Timestamp getEventReverseRunPhaseCFirstEnd() {
		return EventReverseRunPhaseCFirstEnd;
	}

	public void setEventReverseRunPhaseCFirstEnd(Timestamp eventReverseRunPhaseCFirstEnd) {
		EventReverseRunPhaseCFirstEnd = eventReverseRunPhaseCFirstEnd;
	}

	public Timestamp getEventReverseRunPhaseCSecondEnd() {
		return EventReverseRunPhaseCSecondEnd;
	}

	public void setEventReverseRunPhaseCSecondEnd(Timestamp eventReverseRunPhaseCSecondEnd) {
		EventReverseRunPhaseCSecondEnd = eventReverseRunPhaseCSecondEnd;
	}

	public Timestamp getEventReverseRunPhaseCThirdEnd() {
		return EventReverseRunPhaseCThirdEnd;
	}

	public void setEventReverseRunPhaseCThirdEnd(Timestamp eventReverseRunPhaseCThirdEnd) {
		EventReverseRunPhaseCThirdEnd = eventReverseRunPhaseCThirdEnd;
	}

	public Timestamp getEventReverseRunPhaseCFourthEnd() {
		return EventReverseRunPhaseCFourthEnd;
	}

	public void setEventReverseRunPhaseCFourthEnd(Timestamp eventReverseRunPhaseCFourthEnd) {
		EventReverseRunPhaseCFourthEnd = eventReverseRunPhaseCFourthEnd;
	}

	public Timestamp getEventReverseRunPhaseCFifthEnd() {
		return EventReverseRunPhaseCFifthEnd;
	}

	public void setEventReverseRunPhaseCFifthEnd(Timestamp eventReverseRunPhaseCFifthEnd) {
		EventReverseRunPhaseCFifthEnd = eventReverseRunPhaseCFifthEnd;
	}

	public BigDecimal getEventPowerOnCount() {
		return EventPowerOnCount;
	}

	public void setEventPowerOnCount(BigDecimal eventPowerOnCount) {
		EventPowerOnCount = eventPowerOnCount;
	}

	public Timestamp getEventPowerOnFirst() {
		return EventPowerOnFirst;
	}

	public void setEventPowerOnFirst(Timestamp eventPowerOnFirst) {
		EventPowerOnFirst = eventPowerOnFirst;
	}

	public Timestamp getEventPowerOnSecond() {
		return EventPowerOnSecond;
	}

	public void setEventPowerOnSecond(Timestamp eventPowerOnSecond) {
		EventPowerOnSecond = eventPowerOnSecond;
	}

	public Timestamp getEventPowerOnThird() {
		return EventPowerOnThird;
	}

	public void setEventPowerOnThird(Timestamp eventPowerOnThird) {
		EventPowerOnThird = eventPowerOnThird;
	}

	public Timestamp getEventPowerOnFourth() {
		return EventPowerOnFourth;
	}

	public void setEventPowerOnFourth(Timestamp eventPowerOnFourth) {
		EventPowerOnFourth = eventPowerOnFourth;
	}

	public Timestamp getEventPowerOnFifth() {
		return EventPowerOnFifth;
	}

	public void setEventPowerOnFifth(Timestamp eventPowerOnFifth) {
		EventPowerOnFifth = eventPowerOnFifth;
	}

	public BigDecimal getEventResetCounterCount() {
		return EventResetCounterCount;
	}

	public void setEventResetCounterCount(BigDecimal eventResetCounterCount) {
		EventResetCounterCount = eventResetCounterCount;
	}

	public Timestamp getEventResetCounterFirstStart() {
		return EventResetCounterFirstStart;
	}

	public void setEventResetCounterFirstStart(Timestamp eventResetCounterFirstStart) {
		EventResetCounterFirstStart = eventResetCounterFirstStart;
	}

	public Timestamp getEventResetCounterSecondStart() {
		return EventResetCounterSecondStart;
	}

	public void setEventResetCounterSecondStart(Timestamp eventResetCounterSecondStart) {
		EventResetCounterSecondStart = eventResetCounterSecondStart;
	}

	public Timestamp getEventResetCounterThirdStart() {
		return EventResetCounterThirdStart;
	}

	public void setEventResetCounterThirdStart(Timestamp eventResetCounterThirdStart) {
		EventResetCounterThirdStart = eventResetCounterThirdStart;
	}

	public Timestamp getEventResetCounterFourthStart() {
		return EventResetCounterFourthStart;
	}

	public void setEventResetCounterFourthStart(Timestamp eventResetCounterFourthStart) {
		EventResetCounterFourthStart = eventResetCounterFourthStart;
	}

	public Timestamp getEventResetCounterFifthStart() {
		return EventResetCounterFifthStart;
	}

	public void setEventResetCounterFifthStart(Timestamp eventResetCounterFifthStart) {
		EventResetCounterFifthStart = eventResetCounterFifthStart;
	}

	public Timestamp getEventResetCounterFirstEnd() {
		return EventResetCounterFirstEnd;
	}

	public void setEventResetCounterFirstEnd(Timestamp eventResetCounterFirstEnd) {
		EventResetCounterFirstEnd = eventResetCounterFirstEnd;
	}

	public Timestamp getEventResetCounterSecondEnd() {
		return EventResetCounterSecondEnd;
	}

	public void setEventResetCounterSecondEnd(Timestamp eventResetCounterSecondEnd) {
		EventResetCounterSecondEnd = eventResetCounterSecondEnd;
	}

	public Timestamp getEventResetCounterThirdEnd() {
		return EventResetCounterThirdEnd;
	}

	public void setEventResetCounterThirdEnd(Timestamp eventResetCounterThirdEnd) {
		EventResetCounterThirdEnd = eventResetCounterThirdEnd;
	}

	public Timestamp getEventResetCounterFourthEnd() {
		return EventResetCounterFourthEnd;
	}

	public void setEventResetCounterFourthEnd(Timestamp eventResetCounterFourthEnd) {
		EventResetCounterFourthEnd = eventResetCounterFourthEnd;
	}

	public Timestamp getEventResetCounterFifthEnd() {
		return EventResetCounterFifthEnd;
	}

	public void setEventResetCounterFifthEnd(Timestamp eventResetCounterFifthEnd) {
		EventResetCounterFifthEnd = eventResetCounterFifthEnd;
	}

	public BigDecimal getEventReverseRunThreePhaseCount() {
		return EventReverseRunThreePhaseCount;
	}

	public void setEventReverseRunThreePhaseCount(BigDecimal eventReverseRunThreePhaseCount) {
		EventReverseRunThreePhaseCount = eventReverseRunThreePhaseCount;
	}

	public Timestamp getEventReverseRunThreePhaseFirstStart() {
		return EventReverseRunThreePhaseFirstStart;
	}

	public void setEventReverseRunThreePhaseFirstStart(Timestamp eventReverseRunThreePhaseFirstStart) {
		EventReverseRunThreePhaseFirstStart = eventReverseRunThreePhaseFirstStart;
	}

	public Timestamp getEventReverseRunThreePhaseSecondStart() {
		return EventReverseRunThreePhaseSecondStart;
	}

	public void setEventReverseRunThreePhaseSecondStart(Timestamp eventReverseRunThreePhaseSecondStart) {
		EventReverseRunThreePhaseSecondStart = eventReverseRunThreePhaseSecondStart;
	}

	public Timestamp getEventReverseRunThreePhaseThirdStart() {
		return EventReverseRunThreePhaseThirdStart;
	}

	public void setEventReverseRunThreePhaseThirdStart(Timestamp eventReverseRunThreePhaseThirdStart) {
		EventReverseRunThreePhaseThirdStart = eventReverseRunThreePhaseThirdStart;
	}

	public Timestamp getEventReverseRunThreePhaseFourthStart() {
		return EventReverseRunThreePhaseFourthStart;
	}

	public void setEventReverseRunThreePhaseFourthStart(Timestamp eventReverseRunThreePhaseFourthStart) {
		EventReverseRunThreePhaseFourthStart = eventReverseRunThreePhaseFourthStart;
	}

	public Timestamp getEventReverseRunThreePhaseFifthStart() {
		return EventReverseRunThreePhaseFifthStart;
	}

	public void setEventReverseRunThreePhaseFifthStart(Timestamp eventReverseRunThreePhaseFifthStart) {
		EventReverseRunThreePhaseFifthStart = eventReverseRunThreePhaseFifthStart;
	}

	public Timestamp getEventReverseRunThreePhaseFirstEnd() {
		return EventReverseRunThreePhaseFirstEnd;
	}

	public void setEventReverseRunThreePhaseFirstEnd(Timestamp eventReverseRunThreePhaseFirstEnd) {
		EventReverseRunThreePhaseFirstEnd = eventReverseRunThreePhaseFirstEnd;
	}

	public Timestamp getEventReverseRunThreePhaseSecondEnd() {
		return EventReverseRunThreePhaseSecondEnd;
	}

	public void setEventReverseRunThreePhaseSecondEnd(Timestamp eventReverseRunThreePhaseSecondEnd) {
		EventReverseRunThreePhaseSecondEnd = eventReverseRunThreePhaseSecondEnd;
	}

	public Timestamp getEventReverseRunThreePhaseThirdEnd() {
		return EventReverseRunThreePhaseThirdEnd;
	}

	public void setEventReverseRunThreePhaseThirdEnd(Timestamp eventReverseRunThreePhaseThirdEnd) {
		EventReverseRunThreePhaseThirdEnd = eventReverseRunThreePhaseThirdEnd;
	}

	public Timestamp getEventReverseRunThreePhaseFourthEnd() {
		return EventReverseRunThreePhaseFourthEnd;
	}

	public void setEventReverseRunThreePhaseFourthEnd(Timestamp eventReverseRunThreePhaseFourthEnd) {
		EventReverseRunThreePhaseFourthEnd = eventReverseRunThreePhaseFourthEnd;
	}

	public Timestamp getEventReverseRunThreePhaseFifthEnd() {
		return EventReverseRunThreePhaseFifthEnd;
	}

	public void setEventReverseRunThreePhaseFifthEnd(Timestamp eventReverseRunThreePhaseFifthEnd) {
		EventReverseRunThreePhaseFifthEnd = eventReverseRunThreePhaseFifthEnd;
	}

	public BigDecimal getEventBatteryErrorCount() {
		return EventBatteryErrorCount;
	}

	public void setEventBatteryErrorCount(BigDecimal eventBatteryErrorCount) {
		EventBatteryErrorCount = eventBatteryErrorCount;
	}


	public Timestamp getServerTime() {
		return ServerTime;
	}

	public void setServerTime(Timestamp serverTime) {
		ServerTime = serverTime;
	}

	public Timestamp getEventCoverOpenTimeFirst() {
		return EventCoverOpenTimeFirst;
	}

	public void setEventCoverOpenTimeFirst(Timestamp eventCoverOpenTimeFirst) {
		EventCoverOpenTimeFirst = eventCoverOpenTimeFirst;
	}

	public Timestamp getEventCoverOpenTimeSecond() {
		return EventCoverOpenTimeSecond;
	}

	public void setEventCoverOpenTimeSecond(Timestamp eventCoverOpenTimeSecond) {
		EventCoverOpenTimeSecond = eventCoverOpenTimeSecond;
	}

	public Timestamp getEventCoverOpenTimeThird() {
		return EventCoverOpenTimeThird;
	}

	public void setEventCoverOpenTimeThird(Timestamp eventCoverOpenTimeThird) {
		EventCoverOpenTimeThird = eventCoverOpenTimeThird;
	}

	public Timestamp getEventCoverOpenTimeFourth() {
		return EventCoverOpenTimeFourth;
	}

	public void setEventCoverOpenTimeFourth(Timestamp eventCoverOpenTimeFourth) {
		EventCoverOpenTimeFourth = eventCoverOpenTimeFourth;
	}

	public Timestamp getEventCoverOpenTimeFifth() {
		return EventCoverOpenTimeFifth;
	}

	public void setEventCoverOpenTimeFifth(Timestamp eventCoverOpenTimeFifth) {
		EventCoverOpenTimeFifth = eventCoverOpenTimeFifth;
	}

	public Timestamp getEventCoverCloseTimeFirst() {
		return EventCoverCloseTimeFirst;
	}

	public void setEventCoverCloseTimeFirst(Timestamp eventCoverCloseTimeFirst) {
		EventCoverCloseTimeFirst = eventCoverCloseTimeFirst;
	}

	public Timestamp getEventCoverCloseTimeSecond() {
		return EventCoverCloseTimeSecond;
	}

	public void setEventCoverCloseTimeSecond(Timestamp eventCoverCloseTimeSecond) {
		EventCoverCloseTimeSecond = eventCoverCloseTimeSecond;
	}

	public Timestamp getEventCoverCloseTimeThird() {
		return EventCoverCloseTimeThird;
	}

	public void setEventCoverCloseTimeThird(Timestamp eventCoverCloseTimeThird) {
		EventCoverCloseTimeThird = eventCoverCloseTimeThird;
	}

	public Timestamp getEventCoverCloseTimeFourth() {
		return EventCoverCloseTimeFourth;
	}

	public void setEventCoverCloseTimeFourth(Timestamp eventCoverCloseTimeFourth) {
		EventCoverCloseTimeFourth = eventCoverCloseTimeFourth;
	}

	public Timestamp getEventCoverCloseTimeFifth() {
		return EventCoverCloseTimeFifth;
	}

	public void setEventCoverCloseTimeFifth(Timestamp eventCoverCloseTimeFifth) {
		EventCoverCloseTimeFifth = eventCoverCloseTimeFifth;
	}

	public Timestamp getEventImbalancePowerStartFirst() {
		return EventImbalancePowerStartFirst;
	}

	public void setEventImbalancePowerStartFirst(Timestamp eventImbalancePowerStartFirst) {
		EventImbalancePowerStartFirst = eventImbalancePowerStartFirst;
	}

	public Timestamp getEventImbalancePowerStartSecond() {
		return EventImbalancePowerStartSecond;
	}

	public void setEventImbalancePowerStartSecond(Timestamp eventImbalancePowerStartSecond) {
		EventImbalancePowerStartSecond = eventImbalancePowerStartSecond;
	}

	public Timestamp getEventImbalancePowerStartThird() {
		return EventImbalancePowerStartThird;
	}

	public void setEventImbalancePowerStartThird(Timestamp eventImbalancePowerStartThird) {
		EventImbalancePowerStartThird = eventImbalancePowerStartThird;
	}

	public Timestamp getEventImbalancePowerStartFourth() {
		return EventImbalancePowerStartFourth;
	}

	public void setEventImbalancePowerStartFourth(Timestamp eventImbalancePowerStartFourth) {
		EventImbalancePowerStartFourth = eventImbalancePowerStartFourth;
	}

	public Timestamp getEventImbalancePowerStartFifth() {
		return EventImbalancePowerStartFifth;
	}

	public void setEventImbalancePowerStartFifth(Timestamp eventImbalancePowerStartFifth) {
		EventImbalancePowerStartFifth = eventImbalancePowerStartFifth;
	}

	public Timestamp getEventImbalancePowerEndFirst() {
		return EventImbalancePowerEndFirst;
	}

	public void setEventImbalancePowerEndFirst(Timestamp eventImbalancePowerEndFirst) {
		EventImbalancePowerEndFirst = eventImbalancePowerEndFirst;
	}

	public Timestamp getEventImbalancePowerEndSecond() {
		return EventImbalancePowerEndSecond;
	}

	public void setEventImbalancePowerEndSecond(Timestamp eventImbalancePowerEndSecond) {
		EventImbalancePowerEndSecond = eventImbalancePowerEndSecond;
	}

	public Timestamp getEventImbalancePowerEndThird() {
		return EventImbalancePowerEndThird;
	}

	public void setEventImbalancePowerEndThird(Timestamp eventImbalancePowerEndThird) {
		EventImbalancePowerEndThird = eventImbalancePowerEndThird;
	}

	public Timestamp getEventImbalancePowerEndFourth() {
		return EventImbalancePowerEndFourth;
	}

	public void setEventImbalancePowerEndFourth(Timestamp eventImbalancePowerEndFourth) {
		EventImbalancePowerEndFourth = eventImbalancePowerEndFourth;
	}

	public Timestamp getEventImbalancePowerEndFifth() {
		return EventImbalancePowerEndFifth;
	}

	public void setEventImbalancePowerEndFifth(Timestamp eventImbalancePowerEndFifth) {
		EventImbalancePowerEndFifth = eventImbalancePowerEndFifth;
	}

	public Timestamp getEventReversePowerStartFirst() {
		return EventReversePowerStartFirst;
	}

	public void setEventReversePowerStartFirst(Timestamp eventReversePowerStartFirst) {
		EventReversePowerStartFirst = eventReversePowerStartFirst;
	}

	public Timestamp getEventReversePowerStartSecond() {
		return EventReversePowerStartSecond;
	}

	public void setEventReversePowerStartSecond(Timestamp eventReversePowerStartSecond) {
		EventReversePowerStartSecond = eventReversePowerStartSecond;
	}

	public Timestamp getEventReversePowerStartThird() {
		return EventReversePowerStartThird;
	}

	public void setEventReversePowerStartThird(Timestamp eventReversePowerStartThird) {
		EventReversePowerStartThird = eventReversePowerStartThird;
	}

	public Timestamp getEventReversePowerStartFourth() {
		return EventReversePowerStartFourth;
	}

	public void setEventReversePowerStartFourth(Timestamp eventReversePowerStartFourth) {
		EventReversePowerStartFourth = eventReversePowerStartFourth;
	}

	public Timestamp getEventReversePowerStartFifth() {
		return EventReversePowerStartFifth;
	}

	public void setEventReversePowerStartFifth(Timestamp eventReversePowerStartFifth) {
		EventReversePowerStartFifth = eventReversePowerStartFifth;
	}

	public Timestamp getEventReversePowerEndFirst() {
		return EventReversePowerEndFirst;
	}

	public void setEventReversePowerEndFirst(Timestamp eventReversePowerEndFirst) {
		EventReversePowerEndFirst = eventReversePowerEndFirst;
	}

	public Timestamp getEventReversePowerEndSecond() {
		return EventReversePowerEndSecond;
	}

	public void setEventReversePowerEndSecond(Timestamp eventReversePowerEndSecond) {
		EventReversePowerEndSecond = eventReversePowerEndSecond;
	}

	public Timestamp getEventReversePowerEndThird() {
		return EventReversePowerEndThird;
	}

	public void setEventReversePowerEndThird(Timestamp eventReversePowerEndThird) {
		EventReversePowerEndThird = eventReversePowerEndThird;
	}

	public Timestamp getEventReversePowerEndFourth() {
		return EventReversePowerEndFourth;
	}

	public void setEventReversePowerEndFourth(Timestamp eventReversePowerEndFourth) {
		EventReversePowerEndFourth = eventReversePowerEndFourth;
	}

	public Timestamp getEventReversePowerEndFifth() {
		return EventReversePowerEndFifth;
	}

	public void setEventReversePowerEndFifth(Timestamp eventReversePowerEndFifth) {
		EventReversePowerEndFifth = eventReversePowerEndFifth;
	}

	public Timestamp getEventStealElectricityFirst() {
		return EventStealElectricityFirst;
	}

	public void setEventStealElectricityFirst(Timestamp eventStealElectricityFirst) {
		EventStealElectricityFirst = eventStealElectricityFirst;
	}

	public Timestamp getEventStealElectricitySecond() {
		return EventStealElectricitySecond;
	}

	public void setEventStealElectricitySecond(Timestamp eventStealElectricitySecond) {
		EventStealElectricitySecond = eventStealElectricitySecond;
	}

	public Timestamp getEventStealElectricityThird() {
		return EventStealElectricityThird;
	}

	public void setEventStealElectricityThird(Timestamp eventStealElectricityThird) {
		EventStealElectricityThird = eventStealElectricityThird;
	}

	public Timestamp getEventStealElectricityFourth() {
		return EventStealElectricityFourth;
	}

	public void setEventStealElectricityFourth(Timestamp eventStealElectricityFourth) {
		EventStealElectricityFourth = eventStealElectricityFourth;
	}

	public Timestamp getEventStealElectricityFifth() {
		return EventStealElectricityFifth;
	}

	public void setEventStealElectricityFifth(Timestamp eventStealElectricityFifth) {
		EventStealElectricityFifth = eventStealElectricityFifth;
	}

	public BigDecimal getEventAutoBillingReset() {
		return EventAutoBillingReset;
	}

	public void setEventAutoBillingReset(BigDecimal eventAutoBillingReset) {
		EventAutoBillingReset = eventAutoBillingReset;
	}

	public BigDecimal getEventLogCleared() {
		return EventLogCleared;
	}

	public void setEventLogCleared(BigDecimal eventLogCleared) {
		EventLogCleared = eventLogCleared;
	}

	public BigDecimal getEventNewTimeChanged() {
		return EventNewTimeChanged;
	}

	public void setEventNewTimeChanged(BigDecimal eventNewTimeChanged) {
		EventNewTimeChanged = eventNewTimeChanged;
	}

	public BigDecimal getEventOldTimeChanged() {
		return EventOldTimeChanged;
	}

	public void setEventOldTimeChanged(BigDecimal eventOldTimeChanged) {
		EventOldTimeChanged = eventOldTimeChanged;
	}

	public BigDecimal getEventTamperTerminalCoverRemoved() {
		return EventTamperTerminalCoverRemoved;
	}

	public void setEventTamperTerminalCoverRemoved(BigDecimal eventTamperTerminalCoverRemoved) {
		EventTamperTerminalCoverRemoved = eventTamperTerminalCoverRemoved;
	}

	public BigDecimal getEventTamperTerminalCoverClosed() {
		return EventTamperTerminalCoverClosed;
	}

	public void setEventTamperTerminalCoverClosed(BigDecimal eventTamperTerminalCoverClosed) {
		EventTamperTerminalCoverClosed = eventTamperTerminalCoverClosed;
	}

	public BigDecimal getEventAlarmMeterCoverClosed() {
		return EventAlarmMeterCoverClosed;
	}

	public void setEventAlarmMeterCoverClosed(BigDecimal eventAlarmMeterCoverClosed) {
		EventAlarmMeterCoverClosed = eventAlarmMeterCoverClosed;
	}

	public BigDecimal getEventAlarmMeterCoverOpened() {
		return EventAlarmMeterCoverOpened;
	}

	public void setEventAlarmMeterCoverOpened(BigDecimal eventAlarmMeterCoverOpened) {
		EventAlarmMeterCoverOpened = eventAlarmMeterCoverOpened;
	}

	public BigDecimal getEventAlarmMeterTerminalClosed() {
		return EventAlarmMeterTerminalClosed;
	}

	public void setEventAlarmMeterTerminalClosed(BigDecimal eventAlarmMeterTerminalClosed) {
		EventAlarmMeterTerminalClosed = eventAlarmMeterTerminalClosed;
	}

	public BigDecimal getEventAlarmMeterTerminalRemoved() {
		return EventAlarmMeterTerminalRemoved;
	}

	public void setEventAlarmMeterTerminalRemoved(BigDecimal eventAlarmMeterTerminalRemoved) {
		EventAlarmMeterTerminalRemoved = eventAlarmMeterTerminalRemoved;
	}

	public BigDecimal getEventAlarmAsymPowerEnd() {
		return EventAlarmAsymPowerEnd;
	}

	public void setEventAlarmAsymPowerEnd(BigDecimal eventAlarmAsymPowerEnd) {
		EventAlarmAsymPowerEnd = eventAlarmAsymPowerEnd;
	}

	public BigDecimal getEventAlarmAsymPowerStart() {
		return EventAlarmAsymPowerStart;
	}

	public void setEventAlarmAsymPowerStart(BigDecimal eventAlarmAsymPowerStart) {
		EventAlarmAsymPowerStart = eventAlarmAsymPowerStart;
	}

	public Timestamp getEventReverseRunThreePhaseStartTime() {
		return EventReverseRunThreePhaseStartTime;
	}

	public void setEventReverseRunThreePhaseStartTime(Timestamp eventReverseRunThreePhaseStartTime) {
		EventReverseRunThreePhaseStartTime = eventReverseRunThreePhaseStartTime;
	}
	
	private String DcuCode;
	private BigDecimal EventProgrammingCount;
	private Timestamp EventProgrammingFirst;
	private Timestamp EventProgrammingSecond;
	private Timestamp EventProgrammingThird;
	private Timestamp EventProgrammingFourth;
	private Timestamp EventProgrammingFifth;
	private BigDecimal EventPasswordChangeCount;
	private Timestamp EventPasswordChangeFirst;
	private Timestamp EventPasswordChangeSecond;
	private Timestamp EventPasswordChangeThird;
	private Timestamp EventPasswordChangeFourth;
	private Timestamp EventPasswordChangeFifth;
	private BigDecimal EventVoltageImbalanceCount;
	private Timestamp EventVoltageImbalanceFirst;
	private Timestamp EventVoltageImbalanceSecond;
	private Timestamp EventVoltageImbalanceThird;
	private Timestamp EventVoltageImbalanceFourth;
	private Timestamp EventVoltageImbalanceFifth;
	private BigDecimal EventPowerFailureCount;
	private Timestamp EventPowerFailureFirst;
	private Timestamp EventPowerFailureSecond;
	private Timestamp EventPowerFailureThird;
	private Timestamp EventPowerFailureFourth;
	private Timestamp EventPowerFailureFifth;
	private Timestamp EventPowerRecoverFirst;
	private Timestamp EventPowerRecoverSecond;
	private Timestamp EventPowerRecoverThird;
	private Timestamp EventPowerRecoverFourth;
	private Timestamp EventPowerRecoverFifth;
	private BigDecimal EventTimeDateChangeCount;
	private Timestamp EventTimeDateChangeFirstStart;
	private Timestamp EventTimeDateChangeSecondStart;
	private Timestamp EventTimeDateChangeThirdStart;
	private Timestamp EventTimeDateChangeFourthStart;
	private Timestamp EventTimeDateChangeFifthStart;
	private Timestamp EventTimeDateChangeFirstEnd;
	private Timestamp EventTimeDateChangeSecondEnd;
	private Timestamp EventTimeDateChangeThirdEnd;
	private Timestamp EventTimeDateChangeFourthEnd;
	private Timestamp EventTimeDateChangeFifthEnd;
	private BigDecimal EventFailurePhaseACount;
	private Timestamp EventFailurePhaseAFirst;
	private Timestamp EventFailurePhaseASecond;
	private Timestamp EventFailurePhaseAThird;
	private Timestamp EventFailurePhaseAFourth;
	private Timestamp EventFailurePhaseAFifth;
	private Timestamp EventPowerOnPhaseAFirst;
	private Timestamp EventPowerOnPhaseASecond;
	private Timestamp EventPowerOnPhaseAThird;
	private Timestamp EventPowerOnPhaseAFourth;
	private Timestamp EventPowerOnPhaseAFifth;
	private BigDecimal EventFailurePhaseBCount;
	private Timestamp EventFailurePhaseBFirst;
	private Timestamp EventFailurePhaseBSecond;
	private Timestamp EventFailurePhaseBThird;
	private Timestamp EventFailurePhaseBFourth;
	private Timestamp EventFailurePhaseBFifth;
	private Timestamp EventPowerOnPhaseBFirst;
	private Timestamp EventPowerOnPhaseBSecond;
	private Timestamp EventPowerOnPhaseBThird;
	private Timestamp EventPowerOnPhaseBFourth;
	private Timestamp EventPowerOnPhaseBFifth;
	private BigDecimal EventFailurePhaseCCount;
	private Timestamp EventFailurePhaseCFirst;
	private Timestamp EventFailurePhaseCSecond;
	private Timestamp EventFailurePhaseCThird;
	private Timestamp EventFailurePhaseCFourth;
	private Timestamp EventFailurePhaseCFifth;
	private Timestamp EventPowerOnPhaseCFirst;
	private Timestamp EventPowerOnPhaseCSecond;
	private Timestamp EventPowerOnPhaseCThird;
	private Timestamp EventPowerOnPhaseCFourth;
	private Timestamp EventPowerOnPhaseCFifth;
	private BigDecimal EventReverseRunPhaseACount;
	private Timestamp EventReverseRunPhaseAFirstStart;
	private Timestamp EventReverseRunPhaseASecondStart;
	private Timestamp EventReverseRunPhaseAThirdStart;
	private Timestamp EventReverseRunPhaseAFourthStart;
	private Timestamp EventReverseRunPhaseAFifthStart;
	private Timestamp EventReverseRunPhaseAFirstEnd;
	private Timestamp EventReverseRunPhaseASecondEnd;
	private Timestamp EventReverseRunPhaseAThirdEnd;
	private Timestamp EventReverseRunPhaseAFourthEnd;
	private Timestamp EventReverseRunPhaseAFifthEnd;
	private BigDecimal EventReverseRunPhaseBCount;
	private Timestamp EventReverseRunPhaseBFirstStart;
	private Timestamp EventReverseRunPhaseBSecondStart;
	private Timestamp EventReverseRunPhaseBThirdStart;
	private Timestamp EventReverseRunPhaseBFourthStart;
	private Timestamp EventReverseRunPhaseBFifthStart;
	private Timestamp EventReverseRunPhaseBFirstEnd;
	private Timestamp EventReverseRunPhaseBSecondEnd;
	private Timestamp EventReverseRunPhaseBThirdEnd;
	private Timestamp EventReverseRunPhaseBFourthEnd;
	private Timestamp EventReverseRunPhaseBFifthEnd;
	private BigDecimal EventReverseRunPhaseCCount;
	private Timestamp EventReverseRunPhaseCFirstStart;
	private Timestamp EventReverseRunPhaseCSecondStart;
	private Timestamp EventReverseRunPhaseCThirdStart;
	private Timestamp EventReverseRunPhaseCFourthStart;
	private Timestamp EventReverseRunPhaseCFifthStart;
	private Timestamp EventReverseRunPhaseCFirstEnd;
	private Timestamp EventReverseRunPhaseCSecondEnd;
	private Timestamp EventReverseRunPhaseCThirdEnd;
	private Timestamp EventReverseRunPhaseCFourthEnd;
	private Timestamp EventReverseRunPhaseCFifthEnd;
	private BigDecimal EventPowerOnCount;
	private Timestamp EventPowerOnFirst;
	private Timestamp EventPowerOnSecond;
	private Timestamp EventPowerOnThird;
	private Timestamp EventPowerOnFourth;
	private Timestamp EventPowerOnFifth;
	private BigDecimal EventResetCounterCount;
	private Timestamp EventResetCounterFirstStart;
	private Timestamp EventResetCounterSecondStart;
	private Timestamp EventResetCounterThirdStart;
	private Timestamp EventResetCounterFourthStart;
	private Timestamp EventResetCounterFifthStart;
	private Timestamp EventResetCounterFirstEnd;
	private Timestamp EventResetCounterSecondEnd;
	private Timestamp EventResetCounterThirdEnd;
	private Timestamp EventResetCounterFourthEnd;
	private Timestamp EventResetCounterFifthEnd;
	private BigDecimal EventReverseRunThreePhaseCount;
	private Timestamp EventReverseRunThreePhaseFirstStart;
	private Timestamp EventReverseRunThreePhaseSecondStart;
	private Timestamp EventReverseRunThreePhaseThirdStart;
	private Timestamp EventReverseRunThreePhaseFourthStart;
	private Timestamp EventReverseRunThreePhaseFifthStart;
	private Timestamp EventReverseRunThreePhaseFirstEnd;
	private Timestamp EventReverseRunThreePhaseSecondEnd;
	private Timestamp EventReverseRunThreePhaseThirdEnd;
	private Timestamp EventReverseRunThreePhaseFourthEnd;
	private Timestamp EventReverseRunThreePhaseFifthEnd;
	private BigDecimal EventBatteryErrorCount;

	private Timestamp ServerTime;
	// Sự kiện gelex
	private Timestamp EventCoverOpenTimeFirst;
	private Timestamp EventCoverOpenTimeSecond;
	private Timestamp EventCoverOpenTimeThird;
	private Timestamp EventCoverOpenTimeFourth;
	private Timestamp EventCoverOpenTimeFifth;
	private Timestamp EventCoverCloseTimeFirst;
	private Timestamp EventCoverCloseTimeSecond;
	private Timestamp EventCoverCloseTimeThird;
	private Timestamp EventCoverCloseTimeFourth;
	private Timestamp EventCoverCloseTimeFifth;
	private Timestamp EventImbalancePowerStartFirst;
	private Timestamp EventImbalancePowerStartSecond;
	private Timestamp EventImbalancePowerStartThird;
	private Timestamp EventImbalancePowerStartFourth;
	private Timestamp EventImbalancePowerStartFifth;
	private Timestamp EventImbalancePowerEndFirst;
	private Timestamp EventImbalancePowerEndSecond;
	private Timestamp EventImbalancePowerEndThird;
	private Timestamp EventImbalancePowerEndFourth;
	private Timestamp EventImbalancePowerEndFifth;
	private Timestamp EventReversePowerStartFirst;
	private Timestamp EventReversePowerStartSecond;
	private Timestamp EventReversePowerStartThird;
	private Timestamp EventReversePowerStartFourth;
	private Timestamp EventReversePowerStartFifth;
	private Timestamp EventReversePowerEndFirst;
	private Timestamp EventReversePowerEndSecond;
	private Timestamp EventReversePowerEndThird;
	private Timestamp EventReversePowerEndFourth;
	private Timestamp EventReversePowerEndFifth;
	private Timestamp EventStealElectricityFirst;
	private Timestamp EventStealElectricitySecond;
	private Timestamp EventStealElectricityThird;
	private Timestamp EventStealElectricityFourth;
	private Timestamp EventStealElectricityFifth;
	private BigDecimal EventAutoBillingReset;
	private BigDecimal EventLogCleared;
	private BigDecimal EventNewTimeChanged;
	private BigDecimal EventOldTimeChanged;
	private BigDecimal EventTamperTerminalCoverRemoved;
	private BigDecimal EventTamperTerminalCoverClosed;
	private BigDecimal EventAlarmMeterCoverClosed;
	private BigDecimal EventAlarmMeterCoverOpened;
	private BigDecimal EventAlarmMeterTerminalClosed;
	private BigDecimal EventAlarmMeterTerminalRemoved;
	private BigDecimal EventAlarmAsymPowerEnd;
	private BigDecimal EventAlarmAsymPowerStart;

	private Timestamp EventReverseRunThreePhaseStartTime;
}
