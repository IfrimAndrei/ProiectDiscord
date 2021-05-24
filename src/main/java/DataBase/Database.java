package DataBase;

import Datatypes.SQL.UserCommand;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Database {
    private Connection con= null;
    private Statement stmt;
    private String sql;
    private ResultSet rs;
    private static Database single_instance = null;


    private Database(){
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/discordbot_db", "root", "" );
            con.setAutoCommit(false);
            stmt=con.createStatement();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance(){
        if (single_instance == null)
            single_instance = new Database();
        return single_instance;
    }

    public void disconnect(){
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commit()
    {
        try {
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UserCommand> getUserCommands(){
        List<UserCommand> userCommands = new LinkedList<>();
        sql = " SELECT * FROM user_commands";
        try{
            rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                UserCommand userCommand = new UserCommand();
                userCommand.setCommand( rs.getString("command"));
                userCommand.setUrl( rs.getString("url"));
                userCommands.add(userCommand);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userCommands;
    }

    public void executeCommand(String command){
        try {
            stmt.execute(command);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
