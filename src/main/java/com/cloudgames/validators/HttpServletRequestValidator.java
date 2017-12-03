package com.cloudgames.validators;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cloudgames.validators.strategies.ValidatorStrategyInterface;

/**
 * << strategy >>
 * 
 * logic for validating an HttpServletRequest
 * 
 * @author john.w.brown.jr@gmail.com
 *
 */
public class HttpServletRequestValidator 
{
	private List<ValidatorStrategyInterface> strategies;
	
	public HttpServletRequestValidator()
	{
		this.strategies = new ArrayList<>();
	}
	
	/**
	 * inject a validator strategy
	 * 
	 * @param ValidatorStrategyInterface strategy
	 */
	public void addStrategy(ValidatorStrategyInterface strategy)
	{
		this.strategies.add( strategy);
	}
	
	/**
	 * validates an HttpServletRequest by passing it
	 * through the test method for each
	 * ValidatorStrategyInterface injected
	 * 
	 * @param HttpServletRequest request
	 * 
	 * @return boolean
	 */
	public boolean validate(HttpServletRequest request)
	{
		for(ValidatorStrategyInterface strategy: this.strategies ) {
			if ( strategy.test(request) == false ) {
				return false;
			}
		}
		
		return true;
	}
}
