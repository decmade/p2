package com.cloudgames.logger;

public interface LoggerInterface 
{
	/**
	 * logs trace messages
	 * 
	 * @param String message
	 */
	public void trace(String message);
	
	/**
	 * logs debug messages
	 * 
	 * @param String message
	 */
	public void debug(String message);
	
	/**
	 * logs info messages
	 * 
	 * @param String message
	 */
	public void info(String message);
	
	/**
	 * logs warn messages
	 * 
	 * @param String message
	 */
	public void warn(String message);
	
	/**
	 * logs error messages
	 * 
	 * @param String message
	 */
	public void error(String message);
	
	/**
	 * logs fatal messages
	 * 
	 * @param String message
	 */
	public void fatal(String message);
}
