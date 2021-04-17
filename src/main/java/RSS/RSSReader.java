package RSS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class RSSReader {


    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            RSSReader test = new RSSReader();
            //test.rssRedditFeed( null,null,null );
            test.readRSSFeed("https://mkyong.com/feed/");
        }
    }

    public static List<Article> rssRedditFeed(String urlAddress) {
        List<Article> rssArticles = new LinkedList<>();

        String page;
        Article article = new Article();

        try {
            URL rssUrl = new URL(urlAddress);
            URLConnection con = rssUrl.openConnection();
            con.setRequestProperty("User-Agent", "Chrome");//Error 429 otherwise

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            page = in.readLine();
            //System.out.println( line );

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

                /*
                if (subreddit != null) {
                    String webPage = "https://www.reddit.com/r/" + subreddit + "/comments";
                    firstPos = line.indexOf(webPage);
                    temp = line.substring(firstPos);
                    lastPos = temp.indexOf("&quot");
                    temp = temp.substring(0, lastPos);
                    article.setLink(temp);
                    System.out.println(article.getLink());
                }


                 */
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
                    //rezolvat link-uri

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
            List<String> contents = new ArrayList<>(Arrays.asList(page.split("<item>")));
            contents.remove(0);
            //for(int contor = 0; contor< contents.size(); contor++)
            //System.out.println(contents.get(contor));
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rssArticles;
    }
}
