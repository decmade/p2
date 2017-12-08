package com.cloudgames.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import com.cloudgames.logger.LoggerInterface;

/**
 * a utility class to provide convenience for loading
 * properties contained in a properties file
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
@Component("properties-file")
public class PropertiesFile 
{
	@Autowired
	@Qualifier("logger-io")
	private LoggerInterface log;
	
	/**
	 * load the data in a properties file and
	 * return the Properties object that represents
	 * its contents
	 * 
	 * @param String fileName
	 * 
	 * @return Properties
	 */
	public Properties load(String fileName)
	{
		Properties props = new Properties();
		InputStream reader = null;

		log.trace( String.format("attempting to load properties file: [%s]", fileName) );
		
		try {
			reader = PropertiesFile.class.getResourceAsStream( String.format("/%s", fileName) );
			props.load(reader);
			
			log.trace( String.format("successfully loaded properties file: [%s]", fileName) );
			
		} catch(IOException e) {
			log.error( e.getMessage() );
		} 

		
		return props;
	}
}
