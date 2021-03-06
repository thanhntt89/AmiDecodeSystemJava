/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: MessageQueueManager.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.messagemanagement;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.SystemConfig;
import saoviet.amisystem.model.meter.threephase.decode.IThreePhaseDecode;
import saoviet.amisystem.model.meter.threephase.decode.ThreePhaseDecode;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;
import saoviet.amisystem.ulti.SaveMessageUlti;
import saoviet.amisystem.ulti.SessionEntity;

public class MessageQueueManager implements IMessageQueueManagement {

	private LogUlti logUlti = new LogUlti(MessageQueueManager.class);
	private SystemEventCallback systemEventCallback;
	private Thread decodeThread;
	private Thread loadMessageLogThread;
	private String threadName;
	private Boolean isDecode = false;
	private boolean isAllThreadStop = false;
	private SystemConfig sys;

	// Thread decode parameter
	private int corePoolSize;
	private int maxPoolSize;
	private long keepAlive;
	private TimeUnit unit;

	private MessageQueue messagequeue;
	// private IOnePhaseDecode iOnePhaseDecode;
	private IThreePhaseDecode iThreePhaseDecode;

	private boolean isSqlDisconnected = false;

	// Queue logmessage
	private BlockingQueue<Runnable> workQueueLogMessage;
	private RejectedExecutionHandler rejectedExecutionHandlerLogMessage;
	// Creating the ThreadPoolExecutor
	private ThreadPoolExecutor threadPoolExecutorLogMessage;

	/*
	 * H??m t???o c???a class MessageQueueManager
	 */
	/**
	 * @param sys
	 */
	public MessageQueueManager(SystemConfig sys) {
		this.sys = sys;
		this.corePoolSize = this.sys.getMinThreadNum();
		this.maxPoolSize = this.sys.getMaxThreadNum();
		this.keepAlive = this.sys.getThreadKeepAlive();
		this.unit = TimeUnit.SECONDS;

		this.workQueueLogMessage = new ArrayBlockingQueue<Runnable>(corePoolSize, true);
		this.rejectedExecutionHandlerLogMessage = new ThreadPoolExecutor.CallerRunsPolicy();
		this.threadPoolExecutorLogMessage = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAlive, unit,
				workQueueLogMessage, rejectedExecutionHandlerLogMessage);

		this.threadName = "DecodeMessageQueue";

		/*
		 * this.iOnePhaseDecode = new OnePhaseDecode();
		 * this.iOnePhaseDecode.setSystemEventCallBack(this);
		 */

		this.iThreePhaseDecode = new ThreePhaseDecode();

		this.messagequeue = new MessageQueue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see saoviet.amisystem.messagemanagement.IMessageQueueManagement#
	 * setSystemEventCallBack(saoviet.amisystem.event.SystemEventCallback)
	 */
	// S??? ki???n khi c?? th??ng s??? thay ?????i
	@Override
	public void setSystemEventCallBack(SystemEventCallback systemEventCallback) {
		// TODO Auto-generated method stub
		this.systemEventCallback = systemEventCallback;
		this.iThreePhaseDecode.setSystemEventCallBack(this);
	}

	/*
	 * Chi ti???t: saoviet.amisystem.messagemanagement#startThreadDequeue()
	 */
	@Override
	public void startThreadDequeue() {
		if (this.decodeThread == null) {
			this.decodeThread = new Thread(this, threadName);
			this.decodeThread.start();
		} else if (!this.isDecode) {
			this.decodeThread.start();
		}
		
		// Start thread autoinsert databae
		if(this.iThreePhaseDecode!=null)
			this.iThreePhaseDecode.StartThreadAutoInsertDatabase();

		// Load data from message log
		if (this.sys.isLoadMessage())
			this.startLoadMessageLog();
	}

	/**
	 * Ch???y thread load b???n tin ???? l??u ch??a x??? l?? trong database ????a v??o h??ng ?????i
	 */
	public void startLoadMessageLog() {
		if (this.loadMessageLogThread == null) {
			this.loadMessageLogThread = new Thread(new GetMessageLog(this.messagequeue), "LoadMessageThread");
			this.loadMessageLogThread.start();
		}
	}

	/*
	 * Chi ti???t: saoviet.amisystem.messagemanagement#stopThreadDequeue()
	 */
	@Override
	public Boolean stopThreadDequeue() {
		// Tr?????ng h???p ??ang c??n d??? li???u ??? queue th?? ch??? cho ?????n khi x??? l?? h???t ????
		// sau ???? m???i k???t th??c x??? l??
		if (this.messagequeue.getSize() > 0)
			return false;

		this.isAllThreadStop = this.iThreePhaseDecode.StopThreadAutoInsertDatabase();

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

	/*
	 * Chi ti???t: saoviet.amisystem.messagemanagement#enQueuemessage(String
	 * subscribeTopic, MqttMessage message)
	 */
	@Override
	public void enQueueMessage(String subscribeTopic, MqttMessage message) {
		// Holding enqueue
		MessageBase messageBase = new MessageBase();
		messageBase.setPreTopic(subscribeTopic);
		messageBase.setData(message.getPayload());
		this.messagequeue.enQueue(messageBase);

		if (this.sys.isSaveMessage())
			// Backup data
			this.threadPoolExecutorLogMessage
					.execute(new SaveMessageUlti(subscribeTopic, ConvertUlti.toHexString(messageBase.getData())));
		messageBase = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Started thread dequeue: " + threadName);

		logUlti.writeLog(LogType.INFO, "RUN THREAD DEQUEUE: " + threadName);
		this.isDecode = true;
		this.isSqlDisconnected = false;
		ExecutorService service = Executors.newCachedThreadPool();
		
		while (this.isDecode) {
			try {

				// Check sql disconnect break system
				if (this.isSqlDisconnected) {
					break;
				}

				MessageBase msg = this.messagequeue.deQueue();

				String[] topic = msg.getPreTopic().split("/");

				if (topic.length > 5) {
					msg.setType(topic[1]);
					msg.setMeterType(topic[2]);
					msg.setMessageType(topic[3]);
					msg.setDcuCode(topic[4]);
					msg.setMeterCode(topic[5]);

					if (msg.getMeterCode().contains("MT"))
						continue;

					switch (msg.getMessageType()) {
					case Constant.SAO_VIET_MESSAGE_TYPE_MEPDV:
					case Constant.SAO_VIET_MESSAGE_TYPE_DCU_WARNING:
					case Constant.SAO_VIET_MESSAGE_TYPE_HISTORICAL:
					case Constant.SAO_VIET_MESSAGE_TYPE_INTANTANEOUS:
					case Constant.SAO_VIET_MESSAGE_TYPE_LOAPROFILE:
					case Constant.SAO_VIET_MESSAGE_TYPE_METER_EVENT:
					case Constant.SAO_VIET_MESSAGE_TYPE_OPERATION:
						service.execute(new ProcessMessage(this.iThreePhaseDecode, msg));
						break;
					default:
						msg = null;
						break;
					}
				}
				topic = null;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		service.shutdown();

		// ?????i cho ?????n khi c??c thread k???t th??c m???i th???c hi???n d???ng thread main
		while (!service.isTerminated()) {
			this.isAllThreadStop = false;
		}

		service = null;
		this.isAllThreadStop = true;

		logUlti.writeLog(LogType.INFO, "STOP THREAD DEQUEUE: " + threadName);
		System.out.println("Stopped thread dequeue: " + threadName);
		System.out.println("Finished all threads");
	}

	@Override
	public void sqlDisconnect() {
		System.out.println("MessageQueueManager: sqlDisconnect");
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
	public void sendData(String topics, byte[] data) {
		// TODO Auto-generated method stub
		if (this.systemEventCallback != null)
			this.systemEventCallback.sendData(topics, data);
	}

	@Override
	public void printData(String data) {
		// TODO Auto-generated method stub
		if (this.systemEventCallback != null)
			this.systemEventCallback.printData(data);
	}
}
