package com.cloudgames.http;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import com.cloudgames.logger.interfaces.LoggerInterface;

/**
* utility class for performing common tasks
* with HttpServletRequest objects
* 
* @author john.w.brown.jr@gmail.com
*
*/
@Component("utility-http-request")
public class HttpServletRequestUtil
{
	@Autowired
	@Qualifier("logger-controller")
	private LoggerInterface log;
	
	/**
	 * extracts the route parameter at the end of the 
	 * request URI
	 * 
	 * @param HttpServletRequest request
	 * 
	 * @return String
	 */
	public String getRouteParameter(HttpServletRequest request)
	{
		String uri = extractRelativeUri( request );
		String[] parts = uri.split("/");
		
		/*
		 * a request must have at least 3 nodes in order to have
		 * [project:1]/[controller:2]/[param:3]
		 */
		if ( parts.length < 3 ) {
			return "";
		} else {
			return parts[ parts.length - 1];
			
		}
	}
	
	/**
	 * returns the contents of the request body
	 * 
	 * @param HttpServletRequest request
	 * 
	 * @return String
	 */
	public String getRequestBody(HttpServletRequest request)
	{
		String output = null;
		BufferedReader reader = null;	
		
		reader = getHttpRequestBodyReader( request );
		output = reader.lines().reduce( (lines, line) -> lines += line ).get();
	
		return String.valueOf( output ).trim();
	}
	
	/**
	 * extracts the relevant part of the URI from
	 * the request
	 * 
	 * @param HttpServletRequest request
	 * 
	 * @return String
	 */
	public String extractRelativeUri(HttpServletRequest request)
	{
		return request.getRequestURI()
			.substring( request.getContextPath().length() )
			.toLowerCase();
	}
	
	/**
	 * returns the BufferedReader object for the
	 * request body
	 * 
	 * @param BufferedReader request
	 * 
	 * @return BufferedReader
	 */
	private BufferedReader getHttpRequestBodyReader(HttpServletRequest request)
	{
		BufferedReader reader = null;
		
		try {
			reader = request.getReader();
			reader.mark(0);
			reader.reset();
		} catch(IOException e) {
			this.log.error(e.getMessage());
		}
		
		return reader;
	}

}
