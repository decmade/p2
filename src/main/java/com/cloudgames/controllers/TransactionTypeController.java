package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudgames.entities.TransactionType;
import com.cloudgames.entities.UserStatus;
import com.cloudgames.entities.interfaces.TransactionTypeInterface;
import com.cloudgames.entities.interfaces.UserStatusInterface;
import com.cloudgames.services.interfaces.TransactionTypeServiceInterface;
import com.cloudgames.services.interfaces.UserStatusServiceInterface;

@RestController("transactiontype-controller")
@RequestMapping("transactiontype")
public class TransactionTypeController extends AbstractController<TransactionTypeInterface, TransactionType> {

	@Autowired
	@Qualifier("transactiontype-service")
	private TransactionTypeServiceInterface service;
	
	@Override
	@GetMapping("{id:[0-9]+")
	public TransactionTypeInterface get(@PathVariable int id) {		
		log.debug("retreiving TransactionType with ID: " + id);
		
		return this.service.fetchById(id);
	}

	@Override
	@GetMapping
	public List<TransactionTypeInterface> getAll() {
		log.debug("retrieving all TransactionTypes");
		
		return this.service.fetchAll();
	}

	@Override
	@PostMapping
	public TransactionTypeInterface save(TransactionType type) {
		if ( type.getId() > 0 ) {
			log.debug("updating TransactionType with ID: " + type.getId() );
		} else {
			log.debug("adding new TransactionType");
		}
		
		return this.service.save(type);
	}

	@Override
	@DeleteMapping
	public void remove(TransactionType type) {		
		log.debug("removing TransactionType with ID: " + type.getId() );
		
		this.service.delete(type);		
	}
}
