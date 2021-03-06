package ioopm.mud.communication.messages.server;

import ioopm.mud.communication.messages.Message;
import ioopm.mud.communication.messages.MessageType;

/**
 * A message returned after a connection has requested to register a player in the server.
 *
 * @author TheGrandmother
 */
public class RegistrationReplyMessage extends Message {
	/**
	 * Constructs the message.
	 *
	 * @param receiver the name of the connection who wanted to register
	 * @param success  Weather or not the registration was successful
	 */
	public RegistrationReplyMessage(String receiver, boolean success, String message) {
		super(receiver, "server", MessageType.REGISTRATION_REPLY, null, String.valueOf(success), message);
	}
}
