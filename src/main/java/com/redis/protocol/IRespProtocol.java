package com.redis.protocol;

import com.redis.protocol.command.ICommand;
import com.redis.protocol.command.PingCommand;
import com.redis.protocol.command.UnknownCommand;
import com.redis.protocol.command.parser.*;

import java.util.ArrayList;

public interface IRespProtocol {
    /**
     * @param request
     * @return
     */
    static String encode(String[] request){
        return "";
    }

    /**
     * @param request
     * @return
     */
    static ICommand decode(ArrayList<String> request) {
        int size = Integer.parseInt(request.get(0).substring(1,2));
        if (request.get(2).equalsIgnoreCase("PING")) {
            return IPingParser.decode(request, size);
        } else if (request.get(2).equalsIgnoreCase("ECHO")) {
            return IEchoParser.decode(request, size);
        } else if (request.get(2).equalsIgnoreCase("SET")) {
            return ISetParser.decode(request, size);
        } else if (request.get(2).equalsIgnoreCase("GET")) {
            return IGetParser.decode(request, size);
        }
        return IUnknownParser.decode(request, size);
    }
}
