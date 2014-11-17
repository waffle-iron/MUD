package yolo.ioopm.mud.communication.server.runnables;

import yolo.ioopm.mud.communication.Message;
import yolo.ioopm.mud.communication.MessageType;
import yolo.ioopm.mud.communication.messages.internal.NewConnectionMessage;
import yolo.ioopm.mud.communication.messages.server.AuthenticationReplyMessage;
import yolo.ioopm.mud.communication.messages.server.RegistrationReplyMessage;
import yolo.ioopm.mud.communication.server.ClientConnection;
import yolo.ioopm.mud.game.GameEngine;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnectionVerifier implements Runnable {

	private static final Logger logger = Logger.getLogger(ServerConnectionVerifier.class.getName());

	private final ClientConnection              client;
	private final Map<String, ClientConnection> connections;
	private final Map<String, Long>             timestamps;
	private final Queue<Message>                inbox;

	public ServerConnectionVerifier(ClientConnection client, Map<String, ClientConnection> connections, Map<String, Long> timestamps, Queue<Message> inbox) {
		this.client = client;
		this.connections = connections;
		this.timestamps = timestamps;
		this.inbox = inbox;
		logger.fine("New ServerConnectionVerifier created!");
	}

	@Override
	public void run() {
		while(true) {
			String data;
			try {
				data = client.readLine();
			}
			catch(IOException e) {
				logger.log(Level.SEVERE, "IOException while listening for ClientRegistrationMessage!", e);
				return;
			}

			if(data == null) {
				logger.severe("Received null data when listening for ClientRegistrationMessage!");
				return;
			}

			Message msg = Message.deconstructTransmission(data);

			if(msg == null) {
				logger.severe("Failed to deconstruct transmission! Transmission: \"" + data + "\"");
				return;
			}

			String[] nouns = msg.getArguments();
			if(msg.getType() == MessageType.AUTHENTICATION && nouns != null && nouns.length == 2) {

				String username = nouns[0];
				String password = nouns[1];

				if(!connections.containsKey(username) && GameEngine.checkUsernamePassword(username, password)) {
					connections.put(username, client);
					timestamps.put(username, System.currentTimeMillis());

					logger.fine("Client successfully authenticated against server!");
					client.write(new AuthenticationReplyMessage(username, true).getMessage());

					inbox.offer(new NewConnectionMessage(username));

					// Terminate the thread.
					return;
				}
				else {
					client.write(new AuthenticationReplyMessage(username, false).getMessage());
					logger.fine("Client tried to authenticate with incorrect details, or username is already in use!");
					return;
				}
			}
			else if(msg.getType() == MessageType.REGISTRATION && nouns != null && nouns.length == 2) {

				String username = nouns[0];
				String password = nouns[1];

				logger.fine("User is attempting to register at server! Username: \"" + username + "\", Password: \"" + password + "\"");

				boolean success = false;
				if(GameEngine.register(username, password)) {
					logger.fine("Client successfully registered at server!");
					success = true;
				}

				client.write(new RegistrationReplyMessage(username, success).getMessage());
			}
			else if(msg.getType() == MessageType.HEARTBEAT) {
				// Do nothing
			}
			else {
				logger.warning("ServerConnectionVerifier received illegal message! Type: \"" + msg.getType() + "\"");
			}
		}
	}
}
