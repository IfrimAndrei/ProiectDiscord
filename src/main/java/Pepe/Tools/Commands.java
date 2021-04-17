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
    public static String site;

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
                                ~javapapers
                                ~mkyong
                                ~reddit [subreddit_name] ...[extra]
                            And other secret commands :wink:
                            """
                                            ).queue();
        }

        if(args[0].equalsIgnoreCase( Main.prefix + "javapapers")){
            myReader.clear();
            site="javapapers";
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
            site="mkyong";
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
            site="reddit";
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

        if( site.equals( "javapapers" ) )
            info.setThumbnail( "https://javapapers.com/favicon-32x32.png?v=261118" );
        else if ( site.equals( "mkyong" ) ){
            info.setThumbnail( "https://i.pinimg.com/originals/41/df/26/41df26f532af6e8cfddc2b217e096c49.png" );
        }
        else if (site.equals( "reddit" ) ){
            info.setThumbnail( "https://upload.wikimedia.org/wikipedia/ro/thumb/b/b4/Reddit_logo.svg/1200px-Reddit_logo.svg.png" );

        }
        info.setImage( myArticle.getImageURL());
        info.setTitle( myArticle.getTitle() );
        info.setDescription( myArticle.getDescription() + '\n' + myArticle.getLink()  );

        //info.setFooter( myArticle.getLink() );

        return info;
    }


}
