package de.nordakademie.iaa.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author Maik Voigt
 *
 * Exceptionklasse für die Service Schicht
 */

public class ServiceException extends Exception {

	private static final long serialVersionUID = -5672734137052028836L;

	private List <String> messages;

	public ServiceException(List<String> messages) {
		super();
		this.messages = messages;
	}
	public ServiceException(String message) {
		this (Arrays.asList(message));
	}
	public ServiceException() {
		this (new ArrayList<>());
	}
	
	public void addMessage (String message) {
		messages.add(message);
	}	
	public List<String> getMessages() {
		return messages;
	}
}
