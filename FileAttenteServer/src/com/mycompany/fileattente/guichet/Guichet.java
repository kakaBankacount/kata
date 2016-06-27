package com.mycompany.fileattente.guichet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

import com.mycompany.fileattente.Message;
import com.mycompany.fileattente.MessageType;
import com.mycompany.fileattente.QueueByServiceProvider;
import com.mycompany.fileattente.Ticket;

public class Guichet extends GuichetObservable implements Runnable{

	private Socket socket;
	private PrintWriter guichetSocketOut;
	private BufferedReader guichetSocketIn;
	
	static int COUNTER = 0;
	private int numeroGuichet;
	private String service;
	private volatile boolean available;
	private volatile boolean ready;
	private BlockingQueue<Ticket> serviceQueue;
	
	private LinkedList<Ticket> ticketPile = new TicketPile<>();
	private int currentIndex = 0;
	
	public Guichet(Socket socket, String service){
		
		this.socket = socket;
		this.service = service;
		numeroGuichet = ++COUNTER;	
		serviceQueue = QueueByServiceProvider.getQueue(service);
		this.available = true;
		
		System.out.println("Create Guichet n° " + numeroGuichet + " with service: " + service);
	}
	
	private Runnable waitingNewTicketRunnable = new Runnable() {

		@Override
		public void run() {
			while (ready) {
				try {
					Ticket ticket = serviceQueue.take();
					Guichet.this.ready = false;
					Guichet.this.ticketPile.addFirst(ticket);
					onTicketUpdateToGuichet(ticket);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	};
	
	
	public int getNumeroGuichet(){
		return numeroGuichet;
	}
	
	public String getService(){
		return service;
	}

	@Override
	public void run() {
		try {
			guichetSocketOut = new PrintWriter(socket.getOutputStream());
			guichetSocketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			guichetSocketOut.println("Guichet n°: " + numeroGuichet);
			guichetSocketOut.flush();
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (available) {
						try {
							String message = guichetSocketIn.readLine();
							System.out.println("Message from guichet " + numeroGuichet + "/" + service + ": " + message);
							readFromGuichet(message);
						} catch (IOException e) {
							e.printStackTrace();
							if (guichetSocketIn != null) {
								try {
									guichetSocketIn.close();
									break;
								} catch (IOException e1) {
									// TODO Auto-generated catch block
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
	
	private void readFromGuichet(String message) {
		try {

			Message socketMessage = Message.newMessage(message);

			if (socketMessage == null) {
				throw new IllegalArgumentException("message from guichet can be interpreted: " + message);
			}

			switch (socketMessage.getType()) {
			case suivant:
				suivant();
				break;
			case rappel:
				rappel();
				break;
			case precedent:
				precedent();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void suivant() {
		if (currentIndex == 0) {
			if (this.ready) {
				System.out.println("Already kown to wait next ticket");
				return;
			}
			
			System.out.println("Wait next ticket...");
			
			this.ready = true;
			if (this.ticketPile.size() > 0) {
				notifyClientDone(ticketPile.get(0));
			}
		    new Thread(waitingNewTicketRunnable).start();
			
		} else {
			
			Ticket suivant = ticketPile.get(--currentIndex);
			onTicketUpdateToGuichet(suivant);
		}
	}
	
	private void onTicketUpdateToGuichet(Ticket update) { 
		System.out.println("Ticket " + update + " au guichet " + numeroGuichet);
		
		Message message = new Message(MessageType.notifyGuichetReceiveClient, 
				Arrays.asList(String.valueOf(update.getNumero())));

		guichetSocketOut.println(message.serialize());
		guichetSocketOut.flush();
		
		notifyClientGoToGuichet(update, this.numeroGuichet);
	}
	
	public void rappel() {
		Ticket rappel = ticketPile.get(currentIndex);
		onTicketUpdateToGuichet(rappel);
	}
	
	public void precedent() {
		if (currentIndex < ticketPile.size() - 1) {
			currentIndex++;
			Ticket precedent = ticketPile.get(currentIndex);
			onTicketUpdateToGuichet(precedent);
		} else {
			System.out.println("Plus de precedent");
		}
		
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}

	public LinkedList<Ticket> getTicketPile() {
		return ticketPile;
	}
}

class TicketPile<Ticket> extends LinkedList<Ticket> {
	
	//public final static int MAX_SIZE = 10;
	
	@Override
	public boolean add(Ticket ticket) {
		return false; 
	}
	@Override
	public void addFirst(Ticket ticket) {
//		if (size() >= MAX_SIZE) {
//			removeLast();
//		}
		super.addFirst(ticket);
	}
	@Override
	public void addLast(Ticket ticket) {
		
	}
}
