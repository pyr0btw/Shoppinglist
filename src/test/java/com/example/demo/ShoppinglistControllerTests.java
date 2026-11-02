package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
		mockMvc.perform(get("/shoppinglists").with(jwt().jwt(token -> token.subject("timo@example.com"))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].owner").value("timo@example.com"));
	}

	@Test
	void startseiteBleibtOeffentlich() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk());
	}

}
