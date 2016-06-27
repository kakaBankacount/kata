package com.mycompany.fileattente;

import com.mycompany.fileattente.client.ClientSocketConnexion;
import com.mycompany.fileattente.guichet.GuichetSocketConnexion;

public class FileAttenteServer {
	
	public static void main(String[] args) {
		
		System.out.println("=======================");
		System.out.println("        Server         ");
		System.out.println("=======================");
		
		
		ClientSocketConnexion clientSocketConnexion = new ClientSocketConnexion();
		GuichetSocketConnexion guichetSocketConnexion = new GuichetSocketConnexion(clientSocketConnexion);
		
		new Thread(clientSocketConnexion).start();
		new Thread(guichetSocketConnexion).start();
		
	}
}
