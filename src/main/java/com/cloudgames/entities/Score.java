package com.cloudgames.entities;

import com.cloudgames.entities.interfaces.GameInterface;
import com.cloudgames.entities.interfaces.ScoreInterface;

public class Score extends AbstractEntity implements ScoreInterface {

	private int id;
	private int points;
	private Game game;
	private Team team;
	
	
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Game Game() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public Team Team() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	@Override
	public String getGame() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setGame(String games) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setTeam(String teams) {
		// TODO Auto-generated method stub
		

}
}
	