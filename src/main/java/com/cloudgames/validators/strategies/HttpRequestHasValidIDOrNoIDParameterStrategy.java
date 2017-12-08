package com.cloudgames.validators.strategies;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cloudgames.http.HttpServletRequestUtil;

@Component("strategy-validator-httprequest-validornoid")
public class HttpRequestHasValidIDOrNoIDParameterStrategy implements ValidatorStrategyInterface
{
	@Autowired
	@Qualifier("utility-http-request")
	private HttpServletRequestUtil requestUtil;
	
	@Override
	public boolean test(Object subject) 
	{
		HttpServletRequest request = (HttpServletRequest)subject;
		String routeParam = this.requestUtil.getRouteParameter(request);
		
		if ( routeParam.isEmpty() ) {
			return true;
		}
		
		if ( routeParam.matches("[0-9]+") ) {
			return true;
		}
		
		return false;
	}

}
