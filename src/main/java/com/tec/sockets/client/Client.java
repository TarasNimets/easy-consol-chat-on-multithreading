package com.tec.sockets.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

class Client {

    private Client(String host, int port) throws UnknownHostException, IOException {
        Socket socket = new Socket(host, port);
        new Thread(new ReadStream(socket)).start();
        new Thread(new WriteSocket(socket)).start();
    }

    public static Client getClient(String host, int port) throws UnknownHostException, IOException {
        return new Client(host, port);
    }

    private class ReadStream implements Runnable {

        private Socket socket;

        public ReadStream(Socket socket) {
            this.socket = socket;
        }

        public void run() {

            try (Scanner scanner = new Scanner(socket.getInputStream())) {

                while (true) {
                    String msg = scanner.nextLine();
                    if (msg == null)
                        break;
                    System.out
                            .println(String.format("Massage from %s - %s", socket.getInetAddress().getHostName(), msg));
                }

            } catch (IOException ignore) {
                /* NOPE */}
            System.out.println("Client left chat room");
        }

    }

    private class WriteSocket implements Runnable {

        private Socket socket;
        private final static int BYTE_SIZE = 1000;

        private WriteSocket(Socket socket) {
            this.socket = socket;
        }

        public void run() {

            byte[] buffer = new byte[BYTE_SIZE];

            try {

                while (true) {
                    System.out.println(String.format("Massage from %s - ", socket.getInetAddress().getHostName()));
                    int count = System.in.read(buffer, 0, BYTE_SIZE);
                    socket.getOutputStream().write(buffer, 0, count);
                }

            } catch (IOException ignore) {
                /* NOPE */}
            System.out.println("Left chat room");

        }

    }
}
