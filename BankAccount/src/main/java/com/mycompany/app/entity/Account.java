package com.mycompany.app.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Account {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String refNumber;
	
	private double balance;
	
	@OneToMany(mappedBy="account")
	private List<Operation> operations = new ArrayList<Operation>();
		
	public Account() {
		
	}
	
	public Account(String refNumber, double balance) {
		this.refNumber = refNumber;
		this.balance = balance;
	}

	public long getId() {
		return id;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public List<Operation> getOperations() {
		return operations;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id :").append(id)
		.append("/ refNumber: ").append(refNumber)
		.append("/ balance: ").append(balance);
		return sb.toString();
	}
}
