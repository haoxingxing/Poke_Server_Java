package main;

import io.log;
import network.protocol;
import process.classifier;

import java.io.*;
import java.net.Socket;

class serverthread extends Thread {
    private Thread t;
    private Socket sockettoclient;
    private BufferedReader recv;
    private BufferedWriter send;

    serverthread(Socket stocs) throws IOException {
        this.sockettoclient = stocs;
        log.printf("[" + sockettoclient.getRemoteSocketAddress().toString() + "]" + "Connected");
        recv = new BufferedReader(new InputStreamReader(sockettoclient.getInputStream()));
        send = new BufferedWriter(new OutputStreamWriter(sockettoclient.getOutputStream()));
    }

    public void run() {
        while (!sockettoclient.isClosed()) {
            StringBuilder recvstr = new StringBuilder();
            try {
                recvstr.append(recv.readLine());
                while (!recvstr.toString().substring(recvstr.length() - 1).equals("}")) {
                    recvstr.append(recv.readLine());
                }
                protocol decoder = new protocol(recvstr.toString());
                log.printf("[" + sockettoclient.getRemoteSocketAddress().toString() + "]" + recvstr);
                if (!recvstr.toString().equals("exit")) {
                    classifier process = new classifier(decoder.getCommand(), decoder.getParameter());
                    send.write(process.returnback());
                    send.write("\r\n");
                    send.flush();
                } else {
                    sockettoclient.close();
                }
            } catch (IOException e) {
                log.printf("[" + sockettoclient.getRemoteSocketAddress().toString() + "]" + " Error:\n" + e.toString());
            }
        }
        log.printf(sockettoclient.getRemoteSocketAddress().toString() + " exited");
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, sockettoclient.toString());
            t.start();
        }
    }
}