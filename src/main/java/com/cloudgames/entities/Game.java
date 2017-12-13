package com.cloudgames.entities;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.GameInterface;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="games")
public class Game extends AbstractSportsRadarEntity implements GameInterface {

	private int number;
	private int attendance;
	private String weather;
	@Column(length = 15)
	private String status;
	
	private String scheduled;
	private int homeSpread;
	private int awaySpread;
	private double total;
	private int homeMoneyLine;
	private int awayMoneyLine;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="sport_id")
	private Sport sport;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "home_team_id")
	private Team homeTeam;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "away_team_id")
	private Team awayTeam;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "venue_id")
	private Venue venue;
	
	private int homeScore;
	
	private int awayScore;

	
	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public int getAttendance() {
		return attendance;
	}

	@Override
	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}

	@Override
	public String getWeather() {
		return weather;
	}

	@Override
	public void setWeather(String weather) {
		this.weather = weather;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getScheduled() {
		return scheduled;
	}

	@Override
	public void setScheduled(String scheduled) {
		this.scheduled = scheduled;
	}

	@Override
	public Sport getSport() {
		return sport;
	}

	@Override
	public void setSport(Sport sport) {
		this.sport = sport;
	}

	@Override
	public Team getHomeTeam() {
		return homeTeam;
	}

	@Override
	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	@Override
	public Team getAwayTeam() {
		return awayTeam;
	}

	@Override
	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}

	@Override
	public int getHomeScore() {
		return homeScore;
	}

	@Override
	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	@Override
	public int getAwayScore() {
		return awayScore;
	}

	@Override
	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	@Override
	public Venue getVenue() {
		return venue;
	}

	@Override
	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	@Override
	public int getHomeSpread() {
		return homeSpread;
	}

	@Override
	public void setHomeSpread(int homeSpread) {
		this.homeSpread = homeSpread;
	}

	@Override
	public int getAwaySpread() {
		return awaySpread;
	}

	@Override
	public void setAwaySpread(int awaySpread) {
		this.awaySpread = awaySpread;
	}

	@Override
	public double getTotal() {
		return total;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public int getHomeMoneyLine() {
		return homeMoneyLine;
	}

	@Override
	public void setHomeMoneyLine(int homeMoneyLine) {
		this.homeMoneyLine = homeMoneyLine;
	}

	@Override
	public int getAwayMoneyLine() {
		return awayMoneyLine;
	}

	@Override
	public void setAwayMoneyLine(int awayMoneyLine) {
		this.awayMoneyLine = awayMoneyLine;
	}
	
	

	
}
