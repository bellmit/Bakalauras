package com.imckify.bakis.services;

import com.apptastic.rssreader.Item;
import com.apptastic.rssreader.RssReader;
import com.imckify.bakis.models.Notifications;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RSSReaderService {
    @Cacheable("rss")
    public List<Notifications> getRssEntries() {
        List<Notifications> news = new ArrayList<Notifications>();
        RssReader reader = new RssReader();
        try {
            Stream<Item> rssFeed = reader.read("https://www.sec.gov/Archives/edgar/xbrlrss.all.xml");
            List<Item> articles = rssFeed.filter(i -> i.getTitle().isPresent()).collect(Collectors.toList());
            for (Item item: articles) {
                Notifications article = new Notifications();
                article.setName(item.getTitle().get());
                article.setPeriod(item.getPubDate().get());
                article.setType(item.getDescription().get());
                news.add(article);
            }
        } catch(IOException e) {}

        return news;
    }
}
