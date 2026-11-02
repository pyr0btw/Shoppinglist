package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppinglistService {

	public List<Shoppinglist> getAll() {
		return List.of(
				new Shoppinglist(1, "Wocheneinkauf", List.of(
						new ShoppinglistEntry(1, "Milch", 2, "Rewe", "1,5 % Fett", false),
						new ShoppinglistEntry(2, "Butter", 1, "Rewe", "", true),
						new ShoppinglistEntry(3, "Käse", 1, "Aldi", "Gouda, ca. 400 g", false),
						new ShoppinglistEntry(4, "Brot", 1, "Bäckerei", "Vollkorn", false),
						new ShoppinglistEntry(5, "Äpfel", 6, "", "", false))),
				new Shoppinglist(2, "Grillabend", List.of(
						new ShoppinglistEntry(6, "Würstchen", 2, "Aldi", "2 Packungen", false),
						new ShoppinglistEntry(7, "Grillkohle", 1, "Baumarkt", "", false),
						new ShoppinglistEntry(8, "Ketchup", 1, "", "", false))),
				new Shoppinglist(3, "Drogerie", List.of(
						new ShoppinglistEntry(9, "Zahnpasta", 1, "dm", "", false),
						new ShoppinglistEntry(10, "Duschgel", 2, "dm", "", false))));
	}

}
