package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ShoppinglistService {

	private final ShoppinglistRepository repository;
	private final ShoppinglistEntryRepository entryRepository;

	public ShoppinglistService(ShoppinglistRepository repository, ShoppinglistEntryRepository entryRepository) {
		this.repository = repository;
		this.entryRepository = entryRepository;
	}

	// Nur die Listen der angemeldeten Person
	public List<Shoppinglist> getAllFor(String owner) {
		return repository.findByOwnerOrderByIdAsc(owner);
	}

	public Shoppinglist create(String name, String owner) {
		return repository.save(new Shoppinglist(name.trim(), owner));
	}

	@Transactional
	public ShoppinglistEntry addProdukt(Long listeId, String owner, NeuesProdukt neu) {
		Shoppinglist liste = findeEigeneListe(listeId, owner);
		ShoppinglistEntry produkt = new ShoppinglistEntry(neu.name().trim(), neu.menge(),
				neu.markt() == null ? "" : neu.markt().trim(), neu.notiz() == null ? "" : neu.notiz().trim(), false);
		entryRepository.save(produkt); // vergibt die id
		liste.getProdukte().add(produkt); // verknüpft das Produkt beim Speichern mit der Liste
		return produkt;
	}

	@Transactional
	public ShoppinglistEntry setErledigt(Long listeId, Long produktId, String owner, boolean erledigt) {
		ShoppinglistEntry produkt = findeProdukt(findeEigeneListe(listeId, owner), produktId);
		produkt.setErledigt(erledigt); // wird am Ende der Transaktion automatisch gespeichert
		return produkt;
	}

	@Transactional
	public void deleteProdukt(Long listeId, Long produktId, String owner) {
		Shoppinglist liste = findeEigeneListe(listeId, owner);
		liste.getProdukte().remove(findeProdukt(liste, produktId)); // orphanRemoval löscht die Zeile
	}

	// Fremde Listen behandeln wir wie nicht vorhandene (404), damit niemand fremde ids ausprobieren kann
	private Shoppinglist findeEigeneListe(Long listeId, String owner) {
		return repository.findById(listeId)
				.filter(liste -> liste.getOwner().equals(owner))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Liste nicht gefunden"));
	}

	private ShoppinglistEntry findeProdukt(Shoppinglist liste, Long produktId) {
		return liste.getProdukte().stream()
				.filter(produkt -> produkt.getId().equals(produktId))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produkt nicht gefunden"));
	}

}
