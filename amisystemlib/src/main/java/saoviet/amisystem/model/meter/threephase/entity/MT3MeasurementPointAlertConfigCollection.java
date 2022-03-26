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
import java.util.ArrayList;
import java.util.List;


public class MT3MeasurementPointAlertConfigCollection implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<MT3MeasurementPointAlertConfigField> alertValueList = new ArrayList<MT3MeasurementPointAlertConfigField>();
	
	public List<MT3MeasurementPointAlertConfigField> getAlertValueList() {
		return alertValueList;
	}

	public void add(String dcucode, String meterid,
			String parameterconfig, String typeconfig, BigDecimal value)
	{
		MT3MeasurementPointAlertConfigField item = new MT3MeasurementPointAlertConfigField();
		item.setDcuCode(dcucode);
		item.setMeterCode(meterid);
		item.setConfigParameter(parameterconfig);
		item.setConfigType(typeconfig);
		item.setConfigValue(value);
		this.alertValueList.add(item);
	}
}
