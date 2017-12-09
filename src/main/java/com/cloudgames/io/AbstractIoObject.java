package com.cloudgames.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.cloudgames.logger.interfaces.LoggerInterface;

public class AbstractIoObject {
	@Autowired
	@Qualifier("logger-io")
	protected LoggerInterface log;
}
