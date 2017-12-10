package com.cloudgames.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudgames.entities.Transaction;
import com.cloudgames.entities.interfaces.TransactionInterface;
import com.cloudgames.services.interfaces.TransactionServiceInterface;

@RestController("transaction-controller")
@RequestMapping("transactions")
public class TransactionController extends AbstractController<TransactionInterface, Transaction> {

	@Autowired
	@Qualifier("transaction-service")
	private TransactionServiceInterface service;

	@Override
	@PostMapping
	public TransactionInterface save(@RequestBody Transaction trans) {
		if ( trans.getId() > 0 ) {
			log.debug("updating transaction with ID: " + trans.getId() );
		} else {
			log.debug("adding new transaction");
		}
		
		return this.service.save(trans);
	}

	@Override
	@GetMapping("{id:[0-9]+}")
	public TransactionInterface get(@PathVariable int id) {
		log.debug("retrieving Transaction with ID: " + id);
		
		return this.service.fetchById(id);
	}

	@Override
	@GetMapping
	public List<TransactionInterface> getAll() {
		log.debug("retrieving all transactions");
		
		return this.service.fetchAll();
	}

	@Override
	@DeleteMapping
	public void remove(@RequestBody Transaction trans) {
		log.debug("removing transaction with ID: " + trans.getId());
		
		this.service.delete(trans);
	}
}
