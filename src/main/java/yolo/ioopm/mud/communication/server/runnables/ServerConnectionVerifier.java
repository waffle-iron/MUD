package yolo.ioopm.mud.communication.server.runnables;

import yolo.ioopm.mud.communication.Message;
import yolo.ioopm.mud.communication.MessageType;
import yolo.ioopm.mud.communication.messages.server.ClientIncorrectLoginMessage;
import yolo.ioopm.mud.communication.messages.server.ClientSuccessfullLoginMessage;
import yolo.ioopm.mud.communication.server.ClientConnection;
import yolo.ioopm.mud.game.GameEngine;

import java.io.IOException;
import java.util.Map;

public class ServerConnectionVerifier implements Runnable {

	private final ClientConnection              client;
	private final Map<String, ClientConnection> connections;
	private final Map<String, Long>             timestamps;

	public ServerConnectionVerifier(ClientConnection client, Map<String, ClientConnection> connections, Map<String, Long> timestamps) {
		this.client = client;
		this.connections = connections;
		this.timestamps = timestamps;
	}

	@Override
	public void run() {
		while(true) {
			String data;
			try {
				data = client.readLine();
			}
			catch(IOException e) {
				System.out.println("IOException while listening for ClientRegistrationMessage!");
				e.printStackTrace();
				return;
			}

			if(data == null) {
				System.out.println("Received null data when listening for ClientRegistrationMessage!");
				return;
			}

			Message msg = Message.deconstructTransmission(data);

			if(msg == null) {
				System.out.println("Failed to deconstruct transmission! Transmission: \"" + data + "\"");
				return;
			}

			String[] nouns = msg.getArguments();
			if(msg.getType() == MessageType.AUTHENTICATION && nouns != null && nouns.length == 2) {

				String username = nouns[0];
				String password = nouns[1];

				if(GameEngine.checkUsernamePassword(username, password)) {
					connections.put(username, client);
					timestamps.put(username, System.currentTimeMillis());

					System.out.println("Client successfully authenticated against server!");
					client.write(new ClientSuccessfullLoginMessage(username).getMessage());

					// Terminate the thread.
					return;
				}
				else {
					client.write(new ClientIncorrectLoginMessage(username).getMessage());
					System.out.println("Client tried to authenticate with incorrect details!");
				}
			}
			else if(msg.getType() == MessageType.HEARTBEAT) {
				// Do nothing
			}
			else {
				System.out.println("ServerConnectionVerifier received illegal message! Type: \"" + msg.getType() + "\"");
			}
		}
	}
}
