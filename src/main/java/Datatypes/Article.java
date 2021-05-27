package Datatypes;


/**
 * Datatype for an article
 */
public class Article {
    private String title;
    private String link;
    private String description;
    private String imageURL;


    @Override
    public String toString( ) {
        return "Article{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getImageURL( ) {
        return imageURL;
    }

    public String getTitle( ) {
        return title;
    }

    public String getLink( ) {
        return link;
    }

    public String getDescription( ) {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
