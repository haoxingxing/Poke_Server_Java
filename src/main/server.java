package main;

import io.log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

class server {
    private ServerSocket serverSocket;

    server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        log.printf("ServerPort:" + port);
        serverSocket.setSoTimeout(6000000);
    }

    void run() {
        while (true) {
            try {
                Socket stocs = serverSocket.accept();
                if (stocs.isConnected()) {
                    serverthread s = new serverthread(stocs);
                    s.start();
                }
            } catch (SocketTimeoutException ignored) {
            } catch (IOException e) {
                log.printf("Error:\n" + e.toString());
                break;
            }
        }
    }
}
