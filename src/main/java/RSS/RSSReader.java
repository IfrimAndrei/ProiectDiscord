package RSS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class RSSReader {
    private List<Article> rssArticles = new LinkedList<>();

    public List<Article> getRssArticles( ) {
        return rssArticles;
    }

    public static void main(String[] args){
        //RSSReader test= new RSSReader();
        for(int i=0;i<1;i++) {
            RSSReader test= new RSSReader();
            test.rssRedditFeed( null,null,null );
        }
    }
    public void clear(){
        rssArticles=new LinkedList<>();
    }

    public void readRSSFeed(String urlAddress){
        try{
            URL rssUrl = new URL (urlAddress);
            BufferedReader in = new BufferedReader(new InputStreamReader(rssUrl.openStream()));

            String line;
            Article article = new Article();
            int articlePieces=0;
            while((line=in.readLine())!=null){
                if(line.contains("<title>")){
                    articlePieces++;
                    //System.out.println(line);
                    int firstPos = line.indexOf("<title>");
                    String temp = line.substring(firstPos);
                    temp=temp.replace("<title>","");
                    int lastPos = temp.indexOf("</title>");
                    temp = temp.substring(0,lastPos);

                    article.setTitle( temp );
                }
                else if(line.contains("<link>")){
                    articlePieces++;
                    //System.out.println(line);
                    int firstPos = line.indexOf("<link>");
                    String temp = line.substring(firstPos);
                    temp=temp.replace("<link>","");
                    int lastPos = temp.indexOf("</link>");
                    temp = temp.substring(0,lastPos);

                    article.setLink( temp );
                }
                else if(line.contains("<description>")){
                    articlePieces++;
                    //System.out.println(line);
                    int firstPos = line.indexOf("<description>");
                    String temp = line.substring(firstPos);
                    temp=temp.replace("<description><![CDATA[","");
                    //int lastPos = temp.indexOf("</description>");
                    //temp = temp.substring(0,lastPos+1);
                    temp=temp.replace("[&#8230;]","");//...
                    temp=temp.replace("</p>","");
                    temp=temp.replace("<p>","");
                    temp=temp.replace("</description>","");
                    temp=temp.replace("]]>","");
                    article.setDescription( temp );
                }
                if(articlePieces==3){
                    rssArticles.add(article);
                    article = new Article();
                    articlePieces=0;
                }
            }
            in.close();
            for(int i=0;i<=5;i++)
                System.out.println("\n");
            for(Article art : rssArticles)
            {
                System.out.println(art);
            }

        } catch (MalformedURLException ue){
            System.out.println("Malformed URL");
        } catch (IOException ioe){
            System.out.println("Something went wrong reading the contents");
        }

    }
    public void rssRedditFeed(String subreddit,String searchType,String timePeriod) {
        String urlAddress;
        if(subreddit!=null)
        {
            if(searchType==null)
                searchType="hot";
            urlAddress = "https://www.reddit.com/r/";
            urlAddress += subreddit;
            urlAddress += "/" + searchType;
            urlAddress += "/.rss";
            if ( searchType.equals( "top" ) && timePeriod != null ) {
                urlAddress += "?t=" + timePeriod;
            }
        }
        else {
             urlAddress ="https://www.reddit.com/.rss";
        }
        System.out.println(urlAddress);

        String page;
        Article article = new Article();

        try {
            URL rssUrl = new URL( urlAddress );
            URLConnection con = rssUrl.openConnection();
            con.setRequestProperty( "User-Agent", "Chrome" );//Error 429 otherwise

            BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream(), StandardCharsets.UTF_8 ));
            page = in.readLine();
            //System.out.println( line );

            List<String> contents = new ArrayList<>(Arrays.asList(page.split("<author>")));
            contents.remove(0);
            int firstPos;
            int lastPos;
            String temp;
            for(String line : contents){
                System.out.println("\n" + line);

                firstPos = line.indexOf("<title>");
                lastPos = line.indexOf( "</title>" );
                article.setTitle( line.substring( firstPos+"<title>".length(),lastPos ));
                System.out.println(article.getTitle());

                if(subreddit!=null) {
                    String webPage = "https://www.reddit.com/r/" + subreddit + "/comments";
                    firstPos = line.indexOf( webPage );
                    temp = line.substring( firstPos );
                    lastPos = temp.indexOf( "&quot" );
                    temp = temp.substring( 0, lastPos );
                    article.setLink( temp );
                    System.out.println( article.getLink() );
                }

                firstPos = -1;
                firstPos = line.indexOf("https://i.");
                if(firstPos==-1)
                    System.out.println("nu are imagine");
                else {
                    temp = line.substring( firstPos );
                    lastPos = temp.indexOf( "&quot" );
                    temp = temp.substring( 0, lastPos );
                    article.setImageURL( temp );
                    System.out.println( article.getImageURL() );
                }

                firstPos= -1;
                firstPos = line.indexOf( "md&quot;&gt;&lt;p&gt;" );
                    if(firstPos==-1)
                        System.out.println("nu are descriere");
                    else {
                        temp = line.substring( firstPos + "md&quot;&gt;&lt;p&gt;".length() );
                        temp = temp.replace("&lt;/p&gt; &lt;p&gt;","\n");

                        lastPos = temp.indexOf( ";/p&gt" );
                        temp = temp.substring( 0, lastPos );
                        temp=temp.replace("&lt",".");
                        //temp = temp.replace("a href=&quot;","");
                        temp = temp.replace ("&quot;&gt;[link]","");
                        temp = temp.replace(".;/a&gt;","");
                        temp= temp.replace(";a href=&quot;","");
                        temp = temp.replace ("&;quot;&gt;","");
                        temp=temp.replace("&amp;quot;", "\"");
                        temp=temp.replace("&amp;#39;","'");
                        article.setDescription( temp );
                        System.out.println( article.getDescription() );
                        //rezolvat link-uri
                    }
                rssArticles.add(article);
                article=new Article();
            }

        } catch (Exception e) {
                e.printStackTrace();
        }

        }


}
