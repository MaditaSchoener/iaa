package de.nordakademie.iaa.model.repository;

import java.util.List;

import de.nordakademie.iaa.model.BankingDetails;

public interface IBankingDetailsRepository {

	BankingDetails findByIban (String iban);
	
	List <BankingDetails> findMatchingDetails (String ibanStart);

	BankingDetails create (BankingDetails details) throws IllegalEntityException ;
}
