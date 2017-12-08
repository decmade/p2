package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudgames.entities.Game;
import com.cloudgames.services.GameService;



@Controller
@RequestMapping("Games")
public class GameController {

	@Autowired
	private GameService gs;
	
	@GetMapping
	@ResponseBody
	public List<Game> getAllGames() {
		return gs.findAll();
	}
}
	
