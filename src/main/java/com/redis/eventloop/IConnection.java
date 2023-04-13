package com.redis.eventloop;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface IConnection {
    BufferedReader in();
    PrintWriter out();
    void close();
}
