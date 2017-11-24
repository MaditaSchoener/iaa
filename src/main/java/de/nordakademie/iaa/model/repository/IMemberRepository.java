package de.nordakademie.iaa.model.repository;

import java.util.List;

import de.nordakademie.iaa.model.Member;
import de.nordakademie.iaa.model.repository.MemberRepository.SearchCriteria;

public interface IMemberRepository {

	List <Member> findAll ();
	
	Member find(Long id);
	
	List <Member> search (SearchCriteria criteria);
	
	
	Member create(Member member);
	
	Member update(Member member);
	
	void delete(Member member);
}
