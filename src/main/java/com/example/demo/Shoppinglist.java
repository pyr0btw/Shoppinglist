package com.example.demo;

import java.util.List;

// owner ist die Okta-Benutzerkennung (E-Mail-Adresse) der Person, der die Liste gehört.
public record Shoppinglist(int id, String name, String owner, List<ShoppinglistEntry> produkte) {
}
