/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: ConnectionViettelDcuManagement.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-06-06 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.connectionmanagement;

import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import saoviet.amisystem.event.ConnectionStatusEventCallback;
import saoviet.amisystem.messagemanagement.IMessageQueueManagement;
import saoviet.amisystem.messagemanagement.ViettelDcuMessageQueueManager;
import saoviet.amisystem.model.SystemConfig;
import saoviet.amisystem.ulti.FileUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class ConnectionViettelDcuManagement implements IConnectionManagement {

	private ConnectionStatusEventCallback connectionStatusEventCallback;
	private LogUlti logUlti = new LogUlti(ConnectionViettelDcuManagement.class);
	// Default config value
	private int keepAlive = 15;
	private String[] subscribeTopic = { "Test/#" };
	private int[] qos = { 2 };
	private String broker = "tcp://124.158.5.154:1883";
	private String username = "admin";
	private char[] password = "admin".toCharArray();
	private String clientId = "Java-client-id";
	private boolean isCleanSession = false;
	private boolean isUnSubcribeAffterDisconnect = false;
	private MqttClient client;
	private MemoryPersistence persistence;
	private MqttConnectOptions options;
	private IMessageQueueManagement iMessageQueueManagement;
	private SystemConfig sys;
	private IProperties iProperties;
	
	public ConnectionViettelDcuManagement() {
		System.out.println("Init ConnectionManagement ...");
		this.loadSystemConfig();
		this.iProperties = new IProperties();
		this.iMessageQueueManagement = this.iMessageQueueManagement();
		this.iMessageQueueManagement.setSystemEventCallBack(this);
		this.persistence = new MemoryPersistence();
		this.options = new MqttConnectOptions();
		this.options.setCleanSession(isCleanSession);
		this.options.setUserName(username);
		this.options.setPassword(password);
		this.options.setKeepAliveInterval(keepAlive);

		try {
			this.client = new MqttClient(this.broker, this.clientId, this.persistence);
			// Ham su kien, nhan ban tin tu broker gui ve
			this.client.setCallback(this);

		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost");
		// TODO Auto-generated method stub
		logUlti.writeLog(LogType.INFO, (new Date()) + " - Connection Lost");

		while (!this.client.isConnected()) {
			try {

				// Waiting for 2 minutes to reconnect
				Thread.sleep(this.sys.getTimeAutoReconnect() * 60000);
				System.out.println("Try reconnect");
				this.connect();

			} catch (InterruptedException e) {
				System.out.println("Erro try reconnect");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		logUlti.writeLog(LogType.INFO, (new Date()) + " - Reconnected");
	}

	@Override
	public void messageArrived(String subscribeTopic, MqttMessage message) throws Exception {
		this.iMessageQueueManagement.enQueueMessage(subscribeTopic, message);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		logUlti.writeLog(LogType.INFO, (new Date()) + " - Connection complete token:" + token.getMessageId());
	}

	@Override
	public void sendData(String topics, byte[] data) {
		if (this.client != null && this.client.isConnected()) {
			try {
				this.client.publish(topics, data, qos[0], false);
				logUlti.writeLog(LogType.INFO, "Public topic: " + topics);
			} catch (MqttPersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logUlti.writeLog(LogType.ERROR, "ERROR public topic: " + topics, e);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logUlti.writeLog(LogType.ERROR, "ERROR public topic: " + topics, e);
			}
		}
	}

	@Override
	public void sqlDisconnect() {
		this.iProperties.setClientClosedByUserStatus(false);
		// TODO Auto-generated method stub
		this.disconnect();
	}

	@Override
	public void printData(String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConnectionStatusEventCallback(ConnectionStatusEventCallback connectionStatusEventCallback) {
		// TODO Auto-generated method stub
		this.connectionStatusEventCallback = connectionStatusEventCallback;
	}

	@Override
	public IProperties properties() {
		return this.iProperties;
	}

	@Override
	public IMessageQueueManagement iMessageQueueManagement() {
		return new ViettelDcuMessageQueueManager(this.sys);
	}

	@Override
	public void connect() {
		try {
			this.iMessageQueueManagement.startThreadDequeue();
			this.client.connect(this.options);
			this.client.subscribe(this.subscribeTopic, this.qos);

			System.out.println("Connected");

			logUlti.writeLog(LogType.INFO, "Connect to broker:" + broker);
			logUlti.writeLog(LogType.INFO, "Client name:" + sys.getClientId());
			logUlti.writeLog(LogType.INFO, "Client name:" + sys.getMqttTopics());

			if (this.connectionStatusEventCallback != null)
				this.connectionStatusEventCallback.ConnectionStatus(true, "Connected");

			this.iProperties.setConnectedStatus(true);
		} catch (MqttException me) {			
			System.out.println("Connect fail");
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();

			logUlti.writeLog(LogType.ERROR, "Connect to broker", me);

			this.iProperties.setClientClosedByUserStatus(false);
			if (this.connectionStatusEventCallback != null)
				this.connectionStatusEventCallback.ConnectionStatus(false, "Error Connect");
			this.iProperties.setConnectedStatus(false);
			
		}
	}

	@Override
	public Boolean disconnect() {
		if (!this.client.isConnected())
			return true;
		// TODO Auto-generated method stub
		try {
			
			if(this.isUnSubcribeAffterDisconnect)
				this.unSubcriber();
			
			this.client.disconnect();

			this.iProperties.setClientClosedByUserStatus(true);

			if (this.connectionStatusEventCallback != null)
				this.connectionStatusEventCallback.ConnectionStatus(false, "Disconnected");
			System.out.println("Disconnect");
			return true;
		} catch (MqttException ex) {
			System.out.println("Error disconnect");
			if (this.connectionStatusEventCallback != null)
				this.connectionStatusEventCallback.ConnectionStatus(true, "Error disconnect");
		}
		return false;
	}

	@Override
	public Boolean existThreadDecode() {
		// Stop connecting to gateway frist
				if (!this.disconnect())
					return false;

				// Stop dequeuing
				if (!this.iMessageQueueManagement.stopThreadDequeue())
					return false;

				// TODO Auto-generated method stub
				logUlti.writeLog(LogType.INFO, "System thread main stop");
				return true;
	}

	@Override
	public void unSubcriber() {
		// TODO Auto-generated method stub
				try {
					if (this.client.isConnected())
						this.client.unsubscribe(subscribeTopic);
					logUlti.writeLog(LogType.INFO, "Unscriber:" + sys.getMqttTopics());
					System.out.println("Unsubribe subscribeTopic: " + subscribeTopic);
				} catch (MqttException ex) {
					this.iProperties.setClientClosedByUserStatus(client.isConnected());
					logUlti.writeLog(LogType.ERROR, "Unscriber error.", ex);
				}
	}

	@Override
	public void loadSystemConfig() {
		this.sys = FileUlti.getSystemConfig();
		this.keepAlive = this.sys.getMqttKeepAlive();
		this.broker = "tcp://" + this.sys.getMqttGatwayAddress() + ":" + this.sys.getMqttPort();
		this.username = this.sys.getMqttUserName();
		this.password = this.sys.getMqttPassword().toCharArray();
		this.clientId = this.sys.getClientId();
		this.isCleanSession = this.sys.getCleanSession() == 1 ? true : false;
		this.subscribeTopic = this.sys.getMqttTopics().split(";");
		this.isUnSubcribeAffterDisconnect = this.sys.getUnSubcribeAffterDisconnect() == 1 ? true : false;
		this.qos = new int[this.subscribeTopic.length];
		for (int i = 0; i < this.subscribeTopic.length; i++) {
			this.qos[i] = this.sys.getMqttQoSSubcribe();
		}
	}

}
