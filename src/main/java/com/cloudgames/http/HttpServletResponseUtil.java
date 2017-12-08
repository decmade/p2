package com.cloudgames.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.cloudgames.exceptions.AbstractCustomHttpException;
import com.cloudgames.logger.ControllerLogger;
import com.cloudgames.logger.LoggerInterface;

/**
 * utility class for performing common tasks
 * with HttpServletResponse objects
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
public class HttpServletResponseUtil 
{
	private static LoggerInterface log = new ControllerLogger();
	

	/**
	 * appends data to a response object
	 * 
	 * @param HttpServletResponse response
	 * @param String message
	 */
	public static void writeTo(HttpServletResponse response, String data)
	{
		PrintWriter writer = null;
		
		try {
			writer = response.getWriter();
			response.setContentType("application/json");
			
			writer.println( data );
			writer.flush();
			writer.close();
			
		} catch(Exception e) {
			log.error( e.getMessage() );
		}
	}
	
	/**
	 * slips the binary data for a receipt file into the response and
	 * sets the MIME type accordingly
	 * 
	 * @param HttpServletResponse response
	 * @param ReceiptInterface receipt
	 * @param ServletContext context
	 * 
	 */
	public static void sendFile(HttpServletResponse response, File file) {
		OutputStream out = null;
		InputStream in = null;
		BufferedOutputStream bufferedOut = null;
		BufferedInputStream bufferedIn = null;
		byte[] buffer = new byte[10240];
		int byteCount = -1;
			
		
		try {
			in = new FileInputStream( file );
			bufferedIn = new BufferedInputStream( in );
			out = response.getOutputStream();
			bufferedOut = new BufferedOutputStream( out );
			
			response.setContentLength( (int)file.length() );
			
			while ( ( byteCount = bufferedIn.read(buffer) ) != -1) {
				bufferedOut.write(buffer, 0, byteCount);
				bufferedOut.flush();
			}
			
			bufferedIn.close();
			bufferedOut.close();
			
			
		
		} catch(IOException e) {
			log.error( e.getMessage() );
		}
	}
	
	/**
	 * handles a custom HTTP exception
	 * 
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * 
	 * @param AbstractCustomHttpException exception
	 */
	public static void handleCustomException(HttpServletRequest request, HttpServletResponse response, AbstractCustomHttpException exception)
	{
		String message = exception.getRequestMessage(request);
		
		log.error( message );
		
		try {
			response.sendError(exception.getStatusCode(), message );
		} catch(IOException e) {
			log.error( e.getMessage() );
		}
	}
}
