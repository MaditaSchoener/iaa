package de.nordakademie.iaa.model.repository;

import de.nordakademie.iaa.model.BankingDetails;

public interface IBankingDetailsRepository {

	BankingDetails findByIban (String iban);

	BankingDetails create (BankingDetails details);
}
