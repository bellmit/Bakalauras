package blog;

import com.rometools.fetcher.FetcherEvent;
import com.rometools.fetcher.FetcherListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Listener implements FetcherListener {
    private static Logger logger = LoggerFactory.getLogger(Listener.class);

    public void fetcherEvent(FetcherEvent event) {
        String eventType = event.getEventType();
        if (FetcherEvent.EVENT_TYPE_FEED_POLLED.equals(eventType)) {
            logger.info("EVENT: Feed Polled. URL = {}", event.getUrlString());
        } else if (FetcherEvent.EVENT_TYPE_FEED_RETRIEVED.equals(eventType)) {
            logger.info("EVENT: Feed Retrieved. URL = {}", event.getUrlString());
        } else if (FetcherEvent.EVENT_TYPE_FEED_UNCHANGED.equals(eventType)) {
            logger.info("EVENT: Feed Unchanged. URL = {}", event.getUrlString());
        }
    }
}
