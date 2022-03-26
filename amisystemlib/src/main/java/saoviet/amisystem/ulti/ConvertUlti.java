/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: ConvertUtil.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.ulti;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;

@SuppressWarnings("restriction")
public class ConvertUlti {

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		if (bytes == null)
			return "";
		try {
			char[] hexChars = new char[bytes.length * 2];
			for (int j = 0; j < bytes.length; j++) {
				int v = bytes[j] & 0xFF;
				hexChars[j * 2] = hexArray[v >>> 4];
				hexChars[j * 2 + 1] = hexArray[v & 0x0F];
			}
			return new String(hexChars);
		} catch (Exception ex) {
			return "";
		}
	}

	public static long byteToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.put(bytes);
		buffer.flip();// need flip
		return buffer.getLong();
	}

	public static int hex2Int(String s) {
		return Integer.parseInt(s, 16);
	}

	public static long hex2Long(String s) {
		return Long.parseLong(s, 16);
	}

	public static String byteArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for (byte b : a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}

	public static int byteArrayToInt(byte[] b) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		return value;
	}

	public static String toHexString(byte[] array) {
		if (array == null)
			return "";
		return DatatypeConverter.printHexBinary(array);
	}

	public static byte[] toByteArray(String s) {
		return DatatypeConverter.parseHexBinary(s);
	}

	static public Boolean ValidateByte(byte[] packetData) {
		Boolean isResult = false;
		long _checkSumByte = 0;

		if (packetData.length < 2)
			return false;

		int last_byte = (packetData[packetData.length - 1] + 256) % 256;// trường
																		// hợp
																		// âm

		for (int i = 0; i < packetData.length - 1; i++)
			_checkSumByte += packetData[i];

		// Chia lấy phần dư của 256 ( 1 byte giá trị từ 0-255) kieu add bytes//
		// tính toán trogn trường hợp giá trị trả về là âm
		_checkSumByte = ((_checkSumByte % 256) + 256) % 256;

		if (last_byte == _checkSumByte)
			isResult = true;

		return isResult;
	}

	/**
	 * Remove checksume byte
	 * 
	 * @param _packetData
	 * @return
	 */
	static public byte[] RemovedCheckSum(byte[] _packetData) {
		if (_packetData.length < 2)
			return null;

		byte[] temp = new byte[_packetData.length - 1];

		if (!ValidateByte(_packetData)) {
			return null;
		}
		try {
			temp = Arrays.copyOf(_packetData, temp.length);
		} catch (Exception ex) {
			return null;
		}
		return temp;
	}

	public static byte[] fromStringToBytes(String data) {
		return data.getBytes();
	}

	public static byte[] toBytes(int i) {
		byte[] result = new byte[4];

		result[0] = (byte) (i >> 24);
		result[1] = (byte) (i >> 16);
		result[2] = (byte) (i >> 8);
		result[3] = (byte) (i /* >> 0 */);

		return result;
	}

	public static int convertIntToIntHex(int n) {
		return Integer.valueOf(String.valueOf(n), 16);
	}

	public static BigDecimal convertHexToDecimal(String hexString) {
		try {
			return new BigDecimal(Integer.parseInt(hexString, 16));
		} catch (Exception ex) {
			return null;
		}
	}

	public static BigDecimal convertHexToLongDecimal(String hexString) {
		try {
			return new BigDecimal(Long.parseLong(hexString, 16));
		} catch (Exception ex) {
			return null;
		}
	}

	public static String convertHexToDecimalString(String hexString) {
		try {
			return String.valueOf(Integer.parseInt(hexString, 16));
		} catch (Exception ex) {
			return null;
		}
	}

	public static String convertHexaToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);
		}

		temp = null;
		return sb.toString();
	}

	public static BigDecimal convertNegativeValue(String hexString) {
		try {
			byte[] value;
			if (hexString.substring(0, 2).equals("FF")) {
				value = ConvertUlti.toByteArray(hexString);
				for (int i = 0; i < value.length; i++)
					value[i] = (byte) ~value[i];
				int dataConvert = ConvertUlti.byteArrayToInt(value);
				return BigDecimal.valueOf(~dataConvert);
			}
			return BigDecimal.valueOf(Long.parseLong(hexString, 16));
		} catch (Exception ex) {
			return null;
		}
	}

	public static byte[] asnEncode(String tagCodeHex, String valueHex) {
		byte[] buff = null;
		byte[] value = null;
		try {
			value = ByteUltil.Combine(toByteArray(valueHex));

			buff = ByteUltil.Combine(toByteArray(tagCodeHex));
			buff = ByteUltil.Combine(buff, toByteArray(StringUlti.PadLeft(String.valueOf(value.length), 1, '0')));
			buff = ByteUltil.Combine(buff, value);
		} catch (Exception ex) {
		}
		value = null;
		return buff;
	}

}
