package process;

import network.protocol;
public class classifier {
    private String returnbackstr;

    public classifier(protocol decoder,people t) {
        int mode = 0;
        if (decoder.getCommand().equals("userlog")) mode = 1;
        if (decoder.getCommand().equals("userreg")) mode = 2;
        if (decoder.getCommand().equals("gomoku_joinmatch")) mode=3;
        if (decoder.getCommand().equals("gomoku_ismatched")) mode=4;
        switch (mode) {
            case 1:
                if (decoder.PararmeterSize() == user.logpar) {
                    if (user.login(decoder.getParameter()[0], decoder.getParameter()[1],t))
                        returnbackstr = "succ";
                    else
                        returnbackstr = "fall";
                    break;
                } else {
                    returnbackstr = "erro";
                    break;
                }
            case 2:
                if (decoder.PararmeterSize() == user.regpar) {
                    if (user.register(decoder.getParameter()[0], decoder.getParameter()[1],t))
                        returnbackstr = "succ";
                    else
                        returnbackstr = "fall";
                    break;
                } else {
                    returnbackstr = "erro";
                    break;
                }
            case 3:
                Gomoku gomoku=new Gomoku(t);
                gomoku.joinqueue();
                returnbackstr="ok";
                break;
            case 4:
                Gomoku gomokud=new Gomoku(t);
                if (gomokud.ismatched())
                {
                    returnbackstr="true";
                }
                else
                {
                    returnbackstr="false";
                }
                break;
            default:
                returnbackstr = "E";
                break;
        }
    }
    public String returnback() {
        return returnbackstr;
    }
}
