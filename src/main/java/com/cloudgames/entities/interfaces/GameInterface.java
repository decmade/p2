package com.cloudgames.entities.interfaces;


import com.cloudgames.entities.Sport;
import com.cloudgames.entities.Team;
import com.cloudgames.entities.Venue;

public interface GameInterface extends SportsRadarEntityInterface {

	int getNumber();

	void setNumber(int number);

	int getAttendance();

	void setAttendance(int attendance);

	String getWeather();

	void setWeather(String weather);

	String getStatus();

	void setStatus(String status);

	String getScheduled();

	void setScheduled(String scheduled);

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

	Venue getVenue();

	void setVenue(Venue venue);

	int getHomeSpread();

	void setHomeSpread(int homeSpread);

	int getAwaySpread();

	void setAwaySpread(int awaySpread);

	double getTotal();

	void setTotal(double total);

	int getHomeMoneyLine();

	void setHomeMoneyLine(int homeMoneyLine);

	int getAwayMoneyLine();

	void setAwayMoneyLine(int awayMoneyLine);

}
