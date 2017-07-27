package com.sap.mango.jiraintegration.utils;

import org.apache.log4j.LogManager;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

/**
 * Stores encryption/decryption methods
 */
public class EncryptionUtils {
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final String KEY_ALGORITHM = "AES";
    private static final org.apache.log4j.Logger log = LogManager.getLogger(JsonUtil.class);
    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128;
    private static byte[] ivBytes = {0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8};


    public static byte[] encrypt(byte[] message, byte[] encKey) {
        try {
            SecretKeySpec key = new SecretKeySpec(encKey, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
            return cipher.doFinal(message);
        } catch (  InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] decrypt(byte[] message, byte[] encKey) {
        try {
            SecretKeySpec key = new SecretKeySpec(encKey, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
            return cipher.doFinal(message);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException  | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static byte[] getKeyFromPass(String pass, byte[] salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(tmp.getEncoded(), "AES");
            return secret.getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e);
            return null;
        }
    }

    public static String encrypt(String message, String pass) {
        final byte[] salt = getNextSalt();
        byte[] encrypt = new byte[0];
        try {
            encrypt = encrypt(message.getBytes("UTF-8"), getKeyFromPass(pass, salt));
        } catch (UnsupportedEncodingException e) {
            log.error("error while encrypting password");
        }
        final byte[] result = new byte[encrypt.length + 20];
        System.arraycopy(encrypt, 0, result, 0, encrypt.length);
        System.arraycopy(salt, 0, result, encrypt.length, salt.length);
        return new org.apache.commons.codec.binary.Base64().encodeToString(result);
    }

    public static String decrypt(String message, String pass) {
        final byte[] enc_bytes = new org.apache.commons.codec.binary.Base64().decode(message);
        final byte[] enc_message = new byte[enc_bytes.length - 20];
        final byte[] salt = new byte[20];
        System.arraycopy(enc_bytes, 0, enc_message, 0, enc_message.length);
        System.arraycopy(enc_bytes, enc_message.length, salt, 0, salt.length);
        try {
            return new String(decrypt(enc_message, getKeyFromPass(pass, salt)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("error while decrypting password");
        }
        return null;
    }

    private static byte[] getNextSalt() {
        byte bytes[] = new byte[20];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

}
