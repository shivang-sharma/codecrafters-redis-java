package com.redis.store;

import com.redis.store.interfaces.IStore;

import java.util.Optional;

public class Store implements IStore {
    private static Store instance = new Store();

    private Store() {}

    @Override
    public boolean set(String key, String value) {
        return MapWrapper.set(key, value);
    }

    @Override
    public boolean set(String key, String value, String timeUnit, String expiryTime) {
        return MapWrapper.set(key, value, timeUnit, expiryTime);
    }

    @Override
    public Optional<String> get(String key) {
        return MapWrapper.get(key);
    }

    public static Store getInstance() {
        return instance;
    }
}
