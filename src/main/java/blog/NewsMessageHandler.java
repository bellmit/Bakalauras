package blog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import java.util.Date;
import java.util.List;

/**
 * Created by Bikash on 3/9/2016.
 */
public class NewsMessageHandler {
    private static Logger logger = LoggerFactory.getLogger(NewsMessageHandler.class);

    public void handleMessage(Message<List<NewsItem>> message) {
        List<NewsItem> news = message.getPayload();
        String date = new Date(message.getHeaders().getTimestamp()).toString();
        logger.info("At {} received a message with feedid {} and payload:", date, message.getHeaders().get("feedid",String.class));
        for (int i = 0; i < 5; i++) {
            System.out.println(news.get(i));
        }
        System.out.println("==============================================");
    }
}
