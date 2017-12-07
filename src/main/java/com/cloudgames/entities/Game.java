package com.cloudgames.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cloudgames.entities.interfaces.GameInterface;

@Entity
@Table(name="Game")


public class Game extends AbstractEntity implements GameInterface {

	public String Score;
	public String Sport;
	public String Teams;
	
	
	
	public Game(String score, String sport, String teams) {
		super();
		Score = score;
		Sport = sport;
		Teams = teams;
	}
	@Override
	public String toString() {
		return "Game [Score=" + Score + ", Sport=" + Sport + ", Teams=" + Teams + "]";
	}
	public String getScore() {
		return Score;
	}
	public void setScore(String score) {
		Score = score;
	}
	public String getSport() {
		return Sport;
	}
	public void setSport(String sport) {
		Sport = sport;
	}
	public String getTeams() {
		return Teams;
	}
	public void setTeams(String teams) {
		Teams = teams;
	}
	
	
}
