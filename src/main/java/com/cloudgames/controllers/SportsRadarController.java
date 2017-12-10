package com.cloudgames.controllers;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.cloudgames.entities.*;
import com.cloudgames.entities.interfaces.*;
import com.cloudgames.io.Json;
import com.cloudgames.io.SportsRadarImportProcessor;
import com.cloudgames.services.interfaces.*;

@RestController
@RequestMapping("update")
public class SportsRadarController extends AbstractBasicController {

	@Autowired
	@Qualifier("sports-radar-processor")
	private SportsRadarImportProcessor srProcessor;
	
	@Autowired
	@Qualifier("json")
	private Json json;
	
	@GetMapping("test")
	public Object test() {
		String path = this.getClass().getClassLoader().getResource("football-schedule.json").getFile();
		
		File dataFile = new File( path );
		Object response = null;
		StringBuilder sb = new StringBuilder();
		
		if ( dataFile.exists() ) {
			try {
				Files.readAllLines( dataFile.toPath() ).stream().forEach( (line) -> {
					sb.append(line);
				});
				
				response = this.json.decode( String.valueOf(sb) );
			} catch(Exception e) {
				this.log.error( e.getMessage() );
			}
			
		}
		
		return response;
	}
	
	@GetMapping("football")
	public SRScheduleResponse updateFootballSchedule() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/bet/update/test";
		SRScheduleResponse response = restTemplate.getForObject(url, SRScheduleResponse.class);
		try {
			this.srProcessor.process(response);
		} catch(Exception e) {
			log.error( e.getMessage() );
		}
		
		return response;
	}
	
}
