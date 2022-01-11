package com.imckify.bakis.adapters.feed;

/**
 * FilingAdapter class is an automatic scheduled background feed poller (otherwise known as adapter) of Edgar
 * RSS/Atom filings. Every 180 seconds prints freshly published filings if there are any. Works between multiple
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
 * Spring cron skips initial poll
 */

import com.imckify.bakis.Bakis;
import com.imckify.bakis.models.Companies;
import com.imckify.bakis.models.Filings;
import com.imckify.bakis.repos.CompaniesRepo;
import com.imckify.bakis.repos.FilingsRepo;
import com.rometools.rome.feed.synd.SyndEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.*;
import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.messaging.Message;
import org.springframework.scheduling.support.CronTrigger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@EnableIntegration
public class FilingAdapter {

    public static final Logger logger = LoggerFactory.getLogger(FilingAdapter.class);

    @Autowired
    private Bakis.PropsConfig propsConfig;

    @Autowired
    private FilingsRepo FilingsRepo;

    @Autowired
    private CompaniesRepo CompaniesRepo;

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
                filing.setMessageGroupID(entry.getComments().split(",")[0]);
                filing.setMessageGroupComplete(Boolean.parseBoolean(entry.getComments().split(",")[1]));
                return filing;
            }
        };

    }

    @Bean
    public IntegrationFlow filingFlow() {
        return IntegrationFlows
                .from(new MultiFeedEntryMessageSource(propsConfig.getFeeds(), "myKey"),
                        e -> e.poller(p -> p.trigger(new CronTrigger("0 0/3 6-22 ? * MON-FRI", TimeZone.getTimeZone("EST"))).maxMessagesPerPoll(100))
                )
                .transform(transformToFiling())
                .channel("myFeedChannel")
                .aggregate(a -> a
                        .correlationStrategy(m -> ((Filings) m.getPayload()).getMessageGroupID())
                        .releaseStrategy(g -> {
                            List<Message<?>> messages = new ArrayList<>(g.getMessages());
                            Filings last = (Filings) messages.get(messages.size() - 1).getPayload();
                            return last.isMessageGroupComplete();
                        })
                )
                .handle(m -> {
                    List<Companies> companies = this.CompaniesRepo.findAll();
                    List<Filings> filings = new ArrayList<>();

                    for (Object e : (List) m.getPayload()){
                        Filings f = (Filings) e;
                        String filingName = f.getName();
                        String companyName = filingName.substring(0, filingName.indexOf(" ("));

                        // finds Companies for each entry
                        Optional<Companies> company = companies.stream().filter(c -> c.getName().equals(companyName)).findFirst();
                        if (company.isPresent()) {
                            int companyID = company.get().getID();
                            f.setCompaniesID(companyID);
                            filings.add(f);
                        }
                    }

                    if (!filings.isEmpty()) {
                        this.FilingsRepo.saveAll(filings);
                        logger.info("Saved {} filings", filings.size());
                    }
                })
                .get();
    }
}
