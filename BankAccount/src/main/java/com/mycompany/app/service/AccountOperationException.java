package com.mycompany.app.service;
abcdefghijklm
public class AccountOperationException extends Exception {
a	
	public AccountOperationException(String errorMessage) {
		super(errorMessage);
	}
	public AccountOperationException(Exception cause) {
		super(cause);
	}
}
