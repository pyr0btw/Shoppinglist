package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppinglistService {

	public List<Shoppinglist> getAll() {
		return List.of(
				new Shoppinglist(1, "Sommerfest", List.of(
						new ShoppinglistEntry(1, "Limonade", 6, "Rewe", "Zitrone", false),
						new ShoppinglistEntry(2, "Wassermelone", 1, "Aldi", "", true),
						new ShoppinglistEntry(3, "Nudelsalat", 2, "Rewe", "Mit Pesto", false),
						new ShoppinglistEntry(4, "Baguette", 3, "Bäckerei", "", false),
						new ShoppinglistEntry(5, "Pappteller", 1, "", "", false))),
				new Shoppinglist(2, "Frühstück", List.of(
						new ShoppinglistEntry(6, "Haferflocken", 1, "Aldi", "Kernig", false),
						new ShoppinglistEntry(7, "Honig", 1, "Wochenmarkt", "", false),
						new ShoppinglistEntry(8, "Kaffee", 2, "", "", false))),
				new Shoppinglist(3, "Haushalt", List.of(
						new ShoppinglistEntry(9, "Spülmittel", 1, "dm", "", false),
						new ShoppinglistEntry(10, "Küchenrolle", 2, "dm", "", false))));
	}

}
