package de.nordakademie.iaa.service;

import java.util.List;

import de.nordakademie.iaa.model.BankingDetails;

public interface IBankingDetailsService {
	
	List <String> getIbansStartingWith (String iban);
	
	BankingDetails findBankingDetails (String iban); 
}
