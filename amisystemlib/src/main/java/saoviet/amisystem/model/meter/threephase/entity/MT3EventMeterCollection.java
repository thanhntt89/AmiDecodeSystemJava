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
import java.util.ArrayList;
import java.util.List;


public class MT3EventMeterCollection implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<MT3EventMeterField> eventValueList = new ArrayList<MT3EventMeterField>();

	public List<MT3EventMeterField> getEventMeterList() {
		return eventValueList;
	}

	public void add(String dcucode,String meterCode, Timestamp servertime, String name, BigDecimal count, BigDecimal timevalue,
			Timestamp starttime1, Timestamp starttime2, Timestamp starttime3, Timestamp starttime4,
			Timestamp starttime5, Timestamp endtime1, Timestamp endtime2, Timestamp endtime3, Timestamp endtime4,
			Timestamp endtime5, String note) {
		MT3EventMeterField item = new MT3EventMeterField();
		item.setDcuCode(dcucode);
		item.setMeterId(meterCode);
		item.setServerTime(servertime);
		item.setEventName(name);
		item.setEventCount(count);
		item.setEventTime(timevalue);
		item.setEventStartTime1(starttime1);
		item.setEventStartTime2(starttime2);
		item.setEventStartTime3(starttime3);
		item.setEventStartTime4(starttime4);
		item.setEventStartTime5(starttime5);
		item.setEventEndTime1(endtime1);
		item.setEventEndTime2(endtime2);
		item.setEventEndTime3(endtime3);
		item.setEventEndTime4(endtime4);
		item.setEventEndTime5(endtime5);
		item.setNotes(note);
		this.eventValueList.add(item);
		item = null;
		Runtime.getRuntime().gc();
	}
}
