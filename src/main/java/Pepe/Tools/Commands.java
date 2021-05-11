package Pepe.Tools;

import Datatypes.ArticleManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Collection;

public class Commands extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){

        String[] args = event.getMessage().getContentRaw().split( " " );
        if(args[0].equalsIgnoreCase( "uwu" )){
            event.getChannel().sendMessage(">W<").queue();
        }

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
        urlAddress+="?&limit=100";
        System.out.println(urlAddress);

        return urlAddress;
    }

    public void createArticle(String urlAddress,GuildMessageReceivedEvent event){
        EmbedBuilder info = ArticleManager.getPage(urlAddress,0,true);
        if(info==null)
            event.getChannel().sendMessage("Error! Subreddit not found.").queue();
        else{
            event.getChannel().sendMessage(info.build()).queue(message -> {
                message.addReaction("⬅️").queue();
                message.addReaction("➡️").queue();
                message.addReaction("❌"   ).queue();
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
                            JavaBlogs:
                                ~javapapers
                                ~mkyong
                                ~javacodegeeks
                                ~awsamazon
                                ~stackabuse
                                ~knpcode
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

            if(args.length==4) {
                redditAddress = createRedditAddress( args[1], args[2], args[3] );

            }
            else if(args.length==3) {
                redditAddress = createRedditAddress( args[1], args[2], null );

            }
            else if(args.length==2) {
                redditAddress = createRedditAddress( args[1], null, null );

            }
            else if(args.length==1){
                event.getChannel().sendMessage("For better details add a subreddit in the command.").queue();
                redditAddress = createRedditAddress( null, null, null );

            }
            else{
                event.getChannel().sendMessage("Too many arguments.").queue();
            }

            createArticle(redditAddress,event);

        }
    }

    public void createYoutubeArticle (String[] args,GuildMessageReceivedEvent event){
        {
            if(args.length == 1)
            {
                event.getChannel().sendMessage( """
                            Available youtube channel list:\s
                              -  LinusTechTips
                              -  OrdinaryThings
                              -  thenewboston
                              -  GMHikaru
                              -  AbdulBari
                            """
                ).queue();
            }
            else if(args.length > 2){
                event.getChannel().sendMessage("Invalid number of arguments").queue();
            }
            else {
                String youtubeLink = null;
                String channelName = args[1].toLowerCase();
                switch (channelName){
                    case "linustechtips"  -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCXuqSBlHAE6Xw-yeJA0Tunw";
                    case "gmhikaru"       -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCweCc7bSMX5J4jEH7HFImng";
                    case "ordinarythings" -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCxLYtICsUCWdr1YPrj5DtwA";
                    case "thenewboston"   -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCJbPGzawDH1njbqV-D5HqKw";
                    case "abdulbari"      -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCZCFT11CWBi3MHNlGf019nw";
                }

                if(youtubeLink!=null) {
                    createArticle( youtubeLink, event );
                }
                else{
                    event.getChannel().sendMessage("Error! YouTube channel not found.").queue();
                }

            }
        }
    }

}
