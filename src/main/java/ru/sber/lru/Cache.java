package ru.sber.lru;

import java.util.Map;

public interface Cache<K, V> {
    V get(K key);
    V put(K key, V value);
    Map<K, V> getCacheMap();
}
