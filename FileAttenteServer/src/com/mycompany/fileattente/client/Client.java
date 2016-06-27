package com.mycompany.fileattente.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.mycompany.fileattente.Message;
import com.mycompany.fileattente.MessageType;
import com.mycompany.fileattente.QueueByServiceProvider;
import com.mycompany.fileattente.Ticket;
import com.mycompany.fileattente.guichet.Guichet;

public class Client implements Runnable, IGuichetListener {
	private Socket socket;
	private PrintWriter clientSocketOut;
	private BufferedReader clientSocketIn;
	
	private final static int WAIT_TIME = 5;
	
	private Map<String, Integer> countServciceByGuichet = new HashMap<>();
	
	public Client(Socket s){
		socket = s;
	}
	
	public void run() {
		try {
			
			clientSocketOut = new PrintWriter(socket.getOutputStream());
			clientSocketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							String message = clientSocketIn.readLine();
							System.out.println("Message from client:" + message);
							readFromClient(message);
						} catch (IOException e) {
							e.printStackTrace();
							if (clientSocketIn != null) {
								try {
									clientSocketIn.close();
									break;
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}
					}
				}

				
			}).start();
			
		} catch (IOException e) {
			System.err.println("Le serveur distant s'est déconnecté !");
		}
	}

	private void readFromClient(String message) {
		if (message == null || message.isEmpty()) {
			return;
		}
		try {
		Message socketMessage = Message.newMessage(message);

		if (socketMessage == null) {
			System.out.println("Unknown message from client: " + message);
			return;
		}

		switch (socketMessage.getType()) {
			case getTicket:
			    createTicket(socketMessage);
			break;
			case getStats:
			    getStats(socketMessage);
			break;
		default:
			break;
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getStats(Message socketMessage) {
		String stat = QueueByServiceProvider.getStats();
		this.clientSocketOut.print("STATS Queue: \r\n" + stat + "\r\n");
		this.clientSocketOut.print("STATS Guichet: \r\n");
		for (Guichet g : ClientSocketConnexion.SAVED_GUICHETS) {
			this.clientSocketOut.print("Guichet " 
					+ g.getNumeroGuichet() + "/" 
					+ g.getService() + ": Number of treated ticket=" + g.getTicketPile().size() + "\r\n");
		}
		
		this.clientSocketOut.flush();
	}

	private void createTicket(Message message) {
		Ticket ticket = new Ticket(message.getParams().get(0));
		BlockingQueue<Ticket> queue = QueueByServiceProvider.getQueue(ticket.getService());
		if (queue == null) {
			throw new RuntimeException("queue doesn't exist for the service " + ticket.getService());
		}
		try {
			queue.put(ticket);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int ticketNumber = queue.size();
		Integer numberGuichetByService = countServciceByGuichet.get(ticket.getService());
		if (numberGuichetByService == null) {
			System.out.println("No guichet is open. Create a ticket and put it in queue.");
			numberGuichetByService = 1;
		}
		int waitTime = (ticketNumber * WAIT_TIME) / numberGuichetByService;
		
		Message messageToClient = new Message(MessageType.notifyClientTicketInfo,
				Arrays.asList(String.valueOf(ticket.getNumero()), String.valueOf(waitTime)));		
		
		this.clientSocketOut.println(messageToClient.serialize());
		this.clientSocketOut.flush();
	}

	@Override
	public void notifyClientDone(Ticket ticket) {
		Message message = new Message(MessageType.notifyClientDone,
					Arrays.asList(String.valueOf(ticket.getNumero())));
		clientSocketOut.println(message.serialize());
		clientSocketOut.flush();
	}

	@Override
	public void notifyClientGoToGuichet(Ticket ticket, int guichetNum) {
		Message message = new Message(MessageType.notifyClientGoToGuichet,
				Arrays.asList(String.valueOf(ticket.getNumero()), String.valueOf(guichetNum)));
		clientSocketOut.println(message.serialize());
		clientSocketOut.flush();
	}

	@Override
	public void notifyNewConnectedGuichet(Guichet guichet) {
		Integer count = countServciceByGuichet.get(guichet.getService());
		if (count == null) {
			countServciceByGuichet.put(guichet.getService(), 1);
		} else {
			countServciceByGuichet.put(guichet.getService(), count++);
		}
	}

	@Override
	public void notifyRemovedGuichet(Guichet guichet) {
		Integer count = countServciceByGuichet.get(guichet.getService());
		if (count != null && count > 0) {		
			countServciceByGuichet.put(guichet.getService(), count--);
		}
	}

	
}
