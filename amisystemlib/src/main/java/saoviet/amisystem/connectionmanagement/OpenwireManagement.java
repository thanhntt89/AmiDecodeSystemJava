/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 *  Class Name: OpenwireManagement.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-05-07 15:47:06
 */

package saoviet.amisystem.connectionmanagement;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.RemoveInfo;

import saoviet.amisystem.business.DcuBusiness;
import saoviet.amisystem.business.IDcuBusiness;
import saoviet.amisystem.event.ConnectionStatusEventCallback;
import saoviet.amisystem.model.SystemConfig;
import saoviet.amisystem.ulti.FileUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class OpenwireManagement implements MessageListener, IOpenwireManagement, Runnable {

	private ConnectionStatusEventCallback connectionStatusEventCallback;

	private MessageConsumer consumerAdvisory;
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private SystemConfig sysconfig;
	private IDcuBusiness iDcuBusiness;
	private Thread threadAutoUpdateMeterConnectionStatus;
	private boolean isRunning = false;
	private LogUlti logUlti = new LogUlti(OpenwireManagement.class);

	public OpenwireManagement() {
		System.out.println("Load config");
		this.iDcuBusiness = new DcuBusiness();
		this.loadSystemConfig();
	}

	private void loadSystemConfig() {
		this.sysconfig = FileUlti.getSystemConfig();

		// Create a connection factory referring to the broker host and port
		this.connectionFactory = new ActiveMQConnectionFactory(this.sysconfig.getMqttUserName(),
				this.sysconfig.getMqttPassword(),
				"tcp://" + this.sysconfig.getMqttGatwayAddress() + ":" + this.sysconfig.getOpenwirePort());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see saoviet.amisystem.connectionmanagement.IOpenwireManagement#connect()
	 */
	@Override
	public void connect() {
		try {
			if (this.connection == null) {
				this.connection = this.connectionFactory.createConnection();
				this.connection.setClientID(this.sysconfig.getClientId());
				this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			}

			this.connection.start();
			Destination destinationAdvisory = this.session.createTopic(this.sysconfig.getOpenwireTopics());
			this.consumerAdvisory = this.session.createConsumer(destinationAdvisory);

			// Event receive message
			this.consumerAdvisory.setMessageListener(this);
			logUlti.writeLog(LogType.INFO, "OPENWIRE CONNECTED");
			if (this.connectionStatusEventCallback != null)
				this.connectionStatusEventCallback.ConnectionStatus(true, "CONNECTED");
			System.out.print("\nAPPLICATION IS STARTED");

		} catch (Exception ex) {
			if (this.connectionStatusEventCallback != null)
				this.connectionStatusEventCallback.ConnectionStatus(false, "ERROR CONNECT: " + ex.getMessage());
			logUlti.writeLog(LogType.ERROR, "Connect openwire error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.connectionmanagement.IOpenwireManagement#disconnect()
	 */
	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		try {
			this.connection.stop();
			if (this.connectionStatusEventCallback != null)
				this.connectionStatusEventCallback.ConnectionStatus(false, "STOP");
			logUlti.writeLog(LogType.INFO, "OPENWIRE STOP");
		} catch (JMSException ex) {
			logUlti.writeLog(LogType.ERROR, "ERROR OPENWIRE STOP: " + ex.getMessage());
			// TODO Auto-generated catch block
			ex.printStackTrace();
			if (this.connectionStatusEventCallback != null)
				this.connectionStatusEventCallback.ConnectionStatus(false, "ERROR DISCONNECT");
		}
	}

	/*
	 * Method receive messages come
	 */
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		if (message instanceof ActiveMQMessage) {
			ActiveMQMessage activeMessage = (ActiveMQMessage) message;
			Object command = activeMessage.getDataStructure();
			if (command instanceof ConsumerInfo) {
				System.out.println("A consumer subscribed to a topic or queue: " + command + "\n");
			} else if (command instanceof RemoveInfo) {
				RemoveInfo removeInfo = (RemoveInfo) command;
				if (removeInfo.isConsumerRemove()) {
					System.out.println("A consumer unsubscribed from a topic or queue\n");
				} else {
					this.iDcuBusiness.updateDcuStatus("", 0, this.sysconfig.getMqttGatwayAddress(),
							((RemoveInfo) command).getObjectId().toString());
				}
			} else if (command instanceof ConnectionInfo) {
				this.iDcuBusiness.updateDcuStatus(((ConnectionInfo) command).getClientId(), 1,
						this.sysconfig.getMqttGatwayAddress(), ((ConnectionInfo) command).getConnectionId().toString());
			} else {
				System.out.println("Unknown command: " + command + "\n");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see saoviet.amisystem.connectionmanagement.IOpenwireManagement#
	 * StartThreadCheckingDCUStatus()
	 */
	@Override
	public boolean isStartThreadCheckingDCUStatus() {
		// TODO Auto-generated method stub
		this.isRunning = true;
		this.threadAutoUpdateMeterConnectionStatus = new Thread(this, "ThreadAutoUpdateMeterConnectionStatus");
		this.threadAutoUpdateMeterConnectionStatus.start();
		return true;
	}

	/*
	 * Thread xử lý tự động cập nhật trạng thái kết nối của đồng hồ sau khoảng
	 * thời gian định sẵn (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (this.isRunning) {
			try {
				synchronized (this) {
					this.wait(1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("STRART THREAD: ThreadAutoUpdateMeterConnectionStatus");
		}
	}

	@Override
	public void setConnectionStatusEventCallback(ConnectionStatusEventCallback connectionStatusEventCallback) {
		// TODO Auto-generated method stub
		this.connectionStatusEventCallback = connectionStatusEventCallback;
	}

}
