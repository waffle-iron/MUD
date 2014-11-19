package yolo.ioopm.mud.communication.client.runnables;

import yolo.ioopm.mud.communication.Adapter;
import yolo.ioopm.mud.communication.Message;
import yolo.ioopm.mud.communication.messages.client.HeartBeatMessage;

import java.util.Queue;
import java.util.logging.Logger;

public class ClientHeartbeatSender implements Runnable {

	private static final Logger logger = Logger.getLogger(ClientHeartbeatSender.class.getName());

	private final Queue<Message> outbox;
	private final String         USERNAME;

	/**
	 * Adds HeartBeatMessage's to the given outbox with the given username.
	 * @param outbox the outbox to put the messages in
	 * @param username the username to sign the messages with
	 */
	public ClientHeartbeatSender(Queue<Message> outbox, String username) {
		this.outbox = outbox;
		this.USERNAME = username;
		logger.fine("ClientHeartbeatSender created!");
	}

	@Override
	public void run() {
		while(true) {

			try {
				Thread.sleep(Adapter.HEARTBEAT_FREQUENCY);
			}
			catch(InterruptedException e) {
				//TODO unhandled exception
				e.printStackTrace();
			}

			outbox.offer(new HeartBeatMessage(USERNAME));
		}
	}
}
