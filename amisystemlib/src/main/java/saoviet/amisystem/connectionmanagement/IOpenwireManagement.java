/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 *  Class Name: IOpenwireManagement.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-05-07 15:47:06
 */


package saoviet.amisystem.connectionmanagement;

import saoviet.amisystem.event.ConnectionStatusEventCallback;

public interface IOpenwireManagement {
	 void setConnectionStatusEventCallback(ConnectionStatusEventCallback connectionStatusEventCallback);
	 void connect();
	 void disconnect();
	 boolean isStartThreadCheckingDCUStatus();
}
