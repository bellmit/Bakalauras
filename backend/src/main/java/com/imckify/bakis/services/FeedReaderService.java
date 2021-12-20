package com.imckify.bakis.services;

import com.imckify.bakis.models.Notifications;
import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.HashMapFeedInfoCache;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedReaderService {
    public static final Logger logger = LoggerFactory.getLogger(FeedReaderService.class);
    FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();

    @Cacheable(value="feed") // keyGenerator="keygen"
    public List<Notifications> pollFeed() throws Exception {
        logger.info("Executing Cacheable {}()", new Object(){}.getClass().getEnclosingMethod().getName());
        List<Notifications> news = new ArrayList<Notifications>();
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";
        String url2 = "https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent&CIK=&type=&company=&dateb=&owner=include&start=0&count=40&output=atom";
        String url = "https://www.sec.gov/Archives/edgar/xbrlrss.all.xml";

        XmlReader reader = new XmlReader(new URL(url));
        SyndFeed feed = new SyndFeedInput().build(reader);
        news = parseRssFeed(feed);

        FeedFetcher feedFetcher3 = new HttpURLFeedFetcher(feedInfoCache);
        SyndFeed feed3 = feedFetcher3.retrieveFeed(userAgent, new URL(url));
        news = parseRssFeed(feed3);

        return news;
    }

    private List<Notifications> parseRssFeed(SyndFeed feed) {
        List<Notifications> news = new ArrayList<Notifications>();
        for (SyndEntry entry : feed.getEntries()) {
            Notifications article = new Notifications();
            article.setName(entry.getTitle());
            article.setPeriod(entry.getPublishedDate().toString());
            article.setType(entry.getDescription().getValue());
            article.setInvestorsID(1);
            news.add(article);
        }
        return news;
    }
}
