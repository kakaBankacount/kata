package com.mycompany.fileattente.guichet;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.fileattente.Ticket;
import com.mycompany.fileattente.client.IGuichetListener;

public class GuichetObservable implements IGuichetListener  {
	
	private List<IGuichetListener> obervers = new ArrayList<IGuichetListener>();
	
	public GuichetObservable() {
		
	}
	
	public void addObeserver(IGuichetListener observer) {
		obervers.add(observer);
	}
	
	public void removeObeserver(IGuichetListener observer) {
		obervers.remove(observer);
	}

	@Override
	public void notifyClientDone(Ticket ticket) {
		for (IGuichetListener client: obervers) {
			client.notifyClientDone(ticket);
		}
	}

	@Override
	public void notifyClientGoToGuichet(Ticket ticket, int numeroGuichet) {
		for (IGuichetListener client: obervers) {
			client.notifyClientGoToGuichet(ticket, numeroGuichet);
		}
	}

	@Override
	public void notifyNewConnectedGuichet(Guichet guichet) {
		for (IGuichetListener client: obervers) {
			client.notifyNewConnectedGuichet(guichet);
		}
	}

	@Override
	public void notifyRemovedGuichet(Guichet guichet) {
		for (IGuichetListener client: obervers) {
			client.notifyRemovedGuichet(guichet);
		}
	}
}
