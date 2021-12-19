package blog;

import blog.helper.FetcherEventListenerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableIntegration
public class MyConfig {
    private static Logger logger = LoggerFactory.getLogger(MyConfig.class);

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        PeriodicTrigger trigger = new PeriodicTrigger(5000);
        trigger.setFixedRate(true);
        pollerMetadata.setTrigger(trigger);
        pollerMetadata.setMaxMessagesPerPoll(1);
        return pollerMetadata;
    }

    @Bean
    @InboundChannelAdapter(value = "newsInput")
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
    public NewsItemTransformer newsItemTransformer() {
        return new NewsItemTransformer();
    }

    @Bean
    public NewsMessageHandler newsMessageHandler() {
        return new NewsMessageHandler();
    }

    @Bean
    public IntegrationFlow myFlow() {
        return IntegrationFlows
                .from("newsInput")
                .transform(newsItemTransformer())
                .channel("newsOutput")
                .handle(newsMessageHandler())
                .get();
    }
}
