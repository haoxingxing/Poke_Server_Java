package io;

import java.io.*;

public class file {
    static public void write(String file, String data) throws IOException {
        File f = new File("data/" + file);
        FileOutputStream fop = new FileOutputStream(f);
        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
        writer.append(data);
        writer.close();
        fop.close();
    }

    static public String read(String file) throws IOException {
        FileInputStream fip = new FileInputStream("data/" + file);
        InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
        StringBuilder sb = new StringBuilder();
        while (reader.ready()) {
            sb.append((char) reader.read());
        }
        reader.close();
        fip.close();
        return sb.toString();
    }

    static public boolean isexists(String file) {
        File f = new File("data/" + file);
        return f.exists();
    }
}
