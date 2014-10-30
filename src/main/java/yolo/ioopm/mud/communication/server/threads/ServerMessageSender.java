package yolo.ioopm.mud.communication.server.threads;

import yolo.ioopm.mud.communication.Message;
import yolo.ioopm.mud.communication.server.ClientConnection;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerMessageSender extends Thread {

	private final ConcurrentHashMap<String, ClientConnection> connections;
	private final ConcurrentLinkedQueue<Message>      outbox;

	public ServerMessageSender(ConcurrentHashMap<String, ClientConnection> connections, ConcurrentLinkedQueue<Message> outbox) {
		this.connections = connections;
		this.outbox = outbox;
	}

	@Override
	public void run() {
		//TODO sleep the thread after every iteration over the box
	}
}