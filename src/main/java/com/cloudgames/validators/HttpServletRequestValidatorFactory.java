package com.cloudgames.validators;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

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
@Component("factory-validator-httprequest")
public class HttpServletRequestValidatorFactory 
{
	@Autowired
	@Qualifier("strategy-validator-httprequet-bodyhasdata")
	ValidatorStrategyInterface bodyHasDataStrategy;
	
	@Autowired
	@Qualifier("strategy-validator-httprequest-validornoid")
	ValidatorStrategyInterface requestHasValidOrNoIdtrategy;
	
	@Autowired
	@Qualifier("strategy-validator-httprequest-id")
	ValidatorStrategyInterface requestHasIdStrategy;
	
	
	public HttpServletRequestValidator assemble(String method)
	{
		HttpServletRequestValidator validator = new HttpServletRequestValidator();
		
		switch( method.toLowerCase() ) {
			case "post" :
				validator.addStrategy(bodyHasDataStrategy);
				break;
			case "put" :
				validator.addStrategy( bodyHasDataStrategy );
				validator.addStrategy( requestHasValidOrNoIdtrategy );
				break;
			case "delete" :
				validator.addStrategy( requestHasIdStrategy );
				break;
			case "get" :
				validator.addStrategy(requestHasValidOrNoIdtrategy );
				break;
		}
		
		return validator;
	}
}
