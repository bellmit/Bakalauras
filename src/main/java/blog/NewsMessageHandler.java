package blog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;

import java.util.Date;
import java.util.List;

/**
 * Created by Bikash on 3/9/2016.
 */
public class NewsMessageHandler {
    private static Logger logger = LoggerFactory.getLogger(NewsMessageHandler.class);

    public void handleMessage(Message<List<NewsItem>> message) {
        logger.info("At {} I received a message with feedid {} and payload {}",new String[] {
                new Date(message.getHeaders().getTimestamp()).toString(),
                message.getHeaders().get("feedid",String.class),
                message.getPayload().toString()});
    }
}
