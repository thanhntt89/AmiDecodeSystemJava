/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: IMT1Business.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.business;

import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.meter.onephase.entity.MT1EventEntity;
import saoviet.amisystem.model.meter.onephase.entity.MT1HistoricalEntity;
import saoviet.amisystem.model.meter.onephase.entity.MT1MePDVEntity;
import saoviet.amisystem.model.meter.onephase.entity.MT1OperationEntity;

public interface IMT1Business {
	void insertOperation(MT1OperationEntity operation);

	void insertIntantaneous(MT1OperationEntity intantaneous);

	void insertEvent(MT1EventEntity event);

	void insertMePDV(MT1MePDVEntity mepdv);

	void insertHistorical(MT1HistoricalEntity historical);

	void setSystemEventCallback(SystemEventCallback systemEventCallback);
}
