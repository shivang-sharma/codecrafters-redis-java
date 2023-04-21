package com.redis.protocol.command;

import java.util.Optional;

public class EchoCommand implements ICommand {
    public String message;
    public String error;

    /**
     * @param message
     * @param error
     */
    public EchoCommand(String message, String error) {
        this.message = message;
        this.error = error;
    }

    /**
     * @return
     */
    @Override
    public Optional<String> getMessage() {
        return Optional.ofNullable(this.message);
    }

    /**
     * @return
     */
    @Override
    public Optional<String> getError() {
        return Optional.ofNullable(this.error);
    }

    /**
     * @return
     */
    @Override
    public String generateResponse() {
        if (this.getMessage().isPresent()) {
            return "$"+this.getMessage().get().length() + DELIMITER + this.getMessage().get()+ DELIMITER;
        } else {
            return "-" + this.getError().get() + DELIMITER;
        }
    }
}
