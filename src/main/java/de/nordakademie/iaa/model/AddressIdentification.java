package de.nordakademie.iaa.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * @author Maik Voigt
 *
 * Fachmodell für die Adressen Identifikation, sodass ein Vergleich von Adressen ermöglicht wird und
 * Duplikate verhindert werden
 */

@Embeddable
public class AddressIdentification implements Serializable {

	private static final long serialVersionUID = 5925052899400618155L;

	private String street;
	private int zip;
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + zip;
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
		AddressIdentification other = (AddressIdentification) obj;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zip != other.zip)
			return false;
		return true;
	}
	
	
	
}
