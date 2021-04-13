package Pepe.Tools;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {
    public static JDA jda;
    public static String prefix = "~";

    public static void main(String[] args) throws LoginException{
        jda=JDABuilder.createDefault("ODMxNDY2MTYwMDIwMTI3NzY0.YHVpNA.Ei9GqDYRUSb7Fkj6YXQtwNTiHWE").build();
        jda.getPresence().setActivity( Activity.watching( "paint drying" ) );

        jda.addEventListener( new Commands() );
        jda.addEventListener( new GuildMessageReactionAdd() );
    }
}
