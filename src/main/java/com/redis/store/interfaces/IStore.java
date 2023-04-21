package com.redis.store.interfaces;

import java.util.Optional;

public interface IStore {
    /**
     * @param key
     * @param value
     * @return
     */
    boolean set(String key, String value);

    /**
     * @param key
     * @param value
     * @param timeUnit
     * @param expiryTime
     * @return
     */
    boolean set(String key, String value, String timeUnit, String expiryTime);

    /**
     * @param key
     * @return
     */
    Optional<String> get(String key);
}
