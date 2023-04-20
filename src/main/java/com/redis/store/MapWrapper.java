package com.redis.store;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class MapWrapper {
    private static final ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<String, String>(50);

    public static boolean set(String key, String value) {
        try {
            concurrentHashMap.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Optional<String> get(String key) {
        return Optional.ofNullable(concurrentHashMap.get(key));
    }
}
