package reactor;

import reactorapi.*;

public class WorkerThread<T> extends Thread {
	private final EventHandler<T> handler;
	private final BlockingEventQueue<Object> queue;
	private volatile boolean isRunning = true;
	
	// Additional fields are allowed.

	public WorkerThread(EventHandler<T> eh, BlockingEventQueue<Object> q) {
		handler = eh;
		queue = q;
	}

	public void run() {
		while ( isRunning ) {
			Object eventTmp = handler.getHandle().read();
			if (eventTmp == null) {
				cancelThread();
			}
			Event<Object> event = new Event(eventTmp, handler);
	
			try {
				queue.put(event);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				cancelThread();
			}
		}
	}

	public void cancelThread() {
		isRunning = false;
	}
}