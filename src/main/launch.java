package main;

import io.log;

import java.io.IOException;

public class launch {
    public static void main(String[] args) {
        log.printf("Server Starting...");
        try {
            server s = new server(8864);
            s.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
