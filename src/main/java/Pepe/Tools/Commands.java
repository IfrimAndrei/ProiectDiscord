package Pepe.Tools;

import Datatypes.ArticleManager;
import RSS.RSSReader;
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
            event
                    .getChannel().deleteMessages( messages ).queue();
        }

        if(args[0].equals( Main.prefix + "clear")){
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

        if(args[0].equalsIgnoreCase( Main.prefix + "info" )){
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Pepe Bot Info");
            info.setDescription( "Getting Better Every Day");
            info.setFooter("Created by sushy & adrianchiru" );
            event.getChannel().sendMessage(info.build()).queue();
            info.clear();
        }

        if(args[0].equalsIgnoreCase( Main.prefix + "commands" ))
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

        if(args[0].equalsIgnoreCase( Main.prefix + "youtube"))
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
                event.getMessage().delete().queue();
                if(args[1].equalsIgnoreCase("LinusTechTips")) {

                    EmbedBuilder info = ArticleManager.getPage("https://www.youtube.com/feeds/videos.xml?channel_id=UCXuqSBlHAE6Xw-yeJA0Tunw", 0, true);
                    event.getChannel().sendMessage(info.build()).queue(message -> {
                        message.addReaction("⬅️").queue();
                        message.addReaction("➡️").queue();
                        message.addReaction("❌").queue();
                    });
                }
                if(args[1].equalsIgnoreCase("GMHikaru")){
                    EmbedBuilder info = ArticleManager.getPage("https://www.youtube.com/feeds/videos.xml?channel_id=UCweCc7bSMX5J4jEH7HFImng", 0, true);
                    event.getChannel().sendMessage(info.build()).queue(message -> {
                        message.addReaction("⬅️").queue();
                        message.addReaction("➡️").queue();
                        message.addReaction("❌").queue();
                    });
                }
                if(args[1].equalsIgnoreCase("OrdinaryThings")){
                    EmbedBuilder info = ArticleManager.getPage("https://www.youtube.com/feeds/videos.xml?channel_id=UCxLYtICsUCWdr1YPrj5DtwA", 0, true);
                    event.getChannel().sendMessage(info.build()).queue(message -> {
                        message.addReaction("⬅️").queue();
                        message.addReaction("➡️").queue();
                        message.addReaction("❌").queue();
                    });
                }
                if(args[1].equalsIgnoreCase("thenewboston")){
                    EmbedBuilder info = ArticleManager.getPage("https://www.youtube.com/feeds/videos.xml?channel_id=UCJbPGzawDH1njbqV-D5HqKw", 0, true);
                    event.getChannel().sendMessage(info.build()).queue(message -> {
                        message.addReaction("⬅️").queue();
                        message.addReaction("➡️").queue();
                        message.addReaction("❌").queue();
                    });
                }
                if(args[1].equalsIgnoreCase("AbdulBari")){
                    EmbedBuilder info = ArticleManager.getPage("https://www.youtube.com/feeds/videos.xml?channel_id=UCZCFT11CWBi3MHNlGf019nw", 0, true);
                    event.getChannel().sendMessage(info.build()).queue(message -> {
                        message.addReaction("⬅️").queue();
                        message.addReaction("➡️").queue();
                        message.addReaction("❌").queue();
                    });
                }
            }
        }


        if(args[0].equalsIgnoreCase( Main.prefix + "javapapers")){


            EmbedBuilder info = ArticleManager.getPage("https://javapapers.com/category/java/feed/",0,true);

            event.getChannel().sendMessage(info.build()).queue(message -> {
                                                        message.addReaction("⬅️").queue();
                                                        message.addReaction("➡️").queue();
                                                        message.addReaction("❌"   ).queue();
                                                        });

        }

        if(args[0].equalsIgnoreCase( Main.prefix + "mkyong")){

            ;
            EmbedBuilder info = ArticleManager.getPage("https://mkyong.com/feed/",0,true);

            event.getChannel().sendMessage(info.build()).queue(message -> {
                message.addReaction("⬅️").queue();
                message.addReaction("➡️").queue();
                message.addReaction("❌"   ).queue();
            });
        }

        if(args[0].equalsIgnoreCase( Main.prefix + "javacodegeeks")){


            EmbedBuilder info = ArticleManager.getPage("https://www.javacodegeeks.com/feed",0,true);

            event.getChannel().sendMessage(info.build()).queue(message -> {
                message.addReaction("⬅️").queue();
                message.addReaction("➡️").queue();
                message.addReaction("❌"   ).queue();
            });

        }

        if(args[0].equalsIgnoreCase( Main.prefix + "awsamazon")){


            EmbedBuilder info = ArticleManager.getPage("https://aws.amazon.com/blogs/developer/category/programing-language/java/feed/",0,true);

            event.getChannel().sendMessage(info.build()).queue(message -> {
                message.addReaction("⬅️").queue();
                message.addReaction("➡️").queue();
                message.addReaction("❌"   ).queue();
            });

        }

        if(args[0].equalsIgnoreCase( Main.prefix + "stackabuse")){

            EmbedBuilder info = ArticleManager.getPage("https://stackabuse.com/rss/",0,true);

            event.getChannel().sendMessage(info.build()).queue(message -> {
                message.addReaction("⬅️").queue();
                message.addReaction("➡️").queue();
                message.addReaction("❌"   ).queue();
            });

        }

        if(args[0].equalsIgnoreCase( Main.prefix + "freeSearch"))
        {
            if(args.length != 2){
                event.getChannel().sendMessage("Invalid number of arguments").queue();
            }
            else{
                EmbedBuilder info = ArticleManager.getPage(args[1],0,true);

                event.getChannel().sendMessage(info.build()).queue(message -> {
                    message.addReaction("⬅️").queue();
                    message.addReaction("➡️").queue();
                    message.addReaction("❌"   ).queue();
                });
            }
        }

        if(args[0].equalsIgnoreCase( Main.prefix + "reddit" )){
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

            EmbedBuilder info = ArticleManager.getPage(redditAddress,0,true);
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
        System.out.println(urlAddress);
        return urlAddress;
    }


}
