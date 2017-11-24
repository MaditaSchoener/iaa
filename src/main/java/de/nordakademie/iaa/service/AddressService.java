package de.nordakademie.iaa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.nordakademie.iaa.model.Address;
import de.nordakademie.iaa.model.repository.IAddressRepository;

@Service
public class AddressService implements IAddressService {

	@Autowired	
	private IAddressRepository repository;
	
	@Override
	@Transactional(readOnly=true)
	public List<String> getStreetsStartingWith(String start) {
		return repository.findMatchingAddresses(start)
							.stream()
							.map(address -> address.getStreet())
							.collect(Collectors.toList());
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Integer> getZipsForStreet(String street) {
		return repository.findMatchingAddresses(street)
							.stream()
							.map(address -> address.getZip())
							.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly=true)
	public Address findAddress(String street, int zip) {
		return repository.findAddress(street, zip);
	}
}
