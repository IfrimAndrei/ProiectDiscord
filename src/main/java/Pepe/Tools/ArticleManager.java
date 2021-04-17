package Pepe.Tools;

import RSS.Article;
import RSS.RSSReader;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.List;

public class ArticleManager {
    private static RSSReader myReader = new RSSReader();
    private static String site;
    private static int pageNumber;

    public static int getPageNumber( ) {
        return pageNumber;
    }

    public static void setPageNumber(int pageNumber) {
        ArticleManager.pageNumber = pageNumber;
    }

    public static String getSite( ) {
        return site;
    }

    public static void setSite(String site) {
        ArticleManager.site = site;
    }

    public static RSSReader getMyReader( ) {
        return myReader;
    }

    public static void setMyReader(RSSReader myReader) {
        ArticleManager.myReader = myReader;
    }

/*
    public static EmbedBuilder newPage(String UrlAdress){
        myReader.clear();
        myReader.readRSSFeed( UrlAdress );
        pageNumber=0;
        return rssPage(pageNumber);
    }
*/
    /*
    public static EmbedBuilder nextPage(){
        if(pageNumber==myReader.getRssArticles().size()-1)
            pageNumber=0;
        else
            pageNumber++;
        return rssPage( pageNumber );
    }
    */
     /*
    public static EmbedBuilder previousPage(){
        if(pageNumber==0)
            pageNumber=myReader.getRssArticles().size()-1;
        else
            pageNumber--;
        return rssPage( pageNumber );
    }
    */
    public static EmbedBuilder rssPage( String urlAddress, int page){

        List<Article> articles = RSSReader.readRSSFeed(urlAddress);
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

        if( site.equals( "javapapers" ) )
            info.setThumbnail( "https://javapapers.com/favicon-32x32.png?v=261118" );
        else if ( site.equals( "mkyong" ) ){
            info.setThumbnail( "https://i.pinimg.com/originals/41/df/26/41df26f532af6e8cfddc2b217e096c49.png" );
        }
        else if (site.equals( "reddit" ) ){
            info.setThumbnail( "https://upload.wikimedia.org/wikipedia/ro/thumb/b/b4/Reddit_logo.svg/1200px-Reddit_logo.svg.png" );
        }
        info.setImage( myArticle.getImageURL());
        info.setTitle( myArticle.getTitle() );
        if(myArticle.getDescription()!=null && myArticle.getDescription().length()<2048)
            info.setDescription( myArticle.getDescription() + '\n' + myArticle.getLink()  );
        else
            info.setDescription( myArticle.getLink() );
        info.setFooter( site + " " + page);

        return info;
    }
}
