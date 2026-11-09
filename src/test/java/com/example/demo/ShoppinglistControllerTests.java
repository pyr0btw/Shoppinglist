package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppinglistControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void ohneTokenGibtEs401() throws Exception {
		mockMvc.perform(get("/shoppinglists"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void mitTokenKommenDieListenDerPerson() throws Exception {
		mockMvc.perform(post("/shoppinglists").with(jwt().jwt(token -> token.subject("timo@example.com")))
						.contentType("application/json").content("{\"name\": \"Wocheneinkauf\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.owner").value("timo@example.com"));

		mockMvc.perform(get("/shoppinglists").with(jwt().jwt(token -> token.subject("timo@example.com"))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Wocheneinkauf"))
				.andExpect(jsonPath("$[0].owner").value("timo@example.com"));

		// Andere Personen sehen die Liste nicht
		mockMvc.perform(get("/shoppinglists").with(jwt().jwt(token -> token.subject("anna@example.com"))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}

	@Test
	void listeOhneNameWirdAbgelehnt() throws Exception {
		mockMvc.perform(post("/shoppinglists").with(jwt().jwt(token -> token.subject("timo@example.com")))
						.contentType("application/json").content("{\"name\": \" \"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void startseiteBleibtOeffentlich() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk());
	}

	@Test
	void healthEndpunktIstOhneTokenErreichbar() throws Exception {
		mockMvc.perform(get("/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("ok"));
	}

	private static RequestPostProcessor als(String person) {
		return jwt().jwt(token -> token.subject(person));
	}

	private long idAus(String json) {
		return JsonMapper.builder().build().readTree(json).get("id").asLong();
	}

	@Test
	void produkteWerdenGespeichertAbgehaktUndGeloescht() throws Exception {
		String liste = mockMvc.perform(post("/shoppinglists").with(als("lena@example.com"))
						.contentType("application/json").content("{\"name\": \"Grillabend\"}"))
				.andReturn().getResponse().getContentAsString();
		long listeId = idAus(liste);

		String produkt = mockMvc.perform(post("/shoppinglists/" + listeId + "/produkte").with(als("lena@example.com"))
						.contentType("application/json")
						.content("{\"name\": \"Würstchen\", \"menge\": 2, \"markt\": \"Aldi\", \"notiz\": \"\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.erledigt").value(false))
				.andReturn().getResponse().getContentAsString();
		long produktId = idAus(produkt);

		mockMvc.perform(patch("/shoppinglists/" + listeId + "/produkte/" + produktId).with(als("lena@example.com"))
						.contentType("application/json").content("{\"erledigt\": true}"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/shoppinglists").with(als("lena@example.com")))
				.andExpect(jsonPath("$[0].produkte[0].name").value("Würstchen"))
				.andExpect(jsonPath("$[0].produkte[0].menge").value(2))
				.andExpect(jsonPath("$[0].produkte[0].erledigt").value(true));

		// Fremde Personen dürfen die Liste nicht verändern
		mockMvc.perform(delete("/shoppinglists/" + listeId + "/produkte/" + produktId).with(als("anna@example.com")))
				.andExpect(status().isNotFound());

		mockMvc.perform(delete("/shoppinglists/" + listeId + "/produkte/" + produktId).with(als("lena@example.com")))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/shoppinglists").with(als("lena@example.com")))
				.andExpect(jsonPath("$[0].produkte.length()").value(0));
	}

	@Test
	void listeWirdUmbenanntUndGeloescht() throws Exception {
		String liste = mockMvc.perform(post("/shoppinglists").with(als("max@example.com"))
						.contentType("application/json").content("{\"name\": \"Party\"}"))
				.andReturn().getResponse().getContentAsString();
		long listeId = idAus(liste);
		mockMvc.perform(post("/shoppinglists/" + listeId + "/produkte").with(als("max@example.com"))
						.contentType("application/json").content("{\"name\": \"Chips\", \"menge\": 3}"))
				.andExpect(status().isCreated());

		mockMvc.perform(put("/shoppinglists/" + listeId).with(als("max@example.com"))
						.contentType("application/json").content("{\"name\": \"Geburtstag\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Geburtstag"));

		// Fremde Personen dürfen weder umbenennen noch löschen
		mockMvc.perform(put("/shoppinglists/" + listeId).with(als("anna@example.com"))
						.contentType("application/json").content("{\"name\": \"Gehackt\"}"))
				.andExpect(status().isNotFound());
		mockMvc.perform(delete("/shoppinglists/" + listeId).with(als("anna@example.com")))
				.andExpect(status().isNotFound());

		mockMvc.perform(get("/shoppinglists").with(als("max@example.com")))
				.andExpect(jsonPath("$[0].name").value("Geburtstag"));

		mockMvc.perform(delete("/shoppinglists/" + listeId).with(als("max@example.com")))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/shoppinglists").with(als("max@example.com")))
				.andExpect(jsonPath("$.length()").value(0));
	}

	private String feld(String json, String name) {
		return JsonMapper.builder().build().readTree(json).get(name).asString();
	}

	@Test
	void listeWirdPerEinladungslinkGeteilt() throws Exception {
		String liste = mockMvc.perform(post("/shoppinglists").with(als("tom@example.com"))
						.contentType("application/json").content("{\"name\": \"WG-Einkauf\"}"))
				.andReturn().getResponse().getContentAsString();
		long listeId = idAus(liste);
		mockMvc.perform(post("/shoppinglists/" + listeId + "/produkte").with(als("tom@example.com"))
				.contentType("application/json").content("{\"name\": \"Milch\", \"menge\": 1}"));

		// Nur der Besitzer darf einen Link erzeugen
		String link = mockMvc.perform(post("/shoppinglists/" + listeId + "/einladung").with(als("tom@example.com")))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		String token = feld(link, "token");
		mockMvc.perform(get("/shoppinglists/" + listeId + "/einladung").with(als("tom@example.com")))
				.andExpect(jsonPath("$.token").value(token));

		// Der Token steht nicht im normalen Listen-JSON
		mockMvc.perform(get("/shoppinglists").with(als("tom@example.com")))
				.andExpect(jsonPath("$[0].einladungToken").doesNotExist());

		// Vorschau und Beitreten
		mockMvc.perform(get("/einladungen/" + token).with(als("pia@example.com")))
				.andExpect(jsonPath("$.listeName").value("WG-Einkauf"))
				.andExpect(jsonPath("$.owner").value("tom@example.com"))
				.andExpect(jsonPath("$.bereitsMitglied").value(false));
		mockMvc.perform(post("/einladungen/" + token + "/annehmen").with(als("pia@example.com")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.mitglieder[0]").value("pia@example.com"));

		// Pia sieht die Liste genau einmal, mit dem einen Produkt, und kann mitbearbeiten
		mockMvc.perform(get("/shoppinglists").with(als("pia@example.com")))
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].produkte.length()").value(1));
		mockMvc.perform(post("/shoppinglists/" + listeId + "/produkte").with(als("pia@example.com"))
						.contentType("application/json").content("{\"name\": \"Brot\", \"menge\": 1}"))
				.andExpect(status().isCreated());
		mockMvc.perform(get("/shoppinglists").with(als("tom@example.com")))
				.andExpect(jsonPath("$[0].produkte.length()").value(2));

		// Löschen und Teilen bleiben dem Besitzer vorbehalten
		mockMvc.perform(delete("/shoppinglists/" + listeId).with(als("pia@example.com")))
				.andExpect(status().isForbidden());
		mockMvc.perform(post("/shoppinglists/" + listeId + "/einladung").with(als("pia@example.com")))
				.andExpect(status().isForbidden());

		// Nach dem Widerrufen funktioniert der Link nicht mehr
		mockMvc.perform(delete("/shoppinglists/" + listeId + "/einladung").with(als("tom@example.com")))
				.andExpect(status().isNoContent());
		mockMvc.perform(post("/einladungen/" + token + "/annehmen").with(als("ben@example.com")))
				.andExpect(status().isNotFound());

		// Pia verlässt die Liste und sieht sie danach nicht mehr
		mockMvc.perform(delete("/shoppinglists/" + listeId + "/mitglieder/pia@example.com").with(als("pia@example.com")))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/shoppinglists").with(als("pia@example.com")))
				.andExpect(jsonPath("$.length()").value(0));
	}

}
