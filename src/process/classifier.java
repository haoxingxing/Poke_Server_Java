package process;

import network.protocol;
public class classifier {
    private String returnbackstr;

    public classifier(protocol decoder) {
        int mode = 0;
        if (decoder.getCommand().equals("ulog")) mode = 1;
        if (decoder.getCommand().equals("ureg")) mode = 2;
        switch (mode) {
            case 1:
                if (decoder.PararmeterSize() == user.logpar) {
                    if (user.login(decoder.getParameter()[0], decoder.getParameter()[1]))
                        returnbackstr = "succ";
                    else
                        returnbackstr = "fall";
                } else {
                    returnbackstr = "erro";
                }
            case 2:
                if (decoder.PararmeterSize() == user.regpar) {
                    if (user.register(decoder.getParameter()[0], decoder.getParameter()[1]))
                        returnbackstr = "succ";
                    else
                        returnbackstr = "fall";
                } else {
                    returnbackstr = "erro";
                }
            default:
                returnbackstr = "E";
        }
    }
    public String returnback() {
        return returnbackstr;
    }
}
