package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

	@GetMapping("/")
	public List<ShoppinglistEntry> index() {
		return List.of(new ShoppinglistEntry("Butter"), new ShoppinglistEntry("Käse"), new ShoppinglistEntry("Milch"));
	}

}