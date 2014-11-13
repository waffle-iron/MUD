package yolo.ioopm.mud.communication.client.runnables;

import yolo.ioopm.mud.communication.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Queue;

public class ClientMessageListener implements Runnable {

	private final BufferedReader br;
	private final Queue<Message> inbox;

	public ClientMessageListener(BufferedReader br, Queue<Message> inbox) {
		this.br = br;
		this.inbox = inbox;
	}

	@Override
	public void run() {

		// There is no need for this thread to sleep, br.readLine() is a blocking method.

		while(true) {
			String data;
			synchronized(br) {
				try {
					data = br.readLine();
				}
				catch(IOException e) {
					System.out.println("IOException when reading from BufferedReader! Terminating thread!");
					e.printStackTrace();
					return;
				}
			}

			if(data == null) {
				System.out.println("Data was null after reading BufferedReader! Did the connection close? Terminating thread!");
				return;
			}

			Message msg = Message.deconstructTransmission(data);

			if(msg != null) {
				System.out.println("Received msg: \"" + msg.getMessage() + "\"");
				inbox.offer(msg);
			}
			else {
				System.out.println("Failed to deconstruct transmission! Transmission: \"" + data + "\"");
				continue;
			}
		}
	}
}
