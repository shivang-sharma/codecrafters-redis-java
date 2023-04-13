package com.redis.protocol.command;

import java.util.Optional;

public interface ICommand {
    public Optional<String> getMessage();
    public Optional<String> getError();

    public String generateResponse();
}
