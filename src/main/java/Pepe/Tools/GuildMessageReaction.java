package Pepe.Tools;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


import java.util.List;

public class GuildMessageReaction extends ListenerAdapter {

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event){


        if(event.getReactionEmote().getName().equals("⬅️") && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
            String tempLink = "";
            int tempNumber = 0;
            List<Message> messageHistory = event.getChannel().getHistory().retrievePast(100).complete();
            for ( Message m  : messageHistory) {
                if (m.getId().equals(event.getMessageId())) {
                    for (MessageEmbed l : m.getEmbeds()) {
                        //System.out.println(l.getFooter().getText());
                        String messageInfo = l.getFooter().getText();
                        tempLink = messageInfo.substring(0, messageInfo.indexOf(" "));
                        tempNumber = Integer.parseInt(messageInfo.substring(messageInfo.indexOf(" ") + 1));
                        //System.out.println();
                        //System.out.println(tempNumber + " " + tempLink);
                    }
                    break;
                }
            }
            EmbedBuilder info = ArticleManager.rssPage(tempLink, tempNumber-1);
            event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();
        }

        if(event.getReactionEmote().getName().equals("➡️") && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
            long startTime = System.nanoTime();

            String tempLink = "";
            int tempNumber = 0;
            List<Message> messageHistory = event.getChannel().getHistory().retrievePast(100).complete();
            for ( Message m  : messageHistory) {
                if (m.getId().equals(event.getMessageId())) {
                    for (MessageEmbed l : m.getEmbeds()) {
                        //System.out.println(l.getFooter().getText());
                        String messageInfo = l.getFooter().getText();
                        tempLink = messageInfo.substring(0, messageInfo.indexOf(" "));
                        tempNumber = Integer.parseInt(messageInfo.substring(messageInfo.indexOf(" ") + 1));
                        //System.out.println();
                        //System.out.println(tempNumber + " " + tempLink);
                    }
                    break;
                }
            }
            EmbedBuilder info = ArticleManager.rssPage(tempLink, tempNumber+1);
            event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();
            long endTime = System.nanoTime();

            long duration = (endTime - startTime)/100000000;  //divide by 1000000 to get milliseconds.
            System.out.println(duration);
        }
        if(event.getReactionEmote().getName().equals("❌") && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
            event.getChannel().deleteMessageById( event.getMessageId() ).queue();
        }
    }


    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event)
    {
        if(event.getReactionEmote().getName().equals("⬅️")) {

            String tempLink = "";
            int tempNumber = 0;
            List<Message> messageHistory = event.getChannel().getHistory().retrievePast(100).complete();
            for ( Message m  : messageHistory) {
                if (m.getId().equals(event.getMessageId())) {
                    for (MessageEmbed l : m.getEmbeds()) {
                        //System.out.println(l.getFooter().getText());
                        String messageInfo = l.getFooter().getText();
                        tempLink = messageInfo.substring(0, messageInfo.indexOf(" "));
                        tempNumber = Integer.parseInt(messageInfo.substring(messageInfo.indexOf(" ") + 1));
                        //System.out.println();
                        //System.out.println(tempNumber + " " + tempLink);
                    }
                    break;
                }
            }
            EmbedBuilder info = ArticleManager.rssPage(tempLink, tempNumber-1);
            event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();

        }
        if(event.getReactionEmote().getName().equals("➡️")) {

            String tempLink = "";
            int tempNumber = 0;
            List<Message> messageHistory = event.getChannel().getHistory().retrievePast(100).complete();
            for ( Message m  : messageHistory) {
                if (m.getId().equals(event.getMessageId())) {
                    for (MessageEmbed l : m.getEmbeds()) {
                        //System.out.println(l.getFooter().getText());
                        String messageInfo = l.getFooter().getText();
                        tempLink = messageInfo.substring(0, messageInfo.indexOf(" "));
                        tempNumber = Integer.parseInt(messageInfo.substring(messageInfo.indexOf(" ") + 1));
                        //System.out.println();
                        //System.out.println(tempNumber + " " + tempLink);
                    }
                    break;
                }
            }
            EmbedBuilder info = ArticleManager.rssPage(tempLink, tempNumber+1);
            event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();
        }
    }



}