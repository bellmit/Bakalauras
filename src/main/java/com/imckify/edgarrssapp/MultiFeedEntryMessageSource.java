package com.imckify.edgarrssapp;

/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Reader;
import java.io.Serializable;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.integration.endpoint.AbstractMessageSource;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.metadata.SimpleMetadataStore;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

/**
 * This implementation of {@link org.springframework.integration.core.MessageSource} will
 * produce individual {@link SyndEntry}s for a multiples feeds identified with the 'feedUrls'
 * attribute.
 *
 * @author <a href="https://github.com/iMckify">Rokas Mockevicius</a>
 */
public class MultiFeedEntryMessageSource extends AbstractMessageSource<SyndEntry> {

    private final List<URL> feedUrls;
    private final String metadataKeyPrefix;
    private final Queue<AbstractMap.SimpleEntry<URL, SyndEntry>> entries = new ConcurrentLinkedQueue<>();
    private final Comparator<SyndEntry> syndEntryComparator = new MultiFeedEntryMessageSource.SyndEntryPublishedDateComparator();
    private final Object monitor = new Object();
    private final Object feedMonitor = new Object();
    private MetadataStore metadataStore;
    private final Map<URL, Long> lastTimes = new HashMap<>();
    private volatile boolean initialized;
    private boolean preserveWireFeed = false;

    /**
     * Creates a MultiFeedEntryMessageSource that will use a HttpURLFeedFetcher to read feeds from the given URLs.
     * If the feed URL has a protocol other than http*, consider providing a custom implementation of the
     * org.springframework.core.io.Resource via the alternate constructor (Not Implemented yet).
     * @param feedUrls list of feed URLs.
     * @param metadataKey The metadata key.
     */
    public MultiFeedEntryMessageSource(List<URL> feedUrls, String metadataKey) {
        Assert.notEmpty(feedUrls, "'feedUrls' must not be empty");
        Assert.notNull(metadataKey, "'metadataKeyPrefix' must not be null");
        this.feedUrls = feedUrls;
        this.metadataKeyPrefix = metadataKey + ".";
    }

    /**
     * Specify a flag to indication if {@code WireFeed} should be preserved in the target {@link SyndFeed}.
     * @param preserveWireFeed the {@code boolean} flag.
     * @see SyndFeedInput#setPreserveWireFeed(boolean)
     * @return this
     */
    public MultiFeedEntryMessageSource preserveWireFeed(boolean preserveWireFeed) {
        this.preserveWireFeed = preserveWireFeed;
        return this;
    }

    @Override
    public String getComponentType() { return "multiple-feed:inbound-channel-adapter"; }

    @Override
    protected void onInit() {
        if (this.metadataStore == null) {
            // first try to look for a 'messageStore' in the context
            BeanFactory beanFactory = getBeanFactory();
            if (beanFactory != null) {
                this.metadataStore = IntegrationContextUtils.getMetadataStore(beanFactory);
            }
            // if no 'messageStore' in context, fall back to in-memory Map-based default
            if (this.metadataStore == null) {
                this.metadataStore = new SimpleMetadataStore();
            }
        }

        for (URL url : this.feedUrls) {
            String metadataKey = this.metadataKeyPrefix + url.toString();
            String lastTimeValue = this.metadataStore.get(metadataKey);

            if (StringUtils.hasText(lastTimeValue)) {
                this.lastTimes.put(url, Long.parseLong(lastTimeValue));
            } else {
                this.lastTimes.put(url, -1L);
            }
        }

        this.initialized = true;
    }

    @Override
    protected Object doReceive() {
        Assert.isTrue(this.initialized, "'MultiFeedEntryMessageSource' must be initialized before it can produce Messages.");

        SyndEntry nextEntry;
        synchronized (this.monitor) {
            nextEntry = getNextEntry();
            if (nextEntry == null) {
                // read feeds and try again
                populateEntryList();
                nextEntry = getNextEntry();
            }
        }
        return nextEntry;
    }

    private SyndEntry getNextEntry() {
        AbstractMap.SimpleEntry<URL, SyndEntry> pairUrlSyndEntry = this.entries.poll();
        if (pairUrlSyndEntry == null || pairUrlSyndEntry.getValue() == null) {
            return null;
        }
        URL url = pairUrlSyndEntry.getKey();
        SyndEntry next = pairUrlSyndEntry.getValue();

        Date lastModifiedDate = MultiFeedEntryMessageSource.getLastModifiedDate(next);
        if (lastModifiedDate != null) {
            this.lastTimes.put(url, lastModifiedDate.getTime());
        } else {
            this.lastTimes.put(url, 1 + this.lastTimes.get(url));
        }
        this.metadataStore.put(this.metadataKeyPrefix + url, this.lastTimes.get(url) + "");
        return next;
    }

    private void populateEntryList() {
        Map<URL, SyndFeed> syndFeeds = this.getFeeds();
        for (URL url : syndFeeds.keySet()) {
            SyndFeed syndFeed = syndFeeds.get(url);
            if (syndFeed != null) {
                List<SyndEntry> retrievedEntries = syndFeed.getEntries();
                if (!CollectionUtils.isEmpty(retrievedEntries)) {
                    boolean withinNewEntries = false;
                    retrievedEntries.sort(this.syndEntryComparator);
                    // todo list mixed
                    for (SyndEntry entry : retrievedEntries) {
                        Date entryDate = getLastModifiedDate(entry);
                        long lastTime = this.lastTimes.get(url);
                        if ((entryDate != null && entryDate.getTime() > lastTime)
                                || (entryDate == null && withinNewEntries)) {
                            AbstractMap.SimpleEntry<URL,SyndEntry> simpleEntry = new AbstractMap.SimpleEntry<>(url, entry);
                            this.entries.add(simpleEntry); // todo add to list mixed
                            withinNewEntries = true;
                        }
                    }
                    // todo this.entries.addAll(list)
                }
            }
        }
    }

    private Map<URL, SyndFeed> getFeeds() {
        try {
            synchronized (this.feedMonitor) {
                Map<URL, SyndFeed> feeds = new HashMap<>();

                for (URL url : this.feedUrls) {
                    Reader reader = new XmlReader(url);
                    SyndFeedInput syndFeedInput = new SyndFeedInput();
                    syndFeedInput.setPreserveWireFeed(this.preserveWireFeed);
                    SyndFeed feed = syndFeedInput.build(reader);
                        logger.debug(() -> this.getClass().getSimpleName() + " Retrieved feed for [" + url + "]");
                    if (feed == null) {
                        logger.debug(() -> this.getClass().getSimpleName() + " No feeds updated for [" + url + "], returning null");
                    }
                    feeds.put(url, feed);
                }
                return feeds;
            }
        }
        catch (Exception e) {
            throw new MessagingException("Failed to retrieve feeds for '" + this.getClass().getSimpleName() + "'", e);
        }
    }

    private static Date getLastModifiedDate(SyndEntry entry) {
        return (entry.getUpdatedDate() != null) ? entry.getUpdatedDate() : entry.getPublishedDate();
    }

    private static final class SyndEntryPublishedDateComparator implements Comparator<SyndEntry>, Serializable {

        SyndEntryPublishedDateComparator() {
        }

        @Override
        public int compare(SyndEntry entry1, SyndEntry entry2) {
            Date date1 = getLastModifiedDate(entry1);
            Date date2 = getLastModifiedDate(entry2);
            if (date1 != null && date2 != null) {
                return date1.compareTo(date2);
            }
            if (date1 == null && date2 == null) {
                return 0;
            }
            return date2 == null ? -1 : 1;
        }

    }

}
