package com.example.demo;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShoppinglistController {

	private final ShoppinglistService service;

	public ShoppinglistController(ShoppinglistService service) {
		this.service = service;
	}

	// Der Besitzer kommt aus dem geprüften Token, nicht aus einem Query-Parameter.
	// So kann niemand mit "?owner=..." die Listen anderer Personen abrufen.
	@GetMapping("/shoppinglists")
	public List<Shoppinglist> getAllLists(@AuthenticationPrincipal Jwt jwt) {
		return service.getAllFor(jwt.getSubject());
	}

}
