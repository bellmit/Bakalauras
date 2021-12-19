package blog;

import blog.helper.FetcherEventListenerImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableIntegration
public class MyConfig {

    @Bean
    @InboundChannelAdapter(value = "newsInput", poller = @Poller(fixedRate = "5000", maxMessagesPerPoll = "1"))
    public NewsReader newsReader() {
        List<String> urls = Arrays.asList(
                "https://www.sec.gov/Archives/edgar/xbrlrss.all.xml",
                "https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent&amp;CIK=&amp;type=&amp;company=&amp;dateb=&amp;owner=include&amp;start=0&amp;count=10&amp;output=atom",
                "https://spring.io/blog.atom",
                "http://feeds.foxnews.com/foxnews/video?format=xml"
        );
        NewsReader reader = new NewsReader();
        reader.setUrls(urls);
        reader.setFetcherListener(new FetcherEventListenerImpl());
        return reader;
    }

    @Bean
    public AbstractPayloadTransformer<List<SyndFeed>, List<NewsItem>> transformMessages() {
        return new AbstractPayloadTransformer<List<SyndFeed>, List<NewsItem>>() {
            @Override
            protected List<NewsItem> transformPayload(List<SyndFeed> syndFeeds) {
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

                return news;
            }
        };
    }

    @Bean
    public IntegrationFlow myFlow() {
        return IntegrationFlows
                .from("newsInput")
                .transform(transformMessages())
                .channel("newsOutput")
                .handle(message -> {
                    List<NewsItem> news = (List<NewsItem>) message.getPayload();
                    String date = new Date(message.getHeaders().getTimestamp()).toString();
                    LoggerFactory.getLogger(MyConfig.class).info("At {} received a message with feedid {} and payload:", date, message.getHeaders().get("feedid",String.class));
                    for (int i = 0; i < 5; i++) {
                        System.out.println(news.get(i).getTitle());
                    }
                })
                .get();
    }
}
