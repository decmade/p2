package com.cloudgames.io;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.cloudgames.logger.LoggerInterface;

@Component("json")
public class Json 
{
	@Autowired
	@Qualifier("logger-io")
	private LoggerInterface log;
	private ObjectMapper mapper;
	private ObjectWriter writer;
	
	public Json() {
		this.mapper = new ObjectMapper();
		this.writer = mapper.writer().withDefaultPrettyPrinter();
	}
	
	/**
	 * encodes an Object into
	 * a JSON string
	 * 
	 * @param List< Map<String, String> > data
	 * 
	 * @return String (JSON formatted)
	 */
	public String encode(Object data) 
	{
		/*
		 * needs jackson databind package from Maven
		 */
		String output = "";
		
		try {
			output = writer.writeValueAsString( data );
			
			if ( output.isEmpty() ) {
				if ( data.getClass().isInstance( Collections.class ) ) {
					output = "[]";
				} else {
					output = "{}";
				}
			}
		} catch(Exception e) {
			log.error( e.getMessage() );
		}
		
		return output;
	}
	
	/**
	 * decodes a JSON string to a Map<String, String>
	 * 
	 * @param String json
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> decode(String json)
	throws IOException
	{
		Map<String, String> properties = new HashMap<>();
		
		TypeReference< HashMap<String,String> > typeRef = new TypeReference< HashMap<String,String> >(){};
		properties = mapper.readValue( json, typeRef );
		
		return properties;
	}

}
