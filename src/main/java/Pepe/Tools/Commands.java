package Pepe.Tools;

import DataBase.Dao.UserCommandsDao;
import DataBase.Database;
import Datatypes.ArticleManager;
import Datatypes.SQL.UserCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

public class Commands extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){

        String[] args = event.getMessage().getContentRaw().split( " " );

        if(args[0].equals( "wait") && args[1].equals( "what" )){
            Collection<Message> messages = event.getChannel().getHistory().retrievePast(2).complete();
            event.getChannel().deleteMessages( messages ).queue();
        }

        if(args[0].indexOf( Main.prefix ) == 0)
        {
            String userCommand = args[0].substring( 1 ).toLowerCase();
            switch (userCommand) {
            //basic commands
                case "info"          -> showInfo(event);
                case "commands"      -> showCommands( event );
                case "clear"         -> clearMessages( args,event );
                case "datetime"      -> getCurrentDate(event);
            //java blogs
                case "javapapers"    -> createArticle( "https://javapapers.com/category/java/feed/", event );
                case "mkyong"        -> createArticle( "https://mkyong.com/feed/", event );
                case "javacodegeeks" -> createArticle( "https://www.javacodegeeks.com/feed", event );
                case "awsamazon"     -> createArticle( "https://aws.amazon.com/blogs/developer/category/programing-language/java/feed/", event );
                case "stackabuse"    -> createArticle( "https://stackabuse.com/rss/", event );
            //Miscellaneous
                case "freesearch"    -> createArticle(args[1],event);
                case "reddit"        -> createRedditArticle(args,event);
                case "youtube"       -> createYoutubeArticle(args,event);
                case "addcommand"    -> addNewCommand(args[1], args[2], event);
                case "deletecommand" -> deleteCommand(args[1],event);
                default              -> createNewArticle(args,event);
            }
        }

    }

    public String createRedditAddress(String subreddit, String searchType, String timePeriod){
        String urlAddress;
        if (subreddit != null) {
            if (searchType == null)
                searchType = "hot";
            urlAddress = "https://www.reddit.com/r/";
            urlAddress += subreddit;
            urlAddress += "/" + searchType;
            urlAddress += "/.rss";
            if (searchType.equals("top") && timePeriod != null) {
                urlAddress += "?t=" + timePeriod;
            }
        } else {
            urlAddress = "https://www.reddit.com/.rss";
        }
        if(timePeriod!=null)
            urlAddress+="&";
        else
            urlAddress+="?";
        urlAddress+="limit=100";
        System.out.println(urlAddress);

        return urlAddress;
    }

    public void createArticle(String urlAddress,GuildMessageReceivedEvent event){
        EmbedBuilder info = ArticleManager.getPage(urlAddress,0,true);
        if(info==null)
            event.getChannel().sendMessage("Error! URL not found.").queue();
        else{
            event.getChannel().sendMessage(info.build()).queue(message -> {
                message.addReaction("⬅️").queue();
                message.addReaction("➡️").queue();
                message.addReaction("❌"   ).queue();
                message.addReaction("🔄"   ).queue();
            });
        }
    }

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
    public void showInfo(GuildMessageReceivedEvent event){
        {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Pepe Bot Info");
            info.setDescription( "Getting Better Every Day");
            info.setFooter("Created by sushy & adrianchiru" );
            event.getChannel().sendMessage(info.build()).queue();
        }
    }

    public void showCommands(GuildMessageReceivedEvent event){
        {
            event.getChannel().sendMessage( """
                            Commands that I can help with:\s
                                ~commands
                                ~info
                                ~clear [integer]
                                ~datetime
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
                            And other secret commands :wink:
                            """
            ).queue();
        }
    }

    public void createRedditArticle(String[] args,GuildMessageReceivedEvent event){
        {
            String redditAddress = "";
            boolean ok = false;
            if(args.length==4) {
                redditAddress = createRedditAddress( args[1], args[2], args[3] );
                ok=true;
            }
            else if(args.length==3) {
                redditAddress = createRedditAddress( args[1], args[2], null );
                ok=true;
            }
            else if(args.length==2) {
                redditAddress = createRedditAddress( args[1], null, null );
                ok=true;
            }
            else if(args.length==1){
                event.getChannel().sendMessage("Add a subreddit too").queue();
            }
            else{
                event.getChannel().sendMessage("Too many arguments.").queue();
            }


            if(ok) {
                createArticle( redditAddress, event );
            }
        }
    }

    public void createYoutubeArticle (String[] args,GuildMessageReceivedEvent event){

            String channelName = null;
            if(args.length == 1)
            {
                event.getChannel().sendMessage( """
                            Exemple for a youtube channel list:\s
                              -  LinusTechTips
                              -  OrdinaryThings
                              -  thenewboston
                              -  GMHikaru
                              -  AbdulBari
                            """
                ).queue();
            }
            else if(args.length >= 2){
                channelName = args[1].toLowerCase();
                for(int i=2;i<args.length;i++)
                    channelName = channelName.concat(args[i].toLowerCase());
                String youtubeLink;

                switch (channelName){
                    case "linustechtips"  -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCXuqSBlHAE6Xw-yeJA0Tunw";
                    case "gmhikaru"       -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCweCc7bSMX5J4jEH7HFImng";
                    case "ordinarythings" -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCxLYtICsUCWdr1YPrj5DtwA";
                    case "thenewboston"   -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCJbPGzawDH1njbqV-D5HqKw";
                    case "abdulbari"      -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCZCFT11CWBi3MHNlGf019nw";
                    default               -> youtubeLink = defaultYoutubeSearch(channelName, event);
                }


                if(!youtubeLink.equals("nu exista")) {
                    createArticle( youtubeLink, event );
                }
                else{
                    event.getChannel().sendMessage("Error! YouTube channel not found.").queue();
                }

            }
    }

    public void getCurrentDate(GuildMessageReceivedEvent event){
        Date currentDate = new Date();
        event.getChannel().sendMessage(currentDate.toString()).queue();
    }

    public String defaultYoutubeSearch(String channelName, GuildMessageReceivedEvent event){
        String youtubeLink;
        String source = "https://www.youtube.com/c/";
        source = source.concat(channelName);
        System.out.println(source);
        try {
            URL sourceUrl = new URL(source);
            HttpURLConnection conn = (HttpURLConnection) sourceUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                source = "https://www.youtube.com/user/";
                source = source.concat(channelName);

                try {
                    sourceUrl = new URL(source);
                    conn = (HttpURLConnection) sourceUrl.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    if (conn.getResponseCode() != 200) {
                        event.getChannel().sendMessage("Error! YouTube channel not found.").queue();
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));

                    String page = in.readLine();
                    while (!page.contains("externalId")) {
                        page = in.readLine();
                        System.out.println(page);
                    }
                    int firstPos = page.indexOf("externalId\":");
                    String temp = page.substring(firstPos + "externalId\":".length() + 1, firstPos + "externalId\":".length() + 25);
                    System.out.println(temp);

                    youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=";
                    youtubeLink = youtubeLink.concat(temp);
                    return youtubeLink;
                }catch (IOException e) {
                    e.printStackTrace();
                    return "nu exista";
                }
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String page = in.readLine();
            while(!page.contains("externalId")) {
                page = in.readLine();
                System.out.println(page);
            }
            int firstPos  = page.indexOf("externalId\":");
            String temp = page.substring(firstPos + "externalId\":".length() + 1, firstPos + "externalId\":".length() + 25);
            System.out.println(temp);

            youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=";
            youtubeLink = youtubeLink.concat(temp);
            return youtubeLink;

        } catch (IOException e) {
            e.printStackTrace();
            return "nu exista";
        }
    }


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

        List<UserCommand> userCommands = new LinkedList<>();
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

    public void createNewArticle(String[] args, GuildMessageReceivedEvent event){
        if(args.length == 1)
        {
            args[0] = args[0].substring(1);
            System.out.println(args[0]);
            Database myDatabase = Database.getInstance();
            UserCommandsDao userCommandsDao = new UserCommandsDao();
            List<UserCommand> userCommands = new LinkedList<>();
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
    }

    public void deleteCommand(String command, GuildMessageReceivedEvent event){
        Database myDatabase = Database.getInstance();
        UserCommandsDao userCommandsDao = new UserCommandsDao();
        List<UserCommand> userCommands = new LinkedList<>();
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
}
