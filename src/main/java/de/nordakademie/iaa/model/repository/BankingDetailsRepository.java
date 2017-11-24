package de.nordakademie.iaa.model.repository;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.nordakademie.iaa.model.BankingDetails;

@Repository
public class BankingDetailsRepository extends AbstractRepository<BankingDetails> implements IBankingDetailsRepository {

	public BankingDetailsRepository() {
		super(BankingDetails.class);
	}

	@Override
	public BankingDetails findByIban(String iban) {
		TypedQuery<BankingDetails> query = 
				entityManager().createQuery("SELECT b FROM BankingDetails b WHERE b.iban = :iban", BankingDetails.class);
		query.setParameter("iban", iban);
		List <BankingDetails> details = query.getResultList();
//		There can only be one
		return details.isEmpty()? null: details.get(0);
	}

	@Override
	public BankingDetails create(BankingDetails details) {
		return super.persist(details);
	}

}
