package com.imckify.edgarrssapp;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.feed.dsl.Feed;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@SpringBootApplication
public class EdgarRssAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdgarRssAppApplication.class, args);
    }

    String url = "https://www.sec.gov/Archives/edgar/xbrlrss.all.xml";  // delete metadata-store.properties if no output

    @Bean
    @InboundChannelAdapter("feedChannel")
    FeedEntryMessageSource feedEntrySource() throws MalformedURLException {
//        UrlResource urlResource =
//                new UrlResource(url) {
//
//                    @Override
//                    protected void customizeConnection(HttpURLConnection connection) throws IOException {
//                        super.customizeConnection(connection);
//                        connection.setConnectTimeout(10000);
//                        connection.setReadTimeout(5000);
//                    }
//                };
        FeedEntryMessageSource source = new FeedEntryMessageSource(new URL(url), "myKey");
        return source;
    }

    @Bean
    public DirectChannel feedChannel() {
        return new DirectChannel();
    }

    @Bean
    public MetadataStore metadataStore() {
        PropertiesPersistingMetadataStore metadataStore = new PropertiesPersistingMetadataStore();
        metadataStore.setBaseDirectory("target/classes");
        return metadataStore;
    }

    @Bean
    public IntegrationFlow feedFlow() throws MalformedURLException {
        return IntegrationFlows
                .from(Feed.inboundAdapter(new URL(url), "myKey").metadataStore(metadataStore()),
                        e -> e.poller(p -> p.fixedRate(10000)))
                .handle(message -> {
                    SyndEntry entry = (SyndEntry) message.getPayload();
                    String epoch = entry.getPublishedDate() != null ? String.valueOf(entry.getPublishedDate().toInstant().toEpochMilli()) : "";
                    String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";
                    String title = entry.getTitle();
                    System.out.println(title);
                }) // or .channel(c -> c.queue("entries"))
                .get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {

        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(60000));
        return pollerMetadata;
    }
//    @Bean
//    public Message handleMessage(Message message) {
//            Object o = message.getPayload();
//            List entries = (List) o;
//            for (int i = 0; i < 5; i++) {
//                System.out.println(entries.get(i));
//            }
//            System.out.println("==============================================");
//        return null;
//    }

//    Todo debug each method in cloned (other) project
//    @Bean
//    public Message<List<SyndFeed>> handle() {
//        List<SyndFeed> syndFeeds = syndFeedMessage.getPayload();
//        for (SyndFeed syndFeed : syndFeeds) {
//            List<SyndEntry> items = syndFeed.getEntries();
//
//            if (syndFeed.getFeedType() == "rss_2.0") {
//                for (SyndEntry entry : items) {
//                    String epoch = entry.getPublishedDate() != null ? String.valueOf(entry.getPublishedDate().toInstant().toEpochMilli()) : "";
//                    String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";
//                    String title = entry.getTitle();
//                    newsItems.add(new NewsItem(title, description, epoch));
//                }
//            }
//        }
//
//        List<NewsItem> news = newsItems.stream()
//                .sorted((n1, n2) -> Long.compare(Long.parseLong(n2.getDate()), Long.parseLong(n1.getDate())))
//                .distinct()
//                .map(n -> {
//                    Date date = new Date(Long.parseLong(n.getDate()));
//                    return new NewsItem(n.getTitle(), n.getDescription(), date.toString());
//                }).collect(Collectors.toList());
//
//        Message<List<NewsItem>> newMessage = MessageBuilder.withPayload(news)
//                .copyHeaders(syndFeedMessage.getHeaders()).build();
//        return newMessage;
//    }

//    @Configuration
//    public class MyMessageHandler implements MessageHandler {
//
//        @Override
//        public void handleMessage(Message<?> message) throws MessagingException {
//            Object o = message.getPayload();
//            List entries = (List) o;
//            for (int i = 0; i < 5; i++) {
//                System.out.println(entries.get(i));
//            }
//            System.out.println("==============================================");
//        }
//    }
}
