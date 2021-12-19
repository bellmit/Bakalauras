package blog;

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
public class AdapterConfig {

    @Bean
    @InboundChannelAdapter(value = "newsInput", poller = @Poller(fixedRate = "5000", maxMessagesPerPoll = "1"))
    public Fetcher newsFetcher() {
        List<String> urls = Arrays.asList(
                "https://www.sec.gov/Archives/edgar/xbrlrss.all.xml",
                "https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent&count=40&output=atom"
        );
        Fetcher fetcher = new Fetcher();
        fetcher.setUrls(urls);
        fetcher.setFetcherListener(new Listener());
        return fetcher;
    }

    @Bean
    public AbstractPayloadTransformer<List<SyndFeed>, List<Item>> transformMessages() {
        return new AbstractPayloadTransformer<List<SyndFeed>, List<Item>>() {
            @Override
            protected List<Item> transformPayload(List<SyndFeed> syndFeeds) {
                List<Item> newsItems = new ArrayList<Item>();
                for (SyndFeed syndFeed : syndFeeds) {
                    List<SyndEntry> items = syndFeed.getEntries();

                    if (syndFeed.getFeedType() == "rss_2.0") {
                        for (SyndEntry entry : items) {
                            String epoch = entry.getPublishedDate() != null ? String.valueOf(entry.getPublishedDate().toInstant().toEpochMilli()) : "";
                            String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";
                            String title = entry.getTitle();
                            newsItems.add(new Item(title, description, epoch));
                        }
                    } else if (syndFeed.getFeedType() == "atom_1.0") {
                        for (SyndEntry entry : items) {
                            String date = entry.getUpdatedDate() != null ? entry.getUpdatedDate().toString() : "";
                            String epoch = entry.getUpdatedDate() != null ? String.valueOf(entry.getUpdatedDate().toInstant().toEpochMilli()) : "";

                            String description = entry.getCategories().size() != 0 ? entry.getCategories().get(0).getName() : "";
                            String title = entry.getTitle().replace(description + " - ", "");
                            newsItems.add(new Item(title, description, epoch));
                        }
                    }
                }

                List<Item> news = newsItems.stream()
                        .sorted((n1, n2) -> Long.compare(Long.parseLong(n2.getDate()), Long.parseLong(n1.getDate())))
                        .distinct()
                        .map(n -> {
                            Date date = new Date(Long.parseLong(n.getDate()));
                            return new Item(n.getTitle(), n.getDescription(), date.toString());
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
                    List<Item> news = (List<Item>) message.getPayload();
                    String date = new Date(message.getHeaders().getTimestamp()).toString();
                    LoggerFactory.getLogger(AdapterConfig.class).info("At {} received a message with feedid {} and payload:", date, message.getHeaders().get("feedid",String.class));
                    for (int i = 0; i < 5; i++) {
                        System.out.println(news.get(i).getTitle());
                    }
                })
                .get();
    }
}
