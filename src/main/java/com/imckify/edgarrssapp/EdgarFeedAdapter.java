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
 *
 * Notes:
 * To remove caching delete target/classes/metadata-store.properties or comment metadataStore() method
 * Spring cron skips initial poll // Todo set cron to "0 0/10 6-22 ? * MON-FRI"
 * Todo .handle() to simulate database operation
 */

import com.rometools.rome.feed.synd.SyndEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.*;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.scheduling.support.CronTrigger;

import java.net.URL;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@EnableIntegration
public class EdgarFeedAdapter {

    @Value("https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent&CIK=&type=&company=&dateb=&owner=include&start=0&count=40&output=atom")
    private URL feedUrl;

    @Bean
    public MetadataStore metadataStore() {
        PropertiesPersistingMetadataStore metadataStore = new PropertiesPersistingMetadataStore();
        metadataStore.setBaseDirectory("target/classes");
        return metadataStore;
    }

    @Bean
    public AbstractPayloadTransformer<SyndEntry, NewsItem> transformToNewsItem() {
        return new AbstractPayloadTransformer<SyndEntry, NewsItem>() {
            @Override
            protected NewsItem transformPayload(SyndEntry entry) {
                String epoch = ""; // for Long.compare()
                String description = "";
                String title = "";

                String link = "";
                String cik = "";
                String acc = "";

                if (entry.getPublishedDate() != null && entry.getDescription() != null) {           // rss_2.0 format
                    epoch = String.valueOf(entry.getPublishedDate().toInstant().toEpochMilli());
                    description = entry.getDescription().getValue();
                    title = entry.getTitle();
                } else if (entry.getUpdatedDate() != null && entry.getCategories().size() != 0) {   // atom_1.0 format
                    epoch = String.valueOf(entry.getUpdatedDate().toInstant().toEpochMilli());
                    description = entry.getCategories().get(0).getName();
                    title = entry.getTitle().replace(description + " - ", "");
                }

                link = entry.getLink();
                Matcher matcherCik = Pattern.compile("(?:\\d{10})", Pattern.MULTILINE).matcher(title);
                if (matcherCik.find()) {
                    cik = matcherCik.group();
                }
                Matcher matcherAcc = Pattern.compile("(?:\\d{10}-\\d+-\\d+)", Pattern.MULTILINE).matcher(link);
                if (matcherAcc.find()) {
                    acc = matcherAcc.group();
                }

                String dateReadable = epoch != "" ? new Date(Long.parseLong(epoch)).toString() : "";

                return new NewsItem(title, description, dateReadable); // Filing
            }
        };

    }

    @Bean
    public IntegrationFlow myFeedFlow() {
        return IntegrationFlows
                .from(new MultiFeedEntryMessageSource(feedUrl, "myKey"),
                        e -> e.poller(p -> p.trigger(new CronTrigger("0/5 * * ? * *", TimeZone.getTimeZone("EST"))).maxMessagesPerPoll(300))
                )
                .transform(transformToNewsItem())
                .channel("myFeedChannel")
                .log(LoggingHandler.Level.WARN, m -> m.getPayload())
                .get();
    }
}
