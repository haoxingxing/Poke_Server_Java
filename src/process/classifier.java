package process;

public class classifier {
    private String retrunbackstr;

    public classifier(String command, String parameter) {
        if (command.equals("test")) {
            test process = new test(parameter);
            retrunbackstr = process.returnback();
        } else
            retrunbackstr = "cmnf";
    }

    public String returnback() {
        return retrunbackstr;
    }
}
