package lib.finlay.core.collections;

import java.util.TreeMap;

public class TreeMapBuilder<K, V> {
	
	private TreeMap<K, V> map;
	
	public TreeMapBuilder() {
		this.map = new TreeMap<>();
	}

	public TreeMapBuilder<K, V> put(K key, V value) {
		if(key == null) {
			return this;
		}
		
		map.put(key, value);
		return this;
	}
	
	public TreeMap<K, V> build() {
		return this.map;
	}
	
}
