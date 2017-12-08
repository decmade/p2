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
@Component("logger-sys")
public class SysLogger extends AbstractLog4jLogger
{
	public SysLogger()
	{
		super();
		
		this.logger = Logger.getRootLogger();
	}
	
}
