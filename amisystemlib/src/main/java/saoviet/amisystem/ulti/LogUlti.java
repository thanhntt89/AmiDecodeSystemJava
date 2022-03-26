/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: LogUlti.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-22 15:47:06
 */

package saoviet.amisystem.ulti;

import java.io.File;
import org.apache.log4j.*;

public class LogUlti {

	public enum LogType {
		TRACE, DEBUG, INFO, WARN, ERROR
	}

	private Logger logger;

	public LogUlti(Class<?> _classname) {
		try {
			if (logger == null) {
				logger = Logger.getLogger(_classname);
				File log4jfile = new File("./config/SystemLogConfig.properties");
				PropertyConfigurator.configure(log4jfile.getAbsolutePath());
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	public void writeLog(LogType logType, String logdata) {

		switch (logType) {
		case ERROR:
			logger.error(logdata);
			break;
		case DEBUG:
			logger.debug(logdata);
			break;
		case INFO:
			logger.info(logdata);
			break;
		case WARN:
			logger.warn(logdata);
			break;
		case TRACE:
			logger.trace(logdata);
			break;
		default:
			break;
		}
	}

	public void writeLog(LogType logType, String logdata, Exception ex) {

		switch (logType) {
		case ERROR:
			logger.error(logdata, ex);
			break;
		case DEBUG:
			logger.debug(logdata, ex);
			break;
		case INFO:
			logger.debug(logdata, ex);
			break;
		case TRACE:
			logger.warn(logdata, ex);
			break;
		default:
			break;
		}
	}
}
