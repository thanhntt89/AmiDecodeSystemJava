package saoviet.amisystem.business;

import saoviet.amisystem.event.SystemEventCallback;
import saoviet.amisystem.model.datacollection.DataCollection;
import saoviet.amisystem.model.dcu.DcuInforEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3EventMeterCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3HistoricalEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3LoadProfile3PMessageEntity;
import saoviet.amisystem.model.meter.threephase.entity.MT3MeasurementPointAlertConfigCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3MeterAlertCollection;
import saoviet.amisystem.model.meter.threephase.entity.MT3OperationEntity;

public interface IMT3Business extends Runnable{
	void addMessageOperation(MT3OperationEntity operation);

	boolean StopThreadAutoInsert();
	
	void insertIntantaneous(MT3OperationEntity intantaneus);

	void insertEvent(MT3EventMeterCollection list);

	void insertHistorical(MT3HistoricalEntity historical);

	void setSystemEventCallback(SystemEventCallback systemEventCallback);

	/**
	 * Hàm truyền giá trị đưa vào DB từ bản tin MeDPV,xử lý thêm mới điểm,
	 * thêm(cập nhật) DCU, Công tơ cho điêm đo cho điểm đo. Tuỳ trường hợp sẽ
	 * trả ra các giá trị khác nhau.
	 * 
	 * @param dcuCode
	 * @param meterId
	 * @param meterType
	 * @return
	 */
	int UpdateDcuIdForMeasurementPointByDcuCodeAndMeterId(String dcuCode, String meterId, String meterType);

	/**
	 * Cập nhật Scale cho meter vào DB
	 * 
	 * @param meterId
	 * @param scaleList
	 * @param isUsedScale
	 */
	void UpdateMeterObisScale(String meterId, DataCollection scaleList, boolean isUsedScale);

	/**
	 * Cập nhật ngưỡng cảnh báo vào DB
	 * 
	 * @param alertValueList
	 */
	void UpdateMeasurementPointAlertConfig(MT3MeasurementPointAlertConfigCollection alertValueList);

	/**
	 * Cập nhật thông tin cho DCU vào DB
	 * 
	 * @param dcuInfo
	 */
	void UpdateDcuInfo(DcuInforEntity dcuInfo);

	void insertLoadProfilePacket(MT3LoadProfile3PMessageEntity load);

	void insertMeterAlert(MT3MeterAlertCollection meterAlertList);
/**
 * Lấy scale của công tơ từ DB
 * @param dcuCode
 * @param scaleList
 * @return
 */
	void getMeterObisScale(String meterid, String dcuCode, DataCollection scaleList);
}
