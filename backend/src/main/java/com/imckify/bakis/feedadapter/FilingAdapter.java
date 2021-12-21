package com.imckify.bakis.feedadapter;

/**
 * FilingAdapter class is an automatic scheduled background feed poller (otherwise known as adapter) of Edgar
 * RSS/Atom filings. Every 60 seconds prints freshly published filings if there are any. Works between multiple
 * Application runtime sessions. Before stopped, FilingAdapter updates target/classes/metadata-store.properties file
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

import com.imckify.bakis.models.Filings;
import com.rometools.rome.feed.synd.SyndEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.*;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.scheduling.support.CronTrigger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@EnableIntegration
public class FilingAdapter {

    @Value("https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent&CIK=&type=&company=&dateb=&owner=include&start=0&count=40&output=atom")
    private URL feedUrl;

    @Value("https://www.sec.gov/Archives/edgar/xbrlrss.all.xml")
    private URL rssUrl;

    @Bean
    public AbstractPayloadTransformer<SyndEntry, Filings> transformToFiling() {
        return new AbstractPayloadTransformer<SyndEntry, Filings>() {
            @Override
            protected Filings transformPayload(SyndEntry entry) {
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

                Filings filing = new Filings();
                filing.setName(title);
                filing.setForm(description);
                filing.setDate(dateReadable);
                filing.setRef(link);
                filing.setCik(cik);
                filing.setAccno(acc);
//                filing.setCompaniesID();
                return filing;
            }
        };

    }

    @Bean
    public IntegrationFlow filingFlow() {
        return IntegrationFlows
                .from(new MultiFeedEntryMessageSource(new ArrayList<URL>() {{ add(feedUrl); add(rssUrl); }}, "myKey"),
                        e -> e.poller(p -> p.trigger(new CronTrigger("0/5 * * ? * *", TimeZone.getTimeZone("EST"))).maxMessagesPerPoll(300))
                )
                .transform(transformToFiling())
                .channel("myFeedChannel")
                .log(LoggingHandler.Level.WARN, m -> {
                    Filings f = (Filings)m.getPayload();
                    return f.getDate() + ", " + String.format("%7s", f.getForm()) + ", " + f.getName();
                })
                .get();
    }
}
