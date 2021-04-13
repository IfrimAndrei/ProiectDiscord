package RSS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

//webography https://www.youtube.com/watch?v=xiK-DH74oJg

public class RSSReader {
    private List<Article> rssArticles = new LinkedList<>();

    public List<Article> getRssArticles( ) {
        return rssArticles;
    }


    public String readRSSFeed(String urlAddress){
        try{
            URL rssUrl = new URL (urlAddress);
            BufferedReader in = new BufferedReader(new InputStreamReader(rssUrl.openStream()));

            String line;
            Article article = new Article();
            while((line=in.readLine())!=null){
                if(line.contains("<title>")){
                    System.out.println(line);
                    int firstPos = line.indexOf("<title>");
                    String temp = line.substring(firstPos);
                    temp=temp.replace("<title>","");
                    int lastPos = temp.indexOf("</title>");
                    temp = temp.substring(0,lastPos);

                    article.setTitle( temp );
                }
                else if(line.contains("<link>")){
                    System.out.println(line);
                    int firstPos = line.indexOf("<link>");
                    String temp = line.substring(firstPos);
                    temp=temp.replace("<link>","");
                    int lastPos = temp.indexOf("</link>");
                    temp = temp.substring(0,lastPos);

                    article.setLink( temp );
                }
                else if(line.contains("<description>")){
                    System.out.println(line);
                    int firstPos = line.indexOf("<description>");
                    String temp = line.substring(firstPos);
                    temp=temp.replace("<description><![CDATA[","");
                    int lastPos = temp.indexOf("</description>");
                    temp = temp.substring(0,lastPos+1);
                    temp=temp.replace("[&#8230;]]]><","...");
                    article.setDescription( temp );

                    rssArticles.add(article);
                    article = new Article();
                }
            }
            in.close();
            for(int i=0;i<=5;i++)
                System.out.println("\n");
            for(Article art : rssArticles)
            {
                System.out.println(art);
            }
            return null;
        } catch (MalformedURLException ue){
            System.out.println("Malformed URL");
        } catch (IOException ioe){
            System.out.println("Something went wrong reading the contents");
        }
        return null;
    }
}
