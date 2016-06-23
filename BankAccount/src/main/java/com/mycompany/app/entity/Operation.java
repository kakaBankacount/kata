package com.mycompany.app.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Operation {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private Account account;
	
	private Date operationDate;
	
	private double amount;

	public Operation() {
		
	}
	
	public Operation(Date date, double amount) {
		this.operationDate = date;
		this.amount = amount;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getId() {
		return id;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id :").append(id)
		.append("/ operationDate: ").append(operationDate)
		.append("/ amount: ").append(amount);
		return sb.toString();
	}
	
	public boolean equals(Object from) {
		if (!(from instanceof Operation)){
			return false;
		}
		
		Operation fromOperation = (Operation) from;
		
		if (id == fromOperation.id
		   && operationDate.equals(fromOperation.operationDate)
		   && amount == fromOperation.amount) {
			return true;
		} else {
			return false;
		}
	}
	
}
