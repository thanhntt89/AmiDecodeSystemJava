/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: IMessageQueueManagement.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 *----------------------------------------------------------------*/
package saoviet.amisystem.messagemanagement;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import saoviet.amisystem.event.SystemEventCallback;

public interface IMessageQueueManagement extends  Runnable, SystemEventCallback {
	/**
	 * Chạy luồng xử lý lấy bản tin ra khỏi hàng đợi để xử lý
	 */
	void startThreadDequeue();

	/**
	 * Dừng luồng xử lý lấy bản tin ra khỏi hàng đợi
	 */
	Boolean stopThreadDequeue();

	/**
	 * Xử lý bản tin khi vào hàng đợi để xử lý
	 */
	void enQueueMessage(String subscribeTopic, MqttMessage message);

	/**
	 * Load lại bản tin lưu trong log khi kết nối đến tabase bị mất
	 */
	void startLoadMessageLog();
	
	/**
	 * Khai báo sự kiện hệ thống
	 */
	void setSystemEventCallBack(SystemEventCallback systemEventCallback);

}
