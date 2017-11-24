package de.nordakademie.iaa.model.repository;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.nordakademie.iaa.model.Address;

@Repository
public class AddressRepository extends AbstractRepository<Address> implements IAddressRepository {

	public AddressRepository() {
		super(Address.class);
	}

	@Override
	public Address create(Address address) {
		return super.persist(address);
	}
	@Override
	public List <Address> findMatchingAddresses(String street) {
		TypedQuery<Address> query = entityManager()
				.createQuery("Select a FROM Address a WHERE a.identification.street = :street OR"
						+ " a.identification.street LIKE CONCAT(:street, '%')", Address.class);
		
		query.setParameter("street", street);
		return query.getResultList();
	}
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
}
