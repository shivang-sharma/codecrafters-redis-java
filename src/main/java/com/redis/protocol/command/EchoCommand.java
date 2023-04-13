package com.redis.protocol.command;

import java.util.Optional;

public class EchoCommand implements ICommand {
    public String message;
    public String error;
    public EchoCommand(String message, String error) {
        this.message = message;
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

    @Override
    public String generateResponse() {
        String DELIMITER = "\r\n";
        if (this.getMessage().isPresent()) {
            return "$"+this.getMessage().get().length() + DELIMITER + this.getMessage().get()+ DELIMITER;
        } else {
            return "-" + this.getError().get() + DELIMITER;
        }
    }
}
