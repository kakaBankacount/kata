aapackage com.mycompany.app.service;
abccccsd
public class BankAccountServiceFactory {
	
	public static IBankService createBankAccountService() {
		
		return new BankService();
	}

}
