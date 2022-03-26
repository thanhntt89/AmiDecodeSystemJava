/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: IThreePhaseDecode.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-09 15:47:06
 */
package saoviet.amisystem.model.meter.threephase.decode;

import saoviet.amisystem.event.SystemEventCallback;

public interface IThreePhaseDecode {

	void  StartThreadAutoInsertDatabase();
	boolean StopThreadAutoInsertDatabase();

	IThreePhaseMessageDecode getIElsterDecode();

	IThreePhaseMessageDecode getILandisDecode();

	IThreePhaseMessageDecode getIGelexDecode();

	IThreePhaseMessageDecode getIStarDecode();

	void setSystemEventCallBack(SystemEventCallback systemEventCallback);
}
