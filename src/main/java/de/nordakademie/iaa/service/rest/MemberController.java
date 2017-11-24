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

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private IMemberService memberService;

	@GetMapping
	public List<Member> findAll() {
		return memberService.findAll();
	}

	@GetMapping(path = "find")
	public Member findOne(@RequestParam(name = "id", required = true) Long id) {
		return memberService.find(id);
	}

	@GetMapping(path = "/types")
	public Membership[] readTypes() {
		return Membership.values();
	}

	@PostMapping(path = "/persist")
	public Member saveMember(@RequestBody final Member member) throws RestException {
		try {
			return memberService.create(member);
		} catch (ServiceException e) {
			throw new RestException(e.getMessages());
		}
	}

	@PostMapping(path = "/update")
	public Member updateMember(@RequestBody final Member member) throws RestException {
		try {
			return memberService.update(member);
		} catch (ServiceException e) {
			throw new RestException(e.getMessages());
		}
	}

	@PostMapping(path = "/search")
	public List<Member> searchMatchingMember(@RequestBody final Member blueprint) {
		return memberService.search(blueprint);
	}

	@PostMapping(path = "/cancel")
	public Member cancelMember(@RequestBody final Member member) {
		return memberService.cancelContract(member);
	}

	@PostMapping(path = "/delete")
	public Member deleteMember(@RequestBody final Member member) throws RestException {
		try {
			return memberService.delete(member);
		} catch (ServiceException e) {
			throw new RestException(e.getMessages());
		}
	}

	@PostMapping(path = "/contribution")
	public Contribution calculateContribution(@RequestBody final Member member) {
		return memberService.calculateContribution(member);
	}

	
	
	
	@ExceptionHandler(RestException.class)
	public ResponseEntity<Object> handleError(RestException ex) {
		return new ResponseEntity<Object>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
