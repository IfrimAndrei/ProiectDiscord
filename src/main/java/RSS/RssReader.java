package RSS;

import Datatypes.Article;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class RssReader {


    public static void main(String[] args) {
        List<Article> articles = readRSSFeed("https://www.youtube.com/feeds/videos.xml?channel_id=UCQeRaTukNYft1_6AZPACnog");
        for(Article article : articles)
        {
            System.out.println(article);
        }
    }

    public static List<Article> readRedditFeed(String urlAddress) {
        List<Article> rssArticles = new LinkedList<>();

        String page;
        Article article = new Article();

        try {
            URL rssUrl = new URL(urlAddress);
            URLConnection con = rssUrl.openConnection();
            con.setRequestProperty("User-Agent", "Chrome");//Error 429 otherwise
            try {
                BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream(), StandardCharsets.UTF_8 ) );
                page = in.readLine();
            }catch (Exception e) {
                return null;
            }

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
        }
        return rssArticles;
    }

    public static List<Article> readRSSFeed(String urlAddress) {
        List<Article> rssArticles = new LinkedList<>();
        try {
            URL rssUrl = new URL(urlAddress);
            BufferedReader in = new BufferedReader(new InputStreamReader(rssUrl.openStream()));
            String lines;
            Article article = new Article();

            String page = "";
            while ((lines = in.readLine()) != null) {
                page += lines;
            }

            List<String> contents;
            if(urlAddress.contains("www.youtube"))
            contents = new ArrayList<>(Arrays.asList(page.split("<entry>")));
            else{
                contents = new ArrayList<>(Arrays.asList(page.split("<item>")));
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
                    temp = temp.replace("<![CDATA[", "");
                    temp = temp.replace("]]>", "");

                    if (urlAddress.contains("protv")) {
                        firstPos = temp.indexOf("/&gt;");
                        temp = temp.substring(firstPos + "/&gt;".length(), temp.length() - 1);

                    }

                    if (urlAddress.contains("mkyong")) {
                        firstPos = temp.indexOf("<p>");
                        lastPos = temp.indexOf(".");
                        temp = temp.substring(firstPos + "<p>".length(), lastPos + 1);

                    }

                    if (urlAddress.contains("javapapers")) {
                        temp = temp.replace(" [&#8230;]", "");

                    }


                    System.out.println(temp);
                    article.setDescription(temp);
                    rssArticles.add(article);
                    article = new Article();
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
                    rssArticles.add(article);
                    article = new Article();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rssArticles;
    }
}
