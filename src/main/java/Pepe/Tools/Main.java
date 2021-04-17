package Pepe.Tools;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {
    public static JDA jda;
    public static String prefix = "~";

    public static void main(String[] args) throws LoginException{
        jda=JDABuilder.createDefault("ODMxNDY2MTYwMDIwMTI3NzY0.YHVpNA.f4wbAZ7W0x81rHDo6kOIeULFBrs").build();
        jda.getPresence().setActivity( Activity.watching( "paint drying" ) );

        jda.addEventListener( new Commands() );
        jda.addEventListener( new GuildMessageReaction() );
    }
}
