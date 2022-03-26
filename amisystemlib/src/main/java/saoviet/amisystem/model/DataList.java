/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: MT3Business.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-06-06 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model;

import java.math.BigDecimal;
import saoviet.amisystem.model.datacollection.DataCollection;

public class DataList {
	private String dataString;
	private int dataInt;
	private BigDecimal dataBigDecimal;
	private DataCollection dataDataField;
	public String getDataString() {
		return dataString;
	}
	public void setDataString(String dataString) {
		this.dataString = dataString;
	}
	public int getDataInt() {
		return dataInt;
	}
	public void setDataInt(int dataInt) {
		this.dataInt = dataInt;
	}
	public BigDecimal getDataBigDecimal() {
		return dataBigDecimal;
	}
	public void setDataBigDecimal(BigDecimal dataBigDecimal) {
		this.dataBigDecimal = dataBigDecimal;
	}
	public DataCollection getDataDataField() {
		return dataDataField;
	}
	public void setDataDataField(DataCollection dataDataField) {
		this.dataDataField = dataDataField;
	}

}
