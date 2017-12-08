package com.cloudgames.acl;

import org.springframework.beans.factory.annotation.*;

import com.cloudgames.logger.LoggerInterface;

public class AbstractAclObject {
	
	@Autowired
	@Qualifier("logger-acl")
	protected LoggerInterface log;
}
