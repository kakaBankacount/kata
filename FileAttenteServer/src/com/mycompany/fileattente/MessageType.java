package com.mycompany.fileattente;

public enum MessageType {
	
	//---- Message Client/Server
	//   Client => Server
	getTicket,
	getStats,
	//  Server => Client
	notifyClientTicketInfo,
	notifyClientDone,
	notifyClientGoToGuichet,
	suivant,
	rappel,
	precedent,
	//  Server => Guichet
	notifyGuichetReceiveClient
}
