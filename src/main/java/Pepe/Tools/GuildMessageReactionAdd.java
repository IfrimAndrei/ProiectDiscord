package Pepe.Tools;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Collection;

public class GuildMessageReactionAdd extends ListenerAdapter {
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event){

        if(event.getReactionEmote().getName().equals("⬅️") && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {


            Commands textCommand = new Commands();

            Commands.setJavapapersPage(Commands.getJavapapersPage()-1  );
            if(Commands.getJavapapersPage()==0 ){
                Commands.setJavapapersPage( 1 );
            }
            EmbedBuilder info = textCommand.rssPage( Commands.getJavapapersPage() );

            event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();

        }
        if(event.getReactionEmote().getName().equals("➡️") && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {


            Commands textCommand = new Commands();
            Commands.setJavapapersPage(Commands.getJavapapersPage()+1  );
            EmbedBuilder info = textCommand.rssPage( Commands.getJavapapersPage() );

            event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();
        }
        if(event.getReactionEmote().getName().equals("❌") && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
            //event.getChannel().deleteMessageById( event.getMessageId() ).queue();
            //Collection<Message> messages = event.getChannel().getHistory().retrievePast(2).complete();
            event.getChannel().deleteMessageById( event.getMessageId() ).queue();
        }
    }
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event)
    {
        if(event.getReactionEmote().getName().equals("⬅️")) {
            Commands textCommand = new Commands();

            Commands.setJavapapersPage(Commands.getJavapapersPage()-1  );
            if(Commands.getJavapapersPage()==0 ){
                Commands.setJavapapersPage( 1 );
            }
            EmbedBuilder info = textCommand.rssPage( Commands.getJavapapersPage() );

            event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();
        }
        if(event.getReactionEmote().getName().equals("➡️")) {
            Commands textCommand = new Commands();

            Commands.setJavapapersPage(Commands.getJavapapersPage()+1  );

            EmbedBuilder info = textCommand.rssPage( Commands.getJavapapersPage() );

            event.getChannel().editMessageById( event.getMessageId(),info.build() ).queue();
        }
    }

}