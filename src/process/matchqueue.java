package process;

import io.file;

import java.io.IOException;

public class matchqueue {
    private String queuename;
    private int queuesize;
    private int queueid;
    matchqueue(String queuename, int size)
    {
         this.queuename=queuename;
         this.queuesize=size;
    }
    private void createqueue()
    {
        try {
            queueid = Integer.parseInt(file.read(queuename + "." + queuesize + ".queue.config"));
            queueid++;
            file.write(queuename + "." + queuesize + ".queue.config", queueid + "");
        } catch (IOException e) {
            queueid = 0;
            try {
                file.write(queuename+"."+queuesize+".queue.config","0");
            } catch (IOException ignored){
            }
        }
        try {
            file.write(queuename + "." + queuesize + ".queue." + queueid, "\u0001\u001f\u0001");
        } catch (IOException ignored) {
        }
    }
    void addtoqueue(String name) {
        try {
            queueid = Integer.parseInt(file.read(queuename + "." + queuesize + ".queue.config"));
        } catch (IOException e) {
            queueid = 0;
        }
        if (io.file.isexists(queuename + "." + queuesize + ".queue." + queueid))
            try {
                while (true) {
                    String[] queue = file.read(queuename + "." + queuesize + ".queue." + queueid).split("\u001f");
                    if (queue[0].equals("\u0001")) {
                        queue[0] = name;
                        file.write(queuename + "." + queuesize + ".queue.user." + name, queueid + "");
                        file.write(queuename + "." + queuesize + ".queue." + queueid, queue[0] + "\u001f" + queue[1]);
                        break;
                    } else if (queue[1].equals("\u0001")) {
                        queue[1] = name;
                        file.write(queuename + "." + queuesize + ".queue.user." + name, queueid + "");
                        file.write(queuename + "." + queuesize + ".queue." + queueid, queue[0] + "\u001f" + queue[1]);
                        break;
                    } else {
                        this.createqueue();
                    }
                }
            } catch (IOException ignored) {
            }
        else
        {
            this.createqueue();
        }
    }
    boolean ismatched(String name)
    {
        while (!file.isexists(queuename+"."+queuesize+".queue.user."+name))
        {
            this.addtoqueue(name);
        }
        try {
            queueid = Integer.parseInt(file.read(queuename + "." + queuesize + ".queue.user." + name));
            String[] queue = file.read(queuename + "." + queuesize + ".queue." + queueid).split("\u001f");
            return !queue[1].equals("\u0001") && !queue[0].equals("\u0001");
        } catch (IOException e) {
            return false;
        }
    }
    public String getOtherName(String name) {
        while (!file.isexists(queuename+"."+queuesize+".queue.user."+name))
        {
            this.addtoqueue(name);
        }
        try {
            queueid = Integer.parseInt(file.read(queuename + "." + queuesize + ".queue." + name));
            String[] queue = file.read(queuename + "." + queuesize + ".queue." + queueid).split("\u001f");
            if (queue[0].equals(name))
            {
                return queue[1];
            }
            else
            {
                return queue[0];
            }
        } catch (IOException e) {
            return "";
        }
    }
}
