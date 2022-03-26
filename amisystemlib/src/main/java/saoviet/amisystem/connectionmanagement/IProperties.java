/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 *  Class Name: IProperties.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.connectionmanagement;

//Các thuộc tính sử dụng trong interface kế thừa
public class IProperties {

	protected int queueMessageCount;
	protected Boolean connectedStatus;
	protected Boolean clientClosedByUserStatus;

	// Lấy về giá trị
	public int queueMessageCount() {
		return this.queueMessageCount;
	}

	public Boolean getconnectedStatus() {
		return this.connectedStatus;
	}

	public Boolean getIsClientClosedByUer() {
		return this.clientClosedByUserStatus;
	}

	// Gán giá trị cho biến
	public void setQueueMessageCount(int value) {
		this.queueMessageCount = value;
	}

	public void setConnectedStatus(boolean connectedStatus) {
		this.connectedStatus = connectedStatus;
	}

	public void setClientClosedByUserStatus(boolean status) {
		this.clientClosedByUserStatus = status;
	}
}
