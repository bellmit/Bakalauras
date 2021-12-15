package com.imckify.bakis.services;

import com.imckify.bakis.models.Notifications;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedReaderService {
    @Cacheable("feed")
    public List<Notifications> pollFeed() {
        List<Notifications> news = new ArrayList<Notifications>();
        String url2 = "https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent&CIK=&type=&company=&dateb=&owner=include&start=0&count=40&output=atom";
        String url = "https://www.sec.gov/Archives/edgar/xbrlrss.all.xml";

        try {
            XmlReader reader = new XmlReader(new URL(url));
            SyndFeedInput in = new SyndFeedInput();
            in.setPreserveWireFeed(true);
            SyndFeed feed = in.build(reader); System.out.println(feed.getTitle());
            news = parseRssFeed(feed);
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return news;
    }

    private List<Notifications> parseRssFeed(SyndFeed feed) {
        List<Notifications> news = new ArrayList<Notifications>();
        for (SyndEntry entry : feed.getEntries()) {
            Notifications article = new Notifications();
            article.setName(entry.getTitle());
            article.setPeriod(entry.getPublishedDate().toString());
            article.setType(entry.getDescription().getValue());
            news.add(article);
        }
        return news;
    }
}
