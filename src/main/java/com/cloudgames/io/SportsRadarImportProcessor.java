package com.cloudgames.io;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cloudgames.entities.Game;
import com.cloudgames.entities.Sport;
import com.cloudgames.entities.Team;
import com.cloudgames.entities.Venue;

import com.cloudgames.entities.interfaces.*;
import com.cloudgames.models.sr.SRScheduleResponse;
import com.cloudgames.models.sr.SRScheduleTeam;
import com.cloudgames.models.sr.SRScheduleVenue;
import com.cloudgames.services.interfaces.*;

@Component("sports-radar-processor")
public class SportsRadarImportProcessor extends AbstractIoObject {

	@Autowired
	@Qualifier("sport-service")
	private SportServiceInterface sportService;
	
	@Autowired
	@Qualifier("venue-service")
	private VenueServiceInterface venueService;
	
	@Autowired
	@Qualifier("game-service")
	private GameServiceInterface gameService;
	
	@Autowired
	@Qualifier("team-service")
	private TeamServiceInterface teamService;
	
	@Transactional
	public void process(SRScheduleResponse response) {
		SportInterface sport = this.getFootballSport();
		sportService.save(sport);
		
		this.log.debug("attempting to process Football schedule data");
		
		response.weeks.stream().forEach( (srWeek) -> {
			srWeek.games.stream().forEach( (srGame) -> {
				GameInterface game = null;
				TeamInterface homeTeam = null;
				TeamInterface awayTeam = null;
				VenueInterface venue = null;
				
				try {
					this.log.debug( String.format("importing game data for game [%s]", srGame.id) );
					
					game = new Game();
					homeTeam = this.getTeam(srGame.home);
					awayTeam = this.getTeam(srGame.away);
					venue = this.getVenue(srGame.venue);					
					
					homeTeam.setVenue( (Venue)venue );
					teamService.save(homeTeam);					
					
					game.setSportsRadarId(srGame.id);	
					game.setSport( (Sport)sport);
					game.setAttendance( srGame.attendance);
					game.setScheduled( srGame.scheduled );
					game.setNumber(srGame.number);		
					game.setStatus(srGame.status);
					
					game.setHomeTeam( (Team)homeTeam );
					game.setAwayTeam( (Team)awayTeam );
					game.setVenue( (Venue)venue );
					
					if ( srGame.weather != null ) {
						game.setWeather(srGame.weather);
					}
					
					if ( srGame.scoring != null ) {
						game.setAwayScore(srGame.scoring.away_points);
						game.setHomeScore(srGame.scoring.home_points);
					}
					
					gameService.save(game);
					
					this.log.debug("game imported successfully");
				} catch(Exception e) {
					if ( e.getMessage().isEmpty() ) {
						this.log.error( e.toString() );
					} else {
						this.log.error( e.getMessage() );
					}
				}
				
			});
		});
	}

	private VenueInterface getVenue(SRScheduleVenue srVenue) {
		VenueInterface venue = null;
		
		try {
			this.log.debug( String.format("importing venue information for Venue[%s]", srVenue.id) );
			
			venue = this.venueService.fetchBySportsRadarId( srVenue.id);	
			
			if ( venue == null ) {
				venue = new Venue();
				
				venue.setAddress(srVenue.address);
				venue.setCapacity(srVenue.capacity);
				venue.setCity(srVenue.city);
				venue.setName(srVenue.name);
				venue.setRoofType(srVenue.roof_type);
				venue.setSportsRadarId(srVenue.id);
				venue.setState(srVenue.state);
				venue.setSurface(srVenue.surface);
				venue.setZip(srVenue.zip);
			}
			
			venueService.save(venue);
			
			this.log.debug("venue information imported successfully");
		} catch(Exception e) {
			if ( e.getMessage().isEmpty() ) {
				this.log.error( e.toString() );
			} else {
				this.log.error( e.getMessage() );
			}
		}		
		
		return venue;
	}
	
	private TeamInterface getTeam(SRScheduleTeam srTeam) {
		TeamInterface team = null;
		
		try {
			this.log.debug( String.format("importing team data for Team[%s]", srTeam.id) );		
			
			team = this.teamService.fetchBySportsRadarId(srTeam.id);
			
			if ( team == null ) {
				team = new Team();
				
				team.setAlias(srTeam.alias);
				team.setName(srTeam.name);
				team.setSportsRadarId(srTeam.id);
			}
			
			teamService.save(team);
			
			this.log.debug("team data imported successfully");
		} catch(Exception e ) {
			if ( e.getMessage().isEmpty() ) {
				this.log.error( e.toString() );
			} else {
				this.log.error( e.getMessage() );
			}
		}
		
		return team;
	}
	
	private SportInterface getFootballSport() {
		List<SportInterface> sports = sportService.fetchAll();
		
		for(SportInterface sport: sports) {
			if ( sport.getDescription().equalsIgnoreCase("football") ) {
				return sport;
			}
		}
		
		Sport sport = new Sport();
		
		sport.setDescription("Football");
		return sport;
	}
}
