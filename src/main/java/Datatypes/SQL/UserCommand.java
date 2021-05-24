package Datatypes.SQL;

public class UserCommand {
    private String command;
    private String Url;

    @Override
    public String toString() {
        return "User_Commands{" +
                "command='" + command + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getUrl() {
        return Url;
    }
}
