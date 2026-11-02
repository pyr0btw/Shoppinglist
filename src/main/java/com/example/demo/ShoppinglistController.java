package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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

	// Legt eine neue Liste in der Datenbank an, Body z. B. { "name": "Wocheneinkauf" }
	@PostMapping("/shoppinglists")
	@ResponseStatus(HttpStatus.CREATED)
	public Shoppinglist createList(@Valid @RequestBody NeueListe neueListe, @AuthenticationPrincipal Jwt jwt) {
		return service.create(neueListe.name(), jwt.getSubject());
	}

}
