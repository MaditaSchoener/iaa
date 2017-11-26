package de.nordakademie.iaa.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Aufz�hlung der Mitgliedsarten, sowie der zugeh�rigen Preise. Festlegung des Familienrabatts.
 *
 * @author Maik Voigt
 */

public enum Membership {

	FULL("Vollmitgliedschaft", 25),
	DISCOUNTED("Erm��igt", 23),
	YOUTH("Jugendlich(bis 18 Jahre)", 15),
	AIDED("F�rdermitglied", 10);
	
	
	public static final int FAMILY_DISCOUNT = 3;
	
	@JsonValue
	private final String name;
	private final int price;
	
	private Membership(String name, int price) {
		this.name = name;
		this.price = price;
	}
	public int getPrice() {
		return price;
	}
	public String getName() {
		return name;
	}
	
	@JsonCreator
	public static final Membership fromName(@JsonProperty("name") String name) {
		for (Membership membership: values()) {
			if (membership.getName().equals(name)) {
				return membership;
			}
		}
		return null;
	}
}
