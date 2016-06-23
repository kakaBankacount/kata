package com.mycompany.app.service;

import java.util.Date;
import java.util.List;

import com.mycompany.app.entity.Operation;

public interface IBankService {
	
	Operation withDrawal(String accountRefNumber, double amount) throws AccountOperationException;
	
	Operation deposit(String accountRefNumber, double amount) throws AccountOperationException;
	
	List<Operation> getOperations(String accountRefNumber, Date startDate, Date endDate) throws AccountOperationException;
}
