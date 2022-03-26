package saoviet.amisystem.ulti;

public class SessionEntity {

	private static boolean sqlDisconnect;

	private static int lostCount;
	
	private static int maxLostCount;
	
	private static int timeInsertData;
	
	public static int getTimeInsertData() {
		return timeInsertData;
	}

	public static void setTimeInsertData(int timeInsertData) {
		SessionEntity.timeInsertData = timeInsertData;
	}

	public static int getMaxLostCount() {
		return maxLostCount;
	}

	public static void setMaxLostCount(int maxLostCount) {
		SessionEntity.maxLostCount = maxLostCount;
	}

	public static int getLostCount() {
		return lostCount;
	}

	public static void setLostCount(int lostCount) {
		SessionEntity.lostCount = lostCount;
	}

	public static boolean isSqlDisconnect() {
		return sqlDisconnect;
	}

	public static void setSqlDisconnect(boolean sqlDisconnect) {
		SessionEntity.sqlDisconnect = sqlDisconnect;
	}
	
}
