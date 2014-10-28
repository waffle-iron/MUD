/**
 * 
 */
package yolo.ioopm.mud.communication;


import yolo.ioopm.mud.server.Server;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 		This interface acts as a mediator and passes messages from the player client to the server.
 *
 */
public interface Adapter {

    ConcurrentLinkedQueue<Message> outgoing  = null;
    ConcurrentLinkedQueue<Message> incomming = null;


    public void establishLink(Server server) throws CommunicationError;

    public void sendMessage(Message message) throws CommunicationError;

    public Message pollForMessage() throws CommunicationError;


    @SuppressWarnings("serial")
	class CommunicationError extends Exception{
		
		public CommunicationError(){
			super();
		}
		
		public CommunicationError(String message){
			super(message);
		}
	}
	
}