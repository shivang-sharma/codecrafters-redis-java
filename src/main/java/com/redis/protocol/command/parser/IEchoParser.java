package com.redis.protocol.command.parser;

import com.redis.protocol.command.EchoCommand;
import com.redis.protocol.command.ICommand;

import java.util.ArrayList;

public interface IEchoParser {
    static ICommand decode(ArrayList<String> echoRequest, int size) {
        if (size == 2) {
            return new EchoCommand(echoRequest.get(4), null);
        } else {
            return new EchoCommand(null, "ERR wrong number of arguments for 'echo' command");
        }
    }
    static void encode() {
        // TODO
    }
}
