package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.cloudgames.entities.UserRole;
import com.cloudgames.entities.interfaces.UserRoleInterface;
import com.cloudgames.services.interfaces.UserRoleServiceInterface;

@RestController("userrole-controller")
@RequestMapping("userroles")
public class UserRoleController extends AbstractController<UserRoleInterface, UserRole> {

	@Autowired
	@Qualifier("userrole-service")
	UserRoleServiceInterface service;
	
	@Override
	@GetMapping("{id:[0-9]+")
	public UserRoleInterface get(@PathVariable int id) {
		String message = String.format("retrieving UserRole with ID[%d]", id);
		
		log.debug(message);
		
		return this.service.fetchById(id);
	}

	@Override
	@GetMapping
	public List<UserRoleInterface> getAll() {
		String message = "retrieving all UserRoles";
		
		log.debug(message);
		
		return this.service.fetchAll();
	}

	@Override
	@PostMapping
	public UserRoleInterface save(@RequestBody UserRole role) {
		String message = "";
		
		if ( role.getId() > 0 ) {
			message = String.format("updating UserRole with ID:[%d]", role.getId() );
		} else {
			message = "adding new UserRole";
		}
		
		log.debug(message);
		
		return this.service.save(role);
	}

	@Override
	@DeleteMapping
	public void remove(@RequestBody UserRole role) {
		String message = String.format("removing UserRole with ID[%d]", role.getId() );
		
		log.debug(message);
		
		this.service.delete(role);		
	}

}
