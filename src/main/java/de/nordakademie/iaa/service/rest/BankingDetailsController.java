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
 * REST-Services für das Bankdetailsobjekt, welche vom Frontend aufgerufen werden können.
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
	 * @return Gibt die Ergbenisliste zurück
	 */
	@GetMapping(path="/ibans")
	public List <String> ibansStartingWith(@RequestParam(name="start", defaultValue="") String start) {
		return service.getIbansStartingWith(start);
	}

	/**
	 * Gibt aus der übergebenen IBAN ein Detailobjekt wieder zurück
	 * @param iban IBAN als String
	 * @return
	 */
	@GetMapping(path="/find")
	public BankingDetails findWithIban(@RequestParam(name="iban", required=true) String iban) {
		return service.findBankingDetails(iban);
	}
}
