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
        if (decoder.getCommand().equals("gomoku_getpan")) mode = 5;
        if (decoder.getCommand().equals("gomoku_reportpan")) mode = 6;
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
                    if (gomokud.whichIam()) {
                        returnbackstr = "true";
                    } else {
                        returnbackstr = "false";
                    }
                }
                else
                {
                    returnbackstr = "not";
                }
                break;
            case 5:
                Gomoku gomokudd = new Gomoku(t);
                if (decoder.getParameter().length == 1) {
                    returnbackstr = gomokudd.getP(Integer.parseInt(decoder.getParameter()[0]));
                } else {
                    returnbackstr = gomokudd.getP(0);
                }
                break;
            case 6:
                Gomoku gomokuddd = new Gomoku(t);
                if (decoder.PararmeterSize() == 2) {
                    gomokuddd.setP(Integer.parseInt(decoder.getParameter()[0]), Integer.parseInt(decoder.getParameter()[1]));
                    returnbackstr = "ok";
                } else {
                    returnbackstr = "err";
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
