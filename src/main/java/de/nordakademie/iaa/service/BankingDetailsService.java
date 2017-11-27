package de.nordakademie.iaa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.nordakademie.iaa.model.BankingDetails;
import de.nordakademie.iaa.model.repository.IBankingDetailsRepository;

/**
 * Service Klasse für die Bankdetails
 *
 * @author Maik Voigt
 */
@Service
public class BankingDetailsService implements IBankingDetailsService {

	@Autowired
	private IBankingDetailsRepository repository;

	/**
	 * Ruft eine Liste auf mit IBANs, die mit den gleichen Zeichen beginnen
	 * @param iban IBAN Zeichen als String
	 * @return Gibt eine Liste mit passenden IBANs zurück
	 */
	@Override
	@Transactional(readOnly=true)
	public List<String> getIbansStartingWith(String iban) {
		return repository.findMatchingDetails(iban).stream().map(bd -> bd.getIban()).collect(Collectors.toList());
	}

	/**
	 * Ruft die Bankdetails einer IBAN auf und gibt sie zurück
	 * @param iban IABN als String
	 * @return Gibt eine Ergebnisliste zurück
	 */
	@Override
	@Transactional(readOnly=true)
	public BankingDetails findBankingDetails(String iban) {
		return repository.findByIban(iban);
	}

}
