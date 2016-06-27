package com.mycompany.fileattente.client;

import com.mycompany.fileattente.Ticket;
import com.mycompany.fileattente.guichet.Guichet;

public interface IGuichetListener {
	
	void notifyClientDone(Ticket ticket);
	
	void notifyClientGoToGuichet(Ticket ticket, int guichetNumero);
	
	void notifyNewConnectedGuichet(Guichet guichet);
	
	void notifyRemovedGuichet(Guichet guichet);
}
