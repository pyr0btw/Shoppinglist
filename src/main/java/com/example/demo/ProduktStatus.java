package com.example.demo;

// Request-Body für PATCH /shoppinglists/{id}/produkte/{produktId}, z. B. { "erledigt": true }
public record ProduktStatus(boolean erledigt) {
}
