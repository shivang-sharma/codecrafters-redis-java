package com.redis.protocol.command.parser;

import com.redis.protocol.command.GetCommand;
import com.redis.protocol.command.ICommand;
import com.redis.protocol.command.SetCommand;

import java.util.ArrayList;

public interface IGetParser {
    /**
     * @param getRequest
     * @param size
     * @return
     * Sample Request: [*3, $3, SET, $3, aka, $2, aa]
     */
    static ICommand decode(ArrayList<String> getRequest, int size) {
        if (size == 2) {
            return new GetCommand(getRequest.get(4), null);
        } else {
            return new SetCommand(null, null, "ERR wrong number of arguments for 'get' command");
        }
    }
    static void encode() {
        // TODO
    }
}
