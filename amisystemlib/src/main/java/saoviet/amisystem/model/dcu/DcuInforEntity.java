/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: DcuInforEntity.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.dcu;

import java.math.BigDecimal;

public class DcuInforEntity {

	public String getDcuCode() {
		return this.dcuCode;
	}

	public void setDcuCode(String dcuCode) {
		this.dcuCode = dcuCode;
	}

	public String getProtocolVerSion() {
		return protocolVerSion;
	}

	public void setProtocolVerSion(String protocolVerSion) {
		this.protocolVerSion = protocolVerSion;
	}

	public String getDcuVerSion() {
		return this.dcuVerSion;
	}

	public void setDcuVerSion(String dcuVerSion) {
		this.dcuVerSion = dcuVerSion;
	}

	public String getSvnVersion() {
		return this.svnVersion;
	}

	public void setSvnVersion(String svnVersion) {
		this.svnVersion = svnVersion;
	}

	public String getSingal() {
		return this.singal;
	}

	
	public String getSimSerial() {
		return simSerial;
	}

	public void setSimSerial(String simSerial) {
		this.simSerial = simSerial;
	}

	public void setSingal(String singal) {
		this.singal = singal;
	}

	public BigDecimal getSimTemperature() {
		return this.simTemperature;
	}

	public void setSimTemperature(BigDecimal simTemperature) {
		this.simTemperature = simTemperature;
	}

	public BigDecimal getMcuTemperature() {
		return this.mcuTemperature;
	}

	public void setMcuTemperature(BigDecimal mcuTemperature) {
		this.mcuTemperature = mcuTemperature;
	}

	public BigDecimal getRtcPinVotage() {
		return this.rtcPinVotage;
	}

	public void setRtcPinVotage(BigDecimal rtcPinVotage) {
		this.rtcPinVotage = rtcPinVotage;
	}

	public String getCellIdConnect() {
		return this.cellIdConnect;
	}

	public void setCellIdConnect(String cellIdConnect) {
		this.cellIdConnect = cellIdConnect;
	}

	private String dcuCode;
	private String protocolVerSion;
	private String dcuVerSion;
	private String svnVersion;
	private String singal;
	private BigDecimal simTemperature;
	private BigDecimal mcuTemperature;
	private BigDecimal rtcPinVotage;
	private String cellIdConnect;
	private String simSerial;
}
