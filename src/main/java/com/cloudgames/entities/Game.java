package com.cloudgames.entities;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.cloudgames.entities.interfaces.GameInterface;

@Entity
@Table(name="games")
public class Game extends AbstractSportsRadarEntity implements GameInterface {

	private int number;
	private int attendance;
	private String weather;
	@Column(length = 15)
	private String status;
	private LocalDateTime scheduled;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name="sport_id")
	private Sport sport;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "home_team_id")
	private Team homeTeam;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "away_team_id")
	private Team awayTeam;
	
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
	public LocalDateTime getScheduled() {
		return scheduled;
	}

	@Override
	public void setScheduled(LocalDateTime scheduled) {
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

	
}
