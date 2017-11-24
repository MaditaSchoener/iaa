package de.nordakademie.iaa.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.nordakademie.iaa.model.Address;
import de.nordakademie.iaa.model.BankingDetails;
import de.nordakademie.iaa.model.Contribution;
import de.nordakademie.iaa.model.Member;
import de.nordakademie.iaa.model.Membership;
import de.nordakademie.iaa.model.repository.IAddressRepository;
import de.nordakademie.iaa.model.repository.IBankingDetailsRepository;
import de.nordakademie.iaa.model.repository.IMemberRepository;
import de.nordakademie.iaa.model.repository.MemberRepository.SearchCriteria;

@Service
public class MemberService implements IMemberService {

	@Autowired
	private IMemberRepository repository;
	@Autowired
	private IAddressRepository addressRepository;
	@Autowired
	private IBankingDetailsRepository bankingDetailsRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<Member> findAll() {
		return repository.findAll();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Member find(Long id) {
		return repository.find(id);
	}
	
	@Override
	@Transactional
	public Member create(Member member) {
		mergeAssociations(member);
		member.setContribution(LocalDate.now().getYear(), calculateContribution(member));
		return repository.create(member);
	}

	
	@Override
	@Transactional
	public Member update(Member member) {
		mergeAssociations(member);
//		calculate the contribution for the next year if the membership changed
		Member persistentMember = repository.find(member.getNumber());
		if (persistentMember.getType() != member.getType()) {
			member.setContribution(LocalDate.now().getYear() + 1, calculateContribution(member));
		}
		return repository.update(member);
	}
	
	@Override
	public Member delete(Member member) throws ServiceException {
//		delete only allowed after 3 months
		if (member.isCancelled() && LocalDate.now().isAfter(member.getExitDate().plusMonths(3))) {
			repository.delete(member);
			return member;
		} else {
			throw new ServiceException("Das Löschen inaktiver Mitglieder ist erst 3 Monate nach Vereinsaustritt möglich.");
		}
	}
	
	@Override
	@Transactional
	public Member cancelContract(Member member) {
		LocalDate cancelDate = LocalDate.now();
		LocalDate exitDate = LocalDate.of(cancelDate.getYear(), 12, 31);
		member.cancelContract(cancelDate, exitDate);
		return this.update(member);
	}
	
	@Override
	public List<Member> search(Member member) {
		if (member.getNumber() != null) {
			return Arrays.asList(repository.find(member.getNumber()));
		} else {
			SearchCriteria criteria = new SearchCriteria();
			if (member.getType() != null) {
				criteria.appendType(member.getType());
			}
			if (member.getName() != null) {
				criteria.appendName(member.getName());
			}
			if (member.getSurname() != null) {
				criteria.appendSurname(member.getSurname());
			}
			Address address = member.getAddress();
			if (address != null) {
				if (address.getStreet() != null) {
					criteria.appendStreet(address.getStreet());
				}
				if (address.getZip() > 0) {
					criteria.appendZip(address.getZip());
				}
			}
			return repository.search(criteria);
		}
	}
	
	private void mergeAssociations(Member member) {
		mergeMembersAddress(member);
		mergeMembersBankingDetails(member);
	}

	private void mergeMembersAddress(Member member) {
		Address memberAddress = member.getAddress();
		Address persistentAddress = addressRepository.findAddress(memberAddress.getStreet(), memberAddress.getZip());
		if (persistentAddress == null) {
			addressRepository.create(memberAddress);
		} else {
			member.setAddress(persistentAddress);
		}
	}
	private void mergeMembersBankingDetails(Member member) {
		BankingDetails bankingDetails = member.getBankingDetails();
		BankingDetails persistentDetails = bankingDetailsRepository.findByIban(bankingDetails.getIban());
		if (persistentDetails == null) {
			bankingDetailsRepository.create(bankingDetails);
		} else {
			member.setBankingDetails(persistentDetails);
		}
	}
	
	@Transactional(readOnly=true)
	public Contribution calculateContribution (Member member) {
		Contribution result = new Contribution();
		int contributionToPay = member.getType().getPrice();
		
		Member familyMember = member.getFamilyMember();
		if (familyMember != null && familyMember.getNumber() != null) {
			Member persistentFamilyMember = familyMember != null? repository.find(familyMember.getNumber()): null;
			if (persistentFamilyMember != null) {
				contributionToPay = contributionToPay - Membership.FAMILY_DISCOUNT;
			}
		}
		
		result.setAmount(contributionToPay);
		result.setPaid(false);
		return result;
	}
}
