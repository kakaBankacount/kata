package com.mycompany.fileattente.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.fileattente.guichet.Guichet;
import com.mycompany.fileattente.guichet.IGuichetConnected;

public class ClientSocketConnexion implements Runnable, IGuichetConnected {

	private Client client;
	
	public final static List<Guichet> SAVED_GUICHETS = new ArrayList<Guichet>();
	
	public ClientSocketConnexion() {
	}

	@Override
	public void run() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(1009);
			System.out.println("Le serveur est à l'écoute du port "+ss.getLocalPort());
			Socket socket = null;
			while ((socket = ss.accept()) != null) {
				
				if (this.client != null) {
					String message = "Client is alreadt connected. Only one is authorized.";
					System.err.println(message);
					socket.close();
					continue;
				}
				this.client = new Client(socket);
				new Thread(client).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onGuichetConnected(Guichet guichet) {
		
		if (this.client == null) {
			String message = "Client is not connected. Connect it firsts !!!";
			System.err.println(message);
			throw new RuntimeException(message);
		}
		guichet.addObeserver(client);
		client.notifyNewConnectedGuichet(guichet);
		
		SAVED_GUICHETS.add(guichet);
	}

}
