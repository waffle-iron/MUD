package yolo.ioopm.mud.communication.messages.server;

import yolo.ioopm.mud.communication.Message;
import yolo.ioopm.mud.communication.MessageType;
/**
 * 
 * Messages for notifications i.e broadcasts.
 * 
 * @author TheGrandmother
 *
 */
public class NotifactionMesssage extends Message {

	/**
	 * 
	 * Creates a notification message with the sender set to server and the message type set to{@literal MessageType#NOTIFICATION}.
	 * 
	 * @param receiver who the message is no be sent to
	 * @param message the content of the message
	 */
	public NotifactionMesssage(String receiver, String message) {
		super(receiver, "server", MessageType.NOTIFICATION, null, new String[]{message});
		// TODO Auto-generated constructor stub
	}

}
