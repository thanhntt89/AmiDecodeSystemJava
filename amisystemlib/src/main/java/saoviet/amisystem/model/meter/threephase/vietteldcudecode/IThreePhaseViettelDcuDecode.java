/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: IThreePhaseViettelDcuDecode.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-06-06 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.vietteldcudecode;

import saoviet.amisystem.event.SystemEventCallback;

public interface IThreePhaseViettelDcuDecode {
	OperationPacketViettelDecode getMessageOperationDecode() ;
	HistoricalPacketViettelDecode getMessageHistoricalDecode();
	EventPacketViettelDecode getMessageEventDecode() ;
	//WarningPacketViettelDecode getMessageWarningDecode() ;
	LoadProfilePacketViettelDecode getMessageLoadProfileDecode() ;
	void setSystemEventCallBack(SystemEventCallback systemEventCallback);;
}
