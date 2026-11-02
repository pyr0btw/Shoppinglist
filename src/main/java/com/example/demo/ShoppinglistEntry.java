package com.example.demo;

public record ShoppinglistEntry(int id, String name, int menge, String markt, String notiz, boolean erledigt) {

    public ShoppinglistEntry(String name) {
        this(0, name, 1, "", "", false);
    }
}
