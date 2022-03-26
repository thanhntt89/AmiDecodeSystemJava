/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: MT3Business.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import saoviet.amisystem.model.datacollection.DataCollection;

public class MT3LoadProfilePacketList {

	private String ServerTimeValue;

	public String getServerTimeValue() {
		return ServerTimeValue;
	}

	public void setServerTimeValue(String serverTimeValue) {
		ServerTimeValue = serverTimeValue;
	}

	private List<DataCollection> loadProfileList = Collections.synchronizedList(new ArrayList<DataCollection>());

	
	public List<DataCollection> getLoadProfileList() {
		return loadProfileList;
	}

	public void setLoadProfileList(List<DataCollection> loadProfileList) {
		this.loadProfileList = loadProfileList;
	}

	public void addLoadProfileList(DataCollection loadList) {
		this.loadProfileList.add(loadList.copy());
	}
}
