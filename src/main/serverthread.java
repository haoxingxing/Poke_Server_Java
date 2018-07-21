package main;

import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

import io.log;

class serverthread extends Thread {
    private Thread t;
    private Socket stocs;
    private BufferedReader recv;
    private BufferedWriter send;
    private String recvstr;

    serverthread(Socket stocs) {
        this.stocs = stocs;
        log.printf("[" + stocs.getRemoteSocketAddress().toString() + "]" + "Connected");
        try {
            recv = new BufferedReader(new InputStreamReader(stocs.getInputStream()));
            send = new BufferedWriter(new OutputStreamWriter(stocs.getOutputStream()));
        } catch (IOException e) {
            log.printf(e.toString());
        }
    }

    public void run() {
        while (!stocs.isClosed()) {
            recvstr = new String();
            try {
                recvstr = recv.readLine();

                log.printf("[" + stocs.getRemoteSocketAddress().toString() + "]" + recvstr);
                if (recvstr != "exit") {
                    send.write(stocs.getRemoteSocketAddress().toString());
                    send.write("\r\n");
                    send.flush();
                } else {
                    stocs.close();
                }
            } catch (IOException e) {
                log.printf("[" + stocs.getRemoteSocketAddress().toString() + "]" + " Error:\n" + e.toString());
                break;
            }
        }
        log.printf(stocs.getRemoteSocketAddress().toString() + " exited");
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, stocs.toString());
            t.start();
        }
    }
}