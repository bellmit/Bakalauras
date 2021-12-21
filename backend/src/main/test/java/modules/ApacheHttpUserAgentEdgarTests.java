package modules;


import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ApacheHttpUserAgentEdgarTests extends junit.framework.TestCase {

    public ApacheHttpUserAgentEdgarTests(String testName) {
        super(testName);
    }

    @Before
    public void setUp() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.DEBUG);
    }

    public void testSubmissionsApacheAgentEmptyForbidden() throws IOException {
        String url = "https://data.sec.gov/submissions/CIK0000320193.json";
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);

        CloseableHttpResponse response = client.execute(request);
        int code = response.getStatusLine().getStatusCode();
        String err = response.getStatusLine().getReasonPhrase();

        assertEquals(403 , code);
        assertEquals("Forbidden", err);
    }

    public void testSubmissionsApacheAgentRandomForbidden() throws IOException {
        String url = "https://data.sec.gov/submissions/CIK0000320193.json";
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);
        request.addHeader("User-Agent", "iMckify");

        CloseableHttpResponse response = client.execute(request);
        int code = response.getStatusLine().getStatusCode();
        String err = response.getStatusLine().getReasonPhrase();

        assertEquals(403 , code);
        assertEquals("Forbidden", err);    }

    public void testSubmissionsApacheAgentMozillaForbidden() throws IOException {
        String url = "https://data.sec.gov/submissions/CIK0000320193.json";
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);
        request.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36");

        CloseableHttpResponse response = client.execute(request);
        int code = response.getStatusLine().getStatusCode();
        String err = response.getStatusLine().getReasonPhrase();

        assertEquals(403 , code);
        assertEquals("Forbidden", err);    }

    public void testSubmissionsApacheAgentFairAccessSuccess() throws IOException {
        String url = "https://data.sec.gov/submissions/CIK0000320193.json";
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);
        request.addHeader("User-Agent", "iMckify imckify@gmail.com");

        CloseableHttpResponse response = client.execute(request);
        int code = response.getStatusLine().getStatusCode();

        assertEquals(200 , code);
    }

    public void testBrowseApacheAgentEmptyForbidden() throws IOException {
        String url = "https://www.sec.gov/cgi-bin/browse-edgar?action=getcompany&CIK=320193&type=10-K";
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);

        CloseableHttpResponse response = client.execute(request);
        int code = response.getStatusLine().getStatusCode();
        String err = response.getStatusLine().getReasonPhrase();

        assertEquals(403 , code);
        assertEquals("Forbidden", err);    }

    public void testBrowseApacheAgentMozillaForbidden() throws IOException {
        String url = "https://www.sec.gov/cgi-bin/browse-edgar?action=getcompany&CIK=320193&type=10-K";
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);
        request.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36");

        CloseableHttpResponse response = client.execute(request);
        int code = response.getStatusLine().getStatusCode();
        String err = response.getStatusLine().getReasonPhrase();

        assertEquals(403 , code);
        assertEquals("Forbidden", err);    }

    public void testBrowseApacheAgentSuccess() throws IOException {
        String url = "https://www.sec.gov/cgi-bin/browse-edgar?action=getcompany&CIK=320193&type=10-K";
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);
        request.addHeader("User-Agent", "iMckify imckify@gmail.com");

        CloseableHttpResponse response = client.execute(request);
        int code = response.getStatusLine().getStatusCode();

        assertEquals(200 , code);
    }

    public void testFilingApacheAgentSuccess() throws IOException {
        String url = "https://www.sec.gov/Archives/edgar/data/320193/000032019321000105/0000320193-21-000105-index.htm";
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);
        request.addHeader("User-Agent", "iMckify imckify@gmail.com");

        CloseableHttpResponse response = client.execute(request);
        int code = response.getStatusLine().getStatusCode();

        assertEquals(200 , code);
    }

    public void testRssApacheAgentEmptyForbidden() throws IOException {
        String url = "https://www.sec.gov/Archives/edgar/xbrlrss.all.xml";
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);

        CloseableHttpResponse response = client.execute(request);
        int code = response.getStatusLine().getStatusCode();
        String err = response.getStatusLine().getReasonPhrase();

        assertEquals(403 , code);
        assertEquals("Forbidden", err);    }

    public void testRssApacheAgentMozillaForbidden() throws IOException {
        String url = "https://www.sec.gov/Archives/edgar/xbrlrss.all.xml";
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);
        request.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36");

        CloseableHttpResponse response = client.execute(request);
        int code = response.getStatusLine().getStatusCode();
        String err = response.getStatusLine().getReasonPhrase();

        assertEquals(403 , code);
        assertEquals("Forbidden", err);    }

    public void testRssRomeSuccess() throws IOException, FeedException {
        String url = "https://www.sec.gov/Archives/edgar/xbrlrss.all.xml";

        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));

        assertTrue(feed.getEntries().size() > 0);
    }

    public void testRssAgentApacheToRomeSuccess() throws IOException, FeedException {
        String url = "https://www.sec.gov/Archives/edgar/xbrlrss.all.xml";
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);
        request.addHeader("User-Agent", "iMckify imckify@gmail.com");

        CloseableHttpResponse response = client.execute(request);
        int code = response.getStatusLine().getStatusCode();

        assertEquals(200 , code);

        InputStream stream = response.getEntity().getContent();
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(stream));

        assertTrue(feed.getEntries().size() > 0);
    }

    public void testRssAgentApacheToRomeSuccess2() {
        String url = "https://www.sec.gov/Archives/edgar/xbrlrss.all.xml";

        try (CloseableHttpClient client = HttpClients.createMinimal()) {
            HttpUriRequest request = new HttpGet(url);
            request.addHeader("User-Agent", "iMckify imckify@gmail.com");
            try (CloseableHttpResponse response = client.execute(request);
                    InputStream stream = response.getEntity().getContent()) {
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(stream));

                assertTrue(feed.getEntries().size() > 0);
            }
        } catch(IOException | FeedException e) {
            e.printStackTrace();
        }
    }
}