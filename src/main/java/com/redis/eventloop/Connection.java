package com.redis.eventloop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection implements IConnection {
    private final Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    public Connection(Socket connection) {
        this.connection = connection;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            this.out = new PrintWriter(this.connection.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
            this.close();
        }
    }
    public BufferedReader in() {
        return this.in;
    }
    public PrintWriter out() {
        return this.out;
    }
    public void close() {
        try {
            if (this.connection != null) {
                this.in.close();
                this.out.flush();
                this.out.close();
                this.connection.close();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
