package com.cloudgames.validators.strategies;

import javax.servlet.http.HttpServletRequest;

import com.cloudgames.http.HttpServletRequestUtil;

public class HttpRequestHasValidIDParameterStrategy implements ValidatorStrategyInterface
{
	@Override
	public boolean test(Object subject) 
	{
		HttpServletRequest request = (HttpServletRequest)subject;
		String routeParam = HttpServletRequestUtil.getRouteParameter(request);
		
		if ( routeParam.isEmpty() ) {
			return false;
		}
		
		if ( routeParam.matches("[0-9]+") == false ) {
			return false;
		}
		
		return true;
	}

}
