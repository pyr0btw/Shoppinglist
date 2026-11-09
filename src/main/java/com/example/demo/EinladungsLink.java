package com.example.demo;

import java.time.Instant;

// Antwort auf POST /shoppinglists/{id}/einladung; das Frontend baut daraus den Link .../einladung/{token}
public record EinladungsLink(String token, Instant gueltigBis) {
}
