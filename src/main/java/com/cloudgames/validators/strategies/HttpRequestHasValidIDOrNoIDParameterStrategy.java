package com.cloudgames.validators.strategies;

import javax.servlet.http.HttpServletRequest;

import com.cloudgames.http.HttpServletRequestUtil;

public class HttpRequestHasValidIDOrNoIDParameterStrategy implements ValidatorStrategyInterface
{
	@Override
	public boolean test(Object subject) 
	{
		HttpServletRequest request = (HttpServletRequest)subject;
		String routeParam = HttpServletRequestUtil.getRouteParameter(request);
		
		if ( routeParam.isEmpty() ) {
			return true;
		}
		
		if ( routeParam.matches("[0-9]+") ) {
			return true;
		}
		
		return false;
	}

}
