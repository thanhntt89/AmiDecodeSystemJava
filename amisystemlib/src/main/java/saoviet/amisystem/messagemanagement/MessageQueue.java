/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: MessageQueue.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.messagemanagement;

import java.util.concurrent.LinkedBlockingQueue;

import saoviet.amisystem.model.MessageBase;

public class MessageQueue {
	private LinkedBlockingQueue<MessageBase> messageQueue;// LinkedBlockingQueue

	/*
	 * Hàm tạo của lớp xử lý hàng đợi
	 */
	public MessageQueue() {
		this.messageQueue = new LinkedBlockingQueue<MessageBase>();
	}

	/*
	 * Xử lý lấy dữ liệu ra khỏi hàng đợi
	 */
	public MessageBase deQueue() {
		MessageBase mess = null;
		try {
			mess = this.messageQueue.take();			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // or poll
		return mess;
	}

	/*
	 * Xử lý đưa dữ liệu vào hàng đợi
	 */
	public void enQueue(MessageBase messageBase) {
		try {
			this.messageQueue.put(messageBase);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Xử lý lấy kích thước hàng đợi
	 */
	public int getSize() {
		return this.messageQueue.size();
	}
}
