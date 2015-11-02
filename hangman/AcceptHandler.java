package hangman;

import java.io.IOException;
import java.net.Socket;
import reactorapi.EventHandler;
import reactorapi.Handle;

public class AcceptHandler implements EventHandler<Socket> {
	
	private AcceptHandle acceptHandle = null;

	public AcceptHandler() throws IOException {
		acceptHandle = new AcceptHandle();
	}

	public Handle<Socket> getHandle() {
		return acceptHandle;
	}


	public void handleEvent(Socket newSocket) {
		if (newSocket == null) {
			HangmanServer.eventDispatcher.removeHandler(this);
		} else {
			try {
				HangmanServer.eventDispatcher.addHandler(new TCPTextHandler(newSocket));
			} catch (IOException e) {
			}
		}
	}

}