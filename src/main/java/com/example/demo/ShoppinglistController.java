package com.example.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "${FRONTEND_URL:http://localhost:5173}")
public class ShoppinglistController {

	private final ShoppinglistService service;

	public ShoppinglistController(ShoppinglistService service) {
		this.service = service;
	}

	@GetMapping("/shoppinglists")
	public List<Shoppinglist> getAllLists() {
		return service.getAll();
	}

}
