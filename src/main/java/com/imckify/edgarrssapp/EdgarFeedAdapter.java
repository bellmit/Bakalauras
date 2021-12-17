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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.*;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.scheduling.support.CronTrigger;

import java.net.URL;
import java.util.Date;
import java.util.TimeZone;

@Configuration
@EnableIntegration
public class EdgarFeedAdapter {

    @Value("https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent&CIK=&type=&company=&dateb=&owner=include&start=0&count=40&output=atom")
    private URL feedUrl;

    @Bean
    @InboundChannelAdapter("myFeedChannel") // puts message of each SyndEntry to myFeedChannel
    FeedEntryMessageSource myFeedEntrySource() {
        return new FeedEntryMessageSource(this.feedUrl, "myKey");
    }

    @Bean
    public DirectChannelSpec myFeedChannel() {  // hold messages from myFeedEntrySource
        return MessageChannels
                .direct()
                .wireTap("loggingFlow.input");
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
        String cron = "* * 6-22 ? * MON-FRI";   // Todo "0 0/10 6-22 ? * MON-FRI"
        pollerMetadata.setTrigger(new CronTrigger(cron, TimeZone.getTimeZone("EST")));
//        pollerMetadata.setTrigger(new PeriodicTrigger(60 * 1000));
        return pollerMetadata;
    }

    @Bean
    public AbstractPayloadTransformer<SyndEntry, NewsItem> transformToNewsItem() {
        return new AbstractPayloadTransformer<SyndEntry, NewsItem>() {
            @Override
            protected NewsItem transformPayload(SyndEntry entry) {
                String epoch = "";
                String description = "";
                String title = "";

                if (entry.getPublishedDate() != null && entry.getDescription() != null) {           // rss_2.0 format
                    epoch = String.valueOf(entry.getPublishedDate().toInstant().toEpochMilli());
                    description = entry.getDescription().getValue();
                    title = entry.getTitle();
                } else if (entry.getUpdatedDate() != null && entry.getCategories().size() != 0) {   // atom_1.0 format
                    epoch = String.valueOf(entry.getUpdatedDate().toInstant().toEpochMilli());
                    description = entry.getCategories().get(0).getName();
                    title = entry.getTitle().replace(description + " - ", "");
                }

                String dateReadable = epoch != "" ? new Date(Long.parseLong(epoch)).toString() : "";

                return new NewsItem(title, description, dateReadable);
            }
        };

    }

    @Bean
    public IntegrationFlow loggingFlow() {
        return f -> f.log(LoggingHandler.Level.WARN, m -> transformToNewsItem().doTransform(m)); // return f -> f.log();
    }

    @Bean
    public IntegrationFlow myFeedFlow() {
        return IntegrationFlows
                .from("myFeedChannel")
                .transform(transformToNewsItem())
                .handle(m -> {})    // Todo simulate database operation or print
                .get();
    }
}
