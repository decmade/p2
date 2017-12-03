package com.cloudgames.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * template for creating a Log4j logger instance
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
abstract public class AbstractLog4jLogger implements LoggerInterface
{
	protected static String rootClassName = AbstractLog4jLogger.class.getCanonicalName();
	protected Logger logger = null;
	

	
	@Override
	public synchronized void trace(String message)
	{
		this.logger.log(rootClassName, Level.TRACE, message, null);
	}
	
	@Override
	public synchronized void debug(String message)
	{
		this.logger.log(rootClassName, Level.DEBUG, message, null);
	}
	
	@Override
	public synchronized void info(String message)
	{
		this.logger.log(rootClassName, Level.INFO, message, null);
	}
	
	@Override
	public synchronized void warn(String message)
	{
		this.logger.log(rootClassName, Level.WARN, message, null);
	}
	
	@Override
	public synchronized void error(String message)
	{
		this.logger.log(rootClassName, Level.ERROR, message, null);
	}
	
	@Override
	public synchronized void fatal(String message)
	{
		this.logger.log(rootClassName, Level.FATAL, message, null);
	}
}
