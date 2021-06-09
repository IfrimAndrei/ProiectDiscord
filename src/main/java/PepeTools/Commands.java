package PepeTools;

import DataBase.Dao.UserCommandsDao;
import DataBase.Database;
import Datatypes.ArticleManager;
import Datatypes.SQL.UserCommand;
import PepeTools.SpecialPlatform.Reddit;
import PepeTools.SpecialPlatform.Youtube;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

import static PepeTools.Main.jda;

/**
 * Listener for user commands
 */
public class Commands extends ListenerAdapter {
    /**
     * Method starts when the user sends a message.
     * @param event Special parameter
     */
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){

        String[] args = event.getMessage().getContentRaw().split( " " );

        if(args[0].indexOf( Main.prefix ) == 0)
        {
            String userCommand = args[0].substring( 1 ).toLowerCase();
            if( userCommand.equals( "clear" ) )
            {
                clearMessages( args,event );
            }
            else if( userCommand.equals( "reddit" ) )
            {
                new Reddit(this).createRedditArticle(args,event);
            }
            /*
            switch (userCommand) {
            //basic commands
                case "info"          -> showInfo(event);
                //case "commands"      -> showCommands( event );
                case "clear"         -> clearMessages( args,event );
                case "datetime"      -> getCurrentDate(event);
                case "stop"          -> stopDiscord(event);
            //java blogs
                case "javapapers"    -> createArticle( "https://javapapers.com/category/java/feed/", event );
                case "mkyong"        -> createArticle( "https://mkyong.com/feed/", event );
                case "javacodegeeks" -> createArticle( "https://www.javacodegeeks.com/feed", event );
                case "awsamazon"     -> createArticle( "https://aws.amazon.com/blogs/developer/category/programing-language/java/feed/", event );
                case "stackabuse"    -> createArticle( "https://stackabuse.com/rss/", event );
            //Miscellaneous
                case "freesearch"    -> createArticle(args[1],event);
                case "reddit"        -> new Reddit(this).createRedditArticle(args,event);
                case "youtube"       -> new Youtube(this).createYoutubeArticle(args,event);
                case "addcommand"    -> addNewCommand(args[1], args[2], event);
                case "deletecommand" -> deleteCommand(args[1],event);
                default              -> createNewArticle(args,event);
            }*/
        }

    }


    /**
     * Builds and displays the EmbedBuilder with the article information and adds an entry to the HashMap based on the urlAddress
     * @param urlAddress Parameter for the ArticleManager.getPage
     * @param event Special parameter
     * @see ArticleManager
     */
    public void createArticle(String urlAddress,GuildMessageReceivedEvent event){
        EmbedBuilder info = ArticleManager.getPage(urlAddress,0,true);
        if(info==null)
            event.getChannel().sendMessage("Error! URL not found.").queue();
        else{
            event.getChannel().sendMessage(info.build()).queue(message -> {
                message.addReaction("\u2B05").queue();
                message.addReaction("\u27A1").queue();
                message.addReaction("\u274C").queue();
                message.addReaction("\uD83D\uDD04"   ).queue();
            });
        }
    }

    /**
     * Clears the discord last messages based on the arguments given
     * @param args clear/[number of messages we want cleared]
     * @param event Special parameter
     */
    public void clearMessages(String[] args,GuildMessageReceivedEvent event){
        if(args.length!=2){
            event.getChannel().sendMessage ("Invalid Arguments").queue();
        }
        else{
            int number =Integer.parseInt(args[1]);
            if(number>0 && number<100){
                Collection<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])+1).complete();
                event.getChannel().deleteMessages( messages ).queue();
            }
            else{
                event.getChannel().sendMessage ("Can only clear 1-99 messages").queue();
            }

        }
    }

    /**
     * EmbedBuilder display
     * @param event Special parameter
     */
    public void showInfo(GuildMessageReceivedEvent event){
        {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Pepe Bot Info");
            info.setDescription( "Getting Better Every Day");
            info.setFooter("Created by sushy & adrianchiru" );
            event.getChannel().sendMessage(info.build()).queue();
        }
    }

    /**
     * Displays in the Discord chat the possible commands to call
     * @param event Special parameter
     */
    /*public void showCommands(GuildMessageReceivedEvent event){
        {
            event.getChannel().sendMessage( """
                            Commands that I can help with:\s
                                ~commands
                                ~info
                                ~clear [integer]
                                ~datetime
                                ~stop
                            JavaBlogs:
                                ~javapapers
                                ~mkyong
                                ~javacodegeeks
                                ~awsamazon
                                ~stackabuse
                            Miscellaneous:
                                ~reddit [subreddit_name] ...[extra]
                                ~youtube
                                ~youtube [youtuber_name]
                                ~freeSearch [rss link]
                                ~addcommand [command_name] [rss feed link]
                                ~deletecommand [command_name]
                            And other secret commands :wink:
                            """
            ).queue();
        }
    }*/

    public void getCurrentDate(GuildMessageReceivedEvent event){
        Date currentDate = new Date();
        event.getChannel().sendMessage(currentDate.toString()).queue();
    }


    /**
     * Adds a new command in the database if needed
     * @param command The name of the command
     * @param url The URL of the RSS Feed
     * @param event Special Parameter
     * @see Database
     * @see UserCommandsDao
     */
    public void addNewCommand(String command, String url, GuildMessageReceivedEvent event){
        if(command.equals("addcommand") || command.equals("awsamazon") || command.equals("clear") || command.equals("commands")||
           command.equals("datetime") || command.equals("freesearch")|| command.equals("info")|| command.equals("deletecommand") ||
           command.equals("javacodegeeks")|| command.equals("javapapers")|| command.equals("mkyong")|| command.equals("reddit")||
           command.equals("stackabuse")|| command.equals("youtube")) {
            event.getChannel().sendMessage("Error! Command already exists.").queue();
            return;
        }
        Database myDatabase = Database.getInstance();
        UserCommandsDao userCommandsDao = new UserCommandsDao();
        UserCommand userCommand = new UserCommand();
        userCommand.setCommand(command);
        userCommand.setUrl(url);

        List<UserCommand> userCommands;
        userCommands = userCommandsDao.getAll();
        for(UserCommand u : userCommands)
            if(u.getCommand().equals(userCommand.getCommand())) {
                event.getChannel().sendMessage("Error! Command already exists.").queue();
                return;
            }
        userCommandsDao.insert(userCommand);
        myDatabase.commit();
        event.getChannel().sendMessage("Command added!").queue();
    }

    /**
     * Creates an article with the URL gotten from the database call
     * @param args The name of the command
     * @param event Special Parameter
     * @see UserCommandsDao
     */
    public void createNewArticle(String[] args, GuildMessageReceivedEvent event){
        if(args.length == 1)
        {
            args[0] = args[0].substring(1);
            System.out.println(args[0]);
            UserCommandsDao userCommandsDao = new UserCommandsDao();
            List<UserCommand> userCommands;
            userCommands = userCommandsDao.getAll();
            boolean ok = false;
            for(UserCommand u : userCommands)
            {
                if(u.getCommand().equals(args[0]))
                {
                        createArticle(u.getUrl(),event);
                        ok = true;
                        break;
                }
            }

            if(!ok)
            event.getChannel().sendMessage("Error! Command doesn't exist!").queue();
        }
        else {
            event.getChannel().sendMessage("Error! Command doesn't exist!").queue();
        }
    }

    /**
     * Deletes a command from the database if it exists
     * @param command The name of the command
     * @param event Special parameter
     * @see Database
     * @see UserCommandsDao
     */
    public void deleteCommand(String command, GuildMessageReceivedEvent event){
        Database myDatabase = Database.getInstance();
        UserCommandsDao userCommandsDao = new UserCommandsDao();
        List<UserCommand> userCommands;
        userCommands = userCommandsDao.getAll();
        for(UserCommand u : userCommands)
            if(u.getCommand().equals(command)) {
                userCommandsDao.delete(u);
                myDatabase.commit();
                event.getChannel().sendMessage("Command deleted successfully!").queue();
                return;
            }
        event.getChannel().sendMessage("Error! Command doesn't exist.").queue();
    }

    /**
     * Close the discord bot
     * @param event Special parameter
     */
    public void stopDiscord(GuildMessageReceivedEvent event){
        event.getChannel().sendMessage("Bot is shutting down.").queue();
        Database myDatabase = Database.getInstance();
        myDatabase.disconnect();
        jda.shutdown();
    }
}
