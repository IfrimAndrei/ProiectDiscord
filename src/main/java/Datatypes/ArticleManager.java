package Datatypes;

import RSS.RssReader;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class ArticleManager {
    private static HashMap<String,List<Article>> adressArticles = new HashMap();

    public static EmbedBuilder getPage( String urlAddress, int page,boolean createNew){
        List<Article> articles;
        if(adressArticles.get( urlAddress )!=null && !createNew) {
            articles = adressArticles.get( urlAddress );
        }

        else{
            if(urlAddress.contains("www.reddit")) {
                articles = RssReader.readRedditFeed( urlAddress );
                if(articles==null)
                {
                    return null;
                }
            }
            else {
                articles = RssReader.readRSSFeed(urlAddress);

            }
            adressArticles.put(urlAddress,articles);
            articles = adressArticles.get( urlAddress );
        }

        return rssPage (urlAddress,page,articles);

    }

    public static EmbedBuilder rssPage( String urlAddress, int page,List<Article> articles){
        if(page == -1){
            page = articles.size()-1;
        }
        if(page == articles.size())
        {
            page = 0;
        }
        Article myArticle = articles.get(page);
        EmbedBuilder info = new EmbedBuilder();
        info.setColor( new Color( 148, 231, 76 ) );

        info.setImage( myArticle.getImageURL());
        info.setTitle( myArticle.getTitle() );
        if(myArticle.getDescription() != null) {
            if (myArticle.getDescription().length() > 200) {
                myArticle.setDescription(myArticle.getDescription().substring(0, 200) + "...");
            }
        }
        if(myArticle.getDescription()!=null && myArticle.getDescription().length()<2048)
            info.setDescription( myArticle.getDescription() + '\n' + myArticle.getLink()  );
        else
            info.setDescription( myArticle.getLink() );
        info.setFooter(urlAddress + " " + page);

        return info;
    }
}
