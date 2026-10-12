// Beispiel-Entitäten für M2. Ab M3 kommen diese Daten vom Spring-Backend (GET-Route).
export const sampleData = [
  {
    id: 1,
    name: 'Wocheneinkauf',
    produkte: [
      { id: 1, name: 'Milch', menge: 2, markt: 'Rewe', notiz: '1,5 % Fett', erledigt: false },
      { id: 2, name: 'Butter', menge: 1, markt: 'Rewe', notiz: '', erledigt: true },
      { id: 3, name: 'Käse', menge: 1, markt: 'Aldi', notiz: 'Gouda, ca. 400 g', erledigt: false },
      { id: 4, name: 'Brot', menge: 1, markt: 'Bäckerei', notiz: 'Vollkorn', erledigt: false },
      { id: 5, name: 'Äpfel', menge: 6, markt: '', notiz: '', erledigt: false },
    ],
  },
  {
    id: 2,
    name: 'Grillabend',
    produkte: [
      { id: 6, name: 'Würstchen', menge: 2, markt: 'Aldi', notiz: '2 Packungen', erledigt: false },
      { id: 7, name: 'Grillkohle', menge: 1, markt: 'Baumarkt', notiz: '', erledigt: false },
      { id: 8, name: 'Ketchup', menge: 1, markt: '', notiz: '', erledigt: false },
    ],
  },
  {
    id: 3,
    name: 'Drogerie',
    produkte: [
      { id: 9, name: 'Zahnpasta', menge: 1, markt: 'dm', notiz: '', erledigt: false },
      { id: 10, name: 'Duschgel', menge: 2, markt: 'dm', notiz: '', erledigt: false },
    ],
  },
]
