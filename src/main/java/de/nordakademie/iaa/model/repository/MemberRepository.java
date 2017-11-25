package de.nordakademie.iaa.model.repository;

import java.time.LocalDate;
import java.util.ArrayList;
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
	public Member create(Member member) throws IllegalEntityException {
		validate(member);
		return super.persist(member);
	}	
	@Override
	public Member update(Member type) throws IllegalEntityException {
		validate(type);
		return super.update(type);
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
	
	private void validate (Member member) throws IllegalEntityException {
		List <String> messages = new ArrayList<>();
		if (member.getType() == null) {
			messages.add("Es muss eine Mitgliedschaftsart ausgewählt sein!");
		}
		if (member.getName() == null || member.getName().trim().isEmpty()
			|| member.getSurname() == null || member.getSurname().trim().isEmpty()) {
			messages.add("Mitglieder müssen mit Vor-und Nachnamen eingetragen werden!");
		}
		if (member.getBirth() == null || member.getBirth().isAfter(LocalDate.now())) {
			messages.add("Der Geburtstag eines Mitglieds darf nicht in der Zukunft liegen.");
		}
		if (!messages.isEmpty()) {
			throw new IllegalEntityException(messages);
		}
	}
	
	public static class SearchCriteria {

		private final StringBuilder searchCriteriaConcatinator = new StringBuilder();
		private final Map <String, Object> queryVarsToSet = new HashMap<>();
		
		public SearchCriteria appendType(Membership membership) {
			queryVarsToSet.put("type", membership);
			return appendCriteria("type", ":type");
		}
		public SearchCriteria appendStreet(String street) {
			return appendCriteria("address.identification.street", markStringAsString(street));
		}
		public SearchCriteria appendZip(int zip) {
			return appendCriteria("address.identification.zip", zip);
		}
		public SearchCriteria appendName(String name) {
			return appendCriteria("name", markStringAsString(name));
		}
		public SearchCriteria appendSurname(String surname) {
			return appendCriteria("surname", markStringAsString(surname));
		}	
		private SearchCriteria appendCriteria (String propertyName, Object value) {
			if (hasSearchCriteria()) {
				searchCriteriaConcatinator.append(" AND ");
			}
			searchCriteriaConcatinator.append(MEMBER_VAR_NAME);
			searchCriteriaConcatinator.append(".");
			searchCriteriaConcatinator.append(propertyName);
			searchCriteriaConcatinator.append(" = ");
			searchCriteriaConcatinator.append(value);
			return this;
		}
		private String markStringAsString (String string) {
			return "'" + string + "'";
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
