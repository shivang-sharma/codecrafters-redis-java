package com.redis.protocol.command;

import java.util.Optional;

public interface ICommand {
    static String DELIMITER = "\r\n";
    public Optional<String> getMessage();
    public Optional<String> getError();

    public String generateResponse();
}
