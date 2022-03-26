/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: IConnectionServerTimeManagement.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-06-06 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.connectionmanagement;

import org.eclipse.paho.client.mqttv3.MqttCallback;

import saoviet.amisystem.event.ConnectionStatusEventCallback;
import saoviet.amisystem.event.IConnectionStatusEvent;

public interface IConnectionServerTimeManagement extends MqttCallback, IConnectionStatusEvent {
	// Gửi dữ liệu xuống cho gateway
	public void sendData(String topics, byte[] data);

	void setConnectionStatusEventCallback(ConnectionStatusEventCallback connectionStatusEventCallback);

	IProperties properties();

	void connect();

	Boolean disconnect();

	void loadSystemConfig();
	void sendTimeAll();
}
