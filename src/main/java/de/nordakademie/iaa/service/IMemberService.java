package de.nordakademie.iaa.service;

import java.util.List;

import de.nordakademie.iaa.model.Contribution;
import de.nordakademie.iaa.model.Member;

public interface IMemberService {

	List <Member> findAll();
	
	Member find (Long id);

	List<Member> search (Member member);
	
	
	Member create (Member member) throws ServiceException;
	
	Member update (Member member) throws ServiceException;
	
	Member delete (Member member) throws ServiceException;
	
	Member cancelContract (Member member) throws ServiceException;
	
	Contribution calculateContribution (Member member);
}
