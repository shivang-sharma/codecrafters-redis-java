package com.redis.eventloop;

import com.redis.protocol.IRespProtocol;
import com.redis.protocol.command.ICommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ConnectionHandler implements IConnectionHandler {
    private final IConnection connection;
    private final static int EOF = -1;
    public ConnectionHandler(IConnection connection) {
        this.connection = connection;
    }

    @Override
    public void execute() {
        try {
            ArrayList<String> request = new ArrayList<>();
            String token = this.connection.in().readLine();
            request.add(token);
            int length = Integer.parseInt(token.substring(1, 2)) * 2;
            while (length >0) {
                token = this.connection.in().readLine();
                request.add(token);
                length--;
            }
            ICommand command = IRespProtocol.decode(request);
            this.connection.out().write(command.generateResponse());
            this.connection.out().flush();
            this.connection.close();
        } catch (IOException e) {
             e.printStackTrace();
        } finally {
            this.connection.close();
        }
    }
}
