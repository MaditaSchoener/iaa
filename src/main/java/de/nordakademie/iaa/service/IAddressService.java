package de.nordakademie.iaa.service;

import java.util.List;

import de.nordakademie.iaa.model.Address;

/**
 * Interface des Services der Adressen
 *
 * @author Maik Voigt
 */
public interface IAddressService {
	
	List <String> getStreetsStartingWith (String start);
	
	List <Integer> getZipsForStreet (String street);
	
	Address findAddress (String street, int zip); 
}
