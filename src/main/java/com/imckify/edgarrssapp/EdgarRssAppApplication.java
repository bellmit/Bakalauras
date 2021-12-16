package com.imckify.edgarrssapp;

import com.rometools.rome.feed.synd.SyndEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.feed.dsl.Feed;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.support.PeriodicTrigger;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

@SpringBootApplication
public class EdgarRssAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdgarRssAppApplication.class, args);
    }

    String url = "https://www.sec.gov/Archives/edgar/xbrlrss.all.xml";  // delete metadata-store.properties if no output

    @Bean
    @InboundChannelAdapter("myFeedChannel")
    FeedEntryMessageSource feedEntrySource() throws MalformedURLException {
        FeedEntryMessageSource source = new FeedEntryMessageSource(new URL(url), "myKey");
        return source;
    }

    @Bean
    public DirectChannel myFeedChannel() {
        return new DirectChannel();
    }

    @Bean
    public MetadataStore metadataStore() {
        PropertiesPersistingMetadataStore metadataStore = new PropertiesPersistingMetadataStore();
        metadataStore.setBaseDirectory("target/classes");
        return metadataStore;
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {

        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(60000));
        return pollerMetadata;
    }

    @Bean
    public IntegrationFlow feedFlow() throws MalformedURLException {
        return IntegrationFlows
                //v2
                .from("myFeedChannel")
                // v1
//                .from(  // this exact from() caches using
//                        Feed.inboundAdapter(new URL(url), "myKey")
//                        // .metadataStore(metadataStore()) // can be omitted in this exact usage of from()
//                        , e -> e.poller(Pollers.fixedDelay(1))    // overrides defaultPoller, polls each entry
//                )
                .handle(message -> {
                    String msgDate = new Date(message.getHeaders().getTimestamp()).toString();
                    SyndEntry entry = (SyndEntry) message.getPayload();
                    String epoch = entry.getPublishedDate() != null ? String.valueOf(entry.getPublishedDate().toInstant().toEpochMilli()) : "";
                    String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";
                    String title = entry.getTitle();
                    System.out.println("At " + msgDate + " received " + title);
                }) // or .channel(c -> c.queue("entries"))
                .get();
    }

//    public Message handleMessage(Message message) {
//        String msgDate = new Date(message.getHeaders().getTimestamp()).toString();
//        SyndEntry entry = (SyndEntry) message.getPayload();
//        String epoch = entry.getPublishedDate() != null ? String.valueOf(entry.getPublishedDate().toInstant().toEpochMilli()) : "";
//        String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";
//        String title = entry.getTitle();
//        System.out.println("At " + msgDate + " received " + title);
//
//        return null;
//    }

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
