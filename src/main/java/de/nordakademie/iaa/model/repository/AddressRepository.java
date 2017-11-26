package de.nordakademie.iaa.model.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.nordakademie.iaa.model.Address;

/**
 * Repository der Adressen. Straßennamen und Stadtnamen müssen der RegEx enstprechen.
 * Zusätzliche Abfrage für Auto Completion der Straßennamen sowie Validierung der Adresse.
 *
 * @author Maik Voigt
 */

@Repository
public class AddressRepository extends AbstractRepository<Address> implements IAddressRepository {

	private static final String STREET_PATTERN = "[a-zA-Zäöüß]+( )?[0-9]+[a-zA-Z]?";
	private static final String CITY_PATTERN = "[a-zA-Zäöüß]+";
	
	public AddressRepository() {
		super(Address.class);
	}

	/**
	 * Erstellt eine neue Adresse
	 *
	 * @param address Die Adresse vom Typ address
	 * @throws IllegalEntityException Exception, falls Adresse nicht erfolgreich validiert wurde
	 * @return Gibt die Adresse mit einer ID zurück
	 */
	@Override
	public Address create(Address address) throws IllegalEntityException {
		List <String> messages = validate(address);
		if (!messages.isEmpty()) {
			throw new IllegalEntityException(messages);
		}
		return super.persist(address);
	}

	/**
	 * Sucht eine Adresse, die mit der gleichen Zeichenfolge beginnt.
	 *
	 * @param street Die Straße als String
	 * @return Tabelle mit Abfrageergebnissen
	 */
	@Override
	public List <Address> findMatchingAddresses(String street) {
		TypedQuery<Address> query = entityManager()
				.createQuery("Select a FROM Address a WHERE a.identification.street = :street OR"
						+ " a.identification.street LIKE CONCAT(:street, '%')", Address.class);
		
		query.setParameter("street", street);
		return query.getResultList();
	}

	/**
	 * Sucht die Adresse anhand der Straße und der PLZ
	 *
	 * @param street Die Straße als String
	 * @param zip Die PLZ als Integer
	 * @return Gibt die Adresse zurück, falls gefunden
	 */
	@Override
	public Address findAddress(String street, int zip) {
		TypedQuery<Address> query = entityManager()
				.createQuery("Select a FROM Address a WHERE a.identification.street = :street AND "
						+ "									a.identification.zip = :zip", Address.class);
		query.setParameter("street", street);
		query.setParameter("zip", zip);
		List <Address> matchingAddresses = query.getResultList();
//		only one can be present
		return matchingAddresses.isEmpty()? null: matchingAddresses.get(0);
	}

	/**
	 * Validiert die Adresse auf Richtigkeit ihrer Straße, PLZ und Stadt
	 *
	 * @param address Die übergebene Adresse
	 * @return Gibt das Nachrichtenarray zurück
	 */
	private List <String> validate (Address address) {
		List <String> messages = new ArrayList<>();
		if (address.getStreet() == null || !address.getStreet().trim().matches(STREET_PATTERN)) {
			messages.add("Ungültige Straße. Bitte geben Sie eine gültige Straße mit Hausnummer ein");
		}
		if (address.getZip() < 1001) {
			messages.add("Ungültige Postleitzahl. Bitte geben Sie eine gültige fünfstellige Postleitzahl ein.");
		}
		if (address.getCity() == null || !address.getCity().trim().matches(CITY_PATTERN)) {
			messages.add("Ungültige Stadt. Bitte geben Sie eine gültige Stadt ein");
		}
		return messages;
	}
}
