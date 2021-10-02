package ru.sber.lru;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CacheTest {
    private final int cacheSize = 3;
    private final int cycles = 130;
    private final List<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    @Test
    void simpleLruCacheWithOneThreadsTest() {
        verifyResults(oneThreadTest(new SimpleLruCache<>(cacheSize)));

    }

    @Test
    void concurrentLruCacheWithOneThreadsTest() {
        verifyResults(oneThreadTest(new ConcurrentLruCache<>(cacheSize)));
    }

    @Test
    void simpleLruCacheWithManyThreadsTest() {
        verifyResults(concurrentTest(new SimpleLruCache<>(cacheSize)));

    }

    @Test
    void concurrentLruCacheWithManyThreadsTest() {
        verifyResults(concurrentTest(new ConcurrentLruCache<>(cacheSize)));
    }

    void verifyResults(Map<Integer, Integer> cacheMap) {
        assertEquals(cacheSize, cacheMap.size());
        cacheMap.entrySet().forEach( entry -> {
            assertNotNull(entry);
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
            assertEquals(entry.getKey(), entry.getValue());
        });
    }

    private Map<Integer, Integer> oneThreadTest(Cache<Integer, Integer> cache) {
        for (int i = 0; i < cycles; i++) {
            Integer key = values.get(ThreadLocalRandom.current().nextInt(values.size()));
            Integer value = cache.get(key);
            if (value == null) {
                cache.put(key, key);
            }
        }
        return cache.getCacheMap();
    }

    private Map<Integer, Integer> concurrentTest(Cache<Integer, Integer> cache) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < cycles; i++) {
            executorService.submit(() -> {
                Integer key = values.get(ThreadLocalRandom.current().nextInt(values.size()));
                Integer value = cache.get(key);
                if (value == null) {
                    cache.put(key, key);
                }
            });
        }
        try {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cache.getCacheMap();
    }


}
