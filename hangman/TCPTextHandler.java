package hangman;

import java.io.IOException;
import java.net.Socket;

import reactorapi.EventHandler;
import reactorapi.Handle;

public class TCPTextHandler implements EventHandler<String> {
	
	public TCPTextHandle tcpTextHandle = null;

	
	public boolean isFirstTime = true;

	
	public String playerName;

	public TCPTextHandler(Socket newSocket) throws IOException {
		tcpTextHandle = new TCPTextHandle(newSocket);
	}

	public Handle<String> getHandle() {
		return tcpTextHandle;
	}

	public void handleEvent(String playerInput) {
		// if no input then handler is removed else events are processed
		// method in HangmanServer

		if (playerInput == null) {
			HangmanServer.eventDispatcher.removeHandler(this);
		} else {
			HangmanServer.handleTcpTextEvent(playerInput, this);
		}
	}
}