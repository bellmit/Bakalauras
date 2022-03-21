package com.imckify.bakis.adapters.scraper;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Scraper")
public class Scraper {

    private static final Logger logger = LoggerFactory.getLogger(Scraper.class);

    List<String> exchanges = new ArrayList<String>() {{
        add("NASDAQ");
        add("NYSE");
        add("AMEX");
    }};

    private boolean saveExcludedCik = false;
    private boolean scrapeSecDoc = false;
    private boolean validate = false;

    private String url_base_10k_browse = "https://www.sec.gov/cgi-bin/browse-edgar?action=getcompany&CIK={}&type=10-K".replaceAll("\\{\\}", "%s");
    private String url_base_10k_filing = "http://www.sec.gov/Archives/edgar/data/{}/{}-index.html".replaceAll("\\{\\}", "%s");
    private String url_base_10k_file = "http://www.sec.gov/Archives/edgar/data/{}/{}/{}".replaceAll("\\{\\}", "%s");

    private String url_simple_browse = "http://www.sec.gov/cgi-bin/browse-edgar?CIK={}&Find=Search&owner=exclude&action=getcompany".replaceAll("\\{\\}", "%s");
    private String nasdaq = "https://old.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange={}&render=download".replaceAll("\\{\\}", "%s");

    private Map.Entry<String, String> headers = new AbstractMap.SimpleEntry<String, String>("User-Agent", "iMckify imckify@gmail.com");


    @PostConstruct
    private void runScript () {
        List<Ticker> allTickers = fetchTickers();
        List<String> tickers = allTickers.stream().map(Ticker::getSymbol).distinct().collect(Collectors.toList());
        logger.info(tickers.toString());
    }

    private List<Ticker> fetchTickers() {
        List<Ticker> allTickers = new ArrayList<>();
        for (String exchange : this.exchanges) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(String.format("tickers/%s.csv", exchange.toLowerCase()));
            try {
                MappingIterator<Ticker> tickerIter = new CsvMapper().readerWithTypedSchemaFor(Ticker.class).readValues(inputStream);
                List<Ticker> tickers = tickerIter.readAll();
                tickers.remove(0); // skip header
                allTickers.addAll(tickers);
            } catch (IOException e) {
                logger.error("{}(), exchange {}:", new Object(){}.getClass().getEnclosingMethod().getName(), exchange, e);
            }
        }
        return allTickers;
    }
}
