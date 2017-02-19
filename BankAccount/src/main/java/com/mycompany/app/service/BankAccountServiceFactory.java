aapackage com.mycompany.app.service;
abccccsdef
public class BankAccountServiceFactory {
	
	public static IBankService createBankAccountService() {
		
		return new BankService();
	}

}
