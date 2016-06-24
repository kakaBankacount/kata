package com.mycompany.app.service;

import java.util.Date;

import com.mycompany.app.entity.Operation;

public interface IBankService {
	
	Operation withDrawal(String accountRefNumber, double amount) throws AccountOperationException;
	
	Operation deposit(String accountRefNumber, double amount) throws AccountOperationException;
	
	IStatement getOperations(String accountRefNumber, Date startDate, Date endDate) throws AccountOperationException;
}
