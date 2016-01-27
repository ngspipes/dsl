package utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;

public class Event<T> {

	private final Map<Object, Consumer<T>> listeners;

	public Event() {
		this.listeners = new HashMap<>();
	}

	public void addListner(Consumer<T> listener){
		listeners.put(listener, listener);
	}

	public void removeListner(Consumer<T> listener) {
		listeners.remove(listener);
	}

	public void addListner(Runnable listener){
		Consumer<T> consumer = (t)->listener.run();
		listeners.put(listener, consumer);
	}

	public void removeListner(Runnable listener){
		listeners.remove(listener);
	}

	public void trigger(T t){
		for(Object key : new LinkedList<>(listeners.keySet()))
			listeners.get(key).accept(t);
	}
}
