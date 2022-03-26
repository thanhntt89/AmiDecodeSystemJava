package saoviet.amisystem.model;

public class MessageBase {

	private String PreTopic;

	public String getPreTopic() {
		return PreTopic;
	}

	public void setPreTopic(String preTopic) {
		PreTopic = preTopic;
	}

	public String getMessageType() {
		return MessageType;
	}

	public void setMessageType(String messageType) {
		MessageType = messageType;
	}

	public String getMeterCode() {
		return MeterCode;
	}

	public void setMeterCode(String meterCode) {
		MeterCode = meterCode;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getMeterType() {
		return MeterType;
	}

	public void setMeterType(String meterType) {
		MeterType = meterType;
	}

	public String getDcuCode() {
		return DcuCode;
	}

	public void setDcuCode(String dcuCode) {
		DcuCode = dcuCode;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public byte[] getData() {
		return Data;
	}

	public void setData(byte[] data) {
		Data = data;
	}
	
	private String MessageType;
	private String MeterCode;
	private String Type;
	private String MeterType;
	private String DcuCode;
	private String StartTime;
	private String EndTime;
	private byte[] Data;
}
