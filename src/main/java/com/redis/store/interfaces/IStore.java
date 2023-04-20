package com.redis.store.interfaces;

import java.util.Optional;

public interface IStore {
    boolean set(String key, String value);
    Optional<String> get(String key);
}
