package Pepe.Tools;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Collection;

public class GuildMessageReaction extends ListenerAdapter {
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event){

        if(event.getReactionEmote().getName().equals("⬅️") && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {

            EmbedBuilder info = ArticleManager.previousPage();
            event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();

        }
        if(event.getReactionEmote().getName().equals("➡️") && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
            EmbedBuilder info = ArticleManager.nextPage();
            event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();
        }
        if(event.getReactionEmote().getName().equals("❌") && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
            event.getChannel().deleteMessageById( event.getMessageId() ).queue();
        }
    }
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event)
    {
        if(event.getReactionEmote().getName().equals("⬅️")) {

            EmbedBuilder info = ArticleManager.previousPage();
            event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();
        }
        if(event.getReactionEmote().getName().equals("➡️")) {

            EmbedBuilder info = ArticleManager.nextPage();
            event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();
        }
    }

}