package ru.sber.lru;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleLruCache<K, V> implements Cache<K, V> {
    private final Map<K, V> cache;

    public SimpleLruCache(final int maxEntries) {
        this.cache = new LinkedHashMap<K, V>(maxEntries, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxEntries;
            }
        };
    }

    public V put(K key, V value) {
        return cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public Map<K, V> getCacheMap() {
        return cache;
    }
}
