package com.cloudgames.validators.strategies;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import com.cloudgames.http.HttpServletRequestUtil;

@Component("strategy-validator-httprequest-id")
public class HttpRequestHasValidIDParameterStrategy implements ValidatorStrategyInterface
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
			return false;
		}
		
		if ( routeParam.matches("[0-9]+") == false ) {
			return false;
		}
		
		return true;
	}

}
