/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: MT3Business.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/
package saoviet.amisystem.business;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.DataField;
import saoviet.amisystem.model.datacollection.DataCollection;
import saoviet.amisystem.model.datacollection.DataTable;
import saoviet.amisystem.model.dcu.DcuInforEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3EventMeterCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3EventMeterField;
import saoviet.amisystem.model.meter.threephase.entity.MT3HistoricalEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3LoadProfile3PMessageEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3MeasurementPointAlertConfigCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3MeasurementPointAlertConfigField;
import saoviet.amisystem.model.meter.threephase.entity.MT3MeterAlertCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3MeterAlertEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3OperationEntity;
import saoviet.amisystem.sqlhelper.DatabaseConnection;
import saoviet.amisystem.sqlhelper.SqlHelper;
import saoviet.amisystem.ulti.Constant;
import saoviet.amisystem.ulti.LogUlti;
import saoviet.amisystem.ulti.LogUlti.LogType;
import saoviet.amisystem.ulti.SaveMessageUlti;
import saoviet.amisystem.ulti.SessionEntity;

public class MT3Business implements IMT3Business {

	private LogUlti logUlti = new LogUlti(MT3Business.class);
	private SystemEventCallback systemEventCallback;
	// private boolean isSqlDisconnected;
	private SaveMessageUlti save = new SaveMessageUlti();

	// Trạng thái thread ghi dữ liệu vào database
	private boolean isRunning = false;
	private DataTable dtOperation = new DataTable();
	private DataTable dtHistorical = new DataTable();
	private DataTable dtLoadProfile = new DataTable();

	@Override
	public void setSystemEventCallback(SystemEventCallback systemEventCallback) {
		// TODO Auto-generated method stub
		this.systemEventCallback = systemEventCallback;
	}

	public void run() {
		InsertData();
	}

	private void InsertData() {
		this.isRunning = true;
		System.out.println("Started thread: StartThreadAutoInsert");
		logUlti.writeLog(LogType.INFO, "START THREAD INSERT DATABASE: StartThreadAutoInsert");

		// TODO Auto-generated method stub
		while (this.isRunning) {
			try {
				synchronized (this) {
					wait();
				}

				if (this.dtOperation.rowCount() > 0) {
					synchronized (this.dtOperation) {
						SqlHelper.InsertBatch(DatabaseConnection.getSqlConnection(), "INS_ORIGINAL_OPERATION3P",
								this.dtOperation);
						this.dtOperation.rowClear();
					}
				}
				if (this.dtHistorical.rowCount() > 0) {
					synchronized (this.dtHistorical) {
						SqlHelper.InsertBatch(DatabaseConnection.getSqlConnection(), "INS_ORIGINAL_HISTORICAL3P",
								this.dtHistorical);
						System.out.println("INSERT HISTORICAL BATCH:" + dtHistorical.rowCount());
						this.dtHistorical.rowClear();
					}
				}
				if (this.dtLoadProfile.rowCount() > 0) {
					synchronized (this.dtLoadProfile) {
						SqlHelper.InsertBatch(DatabaseConnection.getSqlConnection(), "INS_ORIGINAL_LOADPROFILE3P",
								this.dtLoadProfile);
						System.out.println("INSERT LOADPROFILE BATCH:" + dtLoadProfile.rowCount());
						this.dtLoadProfile.rowClear();
					}
				}
				Thread.sleep(SessionEntity.getTimeInsertData());
			} catch (Exception ex) {
				logUlti.writeLog(LogType.ERROR, "insertOperation", ex);
				if (ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
					if (this.systemEventCallback != null) {
						this.systemEventCallback.sqlDisconnect();
					}
				}
				ex.printStackTrace();
			}
		}

		System.out.println("Stop thread: StartThreadAutoInsert");
		logUlti.writeLog(LogType.INFO, "STOP THREAD INSERT DATABASE: StartThreadAutoInsert");
	}

	public boolean StopThreadAutoInsert() {
		boolean isStop = false;
		if (this.dtOperation.rowCount() == 0 && this.dtHistorical.rowCount() == 0) {
			isStop = true;
			this.isRunning = false;
		}
		return isStop;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void addMessageOperation(MT3OperationEntity operation) {
		try {
			// Làm tròn thời gian
			if (operation.getServerTime() != null) {
				Timestamp serverTime = (Timestamp) operation.getServerTime();
				int del = 0;

				if (serverTime.getMinutes() >= 29 && serverTime.getMinutes() <= 32) {
					del = Math.subtractExact((serverTime.getMinutes() * 60 + serverTime.getSeconds()), 30 * 60);
					operation.getServerTime().setMinutes(30);
					operation.getServerTime().setSeconds(0);
				} else if (serverTime.getMinutes() <= 2 && serverTime.getMinutes() >= 0) {
					del = serverTime.getMinutes() * 60 + serverTime.getSeconds();
					operation.setServerTime(new Timestamp(serverTime.getYear(), serverTime.getMonth(),
							serverTime.getDate(), serverTime.getHours(), 00, 00, 00));
				} else if (serverTime.getMinutes() == 59) {
					del = Math.subtractExact(serverTime.getSeconds(), 60);
					if (serverTime.getHours() == 23) {
						operation.getServerTime().setHours(0);
						operation.getServerTime().setMinutes(0);
						operation.getServerTime().setSeconds(0);
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(operation.getServerTime().getTime());
						cal.add(Calendar.DATE, 1);
						operation.setServerTime(new Timestamp(cal.getTime().getTime()));
					} else {
						operation.setServerTime(new Timestamp(serverTime.getYear(), serverTime.getMonth(),
								serverTime.getDate(), serverTime.getHours() + 1, 00, 00, 00));
					}
				}
				// Nếu số phút không nằm trong quy định bản tin Operation đưa
				// vào instan
				else {
					this.insertIntantaneous(operation);
					return;
				}
				if (del != 0) {
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(operation.getMeterTime().getTime());
					cal.add(Calendar.SECOND, -del);
					operation.setMeterTime(new Timestamp(cal.getTime().getTime()));
				}
			}
			// insert bản tin Operation
			this.insertOperation(operation);
			// check Warning MeasurementPoint
			if (operation.getDcuType() != null && operation.getDcuType().equals(Constant.DCU_TYPE_VIETTEL))
				this.insertMeasurementPointAlertFromOperation(operation);
			else {
				this.checkTimeDeviation(operation);
			}

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "addMessageOperation", ex);
			if (ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}
		}
	}

	private void checkTimeDeviation(MT3OperationEntity op) {
		try {
			MT3MeterAlertCollection meterAlertList = new MT3MeterAlertCollection();

			meterAlertList.setServerTime(op.getServerTime());
			meterAlertList.setMeterCode(op.getMeterId());
			meterAlertList.setDcuCode(op.getDcuCode());
			// checkTime
			long deviationValue;
			if ((op.getServerTime() != null) && (op.getMeterTime() != null)) {
				Calendar calserver = Calendar.getInstance();
				calserver.setTimeInMillis(op.getServerTime().getTime());
				long timeserver = calserver.getTimeInMillis();

				Calendar calmeter = Calendar.getInstance();
				calmeter.setTimeInMillis(op.getMeterTime().getTime());
				long timemeter = calmeter.getTimeInMillis();

				// Tính độ lệch thời gian
				deviationValue = Math.abs(timeserver - timemeter);
				deviationValue = deviationValue / 60000; // tinh so phut
				// so sanh voi 10 phut
				if (deviationValue > 10) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.TIME_DEVIATION.getValue(),
							String.valueOf(deviationValue), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.TIME_DEVIATION.getValue(),
							String.valueOf(deviationValue), Constant.MP_END_ALERT);
				}
			}

			// End decode
			if (meterAlertList != null)
				this.insertMeterAlert(meterAlertList);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "checkTimeDeviation", ex);
		}
	}

	private void insertMeasurementPointAlertFromOperation(MT3OperationEntity op) {
		try {
			MT3MeterAlertCollection meterAlertList = new MT3MeterAlertCollection();
			// Begin decode
			meterAlertList.setServerTime(op.getServerTime());
			meterAlertList.setMeterCode(op.getMeterId());
			meterAlertList.setDcuCode(op.getDcuCode());

			// checkTime
			long deviationValue;
			if ((op.getServerTime() != null) && (op.getMeterTime() != null)) {
				Calendar calserver = Calendar.getInstance();
				calserver.setTimeInMillis(op.getServerTime().getTime());
				long timeserver = calserver.getTimeInMillis();

				Calendar calmeter = Calendar.getInstance();
				calmeter.setTimeInMillis(op.getMeterTime().getTime());
				long timemeter = calmeter.getTimeInMillis();

				// Tính độ lệch thời gian
				deviationValue = Math.abs(timeserver - timemeter);
				deviationValue = deviationValue / 60000; // tinh so phut
				// so sanh voi 10 phut
				if (deviationValue > 10) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.TIME_DEVIATION.getValue(),
							String.valueOf(deviationValue), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.TIME_DEVIATION.getValue(),
							String.valueOf(deviationValue), Constant.MP_END_ALERT);
				}
			}
			// Volt A lost
			if (op.getVoltagePhaseA() == BigDecimal.valueOf(0)) {
				meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_A_LOST.getValue(),
						op.getVoltagePhaseA().toString(), Constant.MP_START_ALERT);
			} else {
				meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_A_LOST.getValue(),
						op.getVoltagePhaseA().toString(), Constant.MP_END_ALERT);
			}

			// Volt B lost
			if (op.getVoltagePhaseB() == BigDecimal.valueOf(0)) {
				meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_C_LOST.getValue(),
						op.getVoltagePhaseB().toString(), Constant.MP_START_ALERT);
			} else {
				meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_C_LOST.getValue(),
						op.getVoltagePhaseB().toString(), Constant.MP_END_ALERT);
			}

			// Volt C lost
			if (op.getVoltagePhaseC() == BigDecimal.valueOf(0)) {
				meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_C_LOST.getValue(),
						op.getVoltagePhaseC().toString(), Constant.MP_START_ALERT);
			} else {
				meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.VOLT_C_LOST.getValue(),
						op.getVoltagePhaseC().toString(), Constant.MP_END_ALERT);
			}

			if (op.getCurrentPhaseA() != BigDecimal.valueOf(0) || op.getCurrentPhaseB() != BigDecimal.valueOf(0)
					|| op.getCurrentPhaseC() != BigDecimal.valueOf(0)) {
				// Current A lost
				if (op.getCurrentPhaseA() == BigDecimal.valueOf(0)) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.CURRENT_A_LOST.getValue(),
							op.getCurrentPhaseA().toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.CURRENT_A_LOST.getValue(),
							op.getCurrentPhaseA().toString(), Constant.MP_END_ALERT);
				}

				// Current B lost
				if (op.getCurrentPhaseB() == BigDecimal.valueOf(0)) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.CURRENT_B_LOST.getValue(),
							op.getCurrentPhaseB().toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.CURRENT_B_LOST.getValue(),
							op.getCurrentPhaseB().toString(), Constant.MP_END_ALERT);
				}

				// Current C lost
				if (op.getCurrentPhaseC() == BigDecimal.valueOf(0)) {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.CURRENT_C_LOST.getValue(),
							op.getCurrentPhaseC().toString(), Constant.MP_START_ALERT);
				} else {
					meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.CURRENT_C_LOST.getValue(),
							op.getCurrentPhaseC().toString(), Constant.MP_END_ALERT);
				}
			}

			// PowerFactor
			if (op.getPowerFactorPhaseTotal() != null
					&& op.getPowerFactorPhaseTotal().compareTo(BigDecimal.valueOf(0.85)) == -1) {
				meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.POWER_FACTOR_LOWER.getValue(),
						op.getPowerFactorPhaseTotal().toString(), Constant.MP_START_ALERT);
			} else {
				meterAlertList.add((int) Constant.THREE_PHASE_ALERT_TYPE.POWER_FACTOR_LOWER.getValue(),
						op.getPowerFactorPhaseTotal().toString(), Constant.MP_END_ALERT);
			}

			// End decode
			if (meterAlertList != null)
				this.insertMeterAlert(meterAlertList);

		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "insertMeasurementPointAlertFromOperation", ex);

		}
	}

	private void insertOperation(MT3OperationEntity packet) {
		Object[] objArr = new Object[80];
		objArr[0] = packet.getServerTime();
		objArr[1] = packet.getMeterTime();
		objArr[2] = packet.getVoltagePhaseA();
		objArr[3] = packet.getVoltagePhaseB();
		objArr[4] = packet.getVoltagePhaseC();
		objArr[5] = packet.getCurrentPhaseA();
		objArr[6] = packet.getCurrentPhaseB();
		objArr[7] = packet.getCurrentPhaseC();
		objArr[8] = packet.getPhaseAnglePhaseA();
		objArr[9] = packet.getPhaseAnglePhaseB();
		objArr[10] = packet.getPhaseAnglePhaseC();
		objArr[11] = packet.getAngleofUL2subUL1();
		objArr[12] = packet.getAngleofUL1subUL3();
		objArr[13] = packet.getFrequency();
		objArr[14] = packet.getNeutralCurrent();
		objArr[15] = packet.getActivePowerPhaseA();
		objArr[16] = packet.getActivePowerPhaseB();
		objArr[17] = packet.getActivePowerPhaseC();
		objArr[18] = packet.getActivePowerPhaseTotal();
		objArr[19] = packet.getReactivePowerPhaseA();
		objArr[20] = packet.getReactivePowerPhaseB();
		objArr[21] = packet.getReactivePowerPhaseC();
		objArr[22] = packet.getReactivePowerPhaseTotal();
		objArr[23] = packet.getApparentPowerPhaseA();
		objArr[24] = packet.getApparentPowerPhaseB();
		objArr[25] = packet.getApparentPowerPhaseC();
		objArr[26] = packet.getApparentPowerPhaseTotal();
		objArr[27] = packet.getPowerFactorPhaseA();
		objArr[28] = packet.getPowerFactorPhaseB();
		objArr[29] = packet.getPowerFactorPhaseC();
		objArr[30] = packet.getPowerFactorPhaseTotal();
		objArr[31] = packet.getEnergyPlusArate1();
		objArr[32] = packet.getEnergyPlusArate2();
		objArr[33] = packet.getEnergyPlusArate3();
		objArr[34] = packet.getEnergyPlusArate4();
		objArr[35] = packet.getEnergySubArate1();
		objArr[36] = packet.getEnergySubArate2();
		objArr[37] = packet.getEnergySubArate3();
		objArr[38] = packet.getEnergySubArate4();
		objArr[39] = packet.getQ1();
		objArr[40] = packet.getQ2();
		objArr[41] = packet.getQ3();
		objArr[42] = packet.getQ4();
		objArr[43] = packet.getImportWh();
		objArr[44] = packet.getExportWh();
		objArr[45] = packet.getEnergyPlusVA();
		objArr[46] = packet.getMaxDemandPlusA();
		objArr[47] = packet.getMaxDemandSubA();
		objArr[48] = packet.getMaxDemandPlusArate1();
		objArr[49] = packet.getMaxDemandPlusArate1Time();
		objArr[50] = packet.getMaxDemandSubArate1();
		objArr[51] = packet.getMaxDemandSubArate1Time();
		objArr[52] = packet.getMaxDemandPlusArate2();
		objArr[53] = packet.getMaxDemandPlusArate2Time();
		objArr[54] = packet.getMaxDemandSubArate2();
		objArr[55] = packet.getMaxDemandSubArate2Time();
		objArr[56] = packet.getMaxDemandPlusArate3();
		objArr[57] = packet.getMaxDemandPlusArate3Time();
		objArr[58] = packet.getMaxDemandSubArate3();
		objArr[59] = packet.getMaxDemandSubArate3Time();
		objArr[60] = packet.getMaxDemandPlusArate4();
		objArr[61] = packet.getMaxDemandPlusArate4Time();
		objArr[62] = packet.getMaxDemandSubArate4();
		objArr[63] = packet.getMaxDemandSubArate4Time();
		objArr[64] = packet.getReactiveEnergyPlusArate1();
		objArr[65] = packet.getReactiveEnergyPlusArate2();
		objArr[66] = packet.getReactiveEnergyPlusArate3();
		objArr[67] = packet.getReactiveEnergySubArate1();
		objArr[68] = packet.getReactiveEnergySubArate2();
		objArr[69] = packet.getReactiveEnergySubArate3();
		objArr[70] = packet.getPhaseRotation();
		objArr[71] = packet.getTu() != null ? packet.getTu() : 1;
		objArr[72] = packet.getTi() != null ? packet.getTi() : 1;
		objArr[73] = packet.getTuT();
		objArr[74] = packet.getTiT();
		objArr[75] = packet.getTuM();
		objArr[76] = packet.getTiM();
		objArr[77] = packet.getSignal();
		objArr[78] = packet.getMeterId();
		objArr[79] = packet.getDcuCode();

		synchronized (this.dtOperation) {
			this.dtOperation.addRow(objArr);
		}
		synchronized (this) {
			notify();
		}

		try {
			// SqlHelper.ExecuteNoneQuery(DatabaseConnection.getSqlConnection(),
			// "INS_ORIGNAL_OPERATION3P", objArr);
			this.save.UpdateMessageLog(packet.getTopic(), 1);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "insertOperation", ex);
			if (ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}
			ex.printStackTrace();
		}
	}

	@Override
	public void insertIntantaneous(MT3OperationEntity packet) {
		Object[] objArr = new Object[80];

		objArr[0] = packet.getServerTime();
		objArr[1] = packet.getMeterTime();
		objArr[2] = packet.getVoltagePhaseA();
		objArr[3] = packet.getVoltagePhaseB();
		objArr[4] = packet.getVoltagePhaseC();
		objArr[5] = packet.getCurrentPhaseA();
		objArr[6] = packet.getCurrentPhaseB();
		objArr[7] = packet.getCurrentPhaseC();
		objArr[8] = packet.getPhaseAnglePhaseA();
		objArr[9] = packet.getPhaseAnglePhaseB();
		objArr[10] = packet.getPhaseAnglePhaseC();
		objArr[11] = packet.getAngleofUL2subUL1();
		objArr[12] = packet.getAngleofUL1subUL3();
		objArr[13] = packet.getFrequency();
		objArr[14] = packet.getNeutralCurrent();
		objArr[15] = packet.getActivePowerPhaseA();
		objArr[16] = packet.getActivePowerPhaseB();
		objArr[17] = packet.getActivePowerPhaseC();
		objArr[18] = packet.getActivePowerPhaseTotal();
		objArr[19] = packet.getReactivePowerPhaseA();
		objArr[20] = packet.getReactivePowerPhaseB();
		objArr[21] = packet.getReactivePowerPhaseC();
		objArr[22] = packet.getReactivePowerPhaseTotal();
		objArr[23] = packet.getApparentPowerPhaseA();
		objArr[24] = packet.getApparentPowerPhaseB();
		objArr[25] = packet.getApparentPowerPhaseC();
		objArr[26] = packet.getApparentPowerPhaseTotal();
		objArr[27] = packet.getPowerFactorPhaseA();
		objArr[28] = packet.getPowerFactorPhaseB();
		objArr[29] = packet.getPowerFactorPhaseC();
		objArr[30] = packet.getPowerFactorPhaseTotal();
		objArr[31] = packet.getEnergyPlusArate1();
		objArr[32] = packet.getEnergyPlusArate2();
		objArr[33] = packet.getEnergyPlusArate3();
		objArr[34] = packet.getEnergyPlusArate4();
		objArr[35] = packet.getEnergySubArate1();
		objArr[36] = packet.getEnergySubArate2();
		objArr[37] = packet.getEnergySubArate3();
		objArr[38] = packet.getEnergySubArate4();
		objArr[39] = packet.getQ1();
		objArr[40] = packet.getQ2();
		objArr[41] = packet.getQ3();
		objArr[42] = packet.getQ4();
		objArr[43] = packet.getImportWh();
		objArr[44] = packet.getExportWh();
		objArr[45] = packet.getEnergyPlusVA();
		objArr[46] = packet.getMaxDemandPlusA();
		objArr[47] = packet.getMaxDemandSubA();
		objArr[48] = packet.getMaxDemandPlusArate1();
		objArr[49] = packet.getMaxDemandPlusArate1Time();
		objArr[50] = packet.getMaxDemandSubArate1();
		objArr[51] = packet.getMaxDemandSubArate1Time();
		objArr[52] = packet.getMaxDemandPlusArate2();
		objArr[53] = packet.getMaxDemandPlusArate2Time();
		objArr[54] = packet.getMaxDemandSubArate2();
		objArr[55] = packet.getMaxDemandSubArate2Time();
		objArr[56] = packet.getMaxDemandPlusArate3();
		objArr[57] = packet.getMaxDemandPlusArate3Time();
		objArr[58] = packet.getMaxDemandSubArate3();
		objArr[59] = packet.getMaxDemandSubArate3Time();
		objArr[60] = packet.getMaxDemandPlusArate4();
		objArr[61] = packet.getMaxDemandPlusArate4Time();
		objArr[62] = packet.getMaxDemandSubArate4();
		objArr[63] = packet.getMaxDemandSubArate4Time();
		objArr[64] = packet.getReactiveEnergyPlusArate1();
		objArr[65] = packet.getReactiveEnergyPlusArate2();
		objArr[66] = packet.getReactiveEnergyPlusArate3();
		objArr[67] = packet.getReactiveEnergySubArate1();
		objArr[68] = packet.getReactiveEnergySubArate2();
		objArr[69] = packet.getReactiveEnergySubArate3();
		objArr[70] = packet.getPhaseRotation();
		objArr[71] = packet.getTu() != null ? packet.getTu() : 1;
		objArr[72] = packet.getTi() != null ? packet.getTi() : 1;
		objArr[73] = packet.getTuT();
		objArr[74] = packet.getTiT();
		objArr[75] = packet.getTuM();
		objArr[76] = packet.getTiM();
		objArr[77] = packet.getSignal();
		objArr[78] = packet.getMeterId();
		objArr[79] = packet.getDcuCode();

		try {
			SqlHelper.ExecuteNoneQuery(DatabaseConnection.getSqlConnection(), "INS_INSTANTANEOUS3P", objArr);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "insertIntantaneous", ex);
			if (ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}
			ex.printStackTrace();
		}
	}

	@Override
	public void insertEvent(MT3EventMeterCollection eventMeterList) {
		DataTable tbEvent = new DataTable();

		for (MT3EventMeterField event : eventMeterList.getEventMeterList()) {
			Object[] objArr = new Object[17];
			objArr[0] = event.getServerTime();
			objArr[1] = event.getMeterTime();
			objArr[2] = event.getEventName();
			objArr[3] = event.getEventCount();
			objArr[4] = event.getEventStartTime1();
			objArr[5] = event.getEventStartTime2();
			objArr[6] = event.getEventStartTime3();
			objArr[7] = event.getEventStartTime4();
			objArr[8] = event.getEventStartTime5();
			objArr[9] = event.getEventEndTime1();
			objArr[10] = event.getEventEndTime2();
			objArr[11] = event.getEventEndTime3();
			objArr[12] = event.getEventEndTime4();
			objArr[13] = event.getEventEndTime5();
			objArr[14] = event.getNotes();
			objArr[15] = event.getMeterId();
			objArr[16] = event.getDcuCode();
			tbEvent.addRow(objArr);
		}

		try {
			SqlHelper.InsertBatch(DatabaseConnection.getSqlConnection(), "INS_EVENT_METER", tbEvent);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "insertEvent", ex);
			if (ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}
		} finally {
			tbEvent = null;
		}
	}

	@Override
	public void insertHistorical(MT3HistoricalEntity packet) {
		Object[] objArr = new Object[47];
		objArr[0] = packet.getServerTime();
		objArr[1] = packet.getMeterTime();
		objArr[2] = packet.getHistoricalTime();
		objArr[3] = packet.getBeginHistoricalTime();
		objArr[4] = packet.getEndHistoricalTime();
		objArr[5] = packet.getEnergyPlusARate1();
		objArr[6] = packet.getEnergyPlusARate2();
		objArr[7] = packet.getEnergyPlusARate3();
		objArr[8] = packet.getEnergyPlusARate4();
		objArr[9] = packet.getEnergySubARate1();
		objArr[10] = packet.getEnergySubARate2();
		objArr[11] = packet.getEnergySubARate3();
		objArr[12] = packet.getEnergySubARate4();
		objArr[13] = packet.getImportWh();
		objArr[14] = packet.getExportWh();
		objArr[15] = packet.getEnergyPlusVA();
		objArr[16] = packet.getQ1();
		objArr[17] = packet.getQ2();
		objArr[18] = packet.getQ3();
		objArr[19] = packet.getQ4();
		objArr[20] = packet.getActivePowerQ1Q4Q2Q3();
		objArr[21] = packet.getReactivePowerQ1Q2Q3Q4();
		objArr[22] = packet.getTotalReactiveEnergyPositve();
		objArr[23] = packet.getMaxDemandPlusArate1();
		objArr[24] = packet.getMaxDemandPlusArate1Time();
		objArr[25] = packet.getMaxDemandSubArate1();
		objArr[26] = packet.getMaxDemandSubArate1Time();
		objArr[27] = packet.getMaxDemandPlusArate2();
		objArr[28] = packet.getMaxDemandPlusArate2Time();
		objArr[29] = packet.getMaxDemandSubArate2();
		objArr[30] = packet.getMaxDemandSubArate2Time();
		objArr[31] = packet.getMaxDemandPlusArate3();
		objArr[32] = packet.getMaxDemandPlusArate3Time();
		objArr[33] = packet.getMaxDemandSubArate3();
		objArr[34] = packet.getMaxDemandSubArate3Time();
		objArr[35] = packet.getMaxDemandPlusArate4();
		objArr[36] = packet.getMaxDemandPlusArate4Time();
		objArr[37] = packet.getMaxDemandSubArate4();
		objArr[38] = packet.getMaxDemandSubArate4Time();
		objArr[39] = packet.getReactiveEnergyPlusArate1();
		objArr[40] = packet.getReactiveEnergyPlusArate2();
		objArr[41] = packet.getReactiveEnergyPlusArate3();
		objArr[42] = packet.getReactiveEnergySubArate1();
		objArr[43] = packet.getReactiveEnergySubArate2();
		objArr[44] = packet.getReactiveEnergySubArate3();
		objArr[45] = packet.getMeterId();
		objArr[46] = packet.getDcuCode();
		synchronized (this.dtHistorical) {
			this.dtHistorical.addRow(objArr);
		}
		synchronized (this) {
			notify();
		}
		try {
			this.save.UpdateMessageLog(packet.getTopic(), 1);
			// SqlHelper.ExecuteNoneQuery(DatabaseConnection.getSqlConnection(),
			// "INS_HISTORICAL3P", objArr);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "insertHistorical", ex);
			if (ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}
		}
	}

	@Override
	public int UpdateDcuIdForMeasurementPointByDcuCodeAndMeterId(String dcuCode, String meterId, String meterType) {
		DataTable datatable = null;
		int mpstatus = -2; // Giá trị khởi tạo ngẫu nhiên
		Object[] objArr = new Object[3];

		objArr[0] = dcuCode;
		objArr[1] = meterId;
		objArr[2] = meterType;

		try {
			datatable = SqlHelper.ExecueDataset(DatabaseConnection.getSqlConnection(), "UPD_MEPDV_MP", objArr);
			if (datatable != null && datatable.rowCount() > 0)
				mpstatus = Integer.parseInt(datatable.getValue(0, 0));
			else
				System.out.println("UpdateDcuIdForMeasurementPointByDcuCodeAndMeterId TRY: " + mpstatus
						+ " - meterType:" + meterType + " - DCU:" + dcuCode + " -METER:" + meterId + "-TABLE NULL");

		} catch (Exception ex) {
			System.out.println("UpdateDcuIdForMeasurementPointByDcuCodeAndMeterId CATH: " + mpstatus + " - meterType:"
					+ meterType + " - DCU:" + dcuCode + " -METER:" + meterId);
			logUlti.writeLog(LogType.ERROR,
					"UpdateDcuIdForMeasurementPointByDcuCodeAndMeterId - DCU:" + dcuCode + " - Meter:" + meterId + "\n",
					ex);
			if (ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}

			datatable = null;

			return mpstatus;
		}

		return mpstatus;
	}

	/*
	 * / (non-Javadoc)
	 * 
	 * @see
	 * saoviet.amisystem.business.IMT3Business#UpdateMeterObisScale(java.lang.
	 * String, saoviet.amisystem.model.operation.DataCollection, boolean)
	 */
	@Override
	public void UpdateMeterObisScale(String meterId, DataCollection scaleList, boolean isUsedScale) {

		DataTable tbScale = new DataTable();

		for (DataField item : scaleList.getdataList()) {
			if (item.getScale() != null) {
				Object[] obj = new Object[5];
				obj[0] = meterId;
				obj[1] = item.getFieldCode();
				obj[2] = item.getName();

				// Scale xet cho DCU Sl6087
				if (isUsedScale == false)
					obj[3] = item.getScale();
				else
					obj[3] = item.getDefaultScale();
				obj[4] = item.getType();

				tbScale.addRow(obj);
			}
		}

		try {
			SqlHelper.InsertBatch(DatabaseConnection.getSqlConnection(), "INS_METERSCALE3P", tbScale);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "UpdateMeterObisScale", ex);
			if (ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}
		} finally {
			tbScale = null;
		}
	}

	@Override
	public void UpdateMeasurementPointAlertConfig(MT3MeasurementPointAlertConfigCollection alertValueList) {
		DataTable tbScale = new DataTable();
		for (MT3MeasurementPointAlertConfigField item : alertValueList.getAlertValueList()) {
			Object[] obj = new Object[4];
			obj[0] = item.getConfigParameter();
			obj[1] = item.getConfigValue();
			obj[2] = item.getConfigType();
			obj[3] = item.getDcuCode();
			tbScale.addRow(obj);
		}
		try {
			SqlHelper.InsertBatch(DatabaseConnection.getSqlConnection(), "INS_MP_ALERT_CONFIG", tbScale);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "UpdateMeasurementPointAlertConfig", ex);
			if (ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}
		}
	}

	public void UpdateDcuInfo(DcuInforEntity dcuInfo) {
		Object[] objArr = new Object[9];

		objArr[0] = dcuInfo.getDcuCode();
		objArr[1] = dcuInfo.getDcuVerSion();
		objArr[2] = dcuInfo.getProtocolVerSion();
		objArr[3] = dcuInfo.getSingal();
		objArr[4] = dcuInfo.getSimTemperature();
		objArr[5] = dcuInfo.getMcuTemperature();
		objArr[6] = dcuInfo.getRtcPinVotage();
		objArr[7] = dcuInfo.getCellIdConnect();
		objArr[8] = dcuInfo.getSimSerial();

		try {
			SqlHelper.ExecuteNoneQuery(DatabaseConnection.getSqlConnection(), "UPD_DCU_INFO", objArr);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "UpdateDcuInfo", ex);
			if (ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}
		}

	}

	@Override
	public void insertLoadProfilePacket(MT3LoadProfile3PMessageEntity packet) {

		Object[] objArr = new Object[51];

		objArr[0] = packet.getServerTime();
		objArr[1] = packet.getMeterTimeFrom();
		objArr[2] = packet.getMeterTimeTo();
		objArr[3] = packet.getVoltagePhaseA();
		objArr[4] = packet.getVoltagePhaseB();
		objArr[5] = packet.getVoltagePhaseC();
		objArr[6] = packet.getCurrentPhaseA();
		objArr[7] = packet.getCurrentPhaseB();
		objArr[8] = packet.getCurrentPhaseC();
		objArr[9] = packet.getPhaseAnglePhaseA();
		objArr[10] = packet.getPhaseAnglePhaseB();
		objArr[11] = packet.getPhaseAnglePhaseC();
		objArr[12] = packet.getActivePowerPhaseA();
		objArr[13] = packet.getActivePowerPhaseB();
		objArr[14] = packet.getActivePowerPhaseC();
		objArr[15] = packet.getActivePowerPhaseTotal();
		objArr[16] = packet.getReactivePowerPhaseA();
		objArr[17] = packet.getReactivePowerPhaseB();
		objArr[18] = packet.getReactivePowerPhaseC();
		objArr[19] = packet.getReactivePowerPhaseTotal();
		objArr[20] = packet.getApparentPowerPhaseA();
		objArr[21] = packet.getApparentPowerPhaseB();
		objArr[22] = packet.getApparentPowerPhaseC();
		objArr[23] = packet.getApparentPowerPhaseTotal();
		objArr[24] = packet.getPowerFactorPhaseA();
		objArr[25] = packet.getPowerFactorPhaseB();
		objArr[26] = packet.getPowerFactorPhaseC();
		objArr[27] = packet.getPowerFactorPhaseTotal();
		objArr[28] = packet.getEnergyPlusARate1();
		objArr[29] = packet.getEnergyPlusARate2();
		objArr[30] = packet.getEnergyPlusARate3();
		objArr[31] = packet.getEnergySubARate1();
		objArr[32] = packet.getEnergySubARate2();
		objArr[33] = packet.getEnergySubARate3();
		objArr[34] = packet.getQ1();
		objArr[35] = packet.getQ3();
		objArr[36] = packet.getImportWh();
		objArr[37] = packet.getExportWh();
		objArr[38] = packet.getReactiveEnergyPlusARate1();
		objArr[39] = packet.getReactiveEnergyPlusARate2();
		objArr[40] = packet.getReactiveEnergyPlusARate3();
		objArr[41] = packet.getReactiveEnergySubARate1();
		objArr[42] = packet.getReactiveEnergySubARate2();
		objArr[43] = packet.getReactiveEnergySubARate3();
		objArr[44] = packet.getPowerAvgPos();
		objArr[45] = packet.getPowerAvgNeg();
		objArr[46] = packet.getReactivePowerAvgPos();
		objArr[47] = packet.getReactivePowerAvgNeg();
		objArr[48] = packet.getLastAvgPowerFactor();
		objArr[49] = packet.getMeterCode();
		objArr[50] = packet.getDcuCode();
		synchronized (this.dtLoadProfile) {
			this.dtLoadProfile.addRow(objArr);
		}
		synchronized (this) {
			notify();
		}
		try {
			// SqlHelper.ExecuteNoneQuery(DatabaseConnection.getSqlConnection(),
			// "INS_LOADPROFILE", objArr);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "insertLoadProfilePacket", ex);
			if (ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}
		}

	}

	public void insertMeterAlert(MT3MeterAlertCollection meterAlertList) {
		DataTable tbAlert = new DataTable();
		for (MT3MeterAlertEntity item : meterAlertList.getAlertValueList()) {
			Object[] objArr = new Object[6];
			objArr[0] = item.getAlertValue();
			objArr[1] = meterAlertList.getServerTime();
			objArr[2] = Constant.THREE_PHASE_ALERT_TYPE.getNameValue(item.getAlertIndex()).toString();
			objArr[3] = meterAlertList.getMeterCode();
			objArr[4] = meterAlertList.getDcuCode();
			objArr[5] = item.getAlertEnd();
			tbAlert.addRow(objArr);
		}
		try {

			SqlHelper.InsertBatch(DatabaseConnection.getSqlConnection(), "INS_METERALERT", tbAlert);
		} catch (Exception ex) {
			logUlti.writeLog(LogType.ERROR, "insertMeterAlert", ex);
			if (ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}
		} finally {
			tbAlert = null;
		}
	}

	@Override
	public void getMeterObisScale(String meterid, String dcuCode, DataCollection scaleList) {
		DataTable scaletable = new DataTable();
		BigDecimal scalevalue = null;
		Object[] obj = new Object[2];
		obj[0] = meterid;
		obj[1] = dcuCode;

		try {

			scaletable = SqlHelper.ExecueDataset(DatabaseConnection.getSqlConnection(), "SEL_METEROBISSCALE", obj);
			if (scaletable != null && scaletable.rowCount() > 0) {
				for (int i = 0; i < scaletable.rowCount(); i++) {
					scalevalue = new BigDecimal(scaletable.getValue(i, Constant.SCALE_VALUE_COLUMN));
					scaleList.add(scaletable.getValue(i, Constant.TAG_NAME_COLUMN), scalevalue);
				}
				// gán MeterType cho DataField ở vị trí đầu tiên trong list(Dành
				// cho DCU SL với bản tin lấy type meter)
				scaleList.getdataList().get(0).setType(scaletable.getValue(0, Constant.METER_TYPE_COLUMN));
			}
		} catch (Exception Ex) {
			logUlti.writeLog(LogType.ERROR, "getMeterObisScale", Ex);
			if (Ex.getMessage().toLowerCase().equals(Constant.SQL_ERROR_CONNECTION.toLowerCase())) {
				if (this.systemEventCallback != null) {
					this.systemEventCallback.sqlDisconnect();
				}
			}
		}

		obj = null;
		scaletable = null;
		scalevalue = null;
	}
}