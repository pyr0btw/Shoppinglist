# Frontend (Vue.js)

Vue-3-App mit Vite und Vue Router für die Einkaufsliste (Milestone 2).

## Starten

```sh
npm install
npm run dev
```

Danach im Browser http://localhost:5173 öffnen.

## Aufbau

| Datei | Inhalt |
| --- | --- |
| `src/router/index.js` | Routen der Seiten |
| `src/views/HomeView.vue` | Hauptmenü mit „Einkaufslisten“ und „Märkte“ |
| `src/views/ListenView.vue` | Listen anlegen, umbenennen, löschen |
| `src/views/ListeDetailView.vue` | Produkte einer Liste, Button „+ Produkt“ |
| `src/components/ProduktDialog.vue` | Pop-up zum Hinzufügen (Name, Menge, Markt, Notiz) |
| `src/views/MaerkteView.vue` | Auswahl einer Liste für die Markt-Ansicht |
| `src/views/MarktAnsichtView.vue` | Produkte nach Markt gruppiert, „Ohne Markt“ am Ende |
| `src/data/sampleData.js` | Beispiel-Entitäten (werden mit `v-for` gerendert) |
| `src/data/store.js` | Gemeinsamer Zustand und Hilfsfunktionen |

Änderungen bleiben bis M4 nur im Browser und sind nach einem Neuladen weg.
Ab M3 werden die Beispieldaten durch einen `fetch` auf die GET-Route des Backends ersetzt.
