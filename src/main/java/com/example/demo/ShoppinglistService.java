package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppinglistService {

	private final ShoppinglistRepository repository;

	public ShoppinglistService(ShoppinglistRepository repository) {
		this.repository = repository;
	}

	// Nur die Listen der angemeldeten Person
	public List<Shoppinglist> getAllFor(String owner) {
		return repository.findByOwnerOrderByIdAsc(owner);
	}

	public Shoppinglist create(String name, String owner) {
		return repository.save(new Shoppinglist(name.trim(), owner));
	}

}
