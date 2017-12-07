package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.services.interfaces.UserServiceInterface;

@RestController("user-controller")
@RequestMapping("users")
public class UserController extends AbstractController<UserInterface, User> {

	@Autowired
	@Qualifier("user-service")
	private UserServiceInterface service;
	
	@Override
	@GetMapping("{id:[0-9]+}")
	public UserInterface get(@PathVariable int id) {
		String message = String.format("retrieving User with ID[%d]", id);
		
		log.debug(message);
		
		return this.service.fetchById(id);
	}

	@Override
	@GetMapping
	public List<UserInterface> getAll() {
		String message = "retrieving all Users";
		
		log.debug(message);
		
		return this.service.fetchAll();
	}

	@Override
	@PostMapping
	public UserInterface save(@RequestBody User user) {
		String message = "";
		
		if ( user.getId() > 0 ) {
			message = String.format("updating User with ID[%d]", user.getId() );
		} else {
			message = "adding new User";
		}
		
		log.debug(message);
		
		return this.service.save(user);
	}

	@Override
	@DeleteMapping
	public void remove(@RequestBody User user) {
		String message = String.format("removing User with ID[%d]", user.getId() );
		
		log.debug(message);
		
		this.service.delete(user);
	}
	
	

}
