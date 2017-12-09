package com.cloudgames.acl;

import org.springframework.beans.factory.annotation.*;

import com.cloudgames.logger.interfaces.LoggerInterface;

public class AbstractAclObject {
	
	@Autowired
	@Qualifier("logger-acl")
	protected LoggerInterface log;
}
