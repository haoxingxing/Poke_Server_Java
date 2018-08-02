package process;

import io.file;

import java.io.IOException;

public class matchqueue {
    private String queuename;
    private int queuesize;
    private int queues;
    matchqueue(String queuename, int size)
    {
         this.queuename=queuename;
         this.queuesize=size;
    }
    private void createqueue()
    {
        try {
            queues=Integer.parseInt(file.read(queuename+"."+queuesize+".queue.config"));
            queues++;
            file.write(queuename+"."+queuesize+".queue.config",queues+"");
        } catch (IOException e) {
            queues=0;
            try {
                file.write(queuename+"."+queuesize+".queue.config","0");
            } catch (IOException ignored){
            }
        }
        try {
            file.write(queuename + "." + queuesize + ".queue." + queues,"\u0001\u001f\u0001");
        } catch (IOException ignored) {
        }
    }
    void addtoqueue(String name) {
        try {
            queues=Integer.parseInt(file.read(queuename+"."+queuesize+".queue.config"));
        } catch (IOException e) {
            queues=0;
        }
        if (io.file.isexists(queuename+"."+queuesize+".queue."+queues))
            try {
                while (true) {
                    String[] queue = file.read(queuename + "." + queuesize + ".queue." + queues).split("\u001f");
                    if (queue[0].equals("\u0001")) {
                        queue[0] = name;
                        file.write(queuename + "." + queuesize + ".queue.user." + name, queues + "");
                        file.write(queuename + "." + queuesize + ".queue." + queues, queue[0] + "\u001f" + queue[1]);
                        break;
                    } else if (queue[1].equals("\u0001")) {
                        queue[1] = name;
                        file.write(queuename + "." + queuesize + ".queue.user." + name, queues + "");
                        file.write(queuename + "." + queuesize + ".queue." + queues, queue[0] + "\u001f" + queue[1]);
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
            queues = Integer.parseInt(file.read(queuename + "." + queuesize + ".queue.user." + name));
            String[] queue = file.read(queuename + "." + queuesize + ".queue." + queues).split("\u001f");
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
            queues = Integer.parseInt(file.read(queuename + "." + queuesize + ".queue." + name));
            String[] queue = file.read(queuename + "." + queuesize + ".queue." + queues).split("\u001f");
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
