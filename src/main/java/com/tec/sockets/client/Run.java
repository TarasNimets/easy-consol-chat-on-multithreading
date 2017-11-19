package com.tec.sockets.client;

import java.io.IOException;
import java.net.UnknownHostException;

class Run {

    public static void main(String[] arg) throws UnknownHostException, IOException {

        Client.getClient("localhost", 8080);
    }
}
