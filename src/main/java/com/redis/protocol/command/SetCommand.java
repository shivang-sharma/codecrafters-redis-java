package com.redis.protocol.command;

import com.redis.store.Store;

import java.util.Optional;

public class SetCommand implements ICommand {
    private final String key;
    private final String value;
    private String message;
    private String error;
    public SetCommand(String key, String value, String error) {
        this.key = key;
        this.value = value;
        this.error = error;
    }
    @Override
    public Optional<String> getMessage() {
        return Optional.ofNullable(this.message);
    }

    @Override
    public Optional<String> getError() {
        return Optional.ofNullable(this.error);
    }

    private void execute() {
        boolean success = Store.getInstance().set(this.key, this.value);
        if (success) {
            this.message = "OK";
        } else {
            this.error = "ERR error";
        }
    }
    @Override
    public String generateResponse() {
        if (this.getError().isPresent()) {
            return "-" + this.getError().get() + DELIMITER;
        } else {
            this.execute();
            if (this.getMessage().isPresent()) {
                return "+" + this.getMessage().get() + DELIMITER;
            } else {
                return "-" + this.getError().get() + DELIMITER;
            }
        }
    }
}
