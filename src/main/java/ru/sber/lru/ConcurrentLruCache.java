package ru.sber.lru;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConcurrentLruCache<K, V> implements Cache<K, V> {
    private final Map<K, V> cache;

    public ConcurrentLruCache(final int maxEntries) {
        this.cache = new LinkedHashMap<K, V>(maxEntries, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxEntries;
            }
        };
    }

    public V put(K key, V value) {
        synchronized (cache) {
            return cache.put(key, value);
        }
    }

    public V get(K key) {
        synchronized (cache) {
            return cache.get(key);
        }
    }

    public Map<K, V> getCacheMap() {
        return cache;
    }
}
