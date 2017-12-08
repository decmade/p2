package com.cloudgames.logger;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * << singleton >>
 * 
 * system-wide log
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
@Service("logger-acl")
public class AclLogger extends AbstractLog4jLogger
{
	final private static String LOG_CATEGORY = "acl";

	
	/**
	 * hidden constructor
	 */
	public AclLogger()
	{	
		this.logger = Logger.getLogger(LOG_CATEGORY);
	}
	
}
