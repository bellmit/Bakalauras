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

/**
 * Created by Bikash on 3/9/2016.
 */
public class NewsReader implements MessageSource, InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(NewsReader.class);
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
        List<SyndFeed> feed = new ArrayList<>();
        try {
            for (String url : urls) {
                feed.add(feedFetcher.retrieveFeed(new URL(url)));
            }
        } catch (IOException e) {
            logger.error("IO Problem while retrieving feed", e);
        } catch (FeedException e) {
            logger.error("Feed Problem while retrieving feed", e);
        } catch (FetcherException e) {
            logger.error("Fetcher Problem while retrieving feed", e);
        }
        return feed;
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
