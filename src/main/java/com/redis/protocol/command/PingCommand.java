package com.redis.protocol.command;

import java.util.Optional;

public class PingCommand implements ICommand {
    public String message;
    public String error;

    /**
     * @param message
     * @param error
     */
    public PingCommand(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(this.message);
    }

    /**
     * @return
     */
    public Optional<String> getError() {
        return Optional.ofNullable(this.error);
    }

    /**
     * @return
     */
    @Override
    public String generateResponse() {
        if (this.getMessage().isPresent()) {
            return "$"+this.getMessage().get().length() + DELIMITER + this.getMessage().get() + DELIMITER;
        } else if (this.getError().isPresent()) {
            return "-" + this.getError().get() + DELIMITER;
        } else {
            return "+PONG" + DELIMITER;
        }
    }
}
