package de.nordakademie.iaa.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Maik Voigt
 *
 * Fachmodell für die Adressen, welche aus Straße, Hausnummer, Stadt und ZIP bestehen. Zusätzlich wird ein Objekt
 * der Adressen Identifikation erzeugt, damit Adressen verglichen werden können und Duplikate gemäß Anforderung
 * verhindert werden.
 */



@Entity
@Table(name="ADDRESS")
public class Address {
	
	@EmbeddedId
	private AddressIdentification identification;
	private String city;
	
	public Address () {
		this.identification = new AddressIdentification();
	}
	public String getStreet() {
		return identification.getStreet();
	}
	public void setStreet(String street) {
		this.identification.setStreet(street);
	}
	public int getZip() {
		return identification.getZip();
	}
	public void setZip(int zip) {
		this.identification.setZip(zip);
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identification == null) ? 0 : identification.hashCode());
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
		Address other = (Address) obj;
		if (identification == null) {
			if (other.identification != null)
				return false;
		} else if (!identification.equals(other.identification))
			return false;
		return true;
	}
		
	
}
