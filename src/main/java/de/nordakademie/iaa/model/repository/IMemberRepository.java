package de.nordakademie.iaa.model.repository;

import java.util.List;

import de.nordakademie.iaa.model.Member;
import de.nordakademie.iaa.model.repository.MemberRepository.SearchCriteria;

/**
 * @author Maik Voigt
 *
 * Interface des Repository der Mitglieder
 */

public interface IMemberRepository {

	List <Member> findAll ();
	
	Member find(Long id);
	
	List <Member> search (SearchCriteria criteria);
	
	
	Member create(Member member) throws IllegalEntityException ;
	
	Member update(Member member) throws IllegalEntityException ;
	
	void delete(Member member);
}
