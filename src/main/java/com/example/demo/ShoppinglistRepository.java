package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShoppinglistRepository extends CrudRepository<Shoppinglist, Long> {

	List<Shoppinglist> findByOwnerOrderByIdAsc(String owner);

}
