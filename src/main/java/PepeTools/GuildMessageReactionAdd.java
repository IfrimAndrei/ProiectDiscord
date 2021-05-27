package PepeTools;

import Datatypes.ArticleManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


import java.util.List;

/**
 * Manages the state of an EmbedBuilder
 */
public class GuildMessageReactionAdd extends ListenerAdapter {

    /**
     * Manages the button presses on the EmbedBuilders when the user reacts to an emote
     * @param event Special parameter
     */
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {


        if ( event.getReactionEmote().getName().equals( "\u2B05" ) && !event.getMember().getUser().equals( event.getJDA().getSelfUser() ) ) {
            previousArticle( getEventMessage( event ),event );
        }

        if ( event.getReactionEmote().getName().equals( "\u27A1" ) && !event.getMember().getUser().equals( event.getJDA().getSelfUser() ) ) {
            long startTime = System.nanoTime();

            nextArticle( getEventMessage( event ),event );

            long endTime = System.nanoTime();

            long duration = (endTime - startTime) / 100000000;
            System.out.println( duration );
        }
        if (event.getReactionEmote().getName().equals( "\u274C" ) && !event.getMember().getUser().equals( event.getJDA().getSelfUser() ) ){
            event.getChannel().deleteMessageById( event.getMessageId() ).queue();
        }

        if (event.getReactionEmote().getName().equals( "\uD83D\uDD04" ) && !event.getMember().getUser().equals( event.getJDA().getSelfUser() ) ){
            refreshArticle( getEventMessage(event), event);
        }
    }

    /**
     * Finds the message that has been emoted on from the past 100 messages.
     * @param event Special parameter
     * @return m - The text from the found message or null
     */
    public Message getEventMessage(GuildMessageReactionAddEvent event){
        List<Message> messageHistory = event.getChannel().getHistory().retrievePast(100).complete();
        for ( Message m  : messageHistory)
            if(m.getId().equals( event.getMessageId() )){
                return m;
            }
        return null;
    }

    /**
     * Request for the message that has been reacted to and transitions 1 article backward using ArticleManager.getPage
     * @param message The message where the transition is used in
     * @param event Special parameter
     * @see ArticleManager
     */
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

    /**
     * Request for the message that has been reacted to and transitions 1 article forward using ArticleManager.getPage
     * @param message The message where the transition is used in
     * @param event Special parameter
     * @see ArticleManager
     */
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

    /**
     * Request for the message that has been reacted to and transitions to the first article of the list using ArticleManage.getPage
     * @param message The message where the transition is used in
     * @param event Special parameter
     * @see ArticleManager
     */
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