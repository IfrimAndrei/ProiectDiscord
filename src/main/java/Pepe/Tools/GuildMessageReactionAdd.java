package Pepe.Tools;

import Datatypes.ArticleManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


import java.util.List;

public class GuildMessageReactionAdd extends ListenerAdapter {

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {


        if ( event.getReactionEmote().getName().equals( "⬅️" ) && !event.getMember().getUser().equals( event.getJDA().getSelfUser() ) ) {
            previousArticle( getEventMessage( event ),event );
        }

        if ( event.getReactionEmote().getName().equals( "➡️" ) && !event.getMember().getUser().equals( event.getJDA().getSelfUser() ) ) {
            long startTime = System.nanoTime();

            nextArticle( getEventMessage( event ),event );

            long endTime = System.nanoTime();

            long duration = (endTime - startTime) / 100000000;
            System.out.println( duration );
        }
        if (event.getReactionEmote().getName().equals( "❌" ) && !event.getMember().getUser().equals( event.getJDA().getSelfUser() ) ){
            event.getChannel().deleteMessageById( event.getMessageId() ).queue();
        }

        if (event.getReactionEmote().getName().equals( "🔄" ) && !event.getMember().getUser().equals( event.getJDA().getSelfUser() ) ){
            refreshArticle( getEventMessage(event), event);
        }
    }

    public Message getEventMessage(GuildMessageReactionAddEvent event){
        List<Message> messageHistory = event.getChannel().getHistory().retrievePast(100).complete();
        for ( Message m  : messageHistory)
            if(m.getId().equals( event.getMessageId() )){
                return m;
            }
        return null;
    }

    public void previousArticle(Message message,GuildMessageReactionAddEvent event){
        String tempLink = "";
        int tempNumber = 0;

        for (MessageEmbed l : message.getEmbeds()) {
            String messageInfo = l.getFooter().getText();
            tempLink = messageInfo.substring(0, messageInfo.indexOf(" "));
            tempNumber = Integer.parseInt(messageInfo.substring(messageInfo.indexOf(" ") + 1));
        }

        EmbedBuilder info = ArticleManager.getPage(tempLink, tempNumber-1,false);
        event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();
    }

    public void nextArticle(Message message,GuildMessageReactionAddEvent event){
        String tempLink = "";
        int tempNumber = 0;

        for ( MessageEmbed l : message.getEmbeds()) {
            String messageInfo = l.getFooter().getText();
            tempLink = messageInfo.substring(0, messageInfo.indexOf(" "));
            tempNumber = Integer.parseInt(messageInfo.substring(messageInfo.indexOf(" ") + 1));
        }

        EmbedBuilder info = ArticleManager.getPage(tempLink, tempNumber+1,false);
        event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();
    }

    public void refreshArticle(Message message, GuildMessageReactionAddEvent event){
        String tempLink = "";

        for(MessageEmbed m : message.getEmbeds()){
            String messageInfo = m.getFooter().getText();
            tempLink = messageInfo.substring(0,messageInfo.indexOf(" "));
            System.out.println("uwu");
        }

        EmbedBuilder info = ArticleManager.getPage(tempLink, 0, true);
        event.getChannel().editMessageById(event.getMessageId(),info.build()).queue();
    }

}