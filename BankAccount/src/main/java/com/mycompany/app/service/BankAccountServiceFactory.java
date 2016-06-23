package com.mycompany.app.service;

public class BankAccountServiceFactory {
	
	public static IBankService createBankAccountService() {
		
		return new BankService();
	}

}
