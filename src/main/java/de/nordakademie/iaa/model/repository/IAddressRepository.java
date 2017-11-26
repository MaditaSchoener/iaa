package de.nordakademie.iaa.model.repository;

import java.util.List;

import de.nordakademie.iaa.model.Address;

/**
 * @author Maik Voigt
 *
 * Interface des Repository der Adressen
 */

public interface IAddressRepository {

	Address create (Address address) throws IllegalEntityException;
	
	List<Address> findMatchingAddresses (String street);
	
	Address findAddress (String street, int zip);
}
