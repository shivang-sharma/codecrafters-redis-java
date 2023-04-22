package com.redis.protocol.command;

import java.util.Optional;

public class UnknownCommand implements ICommand{
    private String error = "ERR unknown command '%s', with args beginning with:";
    private String command;
    private String[] args;

    public UnknownCommand(String command, String[] args) {
        this.command = command;
        this.args = args;
    }


    @Override
    public Optional<String> getMessage() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getError() {
        return Optional.empty();
    }

    @Override
    public String generateResponse() {
        error = error.formatted(this.command);
        StringBuffer errorBuffer = new StringBuffer(error);
        for (String argument: args) {
            errorBuffer.append(" '%s'".formatted(argument));
        }
        error = errorBuffer.toString();
        return "-" + error + DELIMITER;
    }
}
