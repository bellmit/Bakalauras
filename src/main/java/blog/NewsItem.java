package blog;

/**
 * Created by Bikash on 3/9/2016.
 */
public class NewsItem {
    private String title;
    private String description;
    private String content;

    public NewsItem(String title, String description, String content) {

        this.title = title;
        this.description = description;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
