/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: ByteUltil.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.ulti;

import java.io.ByteArrayOutputStream;

public class ByteUltil {
	public static byte[] Combine(byte[] arrays) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte c[] = null;
		try {
			outputStream.write(arrays);
			c = outputStream.toByteArray();
		} catch (Exception ex) {
		}
		outputStream = null;
		return c;
	}

	public static byte[] Combine(byte[] arraysone, byte[] arraystwo) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte c[] = null;
		try {
			outputStream.write(arraysone);
			outputStream.write(arraystwo);
			c = outputStream.toByteArray();
		} catch (Exception ex) {
		}
		outputStream = null;
		return c;
	}

	public static byte[] Combine(byte arrays) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte c[] = null;

		try {
			outputStream.write(arrays);
			c = outputStream.toByteArray();
		} catch (Exception ex) {
		}
		outputStream = null;
		return c;
	}

	public static byte[] Combine(byte[] arraysone, byte arraystwo) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte c[] = null;
		try {
			outputStream.write(arraysone);
			outputStream.write(arraystwo);
			c = outputStream.toByteArray();
		} catch (Exception ex) {
		}
		outputStream = null;
		return c;
	}
}
