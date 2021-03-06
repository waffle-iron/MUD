package ioopm.mud.communication.websocket;

import ioopm.mud.communication.Adapter;
import ioopm.mud.communication.messages.Message;
import ioopm.mud.communication.messages.MessageType;
import ioopm.mud.communication.messages.client.HeartBeatMessage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WSClientAdapter extends WebSocketClient implements Adapter {

	private static final Logger logger = Logger.getLogger(WSClientAdapter.class.getName());

	private final Queue<Message> inbox = new ArrayDeque<>();

	// Dummy locking object
	private final Object send_lock = new Object();

	public WSClientAdapter(final String username, URI serverURI) {
		super(serverURI);

		// Send heartbeats
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(HEARTBEAT_FREQUENCY);
					} catch(InterruptedException e) {
						logger.log(Level.SEVERE, e.getMessage(), e);
					}

					sendMessage(new HeartBeatMessage(username));
				}
			}
		}).start();
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		logger.info("Established a connection to the given URI :)");
	}

	@Override
	public void onMessage(String message) {
		logger.info("Received message: " + message);

		Message msg;
		try {
			msg = Message.deconstructTransmission(message);
		} catch(IllegalArgumentException e) {
			logger.warning("Could not translate answer from server! Message: " + message);
			return;
		}

		// Ignore heartbeats
		if(msg.getType() != MessageType.HEARTBEAT_REPLY) {
			inbox.add(msg);
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		logger.warning("The connection to the server has been closed!");
	}

	@Override
	public void onError(Exception ex) {
		logger.log(Level.WARNING, ex.getMessage(), ex);
	}

	@Override
	public Message poll() {
		return inbox.poll();
	}

	@Override
	public void sendMessage(Message m) {
		synchronized(send_lock) {
			this.send(m.getMessage());
		}
	}
}
