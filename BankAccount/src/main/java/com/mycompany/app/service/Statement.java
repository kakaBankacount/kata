package com.mycompany.app.service;

import java.util.Date;
import java.util.List;

import com.mycompany.app.entity.Account;
import com.mycompany.app.entity.Operation;

public class Statement implements  IStatement {

	private Date startDate;
	
	private Date endDate;
	
	private Account account;
	
	private List<Operation> operations;
	
	public Statement(Account account, List<Operation> operations, Date startDate, Date endDate) {
		this.account = account;
		this.operations = operations;
		this.startDate = startDate;
		this.endDate= endDate;
	}
	
	@Override
	public String getRefAccountNumber() {
		return this.account.getRefNumber();
	}

	@Override
	public double getBalance() {
		return this.account.getBalance();
	}

	@Override
	public Date getStartDate() {
		return this.startDate;
	}

	@Override
	public Date getEndDate() {
		return this.endDate;
	}

	@Override
	public List<Operation> getOperations() {
		return this.operations;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Account ref: ").append(account.getRefNumber()).append("\r\n");
		sb.append("Start date: ").append(startDate).append("\r\n");
		sb.append("End date: ").append(endDate).append("\r\n\r\n");
		
		for (Operation op : operations) {
			sb.append("Operation: ").append(op).append("\r\n");	
		}
		return sb.toString();
	}

}
