package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	// Umbenennen, Body wie beim Anlegen: { "name": "Neuer Name" }
	@PutMapping("/shoppinglists/{id}")
	public Shoppinglist renameList(@PathVariable Long id, @Valid @RequestBody NeueListe liste, @AuthenticationPrincipal Jwt jwt) {
		return service.rename(id, jwt.getSubject(), liste.name());
	}

	@DeleteMapping("/shoppinglists/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteList(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
		service.delete(id, jwt.getSubject());
	}

	@PostMapping("/shoppinglists/{id}/produkte")
	@ResponseStatus(HttpStatus.CREATED)
	public ShoppinglistEntry addProdukt(@PathVariable Long id, @Valid @RequestBody NeuesProdukt neuesProdukt,
			@AuthenticationPrincipal Jwt jwt) {
		return service.addProdukt(id, jwt.getSubject(), neuesProdukt);
	}

	@PatchMapping("/shoppinglists/{id}/produkte/{produktId}")
	public ShoppinglistEntry setErledigt(@PathVariable Long id, @PathVariable Long produktId,
			@RequestBody ProduktStatus status, @AuthenticationPrincipal Jwt jwt) {
		return service.setErledigt(id, produktId, jwt.getSubject(), status.erledigt());
	}

	@DeleteMapping("/shoppinglists/{id}/produkte/{produktId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProdukt(@PathVariable Long id, @PathVariable Long produktId, @AuthenticationPrincipal Jwt jwt) {
		service.deleteProdukt(id, produktId, jwt.getSubject());
	}

}
