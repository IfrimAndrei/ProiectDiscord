package Pepe.Tools;
import RSS.Article;
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
                                ~javapapers
                                ~mkyong
                                ~reddit [subreddit_name] ...[extra]
                            And other secret commands :wink:
                            """
                                            ).queue();
        }

        if(args[0].equalsIgnoreCase( Main.prefix + "javapapers")){
            event.getMessage().delete().queue();

            ArticleManager.setSite("javapapers" );
            EmbedBuilder info = ArticleManager.newPage("https://javapapers.com/category/java/feed/");

            event.getChannel().sendMessage(info.build()).queue(message -> {
                                                        message.addReaction("⬅️").queue();
                                                        message.addReaction("➡️").queue();
                                                        message.addReaction("❌"   ).queue();
                                                        });

        }

        if(args[0].equalsIgnoreCase( Main.prefix + "mkyong")){

            event.getMessage().delete().queue();
            ArticleManager.setSite( "mkyong" );
            EmbedBuilder info = ArticleManager.newPage("https://mkyong.com/feed/");

            event.getChannel().sendMessage(info.build()).queue(message -> {
                message.addReaction("⬅️").queue();
                message.addReaction("➡️").queue();
                message.addReaction("❌"   ).queue();
            });
            info.clear();
        }

        if(args[0].equalsIgnoreCase( Main.prefix + "reddit" )){
            event.getMessage().delete().queue();
            ArticleManager.getMyReader().clear();
            ArticleManager.setSite( "reddit" );


            if(args.length==4) {
                ArticleManager.getMyReader().rssRedditFeed( args[1], args[2], args[3] );
            }
            else if(args.length==3) {
                ArticleManager.getMyReader().rssRedditFeed( args[1], args[2], null );
            }
            else if(args.length==2) {
                ArticleManager.getMyReader().rssRedditFeed( args[1], null, null );
            }
            else if(args.length==1){
                event.getChannel().sendMessage("For better details add a subreddit in the command.").queue();
                ArticleManager.getMyReader().rssRedditFeed( null,null,null );
            }
            else{
                event.getChannel().sendMessage("Too many arguments.").queue();
            }
            ArticleManager.setPageNumber( 0 );
            EmbedBuilder info = ArticleManager.rssPage( 0 );


            event.getChannel().sendMessage(info.build()).queue(message -> {
                message.addReaction("⬅️").queue();
                message.addReaction("➡️").queue();
                message.addReaction("❌"   ).queue();
            });
        }

    }



}
