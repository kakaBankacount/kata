package com.mycompany.fileattente.guichet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class GuichetSocketConnexion implements Runnable {

	IGuichetConnected client;
	
	public GuichetSocketConnexion(IGuichetConnected client) {
		this.client = client;
	}

	@Override
	public void run() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(2009);
			System.out.println("Le serveur est à l'écoute du port "+ss.getLocalPort());
			
			Socket socket = null;
			while ((socket = ss.accept()) != null) {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String service = in.readLine();
				Guichet guichet = new Guichet(socket, service);
				this.client.onGuichetConnected(guichet);
				new Thread(guichet).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
