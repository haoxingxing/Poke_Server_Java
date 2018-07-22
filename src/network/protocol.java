package network;

import java.util.Base64;

public class protocol {
    private String command;
    private String parameter;

    public protocol(String code) throws IllegalArgumentException {
        if (code.substring(code.length() - 1).equals("\u0004"))
            code = code.substring(0, code.length() - 1);
        String firstdecode;
        firstdecode = new String(Base64.getDecoder().decode(code));
        command = firstdecode.substring(0, 4);
        parameter = firstdecode.substring(4);
    }

    public String getCommand() {
        return command;
    }

    public String getParameter() {
        return parameter;
    }
}
