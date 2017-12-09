package com.cloudgames.io;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.cloudgames.io.interfaces.EncryptionInterface;


@Component("encryption")
public class Encryption extends AbstractIoObject implements EncryptionInterface {
	
	final static private String ALGORITHM = "AES";
	
	@Override
	public String generateKey() {
		SecretKey key = null;
		this.log.debug("generating secrety key");
		
		try {
			key = KeyGenerator.getInstance(ALGORITHM).generateKey();
			this.log.debug("secret key generated successfully");
			
			return this.encodeBytesToString( key.getEncoded() );
		} catch(Exception e) {
			this.log.error(e.getMessage() );
			return "<error>";
		}
		
		
	}
	
	@Override
	public String encrypt(String value, String keyString) {
		Cipher cipher = this.getCipher();
		byte[] keyBytes = this.decodeStringToBytes( keyString );
		SecretKey key = new SecretKeySpec(keyBytes, 0, keyBytes.length, ALGORITHM);
		
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return this.encodeBytesToString( cipher.doFinal( value.getBytes() ) );
		} catch (Exception e) {
			this.log.error(	e.toString() );
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
		} catch(Exception e) {
			this.log.error( e.getMessage() );
		}
		
		return cipher;

	}
}
