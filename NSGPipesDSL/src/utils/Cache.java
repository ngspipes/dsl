package utils;

import java.util.HashMap;
import java.util.Map;

public class Cache<K, V> {
	
	private final Map<K, V> cache = new HashMap<>();
	private final Object lock = new Object();
	
	
	public V get(K key){
		synchronized (lock) {
			return (cache.containsKey(key))? cache.get(key) : null;		
		}
	}
	
	public void remove(K key){
		synchronized (lock) {
			if(cache.containsKey(key))
				cache.remove(key);
		}
	}
	
	public void add(K key, V value){
		synchronized (lock) {
			cache.put(key, value);
		}
	}

}
