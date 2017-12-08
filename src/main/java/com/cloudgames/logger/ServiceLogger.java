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
@Component("logger-service")
public class ServiceLogger extends AbstractLog4jLogger
{
	final static private String LOG_CATEGORY = "services";

	public ServiceLogger()
	{
		super();
		
		this.logger = Logger.getLogger(LOG_CATEGORY);
	}
	
}
