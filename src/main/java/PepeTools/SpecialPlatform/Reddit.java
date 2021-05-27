package PepeTools.SpecialPlatform;

import PepeTools.Commands;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;


/**
 * Manages the reddit articles
 */
public class Reddit {

    Commands com;
    public Reddit(Commands com){
        this.com = com;
    }

    /**
     * Returns urlAddress to reddit based on the parameters
     * @param subreddit reddit forum
     * @param searchType Hot/Top/New
     * @param timePeriod Year/Month/Week/Day/Hour/All
     * @return urlAddress
     * @see PepeTools.Commands
     */
    public String createRedditAddress(String subreddit, String searchType, String timePeriod){
        String urlAddress;
        if (subreddit != null) {
            if (searchType == null)
                searchType = "hot";
            urlAddress = "https://www.reddit.com/r/";
            urlAddress += subreddit;
            urlAddress += "/" + searchType;
            urlAddress += "/.rss";
            if (searchType.equals("top") && timePeriod != null) {
                urlAddress += "?t=" + timePeriod;
            }
        } else {
            urlAddress = "https://www.reddit.com/.rss";
        }
        if(timePeriod!=null)
            urlAddress+="&";
        else
            urlAddress+="?";
        urlAddress+="limit=100";
        System.out.println(urlAddress);

        return urlAddress;
    }

    /**
     * Creates the URL with createRedditAddress and sends it to the createArticle method.
     * @param args The user command
     * @param event Special parameter
     * @see PepeTools.Commands
     */
    public void createRedditArticle(String[] args, GuildMessageReceivedEvent event){
        {
            String redditAddress = "";
            boolean ok = false;
            if(args.length==4) {
                redditAddress = createRedditAddress( args[1], args[2], args[3] );
                ok=true;
            }
            else if(args.length==3) {
                redditAddress = createRedditAddress( args[1], args[2], null );
                ok=true;
            }
            else if(args.length==2) {
                redditAddress = createRedditAddress( args[1], null, null );
                ok=true;
            }
            else if(args.length==1){
                event.getChannel().sendMessage("Add a subreddit too").queue();
            }
            else{
                event.getChannel().sendMessage("Too many arguments.").queue();
            }


            if(ok) {
                com.createArticle( redditAddress, event );
            }
        }
    }


}
