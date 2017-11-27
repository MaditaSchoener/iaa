package de.nordakademie.iaa.service.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.nordakademie.iaa.model.BankingDetails;
import de.nordakademie.iaa.service.IBankingDetailsService;

/**
 * REST-Services f�r das Bankdetailsobjekt, welche vom Frontend aufgerufen werden k�nnen.
 *
 * @author Maik Voigt
 */
@RestController
@RequestMapping("/bankingDetails")
public class BankingDetailsController {

	@Autowired
	private IBankingDetailsService service;

	/**
	 * Ruft eine Liste mit IBANS auf, die mit den gleichen Zeichen anfangen
	 * @param start String mit Anfangszeichen
	 * @return Gibt die Ergbenisliste zur�ck
	 */
	@GetMapping(path="/ibans")
	public List <String> ibansStartingWith(@RequestParam(name="start", defaultValue="") String start) {
		return service.getIbansStartingWith(start);
	}

	/**
	 * Gibt aus der �bergebenen IBAN ein Detailobjekt wieder zur�ck
	 * @param iban IBAN als String
	 * @return
	 */
	@GetMapping(path="/find")
	public BankingDetails findWithIban(@RequestParam(name="iban", required=true) String iban) {
		return service.findBankingDetails(iban);
	}
}
