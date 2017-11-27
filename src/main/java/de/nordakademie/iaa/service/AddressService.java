package de.nordakademie.iaa.service;

import java.util.List;
import java.util.stream.Collectors;

import de.nordakademie.iaa.model.repository.IllegalEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.nordakademie.iaa.model.Address;
import de.nordakademie.iaa.model.repository.IAddressRepository;
/**
 * Serviceklasse der Adressen.
 *
 *
 * @author Maik Voigt
 */

@Service
public class AddressService implements IAddressService {

	@Autowired	
	private IAddressRepository repository;

	/**
	 * Ausgabe einer Liste von Straßen, die den Filterkriterien entsprechen
	 *
	 * @param start Straße vom Typ String
	 * @return Gibt ein Array mit den Straßen vom Typ String zurück
	 */
	@Override
	@Transactional(readOnly=true)
	public List<String> getStreetsStartingWith(String start) {
		return repository.findMatchingAddresses(start)
							.stream()
							.map(address -> address.getStreet())
							.collect(Collectors.toList());
	}
	/**
	 * Ausgabe einer Liste von PLZs, die den Filterkriterien entsprechen
	 *
	 * @param street Straße vom Typ String
	 * @return Gibt ein Array mit den PLZs vom Typ Integer zurück
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Integer> getZipsForStreet(String street) {
		return repository.findMatchingAddresses(street)
							.stream()
							.map(address -> address.getZip())
							.collect(Collectors.toList());
	}
	/**
	 * Ausgabe einer Adresse, die den Filterkriterien Entspricht
	 *
	 * @param street Straße vom Typ String
	 * @param zip PLZ vom Typ Integer
	 * @return Gibt ein Objekt vom Typ Adress zurück, welches den Filterkriterien entspricht
	 */
	@Override
	@Transactional(readOnly=true)
	public Address findAddress(String street, int zip) {
		return repository.findAddress(street, zip);
	}
}
