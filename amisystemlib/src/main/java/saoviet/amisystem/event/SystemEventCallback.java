/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 *  Class Name: SystemEventCallback.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.event;

public interface SystemEventCallback {
	// Gửi dữ liệu xuống cho gateway
	public void sendData(String topics, byte[] data);
	public void sqlDisconnect();
	public void printData(String data);
}
