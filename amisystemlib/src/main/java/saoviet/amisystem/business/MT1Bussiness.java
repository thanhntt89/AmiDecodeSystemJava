/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: MT1Bussiness.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.business;

import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.meter.onephase.entity.MT1EventEntity;
import saoviet.amisystem.model.meter.onephase.entity.MT1HistoricalEntity;
import saoviet.amisystem.model.meter.onephase.entity.MT1MePDVEntity;
import saoviet.amisystem.model.meter.onephase.entity.MT1OperationEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3MeterAlert3PMessageEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3MeterAlertCollection;

public class MT1Bussiness implements IMT1Business, Runnable {

	private SystemEventCallback systemEventCallback;

	@Override
	public void setSystemEventCallback(SystemEventCallback systemEventCallback) {
		// TODO Auto-generated method stub
		this.systemEventCallback = systemEventCallback;
	}

	// Test thread sql disconnect
	Thread t;

	@Override
	public void run() {
		int i = 0;
		while (true) {
			i++;
			if (i > 3)
				break;

			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (this.systemEventCallback != null) {
			System.out.println("Stop sql  MT1Business");
			this.systemEventCallback.sqlDisconnect();
		}
	}

	public MT1Bussiness() {
		// TODO Auto-generated constructor stub
		if (this.t == null) {
			this.t = new Thread(this, "dđ");
			this.t.start();
		}
		// TODO Auto-generated constructor stub

		System.out.println("Start thread stop sql  MT1Business");
	}

	@Override
	public void insertOperation(MT1OperationEntity operationEntity) {
		// TODO Auto-generated method stub

		try {

		} catch (Exception ex) {
			if (this.systemEventCallback != null)
				this.systemEventCallback.sqlDisconnect();
		}
	}

	@Override
	public void insertIntantaneous(MT1OperationEntity intantaneous) {
		// TODO Auto-generated method stub
		try {

		} catch (Exception ex) {
			if (this.systemEventCallback != null)
				this.systemEventCallback.sqlDisconnect();
		}
	}

	@Override
	public void insertEvent(MT1EventEntity eventEntity) {
		// TODO Auto-generated method stub
		try {

		} catch (Exception ex) {
			if (this.systemEventCallback != null)
				this.systemEventCallback.sqlDisconnect();
		}
	}

	@Override
	public void insertMePDV(MT1MePDVEntity mepdv) {
		// TODO Auto-generated method stub
		try {

		} catch (Exception ex) {
			if (this.systemEventCallback != null)
				this.systemEventCallback.sqlDisconnect();
		}
	}

	@Override
	public void insertHistorical(MT1HistoricalEntity historical) {
		// TODO Auto-generated method stub
		try {

		} catch (Exception ex) {
			if (this.systemEventCallback != null)
				this.systemEventCallback.sqlDisconnect();
		}
	}

	public static void MeterAlertEnqueue(MT3MeterAlert3PMessageEntity al, MT3MeterAlertCollection meterAlertList) {
		// TODO Auto-generated method stub
		
	}

}
