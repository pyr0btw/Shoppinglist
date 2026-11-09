package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppinglistRepository extends CrudRepository<Shoppinglist, Long> {

	// Eigene Listen und Listen, die mit der Person geteilt wurden
	@Query("select distinct l from Shoppinglist l left join l.mitglieder m where l.owner = :person or m = :person order by l.id")
	List<Shoppinglist> findSichtbareListen(String person);

	Optional<Shoppinglist> findByEinladungToken(String einladungToken);

}
