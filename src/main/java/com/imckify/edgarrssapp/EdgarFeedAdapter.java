package com.imckify.edgarrssapp;

import com.rometools.rome.feed.synd.SyndEntry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

@Configuration
@EnableIntegration
public class EdgarFeedAdapter {
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
        pollerMetadata.setTrigger(new PeriodicTrigger(10 * 1000));
        return pollerMetadata;
    }

    @Bean
    public AbstractPayloadTransformer<SyndEntry, NewsItem> transformToNewsItem() {
        return new AbstractPayloadTransformer<SyndEntry, NewsItem>() {
            @Override
            protected NewsItem transformPayload(SyndEntry payload) {
                SyndEntry entry = payload;
                String epoch = entry.getPublishedDate() != null ? String.valueOf(entry.getPublishedDate().toInstant().toEpochMilli()) : "";
                String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";
                String title = entry.getTitle();

                return new NewsItem(title, description, epoch);
            }
        };

    }

    @Bean
    public IntegrationFlow feedFlow() throws MalformedURLException {
        return IntegrationFlows
                .from("myFeedChannel")
                .transform(transformToNewsItem())
                .handle(message -> System.out.println("At " + new Date(message.getHeaders().getTimestamp()).toString() + " received " + (NewsItem) message.getPayload()))
//                .channel( c -> {
//                    return c.queue("entries");
//                })
                .get();
    }
}
