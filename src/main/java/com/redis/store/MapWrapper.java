package com.redis.store;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

class MapWrapper {
    private static final ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>(50);
    private static final ConcurrentHashMap<String, String> concurrentExpiryHashMap = new ConcurrentHashMap<>(50);

    /**
     * @param key
     * @param value
     * @return
     */
    public static boolean set(String key, String value) {
        try {
            concurrentHashMap.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param key
     * @param value
     * @param timeUnit
     * @param expiryTime
     * @return
     */
    public static boolean set(String key, String value, String timeUnit, String expiryTime) {
        try {
            concurrentHashMap.put(key, value);
            if (timeUnit.equalsIgnoreCase("SECONDS")) {
                expiryTime = String.valueOf(Instant.now().toEpochMilli() + TimeUnit.SECONDS.toMillis(Long.parseLong(expiryTime)));
            } else if (timeUnit.equalsIgnoreCase("MILLISECONDS")) {
                expiryTime = String.valueOf(Instant.now().toEpochMilli() + Long.parseLong(expiryTime));
            }
            concurrentExpiryHashMap.put(key, expiryTime);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param key
     * @return
     */
    public static Optional<String> get(String key) {
        if (concurrentExpiryHashMap.containsKey(key)) {
            long currentTime = Instant.now().toEpochMilli();
            if (currentTime > Long.parseLong(concurrentExpiryHashMap.get(key))) {
                concurrentHashMap.remove(key);
                concurrentExpiryHashMap.remove(key);
                return Optional.empty();
            }
        }
        return Optional.ofNullable(concurrentHashMap.get(key));
    }
}
