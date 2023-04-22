package com.redis.protocol.command.parser;

import com.redis.protocol.command.ICommand;
import com.redis.protocol.command.PingCommand;
import com.redis.protocol.command.UnknownCommand;

import java.util.ArrayList;

public interface IUnknownParser {
    static ICommand decode(ArrayList<String> pingRequest, int size) {
        String[] args = new String[size-1];
        for (int j=0,i=2; i<=size && j<size; i++, j++) {
            args[j] = pingRequest.get(2*i);
        }
        return new UnknownCommand(pingRequest.get(2), args);
    }
}
