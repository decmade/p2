package com.cloudgames.io.interfaces;

public interface EncryptionInterface {

	String generateKey();

	String encrypt(String value, String keyString);

}
