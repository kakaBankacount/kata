package com.mycompany.app.service;

import java.util.Date;
import java.util.List;

import com.mycompany.app.entity.Operation;
a
public interface IStatement {
	
	String getRefAccountNumber();
	
	double getBalance();
	
	Date getStartDate();
	
	Date getEndDate();
	
	List<Operation> getOperations();
	
}
