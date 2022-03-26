/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: ConnectionServerTimeManagement.java
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
import saoviet.amisystem.model.SystemConfig;
import saoviet.amisystem.ulti.ByteUltil;
import saoviet.amisystem.ulti.ConvertUlti;
import saoviet.amisystem.ulti.FileUlti;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class ConnectionServerTimeManagement implements IConnectionServerTimeManagement {
	private ConnectionStatusEventCallback connectionStatusEventCallback;
	private LogUlti logUlti = new LogUlti(ConnectionServerTimeManagement.class);
	private int keepAlive = 15;
	private String[] subscribeTopic = { "Test/#" };
	private int[] qos = { 0 };
	private String broker = "tcp://124.158.5.154:1883";
	private String username = "admin";
	private char[] password = "admin".toCharArray();
	private String clientId = "Java-client-id";
	private boolean isCleanSession = false;
	private MqttClient client;
	private MemoryPersistence persistence;
	private MqttConnectOptions options;
	private IProperties iproperties;
	private SystemConfig sys;
	
	public ConnectionServerTimeManagement() {
		this.loadSystemConfig();
		this.persistence = new MemoryPersistence();
		this.options = new MqttConnectOptions();
		this.options.setCleanSession(isCleanSession);
		this.options.setUserName(username);
		this.options.setPassword(password);
		this.options.setKeepAliveInterval(keepAlive);
		this.iproperties = properties();

		try {

			this.client = new MqttClient(this.broker, this.clientId, this.persistence);

			// Ham su kien, nhan ban tin tu broker gui ve
			this.client.setCallback(this);

		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.Throwable)
	 */
	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		logUlti.writeLog(LogType.INFO, (new Date()) + " - Connection Lost");

				while (!this.client.isConnected()) {
					try {
						// Waiting for 20 seconds to reconnect
						Thread.sleep(20000);

						this.connect();

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				logUlti.writeLog(LogType.INFO, (new Date()) + " - Reconnected");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String, org.eclipse.paho.client.mqttv3.MqttMessage)
	 */
	@SuppressWarnings("deprecaiption")
	@Override
	public void messageArrived(String subscribeTopic, MqttMessage message) throws Exception {
		byte[] temp = null;
		Date timenow = new Date();
		String[] topic = subscribeTopic.split("/");
		try {
			temp = ByteUltil.Combine((byte)ConvertUlti.convertIntToIntHex(timenow.getYear()));
			temp = ByteUltil.Combine(temp, ((byte)ConvertUlti.convertIntToIntHex(timenow.getMonth() + 1)));
			temp = ByteUltil.Combine(temp, ((byte)ConvertUlti.convertIntToIntHex(timenow.getDate())));
			temp = ByteUltil.Combine(temp, ((byte)ConvertUlti.convertIntToIntHex(timenow.getDay())));
			temp = ByteUltil.Combine(temp, ((byte)ConvertUlti.convertIntToIntHex(timenow.getHours())));
			temp = ByteUltil.Combine(temp, ((byte)ConvertUlti.convertIntToIntHex(timenow.getMinutes())));
			temp = ByteUltil.Combine(temp, ((byte)ConvertUlti.convertIntToIntHex(timenow.getSeconds())));
			
			String dcuCode = topic[3]; //name dcucode
			this.sendData("AMI/" + dcuCode + "/sTime", temp);
			// Logger.WriteLog(LogLevelL4N.INFO, "Send time to DCU: " + dcuCode
			// + " Time:" + dateTime);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, " Error send time to DCU", ex);
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken)
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see saoviet.amisystem.connectionmanagement.IConnectionServerTimeManagement#sendData(java.lang.String, byte[])
	 */
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

	/*
	 * (non-Javadoc)
	 * @see saoviet.amisystem.connectionmanagement.IConnectionServerTimeManagement#setConnectionStatusEventCallback(saoviet.amisystem.event.ConnectionStatusEventCallback)
	 */
	@Override
	public void setConnectionStatusEventCallback(ConnectionStatusEventCallback connectionStatusEventCallback) {
		// TODO Auto-generated method stub
				this.connectionStatusEventCallback = connectionStatusEventCallback;
	}

	@Override
	public IProperties properties() {
		return new IProperties();
	}

	@Override
	public void connect() {
		try {
			this.client.connect(this.options);
			this.client.subscribe(this.subscribeTopic, this.qos);
			this.iproperties.setClientClosedByUserStatus(client.isConnected());
			logUlti.writeLog(LogType.INFO, "Connect to broker:" + broker);
			logUlti.writeLog(LogType.INFO, "Client name:" + sys.getClientId());
			logUlti.writeLog(LogType.INFO, "Client name:" + sys.getMqttTopics());

			if (this.connectionStatusEventCallback != null)
				this.connectionStatusEventCallback.ConnectionStatus(true, "Connected");

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();

			logUlti.writeLog(LogType.ERROR, "Connect to broker", me);

			this.iproperties.setClientClosedByUserStatus(false);
			if (this.connectionStatusEventCallback != null)
				this.connectionStatusEventCallback.ConnectionStatus(false, "Error Connect");
		}
	}

	@Override
	public Boolean disconnect() {
		if (!this.client.isConnected())
			return true;
		// TODO Auto-generated method stub
		try {
			this.client.disconnect();

			this.iproperties.setClientClosedByUserStatus(true);

			if (this.connectionStatusEventCallback != null)
				this.connectionStatusEventCallback.ConnectionStatus(false, "Disconnected");
			return true;
		} catch (MqttException ex) {
			if (this.connectionStatusEventCallback != null)
				this.connectionStatusEventCallback.ConnectionStatus(true, "Error disconnect");
		}
		return false;
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
		this.qos = new int[this.subscribeTopic.length];
		for (int i = 0; i < this.subscribeTopic.length; i++) {
			this.qos[i] = this.sys.getMqttQoSSubcribe();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void sendTimeAll() {
		byte[] temp = null;
		Date timenow = new Date();
		try {
			temp = ByteUltil.Combine((byte)ConvertUlti.convertIntToIntHex(timenow.getYear()));
			temp = ByteUltil.Combine(temp, ((byte)ConvertUlti.convertIntToIntHex(timenow.getMonth() + 1)));
			temp = ByteUltil.Combine(temp, ((byte)ConvertUlti.convertIntToIntHex(timenow.getDate())));
			temp = ByteUltil.Combine(temp, ((byte)ConvertUlti.convertIntToIntHex(timenow.getDay())));
			temp = ByteUltil.Combine(temp, ((byte)ConvertUlti.convertIntToIntHex(timenow.getHours())));
			temp = ByteUltil.Combine(temp, ((byte)ConvertUlti.convertIntToIntHex(timenow.getMinutes())));
			temp = ByteUltil.Combine(temp, ((byte)ConvertUlti.convertIntToIntHex(timenow.getSeconds())));
			
			this.sendData("AMI/sTime", temp);
			// Logger.WriteLog(LogLevelL4N.INFO, "Send time to DCU: " + dcuCode
			// + " Time:" + dateTime);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, " Error send time to DCU", ex);
		}
		
	}

}
