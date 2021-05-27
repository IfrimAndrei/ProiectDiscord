package Datatypes.SQL;

/**
 * Datatype for the database to store the user_command table
 */
public class UserCommand {
    private String command;
    private String url;

    @Override
    public String toString() {
        return "User_Commands{" +
                "command='" + command + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
