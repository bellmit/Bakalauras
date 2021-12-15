package blog;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Bikash on 3/9/2016.
 */
public class NewsItemTransformer {

    public Message<List<NewsItem>> transform(Message<List<SyndFeed>> syndFeedMessage) {
        List<SyndFeed> syndFeeds = syndFeedMessage.getPayload();
        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        for (SyndFeed syndFeed : syndFeeds) {
            List<SyndEntry> items = syndFeed.getEntries();

            if (syndFeed.getFeedType() == "rss_2.0") {
                for (SyndEntry entry : items) {
                    String epoch = entry.getPublishedDate() != null ? String.valueOf(entry.getPublishedDate().toInstant().toEpochMilli()) : "";
                    String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";
                    String title = entry.getTitle();
                    newsItems.add(new NewsItem(title, description, epoch));
                }
            } else if (syndFeed.getFeedType() == "atom_1.0") {
                for (SyndEntry entry : items) {
                    String date = entry.getUpdatedDate() != null ? entry.getUpdatedDate().toString() : "";
                    String epoch = entry.getUpdatedDate() != null ? String.valueOf(entry.getUpdatedDate().toInstant().toEpochMilli()) : "";

                    String description = entry.getCategories().size() != 0 ? entry.getCategories().get(0).getName() : "";
                    String title = entry.getTitle().replace(description + " - ", "");
                    newsItems.add(new NewsItem(title, description, epoch));
                }
            }
        }

        List<NewsItem> news = newsItems.stream()
                .sorted((n1, n2) -> Long.compare(Long.parseLong(n2.getDate()), Long.parseLong(n1.getDate())))
                .distinct()
                .map(n -> {
                    Date date = new Date(Long.parseLong(n.getDate()));
                    return new NewsItem(n.getTitle(), n.getDescription(), date.toString());
            }).collect(Collectors.toList());

        Message<List<NewsItem>> newMessage = MessageBuilder.withPayload(news)
                .copyHeaders(syndFeedMessage.getHeaders()).build();
        return newMessage;
    }
}
