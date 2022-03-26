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

public class MT3MeasurementPointAlertConfigField implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ConfigParameter;
    public String getConfigParameter() {
		return ConfigParameter;
	}
	public void setConfigParameter(String configParameter) {
		ConfigParameter = configParameter;
	}
	public BigDecimal getConfigValue() {
		return ConfigValue;
	}
	public void setConfigValue(BigDecimal configValue) {
		ConfigValue = configValue;
	}
	public String getConfigType() {
		return ConfigType;
	}
	public void setConfigType(String configType) {
		ConfigType = configType;
	}
	public String getMeterCode() {
		return MeterCode;
	}
	public void setMeterCode(String meterCode) {
		MeterCode = meterCode;
	}
	public String getDcuCode() {
		return DcuCode;
	}
	public void setDcuCode(String dcuCode) {
		DcuCode = dcuCode;
	}
	private BigDecimal ConfigValue;
    private String ConfigType;
    private String MeterCode;
    private String DcuCode;
   
}
