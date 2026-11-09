package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ShoppinglistService {

	private final ShoppinglistRepository repository;
	private final ShoppinglistEntryRepository entryRepository;

	public ShoppinglistService(ShoppinglistRepository repository, ShoppinglistEntryRepository entryRepository) {
		this.repository = repository;
		this.entryRepository = entryRepository;
	}

	// Wie lange ein Einladungslink gilt
	private static final Duration EINLADUNG_GUELTIG = Duration.ofDays(7);

	// Eigene und mit der Person geteilte Listen
	public List<Shoppinglist> getAllFor(String person) {
		return repository.findSichtbareListen(person);
	}

	public Shoppinglist create(String name, String owner) {
		return repository.save(new Shoppinglist(name.trim(), owner));
	}

	@Transactional
	public Shoppinglist rename(Long listeId, String person, String name) {
		Shoppinglist liste = findeListeMitZugriff(listeId, person);
		liste.setName(name.trim()); // wird am Ende der Transaktion automatisch gespeichert
		return liste;
	}

	@Transactional
	public void delete(Long listeId, String owner) {
		repository.delete(findeEigeneListe(listeId, owner)); // löscht die Produkte mit (cascade)
	}

	@Transactional
	public ShoppinglistEntry addProdukt(Long listeId, String person, NeuesProdukt neu) {
		Shoppinglist liste = findeListeMitZugriff(listeId, person);
		ShoppinglistEntry produkt = new ShoppinglistEntry(neu.name().trim(), neu.menge(), ohneNull(neu.markt()),
				ohneNull(neu.notiz()), false);
		entryRepository.save(produkt); // vergibt die id
		liste.getProdukte().add(produkt); // verknüpft das Produkt beim Speichern mit der Liste
		return produkt;
	}

	@Transactional
	public ShoppinglistEntry setErledigt(Long listeId, Long produktId, String person, boolean erledigt) {
		ShoppinglistEntry produkt = findeProdukt(findeListeMitZugriff(listeId, person), produktId);
		produkt.setErledigt(erledigt); // wird am Ende der Transaktion automatisch gespeichert
		return produkt;
	}

	// Besitzer und Mitglieder dürfen Name, Menge, Markt und Notiz ändern; "erledigt" bleibt wie es ist
	@Transactional
	public ShoppinglistEntry updateProdukt(Long listeId, Long produktId, String person, NeuesProdukt geaendert) {
		ShoppinglistEntry produkt = findeProdukt(findeListeMitZugriff(listeId, person), produktId);
		produkt.bearbeiten(geaendert.name().trim(), geaendert.menge(), ohneNull(geaendert.markt()),
				ohneNull(geaendert.notiz())); // wird am Ende der Transaktion automatisch gespeichert
		return produkt;
	}

	@Transactional
	public void deleteProdukt(Long listeId, Long produktId, String person) {
		Shoppinglist liste = findeListeMitZugriff(listeId, person);
		liste.getProdukte().remove(findeProdukt(liste, produktId)); // orphanRemoval löscht die Zeile
	}

	// Nur der Besitzer darf einen neuen Link erzeugen; ein alter Link wird dadurch ungültig
	@Transactional
	public EinladungsLink einladungErstellen(Long listeId, String person) {
		Shoppinglist liste = findeEigeneListe(listeId, person);
		liste.setEinladung(UUID.randomUUID().toString(), Instant.now().plus(EINLADUNG_GUELTIG));
		return new EinladungsLink(liste.getEinladungToken(), liste.getEinladungGueltigBis());
	}

	// Liefert den aktuellen Link, falls es einen gültigen gibt
	@Transactional(readOnly = true)
	public EinladungsLink einladungLesen(Long listeId, String person) {
		Shoppinglist liste = findeEigeneListe(listeId, person);
		if (liste.getEinladungToken() == null || liste.getEinladungGueltigBis().isBefore(Instant.now())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kein gültiger Einladungslink");
		}
		return new EinladungsLink(liste.getEinladungToken(), liste.getEinladungGueltigBis());
	}

	@Transactional
	public void einladungWiderrufen(Long listeId, String person) {
		findeEigeneListe(listeId, person).setEinladung(null, null);
	}

	@Transactional(readOnly = true)
	public EinladungsInfo einladungAnsehen(String token, String person) {
		Shoppinglist liste = findeEinladung(token);
		return new EinladungsInfo(liste.getId(), liste.getName(), liste.getOwner(), liste.hatZugriff(person));
	}

	@Transactional
	public Shoppinglist einladungAnnehmen(String token, String person) {
		Shoppinglist liste = findeEinladung(token);
		if (!liste.istBesitzer(person)) {
			liste.getMitglieder().add(person);
		}
		return liste;
	}

	// Der Besitzer kann Mitglieder entfernen, ein Mitglied kann sich selbst entfernen (Liste verlassen)
	@Transactional
	public void mitgliedEntfernen(Long listeId, String mitglied, String person) {
		Shoppinglist liste = findeListeMitZugriff(listeId, person);
		if (!liste.istBesitzer(person) && !mitglied.equals(person)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nur der Besitzer kann andere entfernen");
		}
		if (!liste.getMitglieder().remove(mitglied)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kein Mitglied dieser Liste");
		}
	}

	private Shoppinglist findeEinladung(String token) {
		return repository.findByEinladungToken(token)
				.filter(liste -> liste.getEinladungGueltigBis().isAfter(Instant.now()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Einladung ungültig oder abgelaufen"));
	}

	// Besitzer und Mitglieder dürfen die Liste sehen und bearbeiten.
	// Fremde Listen behandeln wir wie nicht vorhandene (404), damit niemand fremde ids ausprobieren kann.
	private Shoppinglist findeListeMitZugriff(Long listeId, String person) {
		return repository.findById(listeId)
				.filter(liste -> liste.hatZugriff(person))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Liste nicht gefunden"));
	}

	// Löschen und Teilen darf nur der Besitzer
	private Shoppinglist findeEigeneListe(Long listeId, String person) {
		Shoppinglist liste = findeListeMitZugriff(listeId, person);
		if (!liste.istBesitzer(person)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nur der Besitzer darf das");
		}
		return liste;
	}

	private static String ohneNull(String text) {
		return text == null ? "" : text.trim();
	}

	private ShoppinglistEntry findeProdukt(Shoppinglist liste, Long produktId) {
		return liste.getProdukte().stream()
				.filter(produkt -> produkt.getId().equals(produktId))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produkt nicht gefunden"));
	}

}
