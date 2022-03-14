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
    
    private static String API_iex = "pk_7e1f70ad6ae64994a516f7472d47b017";
    private static String API_iex_demo = "Tpk_be2fdcac15034f5581ad39dc4fd15746";

    private static String API_alphaVantage = "HG8Z9EXRP76MYY2E";
    private static String API_alphaVantage_demo = "demo";


    @BeforeAll
    public static void setUp() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.INFO);
    }

    @Test
    public void uglyNasdaqReal() throws IOException {
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
    public void prettyDefaultNasdaqReal() throws IOException {
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
    public void nasdaqPrettyArrayReal() throws IOException {
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

    @Test
    public void iexYesterdayReal() throws IOException {
        String url = "https://cloud.iexapis.com/stable/stock/aapl/previous?token=" + API_iex;
//        String url = "https://sandbox.iexapis.com/stable/stock/aapl/quote?token=" + API_iex_demo; // Todo not working

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

    @Test
    public void iexHistoricalReal() throws IOException {
        String url = "https://cloud.iexapis.com/stable/stock/aapl/chart/H12018?token=" + API_iex;

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

    @Test
    public void alphaVantageYesterdayDemo() throws IOException {
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=IBM&apikey=" + API_alphaVantage_demo;

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

    @Test
    public void alphaVantageHistoricalDemo() throws IOException {
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&outputsize=full&apikey=" + API_alphaVantage_demo;

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
    @Test
    public void alphaVantageHistoricalReal() throws IOException {
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AAPL&outputsize=full&apikey=" + API_alphaVantage;

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
