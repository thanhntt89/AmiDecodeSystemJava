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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MT3MeterAlertCollection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<MT3MeterAlertEntity> meterAlertList = new ArrayList<MT3MeterAlertEntity>();
	private String dcuCode;
	private Timestamp serverTime;
	private String meterCode;
	
	public List<MT3MeterAlertEntity> getAlertValueList() {
		return meterAlertList;
	}

	public void add(int index, String value, String status) {
		MT3MeterAlertEntity item = new MT3MeterAlertEntity();
		item.setAlertIndex(index);
		item.setAlertValue(value);
		item.setAlertEnd(status);
		this.meterAlertList.add(item);
		
		item = null;
		Runtime.getRuntime().gc();
	}

	public String getDcuCode() {
		return this.dcuCode;
	}

	public void setDcuCode(String dcuCode) {
		this.dcuCode = dcuCode;
	}

	public String getMeterCode() {
		return this.meterCode;
	}

	public void setMeterCode(String metercode) {
		this.meterCode = metercode;
	}

	public Timestamp getServerTime() {
		return this.serverTime;
	}

	public void setServerTime(Timestamp serverTime) {
		this.serverTime = serverTime;
	}

}
