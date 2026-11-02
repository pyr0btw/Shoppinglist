package com.example.demo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

// Request-Body für POST /shoppinglists/{id}/produkte
public record NeuesProdukt(@NotBlank String name, @Min(1) int menge, String markt, String notiz) {
}
