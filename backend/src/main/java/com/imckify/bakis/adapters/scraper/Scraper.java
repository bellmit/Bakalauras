package com.imckify.bakis.adapters.scraper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import me.tongfei.progressbar.ProgressBar;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Scraper")
public class Scraper {

    private static final Logger logger = LoggerFactory.getLogger(Scraper.class);

    private final Header header = new BasicHeader(HttpHeaders.USER_AGENT, "iMckify imckify@gmail.com");
    private final CloseableHttpClient client = HttpClients.custom().setDefaultHeaders(Collections.singletonList(header)).build();
    private HttpUriRequest req = null;
    private CloseableHttpResponse response = null;

    private List<String> exchanges = new ArrayList<String>() {{
        add("NASDAQ");
        add("NYSE");
        add("AMEX");
    }};
    private String cik_selected = "cik/ndx.json"; //  # "../data/cik/cik.json" or "../data/cik/cik3.json"
    private String cik_excluded = "cik/cik_excluded.json";
    private List<String> tickers = new ArrayList<>();

    private boolean saveExcludedCik = false;
    private boolean scrapeSecDoc = false;
    private boolean validate = false;

    private String url_base_10k_browse = "https://www.sec.gov/cgi-bin/browse-edgar?action=getcompany&CIK={}&type=10-K".replaceAll("\\{\\}", "%s");
    private String url_base_10k_filing = "http://www.sec.gov/Archives/edgar/data/{}/{}-index.html".replaceAll("\\{\\}", "%s");
    private String url_base_10k_file = "http://www.sec.gov/Archives/edgar/data/{}/{}/{}".replaceAll("\\{\\}", "%s");

    private String url_simple_browse = "http://www.sec.gov/cgi-bin/browse-edgar?CIK={}&Find=Search&owner=exclude&action=getcompany".replaceAll("\\{\\}", "%s");
    private String nasdaq = "https://old.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange={}&render=download".replaceAll("\\{\\}", "%s");

    @PostConstruct
    private void main() {
        List<CompanyListed> allCompanies = fetchTickers();
        List<String> tickers = allCompanies.stream().map(CompanyListed::getSymbol).distinct().collect(Collectors.toList()); // .distinct()

        Map<String, String> ticker_cik;
        File selectedJsonFile = new File(getClass().getClassLoader().getResource(this.cik_selected).getPath());
        if (!selectedJsonFile.exists()) {
            ticker_cik = fetchCiks(tickers);
        } else {
            ticker_cik = loadSelected(selectedJsonFile);
        }

        logger.info(ticker_cik.toString()); // Todo from L:164 scraper.py
    }

    private List<CompanyListed> fetchTickers() {
        List<CompanyListed> allCompanies = new ArrayList<>();
        for (String exchange : this.exchanges) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(String.format("tickers/%s.csv", exchange.toLowerCase()));
            try {
                MappingIterator<CompanyListed> companiesIter = new CsvMapper().readerWithTypedSchemaFor(CompanyListed.class).readValues(inputStream); // with JsonPropertyOrder instead of schema
                List<CompanyListed> companies = companiesIter.readAll();
                companies.remove(0); // skip header
                allCompanies.addAll(companies);
            } catch (IOException e) {
                logger.error("{}(), exchange {}:", new Object(){}.getClass().getEnclosingMethod().getName(), exchange, e);
            }
        }
        return allCompanies;
    }

    private Map<String, String> fetchCiks(List<String> tickers) {
        Map<String, String> map = new HashMap<>();
        for (String ticker : ProgressBar.wrap(tickers, "fetchCiks")) {
            this.req = new HttpGet(String.format(this.url_simple_browse, ticker));
            try {
                this.response = this.client.execute(this.req);
                String json = EntityUtils.toString(this.response.getEntity(), StandardCharsets.UTF_8);
                Pattern p = Pattern.compile(".*CIK=(\\d{10}).*");
                Matcher m = p.matcher(json);
                if (m.find()) {
                    String cik = m.group(1);
                    map.put(ticker.toLowerCase(), cik);
                }
            } catch (IOException e) {
                logger.error("{}():", new Object() {}.getClass().getEnclosingMethod().getName(), e);
            }
        }

        return map;
    }

    private Map<String, String> loadSelected(File jsonFile) {
        try {
            InputStream inputStream = new FileInputStream(jsonFile);
            return new ObjectMapper().readValue(inputStream, new TypeReference<HashMap<String, String>>() {});
        } catch (IOException e) {
            logger.error("{}():", new Object() {}.getClass().getEnclosingMethod().getName(), e);
            return new HashMap<>();
        }
    }

}
