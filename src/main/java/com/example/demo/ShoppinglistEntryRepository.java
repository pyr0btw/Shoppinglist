package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface ShoppinglistEntryRepository extends CrudRepository<ShoppinglistEntry, Long> {
}
