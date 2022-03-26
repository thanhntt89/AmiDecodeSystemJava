/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: IOnePhaseDecode.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.model.meter.onephase.decode;

import saoviet.amisystem.event.SystemEventCallback;

public interface IOnePhaseDecode {

	IHexingDecode getIHexingDecode();

	void setSystemEventCallBack(SystemEventCallback systemEventCallback);
}
