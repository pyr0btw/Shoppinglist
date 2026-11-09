package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	@OrderBy("id")
	@Fetch(FetchMode.SELECT)
	private List<ShoppinglistEntry> produkte = new ArrayList<>();

	// Okta-Kennungen der Personen, mit denen die Liste geteilt ist (ohne den Besitzer)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "shoppinglist_mitglieder", joinColumns = @JoinColumn(name = "shoppinglist_id"))
	@Column(name = "person")
	@Fetch(FetchMode.SELECT)
	private Set<String> mitglieder = new HashSet<>();

	// Aktueller Einladungslink; nur der Besitzer bekommt ihn zu sehen, deshalb nicht im JSON
	@JsonIgnore
	private String einladungToken;

	@JsonIgnore
	private Instant einladungGueltigBis;

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

	public Set<String> getMitglieder() {
		return mitglieder;
	}

	public boolean istBesitzer(String person) {
		return owner.equals(person);
	}

	public boolean hatZugriff(String person) {
		return istBesitzer(person) || mitglieder.contains(person);
	}

	public String getEinladungToken() {
		return einladungToken;
	}

	public Instant getEinladungGueltigBis() {
		return einladungGueltigBis;
	}

	public void setEinladung(String token, Instant gueltigBis) {
		this.einladungToken = token;
		this.einladungGueltigBis = gueltigBis;
	}
}
