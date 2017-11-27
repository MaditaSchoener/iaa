package de.nordakademie.iaa.service.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceptionklasse für REST-Schicht
 *
 * @author Maik Voigt
 */
@ResponseStatus(reason="Technical Error")
public class RestException extends Exception {

	private static final long serialVersionUID = -5333820816441831667L;

	private List <String> messages;

	public RestException(List<String> messages) {
		super();
		this.messages = messages;
	}
	
	public RestException() {
		this (new ArrayList<>());
	}

	/**
	 * Fügt einen String an das Stringarray mit den Fehlernachrichten an
	 * @param message Nachricht als String
	 */
	public void addMessage (String message) {
		messages.add(message);
	}	
	public List<String> getMessages() {
		return messages;
	}
}
