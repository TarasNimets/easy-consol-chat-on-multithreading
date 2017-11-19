package com.tec.sockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ServerSocket serverSocket = new ServerSocket(8080);

    public Server() throws IOException {
    }

    public void start() throws IOException {
        System.out.println("Server war run");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println(socket.getInetAddress().getHostAddress() + " is connected");
            new Thread(new ReadSocket(socket)).start();
            new Thread(new WriteSocket(socket)).start();
        }
    }

    public static class ReadSocket implements Runnable {

        private Socket socket;

        public ReadSocket(Socket socket) {
            this.socket = socket;
        }

        public void run() {

            try (InputStream stream = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));) {

                while (true) {
                    String msg = reader.readLine();
                    String template = "Massage from %s - %s";
                    System.out.println(String.format(template, socket.getInetAddress().getHostName(), msg));
                    if (msg == null)
                        break;
                }

            } catch (IOException e) {
                System.out.println("Clien left chat room");
            }

        }

    }

    public static class WriteSocket implements Runnable {

        private Socket socket;
        private final static int BUFFER_SIZE = 1000;

        public WriteSocket(Socket socket) {
            this.socket = socket;
        }

        public void run() {

            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                while (true) {
                    System.out.println(
                            String.format("Enter massege to clent %s - ", socket.getInetAddress().getHostName()));
                    int count = System.in.read(buffer, 0, BUFFER_SIZE);
                    socket.getOutputStream().write(buffer, 0, count);
                }

            } catch (IOException e) {
                System.out.println("Clien left chat room");
            }

        }

    }

}
