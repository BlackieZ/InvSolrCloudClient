/* Title:		
 *
 * Project:     MAS
 *
 * @ Link: 		http://...
 *
 * @ Email:		ivan_ling@hotmail.com
 *
 * @ Copyright: Copyright (c) 2008 mezimedia
 *
 * @ Author 	Ivan.ling
 *
 * @ Version 	1.0
 
 * @ last change time 2008-01-20 	
 */
package com.valueclickbrands.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SecurityUtils {
	public static final String STR_DES_ALGORITHM = "DES";
	public static final String STR_DES_ALGORITHM_3 = "DESede/CBC/PKCS5Padding";

	public static final String STR_IV = "(0@d$G2$";

	public static final String STR_KEY = "1u!4@6&8~0S2#45C^8%012s4";

	public SecurityUtils() {
	}

	@SuppressWarnings("static-access")
	public static void main(String args[]) {
		SecurityUtils su = new SecurityUtils();
		try {
//			String strKey = "123456789012345678901234";
//			// String strKey2 = "1u!4@6&8~0S2#45C^8%012s4";
//			String strKeyDES = "c$9D2@y~";
//
//			String strEN = su.strEncrypt(STR_IV, "4313024495479127", STR_KEY);
//
//			System.out.println("=====****** encrypt word ========= = \""
//					+ strEN + "\"");
//			System.out.println("=====****** decrypt====== = "
//					+ su.strDecrypt(STR_IV, strEN, STR_KEY));

//			// strEN = su.strEncryptWithDES("5105105105105100", strKeyDES);
//			strEN = su.strEncryptWithDES("378282246310005", strKeyDES);
//			System.out.println("=====________des encrypt word ========= = "
//					+ strEN);
//
//			// strEN = "B3lHMe9PLWgpTZbhnNK82/Aqgr57FbBT";
//			System.out.println("=====________des decrypt====== = "
//					+ su.strDecryptWithDES(strEN, strKeyDES));

//			System.out.println("MD5 = " + su.strMD5("111111"));
			System.out.println(getBASE64("http://www.7netshopping.jp/books/detail/-/accd/1106132898/subno/1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ECB module and NoPadding fill DES encrypt method.
	 * 
	 * @param src
	 *            # encrypt string
	 * @param sKeyByte
	 *            # encrypt key
	 * @return encryptByte # cryptograph
	 */
	public static String strEncryptWithDES(String strSrc, String strKeyByte)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher myCipher = Cipher.getInstance(STR_DES_ALGORITHM);
		SecretKey sKey = new SecretKeySpec(strKeyByte.getBytes(), "DES");
		myCipher.init(Cipher.ENCRYPT_MODE, sKey);

		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(myCipher.doFinal(strSrc.getBytes()));
	}

	/**
	 * DES decrypt method.
	 * 
	 * @param src
	 *            # cryptograph
	 * @param sKeyByte
	 *            # encrypt key
	 * @return decryptByte # password
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws IOException
	 */
	public static String strDecryptWithDES(String strSrc, String strKeyByte)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, IOException {
		Cipher myCipher = Cipher.getInstance(STR_DES_ALGORITHM);
		SecretKey sKey = new SecretKeySpec(strKeyByte.getBytes(), "DES");
		myCipher.init(Cipher.DECRYPT_MODE, sKey);

		BASE64Decoder base64Decoder = new BASE64Decoder();
		return new String(myCipher.doFinal(base64Decoder.decodeBuffer(strSrc)));
	}

	public static String strEncrypt(String strAlgorithm, String strIV,
			String strSource, String strKey) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		SecureRandom sr = new SecureRandom();
		DESedeKeySpec dks = new DESedeKeySpec(strKey.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey securekey = keyFactory.generateSecret(dks);
		IvParameterSpec iv = new IvParameterSpec(strIV.getBytes());
		Cipher cipher = Cipher.getInstance(strAlgorithm);
		cipher.init(Cipher.ENCRYPT_MODE, securekey, iv, sr);

		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(cipher.doFinal(strSource.getBytes()));
	}

	public static String strEncrypt(String strIV, String strSource,
			String strKey) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		return strEncrypt(STR_DES_ALGORITHM_3, strIV, strSource, strKey);
	}

	public static String strDecrypt(String strAlgorithm, String strIV,
			String strSource, String strKey) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		SecureRandom sr = new SecureRandom();
		DESedeKeySpec dks = new DESedeKeySpec(strKey.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey securekey = keyFactory.generateSecret(dks);
		IvParameterSpec iv = new IvParameterSpec(strIV.getBytes());
		Cipher cipher = Cipher.getInstance(strAlgorithm);
		cipher.init(Cipher.DECRYPT_MODE, securekey, iv, sr);

		BASE64Decoder base64Decoder = new BASE64Decoder();

		return new String(cipher.doFinal(base64Decoder.decodeBuffer(strSource)));
	}

	public static String strDecrypt(String strIV, String strSource,
			String strKey) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		return strDecrypt(STR_DES_ALGORITHM_3, strIV, strSource, strKey);
	}

	public static String strMD5(String strSource)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");

		return dumpBytes(md.digest(strSource.getBytes()));
	}
	
	public static String getFileMD5String(File file) throws NoSuchAlgorithmException,IOException {
		MessageDigest messagedigest = MessageDigest.getInstance("MD5");
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messagedigest.update(byteBuffer);
		return dumpBytes(messagedigest.digest());
	}

	private static String dumpBytes(byte[] bytes) {
		StringBuilder sb = new StringBuilder(32);
		String s = null;

		for (int i = 0; i < bytes.length; i++) {
			if (i % 32 == 0 && i != 0) {
				sb.append("\n");
			}
			s = Integer.toHexString(bytes[i]);
			if (s.length() < 2) {
				s = "0" + s;
			}
			if (s.length() > 2) {
				s = s.substring(s.length() - 2);
			}
			sb.append(s);
		}
		return sb.toString();
	}

	// 将 s 进行 BASE64 编码
	public static String getBASE64(String s) {
		if (s == null)
			return null;
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	// 将 BASE64 编码的字符串 s 进行解码
	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}
}