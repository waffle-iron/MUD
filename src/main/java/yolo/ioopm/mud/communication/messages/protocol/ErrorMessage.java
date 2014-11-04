package yolo.ioopm.mud.communication.messages.protocol;

import yolo.ioopm.mud.communication.messages.OutgoingMessage;

public class ErrorMessage extends OutgoingMessage {

	public ErrorMessage(String reciever, String error_message) {
		super(reciever, "server", "echo", System.currentTimeMillis(), new String[] {error_message});
		// TODO Auto-generated constructor stub
	}

}