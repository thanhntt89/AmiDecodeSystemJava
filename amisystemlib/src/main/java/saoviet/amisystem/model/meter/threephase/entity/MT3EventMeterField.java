package saoviet.amisystem.model.meter.threephase.entity;
/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: MT3Business.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class MT3EventMeterField implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private Timestamp ServerTime;
	private Timestamp MeterTime;
	private String EventName;
	private BigDecimal EventCount;
	private BigDecimal EventTime;
	private Timestamp EventStartTime1;
	private Timestamp EventEndTime1;
	private Timestamp EventStartTime2;
	private Timestamp EventEndTime2;
	private Timestamp EventStartTime3;
	private Timestamp EventEndTime3;
	private Timestamp EventStartTime4;
	private Timestamp EventEndTime4;
	private Timestamp EventStartTime5;
	private Timestamp EventEndTime5;
	private String Notes;
	private String MeterId;
	private String DcuCode;
	private Boolean HasEndEvent;
	
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

	public String getEventName() {
		return EventName;
	}

	public void setEventName(String eventName) {
		EventName = eventName;
	}

	public BigDecimal getEventCount() {
		return EventCount;
	}

	public void setEventCount(BigDecimal eventCount) {
		EventCount = eventCount;
	}

	public BigDecimal getEventTime() {
		return EventTime;
	}

	public void setEventTime(BigDecimal eventTime) {
		EventTime = eventTime;
	}

	public Timestamp getEventStartTime1() {
		return EventStartTime1;
	}

	public void setEventStartTime1(Timestamp eventStartTime1) {
		EventStartTime1 = eventStartTime1;
	}

	public Timestamp getEventEndTime1() {
		return EventEndTime1;
	}

	public void setEventEndTime1(Timestamp eventEndTime1) {
		EventEndTime1 = eventEndTime1;
	}

	public Timestamp getEventStartTime2() {
		return EventStartTime2;
	}

	public void setEventStartTime2(Timestamp eventStartTime2) {
		EventStartTime2 = eventStartTime2;
	}

	public Timestamp getEventEndTime2() {
		return EventEndTime2;
	}

	public void setEventEndTime2(Timestamp eventEndTime2) {
		EventEndTime2 = eventEndTime2;
	}

	public Timestamp getEventStartTime3() {
		return EventStartTime3;
	}

	public void setEventStartTime3(Timestamp eventStartTime3) {
		EventStartTime3 = eventStartTime3;
	}

	public Timestamp getEventEndTime3() {
		return EventEndTime3;
	}

	public void setEventEndTime3(Timestamp eventEndTime3) {
		EventEndTime3 = eventEndTime3;
	}

	public Timestamp getEventStartTime4() {
		return EventStartTime4;
	}

	public void setEventStartTime4(Timestamp eventStartTime4) {
		EventStartTime4 = eventStartTime4;
	}

	public Timestamp getEventEndTime4() {
		return EventEndTime4;
	}

	public void setEventEndTime4(Timestamp eventEndTime4) {
		EventEndTime4 = eventEndTime4;
	}

	public Timestamp getEventStartTime5() {
		return EventStartTime5;
	}

	public void setEventStartTime5(Timestamp eventStartTime5) {
		EventStartTime5 = eventStartTime5;
	}

	public Timestamp getEventEndTime5() {
		return EventEndTime5;
	}

	public void setEventEndTime5(Timestamp eventEndTime5) {
		EventEndTime5 = eventEndTime5;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
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

	public Boolean getHasEndEvent() {
		return HasEndEvent;
	}

	public void setHasEndEvent(Boolean hasEndEvent) {
		HasEndEvent = hasEndEvent;
	}
}
