/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: Constant.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.ulti;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constant {
	
	public final static String LOG_FILE_PATH = System.getProperty("user.dir") + "\\logs\\log.txt";
	public final static String MESSAGE_DATABASE_FILE_PATH = System.getProperty("user.dir") + "\\data\\messagedata.db";
	public final static String MESSAGE_DATABASE_FILE_PATH_RENAME = System.getProperty("user.dir")
			+ String.format("\\data\\%s messagedata.db", new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()));

	// SQL disconnect
	public final static String SQL_ERROR_CONNECTION = "Closed Connection";

	// Topic response first message from DCU
	public final static String TOPIC_RESPONSE_FOR_DCU_OF_FIRST_MESSAGE = "AMI/%s/MePDV";
	public final static String PAYLOAD_RESPONSE_DCU = "Thank You!!!";
	// Message type
	public final static String SAO_VIET_MESSAGE_TYPE_LOAPROFILE = "LoPro";
	public final static String SAO_VIET_MESSAGE_TYPE_INTANTANEOUS = "Intan";
	public final static String SAO_VIET_MESSAGE_TYPE_OPERATION = "Opera";
	public final static String SAO_VIET_MESSAGE_TYPE_HISTORICAL = "Histo";
	public final static String SAO_VIET_MESSAGE_TYPE_METER_EVENT = "EvMet";
	public final static String SAO_VIET_MESSAGE_TYPE_DCU_WARNING = "EvDcu";
	public final static String SAO_VIET_MESSAGE_TYPE_DCU_LOG = "WaLog";
	public final static String SAO_VIET_MESSAGE_TYPE_MEASUREMENT_POINT_ALERT_CONFIG = "cAlar";
	// MEPDV = Measurement Point Config Value
	public final static String SAO_VIET_MESSAGE_TYPE_MEPDV = "MePDV";
	public final static String DCUINFO_TAG = "FE";
	public final static String SCALE_TAG = "FA";
	public final static String ALERT_TAG = "FB";
	public final static String SERVER_TIME_TAG = "F0";
	// Các loại công tơ 3 phase
	public final static String METER_TYPE_STAR = "01";
	public final static String METER_TYPE_ELSTER = "02";
	public final static String METER_TYPE_LANDIS = "03";
	public final static String METER_TYPE_GENIUS = "04";
	public final static String METER_TYPE_GELEX = "05";
	public final static String METER_TYPE_HEXING = "06";
	// Topic topic meter type
	public final static String TOPIC_TYPE_STAR = "DTS1";
	public final static String TOPIC_TYPE_ELSTER = "ELS1";
	public final static String TOPIC_TYPE_LANDIS = "LAN1";
	public final static String TOPIC_TYPE_GENIUS = "GEN1";
	public final static String TOPIC_TYPE_GELEX = "GEL1";
	public final static String TOPIC_TYPE_HEXING = "HEX1";

	// Metertype
	public final static String DCU_3_PHASE = "ELE3";
	public final static String DCU_1_PHASE = "ELE1";

	// MeasurementPoint Status
	public final static int DCU_CHANGE = 1;
	public final static int METER_CHANGE = 2;
	public final static int NEW_MEASUREMENT_POINT = 3;

	// ================================AMI
	// PORTAL=======================================
	public final static String UPDATE_THRESHOLD_MESSAGE_TOPIC = "REQ/WEB/WEB/MePDV/WEB";
	public final static String UPDATE_METER_TOPIC = "REQ/WEB/WEB/MS409/WEB";
	// Send update firmware
	public final static String UPDATE_FIRMWARE_MESSAGE_TOPIC = "REQ/WEB/WEB/MS410/WEB";

	public final static String REQ_INSTANTANEOUS_MESSAGE_TOPIC = "REQ/WEB/WEB/MS100";
	public final static String REQ_OPERATION_MESSAGE_TOPIC = "REQ/WEB/WEB/MS101";
	public final static String REQ_HISTORICAL_MESSAGE_TOPIC = "REQ/WEB/WEB/MS102";
	public final static String REQ_EVENT_MESSAGE_TOPIC = "REQ/WEB/WEB/MS103";
	public final static String REQ_DCU_SIM_NUMBER_MESSAGE_TOPIC = "REQ/WEB/WEB/MS411";

	public final static int DCU_TYPE_1P = 1;
	public final static int DCU_TYPE_3P = 3;
	public final static String DCU_TYPE_VIETTEL = "Viettel";

	// Default value
	public final static int CURRENT_3P_HIGHT_DEFAULT = 10;
	public final static int COS_3P_DEFAULT = 90;
	public final static int PHASE_3P_DEVIATION_DEFAULT = 15;
	public final static int FREQ_3P_LOW_DEFAULT = 1;
	public final static int FREQ_VALUE_DEFAULT = 50;
	public final static int FREQ_3P_HIGHT_DEFAULT = 1;
	public final static int POWER_3P_LOST_DEFAULT = 10;
	public final static int VOLT_3P_LOW_DEFAULT = 5;
	public final static int VOLT_3P_HIGHT_DEFAULT = 5;
	public final static int VOLT_DEFAULT = 220;

	public final static String FREQUENCY_H_3P = "3P_Frequency_H";
	public final static String FREQUENCY_L_3P = "3P_Frequency_L";

	public final static String CURRENT_A_H_3P = "3P_Current_A_H";
	public final static String CURRENT_B_H_3P = "3P_Current_B_H";
	public final static String CURRENT_C_H_3P = "3P_Current_C_H";

	public final static String CURRENT_A_L_3P = "3P_Current_A_L";
	public final static String CURRENT_B_L_3P = "3P_Current_B_L";
	public final static String CURRENT_C_L_3P = "3P_Current_C_L";

	public final static String COS_3P = "3P_Cos";

	public final static String VOLTAGE_A_F_3P = "3P_Voltage_A_F";
	public final static String VOLTAGE_B_F_3P = "3P_Voltage_B_F";
	public final static String VOLTAGE_C_F_3P = "3P_Voltage_C_F";

	public final static String VOLTAGE_A_L_3P = "3P_Voltage_A_L";
	public final static String VOLTAGE_B_L_3P = "3P_Voltage_B_L";
	public final static String VOLTAGE_C_L_3P = "3P_Voltage_C_L";

	public final static String VOLTAGE_A_H_3P = "3P_Voltage_A_H";
	public final static String VOLTAGE_B_H_3P = "3P_Voltage_B_H";
	public final static String VOLTAGE_C_H_3P = "3P_Voltage_C_H";

	public final static String VOLTAGE_A_LOST_3P = "3P_Voltage_A_LOST";
	public final static String VOLTAGE_B_LOST_3P = "3P_Voltage_B_LOST";
	public final static String VOLTAGE_C_LOST_3P = "3P_Voltage_C_LOST";

	public final static String VOLTAGE_A_3P = "3P_Voltage_A";
	public final static String VOLTAGE_B_3P = "3P_Voltage_B";
	public final static String VOLTAGE_C_3P = "3P_Voltage_C";

	public final static String CURRENT_A_3P = "3P_Current_A";
	public final static String CURRENT_B_3P = "3P_Current_B";
	public final static String CURRENT_C_3P = "3P_Current_C";

	public final static String FREQUENCY_3P = "3P_Frequency";

	public final static String PHASESDEVIATION_3P = "3P_PhasesDeviation";

	public final static String FREQUENCY_H_1P = "1P_Frequency_H";
	public final static String FREQUENCY_L_1P = "1P_Frequency_L";
	public final static String CURRENT_H_1P = "1P_Current_H";
	public final static String CURRENT_L_1P = "1P_Current_L";
	public final static String COS_1P = "1P_Cos";
	public final static String VOLTAGE_F_1P = "1P_Voltage_F";
	public final static String VOLTAGE_L_1P = "1P_Voltage_L";
	public final static String VOLTAGE_H_1P = "1P_Voltage_H";
	public final static String VOLTAGE_LOST_1P = "1P_Voltage_LOST";
	public final static String VOLTAGE_1P = "1P_Voltage";
	public final static String CURRENT_1P = "1P_Current";
	public final static String FREQUENCY_1P = "1P_Frequency";

	// Event 3P ELSTER
	public final static String EVENTPROGRAMMING = "PROGRAM_CHANGING_TIMES";

	public final static String EVENTCOVER = "COVER_OPEN";

	public final static String EVENTPASSWORDCHANGE = "PASSWORD_CHANGING_TIMES";

	public final static String EVENTSYSTEM = "SYSTEM_EVENT";

	public final static String EVENTVOLTAGEIMBALANCE = "VOLT_IMBALANCE";

	public final static String EVENTPOWERFAILURE = "POWER_FAIL";

	public final static String EVENTREVERSEPOWER = "EVENT_REVERSE_POWER";

	public final static String EVENTSTEALELECTRICITY = "EVENT_STEAL_ELECTRICITY";

	public final static String EVENTIMBALANCEPOWER = "EVENT_IMBALANCE_POWER";

	public final static String EVENTTIMEDATECHANGE = "DATETIME_CHANGING";

	public final static String EVENTFAILUREPHASEA = "PHASE_FAIL_A";
	public final static String EVENTFAILUREPHASEB = "PHASE_FAIL_B";
	public final static String EVENTFAILUREPHASEC = "PHASE_FAIL_C_";

	public final static String EVENTREVERSERUNPHASEA = "PHASE_REVERSE_A";
	public final static String EVENTREVERSERUNPHASEB = "PHASE_REVERSE_B";
	public final static String EVENTREVERSERUNPHASEC = "PHASE_REVERSE_C";

	public final static String EVENTREVERSERUNTHREEEPHASE = "PHASE_REVERSE";

	public final static String EVENTINST1PHASEA = "INST1_PHASE_A";
	public final static String EVENTINST1PHASEB = "INST1_PHASE_B";
	public final static String EVENTINST1PHASEC = "INST1_PHASE_C";

	public final static String EVENTINST2PHASEA = "INST2_PHASE_A";
	public final static String EVENTINST2PHASEB = "INST2_PHASE_B";
	public final static String EVENTINST2PHASEC = "INST2_PHASE_C";

	public final static String EVENTINST3PHASEA = "INST3_PHASE_A";
	public final static String EVENTINST3PHASEB = "INST3_PHASE_B";
	public final static String EVENTINST3PHASEC = "INST3_PHASE_C";

	public final static String EVENTINST4PHASEA = "INST4_PHASE_A";
	public final static String EVENTINST4PHASEB = "INST4_PHASE_B";
	public final static String EVENTINST4PHASEC = "INST4_PHASE_C";

	public final static String EVENTINST5PHASEA = "INST5_PHASE_A";
	public final static String EVENTINST5PHASEB = "INST5_PHASE_B";
	public final static String EVENTINST5PHASEC = "INST5_PHASE_C";

	// Gelex
	public final static String EVENTAUTOBILLINGRESET = "AUTO_BILLING_RESET";
	public final static String EVENTLOGCLEARED = "LOG_CLEARED";
	public final static String EVENTNEWTIMECHANGED = "NEW_TIME_CHANGED";
	public final static String EVENTOLDTIMECHANGED = "OLD_TIME_CHANGED";
	public final static String EVENTTAMPERTERMINALCOVERREMOVED = "TAMPER_TERMINAL_COVER_REMOVED";
	public final static String EVENTTAMPERTERMINALCOVERCLOSED = "TAMPER_TERMINAL_COVER_CLOSED";
	public final static String EVENTALARMMETERCOVERCLOSED = "ALARM_METER_COVER_CLOSED";
	public final static String EVENTALARMMETERCOVEROPENED = "ALARM_METER_COVER_OPENED";
	public final static String EVENTALARMTERMINALCOVERCLOSED = "ALARM_TERMINAL_COVER_CLOSED";
	public final static String EVENTALARMTERMINALCOVERREMOVED = "ALARM_TERMINAL_COVER_REMOVED";
	public final static String EVENTALARMASYMPOWEREND = "ALARM_ASYM_POWER_END";
	public final static String EVENTALARMASYMPOWERSTART = "ALARM_ASYM_POWER_START";
	public static final String SCALE_VALUE_COLUMN = "SCALEVALUE";
	public static final String TAG_NAME_COLUMN = "TAGNAME";
	public static final String METER_TYPE_COLUMN = "METERTYPE";
	public static final String MP_START_ALERT = "1";
	public static final String MP_END_ALERT = "0";
	public static final int HAS_ALERT = 1;

	public enum THREE_PHASE_ALERT_TYPE {
		FREQ_OVER(0), FREQ_LOWER(1), CURRENT_A_OVER(2), CURRENT_B_OVER(3), CURRENT_C_OVER(4), CURRENT_AB_OVER(
				5), CURRENT_AC_OVER(6), CURRENT_BC_OVER(7), CURRENT_A_LOWER(8), CURRENT_B_LOWER(
						9), CURRENT_C_LOWER(10), POWER_FACTOR_LOWER(11), VOLT_A_OVER(12), VOLT_A_LOWER(13), VOLT_B_OVER(
								14), VOLT_B_LOWER(15), VOLT_C_OVER(16), VOLT_C_LOWER(17), VOLT_A_FAIL(18), VOLT_B_FAIL(
										19), VOLT_C_FAIL(20), LOST_POWER_ALL(21), LOST_POWER_A(22), LOST_POWER_B(
												23), LOST_POWER_C(24), NEUTRAL_CURRENT(25), CURRENT_A_FAIL(
														26), CURRENT_B_FAIL(27), CURRENT_C_FAIL(28), POWER_FACTOR_A(
																29), POWER_FACTOR_B(30), POWER_FACTOR_C(
																		31), CURRENT_PHASE_A_UNDER_AVG_CURRENT(
																				32), CURRENT_PHASE_B_UNDER_AVG_CURRENT(
																						33), CURRENT_PHASE_C_UNDER_AVG_CURRENT(
																								34), CHANGE_METER_ID(
																										35), VOLT_A_LOST(
																												36), VOLT_B_LOST(
																														37), VOLT_C_LOST(
																																38), CURRENT_A_LOST(
																																		39), CURRENT_B_LOST(
																																				40), CURRENT_C_LOST(
																																						41), TIME_DEVIATION(
																																								42);
		private int value;

		private THREE_PHASE_ALERT_TYPE(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static THREE_PHASE_ALERT_TYPE getNameValue(int value) {
			for (THREE_PHASE_ALERT_TYPE e : THREE_PHASE_ALERT_TYPE.values()) {
				if (e.value == value) {
					return e;
				}
			}
			return null;// not found
		}
	}

	// Message Viettel
	public final static String OPERATIONVIETTELDCU = "currentInfo";
	public final static String HITSTORICALVIETTELDCU = "loadBilling";
	public final static String EVENTVIETTELDCU = "event";
	public final static String WARNINGVIETTELDCU = "warning";
	public final static String LOADPROFILEVIETTELDCU = "loadProfile";

}
