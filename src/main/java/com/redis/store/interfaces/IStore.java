package com.redis.store.interfaces;

import java.util.Optional;

public interface IStore {
    boolean set(String key, String value);
    boolean set(String key, String value, String timeUnit, String expiryTime);
    Optional<String> get(String key);
}
