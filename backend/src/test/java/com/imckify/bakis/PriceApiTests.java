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
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PriceApiTests {

    static final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    private static String API_nasdaq = "iSF53xkj57EvMFfaxSCk";
    
    private static String API_iex = "pk_7e1f70ad6ae64994a516f7472d47b017";
    private static String API_iex_demo = "Tpk_be2fdcac15034f5581ad39dc4fd15746";

    private static String API_alphaVantage = "HG8Z9EXRP76MYY2E";
    private static String API_alphaVantage_demo = "demo";


    @BeforeAll
    public static void setUp() {
        logger.setLevel(Level.INFO);
    }

    private InputStream get(String url) {
        InputStream inputStream = null;
        try {
            CloseableHttpClient client = HttpClients.createMinimal();
            HttpUriRequest request = new HttpGet(url);
            CloseableHttpResponse response = client.execute(request);
            inputStream = response.getEntity().getContent();

            int code = response.getStatusLine().getStatusCode();
            assertEquals(200 , code);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return inputStream;
    }

    private String parse(InputStream inputStream) {
        String pretty = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(inputStream, Object.class);

            DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
            pp.indentArraysWith( DefaultIndenter.SYSTEM_LINEFEED_INSTANCE );

            pretty = mapper.writer(pp).writeValueAsString(json);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return pretty;
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
        
        InputStream inputStream = get(url);

        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(inputStream, Object.class);
        String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

        System.out.println(pretty);
    }

    @Test
    public void nasdaqPrettyArrayReal() {
        String url = "https://data.nasdaq.com/api/v3/datasets/WIKI/FB/data.json?api_key=" + API_nasdaq;

        InputStream inputStream = get(url);

        String pretty = parse(inputStream);

        System.out.println(pretty);
    }

    @Test
    public void iexYesterdayReal() {
        String url = "https://cloud.iexapis.com/stable/stock/aapl/previous?token=" + API_iex;
//        String url = "https://sandbox.iexapis.com/stable/stock/aapl/quote?token=" + API_iex_demo; // Todo not working

        InputStream inputStream = get(url);

        String pretty = parse(inputStream);

        System.out.println(pretty);
    }

    @Test
    public void iexHistoricalReal() {
        String url = "https://cloud.iexapis.com/stable/stock/aapl/chart/H12018?token=" + API_iex;

        InputStream inputStream = get(url);

        String pretty = parse(inputStream);

        System.out.println(pretty);
    }

    @Test
    public void alphaVantageYesterdayDemo() {
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=IBM&apikey=" + API_alphaVantage_demo;

        InputStream inputStream = get(url);

        String pretty = parse(inputStream);

        System.out.println(pretty);
    }

    @Test
    public void alphaVantageHistoricalDemo() {
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&outputsize=full&apikey=" + API_alphaVantage_demo;

        InputStream inputStream = get(url);

        String pretty = parse(inputStream);

        System.out.println(pretty);
    }

    @Test
    public void alphaVantageHistoricalReal() {
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AAPL&outputsize=full&apikey=" + API_alphaVantage;

        InputStream inputStream = get(url);

        String pretty = parse(inputStream);

        System.out.println(pretty);
    }

    @Test
    public void alphaVantageHistoricalAdjustedFail() {
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=AAPL&outputsize=full&apikey=" + API_alphaVantage;

        InputStream inputStream = get(url);

        String pretty = parse(inputStream);

        System.out.println(pretty);

        assertTrue(pretty.contains("This is a premium endpoint"));
    }

    @Test
    public void yahooFinanceHistoricalReal() {
        String url = "https://query1.finance.yahoo.com/v8/finance/chart/AAPL?symbol=AAPL&period1=0&period2=9999999999&interval=1d&includePrePost=true&events=div%7Csplit";

        InputStream inputStream = get(url);

        String pretty = parse(inputStream);

        System.out.println(pretty);
    }
}
