/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: IProcessMessage.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */
package saoviet.amisystem.messagemanagement;

import saoviet.amisystem.model.MessageBase;
import saoviet.amisystem.model.meter.onephase.decode.IOnePhaseDecode;
import saoviet.amisystem.model.meter.threephase.decode.IThreePhaseDecode;

public interface IProcessMessage {
	/*
	 * Xử lý các bản tin sau khi ra khỏi hàng đợi Các bản tin nhận được gồm
	 * nhiều loại đồng hồ khác nhau
	 */
	void processOnePhaseMessage(IOnePhaseDecode iOnePhaseDecode, MessageBase messageBase);

	void processThreePhaseMessage(IThreePhaseDecode iThreePhaseDecode, MessageBase messageBase);
}
