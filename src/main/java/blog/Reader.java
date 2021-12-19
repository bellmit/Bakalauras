package blog;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.FetcherListener;
import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.HashMapFeedInfoCache;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Reader implements MessageSource, InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(Reader.class);
    private FeedFetcherCache feedInfoCache;
    private FeedFetcher feedFetcher;
    private FetcherListener fetcherListener;
    private List<String> urls;

    @Override
    public Message receive() {
        List<SyndFeed> feeds = obtainFeedItems();
        return MessageBuilder.withPayload(feeds)
                .setHeader("feedid", "newsfeed").build();
    }

    private List<SyndFeed> obtainFeedItems() {
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";
        List<SyndFeed> feeds = new ArrayList<>();
        try {
            for (String url : urls) {
                feeds.add(feedFetcher.retrieveFeed(userAgent, new URL(url)));
            }
        } catch (IOException e) {
            logger.error("IO Problem while retrieving feed", e);
        } catch (FeedException e) {
            logger.error("Feed Problem while retrieving feed", e);
        } catch (FetcherException e) {
            logger.error("Fetcher Problem while retrieving feed", e);
        }
        return feeds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        feedInfoCache = HashMapFeedInfoCache.getInstance();
        feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
        if (fetcherListener != null) {
            feedFetcher.addFetcherEventListener(fetcherListener);
        }
    }

    public void setFetcherListener(FetcherListener fetcherListener) {
        this.fetcherListener = fetcherListener;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
