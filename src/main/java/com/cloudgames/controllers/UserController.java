package com.cloudgames.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloudgames.entities.interfaces.UserInterface;

@RestController
@RequestMapping("users")
public class UserController {
	
	@GetMapping
	public List<UserInterface> getAll() {
		// TODO: implement
		//stub
	}

}
