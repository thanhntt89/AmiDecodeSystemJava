package saoviet.amisystem.ulti;

import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

@SuppressWarnings("restriction")
public class SecurityUlti {

	// Vector
	private static byte[] iv = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d,
			0x0e, 0x0f };

	public static String aesDecrypt(String encrypted, String key) {
		String result = "";
		byte[] keys = new BigInteger(key, 16).toByteArray();
		byte[] data = aesDecrypt(new BigInteger(encrypted, 16).toByteArray(), keys, iv);
		result = DatatypeConverter.printHexBinary(data);

		keys = null;
		data = null;
		return result;
	}

	public static String aesEncrypt(String encrypted, String key) {
		String result = "";
		byte[] data = aesEncrypt(new BigInteger(encrypted, 16).toByteArray(), new BigInteger(key, 16).toByteArray(),
				iv);
		result = DatatypeConverter.printHexBinary(data);
		return result;
	}

	/**
	 * getStringAesDecrypt used default iv vector
	 * 
	 * @param encrypted
	 * @param key
	 * @return
	 */
	public static String getStringAesDecrypt(byte[] encrypted, String key) {
		String result = "";
		result = DatatypeConverter.printHexBinary(aesDecrypt(encrypted, new BigInteger(key, 16).toByteArray(), iv));
		return result;
	}

	/**
	 * getBytesEesEncrypt used default iv vector
	 * 
	 * @param descrypted
	 * @param key
	 * @return
	 */
	public static byte[] getBytesEesEncrypt(String descrypted, String key) {
		byte[] result = null;
		result = aesEncrypt(new BigInteger(descrypted, 16).toByteArray(), new BigInteger(key, 16).toByteArray(), iv);
		return result;
	}

	/**
	 * AES decrypt function
	 * 
	 * @param encrypted
	 * @param key
	 *            16, 24, 32 bytes available
	 * @param iv
	 *            initial vector (16 bytes) - if null: ECB mode, otherwise: CBC
	 *            mode
	 * @return
	 */
	public static byte[] aesEncrypt(byte[] encrypted, byte[] key, byte[] iv) {
		if (key == null || (key.length != 16 && key.length != 24 && key.length != 32)) {
			// Logger.e("key's bit length is not 128/192/256");
			return null;
		}
		if (iv != null && iv.length != 16) {
			// Logger.e("iv's bit length is not 16");
			return null;
		}
		try {
			SecretKeySpec keySpec = null;
			Cipher cipher = null;

			if (iv != null) {
				keySpec = new SecretKeySpec(key, "AES");
				cipher = Cipher.getInstance("AES/CBC/NoPadding");

				cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
			} else // if(iv == null)
			{
				keySpec = new SecretKeySpec(key, "AES");
				cipher = Cipher.getInstance("AES/ECB/NoPadding");
				cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			}

			return cipher.doFinal(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * AES decrypt function
	 * 
	 * @param encrypted
	 * @param key
	 *            16, 24, 32 bytes available
	 * @param iv
	 *            initial vector (16 bytes) - if null: ECB mode, otherwise: CBC
	 *            mode
	 * @return
	 */
	public static byte[] aesDecrypt(byte[] encrypted, byte[] key, byte[] iv) {
		if (key == null || (key.length != 16 && key.length != 24 && key.length != 32)) {
			// Logger.e("key's bit length is not 128/192/256");
			return null;
		}
		if (iv != null && iv.length != 16) {
			// Logger.e("iv's bit length is not 16");
			return null;
		}

		try {
			SecretKeySpec keySpec = null;
			Cipher cipher = null;

			if (iv != null) {
				keySpec = new SecretKeySpec(key, "AES");
				cipher = Cipher.getInstance("AES/CBC/NoPadding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
			} else // if(iv == null)
			{
				keySpec = new SecretKeySpec(key, "AES");
				cipher = Cipher.getInstance("AES/ECB/NoPadding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec);
			}

			return cipher.doFinal(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			// Logger.e(e.toString());
		}
		return null;
	}
}
