package DataBase.Dao;


import DataBase.Database;
import Datatypes.SQL.UserCommand;
import java.util.List;

public class UserCommandsDao implements Dao<UserCommand> {
    List<UserCommand> userCommands;
    private final Database db;

    public UserCommandsDao(){
        db = Database.getInstance();
        userCommands = db.getUserCommands();
    }

    @Override
    public UserCommand findByCommand (String command){
        UserCommand userCommand = new UserCommand();
        for(UserCommand userCommandCounter : userCommands){
            if(userCommandCounter.getCommand().equals(command))
            {
                userCommand = userCommandCounter;
                break;
            }
        }
        return userCommand;
    }

    @Override
    public List<UserCommand> getAll(){
        return userCommands;
    }

    @Override
    public void insert(UserCommand userCommand)
    {
        userCommands.add(userCommand);
        String comm = " INSERT INTO user_commands (command, url) VALUES ( '" + userCommand.getCommand() + "','" + userCommand.getUrl() + "')";
        db.executeCommand(comm);
    }

    @Override
    public void delete(UserCommand userCommand){
        userCommands.remove(userCommand);
        String comm = " DELETE FROM user_commands WHERE command='" + userCommand.getCommand() +"'";
        db.executeCommand(comm);
    }
}
