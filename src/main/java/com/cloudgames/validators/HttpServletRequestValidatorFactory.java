package com.cloudgames.validators;

import com.cloudgames.validators.strategies.*;

/**
 * << factory >>
 * 
 * assembles an instance of an HttpServletRequestValidator
 * object per the request method being used
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
public class HttpServletRequestValidatorFactory 
{

	public static HttpServletRequestValidator assemble(String method)
	{
		HttpServletRequestValidator validator = new HttpServletRequestValidator();
		
		switch( method.toLowerCase() ) {
			case "post" :
				validator.addStrategy( new HttpRequestBodyHasDataStrategy() );
				break;
			case "put" :
				validator.addStrategy( new HttpRequestBodyHasDataStrategy() );
				validator.addStrategy( new HttpRequestHasValidIDParameterStrategy() );
				break;
			case "delete" :
				validator.addStrategy( new HttpRequestHasValidIDParameterStrategy() );
				break;
			case "get" :
				validator.addStrategy( new HttpRequestHasValidIDOrNoIDParameterStrategy() );
				break;
		}
		
		return validator;
	}
}
