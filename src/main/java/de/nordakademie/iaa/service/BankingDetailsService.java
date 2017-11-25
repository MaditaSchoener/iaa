package de.nordakademie.iaa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.nordakademie.iaa.model.BankingDetails;
import de.nordakademie.iaa.model.repository.IBankingDetailsRepository;

@Service
public class BankingDetailsService implements IBankingDetailsService {

	@Autowired
	private IBankingDetailsRepository repository;
	
	@Override
	@Transactional(readOnly=true)
	public List<String> getIbansStartingWith(String iban) {
		return repository.findMatchingDetails(iban).stream().map(bd -> bd.getIban()).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly=true)
	public BankingDetails findBankingDetails(String iban) {
		return repository.findByIban(iban);
	}

}
