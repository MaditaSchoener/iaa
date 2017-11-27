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
 * REST-Services f�r das Bankdetailsobjekt, welche vom Frontend aufgerufen werden k�nnen.
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
	 * Aufruf zum Finden eines Mitglieds mit �bergebener ID
	 * @param id ID als Long
	 * @return gibt ein Mitglied als Member zur�ck
	 */
	@GetMapping(path = "find")
	public Member findOne(@RequestParam(name = "id", required = true) Long id) {
		return memberService.find(id);
	}

	/**
	 * Ruft die Mitgliedsarten auf und gibt diese zur�ck
	 * @return Mitgliedsarten als Enum
	 */
	@GetMapping(path = "/types")
	public Membership[] readTypes() {
		return Membership.values();
	}

	/**
	 * Speichert ein �bergebenes Mitglied
	 * @param member Mitglied vom Typ Member
	 * @return Gibt das Mitglied mit ID zur�ck
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
	 * @return Gibt das Mitglied mit ID zur�ck
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
	 * K�ndigt einem Mitglied die Mitgliedschaft
	 * @param member Mitglied vom Typ Member
	 * @return Gibt das Mitglied mit ID zur�ck
	 * @throws RestException Exception, wenn Mitgliedschaft nicht gek�ndigt werden konnte
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
	 * L�scht ein Mitglied
	 * @param member Mitglied vom Typ Member
	 * @return Gibt das Mitglied mit ID zur�ck
	 * @throws RestException Exception, falls L�schen fehlschl�gt
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
	 * Berechnet den Mitgliedsbeitrag f�r ein Mitglied
	 * @param member Mitglied vom Typ Member
	 * @return Gibt den Mitgliedsbeitrag zur�ck
	 */
	@PostMapping(path = "/contribution")
	public Contribution calculateContribution(@RequestBody final Member member) {
		return memberService.calculateContribution(member);
	}

	/**
	 * Exceptionhandler f�r MemberController
	 * @param ex Exception vom Typ RestException
	 */
	@ExceptionHandler(RestException.class)
	public ResponseEntity<Object> handleError(RestException ex) {
		return new ResponseEntity<Object>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
