package com.redis.protocol.command;

import java.util.Optional;

public class PingCommand implements ICommand {
    private final String DELIMETER = "\r\n";
    public String message;
    public String error;
    public PingCommand(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(this.message);
    }

    public Optional<String> getError() {
        return Optional.ofNullable(this.error);
    }

    @Override
    public String generateResponse() {
        if (this.getMessage().isPresent()) {
            return "$"+this.getMessage().get().length() + DELIMETER + this.getMessage().get()+DELIMETER;
        } else if (this.getError().isPresent()) {
            return "-" + this.getError().get() + DELIMETER;
        } else {
            return "+PONG"+DELIMETER;
        }
    }
}
