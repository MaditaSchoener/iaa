package de.nordakademie.iaa.model.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.nordakademie.iaa.model.BankingDetails;

@Repository
public class BankingDetailsRepository extends AbstractRepository<BankingDetails> implements IBankingDetailsRepository {

	private static final String BIC_PATTERN = "([a-zA-Z]{4}[a-zA-Z]{2}[a-zA-Z0-9]{2}([a-zA-Z0-9]{3})?)";
	private static final String IBAN_PATTERN = "DE[0-9]{2}( )?([0-9]{4}( )?){4}[0-9]{2}";
	
	public BankingDetailsRepository() {
		super(BankingDetails.class);
	}

	@Override
	public BankingDetails findByIban(String iban) {
		TypedQuery<BankingDetails> query = entityManager()
				.createQuery("SELECT b FROM BankingDetails b WHERE b.iban = :iban", BankingDetails.class);
		query.setParameter("iban", iban);
		List<BankingDetails> details = query.getResultList();
		// There can only be one
		return details.isEmpty() ? null : details.get(0);
	}

	@Override
	public BankingDetails create(BankingDetails details) throws IllegalEntityException {
		List <String> validationErrors = validate (details);
		if (!validationErrors.isEmpty()) {
			throw new IllegalEntityException(validationErrors);
		}
		return super.persist(details);
	}

	@Override
	public List<BankingDetails> findMatchingDetails(String ibanStart) {
		TypedQuery<BankingDetails> query = entityManager()
				.createQuery("Select b FROM BankingDetails b WHERE b.iban = :iban OR"
						+ " b.iban LIKE CONCAT(:iban, '%')", BankingDetails.class);

		query.setParameter("iban", ibanStart);
		return query.getResultList();
	}
	
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
