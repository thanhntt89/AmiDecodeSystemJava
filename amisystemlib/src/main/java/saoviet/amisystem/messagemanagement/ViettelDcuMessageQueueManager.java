/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: ViettelDcuMessageQueueManager.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-06-06 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.messagemanagement;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.SystemConfig;
import saoviet.amisystem.model.meter.threephase.vietteldcudecode.IThreePhaseViettelDcuDecode;
import saoviet.amisystem.model.meter.threephase.vietteldcudecode.ProcessMessageViettelDcu;
import saoviet.amisystem.model.meter.threephase.vietteldcudecode.ThreePhaseViettelDcuDecode;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.SaveMessageUlti;
import saoviet.amisystem.ulti.SessionEntity;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class ViettelDcuMessageQueueManager implements IMessageQueueManagement {

	private SystemEventCallback systemEventCallback;
	private Thread decodeThread;
	private Thread loadMessageLogThread;
	private String threadName;
	private Boolean isDecode = false;
	private boolean isAllThreadStop = false;
	private SystemConfig sys;
	private LogUlti logUlti = new LogUlti(ViettelDcuMessageQueueManager.class);
	// Creating the ThreadPoolExecutor
	private ThreadPoolExecutor threadPoolExecutorLogMessage;
	// Thread decode parameter
	private int corePoolSize;
	private int maxPoolSize;
	private long keepAlive;
	private TimeUnit unit;
	// Queue logmessage
	private BlockingQueue<Runnable> workQueueLogMessage;
	private RejectedExecutionHandler rejectedExecutionHandlerLogMessage;

	private MessageQueue messagevietteldcuqueue;
	// private IOnePhaseDecode iOnePhaseDecode;
	private IThreePhaseViettelDcuDecode iThreePhaseViettelDcuDecode;

	private boolean isSqlDisconnected = false;

	public ViettelDcuMessageQueueManager(SystemConfig sys) {
		this.sys = sys;

		this.corePoolSize = this.sys.getMinThreadNum();
		this.maxPoolSize = this.sys.getMaxThreadNum();
		this.keepAlive = this.sys.getThreadKeepAlive();
		this.unit = TimeUnit.SECONDS;

		this.workQueueLogMessage = new ArrayBlockingQueue<Runnable>(corePoolSize, true);
		this.rejectedExecutionHandlerLogMessage = new ThreadPoolExecutor.CallerRunsPolicy();
		this.threadPoolExecutorLogMessage = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAlive, unit,
				workQueueLogMessage, rejectedExecutionHandlerLogMessage);

		this.threadName = "DecodeMessageViettelDcuQueue";

		this.iThreePhaseViettelDcuDecode = new ThreePhaseViettelDcuDecode();

		this.messagevietteldcuqueue = new MessageQueue();
	}

	@Override
	public void run() {
		logUlti.writeLog(LogType.INFO, "RUN THREAD DEQUEUE: " + threadName);
		this.isDecode = true;
		this.isSqlDisconnected = false;

		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(this.corePoolSize, true);
		RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
		// Creating the ThreadPoolExecutor
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize,
				this.keepAlive, unit, workQueue, rejectedExecutionHandler);

		// Run main thread dequeue
		while (this.isDecode) {

			// Dequeue
			MessageBase messageOut = this.messagevietteldcuqueue.deQueue();
			if (!this.isSqlDisconnected) {
				threadPoolExecutor.execute(new ProcessMessageViettelDcu(this.iThreePhaseViettelDcuDecode, messageOut));
			} else {
				this.isDecode = false;
				break;
			}
		}

		threadPoolExecutor.shutdown();
		// Đợi cho đến khi các thread kết thúc mới thực hiện dừng thread main
		while (!threadPoolExecutor.isTerminated()) {
			this.isAllThreadStop = false;
		}

		this.isAllThreadStop = true;

		logUlti.writeLog(LogType.INFO, "STOP THREAD DEQUEUE: " + threadName);
		System.out.println("Stopped thread: " + threadName);
		System.out.println("Finished all threads");
	}

	@Override
	public void sendData(String topics, byte[] data) {
		// TODO Auto-generated method stub
		if (this.systemEventCallback != null)
			this.systemEventCallback.sendData(topics, data);
	}

	@Override
	public void sqlDisconnect() {
		System.out.println("MessageViettelDcuQueueManager: sqlDisconnect");
		// TODO Auto-generated method stub
		this.isSqlDisconnected = true;
		try {
			SessionEntity.setSqlDisconnect(true);
			this.loadMessageLogThread.join();
			this.loadMessageLogThread.interrupt();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// this.isDecode = false;
		if (this.systemEventCallback != null)
			this.systemEventCallback.sqlDisconnect();
	}

	@Override
	public void printData(String data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startThreadDequeue() {
		// TODO Auto-generated method stub
		System.out.println("Starting: " + threadName);
		if (this.decodeThread == null) {
			this.decodeThread = new Thread(this, threadName);
			this.decodeThread.start();
		} else if (!this.isDecode) {
			this.decodeThread.start();
		}
		// Load data from message log
		if (this.sys.isLoadMessage())
			this.startLoadMessageLog();
	}

	@Override
	public Boolean stopThreadDequeue() {
		// Trường hợp đang còn dữ liệu ở queue thì chờ cho đến khi xử lý hết đã
		// sau đó mới kết thúc xử lý
		if (this.messagevietteldcuqueue.getSize() > 0)
			return false;

		// Stop decode main thread
		this.isDecode = false;

		try {
			this.decodeThread.join(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		// Stop threading load message log
		if (this.loadMessageLogThread != null)
			try {
				SessionEntity.setSqlDisconnect(true);
				this.loadMessageLogThread.join();
				this.loadMessageLogThread.interrupt();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		// Wait for all thread stop
		return this.isAllThreadStop;
	}

	@Override
	public void enQueueMessage(String subscribeTopic, MqttMessage message) {
		// Holding enqueue
		MessageBase messageBase = new MessageBase();
		messageBase.setPreTopic(subscribeTopic);
		messageBase.setData(message.getPayload());
		this.messagevietteldcuqueue.enQueue(messageBase);
		
		// Save message
		if (this.sys.isSaveMessage())
			// Backup data
			this.threadPoolExecutorLogMessage
					.execute(new SaveMessageUlti(subscribeTopic, ConvertUlti.toHexString(messageBase.getData())));
	}

	@Override
	public void startLoadMessageLog() {
		if (this.loadMessageLogThread == null) {
			this.loadMessageLogThread = new Thread(new GetMessageLog(this.messagevietteldcuqueue), "LoadMessageThread");
			this.loadMessageLogThread.start();
		}
	}

	@Override
	public void setSystemEventCallBack(SystemEventCallback systemEventCallback) {
		// TODO Auto-generated method stub
		this.systemEventCallback = systemEventCallback;
		this.iThreePhaseViettelDcuDecode.setSystemEventCallBack(this);
	}

}
