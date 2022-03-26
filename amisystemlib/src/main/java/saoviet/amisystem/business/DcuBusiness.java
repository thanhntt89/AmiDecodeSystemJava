/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 *  Class Name: DcuBusiness.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-05-07 15:47:06
 */

package saoviet.amisystem.business;

import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.sqlhelper.DatabaseConnection;
import saoviet.amisystem.sqlhelper.SqlHelper;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;

public class DcuBusiness implements IDcuBusiness {

	private LogUlti logUlti = new LogUlti(DcuBusiness.class);
	private SystemEventCallback systemEventCallback;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.business.IDcuBusiness#startThreadCheckingMeterStatus()
	 */
	@Override
	public void startThreadCheckingMeterStatus() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.business.IDcuBusiness#updateDcuOfflineStatusByGatewayIp
	 * (java.lang.String)
	 */
	@Override
	public void updateDcuOfflineStatusByGatewayIp(String gatewayIp) {
		// TODO Auto-generated method stub
		Object[] obj = new Object[1];
		obj[0] = gatewayIp;
		try {
			SqlHelper.ExecuteNoneQuery(DatabaseConnection.getSqlConnection(), "update_DcuConnectionStatusOfflineAll",
					obj);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "Error UpdateDcuOfflineStatusAll: ", ex);

			if (ex.getMessage().contains("Cannot open database")) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
				return;
			}

		}
		
		// execute xong ko dong connection nay vao
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.business.IDcuBusiness#updateDcuStatus(java.lang.String,
	 * int, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateDcuStatus(String dcuCode, int status, String gatewayIp, String activeId) {
		// TODO Auto-generated method stub
		Object[] obj = new Object[4];
		obj[0] = dcuCode;
		obj[1] = status;
		obj[2] = gatewayIp;
		obj[3] = activeId;

		try {
			SqlHelper.ExecuteNoneQuery(DatabaseConnection.getSqlConnection(), "UPD_DCU_CONNECTION_STATUS", obj);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "ERROR UPD_DCU_CONNECTION_STATUS: " + ex);

			if (ex.getMessage().contains(Constant.SQL_ERROR_CONNECTION)) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}
		}
	}

}
