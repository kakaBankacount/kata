aapackage com.mycompany.app.service;
abccccs
public class BankAccountServiceFactory {
	
	public static IBankService createBankAccountService() {
		
		return new BankService();
	}

}
