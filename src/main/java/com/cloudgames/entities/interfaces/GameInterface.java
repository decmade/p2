package com.cloudgames.entities.interfaces;

import java.time.LocalDateTime;

import com.cloudgames.entities.Sport;
import com.cloudgames.entities.Team;

public interface GameInterface extends SportsRadarEntityInterface {

	int getNumber();

	void setNumber(int number);

	int getAttendance();

	void setAttendance(int attendance);

	String getWeather();

	void setWeather(String weather);

	String getStatus();

	void setStatus(String status);

	LocalDateTime getScheduled();

	void setScheduled(LocalDateTime scheduled);

	Sport getSport();

	void setSport(Sport sport);

	Team getHomeTeam();

	void setHomeTeam(Team homeTeam);

	Team getAwayTeam();

	void setAwayTeam(Team awayTeam);

	int getHomeScore();

	void setHomeScore(int homeScore);

	int getAwayScore();

	void setAwayScore(int awayScore);

}
