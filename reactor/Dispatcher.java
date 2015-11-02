package reactor;

import java.util.ArrayList;

import hangman.AcceptHandle;
import hangman.TCPTextHandle;
import reactorapi.*;

public class Dispatcher {
	
	private BlockingEventQueue<Object> eventsQueue;
	private ArrayList<EventHandler> handlers;
	private WorkerThread wt;
	
	public Dispatcher() {
		eventsQueue = new BlockingEventQueue<Object>(10);
		handlers = new ArrayList<EventHandler>();
	}

	public Dispatcher(int capacity) {
		eventsQueue = new BlockingEventQueue<Object>(capacity);
		handlers = new ArrayList<EventHandler>();
	}

	public void handleEvents() throws InterruptedException {
		Event<?> event;
		while ( !handlers.isEmpty() ) {
			event = select();
			// if an event is registered
			if ( handlers.contains(event.getHandler()) ) {
				event.handle();
			}
		}
	}

	public Event<?> select() throws InterruptedException {
		return eventsQueue.get();
	}

	public void addHandler(EventHandler<?> h) {
		handlers.add(h);
		
		wt = new WorkerThread(h, eventsQueue);
		wt.start();		
	}

	public void removeHandler(EventHandler<?> h) {
		handlers.remove(h);
	}

	// Add methods and fields as needed.
	
	// this function receives messages from hangmanserver and prints them to
	// player consoles
	public void BroadCastMessage(String message) {
		for (Object eventHandler : handlers) {
			try {
				((TCPTextHandle) ((EventHandler) eventHandler).getHandle())
						.write(message);
			} catch (Exception ex) {
			}
		}
	}

	// this function is initiated when the game is over. It closes all the
	// handles of tcptexthandle and accepthandle and thus removes all the event
	// handlers
	public void ClearHandlers() {
		for (Object eventHandler : handlers) {
			try {
				((TCPTextHandle) ((EventHandler) eventHandler).getHandle())
						.close();
			} catch (Exception ex) {
				((AcceptHandle) ((EventHandler) eventHandler).getHandle())
						.close();
			}
		}
		handlers.clear();
	}
	
}
