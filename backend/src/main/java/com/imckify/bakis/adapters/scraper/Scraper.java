package com.imckify.bakis.adapters.scraper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
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
import org.apache.tomcat.util.http.fileupload.FileUtils;
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
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
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
    private String msg = "";

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
        File selectedJsonFile = new File(getClass().getClassLoader().getResource(cik_selected).getPath());
        if (!selectedJsonFile.exists()) {
            ticker_cik = fetchCiks(tickers);
        } else {
            ticker_cik = loadSelected(selectedJsonFile);
        }

        Map<String, String> excludedByCik = new HashMap<>();
        Set<String> ciksUnique = new HashSet<>();
        for (Map.Entry<String, String> pair : ticker_cik.entrySet()) {
            if (!ciksUnique.add(pair.getValue())) {
                excludedByCik.put(pair.getKey(), pair.getValue());
            }
        }

        // df.drop_duplicates(subset='cik', keep='first', inplace=True)
        for (String key : excludedByCik.keySet()) {
            ticker_cik.remove(key);
        }

        if (saveExcludedCik) {
            saveExcludedCiks(excludedByCik);
        }

        long start = System.currentTimeMillis();
        logger.info("MODE: scrape");

        for (Map.Entry<String, String> tickerAndCik : ProgressBar.wrap(ticker_cik.entrySet(), "Scraper")) {
            String ticker = tickerAndCik.getKey().toUpperCase();
            String cik = tickerAndCik.getValue();

            try {
                Scrape10K(ticker, cik);
            } catch (IOException ioException) {
                logger.error("{}():", new Object() {}.getClass().getEnclosingMethod().getName(), ioException);
            }
        }

        long duration = System.currentTimeMillis() - start;
        logger.info("Executed Scraper {}() in {}s", new Object(){}.getClass().getEnclosingMethod().getName(), duration/1000);
    }

    private List<CompanyListed> fetchTickers() {
        List<CompanyListed> allCompanies = new ArrayList<>();
        for (String exchange : exchanges) {
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
            req = new HttpGet(String.format(url_simple_browse, ticker));
            try {
                response = client.execute(req);
                String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
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

    private void saveExcludedCiks(Map<String, String> map) {
        try {
            new ObjectMapper().writeValue(new File(String.valueOf(Paths.get("./backend/src/main/resources", cik_excluded))), map);
        } catch (IOException e) {
            logger.error("{}():", new Object() {}.getClass().getEnclosingMethod().getName(), e);
        }
    }

    private void log (String s, Object... var2) {
        int i = 0;
        while(s.contains("{}")) {
            s = s.replaceFirst(Pattern.quote("{}"), "{"+ i++ +"}");
        }

        this.msg = MessageFormat.format(s, var2);

        logger.warn(this.msg);
    }

    // Todo from L:228 scraper.py
    private void Scrape10K (String ticker, String cik) throws IOException {
        // prepare ticker dir
        Path dir = Paths.get("./data/10K", ticker);
        try {
            Files.createDirectory(dir);
        } catch (FileAlreadyExistsException e) {
            FileUtils.cleanDirectory(new File(String.valueOf(dir)));
        }

        String url = String.format(url_base_10k_browse, cik);
        req = new HttpGet(url);
        response = client.execute(req);

        if (response.getStatusLine().getStatusCode() != 200) {
            FileUtils.deleteDirectory(new File(String.valueOf(dir)));
            log("[ERROR] <{}> Browse not loaded. URL[{}]: {}\n", ticker, response.getStatusLine().getStatusCode(), url);
            return;
        }

        System.out.println();
    }
}
