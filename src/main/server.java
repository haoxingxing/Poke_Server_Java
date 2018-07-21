package main;

import java.net.*;
import java.io.*;

import io.log;

public class server {
    private ServerSocket serverSocket;

    public server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        log.printf("ServerPort:" + port);
        serverSocket.setSoTimeout(6000000);
    }

    public void run() {
        while (true) {
            try {
                Socket stocs = serverSocket.accept();
                if (stocs.isConnected()) {
                    serverthread s = new serverthread(stocs);
                    s.start();
                }
            } catch (SocketTimeoutException s) {

            } catch (IOException e) {
                log.printf("Error:\n" + e.toString());
                break;
            }
        }
    }
}
