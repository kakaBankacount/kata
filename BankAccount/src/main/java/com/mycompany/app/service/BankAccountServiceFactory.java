aapackage com.mycompany.app.service;
a
public class BankAccountServiceFactory {
	
	public static IBankService createBankAccountService() {
		
		return new BankService();
	}

}
