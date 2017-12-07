package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.UserInterface;
import com.cloudgames.services.interfaces.ServiceInterface;

@RestController
@RequestMapping("users")
public class UserController extends AbstractController<UserInterface, User> {

	@Autowired
	@Qualifier("user-service")
	private ServiceInterface<UserInterface> service;
	
	@Override
	@GetMapping("{id}")
	public UserInterface get(@PathVariable int id) {
		String message = String.format("retrieving user with ID[%d]", id);
		
		log.debug(message);
		
		return service.fetchById(id);
	}

	@Override
	@GetMapping
	public List<UserInterface> getAll() {
		String message = "retrieving all users";
		
		log.debug(message);
		
		return service.fetchAll();
	}

	@Override
	@PostMapping
	public UserInterface save(@RequestBody User user) {
		String message = "saving user";
		
		log.debug(message);
		
		return service.save(user);
	}

	@Override
	@DeleteMapping
	public void remove(@RequestBody User user) {
		String message = String.format("removing user with ID[%d]", user.getId() );
		
		log.debug(message);
		
		service.delete(user);
	}
	
	

}
