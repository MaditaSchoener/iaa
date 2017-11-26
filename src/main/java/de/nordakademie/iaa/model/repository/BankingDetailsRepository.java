package de.nordakademie.iaa.model.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.nordakademie.iaa.model.BankingDetails;

/**
 * @author Maik Voigt
 *
 * Repository der Bankdetails. Beinhaltet Autocomplete Abfragen und Validierungen.
 */

@Repository
public class BankingDetailsRepository extends AbstractRepository<BankingDetails> implements IBankingDetailsRepository {

	private static final String BIC_PATTERN = "([a-zA-Z]{4}[a-zA-Z]{2}[a-zA-Z0-9]{2}([a-zA-Z0-9]{3})?)";
	private static final String IBAN_PATTERN = "DE[0-9]{2}( )?([0-9]{4}( )?){4}[0-9]{2}";
	
	public BankingDetailsRepository() {
		super(BankingDetails.class);
	}

	/**
	 * Untersucht, ob die IBAN bereits vorhanden ist
	 *
	 * @param iban IBAN als String
	 * @return Gibt die Banking Details zurück, falls die IBAN gefunden wurde
	 */
	@Override
	public BankingDetails findByIban(String iban) {
		TypedQuery<BankingDetails> query = entityManager()
				.createQuery("SELECT b FROM BankingDetails b WHERE b.iban = :iban", BankingDetails.class);
		query.setParameter("iban", iban);
		List<BankingDetails> details = query.getResultList();
		// There can only be one
		return details.isEmpty() ? null : details.get(0);
	}

	/**
	 * Erstellt neue Bankdaten. Wirft Exception falls sie ungültig sind.
	 *
	 * @param details BankDetails Objekt
	 * @throws IllegalEntityException Exception falls die Bankdaten nicht validiert werden konnten.
	 */
	@Override
	public BankingDetails create(BankingDetails details) throws IllegalEntityException {
		List <String> validationErrors = validate (details);
		if (!validationErrors.isEmpty()) {
			throw new IllegalEntityException(validationErrors);
		}
		return super.persist(details);
	}

	/**
	 * Prüft, ob eine IBAN mit dem Zeichenanfang vorhanden ist
	 *
	 * @param ibanStart String mit den ersten Zeichen der IBAN
	 * @return Gibt die Ergebnisliste zurück
	 */
	@Override
	public List<BankingDetails> findMatchingDetails(String ibanStart) {
		TypedQuery<BankingDetails> query = entityManager()
				.createQuery("Select b FROM BankingDetails b WHERE b.iban = :iban OR"
						+ " b.iban LIKE CONCAT(:iban, '%')", BankingDetails.class);

		query.setParameter("iban", ibanStart);
		return query.getResultList();
	}

	/**
	 * Validiert die Bankdaten
	 *
	 * @param bankingDetails Die Bankdaten vom Typ BankingDetails
	 * @return Gibt das Array der Fehlermeldungen zurück
	 */
	private List <String> validate (BankingDetails bankingDetails) {
		List <String> messages = new ArrayList<>();
		if (bankingDetails.getIban() == null || !bankingDetails.getIban().trim().matches(IBAN_PATTERN)) {
			messages.add("Ungültige IBAN. Bitte geben Sie eine gültige IBAN ein (DEXX XXXX XXXX XXXX XXXX XX)");
		}
		if (bankingDetails.getBIC() == null || !bankingDetails.getBIC().trim().matches(BIC_PATTERN)) {
			messages.add("Ungültiger BIC. Bitte geben Sie einen gültigen BIC");
		}
		return messages;
	}
}
