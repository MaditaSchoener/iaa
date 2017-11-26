package de.nordakademie.iaa.model.repository;

import java.util.List;

import de.nordakademie.iaa.model.Address;

/**
 * Interface des Repository der Adressen
 *
 * @author Maik Voigt
 */

public interface IAddressRepository {

	Address create (Address address) throws IllegalEntityException;
	
	List<Address> findMatchingAddresses (String street);
	
	Address findAddress (String street, int zip);
}
