package main;

import io.log;
import io.md5;
import network.protocol;
import process.classifier;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

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
                while (!recvstr.toString().substring(recvstr.toString().length() - 1).equals("\u0005")) {
                    recvstr.append(recv.readLine());
                }
                recvstr.delete(recvstr.length() - 1, recvstr.length());
                if (!recvstr.toString().split("\u0004")[1].equals(Objects.requireNonNull(md5.md5_encode(recvstr.toString().split("\u0004")[0])).toLowerCase()))
                    error = true;
                String r = recvstr.toString().split("\u0004")[0];
                if (!error) {
                    try {
                        decoder = new protocol(r);
                    } catch (IllegalArgumentException e) {
                        log.printf("Error protocol");
                        decoder = new protocol(r);
                        error = true;
                    }
                    if (!error) {
                        if (!decoder.getCommand().equals("exit")) {
                            log.printf("[" + sockettoclient.getRemoteSocketAddress().toString() + "]" + recvstr + " [" + decoder.getCommand() + "](" + decoder.getAllParameter() + ")");
                            classifier process = new classifier(decoder);
                            send.write(Base64.getEncoder().encodeToString(process.returnback().getBytes(StandardCharsets.UTF_8)));
                            log.printf("To[" + sockettoclient.getRemoteSocketAddress().toString() + "]" + " [" + process.returnback() + "]");
                            send.write("\u0004");
                            send.flush();
                        } else {
                            sockettoclient.close();
                        }
                    } else {
                        send.write("\u0006\u0004");
                        send.flush();
                    }
                } else {
                    send.write("\u0006\u0004");
                    send.flush();
                }
            } catch (StringIndexOutOfBoundsException e) {
                try {
                    send.write("\u0006\u0004");
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