package com.mycompany.fileattente;

public class Ticket {
	
	private static int COUNTER;
	private int numero;
	private String service;
	private boolean privilegePriority;

	public Ticket(String service){
		this.numero = ++COUNTER; // to synchronize?
		this.service= service;	
	}

	public int getNumero() {
		return numero;
	}

	public String getService() {
		return service;
	}

	public boolean isPrivilegePriority() {
		return privilegePriority;
	}

	public void setPrivilegePriority(boolean privilegePriority) {
		this.privilegePriority = privilegePriority;
	}

	public String toString() {
		return "[ticket n° " + this.numero + "]";
	}
}
