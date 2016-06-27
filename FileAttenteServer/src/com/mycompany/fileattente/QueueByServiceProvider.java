package com.mycompany.fileattente;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class QueueByServiceProvider {

	public final static int CAPACITY = 10;

	private static Map<String, BlockingQueue<Ticket>> FIFO_BY_SERVICE = new HashMap<>();

	public synchronized static BlockingQueue<Ticket> getQueue(String service) {
		BlockingQueue<Ticket> result = FIFO_BY_SERVICE.get(service);
		if (result == null) {
			result = new PriorityBlockingQueue<Ticket>(CAPACITY, new TicketComarator());
			FIFO_BY_SERVICE.put(service, result);
		}
		return result;
	}

	public static String getStats() {
		
		 Set<Map.Entry<String, BlockingQueue<Ticket>>> entrySet = FIFO_BY_SERVICE.entrySet();
	
		 StringBuilder sb = new StringBuilder();
		 for (Map.Entry<String, BlockingQueue<Ticket>> entry : entrySet) {
			 sb.append("Queue service: " + entry.getKey());
			 sb.append(" :Ticket number: " + entry.getValue().size()).append("\r\n");
			 
			 Iterator<Ticket> iter = entry.getValue().iterator();
			 Ticket ticket;
			 sb.append("Tickets: ");
			 while(iter.hasNext()) {
				 ticket = iter.next();
				 sb.append(ticket).append(" ");
			 }
			 sb.append("\r\n");
		 }
		 return sb.toString();
	}

}

class TicketComarator implements Comparator<Ticket> {

	@Override
	public int compare(Ticket t1, Ticket t2) {

		if (t1.isPrivilegePriority() && !t2.isPrivilegePriority()) {
			return -1;

		} else if (!t1.isPrivilegePriority() && t2.isPrivilegePriority()) {
			return 1;

		} else if (t1.isPrivilegePriority() == t2.isPrivilegePriority()) {

			if (t1.getNumero() < t2.getNumero()) {
				return -1;
			} else if (t1.getNumero() == t2.getNumero()) {
				return 0;
			} else if (t1.getNumero() > t2.getNumero()) {
				return 1;
			}
		}

		return 0;
	}

}