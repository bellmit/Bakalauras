package blog;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bikash on 3/9/2016.
 */
public class NewsItemTransformer {

    public Message<List<NewsItem>> transform(Message<List<SyndFeed>> syndFeedMessage) {
//        logger.debug("Received a feed from the blog {}",syndFeedMessage.getPayload().get(1).getTitle());

        List<SyndFeed> syndFeeds = syndFeedMessage.getPayload();
        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        for (SyndFeed syndFeed : syndFeeds) {

            List syndFeedItems = syndFeed.getEntries();
            for (Object syndFeedEntry:syndFeedItems) {
                SyndEntry syndEntry = (SyndEntry)syndFeedEntry;
                String title = syndEntry.getTitle();
                String author = syndEntry.getAuthor();
                String description = syndEntry.getDescription()!=null ?  syndEntry.getDescription().getValue() : "";
//                SyndContent syndContent = (SyndContent)syndEntry.getContents().size() ;
//                String content = syndContent.getValue();
                // a lot of other information is possible
                newsItems.add(new NewsItem(title,description,""));
            }
        }

        Message<List<NewsItem>> newMessage = MessageBuilder.withPayload(newsItems)
                .copyHeaders(syndFeedMessage.getHeaders()).build();
        return newMessage;
    }
}
