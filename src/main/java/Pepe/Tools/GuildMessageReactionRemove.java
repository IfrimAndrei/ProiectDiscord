package Pepe.Tools;

import Datatypes.ArticleManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class GuildMessageReactionRemove extends ListenerAdapter {

    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event)
    {
        if(event.getReactionEmote().getName().equals("⬅️")) {
            previousArticle( getEventMessage(event),event);

        }
        if(event.getReactionEmote().getName().equals("➡️")) {
            nextArticle( getEventMessage(event),event);
        }
    }


    public Message getEventMessage(GuildMessageReactionRemoveEvent event){
        List<Message> messageHistory = event.getChannel().getHistory().retrievePast(100).complete();
        for ( Message m  : messageHistory)
            if(m.getId().equals( event.getMessageId() )){
                return m;
            }
        return null;
    }

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



}
