package com.cloudgames.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.cloudgames.entities.Game;
import com.cloudgames.entities.SRScheduleGame;
import com.cloudgames.entities.SRScheduleResponse;
import com.cloudgames.entities.SRScheduleTeam;
import com.cloudgames.entities.SRScheduleVenue;
import com.cloudgames.entities.Sport;
import com.cloudgames.entities.Team;
import com.cloudgames.entities.Venue;
import com.cloudgames.entities.interfaces.GameInterface;
import com.cloudgames.entities.interfaces.SportInterface;
import com.cloudgames.entities.interfaces.TeamInterface;
import com.cloudgames.entities.interfaces.VenueInterface;
import com.cloudgames.services.interfaces.GameServiceInterface;
import com.cloudgames.services.interfaces.SportServiceInterface;
import com.cloudgames.services.interfaces.TeamServiceInterface;
import com.cloudgames.services.interfaces.VenueServiceInterface;

@RestController
@RequestMapping("update")
public class SportsRadarController extends AbstractBasicController {

	@Autowired
	@Qualifier("game-service")
	private GameServiceInterface gameService;
	
	@Autowired
	@Qualifier("sport-service")
	private SportServiceInterface sportService;
	
	@Autowired
	@Qualifier("team-service")
	private TeamServiceInterface teamService;
	
	@Autowired
	@Qualifier("venue-service")
	private VenueServiceInterface venueService;
	
	@GetMapping("football")
	public SRScheduleResponse updateFootballSchedule() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://api.sportradar.us/nfl-ot2/games/2017/REG/schedule.json?api_key=ac7eft6p9k3cs8a5ss9b63we";
		SRScheduleResponse response = restTemplate.getForObject(url, SRScheduleResponse.class);
		try {
			this.processScheduleResponse(response);
		} catch(Exception e) {
			log.error( e.getMessage() );
		}
		
		return response;
	}
	
	protected void processScheduleResponse(SRScheduleResponse response) {
		SportInterface sport = this.getFootballSport();
		sportService.save(sport);
		
		response.weeks.stream().forEach( (srWeek) -> {
			srWeek.games.stream().forEach( (srGame) -> {
				GameInterface game = new Game();
				TeamInterface homeTeam = this.getTeam(srGame.home);
				TeamInterface awayTeam = this.getTeam(srGame.away);
				VenueInterface venue = this.getVenue(srGame.venue);
			
				venueService.save(venue);
				
				homeTeam.setVenue( (Venue)venue );
				teamService.save(homeTeam);
				teamService.save(awayTeam);
				
				game.setSportsRadarId(srGame.id);	
				game.setSport( (Sport)sport);
				game.setAttendance( srGame.attendance);
				game.setAwayScore(srGame.scoring.away_points);
				game.setHomeScore(srGame.scoring.home_points);
				game.setScheduled( LocalDateTime.parse(srGame.scheduled, DateTimeFormatter.ISO_DATE_TIME) );
				game.setNumber(srGame.number);
				game.setWeather(srGame.weather);
				game.setStatus(srGame.status);
				
				game.setHomeTeam( (Team)homeTeam );
				game.setAwayTeam( (Team)awayTeam );
				game.setVenue( (Venue)venue );
				
				
				gameService.save(game);
				
			});
		});
	}
	
	private VenueInterface getVenue(SRScheduleVenue srVenue) {
		VenueInterface venue = new Venue();
		
		venue.setAddress(srVenue.address);
		venue.setCapacity(srVenue.capacity);
		venue.setCity(srVenue.city);
		venue.setName(srVenue.name);
		venue.setRoofType(srVenue.roof_type);
		venue.setSportsRadarId(srVenue.id);
		venue.setState(srVenue.state);
		venue.setSurface(srVenue.surface);
		venue.setZip(srVenue.zip);
		
		return venue;
	}
	
	private TeamInterface getTeam(SRScheduleTeam srTeam) {
		TeamInterface team = new Team();
		
		team.setAlias(srTeam.alias);
		team.setName(srTeam.name);
		team.setSportsRadarId(srTeam.id);
		
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
	/*
	 * 
	 * {
    "id": "3cf0e8ec-048d-4872-a706-b248c198831c",
    "year": 2017,
    "type": "REG",
    "name": "REG",
    "weeks": [
        {
            "id": "ba9ff0d2-7221-4cd1-ba9f-80a0b10ad659",
            "sequence": 1,
            "title": "1",
            "games": [
                {
                    "id": "057ded67-c019-4047-8d2d-2e71cbb771b9",
                    "status": "closed",
                    "reference": "57249",
                    "number": 16,
                    "scheduled": "2017-09-12T02:20:00+00:00",
                    "attendance": 76324,
                    "utc_offset": -7,
                    "entry_mode": "INGEST",
                    "weather": "Mostly Cloudy Temp: 73 F, Humidity: 39%, Wind: SouthWest 12 mph",
                    "venue": {
                        "id": "6589e61d-ef1e-4e30-91b5-9acd2072b8a0",
                        "name": "Sports Authority Field at Mile High",
                        "city": "Denver",
                        "state": "CO",
                        "country": "USA",
                        "zip": "80204",
                        "address": "1701 Mile High Stadium Circle",
                        "capacity": 76125,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 1
                    },
                    "away": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 21
                    }
                },
                {
                    "id": "0c556cf1-8e1a-4361-be73-7d84fd772aea",
                    "status": "postponed",
                    "reference": "57241",
                    "number": 8,
                    "scheduled": "2017-09-10T17:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "50a5c833-1570-4c38-abc7-7914cf87dbde",
                        "name": "Hard Rock Stadium",
                        "city": "Miami Gardens",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33056",
                        "address": "2269 Northwest 199th Street",
                        "capacity": 76100,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 1
                    },
                    "away": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 1
                    },
                    "scoring": null
                },
                {
                    "id": "127e32f4-5786-4c37-9fc3-dd04aedd6aa5",
                    "status": "closed",
                    "reference": "57238",
                    "number": 5,
                    "scheduled": "2017-09-10T17:00:00+00:00",
                    "attendance": 67431,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 63 F, Humidity: 53%, Wind: East 11 mph",
                    "venue": {
                        "id": "90c38d91-3774-4f5d-82ca-1c806828219f",
                        "name": "FirstEnergy Stadium",
                        "city": "Cleveland",
                        "state": "OH",
                        "country": "USA",
                        "zip": "44114",
                        "address": "100 Alfred Lerner Way",
                        "capacity": 71516,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 1
                    },
                    "away": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 18,
                        "away_points": 21
                    }
                },
                {
                    "id": "24e20c00-cbe7-492e-bc25-6ddd3f856891",
                    "status": "closed",
                    "reference": "57236",
                    "number": 3,
                    "scheduled": "2017-09-10T17:00:00+00:00",
                    "attendance": 61857,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 64 F, Humidity: 57%, Wind: SE 10 mph",
                    "venue": {
                        "id": "d7866605-5ac6-4b3a-90e8-760cc5a26b75",
                        "name": "Soldier Field",
                        "city": "Chicago",
                        "state": "IL",
                        "country": "USA",
                        "zip": "60605",
                        "address": "1410 South Museum Campus Drive",
                        "capacity": 61500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 1
                    },
                    "away": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 23
                    }
                },
                {
                    "id": "2b859a54-56db-4917-ba6e-e978ab076380",
                    "status": "closed",
                    "reference": "57243",
                    "number": 10,
                    "scheduled": "2017-09-10T17:00:00+00:00",
                    "attendance": 78685,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 67 F, Humidity: 54%, Wind: North 5 mph",
                    "venue": {
                        "id": "7c11bb2d-4a53-4842-b842-0f1c63ed78e9",
                        "name": "FedExField",
                        "city": "Landover",
                        "state": "MD",
                        "country": "USA",
                        "zip": "20785",
                        "address": "1600 FedEx Way",
                        "capacity": 83000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 1
                    },
                    "away": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 30
                    }
                },
                {
                    "id": "34fa52ab-8816-4e2e-a67a-3ce37621ba91",
                    "status": "closed",
                    "reference": "57235",
                    "number": 2,
                    "scheduled": "2017-09-10T17:00:00+00:00",
                    "attendance": 68751,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sun & clouds Temp: 65 F, Humidity: 53%, Wind: NNE 6 mph",
                    "venue": {
                        "id": "e9e0828e-37fc-4238-a317-49037577dd55",
                        "name": "New Era Field",
                        "city": "Orchard Park",
                        "state": "NY",
                        "country": "USA",
                        "zip": "14127",
                        "address": "One Bills Drive",
                        "capacity": 73079,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 1
                    },
                    "away": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 21,
                        "away_points": 12
                    }
                },
                {
                    "id": "72436e91-8e19-4a9e-a6f5-cdc3ff6cc1fe",
                    "status": "closed",
                    "reference": "57240",
                    "number": 7,
                    "scheduled": "2017-09-10T17:00:00+00:00",
                    "attendance": 71710,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 80 F, Humidity: 49%, Wind: NNE 10 mph",
                    "venue": {
                        "id": "6a72e5ca-33d0-40af-8e6b-b32a4d3d9346",
                        "name": "NRG Stadium",
                        "city": "Houston",
                        "state": "TX",
                        "country": "USA",
                        "zip": "77054",
                        "address": "One Reliant Park",
                        "capacity": 71054,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 1
                    },
                    "away": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 7,
                        "away_points": 29
                    }
                },
                {
                    "id": "84e6ee01-6fe8-43b8-8a6c-6571e8d28516",
                    "status": "closed",
                    "reference": "57247",
                    "number": 14,
                    "scheduled": "2017-09-11T00:30:00+00:00",
                    "attendance": 93183,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "1e84213a-ff1f-4c9d-a003-8ee782b25a40",
                        "name": "AT&T Stadium",
                        "city": "Arlington",
                        "state": "TX",
                        "country": "USA",
                        "zip": "76011",
                        "address": "One Legends Way",
                        "capacity": 80000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 1
                    },
                    "away": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 19,
                        "away_points": 3
                    }
                },
                {
                    "id": "c82a4724-5c38-412a-bc00-7228cd619ea4",
                    "status": "closed",
                    "reference": "57239",
                    "number": 6,
                    "scheduled": "2017-09-10T17:00:00+00:00",
                    "attendance": 60957,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Controlled Climate Temp: 68 F, Humidity: 70%, Wind:   mph",
                    "venue": {
                        "id": "6e3bcf22-277d-4c06-b019-62aded51654f",
                        "name": "Ford Field",
                        "city": "Detroit",
                        "state": "MI",
                        "country": "USA",
                        "zip": "48226",
                        "address": "2000 Brush Street",
                        "capacity": 65000,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 1
                    },
                    "away": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 35,
                        "away_points": 23
                    }
                },
                {
                    "id": "d2c3b7b6-5ea7-4571-8206-ec96f77857ef",
                    "status": "closed",
                    "reference": "57234",
                    "number": 1,
                    "scheduled": "2017-09-08T00:30:00+00:00",
                    "attendance": 65878,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Clear and warm Temp: 63 F, Humidity: 77%, Wind: SW 8 mph",
                    "venue": {
                        "id": "e43310b1-cb82-4df9-8be5-e9b39637031b",
                        "name": "Gillette Stadium",
                        "city": "Foxborough",
                        "state": "MA",
                        "country": "USA",
                        "zip": "02035",
                        "address": "One Patriot Place",
                        "capacity": 68756,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 1
                    },
                    "away": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 27,
                        "away_points": 42
                    }
                },
                {
                    "id": "d58c4625-bf2d-44f9-ae45-f7081aea0687",
                    "status": "closed",
                    "reference": "57248",
                    "number": 15,
                    "scheduled": "2017-09-11T23:10:00+00:00",
                    "attendance": 66606,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Indoor Temp: 70 F, Wind:   mph",
                    "venue": {
                        "id": "f5ff00d4-1ed8-4918-bf73-13d66d510f98",
                        "name": "U.S. Bank Stadium",
                        "city": "Minneapolis",
                        "state": "MN",
                        "country": "USA",
                        "zip": "55415",
                        "address": "900 S 5th St",
                        "capacity": 66200,
                        "surface": "turf",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 1
                    },
                    "away": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 29,
                        "away_points": 19
                    }
                },
                {
                    "id": "d8634860-bbcf-47ac-acdf-7e4f621bcb8c",
                    "status": "closed",
                    "reference": "57242",
                    "number": 9,
                    "scheduled": "2017-09-10T17:00:00+00:00",
                    "attendance": 69089,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 72 F, Humidity: 50%, Wind: NE 7 mph",
                    "venue": {
                        "id": "5295c1b7-c85c-49cb-9569-1707c65324e5",
                        "name": "Nissan Stadium",
                        "city": "Nashville",
                        "state": "TN",
                        "country": "USA",
                        "zip": "37213",
                        "address": "One Titans Way",
                        "capacity": 69143,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 1
                    },
                    "away": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 16,
                        "away_points": 26
                    }
                },
                {
                    "id": "e3882005-f8e5-47b4-a3c0-9e3c193a9616",
                    "status": "closed",
                    "reference": "57244",
                    "number": 11,
                    "scheduled": "2017-09-10T20:05:00+00:00",
                    "attendance": 60128,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 90 F, Humidity: 38%, Wind: SW 2 mph",
                    "venue": {
                        "id": "4de1fcde-7c65-4145-a874-3753bdd584b7",
                        "name": "Los Angeles Memorial Coliseum",
                        "city": "Los Angeles",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90037",
                        "address": "3911 South Figueroa Street",
                        "capacity": 93607,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 1
                    },
                    "away": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 46,
                        "away_points": 9
                    }
                },
                {
                    "id": "e3bf7b46-f327-4620-9c1a-a39dbd335533",
                    "status": "closed",
                    "reference": "57245",
                    "number": 12,
                    "scheduled": "2017-09-10T20:25:00+00:00",
                    "attendance": 78381,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Mostly Sunny Temp: 71 F, Humidity: 49%, Wind: S 11 mph",
                    "venue": {
                        "id": "5a60dd3a-302c-41c6-ab0f-dd335c1103c2",
                        "name": "Lambeau Field",
                        "city": "Green Bay",
                        "state": "WI",
                        "country": "USA",
                        "zip": "54304",
                        "address": "1265 Lombardi Avenue",
                        "capacity": 80750,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 1
                    },
                    "away": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 9
                    }
                },
                {
                    "id": "e40fa39f-1708-4065-8ded-9be524c3e55e",
                    "status": "closed",
                    "reference": "57237",
                    "number": 4,
                    "scheduled": "2017-09-10T17:00:00+00:00",
                    "attendance": 55254,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 68 F, Humidity: 43%, Wind: SE 9 mph",
                    "venue": {
                        "id": "b87a1595-d3c8-48ea-8a53-0aab6378a64a",
                        "name": "Paul Brown Stadium",
                        "city": "Cincinnati",
                        "state": "OH",
                        "country": "USA",
                        "zip": "45202",
                        "address": "One Paul Brown Stadium",
                        "capacity": 65535,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 1
                    },
                    "away": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 0,
                        "away_points": 20
                    }
                },
                {
                    "id": "e9c32ef6-39ec-45ae-97e6-a7da444c9a8c",
                    "status": "closed",
                    "reference": "57246",
                    "number": 13,
                    "scheduled": "2017-09-10T20:25:00+00:00",
                    "attendance": 70178,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 87 F, Humidity: 43%, Wind: Northwest 8 mph",
                    "venue": {
                        "id": "24c71dbf-6469-46f1-8165-22005c295c8f",
                        "name": "Levi's Stadium",
                        "city": "Santa Clara",
                        "state": "CA",
                        "country": "USA",
                        "zip": "95054",
                        "address": "4900 Marie P DeBartolo Way",
                        "capacity": 68500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 1
                    },
                    "away": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 3,
                        "away_points": 23
                    }
                }
            ]
        },
        {
            "id": "a4e7eb23-a77a-4c94-993f-0bddb713c01f",
            "sequence": 2,
            "title": "2",
            "games": [
                {
                    "id": "09c26b84-3269-449a-8450-2cbefa422488",
                    "status": "closed",
                    "reference": "57250",
                    "number": 17,
                    "scheduled": "2017-09-15T00:25:00+00:00",
                    "attendance": 52942,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Mostly Coudy Temp: 69 F, Humidity: 78%, Wind: ENE 2 mph",
                    "venue": {
                        "id": "b87a1595-d3c8-48ea-8a53-0aab6378a64a",
                        "name": "Paul Brown Stadium",
                        "city": "Cincinnati",
                        "state": "OH",
                        "country": "USA",
                        "zip": "45202",
                        "address": "One Paul Brown Stadium",
                        "capacity": 65535,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 2
                    },
                    "away": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 9,
                        "away_points": 13
                    }
                },
                {
                    "id": "0d8f5318-5cf5-4b8f-97c5-a56fda6c4210",
                    "status": "closed",
                    "reference": "57251",
                    "number": 18,
                    "scheduled": "2017-09-17T17:00:00+00:00",
                    "attendance": 70605,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Mostly Cloudy Temp: 80 F, Humidity: 69%, Wind: ESE 2 mph",
                    "venue": {
                        "id": "d54faae0-0314-484f-8604-9d8dd08e1149",
                        "name": "M&T Bank Stadium",
                        "city": "Baltimore",
                        "state": "MD",
                        "country": "USA",
                        "zip": "21230",
                        "address": "1101 Russell Street",
                        "capacity": 71008,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 2
                    },
                    "away": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 10
                    }
                },
                {
                    "id": "1bc65a4a-fe9c-4ab6-8914-b59983922b7d",
                    "status": "closed",
                    "reference": "57257",
                    "number": 24,
                    "scheduled": "2017-09-17T17:00:00+00:00",
                    "attendance": 65971,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 78 F, Humidity: 64%, Wind: From S 1 mph",
                    "venue": {
                        "id": "7349a2e6-0ac9-410b-8bd2-ca58c9f7aa34",
                        "name": "Heinz Field",
                        "city": "Pittsburgh",
                        "state": "PA",
                        "country": "USA",
                        "zip": "15212",
                        "address": "100 Art Rooney Avenue",
                        "capacity": 65050,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 2
                    },
                    "away": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 26,
                        "away_points": 9
                    }
                },
                {
                    "id": "246a7239-24a4-4227-8eb7-b5907f1e8d5b",
                    "status": "closed",
                    "reference": "57260",
                    "number": 27,
                    "scheduled": "2017-09-17T20:05:00+00:00",
                    "attendance": 54729,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 70 F, Humidity: 57%, Wind: South 6 mph",
                    "venue": {
                        "id": "ba1ad00f-9130-462c-93f9-5612a0015117",
                        "name": "Oakland Alameda Coliseum",
                        "city": "Oakland",
                        "state": "CA",
                        "country": "USA",
                        "zip": "94621",
                        "address": "7000 Coliseum Way",
                        "capacity": 53200,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 2
                    },
                    "away": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 45,
                        "away_points": 20
                    }
                },
                {
                    "id": "2a4efc7b-f056-410e-9223-5efcec57c41f",
                    "status": "closed",
                    "reference": "57256",
                    "number": 23,
                    "scheduled": "2017-09-17T17:00:00+00:00",
                    "attendance": 73168,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Mostly Cloudy Temp: 85 F, Humidity: 75%, Wind: NNE 7 mph",
                    "venue": {
                        "id": "3c85d89a-ec66-4983-acd5-1381d6c8673a",
                        "name": "Mercedes-Benz Superdome",
                        "city": "New Orleans",
                        "state": "LA",
                        "country": "USA",
                        "zip": "70112",
                        "address": "1500 Sugar Bowl Drive",
                        "capacity": 76468,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 2
                    },
                    "away": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 20,
                        "away_points": 36
                    }
                },
                {
                    "id": "829303a7-6282-4719-b91f-47648a7dc2b7",
                    "status": "closed",
                    "reference": "57258",
                    "number": 25,
                    "scheduled": "2017-09-17T17:00:00+00:00",
                    "attendance": 56640,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 88 F, Humidity: 62%, Wind: NE 7 mph",
                    "venue": {
                        "id": "6fccc39c-80bc-4c81-83d9-2d5a848c8c09",
                        "name": "Raymond James Stadium",
                        "city": "Tampa",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33607",
                        "address": "4201 North Dale Mabry Highway",
                        "capacity": 65890,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 1
                    },
                    "away": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 29,
                        "away_points": 7
                    }
                },
                {
                    "id": "953a076e-bd60-41db-b39f-b54f5fc8f73b",
                    "status": "closed",
                    "reference": "57254",
                    "number": 21,
                    "scheduled": "2017-09-17T17:00:00+00:00",
                    "attendance": 61709,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 84 F, Humidity: 59%, Wind: NW 10 mph",
                    "venue": {
                        "id": "4c5c036d-dd3d-4183-b595-71a43a97560f",
                        "name": "EverBank Field",
                        "city": "Jacksonville",
                        "state": "FL",
                        "country": "USA",
                        "zip": "32202",
                        "address": "One EverBank Field Drive",
                        "capacity": 67246,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 2
                    },
                    "away": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 16,
                        "away_points": 37
                    }
                },
                {
                    "id": "c30e7df5-866c-47bb-829b-3c7f7ed32aca",
                    "status": "closed",
                    "reference": "57253",
                    "number": 20,
                    "scheduled": "2017-09-17T17:00:00+00:00",
                    "attendance": 63137,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Partly sunny Temp: 78 F, Humidity: 66%, Wind: SSW 6 mph",
                    "venue": {
                        "id": "6ed18563-53e0-46c2-a91d-12d73a16456d",
                        "name": "Lucas Oil Stadium",
                        "city": "Indianapolis",
                        "state": "IN",
                        "country": "USA",
                        "zip": "46225",
                        "address": "500 South Capitol Avenue",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 2
                    },
                    "away": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 13,
                        "away_points": 16
                    }
                },
                {
                    "id": "c33ae109-e733-4dbd-94d5-c4c0a884ed16",
                    "status": "closed",
                    "reference": "57255",
                    "number": 22,
                    "scheduled": "2017-09-17T17:00:00+00:00",
                    "attendance": 74971,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 68 F, Humidity: 95%, Wind: Northeast 7 mph",
                    "venue": {
                        "id": "2ec4c411-dac2-403d-b091-6b6aa4a0a914",
                        "name": "Arrowhead Stadium",
                        "city": "Kansas City",
                        "state": "MO",
                        "country": "USA",
                        "zip": "64129",
                        "address": "One Arrowhead Drive",
                        "capacity": 79451,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 2
                    },
                    "away": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 27,
                        "away_points": 20
                    }
                },
                {
                    "id": "c9836c4a-577f-4b32-a73d-80989cd4332c",
                    "status": "closed",
                    "reference": "57261",
                    "number": 28,
                    "scheduled": "2017-09-17T20:25:00+00:00",
                    "attendance": 76919,
                    "utc_offset": -7,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 72 F, Humidity: 41%, Wind: SouthWest 5 mph",
                    "venue": {
                        "id": "6589e61d-ef1e-4e30-91b5-9acd2072b8a0",
                        "name": "Sports Authority Field at Mile High",
                        "city": "Denver",
                        "state": "CO",
                        "country": "USA",
                        "zip": "80204",
                        "address": "1701 Mile High Stadium Circle",
                        "capacity": 76125,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 2
                    },
                    "away": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 42,
                        "away_points": 17
                    }
                },
                {
                    "id": "cd54dbab-6769-4c11-9bca-e8373f78173a",
                    "status": "closed",
                    "reference": "57252",
                    "number": 19,
                    "scheduled": "2017-09-17T17:00:00+00:00",
                    "attendance": 74122,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 83 F, Humidity: 60%, Wind: North 5 mph",
                    "venue": {
                        "id": "39be9ed4-3292-49ac-8699-a381de3a4969",
                        "name": "Bank of America Stadium",
                        "city": "Charlotte",
                        "state": "NC",
                        "country": "USA",
                        "zip": "28202",
                        "address": "800 South Mint Street",
                        "capacity": 73778,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 2
                    },
                    "away": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 9,
                        "away_points": 3
                    }
                },
                {
                    "id": "cdaaefbc-cc4d-41af-b85a-bf06013e15a2",
                    "status": "closed",
                    "reference": "57264",
                    "number": 31,
                    "scheduled": "2017-09-18T00:30:00+00:00",
                    "attendance": 70826,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 78 F, Humidity: 72%, Wind: NW 4 mph",
                    "venue": {
                        "id": "1f2d393b-2291-4835-a412-0714966ea245",
                        "name": "Mercedes-Benz Stadium",
                        "city": "Atlanta",
                        "state": "GA",
                        "country": "USA",
                        "zip": "30313",
                        "address": "441 Martin Luther King Jr Dr NW",
                        "capacity": 71000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 2
                    },
                    "away": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 34,
                        "away_points": 23
                    }
                },
                {
                    "id": "f37339c3-6d36-47ba-932b-8973dd6e5dda",
                    "status": "closed",
                    "reference": "57265",
                    "number": 32,
                    "scheduled": "2017-09-19T00:30:00+00:00",
                    "attendance": 77004,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 72 F, Humidity: 80%, Wind: ENE 5 mph",
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 2
                    },
                    "away": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 10,
                        "away_points": 24
                    }
                },
                {
                    "id": "faf1f598-44ed-43e1-8418-2798348ed450",
                    "status": "closed",
                    "reference": "57263",
                    "number": 30,
                    "scheduled": "2017-09-17T20:25:00+00:00",
                    "attendance": 68729,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Mostly Cloudy Temp: 65 F, Humidity: 60%, Wind: S 12 mph",
                    "venue": {
                        "id": "c6b9e5df-c9e4-434c-b3e6-83928f11cbda",
                        "name": "CenturyLink Field",
                        "city": "Seattle",
                        "state": "WA",
                        "country": "USA",
                        "zip": "98134",
                        "address": "800 Occidental Avenue South",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 2
                    },
                    "away": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 12,
                        "away_points": 9
                    }
                },
                {
                    "id": "fb23cdcd-860a-4e41-8286-0bf4672a4ef0",
                    "status": "closed",
                    "reference": "57259",
                    "number": 26,
                    "scheduled": "2017-09-17T20:05:00+00:00",
                    "attendance": 25381,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 71 F, Humidity: 68%, Wind: W 7 mph",
                    "venue": {
                        "id": "feebab00-4a3e-4123-8d7c-ed0dbdd96540",
                        "name": "StubHub Center",
                        "city": "Carson",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90746",
                        "address": "18400 Avalon Boulevard",
                        "capacity": 27000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 2
                    },
                    "away": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 1
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 19
                    }
                },
                {
                    "id": "ff3276b5-00f1-4987-82f2-1eff5ffd295f",
                    "status": "closed",
                    "reference": "57262",
                    "number": 29,
                    "scheduled": "2017-09-17T20:25:00+00:00",
                    "attendance": 56612,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 76 F, Humidity: 59%, Wind: SW 3 mph",
                    "venue": {
                        "id": "4de1fcde-7c65-4145-a874-3753bdd584b7",
                        "name": "Los Angeles Memorial Coliseum",
                        "city": "Los Angeles",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90037",
                        "address": "3911 South Figueroa Street",
                        "capacity": 93607,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 2
                    },
                    "away": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 20,
                        "away_points": 27
                    }
                }
            ]
        },
        {
            "id": "ad0d19aa-67ee-4962-bfc4-77726e8cdaf2",
            "sequence": 3,
            "title": "3",
            "games": [
                {
                    "id": "07fa1395-5af9-4f45-8060-34689085c052",
                    "status": "closed",
                    "reference": "57277",
                    "number": 44,
                    "scheduled": "2017-09-24T20:05:00+00:00",
                    "attendance": 69127,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 88 F, Humidity: 97%, Wind: E 4 mph",
                    "venue": {
                        "id": "5295c1b7-c85c-49cb-9569-1707c65324e5",
                        "name": "Nissan Stadium",
                        "city": "Nashville",
                        "state": "TN",
                        "country": "USA",
                        "zip": "37213",
                        "address": "One Titans Way",
                        "capacity": 69143,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 3
                    },
                    "away": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 33,
                        "away_points": 27
                    }
                },
                {
                    "id": "0af4a24c-4a58-400b-86c0-2670ba791634",
                    "status": "closed",
                    "reference": "57269",
                    "number": 36,
                    "scheduled": "2017-09-24T17:00:00+00:00",
                    "attendance": 73775,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 82 F, Humidity: 59%, Wind: NNE 6 mph",
                    "venue": {
                        "id": "39be9ed4-3292-49ac-8699-a381de3a4969",
                        "name": "Bank of America Stadium",
                        "city": "Charlotte",
                        "state": "NC",
                        "country": "USA",
                        "zip": "28202",
                        "address": "800 South Mint Street",
                        "capacity": 73778,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 3
                    },
                    "away": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 13,
                        "away_points": 34
                    }
                },
                {
                    "id": "0e8e53e4-3e77-47a5-9e9e-339b0d2eb980",
                    "status": "closed",
                    "reference": "57278",
                    "number": 45,
                    "scheduled": "2017-09-24T20:25:00+00:00",
                    "attendance": 78323,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 89 F, Humidity: 44%, Wind: S 8 mph",
                    "venue": {
                        "id": "5a60dd3a-302c-41c6-ab0f-dd335c1103c2",
                        "name": "Lambeau Field",
                        "city": "Green Bay",
                        "state": "WI",
                        "country": "USA",
                        "zip": "54304",
                        "address": "1265 Lombardi Avenue",
                        "capacity": 80750,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 3
                    },
                    "away": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 27,
                        "away_points": 24
                    }
                },
                {
                    "id": "0ec23489-6d4e-4272-a7a1-65fc6aa8fadc",
                    "status": "closed",
                    "reference": "57268",
                    "number": 35,
                    "scheduled": "2017-09-24T17:00:00+00:00",
                    "attendance": 68865,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 83 F, Humidity: 56%, Wind: W 5 mph",
                    "venue": {
                        "id": "e9e0828e-37fc-4238-a317-49037577dd55",
                        "name": "New Era Field",
                        "city": "Orchard Park",
                        "state": "NY",
                        "country": "USA",
                        "zip": "14127",
                        "address": "One Bills Drive",
                        "capacity": 73079,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 3
                    },
                    "away": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 26,
                        "away_points": 16
                    }
                },
                {
                    "id": "4578f92d-be29-49ff-a49b-e1c192259cba",
                    "status": "closed",
                    "reference": "57272",
                    "number": 39,
                    "scheduled": "2017-09-24T17:00:00+00:00",
                    "attendance": 63351,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 83 F, Humidity: 45%, Wind: ENE 5 mph",
                    "venue": {
                        "id": "6ed18563-53e0-46c2-a91d-12d73a16456d",
                        "name": "Lucas Oil Stadium",
                        "city": "Indianapolis",
                        "state": "IN",
                        "country": "USA",
                        "zip": "46225",
                        "address": "500 South Capitol Avenue",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 3
                    },
                    "away": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 31,
                        "away_points": 28
                    }
                },
                {
                    "id": "4720edf3-0b9c-4ca9-9251-196d5295ac1f",
                    "status": "closed",
                    "reference": "57270",
                    "number": 37,
                    "scheduled": "2017-09-24T17:00:00+00:00",
                    "attendance": 62172,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny, highs to upper 80's Temp: 89 F, Humidity: 48%, Wind: S 5 mph",
                    "venue": {
                        "id": "d7866605-5ac6-4b3a-90e8-760cc5a26b75",
                        "name": "Soldier Field",
                        "city": "Chicago",
                        "state": "IL",
                        "country": "USA",
                        "zip": "60605",
                        "address": "1410 South Museum Campus Drive",
                        "capacity": 61500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 3
                    },
                    "away": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 23,
                        "away_points": 17
                    }
                },
                {
                    "id": "4b049133-1c35-400f-9988-2edd362c756d",
                    "status": "closed",
                    "reference": "57279",
                    "number": 46,
                    "scheduled": "2017-09-24T20:25:00+00:00",
                    "attendance": 25386,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 84 F, Humidity: 25%, Wind: W 5 mph",
                    "venue": {
                        "id": "feebab00-4a3e-4123-8d7c-ed0dbdd96540",
                        "name": "StubHub Center",
                        "city": "Carson",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90746",
                        "address": "18400 Avalon Boulevard",
                        "capacity": 27000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 3
                    },
                    "away": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 10,
                        "away_points": 24
                    }
                },
                {
                    "id": "6c74a897-52a1-4a8d-9e89-0446961cfb87",
                    "status": "closed",
                    "reference": "57281",
                    "number": 48,
                    "scheduled": "2017-09-26T00:30:00+00:00",
                    "attendance": 65102,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Indoor Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "f8e4d2ab-53f9-4e9f-8c4a-9bc278094a84",
                        "name": "University of Phoenix Stadium",
                        "city": "Glendale",
                        "state": "AZ",
                        "country": "USA",
                        "zip": "85305",
                        "address": "One Cardinals Drive",
                        "capacity": 63400,
                        "surface": "turf",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 3
                    },
                    "away": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 28
                    }
                },
                {
                    "id": "8646de0e-6c5f-46d8-bba2-a0808e9c4031",
                    "status": "closed",
                    "reference": "57276",
                    "number": 43,
                    "scheduled": "2017-09-24T17:00:00+00:00",
                    "attendance": 69596,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 89 F, Humidity: 48%, Wind: NNE 6 mph",
                    "venue": {
                        "id": "4fa8c29c-6626-464c-8540-314ed7535e1b",
                        "name": "Lincoln Financial Field",
                        "city": "Philadelphia",
                        "state": "PA",
                        "country": "USA",
                        "zip": "19148",
                        "address": "1020 Pattison Avenue",
                        "capacity": 68532,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 3
                    },
                    "away": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 27,
                        "away_points": 24
                    }
                },
                {
                    "id": "8bba9eec-55c9-44ec-9c01-5db0836c7df4",
                    "status": "closed",
                    "reference": "57266",
                    "number": 33,
                    "scheduled": "2017-09-22T00:25:00+00:00",
                    "attendance": 70178,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Mostly Sunny Temp: 68 F, Humidity: 42%, Wind: West-Southwest 13 mph",
                    "venue": {
                        "id": "24c71dbf-6469-46f1-8165-22005c295c8f",
                        "name": "Levi's Stadium",
                        "city": "Santa Clara",
                        "state": "CA",
                        "country": "USA",
                        "zip": "95054",
                        "address": "4900 Marie P DeBartolo Way",
                        "capacity": 68500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 3
                    },
                    "away": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 39,
                        "away_points": 41
                    }
                },
                {
                    "id": "bb9e95f1-cc9e-4c64-9ff1-8ce40f845521",
                    "status": "closed",
                    "reference": "57274",
                    "number": 41,
                    "scheduled": "2017-09-24T17:00:00+00:00",
                    "attendance": 65878,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 84 F, Humidity: 53%, Wind: NE 4 mph",
                    "venue": {
                        "id": "e43310b1-cb82-4df9-8be5-e9b39637031b",
                        "name": "Gillette Stadium",
                        "city": "Foxborough",
                        "state": "MA",
                        "country": "USA",
                        "zip": "02035",
                        "address": "One Patriot Place",
                        "capacity": 68756,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 3
                    },
                    "away": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 36,
                        "away_points": 33
                    }
                },
                {
                    "id": "bc848438-9106-44a0-be3b-c77161cb0c5a",
                    "status": "closed",
                    "reference": "57280",
                    "number": 47,
                    "scheduled": "2017-09-25T00:30:00+00:00",
                    "attendance": 77123,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 76 F, Humidity: 75%, Wind:  0 mph",
                    "venue": {
                        "id": "7c11bb2d-4a53-4842-b842-0f1c63ed78e9",
                        "name": "FedExField",
                        "city": "Landover",
                        "state": "MD",
                        "country": "USA",
                        "zip": "20785",
                        "address": "1600 FedEx Way",
                        "capacity": 83000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 3
                    },
                    "away": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 27,
                        "away_points": 10
                    }
                },
                {
                    "id": "be53a7b4-1add-472b-85e3-21271f1b52bc",
                    "status": "closed",
                    "reference": "57275",
                    "number": 42,
                    "scheduled": "2017-09-24T17:00:00+00:00",
                    "attendance": 77562,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 88 F, Humidity: 41%, Wind: NE 5 mph",
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 3
                    },
                    "away": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 20,
                        "away_points": 6
                    }
                },
                {
                    "id": "c305639f-e37f-435f-9ab4-a095d86faf78",
                    "status": "closed",
                    "reference": "57271",
                    "number": 38,
                    "scheduled": "2017-09-24T17:00:00+00:00",
                    "attendance": 63240,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Controlled Climate Temp: 68 F, Humidity: 70%, Wind:   mph",
                    "venue": {
                        "id": "6e3bcf22-277d-4c06-b019-62aded51654f",
                        "name": "Ford Field",
                        "city": "Detroit",
                        "state": "MI",
                        "country": "USA",
                        "zip": "48226",
                        "address": "2000 Brush Street",
                        "capacity": 65000,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 3
                    },
                    "away": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 26,
                        "away_points": 30
                    }
                },
                {
                    "id": "c8225358-094d-4ae2-9b0e-f6d2fc2ce0ca",
                    "status": "closed",
                    "reference": "57267",
                    "number": 34,
                    "scheduled": "2017-09-24T13:30:00+00:00",
                    "attendance": 84592,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": "Temp: 70 F, Humidity: 51%, Wind: SE 10 mph",
                    "venue": {
                        "id": "ddaebc21-35aa-4f00-bef6-3f0bc7e76f70",
                        "name": "Wembley Stadium",
                        "city": "London",
                        "state": null,
                        "country": "UK",
                        "zip": "HA9 0WS",
                        "address": "Wembley Stadium",
                        "capacity": 90000,
                        "surface": "turf",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 3
                    },
                    "away": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 3
                    },
                    "scoring": {
                        "home_points": 44,
                        "away_points": 7
                    }
                },
                {
                    "id": "e14feb06-19a6-4bf1-af6a-d77fbfa6d7b7",
                    "status": "closed",
                    "reference": "57273",
                    "number": 40,
                    "scheduled": "2017-09-24T17:00:00+00:00",
                    "attendance": 66390,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Indoor Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "f5ff00d4-1ed8-4918-bf73-13d66d510f98",
                        "name": "U.S. Bank Stadium",
                        "city": "Minneapolis",
                        "state": "MN",
                        "country": "USA",
                        "zip": "55415",
                        "address": "900 S 5th St",
                        "capacity": 66200,
                        "surface": "turf",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 3
                    },
                    "away": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 2
                    },
                    "scoring": {
                        "home_points": 34,
                        "away_points": 17
                    }
                }
            ]
        },
        {
            "id": "8db8eed1-8f0b-4c75-9f75-69c9ceedd4ab",
            "sequence": 4,
            "title": "4",
            "games": [
                {
                    "id": "527260d0-0935-4bd9-b626-496de172142e",
                    "status": "closed",
                    "reference": "57295",
                    "number": 62,
                    "scheduled": "2017-10-01T20:25:00+00:00",
                    "attendance": 76909,
                    "utc_offset": -7,
                    "entry_mode": "INGEST",
                    "weather": "Mostly Cloudy Temp: 72 F, Humidity: 26%, Wind: E 7 mph",
                    "venue": {
                        "id": "6589e61d-ef1e-4e30-91b5-9acd2072b8a0",
                        "name": "Sports Authority Field at Mile High",
                        "city": "Denver",
                        "state": "CO",
                        "country": "USA",
                        "zip": "80204",
                        "address": "1701 Mile High Stadium Circle",
                        "capacity": 76125,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 4
                    },
                    "away": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 16,
                        "away_points": 10
                    }
                },
                {
                    "id": "5288ab63-e6dc-4b60-a747-ba30f9375677",
                    "status": "closed",
                    "reference": "57282",
                    "number": 49,
                    "scheduled": "2017-09-29T00:25:00+00:00",
                    "attendance": 78362,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 65 F, Humidity: 62%, Wind: SW 5 mph",
                    "venue": {
                        "id": "5a60dd3a-302c-41c6-ab0f-dd335c1103c2",
                        "name": "Lambeau Field",
                        "city": "Green Bay",
                        "state": "WI",
                        "country": "USA",
                        "zip": "54304",
                        "address": "1265 Lombardi Avenue",
                        "capacity": 80750,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 4
                    },
                    "away": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 35,
                        "away_points": 14
                    }
                },
                {
                    "id": "55abe8d4-11be-472f-a76e-bbb6e9b0480e",
                    "status": "closed",
                    "reference": "57286",
                    "number": 53,
                    "scheduled": "2017-10-01T17:00:00+00:00",
                    "attendance": 67431,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 63 F, Humidity: 37%, Wind: SE 6 mph",
                    "venue": {
                        "id": "90c38d91-3774-4f5d-82ca-1c806828219f",
                        "name": "FirstEnergy Stadium",
                        "city": "Cleveland",
                        "state": "OH",
                        "country": "USA",
                        "zip": "44114",
                        "address": "100 Alfred Lerner Way",
                        "capacity": 71516,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 4
                    },
                    "away": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 7,
                        "away_points": 31
                    }
                },
                {
                    "id": "6bc6e76d-dabe-4259-9c94-51c280dc7d7b",
                    "status": "closed",
                    "reference": "57284",
                    "number": 51,
                    "scheduled": "2017-10-01T17:00:00+00:00",
                    "attendance": 71273,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "1f2d393b-2291-4835-a412-0714966ea245",
                        "name": "Mercedes-Benz Stadium",
                        "city": "Atlanta",
                        "state": "GA",
                        "country": "USA",
                        "zip": "30313",
                        "address": "441 Martin Luther King Jr Dr NW",
                        "capacity": 71000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 4
                    },
                    "away": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 23
                    }
                },
                {
                    "id": "7bd4d20d-614b-480b-804a-0f42de2da5bb",
                    "status": "closed",
                    "reference": "57285",
                    "number": 52,
                    "scheduled": "2017-10-01T17:00:00+00:00",
                    "attendance": 71126,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 64 F, Humidity: 52%, Wind: North 6 mph",
                    "venue": {
                        "id": "d54faae0-0314-484f-8604-9d8dd08e1149",
                        "name": "M&T Bank Stadium",
                        "city": "Baltimore",
                        "state": "MD",
                        "country": "USA",
                        "zip": "21230",
                        "address": "1101 Russell Street",
                        "capacity": 71008,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 4
                    },
                    "away": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 9,
                        "away_points": 26
                    }
                },
                {
                    "id": "873b442e-09f7-4713-a952-ba9a86d19e47",
                    "status": "closed",
                    "reference": "57292",
                    "number": 59,
                    "scheduled": "2017-10-01T20:05:00+00:00",
                    "attendance": 64121,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Indoors Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "f8e4d2ab-53f9-4e9f-8c4a-9bc278094a84",
                        "name": "University of Phoenix Stadium",
                        "city": "Glendale",
                        "state": "AZ",
                        "country": "USA",
                        "zip": "85305",
                        "address": "One Cardinals Drive",
                        "capacity": 63400,
                        "surface": "turf",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 4
                    },
                    "away": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 18,
                        "away_points": 15
                    }
                },
                {
                    "id": "8f3d6868-6ed9-43a3-bacc-bac20764ce23",
                    "status": "closed",
                    "reference": "57296",
                    "number": 63,
                    "scheduled": "2017-10-02T00:30:00+00:00",
                    "attendance": 68872,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 61 F, Humidity: 54%, Wind: NNW 8 mph",
                    "venue": {
                        "id": "c6b9e5df-c9e4-434c-b3e6-83928f11cbda",
                        "name": "CenturyLink Field",
                        "city": "Seattle",
                        "state": "WA",
                        "country": "USA",
                        "zip": "98134",
                        "address": "800 Occidental Avenue South",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 4
                    },
                    "away": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 46,
                        "away_points": 18
                    }
                },
                {
                    "id": "91843445-5702-4819-b5af-4c8581ee7b01",
                    "status": "closed",
                    "reference": "57289",
                    "number": 56,
                    "scheduled": "2017-10-01T17:00:00+00:00",
                    "attendance": 66730,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Indoor Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "f5ff00d4-1ed8-4918-bf73-13d66d510f98",
                        "name": "U.S. Bank Stadium",
                        "city": "Minneapolis",
                        "state": "MN",
                        "country": "USA",
                        "zip": "55415",
                        "address": "900 S 5th St",
                        "capacity": 66200,
                        "surface": "turf",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 4
                    },
                    "away": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 7,
                        "away_points": 14
                    }
                },
                {
                    "id": "986557a8-6009-41ec-bb36-ae47a3379006",
                    "status": "closed",
                    "reference": "57283",
                    "number": 50,
                    "scheduled": "2017-10-01T13:30:00+00:00",
                    "attendance": 84423,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": "Temp: 63 F, Humidity: 64%, Wind: 13 SSW mph",
                    "venue": {
                        "id": "ddaebc21-35aa-4f00-bef6-3f0bc7e76f70",
                        "name": "Wembley Stadium",
                        "city": "London",
                        "state": null,
                        "country": "UK",
                        "zip": "HA9 0WS",
                        "address": "Wembley Stadium",
                        "capacity": 90000,
                        "surface": "turf",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 3
                    },
                    "away": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 0,
                        "away_points": 20
                    }
                },
                {
                    "id": "9c18eb1c-f211-4d96-b041-4a279ce770b6",
                    "status": "closed",
                    "reference": "57291",
                    "number": 58,
                    "scheduled": "2017-10-01T17:00:00+00:00",
                    "attendance": 77562,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 64 F, Humidity: 44%, Wind: NNE 5 mph",
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 4
                    },
                    "away": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 23,
                        "away_points": 20
                    }
                },
                {
                    "id": "bc23ee90-c478-4712-94df-bc9ed4dbe793",
                    "status": "closed",
                    "reference": "57288",
                    "number": 55,
                    "scheduled": "2017-10-01T17:00:00+00:00",
                    "attendance": 71804,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 87 F, Humidity: 53%, Wind: N 3 mph",
                    "venue": {
                        "id": "6a72e5ca-33d0-40af-8e6b-b32a4d3d9346",
                        "name": "NRG Stadium",
                        "city": "Houston",
                        "state": "TX",
                        "country": "USA",
                        "zip": "77054",
                        "address": "One Reliant Park",
                        "capacity": 71054,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 4
                    },
                    "away": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 57,
                        "away_points": 14
                    }
                },
                {
                    "id": "bc2ce96b-6bd7-40f2-84c2-5ccbd5691e86",
                    "status": "closed",
                    "reference": "57287",
                    "number": 54,
                    "scheduled": "2017-10-01T17:00:00+00:00",
                    "attendance": 91869,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "1e84213a-ff1f-4c9d-a003-8ee782b25a40",
                        "name": "AT&T Stadium",
                        "city": "Arlington",
                        "state": "TX",
                        "country": "USA",
                        "zip": "76011",
                        "address": "One Legends Way",
                        "capacity": 80000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 4
                    },
                    "away": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 30,
                        "away_points": 35
                    }
                },
                {
                    "id": "c0e7cf37-0302-42b1-a94f-50e029a6aae3",
                    "status": "closed",
                    "reference": "57290",
                    "number": 57,
                    "scheduled": "2017-10-01T17:00:00+00:00",
                    "attendance": 65878,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 61 F, Humidity: 43%, Wind: N 6 mph",
                    "venue": {
                        "id": "e43310b1-cb82-4df9-8be5-e9b39637031b",
                        "name": "Gillette Stadium",
                        "city": "Foxborough",
                        "state": "MA",
                        "country": "USA",
                        "zip": "02035",
                        "address": "One Patriot Place",
                        "capacity": 68756,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 4
                    },
                    "away": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 30,
                        "away_points": 33
                    }
                },
                {
                    "id": "c138104b-f475-4896-801f-b23b14f9c793",
                    "status": "closed",
                    "reference": "57293",
                    "number": 60,
                    "scheduled": "2017-10-01T20:05:00+00:00",
                    "attendance": 25374,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 72 F, Humidity: 70%, Wind: S 6 mph",
                    "venue": {
                        "id": "feebab00-4a3e-4123-8d7c-ed0dbdd96540",
                        "name": "StubHub Center",
                        "city": "Carson",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90746",
                        "address": "18400 Avalon Boulevard",
                        "capacity": 27000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 4
                    },
                    "away": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 26
                    }
                },
                {
                    "id": "d9f4c342-bf1a-4491-828a-004e8726cf10",
                    "status": "closed",
                    "reference": "57297",
                    "number": 64,
                    "scheduled": "2017-10-03T00:30:00+00:00",
                    "attendance": 74587,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 82 F, Humidity: 55%, Wind: South Southeast 14 mph",
                    "venue": {
                        "id": "2ec4c411-dac2-403d-b091-6b6aa4a0a914",
                        "name": "Arrowhead Stadium",
                        "city": "Kansas City",
                        "state": "MO",
                        "country": "USA",
                        "zip": "64129",
                        "address": "One Arrowhead Drive",
                        "capacity": 79451,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 4
                    },
                    "away": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 29,
                        "away_points": 20
                    }
                },
                {
                    "id": "e32c946e-3503-4d2c-9c4f-a480c3e6f53a",
                    "status": "closed",
                    "reference": "57294",
                    "number": 61,
                    "scheduled": "2017-10-01T20:05:00+00:00",
                    "attendance": 63916,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 91 F, Humidity: 51%, Wind: E 8 mph",
                    "venue": {
                        "id": "6fccc39c-80bc-4c81-83d9-2d5a848c8c09",
                        "name": "Raymond James Stadium",
                        "city": "Tampa",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33607",
                        "address": "4201 North Dale Mabry Highway",
                        "capacity": 65890,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 3
                    },
                    "away": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 4
                    },
                    "scoring": {
                        "home_points": 25,
                        "away_points": 23
                    }
                }
            ]
        },
        {
            "id": "918f3593-13dc-4bb2-941d-7c7b5da75616",
            "sequence": 5,
            "title": "5",
            "games": [
                {
                    "id": "13584eca-c7d1-439f-a517-be46c251d489",
                    "status": "closed",
                    "reference": "57302",
                    "number": 69,
                    "scheduled": "2017-10-08T17:00:00+00:00",
                    "attendance": 65612,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 68 F, Humidity: 60%, Wind: ESE 12 mph",
                    "venue": {
                        "id": "6ed18563-53e0-46c2-a91d-12d73a16456d",
                        "name": "Lucas Oil Stadium",
                        "city": "Indianapolis",
                        "state": "IN",
                        "country": "USA",
                        "zip": "46225",
                        "address": "500 South Capitol Avenue",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 5
                    },
                    "away": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 26,
                        "away_points": 23
                    }
                },
                {
                    "id": "1f81f519-8535-4037-961e-a39e3ed57a90",
                    "status": "closed",
                    "reference": "57298",
                    "number": 65,
                    "scheduled": "2017-10-06T00:25:00+00:00",
                    "attendance": 64476,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 76 F, Humidity: 94%, Wind: E 9 mph",
                    "venue": {
                        "id": "6fccc39c-80bc-4c81-83d9-2d5a848c8c09",
                        "name": "Raymond James Stadium",
                        "city": "Tampa",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33607",
                        "address": "4201 North Dale Mabry Highway",
                        "capacity": 65890,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 4
                    },
                    "away": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 14,
                        "away_points": 19
                    }
                },
                {
                    "id": "2516af69-4bf9-49d3-844b-c04c4ce37099",
                    "status": "closed",
                    "reference": "57304",
                    "number": 71,
                    "scheduled": "2017-10-08T17:00:00+00:00",
                    "attendance": 77373,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 76 F, Humidity: 90%, Wind: WSW 7 mph",
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 5
                    },
                    "away": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 22,
                        "away_points": 27
                    }
                },
                {
                    "id": "357f73b9-ddd3-4346-aa1d-21aa4804ca78",
                    "status": "closed",
                    "reference": "57303",
                    "number": 70,
                    "scheduled": "2017-10-08T17:00:00+00:00",
                    "attendance": 65135,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 89 F, Humidity: 67%, Wind: ESE 9 mph",
                    "venue": {
                        "id": "50a5c833-1570-4c38-abc7-7914cf87dbde",
                        "name": "Hard Rock Stadium",
                        "city": "Miami Gardens",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33056",
                        "address": "2269 Northwest 199th Street",
                        "capacity": 76100,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 4
                    },
                    "away": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 16,
                        "away_points": 10
                    }
                },
                {
                    "id": "3f69efad-89f2-421d-903f-dce05e371d63",
                    "status": "closed",
                    "reference": "57306",
                    "number": 73,
                    "scheduled": "2017-10-08T17:00:00+00:00",
                    "attendance": 66237,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 73 F, Humidity: 80%, Wind: From SW 9 mph",
                    "venue": {
                        "id": "7349a2e6-0ac9-410b-8bd2-ca58c9f7aa34",
                        "name": "Heinz Field",
                        "city": "Pittsburgh",
                        "state": "PA",
                        "country": "USA",
                        "zip": "15212",
                        "address": "100 Art Rooney Avenue",
                        "capacity": 65050,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 5
                    },
                    "away": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 9,
                        "away_points": 30
                    }
                },
                {
                    "id": "5d629efb-b1f1-44e5-bb0a-3c20061b7e3e",
                    "status": "closed",
                    "reference": "57308",
                    "number": 75,
                    "scheduled": "2017-10-08T20:05:00+00:00",
                    "attendance": 54980,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 72 F, Humidity: 42%, Wind: WNW 8 mph",
                    "venue": {
                        "id": "ba1ad00f-9130-462c-93f9-5612a0015117",
                        "name": "Oakland Alameda Coliseum",
                        "city": "Oakland",
                        "state": "CA",
                        "country": "USA",
                        "zip": "94621",
                        "address": "7000 Coliseum Way",
                        "capacity": 53200,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 5
                    },
                    "away": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 30
                    }
                },
                {
                    "id": "681c7c3c-d01b-4512-b832-2222a325091b",
                    "status": "closed",
                    "reference": "57299",
                    "number": 66,
                    "scheduled": "2017-10-08T17:00:00+00:00",
                    "attendance": 52367,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Light Rain Temp: 63 F, Humidity: 90%, Wind: SSE 10 mph",
                    "venue": {
                        "id": "b87a1595-d3c8-48ea-8a53-0aab6378a64a",
                        "name": "Paul Brown Stadium",
                        "city": "Cincinnati",
                        "state": "OH",
                        "country": "USA",
                        "zip": "45202",
                        "address": "One Paul Brown Stadium",
                        "capacity": 65535,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 5
                    },
                    "away": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 20,
                        "away_points": 16
                    }
                },
                {
                    "id": "75c186f4-77f0-487a-9b82-82427d467d34",
                    "status": "closed",
                    "reference": "57307",
                    "number": 74,
                    "scheduled": "2017-10-08T20:05:00+00:00",
                    "attendance": 60745,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 78 F, Humidity: 50%, Wind:  0 mph",
                    "venue": {
                        "id": "4de1fcde-7c65-4145-a874-3753bdd584b7",
                        "name": "Los Angeles Memorial Coliseum",
                        "city": "Los Angeles",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90037",
                        "address": "3911 South Figueroa Street",
                        "capacity": 93607,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 5
                    },
                    "away": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 10,
                        "away_points": 16
                    }
                },
                {
                    "id": "b46357d0-a317-419f-ae45-db44cb3abdc9",
                    "status": "closed",
                    "reference": "57309",
                    "number": 76,
                    "scheduled": "2017-10-08T20:25:00+00:00",
                    "attendance": 93329,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "1e84213a-ff1f-4c9d-a003-8ee782b25a40",
                        "name": "AT&T Stadium",
                        "city": "Arlington",
                        "state": "TX",
                        "country": "USA",
                        "zip": "76011",
                        "address": "One Legends Way",
                        "capacity": 80000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 5
                    },
                    "away": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 31,
                        "away_points": 35
                    }
                },
                {
                    "id": "bb2d9b2e-c319-4571-9b9f-3e7832e30ce2",
                    "status": "closed",
                    "reference": "57305",
                    "number": 72,
                    "scheduled": "2017-10-08T17:00:00+00:00",
                    "attendance": 69596,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Showers Temp: 77 F, Humidity: 91%, Wind: SSW 10 mph",
                    "venue": {
                        "id": "4fa8c29c-6626-464c-8540-314ed7535e1b",
                        "name": "Lincoln Financial Field",
                        "city": "Philadelphia",
                        "state": "PA",
                        "country": "USA",
                        "zip": "19148",
                        "address": "1020 Pattison Avenue",
                        "capacity": 68532,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 5
                    },
                    "away": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 34,
                        "away_points": 7
                    }
                },
                {
                    "id": "cd3bad2b-80ab-4f92-9f7a-01f684b10b65",
                    "status": "closed",
                    "reference": "57311",
                    "number": 78,
                    "scheduled": "2017-10-10T00:30:00+00:00",
                    "attendance": 61834,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Partly cloudy Temp: 70 F, Humidity: 69%, Wind: NE 10 mph",
                    "venue": {
                        "id": "d7866605-5ac6-4b3a-90e8-760cc5a26b75",
                        "name": "Soldier Field",
                        "city": "Chicago",
                        "state": "IL",
                        "country": "USA",
                        "zip": "60605",
                        "address": "1410 South Museum Campus Drive",
                        "capacity": 61500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 5
                    },
                    "away": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 20
                    }
                },
                {
                    "id": "d26b39df-07a5-481f-a50c-0a23cacdf834",
                    "status": "closed",
                    "reference": "57310",
                    "number": 77,
                    "scheduled": "2017-10-09T00:30:00+00:00",
                    "attendance": 71835,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 89 F, Humidity: 59%, Wind: NW 9 mph",
                    "venue": {
                        "id": "6a72e5ca-33d0-40af-8e6b-b32a4d3d9346",
                        "name": "NRG Stadium",
                        "city": "Houston",
                        "state": "TX",
                        "country": "USA",
                        "zip": "77054",
                        "address": "One Reliant Park",
                        "capacity": 71054,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 5
                    },
                    "away": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 34,
                        "away_points": 42
                    }
                },
                {
                    "id": "e79b7be1-ed0f-4c57-96c0-39d47eee5bc1",
                    "status": "closed",
                    "reference": "57300",
                    "number": 67,
                    "scheduled": "2017-10-08T17:00:00+00:00",
                    "attendance": 62032,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 67 F, Humidity: 71%, Wind: West 8 mph",
                    "venue": {
                        "id": "90c38d91-3774-4f5d-82ca-1c806828219f",
                        "name": "FirstEnergy Stadium",
                        "city": "Cleveland",
                        "state": "OH",
                        "country": "USA",
                        "zip": "44114",
                        "address": "100 Alfred Lerner Way",
                        "capacity": 71516,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 5
                    },
                    "away": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 14,
                        "away_points": 17
                    }
                },
                {
                    "id": "e9280257-7fdc-4392-8c56-8e0b068e19f4",
                    "status": "closed",
                    "reference": "57301",
                    "number": 68,
                    "scheduled": "2017-10-08T17:00:00+00:00",
                    "attendance": 64288,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Controlled Climate Temp: 68 F, Humidity: 70%, Wind:   mph",
                    "venue": {
                        "id": "6e3bcf22-277d-4c06-b019-62aded51654f",
                        "name": "Ford Field",
                        "city": "Detroit",
                        "state": "MI",
                        "country": "USA",
                        "zip": "48226",
                        "address": "2000 Brush Street",
                        "capacity": 65000,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 5
                    },
                    "away": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 27
                    }
                }
            ]
        },
        {
            "id": "204e29fb-e2c2-492c-9de0-4e6920a4762b",
            "sequence": 6,
            "title": "6",
            "games": [
                {
                    "id": "18542c57-bf28-49e0-8f95-c61afd7f4743",
                    "status": "closed",
                    "reference": "57320",
                    "number": 87,
                    "scheduled": "2017-10-15T20:05:00+00:00",
                    "attendance": 63999,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "N/A Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "f8e4d2ab-53f9-4e9f-8c4a-9bc278094a84",
                        "name": "University of Phoenix Stadium",
                        "city": "Glendale",
                        "state": "AZ",
                        "country": "USA",
                        "zip": "85305",
                        "address": "One Cardinals Drive",
                        "capacity": 63400,
                        "surface": "turf",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 6
                    },
                    "away": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 38,
                        "away_points": 33
                    }
                },
                {
                    "id": "398e5bc7-6272-476c-a732-4b45de14cfea",
                    "status": "closed",
                    "reference": "57319",
                    "number": 86,
                    "scheduled": "2017-10-15T17:00:00+00:00",
                    "attendance": 75568,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 68 F, Humidity: 85%, Wind: South 4 mph",
                    "venue": {
                        "id": "7c11bb2d-4a53-4842-b842-0f1c63ed78e9",
                        "name": "FedExField",
                        "city": "Landover",
                        "state": "MD",
                        "country": "USA",
                        "zip": "20785",
                        "address": "1600 FedEx Way",
                        "capacity": 83000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 5
                    },
                    "away": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 26,
                        "away_points": 24
                    }
                },
                {
                    "id": "40ec5836-041c-4870-84c7-cc60e769effc",
                    "status": "closed",
                    "reference": "57321",
                    "number": 88,
                    "scheduled": "2017-10-15T20:05:00+00:00",
                    "attendance": 56232,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "30% Chance of Rain Temp: 88 F, Humidity: 68%, Wind: NE 10 mph",
                    "venue": {
                        "id": "4c5c036d-dd3d-4183-b595-71a43a97560f",
                        "name": "EverBank Field",
                        "city": "Jacksonville",
                        "state": "FL",
                        "country": "USA",
                        "zip": "32202",
                        "address": "One EverBank Field Drive",
                        "capacity": 67246,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 6
                    },
                    "away": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 27
                    }
                },
                {
                    "id": "41690f15-a362-46da-bbd1-b08a1916fce9",
                    "status": "closed",
                    "reference": "57314",
                    "number": 81,
                    "scheduled": "2017-10-15T17:00:00+00:00",
                    "attendance": 70616,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Partly Sunny Temp: 69 F, Humidity: 87%, Wind: s 7 mph",
                    "venue": {
                        "id": "d54faae0-0314-484f-8604-9d8dd08e1149",
                        "name": "M&T Bank Stadium",
                        "city": "Baltimore",
                        "state": "MD",
                        "country": "USA",
                        "zip": "21230",
                        "address": "1101 Russell Street",
                        "capacity": 71008,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 6
                    },
                    "away": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 27
                    }
                },
                {
                    "id": "4716bc49-8c79-4045-8c17-976f196258b0",
                    "status": "closed",
                    "reference": "57324",
                    "number": 91,
                    "scheduled": "2017-10-16T00:30:00+00:00",
                    "attendance": 76721,
                    "utc_offset": -7,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 61 F, Humidity: 17%, Wind: NorthEast 4 mph",
                    "venue": {
                        "id": "6589e61d-ef1e-4e30-91b5-9acd2072b8a0",
                        "name": "Sports Authority Field at Mile High",
                        "city": "Denver",
                        "state": "CO",
                        "country": "USA",
                        "zip": "80204",
                        "address": "1701 Mile High Stadium Circle",
                        "capacity": 76125,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 5
                    },
                    "away": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 10,
                        "away_points": 23
                    }
                },
                {
                    "id": "57316513-09bf-445b-a431-b144da8e0069",
                    "status": "closed",
                    "reference": "57315",
                    "number": 82,
                    "scheduled": "2017-10-15T17:00:00+00:00",
                    "attendance": 71815,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 85 F, Humidity: 62%, Wind: N 7 mph",
                    "venue": {
                        "id": "6a72e5ca-33d0-40af-8e6b-b32a4d3d9346",
                        "name": "NRG Stadium",
                        "city": "Houston",
                        "state": "TX",
                        "country": "USA",
                        "zip": "77054",
                        "address": "One Reliant Park",
                        "capacity": 71054,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 6
                    },
                    "away": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 33,
                        "away_points": 17
                    }
                },
                {
                    "id": "59feb1ea-6da9-4713-b851-1143a1bfa076",
                    "status": "closed",
                    "reference": "57317",
                    "number": 84,
                    "scheduled": "2017-10-15T17:00:00+00:00",
                    "attendance": 73117,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 84 F, Humidity: 80%, Wind: SE 2 mph",
                    "venue": {
                        "id": "3c85d89a-ec66-4983-acd5-1381d6c8673a",
                        "name": "Mercedes-Benz Superdome",
                        "city": "New Orleans",
                        "state": "LA",
                        "country": "USA",
                        "zip": "70112",
                        "address": "1500 Sugar Bowl Drive",
                        "capacity": 76468,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 5
                    },
                    "away": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 52,
                        "away_points": 38
                    }
                },
                {
                    "id": "5ce3f11c-467b-4d89-a930-6273f07a0449",
                    "status": "closed",
                    "reference": "57316",
                    "number": 83,
                    "scheduled": "2017-10-15T17:00:00+00:00",
                    "attendance": 66848,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Indoor Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "f5ff00d4-1ed8-4918-bf73-13d66d510f98",
                        "name": "U.S. Bank Stadium",
                        "city": "Minneapolis",
                        "state": "MN",
                        "country": "USA",
                        "zip": "55415",
                        "address": "900 S 5th St",
                        "capacity": 66200,
                        "surface": "turf",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 6
                    },
                    "away": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 23,
                        "away_points": 10
                    }
                },
                {
                    "id": "931a499a-9700-461a-a35c-45e1f58502ce",
                    "status": "closed",
                    "reference": "57325",
                    "number": 92,
                    "scheduled": "2017-10-17T00:30:00+00:00",
                    "attendance": 63888,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 57 F, Humidity: 55%, Wind: NE 7 mph",
                    "venue": {
                        "id": "5295c1b7-c85c-49cb-9569-1707c65324e5",
                        "name": "Nissan Stadium",
                        "city": "Nashville",
                        "state": "TN",
                        "country": "USA",
                        "zip": "37213",
                        "address": "One Titans Way",
                        "capacity": 69143,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 6
                    },
                    "away": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 36,
                        "away_points": 22
                    }
                },
                {
                    "id": "b9f5901b-320a-4f1c-a7fa-52dd2bf6b1a0",
                    "status": "closed",
                    "reference": "57322",
                    "number": 89,
                    "scheduled": "2017-10-15T20:25:00+00:00",
                    "attendance": 76994,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 60 F, Humidity: 56%, Wind: Northwest 14 mph",
                    "venue": {
                        "id": "2ec4c411-dac2-403d-b091-6b6aa4a0a914",
                        "name": "Arrowhead Stadium",
                        "city": "Kansas City",
                        "state": "MO",
                        "country": "USA",
                        "zip": "64129",
                        "address": "One Arrowhead Drive",
                        "capacity": 79451,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 6
                    },
                    "away": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 13,
                        "away_points": 19
                    }
                },
                {
                    "id": "e56bb731-2c99-46b2-8f34-6d692c342258",
                    "status": "closed",
                    "reference": "57312",
                    "number": 79,
                    "scheduled": "2017-10-13T00:25:00+00:00",
                    "attendance": 74373,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 72 F, Humidity: 88%, Wind: ENE 6 mph",
                    "venue": {
                        "id": "39be9ed4-3292-49ac-8699-a381de3a4969",
                        "name": "Bank of America Stadium",
                        "city": "Charlotte",
                        "state": "NC",
                        "country": "USA",
                        "zip": "28202",
                        "address": "800 South Mint Street",
                        "capacity": 73778,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 6
                    },
                    "away": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 23,
                        "away_points": 28
                    }
                },
                {
                    "id": "ecfc4f4a-5cfc-4ed9-8b28-7d89d06a2bfd",
                    "status": "closed",
                    "reference": "57313",
                    "number": 80,
                    "scheduled": "2017-10-15T17:00:00+00:00",
                    "attendance": 70593,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Indoors Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "1f2d393b-2291-4835-a412-0714966ea245",
                        "name": "Mercedes-Benz Stadium",
                        "city": "Atlanta",
                        "state": "GA",
                        "country": "USA",
                        "zip": "30313",
                        "address": "441 Martin Luther King Jr Dr NW",
                        "capacity": 71000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 5
                    },
                    "away": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 5
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 20
                    }
                },
                {
                    "id": "f29dddd1-cfe4-4c4a-a465-6c986d1078c5",
                    "status": "closed",
                    "reference": "57323",
                    "number": 90,
                    "scheduled": "2017-10-15T20:25:00+00:00",
                    "attendance": 54685,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 76 F, Humidity: 25%, Wind: NNE 9 mph",
                    "venue": {
                        "id": "ba1ad00f-9130-462c-93f9-5612a0015117",
                        "name": "Oakland Alameda Coliseum",
                        "city": "Oakland",
                        "state": "CA",
                        "country": "USA",
                        "zip": "94621",
                        "address": "7000 Coliseum Way",
                        "capacity": 53200,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 6
                    },
                    "away": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 16,
                        "away_points": 17
                    }
                },
                {
                    "id": "fc7f26af-d2de-4074-a59a-684c249748a5",
                    "status": "closed",
                    "reference": "57318",
                    "number": 85,
                    "scheduled": "2017-10-15T17:00:00+00:00",
                    "attendance": 77562,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 73 F, Humidity: 70%, Wind: WNW 13 mph",
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 6
                    },
                    "away": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 24
                    }
                }
            ]
        },
        {
            "id": "1ae39735-f804-4114-a8da-7e81eb6bbab6",
            "sequence": 7,
            "title": "7",
            "games": [
                {
                    "id": "0097e36d-885c-4eeb-b56b-e2a94e143bc6",
                    "status": "closed",
                    "reference": "57339",
                    "number": 106,
                    "scheduled": "2017-10-23T00:30:00+00:00",
                    "attendance": 65878,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy, fog started developing in 2nd quarter Temp: 56 F, Humidity: 88%, Wind: NE 2 mph",
                    "venue": {
                        "id": "e43310b1-cb82-4df9-8be5-e9b39637031b",
                        "name": "Gillette Stadium",
                        "city": "Foxborough",
                        "state": "MA",
                        "country": "USA",
                        "zip": "02035",
                        "address": "One Patriot Place",
                        "capacity": 68756,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 7
                    },
                    "away": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 23,
                        "away_points": 7
                    }
                },
                {
                    "id": "030e3636-3d52-4a30-9249-544957c4ae8d",
                    "status": "closed",
                    "reference": "57335",
                    "number": 102,
                    "scheduled": "2017-10-22T20:25:00+00:00",
                    "attendance": 65363,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 79 F, Humidity: 37%, Wind: From S 6 mph",
                    "venue": {
                        "id": "7349a2e6-0ac9-410b-8bd2-ca58c9f7aa34",
                        "name": "Heinz Field",
                        "city": "Pittsburgh",
                        "state": "PA",
                        "country": "USA",
                        "zip": "15212",
                        "address": "100 Art Rooney Avenue",
                        "capacity": 65050,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 7
                    },
                    "away": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 29,
                        "away_points": 14
                    }
                },
                {
                    "id": "09d5974b-efd4-4dd7-a261-27bc960319aa",
                    "status": "closed",
                    "reference": "57334",
                    "number": 101,
                    "scheduled": "2017-10-22T17:00:00+00:00",
                    "attendance": 66751,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Indoor Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "f5ff00d4-1ed8-4918-bf73-13d66d510f98",
                        "name": "U.S. Bank Stadium",
                        "city": "Minneapolis",
                        "state": "MN",
                        "country": "USA",
                        "zip": "55415",
                        "address": "900 S 5th St",
                        "capacity": 66200,
                        "surface": "turf",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 7
                    },
                    "away": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 7
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 16
                    }
                },
                {
                    "id": "0e456976-da54-46c2-af41-e6c8095c70da",
                    "status": "closed",
                    "reference": "57327",
                    "number": 94,
                    "scheduled": "2017-10-22T17:00:00+00:00",
                    "attendance": 68561,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 74 F, Humidity: 45%, Wind: s 9 mph",
                    "venue": {
                        "id": "e9e0828e-37fc-4238-a317-49037577dd55",
                        "name": "New Era Field",
                        "city": "Orchard Park",
                        "state": "NY",
                        "country": "USA",
                        "zip": "14127",
                        "address": "One Bills Drive",
                        "capacity": 73079,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 6
                    },
                    "away": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 30,
                        "away_points": 27
                    }
                },
                {
                    "id": "34b74d5a-6155-4568-94f1-438f08017f38",
                    "status": "closed",
                    "reference": "57331",
                    "number": 98,
                    "scheduled": "2017-10-22T17:00:00+00:00",
                    "attendance": 63104,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 72 F, Humidity: 59%, Wind: S 17 mph",
                    "venue": {
                        "id": "6ed18563-53e0-46c2-a91d-12d73a16456d",
                        "name": "Lucas Oil Stadium",
                        "city": "Indianapolis",
                        "state": "IN",
                        "country": "USA",
                        "zip": "46225",
                        "address": "500 South Capitol Avenue",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 7
                    },
                    "away": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 7
                    },
                    "scoring": {
                        "home_points": 0,
                        "away_points": 27
                    }
                },
                {
                    "id": "4632b00b-1043-4c2c-9725-ffa0a32f88e5",
                    "status": "closed",
                    "reference": "57333",
                    "number": 100,
                    "scheduled": "2017-10-22T17:00:00+00:00",
                    "attendance": 65025,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 86 F, Humidity: 67%, Wind: ESE 18 mph",
                    "venue": {
                        "id": "50a5c833-1570-4c38-abc7-7914cf87dbde",
                        "name": "Hard Rock Stadium",
                        "city": "Miami Gardens",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33056",
                        "address": "2269 Northwest 199th Street",
                        "capacity": 76100,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 6
                    },
                    "away": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 7
                    },
                    "scoring": {
                        "home_points": 31,
                        "away_points": 28
                    }
                },
                {
                    "id": "48b63029-48ee-4a98-8c20-9560f93dce71",
                    "status": "closed",
                    "reference": "57340",
                    "number": 107,
                    "scheduled": "2017-10-24T00:30:00+00:00",
                    "attendance": 69596,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 70 F, Humidity: 78%, Wind: SSE 9 mph",
                    "venue": {
                        "id": "4fa8c29c-6626-464c-8540-314ed7535e1b",
                        "name": "Lincoln Financial Field",
                        "city": "Philadelphia",
                        "state": "PA",
                        "country": "USA",
                        "zip": "19148",
                        "address": "1020 Pattison Avenue",
                        "capacity": 68532,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 7
                    },
                    "away": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 34,
                        "away_points": 24
                    }
                },
                {
                    "id": "7abb277f-e7e3-4e3d-ad1d-c96c75af42ef",
                    "status": "closed",
                    "reference": "57336",
                    "number": 103,
                    "scheduled": "2017-10-22T20:05:00+00:00",
                    "attendance": 70133,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 68 F, Humidity: 60%, Wind: NW 10 mph",
                    "venue": {
                        "id": "24c71dbf-6469-46f1-8165-22005c295c8f",
                        "name": "Levi's Stadium",
                        "city": "Santa Clara",
                        "state": "CA",
                        "country": "USA",
                        "zip": "95054",
                        "address": "4900 Marie P DeBartolo Way",
                        "capacity": 68500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 7
                    },
                    "away": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 10,
                        "away_points": 40
                    }
                },
                {
                    "id": "81258727-7b71-4bea-96e0-eb5b82fdebf9",
                    "status": "closed",
                    "reference": "57332",
                    "number": 99,
                    "scheduled": "2017-10-22T17:00:00+00:00",
                    "attendance": 73736,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": "Rain Temp: 54 F, Humidity: 72%, Wind: W 10 mph",
                    "venue": {
                        "id": "0104a0ec-fdd7-48a8-932c-4d08344f2bf3",
                        "name": "Twickenham Stadium",
                        "city": "London",
                        "state": null,
                        "country": "UK",
                        "zip": null,
                        "address": "Twickenham Stadium",
                        "capacity": 82000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 7
                    },
                    "away": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 7
                    },
                    "scoring": {
                        "home_points": 33,
                        "away_points": 0
                    }
                },
                {
                    "id": "8b5c735e-1644-47df-af49-31de98170a27",
                    "status": "closed",
                    "reference": "57330",
                    "number": 97,
                    "scheduled": "2017-10-22T17:00:00+00:00",
                    "attendance": 78380,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Rain Temp: 52 F, Humidity: 100%, Wind: W 10 mph",
                    "venue": {
                        "id": "5a60dd3a-302c-41c6-ab0f-dd335c1103c2",
                        "name": "Lambeau Field",
                        "city": "Green Bay",
                        "state": "WI",
                        "country": "USA",
                        "zip": "54304",
                        "address": "1265 Lombardi Avenue",
                        "capacity": 80750,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 7
                    },
                    "away": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 26
                    }
                },
                {
                    "id": "99939f5a-2af2-4029-a37b-748f52d2ed2a",
                    "status": "closed",
                    "reference": "57338",
                    "number": 105,
                    "scheduled": "2017-10-22T20:25:00+00:00",
                    "attendance": 78527,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 75 F, Humidity: 29%, Wind: SSE 7 mph",
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 7
                    },
                    "away": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 7,
                        "away_points": 24
                    }
                },
                {
                    "id": "c1af6810-ec97-43ca-bbb4-88e81323b866",
                    "status": "closed",
                    "reference": "57326",
                    "number": 93,
                    "scheduled": "2017-10-20T00:25:00+00:00",
                    "attendance": 55090,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 62 F, Humidity: 78%, Wind: SW 1 mph",
                    "venue": {
                        "id": "ba1ad00f-9130-462c-93f9-5612a0015117",
                        "name": "Oakland Alameda Coliseum",
                        "city": "Oakland",
                        "state": "CA",
                        "country": "USA",
                        "zip": "94621",
                        "address": "7000 Coliseum Way",
                        "capacity": 53200,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 7
                    },
                    "away": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 7
                    },
                    "scoring": {
                        "home_points": 31,
                        "away_points": 30
                    }
                },
                {
                    "id": "eb40394d-1681-4a46-9370-d0d14aa64747",
                    "status": "closed",
                    "reference": "57337",
                    "number": 104,
                    "scheduled": "2017-10-22T20:25:00+00:00",
                    "attendance": 25388,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 90 F, Humidity: 15%, Wind: W 4 mph",
                    "venue": {
                        "id": "feebab00-4a3e-4123-8d7c-ed0dbdd96540",
                        "name": "StubHub Center",
                        "city": "Carson",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90746",
                        "address": "18400 Avalon Boulevard",
                        "capacity": 27000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 7
                    },
                    "away": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 6
                    },
                    "scoring": {
                        "home_points": 21,
                        "away_points": 0
                    }
                },
                {
                    "id": "f5007f76-0385-4886-9510-c70205c651e3",
                    "status": "closed",
                    "reference": "57328",
                    "number": 95,
                    "scheduled": "2017-10-22T17:00:00+00:00",
                    "attendance": 61256,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy with periods of rain, thunder possible. Winds shifting to WNW, 10-20 mph. Temp: 70 F, Humidity: 71%, Wind: S 15 mph",
                    "venue": {
                        "id": "d7866605-5ac6-4b3a-90e8-760cc5a26b75",
                        "name": "Soldier Field",
                        "city": "Chicago",
                        "state": "IL",
                        "country": "USA",
                        "zip": "60605",
                        "address": "1410 South Museum Campus Drive",
                        "capacity": 61500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 7
                    },
                    "away": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 7
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 3
                    }
                },
                {
                    "id": "f5158111-e00e-4045-882d-fc7dae703656",
                    "status": "closed",
                    "reference": "57329",
                    "number": 96,
                    "scheduled": "2017-10-22T17:00:00+00:00",
                    "attendance": 59061,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 74 F, Humidity: 56%, Wind: South 12 mph",
                    "venue": {
                        "id": "90c38d91-3774-4f5d-82ca-1c806828219f",
                        "name": "FirstEnergy Stadium",
                        "city": "Cleveland",
                        "state": "OH",
                        "country": "USA",
                        "zip": "44114",
                        "address": "100 Alfred Lerner Way",
                        "capacity": 71516,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 7
                    },
                    "away": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 7
                    },
                    "scoring": {
                        "home_points": 9,
                        "away_points": 12
                    }
                }
            ]
        },
        {
            "id": "4e6726b0-464c-4621-a626-2f6748cad2f9",
            "sequence": 8,
            "title": "8",
            "games": [
                {
                    "id": "03d0c59c-ed0f-439b-aa73-b46ac4a27cfa",
                    "status": "closed",
                    "reference": "57347",
                    "number": 114,
                    "scheduled": "2017-10-29T17:00:00+00:00",
                    "attendance": 77562,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Rain Temp: 63 F, Humidity: 93%, Wind: East 5 mph",
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 8
                    },
                    "away": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 7
                    },
                    "scoring": {
                        "home_points": 20,
                        "away_points": 25
                    }
                },
                {
                    "id": "04b4efea-c7d7-4557-a898-77d7f83261fb",
                    "status": "closed",
                    "reference": "57341",
                    "number": 108,
                    "scheduled": "2017-10-27T00:25:00+00:00",
                    "attendance": 70408,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 56 F, Humidity: 49%, Wind: West 9 mph",
                    "venue": {
                        "id": "d54faae0-0314-484f-8604-9d8dd08e1149",
                        "name": "M&T Bank Stadium",
                        "city": "Baltimore",
                        "state": "MD",
                        "country": "USA",
                        "zip": "21230",
                        "address": "1101 Russell Street",
                        "capacity": 71008,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 8
                    },
                    "away": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 7
                    },
                    "scoring": {
                        "home_points": 40,
                        "away_points": 0
                    }
                },
                {
                    "id": "17889026-92ce-4c44-bc8f-38dfa705f48c",
                    "status": "closed",
                    "reference": "57350",
                    "number": 117,
                    "scheduled": "2017-10-29T20:05:00+00:00",
                    "attendance": 69025,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 50 F, Humidity: 90%, Wind: ENE 2 mph",
                    "venue": {
                        "id": "c6b9e5df-c9e4-434c-b3e6-83928f11cbda",
                        "name": "CenturyLink Field",
                        "city": "Seattle",
                        "state": "WA",
                        "country": "USA",
                        "zip": "98134",
                        "address": "800 Occidental Avenue South",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 7
                    },
                    "away": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 7
                    },
                    "scoring": {
                        "home_points": 41,
                        "away_points": 38
                    }
                },
                {
                    "id": "17a97700-f582-4169-bf96-139b6153ac01",
                    "status": "closed",
                    "reference": "57344",
                    "number": 111,
                    "scheduled": "2017-10-29T17:00:00+00:00",
                    "attendance": 57901,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 39 F, Humidity: 68%, Wind: NNW 9 mph",
                    "venue": {
                        "id": "b87a1595-d3c8-48ea-8a53-0aab6378a64a",
                        "name": "Paul Brown Stadium",
                        "city": "Cincinnati",
                        "state": "OH",
                        "country": "USA",
                        "zip": "45202",
                        "address": "One Paul Brown Stadium",
                        "capacity": 65535,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 7
                    },
                    "away": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 23
                    }
                },
                {
                    "id": "1d160a46-0a76-44cd-8055-1457119a788f",
                    "status": "closed",
                    "reference": "57349",
                    "number": 116,
                    "scheduled": "2017-10-29T17:00:00+00:00",
                    "attendance": 58545,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 63 F, Humidity: 63%, Wind: NNW 13 mph",
                    "venue": {
                        "id": "6fccc39c-80bc-4c81-83d9-2d5a848c8c09",
                        "name": "Raymond James Stadium",
                        "city": "Tampa",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33607",
                        "address": "4201 North Dale Mabry Highway",
                        "capacity": 65890,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 7
                    },
                    "away": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 3,
                        "away_points": 17
                    }
                },
                {
                    "id": "50f99fa2-8f6f-4442-b11c-3a1923a68b32",
                    "status": "closed",
                    "reference": "57343",
                    "number": 110,
                    "scheduled": "2017-10-29T17:00:00+00:00",
                    "attendance": 69599,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Rain Temp: 42 F, Humidity: 94%, Wind: NE 7 mph",
                    "venue": {
                        "id": "e9e0828e-37fc-4238-a317-49037577dd55",
                        "name": "New Era Field",
                        "city": "Orchard Park",
                        "state": "NY",
                        "country": "USA",
                        "zip": "14127",
                        "address": "One Bills Drive",
                        "capacity": 73079,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 7
                    },
                    "away": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 34,
                        "away_points": 14
                    }
                },
                {
                    "id": "63db0f3e-f1de-467f-b958-9d4ad1d5625b",
                    "status": "closed",
                    "reference": "57345",
                    "number": 112,
                    "scheduled": "2017-10-29T17:00:00+00:00",
                    "attendance": 65878,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Rain Temp: 62 F, Humidity: 84%, Wind: SE 8 mph",
                    "venue": {
                        "id": "e43310b1-cb82-4df9-8be5-e9b39637031b",
                        "name": "Gillette Stadium",
                        "city": "Foxborough",
                        "state": "MA",
                        "country": "USA",
                        "zip": "02035",
                        "address": "One Patriot Place",
                        "capacity": 68756,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 8
                    },
                    "away": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 21,
                        "away_points": 13
                    }
                },
                {
                    "id": "6838f8f8-4315-4f63-91f5-01df73cac298",
                    "status": "closed",
                    "reference": "57351",
                    "number": 118,
                    "scheduled": "2017-10-29T20:25:00+00:00",
                    "attendance": 78428,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Rain Temp: 60 F, Humidity: 93%, Wind: ESE 4 mph",
                    "venue": {
                        "id": "7c11bb2d-4a53-4842-b842-0f1c63ed78e9",
                        "name": "FedExField",
                        "city": "Landover",
                        "state": "MD",
                        "country": "USA",
                        "zip": "20785",
                        "address": "1600 FedEx Way",
                        "capacity": 83000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 7
                    },
                    "away": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 7
                    },
                    "scoring": {
                        "home_points": 19,
                        "away_points": 33
                    }
                },
                {
                    "id": "87a16aa0-70c3-4f2c-b065-f4e3685219ca",
                    "status": "closed",
                    "reference": "57346",
                    "number": 113,
                    "scheduled": "2017-10-29T17:00:00+00:00",
                    "attendance": 73192,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 54 F, Humidity: 47%, Wind: N 4 mph",
                    "venue": {
                        "id": "3c85d89a-ec66-4983-acd5-1381d6c8673a",
                        "name": "Mercedes-Benz Superdome",
                        "city": "New Orleans",
                        "state": "LA",
                        "country": "USA",
                        "zip": "70112",
                        "address": "1500 Sugar Bowl Drive",
                        "capacity": 76468,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 7
                    },
                    "away": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 20,
                        "away_points": 12
                    }
                },
                {
                    "id": "9d1d54cb-c3c7-4c18-aff4-859df5119aa5",
                    "status": "closed",
                    "reference": "57352",
                    "number": 119,
                    "scheduled": "2017-10-30T00:30:00+00:00",
                    "attendance": 64983,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Controlled Climate Temp: 68 F, Humidity: 70%, Wind:   mph",
                    "venue": {
                        "id": "6e3bcf22-277d-4c06-b019-62aded51654f",
                        "name": "Ford Field",
                        "city": "Detroit",
                        "state": "MI",
                        "country": "USA",
                        "zip": "48226",
                        "address": "2000 Brush Street",
                        "capacity": 65000,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 7
                    },
                    "away": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 15,
                        "away_points": 20
                    }
                },
                {
                    "id": "c91ae988-9c7d-4a74-a422-cd2e3ebaeebe",
                    "status": "closed",
                    "reference": "57353",
                    "number": 120,
                    "scheduled": "2017-10-31T00:30:00+00:00",
                    "attendance": 76573,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 44 F, Humidity: 41%, Wind: Northwest 12 mph",
                    "venue": {
                        "id": "2ec4c411-dac2-403d-b091-6b6aa4a0a914",
                        "name": "Arrowhead Stadium",
                        "city": "Kansas City",
                        "state": "MO",
                        "country": "USA",
                        "zip": "64129",
                        "address": "One Arrowhead Drive",
                        "capacity": 79451,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 8
                    },
                    "away": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 7
                    },
                    "scoring": {
                        "home_points": 29,
                        "away_points": 19
                    }
                },
                {
                    "id": "c94bf0b2-5e41-4a4d-b6b7-004c8fa17a9e",
                    "status": "closed",
                    "reference": "57348",
                    "number": 115,
                    "scheduled": "2017-10-29T17:00:00+00:00",
                    "attendance": 69596,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Rain Temp: 67 F, Humidity: 91%, Wind: SE 12 mph",
                    "venue": {
                        "id": "4fa8c29c-6626-464c-8540-314ed7535e1b",
                        "name": "Lincoln Financial Field",
                        "city": "Philadelphia",
                        "state": "PA",
                        "country": "USA",
                        "zip": "19148",
                        "address": "1020 Pattison Avenue",
                        "capacity": 68532,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 8
                    },
                    "away": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 33,
                        "away_points": 10
                    }
                },
                {
                    "id": "d0733ea4-4313-479f-b053-2c92c62787ec",
                    "status": "closed",
                    "reference": "57342",
                    "number": 109,
                    "scheduled": "2017-10-29T13:30:00+00:00",
                    "attendance": 74237,
                    "utc_offset": -1,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 56 F, Humidity: 56%, Wind: N 11 mph",
                    "venue": {
                        "id": "0104a0ec-fdd7-48a8-932c-4d08344f2bf3",
                        "name": "Twickenham Stadium",
                        "city": "London",
                        "state": null,
                        "country": "UK",
                        "zip": null,
                        "address": "Twickenham Stadium",
                        "capacity": 82000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 8
                    },
                    "away": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 16,
                        "away_points": 33
                    }
                }
            ]
        },
        {
            "id": "905a202e-7f54-4403-8a91-4259d04e7b0d",
            "sequence": 9,
            "title": "9",
            "games": [
                {
                    "id": "06aaea3e-71e9-40ec-bb05-779adc2bb09a",
                    "status": "closed",
                    "reference": "57364",
                    "number": 131,
                    "scheduled": "2017-11-05T21:25:00+00:00",
                    "attendance": 93273,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "1e84213a-ff1f-4c9d-a003-8ee782b25a40",
                        "name": "AT&T Stadium",
                        "city": "Arlington",
                        "state": "TX",
                        "country": "USA",
                        "zip": "76011",
                        "address": "One Legends Way",
                        "capacity": 80000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 8
                    },
                    "away": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 28,
                        "away_points": 17
                    }
                },
                {
                    "id": "19cb6a42-ed78-4324-9728-6f2bcfb6dd3c",
                    "status": "closed",
                    "reference": "57366",
                    "number": 133,
                    "scheduled": "2017-11-07T01:30:00+00:00",
                    "attendance": 77575,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 35 F, Humidity: 58%, Wind: SW 3 mph",
                    "venue": {
                        "id": "5a60dd3a-302c-41c6-ab0f-dd335c1103c2",
                        "name": "Lambeau Field",
                        "city": "Green Bay",
                        "state": "WI",
                        "country": "USA",
                        "zip": "54304",
                        "address": "1265 Lombardi Avenue",
                        "capacity": 80750,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 8
                    },
                    "away": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 30
                    }
                },
                {
                    "id": "1dc7e7a3-61c7-48f4-8732-390f22fabd5b",
                    "status": "closed",
                    "reference": "57357",
                    "number": 124,
                    "scheduled": "2017-11-05T18:00:00+00:00",
                    "attendance": 60720,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Temp: 78 F, Humidity: 74%, Wind: NNE 12 mph",
                    "venue": {
                        "id": "4c5c036d-dd3d-4183-b595-71a43a97560f",
                        "name": "EverBank Field",
                        "city": "Jacksonville",
                        "state": "FL",
                        "country": "USA",
                        "zip": "32202",
                        "address": "One EverBank Field Drive",
                        "capacity": 67246,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 8
                    },
                    "away": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 23,
                        "away_points": 7
                    }
                },
                {
                    "id": "1f5949cf-c3af-4326-8ac4-d296b76f0c7a",
                    "status": "closed",
                    "reference": "57360",
                    "number": 127,
                    "scheduled": "2017-11-05T18:00:00+00:00",
                    "attendance": 69596,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 61 F, Humidity: 89%, Wind: SE 5 mph",
                    "venue": {
                        "id": "4fa8c29c-6626-464c-8540-314ed7535e1b",
                        "name": "Lincoln Financial Field",
                        "city": "Philadelphia",
                        "state": "PA",
                        "country": "USA",
                        "zip": "19148",
                        "address": "1020 Pattison Avenue",
                        "capacity": 68532,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 9
                    },
                    "away": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 51,
                        "away_points": 23
                    }
                },
                {
                    "id": "4bae180d-e953-4bcf-bf63-4439be674056",
                    "status": "closed",
                    "reference": "57363",
                    "number": 130,
                    "scheduled": "2017-11-05T21:05:00+00:00",
                    "attendance": 68927,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Rain Temp: 36 F, Humidity: 86%, Wind: NNW 11 mph",
                    "venue": {
                        "id": "c6b9e5df-c9e4-434c-b3e6-83928f11cbda",
                        "name": "CenturyLink Field",
                        "city": "Seattle",
                        "state": "WA",
                        "country": "USA",
                        "zip": "98134",
                        "address": "800 Occidental Avenue South",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 8
                    },
                    "away": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 14,
                        "away_points": 17
                    }
                },
                {
                    "id": "7e8c1c62-1557-421b-9c58-230938152aba",
                    "status": "closed",
                    "reference": "57355",
                    "number": 122,
                    "scheduled": "2017-11-05T18:00:00+00:00",
                    "attendance": 74244,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Coudy Temp: 61 F, Humidity: 86%, Wind: ENE 4 mph",
                    "venue": {
                        "id": "39be9ed4-3292-49ac-8699-a381de3a4969",
                        "name": "Bank of America Stadium",
                        "city": "Charlotte",
                        "state": "NC",
                        "country": "USA",
                        "zip": "28202",
                        "address": "800 South Mint Street",
                        "capacity": 73778,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 9
                    },
                    "away": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 20,
                        "away_points": 17
                    }
                },
                {
                    "id": "8f3dbfa2-bc28-4117-bed5-a98a625f0f3a",
                    "status": "closed",
                    "reference": "57361",
                    "number": 128,
                    "scheduled": "2017-11-05T18:00:00+00:00",
                    "attendance": 67322,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 75 F, Humidity: 66%, Wind: WSW 10 mph",
                    "venue": {
                        "id": "5295c1b7-c85c-49cb-9569-1707c65324e5",
                        "name": "Nissan Stadium",
                        "city": "Nashville",
                        "state": "TN",
                        "country": "USA",
                        "zip": "37213",
                        "address": "One Titans Way",
                        "capacity": 69143,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 8
                    },
                    "away": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 23,
                        "away_points": 20
                    }
                },
                {
                    "id": "976175fc-66f9-45f5-b856-fb619c27f123",
                    "status": "closed",
                    "reference": "57356",
                    "number": 123,
                    "scheduled": "2017-11-05T18:00:00+00:00",
                    "attendance": 71709,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Mostly Cloudy Temp: 84 F, Humidity: 62%, Wind: SSW 10 mph",
                    "venue": {
                        "id": "6a72e5ca-33d0-40af-8e6b-b32a4d3d9346",
                        "name": "NRG Stadium",
                        "city": "Houston",
                        "state": "TX",
                        "country": "USA",
                        "zip": "77054",
                        "address": "One Reliant Park",
                        "capacity": 71054,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 8
                    },
                    "away": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 14,
                        "away_points": 20
                    }
                },
                {
                    "id": "9baba7f3-80a9-4470-b773-68d095f3cfea",
                    "status": "closed",
                    "reference": "57359",
                    "number": 126,
                    "scheduled": "2017-11-05T18:00:00+00:00",
                    "attendance": 76877,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 56 F, Humidity: 92%, Wind: ENE 6 mph",
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 8
                    },
                    "away": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 51
                    }
                },
                {
                    "id": "caa3c765-63a0-4aca-983f-5a1be65495ab",
                    "status": "closed",
                    "reference": "57365",
                    "number": 132,
                    "scheduled": "2017-11-06T01:30:00+00:00",
                    "attendance": 65139,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Temp: 77 F, Humidity: 82%, Wind: N 2 mph",
                    "venue": {
                        "id": "50a5c833-1570-4c38-abc7-7914cf87dbde",
                        "name": "Hard Rock Stadium",
                        "city": "Miami Gardens",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33056",
                        "address": "2269 Northwest 199th Street",
                        "capacity": 76100,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 8
                    },
                    "away": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 27
                    }
                },
                {
                    "id": "cfc304ee-484c-4d4d-b0b5-eb1425c35a8c",
                    "status": "closed",
                    "reference": "57358",
                    "number": 125,
                    "scheduled": "2017-11-05T18:00:00+00:00",
                    "attendance": 73121,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 79 F, Humidity: 67%, Wind: SW 1 mph",
                    "venue": {
                        "id": "3c85d89a-ec66-4983-acd5-1381d6c8673a",
                        "name": "Mercedes-Benz Superdome",
                        "city": "New Orleans",
                        "state": "LA",
                        "country": "USA",
                        "zip": "70112",
                        "address": "1500 Sugar Bowl Drive",
                        "capacity": 76468,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 8
                    },
                    "away": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 30,
                        "away_points": 10
                    }
                },
                {
                    "id": "e9440ee2-e0c6-4fda-b0a1-0907b292c15c",
                    "status": "closed",
                    "reference": "57362",
                    "number": 129,
                    "scheduled": "2017-11-05T21:05:00+00:00",
                    "attendance": 70133,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 61 F, Humidity: 48%, Wind: NW 8 mph",
                    "venue": {
                        "id": "24c71dbf-6469-46f1-8165-22005c295c8f",
                        "name": "Levi's Stadium",
                        "city": "Santa Clara",
                        "state": "CA",
                        "country": "USA",
                        "zip": "95054",
                        "address": "4900 Marie P DeBartolo Way",
                        "capacity": 68500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 9
                    },
                    "away": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 10,
                        "away_points": 20
                    }
                },
                {
                    "id": "f911a4c2-dc01-4886-9f74-17fc818dc077",
                    "status": "closed",
                    "reference": "57354",
                    "number": 121,
                    "scheduled": "2017-11-03T00:25:00+00:00",
                    "attendance": 77562,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 68 F, Humidity: 70%, Wind: South 11 mph",
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 9
                    },
                    "away": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 8
                    },
                    "scoring": {
                        "home_points": 34,
                        "away_points": 21
                    }
                }
            ]
        },
        {
            "id": "90a3bc40-07c9-46c7-948a-ac62a4e518ed",
            "sequence": 10,
            "title": "10",
            "games": [
                {
                    "id": "1d86a9d5-f3d8-4c9c-aade-9c95c8095c35",
                    "status": "closed",
                    "reference": "57376",
                    "number": 143,
                    "scheduled": "2017-11-12T21:05:00+00:00",
                    "attendance": 60032,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 68 F, Humidity: 63%, Wind:   mph",
                    "venue": {
                        "id": "4de1fcde-7c65-4145-a874-3753bdd584b7",
                        "name": "Los Angeles Memorial Coliseum",
                        "city": "Los Angeles",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90037",
                        "address": "3911 South Figueroa Street",
                        "capacity": 93607,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 9
                    },
                    "away": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 33,
                        "away_points": 7
                    }
                },
                {
                    "id": "4859113d-15ba-4bd1-9f9e-2b424bdbebe2",
                    "status": "closed",
                    "reference": "57369",
                    "number": 136,
                    "scheduled": "2017-11-12T18:00:00+00:00",
                    "attendance": 61285,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Rain likely, temps in low 40's. Temp: 39 F, Humidity: 89%, Wind: WSW 6 mph",
                    "venue": {
                        "id": "d7866605-5ac6-4b3a-90e8-760cc5a26b75",
                        "name": "Soldier Field",
                        "city": "Chicago",
                        "state": "IL",
                        "country": "USA",
                        "zip": "60605",
                        "address": "1410 South Museum Campus Drive",
                        "capacity": 61500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 9
                    },
                    "away": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 16,
                        "away_points": 23
                    }
                },
                {
                    "id": "4edce567-0fb4-4783-98cf-954bea22550b",
                    "status": "closed",
                    "reference": "57379",
                    "number": 146,
                    "scheduled": "2017-11-13T01:30:00+00:00",
                    "attendance": 76820,
                    "utc_offset": -7,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 48 F, Humidity: 40%, Wind: NorthEast 5 mph",
                    "venue": {
                        "id": "6589e61d-ef1e-4e30-91b5-9acd2072b8a0",
                        "name": "Sports Authority Field at Mile High",
                        "city": "Denver",
                        "state": "CO",
                        "country": "USA",
                        "zip": "80204",
                        "address": "1701 Mile High Stadium Circle",
                        "capacity": 76125,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 9
                    },
                    "away": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 16,
                        "away_points": 41
                    }
                },
                {
                    "id": "5a4895c2-9fe0-4636-b530-66dd41bb0aab",
                    "status": "closed",
                    "reference": "57375",
                    "number": 142,
                    "scheduled": "2017-11-12T18:00:00+00:00",
                    "attendance": 74476,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 44 F, Humidity: 44%, Wind: South 4 mph",
                    "venue": {
                        "id": "7c11bb2d-4a53-4842-b842-0f1c63ed78e9",
                        "name": "FedExField",
                        "city": "Landover",
                        "state": "MD",
                        "country": "USA",
                        "zip": "20785",
                        "address": "1600 FedEx Way",
                        "capacity": 83000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 9
                    },
                    "away": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 30,
                        "away_points": 38
                    }
                },
                {
                    "id": "6800736a-bb27-473d-bc6f-c92d2e4dbd51",
                    "status": "closed",
                    "reference": "57368",
                    "number": 135,
                    "scheduled": "2017-11-12T18:00:00+00:00",
                    "attendance": 67501,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 42 F, Humidity: 50%, Wind: WSW 2 mph",
                    "venue": {
                        "id": "e9e0828e-37fc-4238-a317-49037577dd55",
                        "name": "New Era Field",
                        "city": "Orchard Park",
                        "state": "NY",
                        "country": "USA",
                        "zip": "14127",
                        "address": "One Bills Drive",
                        "capacity": 73079,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 9
                    },
                    "away": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 10,
                        "away_points": 47
                    }
                },
                {
                    "id": "6e700668-9432-45e9-830f-ef2553209b4c",
                    "status": "closed",
                    "reference": "57374",
                    "number": 141,
                    "scheduled": "2017-11-12T18:00:00+00:00",
                    "attendance": 67432,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 53 F, Humidity: 88%, Wind: SSW 4 mph",
                    "venue": {
                        "id": "5295c1b7-c85c-49cb-9569-1707c65324e5",
                        "name": "Nissan Stadium",
                        "city": "Nashville",
                        "state": "TN",
                        "country": "USA",
                        "zip": "37213",
                        "address": "One Titans Way",
                        "capacity": 69143,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 9
                    },
                    "away": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 20
                    }
                },
                {
                    "id": "758834b0-db3d-4d2e-952e-d05defda3719",
                    "status": "closed",
                    "reference": "57371",
                    "number": 138,
                    "scheduled": "2017-11-12T18:00:00+00:00",
                    "attendance": 66146,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 42 F, Humidity: 70%, Wind: NE 5 mph",
                    "venue": {
                        "id": "6ed18563-53e0-46c2-a91d-12d73a16456d",
                        "name": "Lucas Oil Stadium",
                        "city": "Indianapolis",
                        "state": "IN",
                        "country": "USA",
                        "zip": "46225",
                        "address": "500 South Capitol Avenue",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 10
                    },
                    "away": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 20
                    }
                },
                {
                    "id": "8c0bab3a-f9aa-44c9-85bf-218b82f9831f",
                    "status": "closed",
                    "reference": "57367",
                    "number": 134,
                    "scheduled": "2017-11-10T01:25:00+00:00",
                    "attendance": 64639,
                    "utc_offset": -7,
                    "entry_mode": "INGEST",
                    "weather": "Temp: 79 F, Humidity: 32%, Wind:  0 mph",
                    "venue": {
                        "id": "f8e4d2ab-53f9-4e9f-8c4a-9bc278094a84",
                        "name": "University of Phoenix Stadium",
                        "city": "Glendale",
                        "state": "AZ",
                        "country": "USA",
                        "zip": "85305",
                        "address": "One Cardinals Drive",
                        "capacity": 63400,
                        "surface": "turf",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 9
                    },
                    "away": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 16,
                        "away_points": 22
                    }
                },
                {
                    "id": "8e0e6ada-a87e-4b68-add2-7542b5cf753e",
                    "status": "closed",
                    "reference": "57377",
                    "number": 144,
                    "scheduled": "2017-11-12T21:25:00+00:00",
                    "attendance": 73761,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "1f2d393b-2291-4835-a412-0714966ea245",
                        "name": "Mercedes-Benz Stadium",
                        "city": "Atlanta",
                        "state": "GA",
                        "country": "USA",
                        "zip": "30313",
                        "address": "441 Martin Luther King Jr Dr NW",
                        "capacity": 71000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 9
                    },
                    "away": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 27,
                        "away_points": 7
                    }
                },
                {
                    "id": "c346d053-5a22-4579-8a90-6fc054f53e5e",
                    "status": "closed",
                    "reference": "57370",
                    "number": 137,
                    "scheduled": "2017-11-12T18:00:00+00:00",
                    "attendance": 64646,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Controlled Climate Temp: 68 F, Humidity: 70%, Wind:   mph",
                    "venue": {
                        "id": "6e3bcf22-277d-4c06-b019-62aded51654f",
                        "name": "Ford Field",
                        "city": "Detroit",
                        "state": "MI",
                        "country": "USA",
                        "zip": "48226",
                        "address": "2000 Brush Street",
                        "capacity": 65000,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 9
                    },
                    "away": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 38,
                        "away_points": 24
                    }
                },
                {
                    "id": "cba381e8-c408-4d25-8385-268c7c26388a",
                    "status": "closed",
                    "reference": "57380",
                    "number": 147,
                    "scheduled": "2017-11-14T01:30:00+00:00",
                    "attendance": 72790,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 49 F, Humidity: 66%, Wind: NNE 6 mph",
                    "venue": {
                        "id": "39be9ed4-3292-49ac-8699-a381de3a4969",
                        "name": "Bank of America Stadium",
                        "city": "Charlotte",
                        "state": "NC",
                        "country": "USA",
                        "zip": "28202",
                        "address": "800 South Mint Street",
                        "capacity": 73778,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 10
                    },
                    "away": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 45,
                        "away_points": 21
                    }
                },
                {
                    "id": "e1734aea-d562-4da0-a84d-277f6d31cc78",
                    "status": "closed",
                    "reference": "57373",
                    "number": 140,
                    "scheduled": "2017-11-12T18:00:00+00:00",
                    "attendance": 57911,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 79 F, Humidity: 69%, Wind: E 16 mph",
                    "venue": {
                        "id": "6fccc39c-80bc-4c81-83d9-2d5a848c8c09",
                        "name": "Raymond James Stadium",
                        "city": "Tampa",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33607",
                        "address": "4201 North Dale Mabry Highway",
                        "capacity": 65890,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 9
                    },
                    "away": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 15,
                        "away_points": 10
                    }
                },
                {
                    "id": "e648aeed-2463-48d9-87ed-c23a9233ee4b",
                    "status": "closed",
                    "reference": "57372",
                    "number": 139,
                    "scheduled": "2017-11-12T18:00:00+00:00",
                    "attendance": 60835,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Temp: 76 F, Humidity: 75%, Wind: NE 5 mph",
                    "venue": {
                        "id": "4c5c036d-dd3d-4183-b595-71a43a97560f",
                        "name": "EverBank Field",
                        "city": "Jacksonville",
                        "state": "FL",
                        "country": "USA",
                        "zip": "32202",
                        "address": "One EverBank Field Drive",
                        "capacity": 67246,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 9
                    },
                    "away": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 20,
                        "away_points": 17
                    }
                },
                {
                    "id": "ec4b988d-67db-42fa-8605-a43db3f69d49",
                    "status": "closed",
                    "reference": "57378",
                    "number": 145,
                    "scheduled": "2017-11-12T21:25:00+00:00",
                    "attendance": 70133,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 68 F, Humidity: 46%, Wind: West 5 mph",
                    "venue": {
                        "id": "24c71dbf-6469-46f1-8165-22005c295c8f",
                        "name": "Levi's Stadium",
                        "city": "Santa Clara",
                        "state": "CA",
                        "country": "USA",
                        "zip": "95054",
                        "address": "4900 Marie P DeBartolo Way",
                        "capacity": 68500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 10
                    },
                    "away": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 9
                    },
                    "scoring": {
                        "home_points": 31,
                        "away_points": 21
                    }
                }
            ]
        },
        {
            "id": "63c47254-3cb0-4a28-bc5d-2b427587410c",
            "sequence": 11,
            "title": "11",
            "games": [
                {
                    "id": "124a1f59-22bf-43e4-97f8-db256b7c80bc",
                    "status": "closed",
                    "reference": "57389",
                    "number": 156,
                    "scheduled": "2017-11-19T21:05:00+00:00",
                    "attendance": 25015,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 76 F, Humidity: 24%, Wind: W 6 mph",
                    "venue": {
                        "id": "feebab00-4a3e-4123-8d7c-ed0dbdd96540",
                        "name": "StubHub Center",
                        "city": "Carson",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90746",
                        "address": "18400 Avalon Boulevard",
                        "capacity": 27000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 10
                    },
                    "away": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 54,
                        "away_points": 24
                    }
                },
                {
                    "id": "14d755ba-26bd-4b4d-9a5a-abfd2e9ea874",
                    "status": "closed",
                    "reference": "57393",
                    "number": 160,
                    "scheduled": "2017-11-21T01:30:00+00:00",
                    "attendance": 69026,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 45 F, Humidity: 85%, Wind: E 6 mph",
                    "venue": {
                        "id": "c6b9e5df-c9e4-434c-b3e6-83928f11cbda",
                        "name": "CenturyLink Field",
                        "city": "Seattle",
                        "state": "WA",
                        "country": "USA",
                        "zip": "98134",
                        "address": "800 Occidental Avenue South",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 10
                    },
                    "away": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 31,
                        "away_points": 34
                    }
                },
                {
                    "id": "2ae2f697-ad11-421c-a7a5-61c9c8833d79",
                    "status": "closed",
                    "reference": "57392",
                    "number": 159,
                    "scheduled": "2017-11-20T01:30:00+00:00",
                    "attendance": 93247,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Clear skies Temp: 54 F, Humidity: 36%, Wind:  0 mph",
                    "venue": {
                        "id": "1e84213a-ff1f-4c9d-a003-8ee782b25a40",
                        "name": "AT&T Stadium",
                        "city": "Arlington",
                        "state": "TX",
                        "country": "USA",
                        "zip": "76011",
                        "address": "One Legends Way",
                        "capacity": 80000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 10
                    },
                    "away": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 9,
                        "away_points": 37
                    }
                },
                {
                    "id": "63bc8526-8102-47d9-ab95-98a218110e73",
                    "status": "closed",
                    "reference": "57387",
                    "number": 154,
                    "scheduled": "2017-11-19T18:00:00+00:00",
                    "attendance": 73138,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 57 F, Humidity: 44%, Wind: N 12 mph",
                    "venue": {
                        "id": "3c85d89a-ec66-4983-acd5-1381d6c8673a",
                        "name": "Mercedes-Benz Superdome",
                        "city": "New Orleans",
                        "state": "LA",
                        "country": "USA",
                        "zip": "70112",
                        "address": "1500 Sugar Bowl Drive",
                        "capacity": 76468,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 10
                    },
                    "away": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 34,
                        "away_points": 31
                    }
                },
                {
                    "id": "67072e4c-062d-4000-a4f7-9a13e9c52611",
                    "status": "closed",
                    "reference": "57388",
                    "number": 155,
                    "scheduled": "2017-11-19T18:00:00+00:00",
                    "attendance": 76363,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Mostly Cloudy Temp: 50 F, Humidity: 44%, Wind: WNW 23 mph",
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 10
                    },
                    "away": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 12,
                        "away_points": 9
                    }
                },
                {
                    "id": "6f0070f3-9a3a-4312-9410-fc4da2d066ea",
                    "status": "closed",
                    "reference": "57384",
                    "number": 151,
                    "scheduled": "2017-11-19T18:00:00+00:00",
                    "attendance": 77945,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 28 F, Humidity: 63%, Wind: W-NW 14 mph",
                    "venue": {
                        "id": "5a60dd3a-302c-41c6-ab0f-dd335c1103c2",
                        "name": "Lambeau Field",
                        "city": "Green Bay",
                        "state": "WI",
                        "country": "USA",
                        "zip": "54304",
                        "address": "1265 Lombardi Avenue",
                        "capacity": 80750,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 10
                    },
                    "away": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 0,
                        "away_points": 23
                    }
                },
                {
                    "id": "76357794-0774-4e31-aa97-63268007eb92",
                    "status": "closed",
                    "reference": "57381",
                    "number": 148,
                    "scheduled": "2017-11-17T01:25:00+00:00",
                    "attendance": 60703,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 40 F, Humidity: 67%, Wind: from W 11-17 mph",
                    "venue": {
                        "id": "7349a2e6-0ac9-410b-8bd2-ca58c9f7aa34",
                        "name": "Heinz Field",
                        "city": "Pittsburgh",
                        "state": "PA",
                        "country": "USA",
                        "zip": "15212",
                        "address": "100 Art Rooney Avenue",
                        "capacity": 65050,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 10
                    },
                    "away": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 40,
                        "away_points": 17
                    }
                },
                {
                    "id": "7844429a-0b29-4796-bb9e-86e4d1f8d568",
                    "status": "closed",
                    "reference": "57386",
                    "number": 153,
                    "scheduled": "2017-11-19T18:00:00+00:00",
                    "attendance": 66809,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "N/A (Indoors) Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "f5ff00d4-1ed8-4918-bf73-13d66d510f98",
                        "name": "U.S. Bank Stadium",
                        "city": "Minneapolis",
                        "state": "MN",
                        "country": "USA",
                        "zip": "55415",
                        "address": "900 S 5th St",
                        "capacity": 66200,
                        "surface": "turf",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 10
                    },
                    "away": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 7
                    }
                },
                {
                    "id": "7a3bb862-d2e5-4d03-a444-b7e444e3ba9f",
                    "status": "closed",
                    "reference": "57241",
                    "number": 8,
                    "scheduled": "2017-11-19T18:00:00+00:00",
                    "attendance": 65089,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 81 F, Humidity: 51%, Wind: W 9 mph",
                    "venue": {
                        "id": "50a5c833-1570-4c38-abc7-7914cf87dbde",
                        "name": "Hard Rock Stadium",
                        "city": "Miami Gardens",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33056",
                        "address": "2269 Northwest 199th Street",
                        "capacity": 76100,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 10
                    },
                    "away": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 20,
                        "away_points": 30
                    }
                },
                {
                    "id": "9ebb8e8c-91c9-4e7d-b82e-9359940f2c52",
                    "status": "closed",
                    "reference": "57391",
                    "number": 158,
                    "scheduled": "2017-11-19T21:25:00+00:00",
                    "attendance": 77357,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 66 F, Humidity: 42%, Wind: N 0 mph",
                    "venue": {
                        "id": "949cb43e-531e-4d33-9ef5-85b54ef52f52",
                        "name": "Aztec Stadium",
                        "city": "Mexico City",
                        "state": null,
                        "country": "MX",
                        "zip": null,
                        "address": "Calzada de Tlalpan, 3465",
                        "capacity": 87000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 10
                    },
                    "away": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 8,
                        "away_points": 33
                    }
                },
                {
                    "id": "a387074a-2840-4b48-bf86-c2c7e048bf69",
                    "status": "closed",
                    "reference": "57383",
                    "number": 150,
                    "scheduled": "2017-11-19T18:00:00+00:00",
                    "attendance": 57003,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cold Temp: 38 F, Humidity: 68%, Wind: West 16 mph",
                    "venue": {
                        "id": "90c38d91-3774-4f5d-82ca-1c806828219f",
                        "name": "FirstEnergy Stadium",
                        "city": "Cleveland",
                        "state": "OH",
                        "country": "USA",
                        "zip": "44114",
                        "address": "100 Alfred Lerner Way",
                        "capacity": 71516,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 10
                    },
                    "away": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 7,
                        "away_points": 19
                    }
                },
                {
                    "id": "b93fab3e-0222-4017-876e-38e12f6a84e9",
                    "status": "closed",
                    "reference": "57390",
                    "number": 157,
                    "scheduled": "2017-11-19T21:25:00+00:00",
                    "attendance": 75707,
                    "utc_offset": -7,
                    "entry_mode": "INGEST",
                    "weather": "Mostly Cloudy Temp: 60 F, Humidity: 10%, Wind: North 7 mph",
                    "venue": {
                        "id": "6589e61d-ef1e-4e30-91b5-9acd2072b8a0",
                        "name": "Sports Authority Field at Mile High",
                        "city": "Denver",
                        "state": "CO",
                        "country": "USA",
                        "zip": "80204",
                        "address": "1701 Mile High Stadium Circle",
                        "capacity": 76125,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 10
                    },
                    "away": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 17,
                        "away_points": 20
                    }
                },
                {
                    "id": "d7922e91-2001-4b2f-9905-b3de10091153",
                    "status": "closed",
                    "reference": "57382",
                    "number": 149,
                    "scheduled": "2017-11-19T18:00:00+00:00",
                    "attendance": 60635,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 33 F, Humidity: 60%, Wind: WNW 16 mph",
                    "venue": {
                        "id": "d7866605-5ac6-4b3a-90e8-760cc5a26b75",
                        "name": "Soldier Field",
                        "city": "Chicago",
                        "state": "IL",
                        "country": "USA",
                        "zip": "60605",
                        "address": "1410 South Museum Campus Drive",
                        "capacity": 61500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 10
                    },
                    "away": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 27
                    }
                },
                {
                    "id": "e3e621f2-553c-4088-81ba-0952699ad47d",
                    "status": "closed",
                    "reference": "57385",
                    "number": 152,
                    "scheduled": "2017-11-19T18:00:00+00:00",
                    "attendance": 71680,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 60 F, Humidity: 31%, Wind: North 13 mph",
                    "venue": {
                        "id": "6a72e5ca-33d0-40af-8e6b-b32a4d3d9346",
                        "name": "NRG Stadium",
                        "city": "Houston",
                        "state": "TX",
                        "country": "USA",
                        "zip": "77054",
                        "address": "One Reliant Park",
                        "capacity": 71054,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 10
                    },
                    "away": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 10
                    },
                    "scoring": {
                        "home_points": 31,
                        "away_points": 21
                    }
                }
            ]
        },
        {
            "id": "d1cc39a8-51bc-4bb0-886c-c508326d14e8",
            "sequence": 12,
            "title": "12",
            "games": [
                {
                    "id": "1e79167b-add2-46b4-92ab-c53dddee7501",
                    "status": "closed",
                    "reference": "57407",
                    "number": 174,
                    "scheduled": "2017-11-26T21:25:00+00:00",
                    "attendance": 54994,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 61 F, Humidity: 95%, Wind: South 13 MPH mph",
                    "venue": {
                        "id": "ba1ad00f-9130-462c-93f9-5612a0015117",
                        "name": "Oakland Alameda Coliseum",
                        "city": "Oakland",
                        "state": "CA",
                        "country": "USA",
                        "zip": "94621",
                        "address": "7000 Coliseum Way",
                        "capacity": 53200,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 11
                    },
                    "away": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 21,
                        "away_points": 14
                    }
                },
                {
                    "id": "2c630d14-757e-4a95-87b5-5295326ed293",
                    "status": "closed",
                    "reference": "57400",
                    "number": 167,
                    "scheduled": "2017-11-26T18:00:00+00:00",
                    "attendance": 74929,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 62 F, Humidity: 36%, Wind: South Southwest 9 mph",
                    "venue": {
                        "id": "2ec4c411-dac2-403d-b091-6b6aa4a0a914",
                        "name": "Arrowhead Stadium",
                        "city": "Kansas City",
                        "state": "MO",
                        "country": "USA",
                        "zip": "64129",
                        "address": "One Arrowhead Drive",
                        "capacity": 79451,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 11
                    },
                    "away": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 10,
                        "away_points": 16
                    }
                },
                {
                    "id": "2ca940d1-c061-4a5a-9bc9-df2ac3c942cc",
                    "status": "closed",
                    "reference": "57402",
                    "number": 169,
                    "scheduled": "2017-11-26T18:00:00+00:00",
                    "attendance": 77562,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 47 F, Humidity: 42%, Wind: NW 14-23 mph",
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 11
                    },
                    "away": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 27,
                        "away_points": 35
                    }
                },
                {
                    "id": "376293d5-e0e3-40b7-be5c-7d94d1eee3a1",
                    "status": "closed",
                    "reference": "57395",
                    "number": 162,
                    "scheduled": "2017-11-23T21:30:00+00:00",
                    "attendance": 93012,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "1e84213a-ff1f-4c9d-a003-8ee782b25a40",
                        "name": "AT&T Stadium",
                        "city": "Arlington",
                        "state": "TX",
                        "country": "USA",
                        "zip": "76011",
                        "address": "One Legends Way",
                        "capacity": 80000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 11
                    },
                    "away": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 6,
                        "away_points": 28
                    }
                },
                {
                    "id": "41e7b5f1-7e3e-4179-8983-288575a44e29",
                    "status": "closed",
                    "reference": "57408",
                    "number": 175,
                    "scheduled": "2017-11-27T01:30:00+00:00",
                    "attendance": 62147,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 38 F, Humidity: 47%, Wind: From WSW 7 mph",
                    "venue": {
                        "id": "7349a2e6-0ac9-410b-8bd2-ca58c9f7aa34",
                        "name": "Heinz Field",
                        "city": "Pittsburgh",
                        "state": "PA",
                        "country": "USA",
                        "zip": "15212",
                        "address": "100 Art Rooney Avenue",
                        "capacity": 65050,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 11
                    },
                    "away": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 31,
                        "away_points": 28
                    }
                },
                {
                    "id": "470ae953-4a2e-4814-8a18-1b50f9236a26",
                    "status": "closed",
                    "reference": "57404",
                    "number": 171,
                    "scheduled": "2017-11-26T21:25:00+00:00",
                    "attendance": 62006,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 72 F, Humidity: 56%, Wind: North 1 mph",
                    "venue": {
                        "id": "4de1fcde-7c65-4145-a874-3753bdd584b7",
                        "name": "Los Angeles Memorial Coliseum",
                        "city": "Los Angeles",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90037",
                        "address": "3911 South Figueroa Street",
                        "capacity": 93607,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 11
                    },
                    "away": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 26,
                        "away_points": 20
                    }
                },
                {
                    "id": "588115c1-cbb0-4058-bbcd-7f4183605447",
                    "status": "closed",
                    "reference": "57403",
                    "number": 170,
                    "scheduled": "2017-11-26T18:00:00+00:00",
                    "attendance": 69596,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 49 F, Humidity: 42%, Wind: NW 17 mph",
                    "venue": {
                        "id": "4fa8c29c-6626-464c-8540-314ed7535e1b",
                        "name": "Lincoln Financial Field",
                        "city": "Philadelphia",
                        "state": "PA",
                        "country": "USA",
                        "zip": "19148",
                        "address": "1020 Pattison Avenue",
                        "capacity": 68532,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 11
                    },
                    "away": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 31,
                        "away_points": 3
                    }
                },
                {
                    "id": "618f5c58-2769-4dd0-930b-d653fe6f1ec2",
                    "status": "closed",
                    "reference": "57405",
                    "number": 172,
                    "scheduled": "2017-11-26T21:05:00+00:00",
                    "attendance": 70134,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "cloudy Temp: 66 F, Humidity: 68%, Wind: Southeast 12 mph",
                    "venue": {
                        "id": "24c71dbf-6469-46f1-8165-22005c295c8f",
                        "name": "Levi's Stadium",
                        "city": "Santa Clara",
                        "state": "CA",
                        "country": "USA",
                        "zip": "95054",
                        "address": "4900 Marie P DeBartolo Way",
                        "capacity": 68500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 11
                    },
                    "away": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 13,
                        "away_points": 24
                    }
                },
                {
                    "id": "7ce39386-21f8-4956-aa77-9a0e7b91b789",
                    "status": "closed",
                    "reference": "57398",
                    "number": 165,
                    "scheduled": "2017-11-26T18:00:00+00:00",
                    "attendance": 51710,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 46 F, Humidity: 52%, Wind: E 3 mph",
                    "venue": {
                        "id": "b87a1595-d3c8-48ea-8a53-0aab6378a64a",
                        "name": "Paul Brown Stadium",
                        "city": "Cincinnati",
                        "state": "OH",
                        "country": "USA",
                        "zip": "45202",
                        "address": "One Paul Brown Stadium",
                        "capacity": 65535,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 11
                    },
                    "away": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 30,
                        "away_points": 16
                    }
                },
                {
                    "id": "813ea22f-1572-4926-bbae-1d699a39f176",
                    "status": "closed",
                    "reference": "57399",
                    "number": 166,
                    "scheduled": "2017-11-26T18:00:00+00:00",
                    "attendance": 62207,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 47 F, Humidity: 51%, Wind: WSW 10 mph",
                    "venue": {
                        "id": "6ed18563-53e0-46c2-a91d-12d73a16456d",
                        "name": "Lucas Oil Stadium",
                        "city": "Indianapolis",
                        "state": "IN",
                        "country": "USA",
                        "zip": "46225",
                        "address": "500 South Capitol Avenue",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 11
                    },
                    "away": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 16,
                        "away_points": 20
                    }
                },
                {
                    "id": "96bd4ab2-c1ac-4ab2-9e2b-61cdda78dbb6",
                    "status": "closed",
                    "reference": "57396",
                    "number": 163,
                    "scheduled": "2017-11-24T01:30:00+00:00",
                    "attendance": 73210,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Clear Temp: 33 F, Humidity: 67%, Wind:  0 mph",
                    "venue": {
                        "id": "7c11bb2d-4a53-4842-b842-0f1c63ed78e9",
                        "name": "FedExField",
                        "city": "Landover",
                        "state": "MD",
                        "country": "USA",
                        "zip": "20785",
                        "address": "1600 FedEx Way",
                        "capacity": 83000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 11
                    },
                    "away": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 20,
                        "away_points": 10
                    }
                },
                {
                    "id": "a4d3a6ae-62bf-4379-af7b-f4041e3efe42",
                    "status": "closed",
                    "reference": "57409",
                    "number": 176,
                    "scheduled": "2017-11-28T01:30:00+00:00",
                    "attendance": 70357,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Fair Temp: 50 F, Humidity: 60%, Wind: Southeast 1 mph",
                    "venue": {
                        "id": "d54faae0-0314-484f-8604-9d8dd08e1149",
                        "name": "M&T Bank Stadium",
                        "city": "Baltimore",
                        "state": "MD",
                        "country": "USA",
                        "zip": "21230",
                        "address": "1101 Russell Street",
                        "capacity": 71008,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 11
                    },
                    "away": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 23,
                        "away_points": 16
                    }
                },
                {
                    "id": "b04995ac-6f37-4ed0-956b-1a4b8541d476",
                    "status": "closed",
                    "reference": "57406",
                    "number": 173,
                    "scheduled": "2017-11-26T21:25:00+00:00",
                    "attendance": 63891,
                    "utc_offset": -7,
                    "entry_mode": "INGEST",
                    "weather": "N/A Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "f8e4d2ab-53f9-4e9f-8c4a-9bc278094a84",
                        "name": "University of Phoenix Stadium",
                        "city": "Glendale",
                        "state": "AZ",
                        "country": "USA",
                        "zip": "85305",
                        "address": "One Cardinals Drive",
                        "capacity": 63400,
                        "surface": "turf",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 11
                    },
                    "away": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 27,
                        "away_points": 24
                    }
                },
                {
                    "id": "c363256d-3df5-4237-aa6e-dfd76ffdd413",
                    "status": "closed",
                    "reference": "57401",
                    "number": 168,
                    "scheduled": "2017-11-26T18:00:00+00:00",
                    "attendance": 65878,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Partly Cloudy Temp: 47 F, Humidity: 37%, Wind: WNW 17 mph",
                    "venue": {
                        "id": "e43310b1-cb82-4df9-8be5-e9b39637031b",
                        "name": "Gillette Stadium",
                        "city": "Foxborough",
                        "state": "MA",
                        "country": "USA",
                        "zip": "02035",
                        "address": "One Patriot Place",
                        "capacity": 68756,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 11
                    },
                    "away": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 35,
                        "away_points": 17
                    }
                },
                {
                    "id": "daae761d-cf70-4f32-ac91-a0fbb95903d0",
                    "status": "closed",
                    "reference": "57397",
                    "number": 164,
                    "scheduled": "2017-11-26T18:00:00+00:00",
                    "attendance": 71036,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "1f2d393b-2291-4835-a412-0714966ea245",
                        "name": "Mercedes-Benz Stadium",
                        "city": "Atlanta",
                        "state": "GA",
                        "country": "USA",
                        "zip": "30313",
                        "address": "441 Martin Luther King Jr Dr NW",
                        "capacity": 71000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 11
                    },
                    "away": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 34,
                        "away_points": 20
                    }
                },
                {
                    "id": "fc0573b5-252b-4ee2-8c72-39d0ec0c734a",
                    "status": "closed",
                    "reference": "57394",
                    "number": 161,
                    "scheduled": "2017-11-23T17:30:00+00:00",
                    "attendance": 66613,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Controlled Climate Temp: 68 F, Humidity: 70%, Wind:   mph",
                    "venue": {
                        "id": "6e3bcf22-277d-4c06-b019-62aded51654f",
                        "name": "Ford Field",
                        "city": "Detroit",
                        "state": "MI",
                        "country": "USA",
                        "zip": "48226",
                        "address": "2000 Brush Street",
                        "capacity": 65000,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 11
                    },
                    "away": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 11
                    },
                    "scoring": {
                        "home_points": 23,
                        "away_points": 30
                    }
                }
            ]
        },
        {
            "id": "e1348483-8bc0-4b83-96b6-1a63ee1a7cd6",
            "sequence": 13,
            "title": "13",
            "games": [
                {
                    "id": "0f022d9b-88bb-4d1e-a3bf-897e320f19b6",
                    "status": "closed",
                    "reference": "57417",
                    "number": 184,
                    "scheduled": "2017-12-03T18:00:00+00:00",
                    "attendance": 65092,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 80 F, Humidity: 58%, Wind: NE 11 mph",
                    "venue": {
                        "id": "50a5c833-1570-4c38-abc7-7914cf87dbde",
                        "name": "Hard Rock Stadium",
                        "city": "Miami Gardens",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33056",
                        "address": "2269 Northwest 199th Street",
                        "capacity": 76100,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 12
                    },
                    "away": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 35,
                        "away_points": 9
                    }
                },
                {
                    "id": "2fe080a3-354e-418c-99ea-ab8c3a06fd4d",
                    "status": "closed",
                    "reference": "57410",
                    "number": 177,
                    "scheduled": "2017-12-01T01:25:00+00:00",
                    "attendance": 91712,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Temp: 58 F, Humidity: 56%, Wind: North 3 mph",
                    "venue": {
                        "id": "1e84213a-ff1f-4c9d-a003-8ee782b25a40",
                        "name": "AT&T Stadium",
                        "city": "Arlington",
                        "state": "TX",
                        "country": "USA",
                        "zip": "76011",
                        "address": "One Legends Way",
                        "capacity": 80000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 12
                    },
                    "away": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 38,
                        "away_points": 14
                    }
                },
                {
                    "id": "38604ab9-c9fa-4ae6-9788-dfc930102682",
                    "status": "closed",
                    "reference": "57418",
                    "number": 185,
                    "scheduled": "2017-12-03T21:25:00+00:00",
                    "attendance": 73171,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 69 F, Humidity: 66%, Wind: NE 7 mph",
                    "venue": {
                        "id": "3c85d89a-ec66-4983-acd5-1381d6c8673a",
                        "name": "Mercedes-Benz Superdome",
                        "city": "New Orleans",
                        "state": "LA",
                        "country": "USA",
                        "zip": "70112",
                        "address": "1500 Sugar Bowl Drive",
                        "capacity": 76468,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 12
                    },
                    "away": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 31,
                        "away_points": 21
                    }
                },
                {
                    "id": "50368732-3474-4507-8c5e-1406e14bb905",
                    "status": "closed",
                    "reference": "57414",
                    "number": 181,
                    "scheduled": "2017-12-03T18:00:00+00:00",
                    "attendance": 61302,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 54 F, Humidity: 44%, Wind: SSW 6 mph",
                    "venue": {
                        "id": "d7866605-5ac6-4b3a-90e8-760cc5a26b75",
                        "name": "Soldier Field",
                        "city": "Chicago",
                        "state": "IL",
                        "country": "USA",
                        "zip": "60605",
                        "address": "1410 South Museum Campus Drive",
                        "capacity": 61500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 12
                    },
                    "away": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 14,
                        "away_points": 15
                    }
                },
                {
                    "id": "5e959f5e-332b-4a76-933c-4b888ff43153",
                    "status": "closed",
                    "reference": "57413",
                    "number": 180,
                    "scheduled": "2017-12-03T18:00:00+00:00",
                    "attendance": 68499,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Mostly cloudy Temp: 48 F, Humidity: 73%, Wind: WSW 13 mph",
                    "venue": {
                        "id": "e9e0828e-37fc-4238-a317-49037577dd55",
                        "name": "New Era Field",
                        "city": "Orchard Park",
                        "state": "NY",
                        "country": "USA",
                        "zip": "14127",
                        "address": "One Bills Drive",
                        "capacity": 73079,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 12
                    },
                    "away": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 3,
                        "away_points": 23
                    }
                },
                {
                    "id": "84f14212-323a-4d20-bcf3-e18cbcce1e8d",
                    "status": "closed",
                    "reference": "57419",
                    "number": 186,
                    "scheduled": "2017-12-03T18:00:00+00:00",
                    "attendance": 77562,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 48 F, Humidity: 64%, Wind:  0 mph",
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 12
                    },
                    "away": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 38,
                        "away_points": 31
                    }
                },
                {
                    "id": "9126e072-cbac-4b81-9bec-f9f42d5c9c13",
                    "status": "closed",
                    "reference": "57423",
                    "number": 190,
                    "scheduled": "2017-12-03T21:25:00+00:00",
                    "attendance": 54994,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 58 F, Humidity: 51%, Wind: West Northwest 11 mph",
                    "venue": {
                        "id": "ba1ad00f-9130-462c-93f9-5612a0015117",
                        "name": "Oakland Alameda Coliseum",
                        "city": "Oakland",
                        "state": "CA",
                        "country": "USA",
                        "zip": "94621",
                        "address": "7000 Coliseum Way",
                        "capacity": 53200,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 12
                    },
                    "away": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 17
                    }
                },
                {
                    "id": "a3553703-b6a6-4350-9c8b-bb0dcaf367f8",
                    "status": "closed",
                    "reference": "57422",
                    "number": 189,
                    "scheduled": "2017-12-03T21:25:00+00:00",
                    "attendance": 63986,
                    "utc_offset": -7,
                    "entry_mode": "INGEST",
                    "weather": "Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "f8e4d2ab-53f9-4e9f-8c4a-9bc278094a84",
                        "name": "University of Phoenix Stadium",
                        "city": "Glendale",
                        "state": "AZ",
                        "country": "USA",
                        "zip": "85305",
                        "address": "One Cardinals Drive",
                        "capacity": 63400,
                        "surface": "turf",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 12
                    },
                    "away": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 16,
                        "away_points": 32
                    }
                },
                {
                    "id": "aacebac9-8991-4ba5-989b-34e2a4348cd2",
                    "status": "closed",
                    "reference": "57424",
                    "number": 191,
                    "scheduled": "2017-12-04T01:30:00+00:00",
                    "attendance": 69075,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 44 F, Humidity: 78%, Wind: NNE 3 mph",
                    "venue": {
                        "id": "c6b9e5df-c9e4-434c-b3e6-83928f11cbda",
                        "name": "CenturyLink Field",
                        "city": "Seattle",
                        "state": "WA",
                        "country": "USA",
                        "zip": "98134",
                        "address": "800 Occidental Avenue South",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 12
                    },
                    "away": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 10
                    }
                },
                {
                    "id": "b600261f-9c55-4efc-9fef-b613fdead185",
                    "status": "closed",
                    "reference": "57420",
                    "number": 187,
                    "scheduled": "2017-12-03T18:00:00+00:00",
                    "attendance": 62758,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 60 F, Humidity: 39%, Wind: East 4 mph",
                    "venue": {
                        "id": "5295c1b7-c85c-49cb-9569-1707c65324e5",
                        "name": "Nissan Stadium",
                        "city": "Nashville",
                        "state": "TN",
                        "country": "USA",
                        "zip": "37213",
                        "address": "One Titans Way",
                        "capacity": 69143,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 12
                    },
                    "away": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 24,
                        "away_points": 13
                    }
                },
                {
                    "id": "b680801b-5ee3-412f-9f08-d4cd8d430c19",
                    "status": "closed",
                    "reference": "57411",
                    "number": 178,
                    "scheduled": "2017-12-03T18:00:00+00:00",
                    "attendance": 71185,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "1f2d393b-2291-4835-a412-0714966ea245",
                        "name": "Mercedes-Benz Stadium",
                        "city": "Atlanta",
                        "state": "GA",
                        "country": "USA",
                        "zip": "30313",
                        "address": "441 Martin Luther King Jr Dr NW",
                        "capacity": 71000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 12
                    },
                    "away": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 9,
                        "away_points": 14
                    }
                },
                {
                    "id": "c0c2a5a1-5721-47ba-a840-f1669177855d",
                    "status": "closed",
                    "reference": "57421",
                    "number": 188,
                    "scheduled": "2017-12-03T21:05:00+00:00",
                    "attendance": 25320,
                    "utc_offset": -8,
                    "entry_mode": "INGEST",
                    "weather": "Sunny Temp: 68 F, Humidity: 63%, Wind: WSW 7 mph",
                    "venue": {
                        "id": "feebab00-4a3e-4123-8d7c-ed0dbdd96540",
                        "name": "StubHub Center",
                        "city": "Carson",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90746",
                        "address": "18400 Avalon Boulevard",
                        "capacity": 27000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 12
                    },
                    "away": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 19,
                        "away_points": 10
                    }
                },
                {
                    "id": "c7d26cb1-3db2-4838-877f-7bae1526babb",
                    "status": "closed",
                    "reference": "57412",
                    "number": 179,
                    "scheduled": "2017-12-03T18:00:00+00:00",
                    "attendance": 70500,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Partly Sunny Temp: 53 F, Humidity: 70%, Wind: WNW 4 mph",
                    "venue": {
                        "id": "d54faae0-0314-484f-8604-9d8dd08e1149",
                        "name": "M&T Bank Stadium",
                        "city": "Baltimore",
                        "state": "MD",
                        "country": "USA",
                        "zip": "21230",
                        "address": "1101 Russell Street",
                        "capacity": 71008,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 12
                    },
                    "away": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 44,
                        "away_points": 20
                    }
                },
                {
                    "id": "d0d311f5-2cb1-4698-afbc-a94be4369059",
                    "status": "closed",
                    "reference": "57415",
                    "number": 182,
                    "scheduled": "2017-12-03T18:00:00+00:00",
                    "attendance": 77684,
                    "utc_offset": -6,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp: 42 F, Humidity: 67%, Wind: SE 8 mph",
                    "venue": {
                        "id": "5a60dd3a-302c-41c6-ab0f-dd335c1103c2",
                        "name": "Lambeau Field",
                        "city": "Green Bay",
                        "state": "WI",
                        "country": "USA",
                        "zip": "54304",
                        "address": "1265 Lombardi Avenue",
                        "capacity": 80750,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 12
                    },
                    "away": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 26,
                        "away_points": 20
                    }
                },
                {
                    "id": "e8aa210f-01a3-4ba0-8481-35153d1b1263",
                    "status": "closed",
                    "reference": "57416",
                    "number": 183,
                    "scheduled": "2017-12-03T18:00:00+00:00",
                    "attendance": 61207,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Temp: 70 F, Humidity: 74%, Wind: NE 7 mph",
                    "venue": {
                        "id": "4c5c036d-dd3d-4183-b595-71a43a97560f",
                        "name": "EverBank Field",
                        "city": "Jacksonville",
                        "state": "FL",
                        "country": "USA",
                        "zip": "32202",
                        "address": "One EverBank Field Drive",
                        "capacity": 67246,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 12
                    },
                    "away": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 30,
                        "away_points": 10
                    }
                },
                {
                    "id": "f35f05f3-44c7-4796-8435-1125e127be06",
                    "status": "closed",
                    "reference": "57425",
                    "number": 192,
                    "scheduled": "2017-12-05T01:30:00+00:00",
                    "attendance": 56029,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy, chance of rain Temp: 61 F, Humidity: 44%, Wind: SSW 9 mph",
                    "venue": {
                        "id": "b87a1595-d3c8-48ea-8a53-0aab6378a64a",
                        "name": "Paul Brown Stadium",
                        "city": "Cincinnati",
                        "state": "OH",
                        "country": "USA",
                        "zip": "45202",
                        "address": "One Paul Brown Stadium",
                        "capacity": 65535,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 12
                    },
                    "away": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 12
                    },
                    "scoring": {
                        "home_points": 20,
                        "away_points": 23
                    }
                }
            ]
        },
        {
            "id": "d3bb33eb-c232-4ce1-95a6-b4e54edeb8d3",
            "sequence": 14,
            "title": "14",
            "games": [
                {
                    "id": "0657cf66-89a6-4843-b9bc-95fa438672cb",
                    "status": "scheduled",
                    "reference": "57434",
                    "number": 201,
                    "scheduled": "2017-12-10T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "6fccc39c-80bc-4c81-83d9-2d5a848c8c09",
                        "name": "Raymond James Stadium",
                        "city": "Tampa",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33607",
                        "address": "4201 North Dale Mabry Highway",
                        "capacity": 65890,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 13
                    },
                    "away": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "19afed55-b6cb-4fae-b243-3d9abd8f60ff",
                    "status": "scheduled",
                    "reference": "57427",
                    "number": 194,
                    "scheduled": "2017-12-10T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "e9e0828e-37fc-4238-a317-49037577dd55",
                        "name": "New Era Field",
                        "city": "Orchard Park",
                        "state": "NY",
                        "country": "USA",
                        "zip": "14127",
                        "address": "One Bills Drive",
                        "capacity": 73079,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 13
                    },
                    "away": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "2511bdc1-c24d-4acd-b119-c28e94329afc",
                    "status": "scheduled",
                    "reference": "57440",
                    "number": 207,
                    "scheduled": "2017-12-11T01:30:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "7349a2e6-0ac9-410b-8bd2-ca58c9f7aa34",
                        "name": "Heinz Field",
                        "city": "Pittsburgh",
                        "state": "PA",
                        "country": "USA",
                        "zip": "15212",
                        "address": "100 Art Rooney Avenue",
                        "capacity": 65050,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 13
                    },
                    "away": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "770615bd-7f86-4895-ad55-638262ab2565",
                    "status": "scheduled",
                    "reference": "57433",
                    "number": 200,
                    "scheduled": "2017-12-10T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "2ec4c411-dac2-403d-b091-6b6aa4a0a914",
                        "name": "Arrowhead Stadium",
                        "city": "Kansas City",
                        "state": "MO",
                        "country": "USA",
                        "zip": "64129",
                        "address": "One Arrowhead Drive",
                        "capacity": 79451,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 13
                    },
                    "away": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "79ced4ab-f0c9-4d13-baac-926e3e6911d3",
                    "status": "closed",
                    "reference": "57426",
                    "number": 193,
                    "scheduled": "2017-12-08T01:25:00+00:00",
                    "attendance": 72866,
                    "utc_offset": -5,
                    "entry_mode": "INGEST",
                    "weather": "Cloudy Temp:  F, Wind:   mph",
                    "venue": {
                        "id": "1f2d393b-2291-4835-a412-0714966ea245",
                        "name": "Mercedes-Benz Stadium",
                        "city": "Atlanta",
                        "state": "GA",
                        "country": "USA",
                        "zip": "30313",
                        "address": "441 Martin Luther King Jr Dr NW",
                        "capacity": 71000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 13
                    },
                    "away": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 13
                    },
                    "scoring": {
                        "home_points": 20,
                        "away_points": 17
                    }
                },
                {
                    "id": "7a41781c-2fb1-4784-af1c-8c58dafa1e75",
                    "status": "scheduled",
                    "reference": "57435",
                    "number": 202,
                    "scheduled": "2017-12-10T21:05:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "f8e4d2ab-53f9-4e9f-8c4a-9bc278094a84",
                        "name": "University of Phoenix Stadium",
                        "city": "Glendale",
                        "state": "AZ",
                        "country": "USA",
                        "zip": "85305",
                        "address": "One Cardinals Drive",
                        "capacity": 63400,
                        "surface": "turf",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 13
                    },
                    "away": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "7af5e34c-8fdb-422f-8bc1-47cc84f82785",
                    "status": "scheduled",
                    "reference": "57429",
                    "number": 196,
                    "scheduled": "2017-12-10T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "b87a1595-d3c8-48ea-8a53-0aab6378a64a",
                        "name": "Paul Brown Stadium",
                        "city": "Cincinnati",
                        "state": "OH",
                        "country": "USA",
                        "zip": "45202",
                        "address": "One Paul Brown Stadium",
                        "capacity": 65535,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 13
                    },
                    "away": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "9cc867cb-9c36-4422-aa31-6ae47e10715e",
                    "status": "scheduled",
                    "reference": "57441",
                    "number": 208,
                    "scheduled": "2017-12-12T01:30:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "50a5c833-1570-4c38-abc7-7914cf87dbde",
                        "name": "Hard Rock Stadium",
                        "city": "Miami Gardens",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33056",
                        "address": "2269 Northwest 199th Street",
                        "capacity": 76100,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 13
                    },
                    "away": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "abeb2ae3-5287-4246-ac11-753229fa7f5d",
                    "status": "scheduled",
                    "reference": "57439",
                    "number": 206,
                    "scheduled": "2017-12-10T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 13
                    },
                    "away": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "aec26251-7614-485a-a6c7-1be4149f8989",
                    "status": "scheduled",
                    "reference": "57438",
                    "number": 205,
                    "scheduled": "2017-12-10T21:25:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "4de1fcde-7c65-4145-a874-3753bdd584b7",
                        "name": "Los Angeles Memorial Coliseum",
                        "city": "Los Angeles",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90037",
                        "address": "3911 South Figueroa Street",
                        "capacity": 93607,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 13
                    },
                    "away": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "afae03cb-a614-402a-b88a-d757b9de48a6",
                    "status": "scheduled",
                    "reference": "57436",
                    "number": 203,
                    "scheduled": "2017-12-10T21:05:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "6589e61d-ef1e-4e30-91b5-9acd2072b8a0",
                        "name": "Sports Authority Field at Mile High",
                        "city": "Denver",
                        "state": "CO",
                        "country": "USA",
                        "zip": "80204",
                        "address": "1701 Mile High Stadium Circle",
                        "capacity": 76125,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 13
                    },
                    "away": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "b1dbf64f-822a-49b7-9bf3-070f5d6da827",
                    "status": "scheduled",
                    "reference": "57428",
                    "number": 195,
                    "scheduled": "2017-12-10T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "39be9ed4-3292-49ac-8699-a381de3a4969",
                        "name": "Bank of America Stadium",
                        "city": "Charlotte",
                        "state": "NC",
                        "country": "USA",
                        "zip": "28202",
                        "address": "800 South Mint Street",
                        "capacity": 73778,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 13
                    },
                    "away": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "b7893266-1844-4a75-ad8a-b826b3834875",
                    "status": "scheduled",
                    "reference": "57430",
                    "number": 197,
                    "scheduled": "2017-12-10T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "90c38d91-3774-4f5d-82ca-1c806828219f",
                        "name": "FirstEnergy Stadium",
                        "city": "Cleveland",
                        "state": "OH",
                        "country": "USA",
                        "zip": "44114",
                        "address": "100 Alfred Lerner Way",
                        "capacity": 71516,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 13
                    },
                    "away": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "ce1444de-d3a3-4412-8eb9-322385944548",
                    "status": "scheduled",
                    "reference": "57437",
                    "number": 204,
                    "scheduled": "2017-12-10T21:05:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "feebab00-4a3e-4123-8d7c-ed0dbdd96540",
                        "name": "StubHub Center",
                        "city": "Carson",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90746",
                        "address": "18400 Avalon Boulevard",
                        "capacity": 27000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 13
                    },
                    "away": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "cfc17e2b-a4ff-4684-ad1d-9a7df876d93f",
                    "status": "scheduled",
                    "reference": "57432",
                    "number": 199,
                    "scheduled": "2017-12-10T21:25:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "4c5c036d-dd3d-4183-b595-71a43a97560f",
                        "name": "EverBank Field",
                        "city": "Jacksonville",
                        "state": "FL",
                        "country": "USA",
                        "zip": "32202",
                        "address": "One EverBank Field Drive",
                        "capacity": 67246,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 13
                    },
                    "away": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 13
                    },
                    "scoring": null
                },
                {
                    "id": "d7c5e4c0-9aa0-4e29-95a8-b3f54f3424f3",
                    "status": "scheduled",
                    "reference": "57431",
                    "number": 198,
                    "scheduled": "2017-12-10T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "6a72e5ca-33d0-40af-8e6b-b32a4d3d9346",
                        "name": "NRG Stadium",
                        "city": "Houston",
                        "state": "TX",
                        "country": "USA",
                        "zip": "77054",
                        "address": "One Reliant Park",
                        "capacity": 71054,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 13
                    },
                    "away": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 13
                    },
                    "scoring": null
                }
            ]
        },
        {
            "id": "415ff7e7-18e8-4bed-a93a-2cf7e7a97d55",
            "sequence": 15,
            "title": "15",
            "games": [
                {
                    "id": "03b325d4-b373-48da-9cfd-410ddb24d7fb",
                    "status": "scheduled",
                    "reference": "57443",
                    "number": 210,
                    "scheduled": "2017-12-16T21:30:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "6e3bcf22-277d-4c06-b019-62aded51654f",
                        "name": "Ford Field",
                        "city": "Detroit",
                        "state": "MI",
                        "country": "USA",
                        "zip": "48226",
                        "address": "2000 Brush Street",
                        "capacity": 65000,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 14
                    },
                    "away": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "0d24209f-a83b-4da4-a068-5c94dfe88081",
                    "status": "scheduled",
                    "reference": "57450",
                    "number": 217,
                    "scheduled": "2017-12-17T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "3c85d89a-ec66-4983-acd5-1381d6c8673a",
                        "name": "Mercedes-Benz Superdome",
                        "city": "New Orleans",
                        "state": "LA",
                        "country": "USA",
                        "zip": "70112",
                        "address": "1500 Sugar Bowl Drive",
                        "capacity": 76468,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 14
                    },
                    "away": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "192172fc-8ef1-4c4d-9832-4a9aa1f329e4",
                    "status": "scheduled",
                    "reference": "57457",
                    "number": 224,
                    "scheduled": "2017-12-19T01:30:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "6fccc39c-80bc-4c81-83d9-2d5a848c8c09",
                        "name": "Raymond James Stadium",
                        "city": "Tampa",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33607",
                        "address": "4201 North Dale Mabry Highway",
                        "capacity": 65890,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 14
                    },
                    "away": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "19ba3fe8-6d6b-4d6c-a0c7-c8b68806b8f6",
                    "status": "scheduled",
                    "reference": "57446",
                    "number": 213,
                    "scheduled": "2017-12-17T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "39be9ed4-3292-49ac-8699-a381de3a4969",
                        "name": "Bank of America Stadium",
                        "city": "Charlotte",
                        "state": "NC",
                        "country": "USA",
                        "zip": "28202",
                        "address": "800 South Mint Street",
                        "capacity": 73778,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 14
                    },
                    "away": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "46b96b1c-1df5-4c18-8999-36b6d39be24f",
                    "status": "scheduled",
                    "reference": "57451",
                    "number": 218,
                    "scheduled": "2017-12-17T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 14
                    },
                    "away": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "4c1f40fb-04ea-45fd-98e1-3aabc4f4e8a7",
                    "status": "scheduled",
                    "reference": "57455",
                    "number": 222,
                    "scheduled": "2017-12-17T21:25:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "24c71dbf-6469-46f1-8165-22005c295c8f",
                        "name": "Levi's Stadium",
                        "city": "Santa Clara",
                        "state": "CA",
                        "country": "USA",
                        "zip": "95054",
                        "address": "4900 Marie P DeBartolo Way",
                        "capacity": 68500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 14
                    },
                    "away": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "5e307142-2f4f-467d-9bde-a5f7cb25f518",
                    "status": "scheduled",
                    "reference": "57445",
                    "number": 212,
                    "scheduled": "2017-12-17T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "e9e0828e-37fc-4238-a317-49037577dd55",
                        "name": "New Era Field",
                        "city": "Orchard Park",
                        "state": "NY",
                        "country": "USA",
                        "zip": "14127",
                        "address": "One Bills Drive",
                        "capacity": 73079,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 14
                    },
                    "away": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "87e23541-9b26-49f9-8599-fe8879ba51d0",
                    "status": "scheduled",
                    "reference": "57453",
                    "number": 220,
                    "scheduled": "2017-12-17T21:05:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "c6b9e5df-c9e4-434c-b3e6-83928f11cbda",
                        "name": "CenturyLink Field",
                        "city": "Seattle",
                        "state": "WA",
                        "country": "USA",
                        "zip": "98134",
                        "address": "800 Occidental Avenue South",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 14
                    },
                    "away": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "99cd8a66-a615-4c29-a17e-0b0136bc555a",
                    "status": "scheduled",
                    "reference": "57447",
                    "number": 214,
                    "scheduled": "2017-12-17T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "90c38d91-3774-4f5d-82ca-1c806828219f",
                        "name": "FirstEnergy Stadium",
                        "city": "Cleveland",
                        "state": "OH",
                        "country": "USA",
                        "zip": "44114",
                        "address": "100 Alfred Lerner Way",
                        "capacity": 71516,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 14
                    },
                    "away": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "a328b412-eb5e-42f8-b2b4-fad4235ee26e",
                    "status": "scheduled",
                    "reference": "57444",
                    "number": 211,
                    "scheduled": "2017-12-17T01:25:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "2ec4c411-dac2-403d-b091-6b6aa4a0a914",
                        "name": "Arrowhead Stadium",
                        "city": "Kansas City",
                        "state": "MO",
                        "country": "USA",
                        "zip": "64129",
                        "address": "One Arrowhead Drive",
                        "capacity": 79451,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 14
                    },
                    "away": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "af8157a8-8a53-46a5-a173-bc42c5b16eb6",
                    "status": "scheduled",
                    "reference": "57452",
                    "number": 219,
                    "scheduled": "2017-12-17T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "7c11bb2d-4a53-4842-b842-0f1c63ed78e9",
                        "name": "FedExField",
                        "city": "Landover",
                        "state": "MD",
                        "country": "USA",
                        "zip": "20785",
                        "address": "1600 FedEx Way",
                        "capacity": 83000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 14
                    },
                    "away": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "b31a21b9-3a44-4ef5-a98a-97b672b30f7a",
                    "status": "scheduled",
                    "reference": "57456",
                    "number": 223,
                    "scheduled": "2017-12-18T01:30:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "ba1ad00f-9130-462c-93f9-5612a0015117",
                        "name": "Oakland Alameda Coliseum",
                        "city": "Oakland",
                        "state": "CA",
                        "country": "USA",
                        "zip": "94621",
                        "address": "7000 Coliseum Way",
                        "capacity": 53200,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 14
                    },
                    "away": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "bc9ac00d-4606-420e-86da-a81a10c0ec78",
                    "status": "scheduled",
                    "reference": "57442",
                    "number": 209,
                    "scheduled": "2017-12-15T01:25:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "6ed18563-53e0-46c2-a91d-12d73a16456d",
                        "name": "Lucas Oil Stadium",
                        "city": "Indianapolis",
                        "state": "IN",
                        "country": "USA",
                        "zip": "46225",
                        "address": "500 South Capitol Avenue",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 14
                    },
                    "away": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "cd2bd061-0acd-4f3b-97e4-9b2ca04f99b1",
                    "status": "scheduled",
                    "reference": "57454",
                    "number": 221,
                    "scheduled": "2017-12-17T21:25:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "7349a2e6-0ac9-410b-8bd2-ca58c9f7aa34",
                        "name": "Heinz Field",
                        "city": "Pittsburgh",
                        "state": "PA",
                        "country": "USA",
                        "zip": "15212",
                        "address": "100 Art Rooney Avenue",
                        "capacity": 65050,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 14
                    },
                    "away": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "db925d38-6d49-4c32-b2f3-cd792db0f147",
                    "status": "scheduled",
                    "reference": "57449",
                    "number": 216,
                    "scheduled": "2017-12-17T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "f5ff00d4-1ed8-4918-bf73-13d66d510f98",
                        "name": "U.S. Bank Stadium",
                        "city": "Minneapolis",
                        "state": "MN",
                        "country": "USA",
                        "zip": "55415",
                        "address": "900 S 5th St",
                        "capacity": 66200,
                        "surface": "turf",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 14
                    },
                    "away": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 14
                    },
                    "scoring": null
                },
                {
                    "id": "fff9072a-d9dc-4544-af0c-1ae707753496",
                    "status": "scheduled",
                    "reference": "57448",
                    "number": 215,
                    "scheduled": "2017-12-17T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "4c5c036d-dd3d-4183-b595-71a43a97560f",
                        "name": "EverBank Field",
                        "city": "Jacksonville",
                        "state": "FL",
                        "country": "USA",
                        "zip": "32202",
                        "address": "One EverBank Field Drive",
                        "capacity": 67246,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 14
                    },
                    "away": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 14
                    },
                    "scoring": null
                }
            ]
        },
        {
            "id": "115af970-dbe9-4a0f-8343-549884be5557",
            "sequence": 16,
            "title": "16",
            "games": [
                {
                    "id": "03a081dd-0102-4077-b423-de3e1dff7cc4",
                    "status": "scheduled",
                    "reference": "57459",
                    "number": 226,
                    "scheduled": "2017-12-24T01:30:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "5a60dd3a-302c-41c6-ab0f-dd335c1103c2",
                        "name": "Lambeau Field",
                        "city": "Green Bay",
                        "state": "WI",
                        "country": "USA",
                        "zip": "54304",
                        "address": "1265 Lombardi Avenue",
                        "capacity": 80750,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 15
                    },
                    "away": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "06281b5e-de29-4edf-90f8-b7c1167cf02c",
                    "status": "scheduled",
                    "reference": "57469",
                    "number": 236,
                    "scheduled": "2017-12-24T21:05:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "24c71dbf-6469-46f1-8165-22005c295c8f",
                        "name": "Levi's Stadium",
                        "city": "Santa Clara",
                        "state": "CA",
                        "country": "USA",
                        "zip": "95054",
                        "address": "4900 Marie P DeBartolo Way",
                        "capacity": 68500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 15
                    },
                    "away": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "30a50d91-b8e1-4cd7-b45c-a982a67f31c7",
                    "status": "scheduled",
                    "reference": "57471",
                    "number": 238,
                    "scheduled": "2017-12-24T21:25:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "1e84213a-ff1f-4c9d-a003-8ee782b25a40",
                        "name": "AT&T Stadium",
                        "city": "Arlington",
                        "state": "TX",
                        "country": "USA",
                        "zip": "76011",
                        "address": "One Legends Way",
                        "capacity": 80000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 15
                    },
                    "away": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "3d37f762-90a1-429c-854e-2a32b3f873a7",
                    "status": "scheduled",
                    "reference": "57460",
                    "number": 227,
                    "scheduled": "2017-12-24T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "39be9ed4-3292-49ac-8699-a381de3a4969",
                        "name": "Bank of America Stadium",
                        "city": "Charlotte",
                        "state": "NC",
                        "country": "USA",
                        "zip": "28202",
                        "address": "800 South Mint Street",
                        "capacity": 73778,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 15
                    },
                    "away": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "46866731-e0a4-41ee-82ab-90d8253d088e",
                    "status": "scheduled",
                    "reference": "57466",
                    "number": 233,
                    "scheduled": "2017-12-24T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 15
                    },
                    "away": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "47dd8e21-2102-4bc8-a37f-2b865b5c3012",
                    "status": "scheduled",
                    "reference": "57470",
                    "number": 237,
                    "scheduled": "2017-12-24T21:25:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "f8e4d2ab-53f9-4e9f-8c4a-9bc278094a84",
                        "name": "University of Phoenix Stadium",
                        "city": "Glendale",
                        "state": "AZ",
                        "country": "USA",
                        "zip": "85305",
                        "address": "One Cardinals Drive",
                        "capacity": 63400,
                        "surface": "turf",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 15
                    },
                    "away": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "5388b04a-3b86-40da-8ed2-6c6d2cc2edec",
                    "status": "scheduled",
                    "reference": "57472",
                    "number": 239,
                    "scheduled": "2017-12-25T21:30:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "6a72e5ca-33d0-40af-8e6b-b32a4d3d9346",
                        "name": "NRG Stadium",
                        "city": "Houston",
                        "state": "TX",
                        "country": "USA",
                        "zip": "77054",
                        "address": "One Reliant Park",
                        "capacity": 71054,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 15
                    },
                    "away": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "5c981331-29d9-4c65-b4f2-5e1101295b3e",
                    "status": "scheduled",
                    "reference": "57468",
                    "number": 235,
                    "scheduled": "2017-12-24T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "7c11bb2d-4a53-4842-b842-0f1c63ed78e9",
                        "name": "FedExField",
                        "city": "Landover",
                        "state": "MD",
                        "country": "USA",
                        "zip": "20785",
                        "address": "1600 FedEx Way",
                        "capacity": 83000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 15
                    },
                    "away": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "9ea314e7-4e50-4488-a799-41724d0fcf0e",
                    "status": "scheduled",
                    "reference": "57461",
                    "number": 228,
                    "scheduled": "2017-12-24T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "d7866605-5ac6-4b3a-90e8-760cc5a26b75",
                        "name": "Soldier Field",
                        "city": "Chicago",
                        "state": "IL",
                        "country": "USA",
                        "zip": "60605",
                        "address": "1410 South Museum Campus Drive",
                        "capacity": 61500,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 15
                    },
                    "away": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "dd32f408-dc48-4295-80b5-8782894bea53",
                    "status": "scheduled",
                    "reference": "57463",
                    "number": 230,
                    "scheduled": "2017-12-24T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "2ec4c411-dac2-403d-b091-6b6aa4a0a914",
                        "name": "Arrowhead Stadium",
                        "city": "Kansas City",
                        "state": "MO",
                        "country": "USA",
                        "zip": "64129",
                        "address": "One Arrowhead Drive",
                        "capacity": 79451,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 15
                    },
                    "away": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "dd7f8df0-c5dc-4245-b88b-44e8f262843a",
                    "status": "scheduled",
                    "reference": "57458",
                    "number": 225,
                    "scheduled": "2017-12-23T21:30:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "d54faae0-0314-484f-8604-9d8dd08e1149",
                        "name": "M&T Bank Stadium",
                        "city": "Baltimore",
                        "state": "MD",
                        "country": "USA",
                        "zip": "21230",
                        "address": "1101 Russell Street",
                        "capacity": 71008,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 15
                    },
                    "away": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "e0bf82c6-2fba-4712-b337-39ef547be27f",
                    "status": "scheduled",
                    "reference": "57473",
                    "number": 240,
                    "scheduled": "2017-12-26T01:30:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "4fa8c29c-6626-464c-8540-314ed7535e1b",
                        "name": "Lincoln Financial Field",
                        "city": "Philadelphia",
                        "state": "PA",
                        "country": "USA",
                        "zip": "19148",
                        "address": "1020 Pattison Avenue",
                        "capacity": 68532,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 15
                    },
                    "away": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "e4d20812-74a2-48ab-b0f7-f3d63acd354e",
                    "status": "scheduled",
                    "reference": "57465",
                    "number": 232,
                    "scheduled": "2017-12-24T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "3c85d89a-ec66-4983-acd5-1381d6c8673a",
                        "name": "Mercedes-Benz Superdome",
                        "city": "New Orleans",
                        "state": "LA",
                        "country": "USA",
                        "zip": "70112",
                        "address": "1500 Sugar Bowl Drive",
                        "capacity": 76468,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 15
                    },
                    "away": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "ed2ead9e-7941-4854-a2a1-9684481a90ae",
                    "status": "scheduled",
                    "reference": "57462",
                    "number": 229,
                    "scheduled": "2017-12-24T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "b87a1595-d3c8-48ea-8a53-0aab6378a64a",
                        "name": "Paul Brown Stadium",
                        "city": "Cincinnati",
                        "state": "OH",
                        "country": "USA",
                        "zip": "45202",
                        "address": "One Paul Brown Stadium",
                        "capacity": 65535,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 15
                    },
                    "away": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "ee225389-956f-490e-9dab-1fccfa6f4029",
                    "status": "scheduled",
                    "reference": "57464",
                    "number": 231,
                    "scheduled": "2017-12-24T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "e43310b1-cb82-4df9-8be5-e9b39637031b",
                        "name": "Gillette Stadium",
                        "city": "Foxborough",
                        "state": "MA",
                        "country": "USA",
                        "zip": "02035",
                        "address": "One Patriot Place",
                        "capacity": 68756,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 15
                    },
                    "away": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 15
                    },
                    "scoring": null
                },
                {
                    "id": "f3c823f1-8902-4271-b2b0-0d5859f4a830",
                    "status": "scheduled",
                    "reference": "57467",
                    "number": 234,
                    "scheduled": "2017-12-24T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "5295c1b7-c85c-49cb-9569-1707c65324e5",
                        "name": "Nissan Stadium",
                        "city": "Nashville",
                        "state": "TN",
                        "country": "USA",
                        "zip": "37213",
                        "address": "One Titans Way",
                        "capacity": 69143,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 15
                    },
                    "away": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 15
                    },
                    "scoring": null
                }
            ]
        },
        {
            "id": "5201c1ff-fcfa-4a7c-b4f2-c736406f24a1",
            "sequence": 17,
            "title": "17",
            "games": [
                {
                    "id": "1a3f5fb8-8cba-4938-9d43-5ef79db26fb7",
                    "status": "scheduled",
                    "reference": "57479",
                    "number": 246,
                    "scheduled": "2017-12-31T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "f5ff00d4-1ed8-4918-bf73-13d66d510f98",
                        "name": "U.S. Bank Stadium",
                        "city": "Minneapolis",
                        "state": "MN",
                        "country": "USA",
                        "zip": "55415",
                        "address": "900 S 5th St",
                        "capacity": 66200,
                        "surface": "turf",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "33405046-04ee-4058-a950-d606f8c30852",
                        "name": "Minnesota Vikings",
                        "alias": "MIN",
                        "game_number": 16
                    },
                    "away": {
                        "id": "7b112545-38e6-483c-a55c-96cf6ee49cb8",
                        "name": "Chicago Bears",
                        "alias": "CHI",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "370fff7a-4bc6-4fb7-b71a-c9f4789a3b1a",
                    "status": "scheduled",
                    "reference": "57477",
                    "number": 244,
                    "scheduled": "2017-12-31T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "6ed18563-53e0-46c2-a91d-12d73a16456d",
                        "name": "Lucas Oil Stadium",
                        "city": "Indianapolis",
                        "state": "IN",
                        "country": "USA",
                        "zip": "46225",
                        "address": "500 South Capitol Avenue",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "82cf9565-6eb9-4f01-bdbd-5aa0d472fcd9",
                        "name": "Indianapolis Colts",
                        "alias": "IND",
                        "game_number": 16
                    },
                    "away": {
                        "id": "82d2d380-3834-4938-835f-aec541e5ece7",
                        "name": "Houston Texans",
                        "alias": "HOU",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "4d935d42-d037-49ca-a1ba-fa2d408ee87c",
                    "status": "scheduled",
                    "reference": "57476",
                    "number": 243,
                    "scheduled": "2017-12-31T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "6e3bcf22-277d-4c06-b019-62aded51654f",
                        "name": "Ford Field",
                        "city": "Detroit",
                        "state": "MI",
                        "country": "USA",
                        "zip": "48226",
                        "address": "2000 Brush Street",
                        "capacity": 65000,
                        "surface": "artificial",
                        "roof_type": "dome"
                    },
                    "home": {
                        "id": "c5a59daa-53a7-4de0-851f-fb12be893e9e",
                        "name": "Detroit Lions",
                        "alias": "DET",
                        "game_number": 16
                    },
                    "away": {
                        "id": "a20471b4-a8d9-40c7-95ad-90cc30e46932",
                        "name": "Green Bay Packers",
                        "alias": "GB",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "4f7f818a-1e0f-4ecf-b97b-d23a776757b0",
                    "status": "scheduled",
                    "reference": "57485",
                    "number": 252,
                    "scheduled": "2017-12-31T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "5295c1b7-c85c-49cb-9569-1707c65324e5",
                        "name": "Nissan Stadium",
                        "city": "Nashville",
                        "state": "TN",
                        "country": "USA",
                        "zip": "37213",
                        "address": "One Titans Way",
                        "capacity": 69143,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "d26a1ca5-722d-4274-8f97-c92e49c96315",
                        "name": "Tennessee Titans",
                        "alias": "TEN",
                        "game_number": 16
                    },
                    "away": {
                        "id": "f7ddd7fa-0bae-4f90-bc8e-669e4d6cf2de",
                        "name": "Jacksonville Jaguars",
                        "alias": "JAC",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "579874b1-61d8-4861-b624-317ecafeee92",
                    "status": "scheduled",
                    "reference": "57480",
                    "number": 247,
                    "scheduled": "2017-12-31T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "e43310b1-cb82-4df9-8be5-e9b39637031b",
                        "name": "Gillette Stadium",
                        "city": "Foxborough",
                        "state": "MA",
                        "country": "USA",
                        "zip": "02035",
                        "address": "One Patriot Place",
                        "capacity": 68756,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "97354895-8c77-4fd4-a860-32e62ea7382a",
                        "name": "New England Patriots",
                        "alias": "NE",
                        "game_number": 16
                    },
                    "away": {
                        "id": "5fee86ae-74ab-4bdd-8416-42a9dd9964f3",
                        "name": "New York Jets",
                        "alias": "NYJ",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "6a5b5716-9f75-4f4a-95ae-a0e09f16d9a9",
                    "status": "scheduled",
                    "reference": "57482",
                    "number": 249,
                    "scheduled": "2017-12-31T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "4fa8c29c-6626-464c-8540-314ed7535e1b",
                        "name": "Lincoln Financial Field",
                        "city": "Philadelphia",
                        "state": "PA",
                        "country": "USA",
                        "zip": "19148",
                        "address": "1020 Pattison Avenue",
                        "capacity": 68532,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "386bdbf9-9eea-4869-bb9a-274b0bc66e80",
                        "name": "Philadelphia Eagles",
                        "alias": "PHI",
                        "game_number": 16
                    },
                    "away": {
                        "id": "e627eec7-bbae-4fa4-8e73-8e1d6bc5c060",
                        "name": "Dallas Cowboys",
                        "alias": "DAL",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "70fa992d-c737-4e08-98f0-d0e584010360",
                    "status": "scheduled",
                    "reference": "57475",
                    "number": 242,
                    "scheduled": "2017-12-31T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "d54faae0-0314-484f-8604-9d8dd08e1149",
                        "name": "M&T Bank Stadium",
                        "city": "Baltimore",
                        "state": "MD",
                        "country": "USA",
                        "zip": "21230",
                        "address": "1101 Russell Street",
                        "capacity": 71008,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ebd87119-b331-4469-9ea6-d51fe3ce2f1c",
                        "name": "Baltimore Ravens",
                        "alias": "BAL",
                        "game_number": 16
                    },
                    "away": {
                        "id": "ad4ae08f-d808-42d5-a1e6-e9bc4e34d123",
                        "name": "Cincinnati Bengals",
                        "alias": "CIN",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "7e3791d4-7795-4109-82a9-8473d8ab35e5",
                    "status": "scheduled",
                    "reference": "57481",
                    "number": 248,
                    "scheduled": "2017-12-31T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "5d4c85c7-d84e-4e10-bd6a-8a15ebecca5c",
                        "name": "MetLife Stadium",
                        "city": "East Rutherford",
                        "state": "NJ",
                        "country": "USA",
                        "zip": "07073",
                        "address": "One MetLife Stadium Drive",
                        "capacity": 82500,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "04aa1c9d-66da-489d-b16a-1dee3f2eec4d",
                        "name": "New York Giants",
                        "alias": "NYG",
                        "game_number": 16
                    },
                    "away": {
                        "id": "22052ff7-c065-42ee-bc8f-c4691c50e624",
                        "name": "Washington Redskins",
                        "alias": "WAS",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "85181f82-4a58-4c54-b16c-b4c596ebed49",
                    "status": "scheduled",
                    "reference": "57488",
                    "number": 255,
                    "scheduled": "2017-12-31T21:25:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "4de1fcde-7c65-4145-a874-3753bdd584b7",
                        "name": "Los Angeles Memorial Coliseum",
                        "city": "Los Angeles",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90037",
                        "address": "3911 South Figueroa Street",
                        "capacity": 93607,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "2eff2a03-54d4-46ba-890e-2bc3925548f3",
                        "name": "Los Angeles Rams",
                        "alias": "LA",
                        "game_number": 16
                    },
                    "away": {
                        "id": "f0e724b0-4cbf-495a-be47-013907608da9",
                        "name": "San Francisco 49ers",
                        "alias": "SF",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "a84ebeed-dc14-4c9c-9279-d376cb798672",
                    "status": "scheduled",
                    "reference": "57489",
                    "number": 256,
                    "scheduled": "2017-12-31T21:25:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "c6b9e5df-c9e4-434c-b3e6-83928f11cbda",
                        "name": "CenturyLink Field",
                        "city": "Seattle",
                        "state": "WA",
                        "country": "USA",
                        "zip": "98134",
                        "address": "800 Occidental Avenue South",
                        "capacity": 67000,
                        "surface": "artificial",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "3d08af9e-c767-4f88-a7dc-b920c6d2b4a8",
                        "name": "Seattle Seahawks",
                        "alias": "SEA",
                        "game_number": 16
                    },
                    "away": {
                        "id": "de760528-1dc0-416a-a978-b510d20692ff",
                        "name": "Arizona Cardinals",
                        "alias": "ARI",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "aa3b4a5c-5b0d-4eec-92c4-5046ebbfce5d",
                    "status": "scheduled",
                    "reference": "57483",
                    "number": 250,
                    "scheduled": "2017-12-31T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "7349a2e6-0ac9-410b-8bd2-ca58c9f7aa34",
                        "name": "Heinz Field",
                        "city": "Pittsburgh",
                        "state": "PA",
                        "country": "USA",
                        "zip": "15212",
                        "address": "100 Art Rooney Avenue",
                        "capacity": 65050,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "cb2f9f1f-ac67-424e-9e72-1475cb0ed398",
                        "name": "Pittsburgh Steelers",
                        "alias": "PIT",
                        "game_number": 16
                    },
                    "away": {
                        "id": "d5a2eb42-8065-4174-ab79-0a6fa820e35e",
                        "name": "Cleveland Browns",
                        "alias": "CLE",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "adbac8aa-f73b-44cf-83a3-0707c4ecddc4",
                    "status": "scheduled",
                    "reference": "57487",
                    "number": 254,
                    "scheduled": "2017-12-31T21:25:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "feebab00-4a3e-4123-8d7c-ed0dbdd96540",
                        "name": "StubHub Center",
                        "city": "Carson",
                        "state": "CA",
                        "country": "USA",
                        "zip": "90746",
                        "address": "18400 Avalon Boulevard",
                        "capacity": 27000,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "1f6dcffb-9823-43cd-9ff4-e7a8466749b5",
                        "name": "Los Angeles Chargers",
                        "alias": "LAC",
                        "game_number": 16
                    },
                    "away": {
                        "id": "1c1cec48-6352-4556-b789-35304c1a6ae1",
                        "name": "Oakland Raiders",
                        "alias": "OAK",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "b0c85836-a7f0-4c42-ba84-17d0ad62cc7f",
                    "status": "scheduled",
                    "reference": "57474",
                    "number": 241,
                    "scheduled": "2017-12-31T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "1f2d393b-2291-4835-a412-0714966ea245",
                        "name": "Mercedes-Benz Stadium",
                        "city": "Atlanta",
                        "state": "GA",
                        "country": "USA",
                        "zip": "30313",
                        "address": "441 Martin Luther King Jr Dr NW",
                        "capacity": 71000,
                        "surface": "artificial",
                        "roof_type": "retractable_dome"
                    },
                    "home": {
                        "id": "e6aa13a4-0055-48a9-bc41-be28dc106929",
                        "name": "Atlanta Falcons",
                        "alias": "ATL",
                        "game_number": 16
                    },
                    "away": {
                        "id": "f14bf5cc-9a82-4a38-bc15-d39f75ed5314",
                        "name": "Carolina Panthers",
                        "alias": "CAR",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "e811d656-0902-4bfb-a472-7e5f70aaaff9",
                    "status": "scheduled",
                    "reference": "57486",
                    "number": 253,
                    "scheduled": "2017-12-31T21:25:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "6589e61d-ef1e-4e30-91b5-9acd2072b8a0",
                        "name": "Sports Authority Field at Mile High",
                        "city": "Denver",
                        "state": "CO",
                        "country": "USA",
                        "zip": "80204",
                        "address": "1701 Mile High Stadium Circle",
                        "capacity": 76125,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "ce92bd47-93d5-4fe9-ada4-0fc681e6caa0",
                        "name": "Denver Broncos",
                        "alias": "DEN",
                        "game_number": 16
                    },
                    "away": {
                        "id": "6680d28d-d4d2-49f6-aace-5292d3ec02c2",
                        "name": "Kansas City Chiefs",
                        "alias": "KC",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "fc25d015-bcb0-4b49-8985-cc48aa5ec25d",
                    "status": "scheduled",
                    "reference": "57478",
                    "number": 245,
                    "scheduled": "2017-12-31T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "50a5c833-1570-4c38-abc7-7914cf87dbde",
                        "name": "Hard Rock Stadium",
                        "city": "Miami Gardens",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33056",
                        "address": "2269 Northwest 199th Street",
                        "capacity": 76100,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4809ecb0-abd3-451d-9c4a-92a90b83ca06",
                        "name": "Miami Dolphins",
                        "alias": "MIA",
                        "game_number": 16
                    },
                    "away": {
                        "id": "768c92aa-75ff-4a43-bcc0-f2798c2e1724",
                        "name": "Buffalo Bills",
                        "alias": "BUF",
                        "game_number": 16
                    },
                    "scoring": null
                },
                {
                    "id": "fe193626-01ac-44fd-8fb6-de65c90cf65a",
                    "status": "scheduled",
                    "reference": "57484",
                    "number": 251,
                    "scheduled": "2017-12-31T18:00:00+00:00",
                    "attendance": 0,
                    "utc_offset": 0,
                    "entry_mode": "INGEST",
                    "weather": null,
                    "venue": {
                        "id": "6fccc39c-80bc-4c81-83d9-2d5a848c8c09",
                        "name": "Raymond James Stadium",
                        "city": "Tampa",
                        "state": "FL",
                        "country": "USA",
                        "zip": "33607",
                        "address": "4201 North Dale Mabry Highway",
                        "capacity": 65890,
                        "surface": "turf",
                        "roof_type": "outdoor"
                    },
                    "home": {
                        "id": "4254d319-1bc7-4f81-b4ab-b5e6f3402b69",
                        "name": "Tampa Bay Buccaneers",
                        "alias": "TB",
                        "game_number": 16
                    },
                    "away": {
                        "id": "0d855753-ea21-4953-89f9-0e20aff9eb73",
                        "name": "New Orleans Saints",
                        "alias": "NO",
                        "game_number": 16
                    },
                    "scoring": null
                }
            ]
        }
    ]
}
	 */

}
