package utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BiEvent<T, R> {

	private final Map<Object, BiConsumer<T, R>> listeners;

	public BiEvent() {
		this.listeners = new HashMap<>();
	}

	public void addListner(BiConsumer<T, R> listener){
		listeners.put(listener, listener);
	}

	public void removeListner(Consumer<T> listener) {
		listeners.remove(listener);
	}

	public void addListner(Runnable listener){
		BiConsumer<T, R> consumer = (t, r)->listener.run();
		listeners.put(listener, consumer);
	}

	public void removeListner(Runnable listener){
		listeners.remove(listener);
	}

	public void trigger(T t, R r){
		for(Object key : new LinkedList<>(listeners.keySet()))
			listeners.get(key).accept(t, r);
	}
}
