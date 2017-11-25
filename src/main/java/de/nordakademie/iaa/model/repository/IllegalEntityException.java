package de.nordakademie.iaa.model.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IllegalEntityException  extends Exception {

	private static final long serialVersionUID = -5672734137052028836L;

	private List <String> messages;

	public IllegalEntityException(List<String> messages) {
		super();
		this.messages = messages;
	}
	public IllegalEntityException(String message) {
		this (Arrays.asList(message));
	}
	public IllegalEntityException() {
		this (new ArrayList<>());
	}
	
	public void addMessage (String message) {
		messages.add(message);
	}	
	public List<String> getMessages() {
		return messages;
	}
}
