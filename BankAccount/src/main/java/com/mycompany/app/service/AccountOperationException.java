package com.mycompany.app.service;
abcdefghij
public class AccountOperationException extends Exception {
	
	public AccountOperationException(String errorMessage) {
		super(errorMessage);
	}
	public AccountOperationException(Exception cause) {
		super(cause);
	}
}
