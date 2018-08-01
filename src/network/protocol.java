package network;

import java.util.Base64;

public class protocol {
    private String command;
    private String parameterAll;
    private String parameters[];
    public protocol(String code) throws IllegalArgumentException {
        if (code.substring(code.length() - 1).equals("\u0004"))
            code = code.substring(0, code.length() - 1);
        String firstdecode;
        firstdecode = new String(Base64.getDecoder().decode(code));
        String[] all = firstdecode.split("\u001c");
        command = all[0];
        parameterAll = all[1];
        parameters = parameterAll.split(String.valueOf('\u001F'));
    }

    public String getCommand() {
        return command;
    }

    public String getAllParameter() {
        return parameterAll;
    }

    public int PararmeterSize() {
        return parameters.length;
    }

    public String[] getParameter() {
        return parameters;
    }
}
