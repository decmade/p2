package com.cloudgames.logger;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * << singleton >>
 * 
 * system-wide log
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
@Component("logger-controller")
public class ControllerLogger extends AbstractLog4jLogger
{
	final private static String LOG_CATEGORY = "controllers";
	
	
	/**
	 * hidden constructor
	 */
	public ControllerLogger()
	{
		super();
		
		this.logger = Logger.getLogger(LOG_CATEGORY);
	}
	
}
