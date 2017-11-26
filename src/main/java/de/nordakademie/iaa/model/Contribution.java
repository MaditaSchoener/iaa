package de.nordakademie.iaa.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Fachmodell für die Mitgliederbeiträge. Besteht aus Betrag, Jahr und einem Boolean, der Auskunft gibt ob bezahlt wurde.
 *
 * @author Maik Voigt
 */

@Entity
@Table(name="CONTRIBUTION")
public class Contribution {

	@Id
	@GeneratedValue
	private Long id;
	@Basic(optional=false)
	private int year;
	@Basic(optional=false)
	private int amount;
	@Basic(optional=false)
	private boolean paid;
	
	public Long getId() {
		return id;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public boolean isPaid() {
		return paid;
	}
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	protected int getYear() {
		return year;
	}
	protected void setYear(int year) {
		this.year = year;
	}

	/**
	 * Erstellt einen Hash-Code für Hash-Tabellen
	 * @return Hashwert als Integer
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Definition von Vergleichsoperationen
	 * @param obj Das Vergleichsobjekt von Typ Object
	 * @return Vergleichsresultat als Boolean
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contribution other = (Contribution) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
