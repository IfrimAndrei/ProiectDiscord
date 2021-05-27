package PepeTools;

import Datatypes.ArticleManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

/**
 * Manages the state of an EmbedBuilder
 */
public class GuildMessageReactionRemove extends ListenerAdapter {

    /**
     * Manages the button presses on the EmbedBuilders when the user removes his emote
     * @param event Special parameter
     */
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event)
    {
        if(event.getReactionEmote().getName().equals("\u2B05")) {
            previousArticle( getEventMessage(event),event);

        }
        if(event.getReactionEmote().getName().equals("\u27A1")) {
            nextArticle( getEventMessage(event),event);
        }

        if(event.getReactionEmote().getName().equals("\uD83D\uDD04")){
            refreshArticle(getEventMessage(event), event);
        }
    }


    /**
     * Finds the message that has a reaction removed from the past 100 messages.
     * @param event Special parameter
     * @return m - The text from the found message or null
     */
    public Message getEventMessage(GuildMessageReactionRemoveEvent event){
        List<Message> messageHistory = event.getChannel().getHistory().retrievePast(100).complete();
        for ( Message m  : messageHistory)
            if(m.getId().equals( event.getMessageId() )){
                return m;
            }
        return null;
    }


    /**
     * Request for the message that has a reaction removed and transitions 1 article backward using ArticleManager.getPage
     * @param message The message where the transition is used in
     * @param event Special parameter
     * @see ArticleManager
     */
    public void previousArticle(Message message,GuildMessageReactionRemoveEvent event){
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
     * Request for the message that has a reaction removed and transitions 1 article forward using ArticleManager.getPage
     * @param message The message where the transition is used in
     * @param event Special parameter
     * @see ArticleManager
     */
    public void nextArticle(Message message,GuildMessageReactionRemoveEvent event){
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
     * Request for the message that has a reaction removed and transitions to the first article of the list using ArticleManage.getPage
     * @param message The message where the transition is used in
     * @param event Special parameter
     * @see ArticleManager
     */
    public void refreshArticle(Message message, GuildMessageReactionRemoveEvent event){
        String tempLink = "";

        for(MessageEmbed m : message.getEmbeds()){
            String messageInfo = m.getFooter().getText();
            tempLink = messageInfo.substring(0,messageInfo.indexOf(" "));
        }

        EmbedBuilder info = ArticleManager.getPage(tempLink, 0, true);
        event.getChannel().editMessageById(event.getMessageId(),info.build()).queue();
    }

}
