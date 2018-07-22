package io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class log {
    public static void printf(String log) {
        try {
            System.out.println(log);
            FileOutputStream f = new FileOutputStream("logs");
            OutputStreamWriter writer = new OutputStreamWriter(f, "UTF-8");
            writer.write(log + "\r\n");
            writer.close();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
