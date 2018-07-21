package main;

import java.net.*;
import java.io.*;
import java.util.Base64;

import io.log;
import network.protocol;

class serverthread extends Thread {
    private Thread t;
    private Socket stocs;
    private BufferedReader recv;
    private BufferedWriter send;
    private String recvstr;

    public serverthread(Socket stocs) {
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
            recvstr = new String("");
            try {
                while (recvstr.substring(recvstr.length() - 4) != "\r\n") {
                    recvstr = recvstr + recv.readLine();
                }
                protocol decoder = new protocol(recvstr);
                /*

                Base64编码 解码得 “请求命令(前4位）+参数（base64）”
                以\r\n结尾
                 */
                recvstr = new String(Base64.getDecoder().decode(recvstr), "utf-8");
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