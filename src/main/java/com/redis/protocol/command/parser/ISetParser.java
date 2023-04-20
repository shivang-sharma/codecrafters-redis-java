package com.redis.protocol.command.parser;

import com.redis.protocol.command.EchoCommand;
import com.redis.protocol.command.ICommand;
import com.redis.protocol.command.PingCommand;
import com.redis.protocol.command.SetCommand;

import java.util.ArrayList;

public interface ISetParser {
    /**
     * @param setRequest
     * @param size
     * @return
     * Sample Request: [*3, $3, SET, $3, aka, $2, aa]
     */
    static ICommand decode(ArrayList<String> setRequest, int size) {
        if (size == 3) {
            return new SetCommand(setRequest.get(4), setRequest.get(6), null);
        } else if (size > 3) {
            return new SetCommand(null, null, "ERR syntax error");
        } else {
            return new SetCommand(null, null, "ERR wrong number of arguments for 'set' command");
        }
    }
    static void encode() {
        // TODO
    }
}
