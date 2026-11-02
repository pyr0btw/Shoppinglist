package com.example.demo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Shoppinglist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	// Okta-Benutzerkennung (E-Mail-Adresse) der Person, der die Liste gehört
	private String owner;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "shoppinglist_id")
	private List<ShoppinglistEntry> produkte = new ArrayList<>();

	protected Shoppinglist() {
	}

	public Shoppinglist(String name, String owner) {
		this.name = name;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public List<ShoppinglistEntry> getProdukte() {
		return produkte;
	}
}
