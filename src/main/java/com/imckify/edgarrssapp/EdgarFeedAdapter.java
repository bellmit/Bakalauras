package com.imckify.edgarrssapp;

/**
 * EdgarFeedAdapter class is an automatic scheduled background feed poller (otherwise known as adapter) of Edgar
 * RSS/Atom filings. Every 60 seconds prints freshly published filings if there are any. Works between multiple
 * Application runtime sessions. Before stopped, EdgarFeedAdapter updates target/classes/metadata-store.properties file
 * with poll time of last entry and cache state of polled URLs and time polled.
 *
 * Feeds that can be subscribed:
 *      atom_1.0 https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent&count=10&output=atom
 * and
 *      rss_2.0  https://www.sec.gov/Archives/edgar/xbrlrss.all.xml
 */

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
    String url = "https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent&count=10&output=atom";

    @Bean
    @InboundChannelAdapter("myFeedChannel")
    FeedEntryMessageSource myFeedEntrySource() throws MalformedURLException {
        return new FeedEntryMessageSource(new URL(url), "myKey");
    }

    @Bean
    public DirectChannel myFeedChannel() {
        return new DirectChannel();
    }

    @Bean
    public MetadataStore metadataStore() {
        PropertiesPersistingMetadataStore metadataStore = new PropertiesPersistingMetadataStore();
        // delete target/classes/metadata-store.properties there fore delete cache if want output
        metadataStore.setBaseDirectory("target/classes");
        return metadataStore;
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(60 * 1000)); // every 60 s
        return pollerMetadata;
    }

    @Bean
    public AbstractPayloadTransformer<SyndEntry, NewsItem> transformToNewsItem() {
        return new AbstractPayloadTransformer<SyndEntry, NewsItem>() {
            @Override
            protected NewsItem transformPayload(SyndEntry payload) {
                String epoch = payload.getPublishedDate() != null ? String.valueOf(payload.getPublishedDate().toInstant().toEpochMilli()) : "";
                String description = payload.getDescription() != null ? payload.getDescription().getValue() : "";
                String title = payload.getTitle();

                return new NewsItem(title, description, epoch);
            }
        };

    }

    @Bean
    public IntegrationFlow myFeedFlow() {
        return IntegrationFlows
                .from("myFeedChannel")
                .transform(transformToNewsItem())
                .handle(message -> System.out.println("At " + new Date(message.getHeaders().getTimestamp()) + " received " + message.getPayload()))
                .get();
    }
}
