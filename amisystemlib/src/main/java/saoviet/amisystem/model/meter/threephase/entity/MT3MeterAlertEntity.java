/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: MT3Business.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.entity;

public class MT3MeterAlertEntity {
	private int AlertIndex;
	private String AlertEnd;
	private String AlertValue;

	public int getAlertIndex() {
		return AlertIndex;
	}

	public void setAlertIndex(int alertIndex) {
		AlertIndex = alertIndex;
	}

	public String getAlertEnd() {
		return AlertEnd;
	}

	public void setAlertEnd(String alertEnd) {
		AlertEnd = alertEnd;
	}

	public String getAlertValue() {
		return AlertValue;
	}

	public void setAlertValue(String alertValue) {
		AlertValue = alertValue;
	}

}
