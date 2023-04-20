package com.redis.protocol.command;

import com.redis.store.Store;

import java.util.Optional;

public class GetCommand implements ICommand{

    private String key;
    private String message;
    private String error;

    public GetCommand(String key, String error) {
        this.key = key;
        this.error = error;
    }

    @Override
    public Optional<String> getMessage() {
        return Optional.ofNullable(this.message);
    }
    private void execute() {
        Optional<String> value = Store.getInstance().get(this.key);
        this.message = value.orElse("");
    }
    @Override
    public Optional<String> getError() {
        return Optional.ofNullable(this.error);
    }

    @Override
    public String generateResponse() {
        if (this.getError().isPresent()) {
            return "-" + this.getError().get() + DELIMITER;
        } else {
            this.execute();
            if (this.getMessage().isPresent() && this.getMessage().get().length() > 0) {
                return "$" + this.getMessage().get().length() + DELIMITER + this.getMessage().get() + DELIMITER;
            } else {
                return "$-1" + DELIMITER;
            }
        }
    }
}
