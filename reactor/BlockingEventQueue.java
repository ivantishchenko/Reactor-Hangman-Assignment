package reactor;

import reactorapi.BlockingQueue;

import java.util.ArrayList;
import java.util.List;

public class BlockingEventQueue<T> implements BlockingQueue<Event<? extends T>> {
	
	private ArrayList<Event<? extends T>> data;
	private final int maxSize;
	
	public BlockingEventQueue(int capacity) {
		data = new ArrayList<Event<? extends T>>();
		this.maxSize = capacity;
	}

	public int getSize() {
		synchronized (data) {
			return data.size();
		}
	}

	public int getCapacity() {
		return maxSize;
	}

	public Event<? extends T> get() throws InterruptedException {
		Event<? extends T> item = null;
		synchronized (data) {
			while ( data.isEmpty() ) {
				data.wait();
			}
			item = data.remove(0);
			data.notifyAll();
			return item;
		}
	}

	public List<Event<? extends T>> getAll(){
		synchronized (data) {
			ArrayList<Event<? extends T>> buf = new ArrayList<Event<? extends T>>(data);
			data.clear();
			data.notifyAll();
			return buf;
		}
	}

	public void put(Event<? extends T> event) throws InterruptedException {
		synchronized (data) {
			while ( data.size() >= maxSize ) {
				data.wait();
			}
			data.add(event);;
			data.notifyAll();
		}
	}

	// Add other methods and variables here as needed.
}