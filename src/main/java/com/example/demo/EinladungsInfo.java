package com.example.demo;

// Vorschau für die Einladungsseite: "owner lädt dich zur Liste listeName ein"
public record EinladungsInfo(Long listeId, String listeName, String owner, boolean bereitsMitglied) {
}
