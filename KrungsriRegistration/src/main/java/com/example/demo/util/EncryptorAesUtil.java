package com.example.demo.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.stereotype.Component;

@Component
public class EncryptorAesUtil {

	private static final String algorithm = "AES/CBC/PKCS5Padding";

	private static SecretKey key;

	private final static IvParameterSpec iv = AESEncryptionUtil.generateIv();

	public EncryptorAesUtil() {
		try {
			key = AESEncryptionUtil.getKeyFromPassword();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}

	}

	public static String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] cipherText = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(cipherText);
	}

	public static String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText.getBytes(StandardCharsets.UTF_8)));
		return new String(plainText, StandardCharsets.UTF_8);
	}
}
