package Pepe.Tools;
import RSS.Article;
import RSS.RSSReader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Collection;

public class Commands extends ListenerAdapter {
    private static int javapapersPage=1;
    private static RSSReader myReader = new RSSReader();
    public static int getJavapapersPage( ) {
        return javapapersPage;
    }

    public static void setJavapapersPage(int javapapersPage) {
        Commands.javapapersPage = javapapersPage;
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split( " " );
        if(args[0].equalsIgnoreCase( "uwu" )){
            event.getChannel().sendMessage(">W<").queue();
        }
        if(args[0].equals( "wait") && args[1].equals( "what" )){

            Collection<Message> messages = event.getChannel().getHistory().retrievePast(2).complete();
            event.getChannel().deleteMessages( messages ).queue();
        }
        if(args[0].equals( Main.prefix + "clear")){
            if(args.length!=2){
                System.out.println("eroare1");
            }
            else{
                int number =Integer.parseInt(args[1]);
                if(!(number>0 && number<11)){
                    System.out.println("eroare2");
                }
                else{
                    Collection<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])+1).complete();
                    event.getChannel().deleteMessages( messages ).queue();
                }

            }
        }
        if(args[0].equalsIgnoreCase( Main.prefix + "info" )){
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Pepe Bot Info");
            info.setDescription( "Getting Better Every Day");
            info.setFooter("Created by Sushy ",event.getMember().getUser().getAvatarUrl());

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
                                ~javapapers
                                ~mkyong
                                ~reddit [subreddit_name] ...[extra]
                            And other secret commands :wink:
                            """
                                            ).queue();
        }
        if(args[0].equalsIgnoreCase( Main.prefix + "javapapers")){
            myReader.clear();
            myReader.readRSSFeed( "https://javapapers.com/category/java/feed/" );
            javapapersPage=1;
            EmbedBuilder info = rssPage( javapapersPage );
            event.getChannel().sendMessage(info.build()).queue(message -> {
                                                        message.addReaction("⬅️").queue();
                                                        message.addReaction("➡️").queue();
                                                        message.addReaction("❌"   ).queue();
                                                        });
            info.clear();


        }
        if(args[0].equalsIgnoreCase( Main.prefix + "mkyong")){
            myReader.clear();
            myReader.readRSSFeed( "https://mkyong.com/feed/");
            javapapersPage=1;
            EmbedBuilder info = rssPage( javapapersPage );

            event.getChannel().sendMessage(info.build()).queue(message -> {
                message.addReaction("⬅️").queue();
                message.addReaction("➡️").queue();
                message.addReaction("❌"   ).queue();
            });
            info.clear();
        }
        if(args[0].equalsIgnoreCase( Main.prefix + "reddit" )){

            myReader.clear();
            if(args.length==4) {
                myReader.rssRedditFeed( args[1], args[2], args[3] );
            }
            else if(args.length==3) {
                myReader.rssRedditFeed( args[1], args[2], null );
            }
            else if(args.length==2) {
                myReader.rssRedditFeed( args[1], null, null );
            }
            else if(args.length==1){
                event.getChannel().sendMessage("For better details add a subreddit in the command.").queue();
                myReader.rssRedditFeed( null,null,null );
            }
            else{
                event.getChannel().sendMessage("Too many arguments.").queue();
            }

            javapapersPage=1;
            EmbedBuilder info = rssPage( javapapersPage );

            event.getChannel().sendMessage(info.build()).queue(message -> {
                message.addReaction("⬅️").queue();
                message.addReaction("➡️").queue();
                message.addReaction("❌"   ).queue();
            });
            info.clear();
        }

    }
    public EmbedBuilder rssPage(int page){


        Article myArticle = myReader.getRssArticles().get(page);
        EmbedBuilder info = new EmbedBuilder();
        info.setColor( new Color( 231, 190, 76 ) );
        if(myArticle.getImageURL()==null)
            info.setThumbnail( "https://javapapers.com/favicon-32x32.png?v=261118" );
        else{
            info.setThumbnail( myArticle.getImageURL() );
        }

        info.setTitle( myArticle.getTitle() );
        info.setDescription( myArticle.getDescription() + '\n' + myArticle.getLink()  );
        //info.setFooter( myArticle.getLink() );

        return info;
    }


}
