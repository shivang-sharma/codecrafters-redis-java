package com.redis.protocol;

import com.redis.protocol.command.ICommand;
import com.redis.protocol.command.PingCommand;
import com.redis.protocol.command.parser.IEchoParser;
import com.redis.protocol.command.parser.IPingParser;

import java.util.ArrayList;

public interface IRespProtocol {
    static String encode(String[] request){
        return "";
    }
    static ICommand decode(ArrayList<String> request) {
        int size = Integer.parseInt(request.get(0).substring(1,2));
        if (request.get(2).equalsIgnoreCase("PING")) {
            return IPingParser.decode(request, size);
        } else if (request.get(2).equalsIgnoreCase("ECHO")) {
            return IEchoParser.decode(request, size);
        }
        return new PingCommand(null, null);
    }
}
