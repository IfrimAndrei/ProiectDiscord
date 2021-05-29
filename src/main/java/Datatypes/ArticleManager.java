package Datatypes;

import RSS.RssReader;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the list of articles
 * @see Article
 */
public class ArticleManager {
    //HashMap in which the list of articles is saved by urlAddress
    private static final Map<String,List<Article>> adressArticles = new HashMap<>();


    /**
     * Updates the HashMap if needed/requested and calls the rssPage method with parameter page and the list of articles found with the urlAddress key in the HashMap .
     * @param urlAddress key for the HashMap
     * @param page The number of the chosen article from the list
     * @param createNew Request to either update the HashMap value for the specified urlAddress
     * @return EmbedBuilder - Discord specific datatype for display
     * @see RssReader
     * @see Article
     */
    public static EmbedBuilder getPage( String urlAddress, int page,boolean createNew){
        List<Article> articles;
        if(adressArticles.get( urlAddress )!=null && !createNew) {
            articles = adressArticles.get( urlAddress );
        }

        else{ RssReader tempReader = new RssReader();
            if(urlAddress.contains("www.reddit")) {
                articles = tempReader.readRedditFeed( urlAddress );
            }
            else {
                articles = tempReader.readRSSFeed(urlAddress);

            }
            adressArticles.put(urlAddress,articles);
            articles = adressArticles.get( urlAddress );
        }

        return rssPage (urlAddress,page,articles);

    }

    /**
     * Takes the information from the article in the list with the location as the parameter page and creates and returns the EmbedBuilder
     * @param urlAddress The footer in order to be easier to access the article again
     * @param page The location of the article in the list
     * @param articles The list where the article chosen is found
     * @return EmbedBuilder - The EmbedBuilder with the information of the article
     * @see Article
     */
    public static EmbedBuilder rssPage( String urlAddress, int page,List<Article> articles){
        if(articles == null)
            return null;
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
