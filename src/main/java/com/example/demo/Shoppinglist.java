package com.example.demo;

import java.util.List;

public record Shoppinglist(int id, String name, List<ShoppinglistEntry> produkte) {
}
