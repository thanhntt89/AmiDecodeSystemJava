/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 *  Class Name: IDcuBusiness.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-05-07 15:47:06
 */

package saoviet.amisystem.business;

public interface IDcuBusiness {

	// Offline all dcu by gatewayIP
	void updateDcuOfflineStatusByGatewayIp(String gatewayIp);

	// Start thread checking Meter status use for 1 phase
	void startThreadCheckingMeterStatus();

	/***Update dcu status** */ 
	void updateDcuStatus(String dcuCode, int status, String gatewayIp, String activeId);
}
