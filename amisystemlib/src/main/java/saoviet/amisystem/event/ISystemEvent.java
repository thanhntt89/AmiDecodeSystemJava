/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 *  Class Name: ISystemEvent.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.event;

public interface ISystemEvent {
	// Định nghĩa phương thức sự kiện sẽ trả về cho class cha
	public void setSystemEventCallBack(SystemEventCallback systemEventCallback);
}
