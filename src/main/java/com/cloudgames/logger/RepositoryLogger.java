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
@Component("logger-repository")
public class RepositoryLogger extends AbstractLog4jLogger
{	
	final static private String LOG_CATEGORY = "respositories";

	public RepositoryLogger()
	{
		super();
		
		this.logger = Logger.getLogger(LOG_CATEGORY);
	}
	
}
