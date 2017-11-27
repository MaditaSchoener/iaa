package de.nordakademie.iaa.model.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Exceptionklasse f�r die Repository Schicht
 *
 * @author Maik Voigt
 */

public class IllegalEntityException  extends Exception {

	private static final long serialVersionUID = -5672734137052028836L;

	private List <String> messages;

	public IllegalEntityException(List<String> messages) {
		super();
		this.messages = messages;
	}

	/**
	 * Erzeugt Exceptions und f�gt die �bergebene Fehlermeldung an das Nachrichtenarray
	 * @param message Fehlermeldung als String
	 */
	public IllegalEntityException(String message) {
		this (Arrays.asList(message));
	}
	public IllegalEntityException() {
		this (new ArrayList<>());
	}

	/**
	 * F�gt eine Nachricht der Exception hinzu
	 * @param message Fehlermeldung als String
	 */
	public void addMessage (String message) {
		messages.add(message);
	}	

	public List<String> getMessages() {
		return messages;
	}
}
