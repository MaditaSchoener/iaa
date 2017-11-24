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

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private IAddressService addressService;
	
	@GetMapping(path="/streets")
	public List <String> streetsStartingWith(@RequestParam(name="start", defaultValue="") String start) {
		return addressService.getStreetsStartingWith(start);
	}
	@GetMapping(path="/zips")
	public List <Integer> zipsForStreet(@RequestParam(name="street", required=true) String street) {
		return addressService.getZipsForStreet(street);
	}
	@GetMapping(path="/find")
	@ResponseBody
	public Address find(@RequestParam(name="street", required=true) String street, 
								@RequestParam(name="zip", required=true) int zip) {
		return addressService.findAddress(street, zip);
	}
}
