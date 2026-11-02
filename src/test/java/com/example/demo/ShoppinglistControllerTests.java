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

}
