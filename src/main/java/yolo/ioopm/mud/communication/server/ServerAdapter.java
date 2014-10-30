package yolo.ioopm.mud.communication.server;

import yolo.ioopm.mud.communication.Adapter;
import yolo.ioopm.mud.communication.server.threads.ServerConnectionListener;
import yolo.ioopm.mud.communication.server.threads.ServerMessageListener;
import yolo.ioopm.mud.communication.server.threads.ServerMessageSender;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

public class ServerAdapter extends Adapter {

	private final ConcurrentHashMap<String, ClientConnection> connections = new ConcurrentHashMap<>();

	public ServerAdapter(int port) throws IOException {

		// Async thread - Listens for new connections and adds them to connections.
		new ServerConnectionListener(new ServerSocket(port), connections).start();

		// Async thread - Listens for new messages from the clients.
		new ServerMessageListener(connections, inbox).start();

		// Async thread - Sends the messages that are currently in the outbox.
		new ServerMessageSender(connections, outbox).start();
	}
}