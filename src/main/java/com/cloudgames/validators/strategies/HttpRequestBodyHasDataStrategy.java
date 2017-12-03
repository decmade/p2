package com.cloudgames.validators.strategies;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestBodyHasDataStrategy implements ValidatorStrategyInterface
{

	@Override
	public boolean test(Object subject) 
	{
		HttpServletRequest request = (HttpServletRequest)subject;
		
		return ( request.getContentLength() > 0 );
	}

}
