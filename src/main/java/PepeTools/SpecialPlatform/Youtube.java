package PepeTools.SpecialPlatform;

import PepeTools.Commands;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Manages the youtube articles
 */
public class Youtube {
    Commands com;
    public Youtube(Commands com){
        this.com = com;
    }

    /**
     * Calls for the youtube URL of the channel given as parameter using defaultYoutubeSearch and if it finds the URL it calls createArticle with that URL
     * @param args The name of the youtube channel
     * @param event Special parameter
     * @see PepeTools.Commands
     */
    public void createYoutubeArticle(String[] args, GuildMessageReceivedEvent event){
        String channelName;
        if(args.length == 1)
        {
            event.getChannel().sendMessage( """
                            Exemple for a youtube channel list:\s
                              -  LinusTechTips
                              -  OrdinaryThings
                              -  thenewboston
                              -  GMHikaru
                              -  AbdulBari
                            """
            ).queue();
        }
        else if(args.length >= 2){
            channelName = args[1].toLowerCase();
            for(int i=2;i<args.length;i++)
                channelName = channelName.concat(args[i].toLowerCase());
            String youtubeLink;

            switch (channelName){
                case "linustechtips"  -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCXuqSBlHAE6Xw-yeJA0Tunw";
                case "gmhikaru"       -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCweCc7bSMX5J4jEH7HFImng";
                case "ordinarythings" -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCxLYtICsUCWdr1YPrj5DtwA";
                case "thenewboston"   -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCJbPGzawDH1njbqV-D5HqKw";
                case "abdulbari"      -> youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=UCZCFT11CWBi3MHNlGf019nw";
                default               -> youtubeLink = defaultYoutubeSearch(channelName, event);
            }


            if(!youtubeLink.equals("nu exista")) {
                com.createArticle( youtubeLink, event );
            }
            else{
                event.getChannel().sendMessage("Error! YouTube channel not found.").queue();
            }

        }
    }

    /**
     * Sends a request for the URL address made with the channel name given as a parameter
     * @param channelName The name of the youtube channel being searched for
     * @param event Special parameter
     * @return youtubeLink - The URL for the article
     */
    public String defaultYoutubeSearch(String channelName, GuildMessageReceivedEvent event){
        String youtubeLink;
        String source = "https://www.youtube.com/c/";
        source = source.concat(channelName);
        System.out.println(source);
        try {
            URL sourceUrl = new URL(source);
            HttpURLConnection conn = (HttpURLConnection) sourceUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                source = "https://www.youtube.com/user/";
                source = source.concat(channelName);

                try {
                    sourceUrl = new URL(source);
                    conn = (HttpURLConnection) sourceUrl.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    if (conn.getResponseCode() != 200) {
                        event.getChannel().sendMessage("Error! YouTube channel not found.").queue();
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));

                    String page = in.readLine();
                    while (!page.contains("externalId")) {
                        page = in.readLine();
                        System.out.println(page);
                    }
                    int firstPos = page.indexOf("externalId\":");
                    String temp = page.substring(firstPos + "externalId\":".length() + 1, firstPos + "externalId\":".length() + 25);
                    System.out.println(temp);

                    youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=";
                    youtubeLink = youtubeLink.concat(temp);
                    return youtubeLink;
                }catch (IOException e) {
                    e.printStackTrace();
                    return "nu exista";
                }
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String page = in.readLine();
            while(!page.contains("externalId")) {
                page = in.readLine();
                System.out.println(page);
            }
            int firstPos  = page.indexOf("externalId\":");
            String temp = page.substring(firstPos + "externalId\":".length() + 1, firstPos + "externalId\":".length() + 25);
            System.out.println(temp);

            youtubeLink = "https://www.youtube.com/feeds/videos.xml?channel_id=";
            youtubeLink = youtubeLink.concat(temp);
            return youtubeLink;

        } catch (IOException e) {
            e.printStackTrace();
            return "nu exista";
        }
    }
}
