package saoviet.amisystem.model;

public class SystemConfig {

	private String mqttGatwayAddress;

	public String getMqttGatwayAddress() {
		return mqttGatwayAddress;
	}

	public void setMqttGatwayAddress(String mqttGatwayAddress) {
		this.mqttGatwayAddress = mqttGatwayAddress;
	}

	public String getMqttUserName() {
		return mqttUserName;
	}

	public void setMqttUserName(String mqttUserName) {
		this.mqttUserName = mqttUserName;
	}

	public String getMqttPassword() {
		return mqttPassword;
	}

	public void setMqttPassword(String mqttPassword) {
		this.mqttPassword = mqttPassword;
	}

	public int getMqttPort() {
		return mqttPort;
	}

	public void setMqttPort(int mqttPort) {
		this.mqttPort = mqttPort;
	}

	public String getMqttTopics() {
		return mqttTopics;
	}

	public void setMqttTopics(String mqttTopics) {
		this.mqttTopics = mqttTopics;
	}

	public int getMqttQoSSubcribe() {
		return mqttQoSSubcribe;
	}

	public void setMqttQoSSubcribe(int mqttQoSSubcribe) {
		this.mqttQoSSubcribe = mqttQoSSubcribe;
	}

	public String getDbConnectionString() {
		return dbConnectionString;
	}

	public void setDbConnectionString(String dbConnectionString) {
		this.dbConnectionString = dbConnectionString;
	}

	public int getTimeAutoReconnect() {
		return timeAutoReconnect;
	}

	public void setTimeAutoReconnect(int timeAutoReconnect) {
		this.timeAutoReconnect = timeAutoReconnect;
	}

	public int getMqttKeepAlivee() {
		return mqttKeepAlive;
	}

	public void setMqttKeepAlive(int keepAlive) {
		this.mqttKeepAlive = keepAlive;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getCleanSession() {
		return cleanSession;
	}

	public void setCleanSession(int cleanSession) {
		this.cleanSession = cleanSession;
	}

	public int getUnSubcribeAffterDisconnect() {
		return unSubcribeAffterDisconnect;
	}

	public void setUnSubcribeAffterDisconnect(int unSubcribeAffterDisconnect) {
		this.unSubcribeAffterDisconnect = unSubcribeAffterDisconnect;
	}

	public int getMaxThreadNum() {
		return maxThreadNum;
	}

	public void setMaxThreadNum(int maxThreadNum) {
		this.maxThreadNum = maxThreadNum;
	}

	public int getMinThreadNum() {
		return minThreadNum;
	}

	public void setMinThreadNum(int minThreadNum) {
		this.minThreadNum = minThreadNum;
	}

	public int getThreadKeepAlive() {
		return threadKeepAlive;
	}

	public void setThreadKeepAlive(int threadKeepAlive) {
		this.threadKeepAlive = threadKeepAlive;
	}

	public int getMqttKeepAlive() {
		return mqttKeepAlive;
	}

	public String getSqlUserName() {
		return this.sqlUserName;
	}

	public void setSqlUserName(String sqlUserName) {
		this.sqlUserName = sqlUserName;
	}

	public String getSqlPassword() {
		return this.sqlPassword;
	}

	public void setSqlPassword(String sqlPassword) {
		this.sqlPassword = sqlPassword;
	}

	public boolean isSqlDefaultAutoCommit() {
		return this.sqlDefaultAutoCommit;
	}

	public void setSqlDefaultAutoCommit(boolean sqlDefaultAutoCommit) {
		this.sqlDefaultAutoCommit = sqlDefaultAutoCommit;
	}

	public int getSqlPoolInitSize() {
		return this.sqlPoolInitSize;
	}

	public void setSqlPoolInitSize(int sqlPoolInitSize) {
		this.sqlPoolInitSize = sqlPoolInitSize;
	}

	public int getSqlMinIdl() {
		return sqlMinIdl;
	}

	public void setSqlMinIdl(int sqlMinIdl) {
		this.sqlMinIdl = sqlMinIdl;
	}

	public int getSqlMaxIdle() {
		return sqlMaxIdle;
	}

	public void setSqlMaxIdle(int sqlMaxIdle) {
		this.sqlMaxIdle = sqlMaxIdle;
	}

	public int getSqlQueryTimeOut() {
		return this.sqlQueryTimeOut;
	}

	public void setSqlQueryTimeOut(int sqlQueryTimeOut) {
		this.sqlQueryTimeOut = sqlQueryTimeOut;
	}

	public int getSqlLoginTimeout() {
		return this.sqlLoginTimeout;
	}

	public void setSqlLoginTimeout(int sqlLoginTimeout) {
		this.sqlLoginTimeout = sqlLoginTimeout;
	}
	
	public int getSqlMaxActive() {
		return sqlMaxActive;
	}

	public void setSqlMaxActive(int sqlMaxActive) {
		this.sqlMaxActive = sqlMaxActive;
	}
	
	public int getOpenwirePort() {
		return openwirePort;
	}

	public void setOpenwirePort(int openwirePort) {
		this.openwirePort = openwirePort;
	}

	public String getOpenwireTopics() {
		return openwireTopics;
	}

	public void setOpenwireTopics(String openwireTopics) {
		this.openwireTopics = openwireTopics;
	}

	public boolean isSaveMessage() {
		return isSaveMessage;
	}

	public void setSaveMessage(boolean isSaveMessage) {
		this.isSaveMessage = isSaveMessage;
	}

	public boolean isLoadMessage() {
		return isLoadMessage;
	}

	public void setLoadMessage(boolean isLoadMessage) {
		this.isLoadMessage = isLoadMessage;
	}

	public int getSqlReConnectionCount() {
		return sqlReConnectionCount;
	}

	public void setSqlReConnectionCount(int sqlReConnectionCount) {
		this.sqlReConnectionCount = sqlReConnectionCount;
	}
	
	private int unSubcribeAffterDisconnect;
	private String clientId;
	private int cleanSession;
	private String mqttUserName;
	private String mqttPassword;
	private int mqttPort;
	private int openwirePort;
	private String mqttTopics;
	private String openwireTopics;
	private int mqttQoSSubcribe;
	private int timeAutoReconnect;
	private int mqttKeepAlive;
	private int maxThreadNum;
	private int minThreadNum;
	private int threadKeepAlive;
	private String dbConnectionString;
	private String sqlUserName;
	private String sqlPassword;
	private boolean sqlDefaultAutoCommit;
	private int sqlPoolInitSize;
	private int sqlMinIdl;
	private int sqlMaxIdle;
	private int sqlMaxActive;
	private int sqlQueryTimeOut;
	private int sqlLoginTimeout;
	private boolean isSaveMessage;	
	private boolean isLoadMessage;
	private int sqlReConnectionCount;

}
