package de.nordakademie.iaa.model.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.nordakademie.iaa.model.Member;
import de.nordakademie.iaa.model.Membership;

@Repository
public class MemberRepository extends AbstractRepository <Member> implements IMemberRepository {

	private static final String MEMBER_VAR_NAME = " m";
	private static final String SELECT_CLAUSE = "SELECT" + MEMBER_VAR_NAME + " FROM Member" + MEMBER_VAR_NAME;
	
	public MemberRepository() {
		super(Member.class);
	}
  
	@Override
	public Member create(Member member) {
		return super.persist(member);
	}	
	@Override
	public List<Member> findAll() {
		return entityManager().createQuery(SELECT_CLAUSE, Member.class).getResultList();
	}
	@Override
	public Member find(Long id) {
		return super.find(id);
	}
	@Override
	public List<Member> search(SearchCriteria criteria) {
		if (criteria.hasSearchCriteria()) {
			StringBuilder queryString = new StringBuilder(SELECT_CLAUSE);
			queryString.append(" WHERE");
			queryString.append(criteria.getJPLString());
			TypedQuery<Member> query = entityManager().createQuery(queryString.toString(), Member.class);
			criteria.getQueryVarsToSet().forEach((name, value) -> query.setParameter(name, value));
			return query.getResultList();
		} else {
			return findAll();
		}	
	}
	
	public static class SearchCriteria {

		private final StringBuilder searchCriteriaConcatinator = new StringBuilder();
		private final Map <String, Object> queryVarsToSet = new HashMap<>();
		
		public SearchCriteria appendType(Membership membership) {
			searchCriteriaConcatinator.append(MEMBER_VAR_NAME);
			searchCriteriaConcatinator.append(".type = :type");
			queryVarsToSet.put("type", membership);
			return this;
		}
		public SearchCriteria appendStreet(String street) {
			searchCriteriaConcatinator.append(MEMBER_VAR_NAME);
			searchCriteriaConcatinator.append(".address.street = ");
			searchCriteriaConcatinator.append(street);
			return this;
		}
		public SearchCriteria appendZip(int zip) {
			searchCriteriaConcatinator.append(MEMBER_VAR_NAME);
			searchCriteriaConcatinator.append(".address.zip = ");
			searchCriteriaConcatinator.append(zip);
			return this;
		}
		public SearchCriteria appendName(String name) {
			searchCriteriaConcatinator.append(MEMBER_VAR_NAME);
			searchCriteriaConcatinator.append(".name = ");
			searchCriteriaConcatinator.append(name);
			return this;
		}
		public SearchCriteria appendSurname(String surname) {
			searchCriteriaConcatinator.append(MEMBER_VAR_NAME);
			searchCriteriaConcatinator.append(".surname = ");
			searchCriteriaConcatinator.append(surname);
			return this;
		}	
		public boolean hasSearchCriteria () {
			return searchCriteriaConcatinator.length() > 0;
		}
		protected String getJPLString () {
			return searchCriteriaConcatinator.toString();
		}
		protected Map <String, Object> getQueryVarsToSet () {
			return queryVarsToSet;
		}
	}

}
