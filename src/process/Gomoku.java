package process;

import io.file;

import java.io.IOException;

class Gomoku {
    private matchqueue queue = new matchqueue("gomoku", 2);
    private people t;

    Gomoku(people t)
    {
        this.t=t;
    }

    void joinqueue()
    {
        queue.addtoqueue(t.username);
    }

    boolean ismatched()
    {
        if (queue.ismatched(t.username)) {
            this.initP();
        }
        return queue.ismatched(t.username);
    }

    boolean whichIam() {
        return queue.getwhichIam(t.username);
    }

    private void initP() {
        if (queue.ismatched(t.username) && !file.isexists("Gomoku.Pan." + queue.queueid)) {
            try {
                file.write("Gomoku.Pan." + queue.queueid, "true/");
            } catch (IOException ignored) {
            }
        }
    }

    String getP(int wherestarts) {
        try {
            String all = file.read("Gomoku.Pan." + queue.getQueueid(t.username));
            StringBuilder ret = new StringBuilder();
            if (wherestarts >= all.split("/").length) {
                ret = new StringBuilder(all);
            } else {
                ret.append(all.split("/")[0]).append("/");
                for (int i = wherestarts + 1; i < all.split("/").length; i++)
                    ret.append(all.split("/")[i]).append("/");
            }
            return ret.toString();
        } catch (IOException ignored) {
            return "e";
        }
    }

    void setP(int x, int y) {
        try {
            String all = this.getP(0);
            StringBuilder save = new StringBuilder();
            if (!all.equals("e")) {
                String[] l = all.split("/");
                String k = "false";
                if (queue.getwhichIam(t.username))
                    k = "true";
                if (l[0].equals(k)) {
                    String[] s = new String[l.length + 1];
                    System.arraycopy(l, 0, s, 0, l.length);
                    if (l[0].equals("true")) {
                        s[0] = "false";
                    } else {
                        s[0] = "true";
                    }
                    s[l.length] = l[0] + "," + x + "," + y;
                    for (String value : s) save.append(value).append("/");
                    file.write("Gomoku.Pan." + queue.getQueueid(t.username), save.toString());
                }
            }
        } catch (IOException ignored) {
        }
    }
}
