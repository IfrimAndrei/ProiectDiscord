package Pepe.Tools;
import RSS.Article;
import RSS.RSSReader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class Commands extends ListenerAdapter {
    private static int javapapersPage=1;

    public static int getJavapapersPage( ) {
        return javapapersPage;
    }

    public static void setJavapapersPage(int javapapersPage) {
        Commands.javapapersPage = javapapersPage;
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split( " " );
        if(args[0].equalsIgnoreCase( "uwu" )){
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(">W<").queue();
        }
        if(args[0].equalsIgnoreCase( Main.prefix + "info" )){
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Pepe Bot Info");
            info.setDescription( "Getting Better Every Day");
            info.setFooter("Created by Sushy",event.getMember().getUser().getAvatarUrl());
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(info.build()).queue();
            info.clear();
        }
        if(args[0].equalsIgnoreCase( Main.prefix + "commands" ))
        {
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("Commands that I can help with: \n" +
                                                "~info\n"+
                                                "~javapapers"
                                            ).queue();
        }
        if(args[0].equalsIgnoreCase( Main.prefix + "javapapers")){
            javapapersPage=1;
            EmbedBuilder info = javapapersPage( javapapersPage );
            event.getChannel().sendMessage(info.build()).queue(message -> {
                                                        message.addReaction("⬅️").queue();
                                                        message.addReaction("➡️").queue();
                                                        message.addReaction("❌"   ).queue();
                                                        });
            info.clear();


        }

    }
    public EmbedBuilder javapapersPage (int page){
        RSSReader myReader = new RSSReader();
        myReader.readRSSFeed( "https://javapapers.com/category/java/feed/" );
        Article myArticle = myReader.getRssArticles().get(page);
        EmbedBuilder info = new EmbedBuilder();
        info.setColor( new Color( 231, 190, 76 ) );
        info.setTitle( myArticle.getTitle() );
        info.setDescription( myArticle.getDescription() );
        info.setFooter( myArticle.getLink() );
        info.setThumbnail( "https://javapapers.com/favicon-32x32.png?v=261118" );

        return info;
    }

}
