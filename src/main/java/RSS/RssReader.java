package RSS;

import Datatypes.Article;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages and Parses the RSS Feed
 */
public class RssReader {


    /**
     * Parses the RSS Feed, for a Reddit Feed, requested with getRSSFeed, into a list of articles to be displayed
     * @param urlAddress the urlAddress to which the request is sent
     * @return rssArticles - a list of articles from the RSS Feed found at the urlAddress
     * @see Article
     */
    public List<Article> readRedditFeed(String urlAddress) {
        List<Article> rssArticles = new LinkedList<>();

        String page;
        Article article = new Article();
        BufferedReader reader = null;
        try {
            reader = getRSSData(urlAddress);

            page = reader.readLine();

            List<String> contents = new ArrayList<>(Arrays.asList(page.split("<author>")));
            contents.remove(0);
            int firstPos;
            int lastPos;
            String temp;
            for (String line : contents) {
                System.out.println("\n" + line);

                firstPos = line.indexOf("<title>");
                lastPos = line.indexOf("</title>");
                article.setTitle(line.substring(firstPos + "<title>".length(), lastPos));
                System.out.println(article.getTitle());

            String subreddit = urlAddress.substring( urlAddress.indexOf( "/r/" ) +3 );
            subreddit = subreddit.substring ( 0, subreddit.indexOf( "/" ));
            System.out.println(subreddit);
            String webPage = "/comments";
            String subredditUrl = "https://www.reddit.com/r/" + subreddit ;
            firstPos = line.indexOf(webPage )- subredditUrl.length();
            temp = line.substring(firstPos);
            lastPos = temp.indexOf("&quot");
            temp = temp.substring(0, lastPos);
            article.setLink(temp);
            System.out.println(article.getLink());

                firstPos = line.indexOf("https://i.");
                if (firstPos == -1)
                    System.out.println("nu are imagine");
                else {
                    temp = line.substring(firstPos);
                    lastPos = temp.indexOf("&quot");
                    temp = temp.substring(0, lastPos);
                    article.setImageURL(temp);
                    System.out.println(article.getImageURL());
                }

                firstPos = line.indexOf("md&quot;&gt;&lt;p&gt;");
                if (firstPos == -1)
                    System.out.println("nu are descriere");
                else {
                    temp = line.substring(firstPos + "md&quot;&gt;&lt;p&gt;".length());
                    temp = temp.replace("&lt;/p&gt; &lt;p&gt;", "\n");

                    lastPos = temp.indexOf(";/p&gt");
                    temp = temp.substring(0, lastPos);
                    temp = temp.replace("&lt", ".");
                    //temp = temp.replace("a href=&quot;","");
                    temp = temp.replace("&quot;&gt;[link]", "");
                    temp = temp.replace(".;/a&gt;", "");
                    temp = temp.replace(";a href=&quot;", "");
                    temp = temp.replace("&;quot;&gt;", "");
                    temp = temp.replace("&amp;quot;", "\"");
                    temp = temp.replace("&amp;#39;", "'");
                    article.setDescription(temp);
                    System.out.println(article.getDescription());


                }
                rssArticles.add(article);
                article = new Article();
            }

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }   finally {
            if (reader != null) {
                try {
                    reader. close ();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return rssArticles;
    }

    /**
     * Parses the RSS Feed, for a nonReddit Feed, requested with getRSSFeed, into a list of articles to be displayed
     * @param urlAddress the urlAddress to which the request is sent
     * @return rssArticles - a list of articles from the RSS Feed found at the urlAddress
     * @see Article
     */
    public List<Article> readRSSFeed(String urlAddress) {
        List<Article> rssArticles = new LinkedList<>();

        BufferedReader reader=null;
        try {
            reader = getRSSData(urlAddress);

            String lines;
            Article article = new Article();

            StringBuilder page = new StringBuilder();
            while ((lines = reader.readLine()) != null) {
                page.append( lines );
            }

            List<String> contents;
            if(urlAddress.contains("www.youtube"))
            contents = new ArrayList<>(Arrays.asList( page.toString().split("<entry>")));
            else{
                contents = new ArrayList<>(Arrays.asList( page.toString().split("<item>")));
            }
            contents.remove(0);


            int firstPos;
            int lastPos;
            String temp;
            for (String line : contents) {
                System.out.println();
                firstPos = line.indexOf("<title>");
                if(firstPos != -1) {
                    lastPos = line.indexOf("</title>");
                    temp = line.substring(firstPos + "<title>".length(), lastPos);
                    temp = temp.replace("<![CDATA[", "");
                    temp = temp.replace("]]>", "");
                    System.out.println(temp);
                    article.setTitle(temp);
                }

                firstPos = line.indexOf("<link>");
                if(firstPos != -1) {
                    lastPos = line.indexOf("</link");
                    temp = line.substring(firstPos + "<link>".length(), lastPos);
                    System.out.println(temp);
                    article.setLink(temp);
                }
                else{
                    if(urlAddress.contains("www.youtube") && line.contains("<link"))
                    {
                        firstPos = line.indexOf("href=\"");
                        lastPos = line.indexOf("\"/>");
                        temp = line.substring(firstPos + "href=\"".length(),lastPos);
                        System.out.println(temp);
                        article.setLink(temp);
                    }
                }
                if(urlAddress.contains("www.youtube") && line.contains("<media:thumbnail"))
                {
                    firstPos=line.indexOf("<media:thumbnail url=\"");
                    String tempThumbnail = line.substring(firstPos);
                    firstPos=tempThumbnail.indexOf("<media:thumbnail url=\"");
                    lastPos=tempThumbnail.indexOf("\" width");
                    temp=tempThumbnail.substring(firstPos + "<media:thumbnail url=\"".length(),lastPos);
                    System.out.println(temp);
                    article.setImageURL(temp);
                }


                firstPos = line.indexOf("<description>");
                if(firstPos != -1) {
                    lastPos = line.indexOf("</description>");
                    temp = line.substring(firstPos + "<description>".length(), lastPos);
                    System.out.println(temp);
                    temp = temp.replace("<![CDATA[", "");
                    temp = temp.replace("]]>", "");
                    temp = temp.replaceAll("<.*?>", "");

                    System.out.println(temp);
                    article.setDescription(temp);
                }
                else{
                    if(urlAddress.contains("www.youtube") && line.contains("<media:description>"))
                    {
                        firstPos = line.indexOf("<media:description>");
                        lastPos = line.indexOf("</media:description>");
                        temp = line.substring(firstPos + "<media:description>".length(),lastPos);
                        System.out.println(temp);
                        article.setDescription(temp);
                    }
                }
                rssArticles.add(article);
                article = new Article();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader. close ();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return rssArticles;
    }

    /**
     * Sends a get request for the @urlAddress and returns the content
     * @param urlAddress the urlAddress to which the request is sent
     * @return BufferReader type
     * @throws IOException Error if URL is not found
     */
    public BufferedReader getRSSData(String urlAddress) throws IOException {
        URL url = new URL(urlAddress);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "application/json");//Error 429 otherwise

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        return new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
    }
}
