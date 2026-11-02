package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ShoppinglistEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private int menge;
	private String markt;
	private String notiz;
	private boolean erledigt;

	protected ShoppinglistEntry() {
	}

	public ShoppinglistEntry(String name) {
		this(name, 1, "", "", false);
	}

	public ShoppinglistEntry(String name, int menge, String markt, String notiz, boolean erledigt) {
		this.name = name;
		this.menge = menge;
		this.markt = markt;
		this.notiz = notiz;
		this.erledigt = erledigt;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getMenge() {
		return menge;
	}

	public String getMarkt() {
		return markt;
	}

	public String getNotiz() {
		return notiz;
	}

	public boolean isErledigt() {
		return erledigt;
	}

	public void setErledigt(boolean erledigt) {
		this.erledigt = erledigt;
	}
}
