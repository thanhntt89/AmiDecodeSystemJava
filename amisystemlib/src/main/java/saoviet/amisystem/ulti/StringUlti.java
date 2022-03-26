/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: StringUlti.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.ulti;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUlti {

	public static String PadLeft(String s, int num, char pad) {
		return String.format("%" + num + "s", s).replace(' ', pad);
	}
	
	public static String getCurrentTimeStamp() {
	    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}
}
