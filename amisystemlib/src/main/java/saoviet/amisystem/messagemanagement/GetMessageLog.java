/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: GetMessageLog.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.messagemanagement;

import java.io.File;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.datacollection.DataTable;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.SaveMessageUlti;
import saoviet.amisystem.ulti.SessionEntity;

public class GetMessageLog implements Runnable {

	private MessageQueue messagequeue;
	private int corePoolSize;
	private int maxPoolSize;
	private long keepAlive;
	private TimeUnit unit;

	public GetMessageLog(MessageQueue messagequeue) {
		// TODO Auto-generated constructor stub
		this.messagequeue = messagequeue;
		this.corePoolSize = 20;
		this.maxPoolSize = 20;
		this.keepAlive = 500;
		this.unit = TimeUnit.SECONDS;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SaveMessageUlti save = new SaveMessageUlti();

		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(this.corePoolSize, true);
		RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
		// Creating the ThreadPoolExecutor
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize,
				this.keepAlive, unit, workQueue, rejectedExecutionHandler);

		DataTable rst = null;
		try {
			File f = new File(Constant.MESSAGE_DATABASE_FILE_PATH);
			if (!f.exists()) {
				// do something
				return;
			}
			rst = save.getMessageLog();
			int count = rst.rowCount();
			String topic = "";

			for (int index = 0; index < count; index++) {

				try {
					// Thread.sleep(1000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (SessionEntity.isSqlDisconnect()) {
					System.out.println("GETMESSAGE-STOP");
					break;
				}

				topic = (String) rst.getValue(index, "Topics");
				MessageBase messageBase = new MessageBase();
				messageBase.setPreTopic(topic);
				byte[] data = ConvertUlti.toByteArray(rst.getValue(index, "Content"));
				messageBase.setData(data);

				threadPoolExecutor.execute(new GetMessage(messageBase, this.messagequeue));
				messageBase = null;
				topic = null;
				data = null;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		threadPoolExecutor.shutdown();
		// Đợi cho đến khi các thread kết thúc mới thực hiện dừng thread main
		while (!threadPoolExecutor.isTerminated()) {
		}
	}

	public class GetMessage implements Runnable {
		private MessageBase message;
		private MessageQueue messagequeue;

		public GetMessage(MessageBase message, MessageQueue messagequeue) {
			// TODO Auto-generated constructor stub
			this.message = message;
			this.messagequeue = messagequeue;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			this.messagequeue.enQueue(this.message);
			this.message = null;
		}

	}

}
