/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 *  Class Name: IActiveMQBase.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.connectionmanagement;

import org.eclipse.paho.client.mqttv3.MqttCallback;

import saoviet.amisystem.event.ConnectionStatusEventCallback;
import saoviet.amisystem.event.IConnectionStatusEvent;
import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.messagemanagement.IMessageQueueManagement;

public interface IConnectionManagement extends MqttCallback, SystemEventCallback, IConnectionStatusEvent {    
	
	 void setConnectionStatusEventCallback(ConnectionStatusEventCallback connectionStatusEventCallback);
	 IProperties properties();
	 IMessageQueueManagement iMessageQueueManagement();
     void connect();
     Boolean disconnect();
     Boolean existThreadDecode();
     void unSubcriber();
     void loadSystemConfig();
}
