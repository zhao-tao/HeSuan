package com.sxonecard.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;



/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 */
public class AESOperator {
	private static final String AESTYPE = "AES/ECB/PKCS5Padding";
	
	private static String pwdkey;

	public static String AES_Encrypt(String plainText) {
		byte[] encrypt = null;
		try {
			Key key = generateKey(pwdkey);
			Cipher cipher = Cipher.getInstance(AESTYPE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encrypt = cipher.doFinal(plainText.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String(Base64.encode(encrypt, Base64.NO_WRAP));
	}

	public static String AES_Decrypt(String encryptData) {
		byte[] decrypt = null;
		try {
			Key key = generateKey(pwdkey);
			Cipher cipher = Cipher.getInstance(AESTYPE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			decrypt = cipher
					.doFinal(Base64.decode(encryptData, Base64.NO_WRAP));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(decrypt).trim();
	}

	private static Key generateKey(String key) throws Exception {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
			return keySpec;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	public static String getKey() {
		return AESOperator.pwdkey;
	}

	public static void setKey(String pwdkey) {
		AESOperator.pwdkey = pwdkey;
	}

	// public static void main(String[] args) {
	
	// String keyStr = "testkey";
	
	// String plainText = "test";
	
	// String encText = AES_Encrypt(keyStr, plainText);
	// String decString = AES_Decrypt(keyStr, encText);
	
	// System.out.println(encText);
	// System.out.println(decString);
	
	// }
}