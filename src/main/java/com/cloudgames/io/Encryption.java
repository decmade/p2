package com.cloudgames.io;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import com.cloudgames.logger.LoggerInterface;

@Component("encryption")
public class Encryption {
	
	@Autowired
	@Qualifier("logger-io")
	private LoggerInterface log;
	
	private String ALGORITHM = "AES";
	
	public String generateKey() {
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
	
	public String encrypt(String value, String keyString) {
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
	
	private byte[] decodeStringToBytes(String input ) {
		return Base64.getDecoder().decode(input);
	}
	
	private String encodeBytesToString(byte[] input) {
		return Base64.getEncoder().encodeToString( input );
	}
	
	private Cipher getCipher() {
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
