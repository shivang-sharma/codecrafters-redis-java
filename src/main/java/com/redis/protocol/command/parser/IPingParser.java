package com.redis.protocol.command.parser;

import com.redis.protocol.command.ICommand;
import com.redis.protocol.command.PingCommand;

import java.util.ArrayList;

public interface IPingParser {
    static ICommand decode(ArrayList<String> pingRequest, int size) {
        if (size > 2) {
            return new PingCommand(null, "ERR wrong number of arguments for 'ping' command..");
        } else if (size > 1) {
            return new PingCommand(pingRequest.get(4), null);
        } else {
            return new PingCommand(null, null);
        }
    }
    static void encode() {
        // TODO
    }
}
