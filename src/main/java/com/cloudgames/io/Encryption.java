package com.cloudgames.io;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.cloudgames.logger.IOLogger;
import com.cloudgames.logger.LoggerInterface;

public class Encryption {
	private static LoggerInterface log = IOLogger.getInstance();
	private static String ALGORITHM = "AES";
	
	public static String generateKey() {
		SecretKey key = null;
		log.debug("generating secrety key");
		
		try {
			key = KeyGenerator.getInstance(ALGORITHM).generateKey();
			log.debug("secret key generated successfully");
			
			return encodeBytesToString( key.getEncoded() );
		} catch(Exception e) {
			log.error(e.getMessage() );
			return "<error>";
		}
		
		
	}
	
	public static String encrypt(String value, String keyString) {
		Cipher cipher = getCipher();
		byte[] keyBytes = decodeStringToBytes( keyString );
		SecretKey key = new SecretKeySpec(keyBytes, 0, keyBytes.length, ALGORITHM);
		
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return encodeBytesToString( cipher.doFinal( value.getBytes() ) );
		} catch (Exception e) {
			log.error( e.getMessage() );
			return "<error>";
		}
		
		
		
	}
	
	private static byte[] decodeStringToBytes(String input ) {
		return Base64.getDecoder().decode(input);
	}
	
	private static String encodeBytesToString(byte[] input) {
		return Base64.getEncoder().encodeToString( input );
	}
	
	private static Cipher getCipher() {
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance( ALGORITHM );
			return cipher;
		} catch(Exception e) {
			log.error( e.getMessage() );
			return null;
		}

	}
}
