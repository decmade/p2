package com.cloudgames.controllers;

import java.io.File;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.cloudgames.io.Json;
import com.cloudgames.io.SportsRadarImportProcessor;
import com.cloudgames.models.sr.SRScheduleResponse;

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
		String url = "http://api.sportradar.us/nfl-ot2/games/2017/REG/schedule.json?api_key=ac7eft6p9k3cs8a5ss9b63we";
		SRScheduleResponse response = restTemplate.getForObject(url, SRScheduleResponse.class);
		try {
			this.srProcessor.process(response);
		} catch(Exception e) {
			log.error( e.getMessage() );
		}
		
		return response;
	}
	
}
