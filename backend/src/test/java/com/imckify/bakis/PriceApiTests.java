package com.imckify.bakis;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceApiTests {

    private static String API_nasdaq = "iSF53xkj57EvMFfaxSCk";

    @BeforeAll
    public static void setUp() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.INFO);
    }

    @Test
    public void nasdaqUgly() throws IOException {
        String url = "https://data.nasdaq.com/api/v3/datasets/WIKI/FB/data.json?api_key=" + API_nasdaq;
        
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);
        CloseableHttpResponse response = client.execute(request);
        String jsonStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        int code = response.getStatusLine().getStatusCode();
        assertEquals(200 , code);
        System.out.println(jsonStr);
    }

    @Test
    public void nasdaqPrettyDefault() throws IOException {
        String url = "https://data.nasdaq.com/api/v3/datasets/WIKI/FB/data.json?api_key=" + API_nasdaq;
        
        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);
        CloseableHttpResponse response = client.execute(request);
        InputStream inputStream = response.getEntity().getContent();

        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(inputStream, Object.class);
        String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

        System.out.println(pretty);
        int code = response.getStatusLine().getStatusCode();
        assertEquals(200 , code);
    }

    @Test
    public void nasdaqPrettyArray() throws IOException {
        String url = "https://data.nasdaq.com/api/v3/datasets/WIKI/FB/data.json?api_key=" + API_nasdaq;

        CloseableHttpClient client = HttpClients.createMinimal();
        HttpUriRequest request = new HttpGet(url);
        CloseableHttpResponse response = client.execute(request);
        InputStream inputStream = response.getEntity().getContent();

        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(inputStream, Object.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.indentArraysWith( DefaultIndenter.SYSTEM_LINEFEED_INSTANCE );
        String pretty = mapper.writer(pp).writeValueAsString(json);

        System.out.println(pretty);
        int code = response.getStatusLine().getStatusCode();
        assertEquals(200 , code);
    }


}
