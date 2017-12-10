package com.cloudgames.entities;

public class SRScheduleGame {

	public String id;
	public String status;
	public String reference;
	public int number;
	public String scheduled;
	public int attendance;
	public int utc_offset;
	public String entry_mode;
	public String weather;
	public SRScheduleVenue venue;
	public SRScheduleTeam home;
	public SRScheduleTeam away;
	public SRScheduleScoring scoring;
}
/*
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
                "broadcast": {
                    "network": "ESPN",
                    "satellite": "206",
                    "internet": "WatchESPN"
                },
                "scoring": {
                    "home_points": 24,
                    "away_points": 21,
                    "periods": [
                        {
                            "period_type": "quarter",
                            "id": "df064ca2-e987-4523-8eac-585da60dcdb7",
                            "number": 1,
                            "sequence": 1,
                            "home_points": 7,
                            "away_points": 0
                        },
                        {
                            "period_type": "quarter",
                            "id": "5323093e-933a-4e71-a363-6e7d9f77a937",
                            "number": 2,
                            "sequence": 2,
                            "home_points": 7,
                            "away_points": 7
                        },
                        {
                            "period_type": "quarter",
                            "id": "1a2d056a-31aa-4119-aa9b-11ea73bdbe60",
                            "number": 3,
                            "sequence": 3,
                            "home_points": 10,
                            "away_points": 0
                        },
                        {
                            "period_type": "quarter",
                            "id": "6a8529b8-7c54-4d6d-9bdb-795612855e5b",
                            "number": 4,
                            "sequence": 4,
                            "home_points": 0,
                            "away_points": 14
                        }
                    ]
                }
            },*/