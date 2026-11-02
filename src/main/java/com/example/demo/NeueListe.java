package com.example.demo;

import jakarta.validation.constraints.NotBlank;

// Request-Body für POST /shoppinglists, z. B. { "name": "Wocheneinkauf" }
public record NeueListe(@NotBlank String name) {
}
