package com.cloudgames.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.cloudgames.entities.UserStatus;
import com.cloudgames.entities.interfaces.UserStatusInterface;
import com.cloudgames.services.interfaces.UserStatusServiceInterface;

@RestController
@RequestMapping("userstatus")
public class UserStatusController extends AbstractController<UserStatusInterface, UserStatus> {

	@Autowired
	@Qualifier("userstatus-service")
	private UserStatusServiceInterface service;
	
	@Override
	@GetMapping("{id:[0-9]+")
	public UserStatusInterface get(@PathVariable int id) {
		String message = String.format("retreiving UserStatus with ID[%d]",  id);
		
		log.debug(message);
		
		return this.service.fetchById(id);
	}

	@Override
	public List<UserStatusInterface> getAll() {
		String message = "retrieving all UserStatuses";
		
		log.debug(message);
		
		return this.service.fetchAll();
	}

	@Override
	public UserStatusInterface save(UserStatus status) {
		String message = "";
		
		if ( status.getId() > 0 ) {
			message = String.format("updating UserStatus with ID[%d]", status.getId() );
		} else {
			message = "adding new UserStatus";
		}
		
		log.debug(message);
		
		return this.service.save(status);
	}

	@Override
	public void remove(UserStatus status) {
		String message = String.format("removing UserStatus with ID[%d]", status.getId() );
		
		log.debug(message);
		
		this.service.delete(status);		
	}
	

}
