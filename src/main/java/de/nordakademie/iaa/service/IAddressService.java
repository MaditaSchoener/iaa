package de.nordakademie.iaa.service;

import java.util.List;
/**
 * Interface des Services der Adressen
 *
 * @author Maik Voigt
 */

import de.nordakademie.iaa.model.Address;

public interface IAddressService {
	
	List <String> getStreetsStartingWith (String start);
	
	List <Integer> getZipsForStreet (String street);
	
	Address findAddress (String street, int zip); 
}
