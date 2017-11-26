package de.nordakademie.iaa.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Maik Voigt
 *
 * Fachmodell für die Bankdetails, welche aus IBAN und BIC bestehen.
 */

@Entity
@Table(name="BANKING_DETAILS")
public class BankingDetails {
	
	@Id
	private String iban;
	@Basic(optional=false)
	private String bic;
	
	
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getBIC() {
		return bic;
	}
	public void setBIC(String bankName) {
		this.bic = bankName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iban == null) ? 0 : iban.hashCode());
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
		BankingDetails other = (BankingDetails) obj;
		if (iban == null) {
			if (other.iban != null)
				return false;
		} else if (!iban.equals(other.iban))
			return false;
		return true;
	}	
	
	
}
