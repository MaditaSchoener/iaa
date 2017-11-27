package de.nordakademie.iaa.service.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.nordakademie.iaa.model.Contribution;
import de.nordakademie.iaa.model.Member;
import de.nordakademie.iaa.model.Membership;
import de.nordakademie.iaa.service.IMemberService;
import de.nordakademie.iaa.service.ServiceException;

/**
 * REST-Services für das Bankdetailsobjekt, welche vom Frontend aufgerufen werden können.
 *
 * @author Maik Voigt
 */
@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private IMemberService memberService;

	@GetMapping
	public List<Member> findAll() {
		return memberService.findAll();
	}

	/**
	 * Aufruf zum Finden eines Mitglieds mit übergebener ID
	 * @param id ID als Long
	 * @return gibt ein Mitglied als Member zurück
	 */
	@GetMapping(path = "find")
	public Member findOne(@RequestParam(name = "id", required = true) Long id) {
		return memberService.find(id);
	}

	/**
	 * Ruft die Mitgliedsarten auf und gibt diese zurück
	 * @return Mitgliedsarten als Enum
	 */
	@GetMapping(path = "/types")
	public Membership[] readTypes() {
		return Membership.values();
	}

	/**
	 * Speichert ein übergebenes Mitglied
	 * @param member Mitglied vom Typ Member
	 * @return Gibt das Mitglied mit ID zurück
	 * @throws RestException Exception, falls Mitglied nicht erstellt werden konnte
	 */
	@PostMapping(path = "/persist")
	public Member saveMember(@RequestBody final Member member) throws RestException {
		try {
			return memberService.create(member);
		} catch (ServiceException e) {
			throw new RestException(e.getMessages());
		}
	}

	/**
	 * Aktualisiert ein Mitgliedsobjekt
	 * @param member Mitglied vom Typ Member
	 * @return Gibt das Mitglied mit ID zurück
	 * @throws RestException Exception, wenn Mitglied nicht aktualisiert werden konnte
	 */
	@PostMapping(path = "/update")
	public Member updateMember(@RequestBody final Member member) throws RestException {
		try {
			return memberService.update(member);
		} catch (ServiceException e) {
			throw new RestException(e.getMessages());
		}
	}

	/**
	 * Erstellt aus einer Filterkriterium
	 * @param blueprint Ein Mitgliedsobjekt als Blueprint
	 * @return Liefert eine Ergebnisliste
	 */
	@PostMapping(path = "/search")
	public List<Member> searchMatchingMember(@RequestBody final Member blueprint) {
		return memberService.search(blueprint);
	}

	/**
	 * Kündigt einem Mitglied die Mitgliedschaft
	 * @param member Mitglied vom Typ Member
	 * @return Gibt das Mitglied mit ID zurück
	 * @throws RestException Exception, wenn Mitgliedschaft nicht gekündigt werden konnte
	 */
	@PostMapping(path = "/cancel")
	public Member cancelMember(@RequestBody final Member member) throws RestException {
		try {
			return memberService.cancelContract(member);
		} catch (ServiceException e) {
			throw new RestException(e.getMessages());
		}
	}

	/**
	 * Löscht ein Mitglied
	 * @param member Mitglied vom Typ Member
	 * @return Gibt das Mitglied mit ID zurück
	 * @throws RestException Exception, falls Löschen fehlschlägt
	 */
	@PostMapping(path = "/delete")
	public Member deleteMember(@RequestBody final Member member) throws RestException {
		try {
			return memberService.delete(member);
		} catch (ServiceException e) {
			throw new RestException(e.getMessages());
		}
	}

	/**
	 * Berechnet den Mitgliedsbeitrag für ein Mitglied
	 * @param member Mitglied vom Typ Member
	 * @return Gibt den Mitgliedsbeitrag zurück
	 */
	@PostMapping(path = "/contribution")
	public Contribution calculateContribution(@RequestBody final Member member) {
		return memberService.calculateContribution(member);
	}

	/**
	 * Exceptionhandler für MemberController
	 * @param ex Exception vom Typ RestException
	 */
	@ExceptionHandler(RestException.class)
	public ResponseEntity<Object> handleError(RestException ex) {
		return new ResponseEntity<Object>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
