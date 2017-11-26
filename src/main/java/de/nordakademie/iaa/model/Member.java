package de.nordakademie.iaa.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @author Maik Voigt
 *
 * Fachmodell für ein Mitglied. Besteht aus einer generierten Mitglieds ID, dem Vornamen, Nachnamen, Geburtstag,
 * Beitrittsdatum, der Mitgliedsart, dem Inaktivitätszustand (wenn gekündigt wird), dem eventuellen Familienmitglied
 * und den Bankdetails.
 * Beinhaltet  auch die Funktionalität, den Mitgliedsbeitrag für andere Jahre festzulegen, sowie Prüfungen ob der
 * Beitrag sich im nächsten Jahr ändert.
 */

@Entity
@Table(name="MEMBER")
public class Member implements Serializable {
	
	private static final long serialVersionUID = -5783579238006422595L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long number;
	@Basic(optional=false)
	private String name;
	@Basic(optional=false)
	private String surname;
	@Basic
	private LocalDate birth;
	
	@Basic
	private LocalDate entryDate;
	@Basic(optional=false)
	private Membership type;
	
	@OneToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Inactivity inactivity;
	@ManyToOne
	private Member familyMember;
	@ManyToOne
	private Address address;
	@ManyToOne
	private BankingDetails bankingDetails;
	
//	Fetch the contributions eagerly to determine the currentContribution (no performance issues expected because not
//	that many datasets)
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="year")
	private Map <Integer, Contribution> contributions = new HashMap<>();
	
	@PrePersist
	protected void beforePersist () {
		this.entryDate = LocalDate.now();
	}
	
	public void cancelContract(LocalDate cancelDate, LocalDate exitDate) {
		this.inactivity = new Inactivity();
		this.inactivity.setCancellationDate(cancelDate);
		this.inactivity.setExitDate(exitDate);
		this.inactivity.setRefMember(this);
	}
	public boolean isCancelled () {
		return inactivity != null;
	}
	public LocalDate getCancelDate () {
		return isCancelled()? inactivity.getCancellationDate(): null;
	}
	public LocalDate getExitDate () {
		return isCancelled()? inactivity.getExitDate(): null;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public LocalDate getBirth() {
		return birth;
	}
	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}
	public LocalDate getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(LocalDate entryDate) {
		this.entryDate = entryDate;
	}
	public Membership getType() {
		return type;
	}
	public void setType(Membership type) {
		this.type = type;
	}
	public Member getFamilyMember() {
		return familyMember;
	}
	public void setFamilyMember(Member familyMember) {
		this.familyMember = familyMember;
	}
	
	public void setContribution (int year, Contribution contribution) {
		contribution.setYear(year);
		contributions.put(year, contribution);
	}
	public void removeContribution (int year) {
		contributions.remove(year);
	}
	public int getCurrentContribution() {
		Contribution contributionForYear = contributions.get(LocalDate.now().getYear());
		return contributionForYear != null? contributionForYear.getAmount(): 0;
	}
	public int getFutureContribution() {
		Contribution futureContribution = contributions.get(LocalDate.now().getYear() + 1);
		return futureContribution != null? futureContribution.getAmount(): getCurrentContribution();
	}
	public boolean getWillContributionChange () {
		return getCurrentContribution() != getFutureContribution();
	}
	public Map <Integer, Contribution> getContributions () {
		return Collections.unmodifiableMap(contributions);
	}
	public void setContributions (Map <Integer, Contribution> contributions) {
		contributions.forEach(this::setContribution);
	}
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public BankingDetails getBankingDetails() {
		return bankingDetails;
	}
	public void setBankingDetails(BankingDetails bankingDetails) {
		this.bankingDetails = bankingDetails;
	}
	public Long getNumber() {
		return number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}	
}
