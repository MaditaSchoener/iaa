package de.nordakademie.iaa.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import de.nordakademie.iaa.model.repository.IllegalEntityException;
import de.nordakademie.iaa.model.repository.MemberRepository.SearchCriteria;

/**
 * Service Klasse vom Mitglied
 *
 * @author Maik Voigt
 */
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

	/**
	 * Erstellt ein Mitglied und bestimmt den Mitgliedsbeitrag
	 * @param member Mitglied vom Typ Member
	 * @return Gibt das Mitglied mit ID zurück
	 * @throws ServiceException Exception, falls Mitglied nicht erstellt werden konnte
	 */
	@Override
	@Transactional
	public Member create(Member member) throws ServiceException {
		mergeAssociations(member);
		member.setContribution(LocalDate.now().getYear(), calculateContribution(member));
		try {
			return repository.create(member);
		} catch (IllegalEntityException e) {
			throw new ServiceException(e.getMessages());
		}
	}

	/**
	 * Aktualisiert ein Mitglied und den zugehörigen Mitgliedsbeitrag
	 * @param member Mitglied vom Typ Member
	 * @return Gibt das Mitglied mit ID zurück
	 * @throws ServiceException Exception, falls Mitglied nicht aktualisiert werden konnte
	 */
	@Override
	@Transactional
	public Member update(Member member) throws ServiceException {
		mergeAssociations(member);
//		calculate the contribution for the next year if the membership changed
		Member persistentMember = repository.find(member.getNumber());
		if (persistentMember.getType() != member.getType()) {
			member.setContribution(LocalDate.now().getYear() + 1, calculateContribution(member));
		}
		try {
			return repository.update(member);
		} catch (IllegalEntityException e) {
			throw new ServiceException(e.getMessages());
		}
	}

	/**
	 * Löscht ein Mitglied, falls es länger als 3 Monate ausgetreten ist
	 * @param member Mitglied vom Typ Member
	 * @return Gibt das Mitglied zurück
	 * @throws ServiceException Exception, falls Mitglied seit weniger als 3 Monaten ausgetreten ist
	 */
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

	/**
	 * Kündigt die Mitgliedschaft eines Mitglieds und setzt die Datumwerte des Kündigungsdatums
	 * und des Austrittsdatums
	 * @param member Mitglied vom Typ Member
	 * @return Gibt das Mitglied mit ID zurück
	 * @throws ServiceException Exception, falls Kündigung fehlgeschlagen ist
	 */
	@Override
	@Transactional
	public Member cancelContract(Member member) throws ServiceException {
		LocalDate cancelDate = LocalDate.now();
		LocalDate exitDate = LocalDate.of(cancelDate.getYear(), 12, 31);
		member.cancelContract(cancelDate, exitDate);
		member.getContributions()
				.keySet()
				.stream()
				.filter(cont -> cont > LocalDate.now().getYear())
				.collect(Collectors.toList())
				.forEach(member::removeContribution);
		return this.update(member);
	}

	/**
	 * Sucht anhand eines oder mehrerer Filterkriterien ein passendes Mitglied
	 * @param member Mitglied zu finden vom Typ Member
	 * @return Gibt die Ergebnisliste zurück
	 */
	@Override
	public List<Member> search(Member member) {
		if (member.getNumber() != null) {
			return Arrays.asList(repository.find(member.getNumber()));
		} else {
			SearchCriteria criteria = new SearchCriteria();
			if (member.getType() != null) {
				criteria.appendType(member.getType());
			}
			if (member.getName() != null && !member.getName().trim().isEmpty()) {
				criteria.appendName(member.getName());
			}
			if (member.getSurname() != null && !member.getSurname().trim().isEmpty()) {
				criteria.appendSurname(member.getSurname());
			}
			Address address = member.getAddress();
			if (address != null) {
				if (address.getStreet() != null && !address.getStreet().trim().isEmpty()) {
					criteria.appendStreet(address.getStreet());
				}
				if (address.getZip() > 0) {
					criteria.appendZip(address.getZip());
				}
			}
			return repository.search(criteria);
		}
	}

	/**
	 * Stellt die Redundanzfreiheit von Adresse und Bankdetails sicher
	 * @param member Mitglied vom Typ Member
	 * @throws ServiceException Exception falls EntityException
	 */
	private void mergeAssociations(Member member) throws ServiceException {
		try {
			mergeMembersAddress(member);
			mergeMembersBankingDetails(member);
		} catch (IllegalEntityException e) {
			throw new ServiceException(e.getMessages());
		}
	}

	/**
	 * Prüft, ob eine Adresse bereits existiert und verweist in diesem Fall auf die bereits vorhandene
	 * Adresse. Falls sie nicht existiert, wird ein neues Adressenobjekt angelegt
	 * @param member Mitglied vom Typ Member
	 * @throws IllegalEntityException Exception, falls Kunde keine Adresse hat
	 */
	private void mergeMembersAddress(Member member) throws IllegalEntityException {
		Address memberAddress = member.getAddress();
		if (memberAddress == null) {
			throw new IllegalEntityException("Ein Kunde muss eine Adresse haben!");
		}
		Address persistentAddress = addressRepository.findAddress(memberAddress.getStreet(), memberAddress.getZip());
		if (persistentAddress == null) {
			addressRepository.create(memberAddress);
		} else {
			member.setAddress(persistentAddress);
		}
	}

	/**
	 * Prüft, ob ein Bankdetailobjekt bereits existiert und verweist in diesem Fall auf das bereits vorhandene
	 * Objekt. Falls es nicht existiert, wird ein neues Bankdetailsobjekt angelegt
	 * @param member Mitglied vom Typ Member
	 * @throws IllegalEntityException Exception, falls Kunde keine Bankdetails hat
	 */
	private void mergeMembersBankingDetails(Member member) throws IllegalEntityException {
		BankingDetails bankingDetails = member.getBankingDetails();
		if (bankingDetails == null || bankingDetails.getIban() == null) {
			throw new IllegalEntityException("Ein Kunde muss BankDetails hinterlegt haben!");
		}
		BankingDetails persistentDetails = bankingDetailsRepository.findByIban(bankingDetails.getIban());
		if (persistentDetails == null) {
			bankingDetailsRepository.create(bankingDetails);
		} else {
			member.setBankingDetails(persistentDetails);
		}
	}

	/**
	 * Berechnet den Mitgliedsbeitrag abhängig von Mitgliedschaft und ob Mitglied den Familienrabatt bekommt
	 * @param member Mitglied vom Typ Member
	 * @return Mitgliedspreis als Integer
	 */
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
