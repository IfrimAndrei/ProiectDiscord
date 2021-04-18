package Datatypes;

import Datatypes.Article;
import RSS.RSSReader;
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
            if(urlAddress.contains("www.reddit"))
                articles = RSSReader.rssRedditFeed(urlAddress);
            else {
                articles = RSSReader.readRSSFeed(urlAddress);
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
        info.setColor( new Color( 231, 190, 76 ) );

        if( urlAddress.contains( "javapapers" ) )
            info.setThumbnail( "https://javapapers.com/favicon-32x32.png?v=261118" );
        else if ( urlAddress.contains( "mkyong" ) ){
            info.setThumbnail( "https://i.pinimg.com/originals/41/df/26/41df26f532af6e8cfddc2b217e096c49.png" );
        }
        else if (urlAddress.contains( "reddit" ) ){
            info.setThumbnail( "https://upload.wikimedia.org/wikipedia/ro/thumb/b/b4/Reddit_logo.svg/1200px-Reddit_logo.svg.png" );
        }
        info.setImage( myArticle.getImageURL());
        info.setTitle( myArticle.getTitle() );
        if(myArticle.getDescription()!=null && myArticle.getDescription().length()<2048)
            info.setDescription( myArticle.getDescription() + '\n' + myArticle.getLink()  );
        else
            info.setDescription( myArticle.getLink() );
        info.setFooter(urlAddress + " " + page);

        return info;
    }
}
