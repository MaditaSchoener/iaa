package de.nordakademie.iaa.service.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.nordakademie.iaa.model.Address;
import de.nordakademie.iaa.service.IAddressService;

/**
 * REST-Services f�r das Adressenobjekt, welche vom Frontend aufgerufen werden k�nnen.
 *
 * @author Maik Voigt
 */
@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private IAddressService addressService;

	@GetMapping(path="/streets")
	public List <String> streetsStartingWith(@RequestParam(name="start", defaultValue="") String start) {
		return addressService.getStreetsStartingWith(start);
	}

	/**
	 * Ruft die zu einer Adresse zugeh�rigen PLZ auf
	 * @param street Stra�e als String
	 * @return Gibt die Ergebnisliste wieder
	 */
	@GetMapping(path="/zips")
	public List <Integer> zipsForStreet(@RequestParam(name="street", required=true) String street) {
		return addressService.getZipsForStreet(street);
	}

	/**
	 * Ruft das zu einer Stra�e und PLZ geh�rige Adressenelement auf
	 * @param street Stra�e als String
	 * @param zip PLZ als Integer
	 * @return Gibt die Ergebnisliste wieder
	 */
	@GetMapping(path="/find")
	@ResponseBody
	public Address find(@RequestParam(name="street", required=true) String street, 
								@RequestParam(name="zip", required=true) int zip) {
		return addressService.findAddress(street, zip);
	}
}
