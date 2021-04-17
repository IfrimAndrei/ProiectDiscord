package Pepe.Tools;

import RSS.Article;
import RSS.RSSReader;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class ArticleManager {
    private static RSSReader myReader = new RSSReader();
    private static String site;
    private static int pageNumber;

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


    public static EmbedBuilder newPage(){
        pageNumber=0;
        return rssPage(pageNumber);
    }
    public static EmbedBuilder nextPage(){ //TODO add constraints, sa nu poata iesi din lista
        pageNumber++;
        return rssPage( pageNumber );
    }
    public static EmbedBuilder previousPage(){  //TODO add constraints, sa nu poata iesi din lista
        pageNumber--;
        return rssPage( pageNumber );
    }

    public static EmbedBuilder rssPage(int page){
        Article myArticle = myReader.getRssArticles().get(page);
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
        info.setDescription( myArticle.getDescription() + '\n' + myArticle.getLink()  );

        return info;
    }
}
