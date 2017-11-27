package de.nordakademie.iaa.service;

import java.util.List;

import de.nordakademie.iaa.model.BankingDetails;
/**
 * Interface des Services der Bankverbindungsdaten
 *
 * @author Maik Voigt
 */
public interface IBankingDetailsService {
	
	List <String> getIbansStartingWith (String iban);
	
	BankingDetails findBankingDetails (String iban); 
}
