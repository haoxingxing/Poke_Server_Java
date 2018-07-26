package main;

import io.log;
import network.protocol;
import process.classifier;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

class serverthread extends Thread {
    private Thread t;
    private Socket sockettoclient;
    private BufferedReader recv;
    private BufferedWriter send;
    private boolean error = false;

    serverthread(Socket stocs) throws IOException {
        this.sockettoclient = stocs;
        log.printf("[" + sockettoclient.getRemoteSocketAddress().toString() + "]" + "Connected");
        recv = new BufferedReader(new InputStreamReader(sockettoclient.getInputStream()));
        send = new BufferedWriter(new OutputStreamWriter(sockettoclient.getOutputStream()));
    }

    public void run() {
        while (!sockettoclient.isClosed()) {
            protocol decoder;
            StringBuilder recvstr = new StringBuilder();
            try {
                recvstr.append(recv.readLine());
                while (!recvstr.toString().substring(recvstr.length() - 1).equals("\u0004")) {
                    recvstr.append(recv.readLine());
                }
                try {
                    decoder = new protocol(recvstr.toString());
                } catch (IllegalArgumentException e) {
                    log.printf("Error protocol");
                    decoder = new protocol("ZXJyZg==");
                    error = true;
                }
                if (!error) {
                    if (!decoder.getCommand().equals("exit")) {
                        log.printf("[" + sockettoclient.getRemoteSocketAddress().toString() + "]" + recvstr + "C:" + decoder.getCommand() + "P:" + decoder.getAllParameter());
                        classifier process = new classifier(decoder);
                        send.write(Base64.getEncoder().encodeToString(process.returnback().getBytes("UTF-8")));
                        log.printf("To[" + sockettoclient.getRemoteSocketAddress().toString() + "]"+process.returnback());
                        send.write("\u0004");
                        send.flush();
                    } else {
                        sockettoclient.close();
                    }
                } else {
                    send.write("\u0004");
                    send.flush();
                }
            } catch (StringIndexOutOfBoundsException e) {
                try {
                    send.write("\u0004");
                    send.flush();
                } catch (IOException el) {
                    log.printf("[" + sockettoclient.getRemoteSocketAddress().toString() + "]" + " Error:\n" + el.toString());
                    break;
                }
            } catch (IOException e) {
                log.printf("[" + sockettoclient.getRemoteSocketAddress().toString() + "]" + " Error:\n" + e.toString());
                break;
            }
            error = false;
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