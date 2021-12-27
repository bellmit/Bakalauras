package com.imckify.bakis.adapters.company;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.imckify.bakis.adapters.company.FilingRecent.parseJSON;

@Component
public class CompanyInfoAdapter {

    public static final Logger logger = LoggerFactory.getLogger(CompanyInfoAdapter.class);

    private Map<String, String> ndx = new HashMap<>();

    @PostConstruct
    private void loadNDX () throws IOException {
        File json = Objects.requireNonNull(Paths.get(System.getProperty("user.dir"), "../data/cik/ndx.json")).toFile();

        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> map = mapper.readValue(json, new TypeReference<HashMap<String, String>>() {});
        this.ndx = map;
    }

    @Scheduled(fixedDelay = 1000 * 10)
    private void getCompanyInfo() {
        long start = System.currentTimeMillis();
        logger.info("Executing scheduled task {}()", new Object(){}.getClass().getEnclosingMethod().getName());

        for (Map.Entry<String, String> e : this.ndx.entrySet()) {
            String cik = e.getValue();
            String cikFormatted = ("0000000000" + cik).substring(cik.length());

            Submission submission = getSubmission(cikFormatted);

            ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(submission.getLastModified()), ZoneId.of("EST", ZoneId.SHORT_IDS));
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
            String dateReadable = zdt.format(formatter2);


            logger.warn("Received {} company info, lastModified {}", String.format("%4s", submission.getTickers().get(0)), dateReadable); // Todo update database
        }
        long duration = System.currentTimeMillis() - start;
        logger.info("Executed scheduled task {}() in {}s", new Object(){}.getClass().getEnclosingMethod().getName(), duration/1000);
    }

    public Submission getSubmission(String cik) {
        String url = "https://data.sec.gov/submissions/CIK" + cik + ".json";

        try (CloseableHttpClient client = HttpClients.createMinimal()) {
            HttpUriRequest request = new HttpGet(url);
            request.addHeader("User-Agent", "iMckify imckify@gmail.com");
            try (CloseableHttpResponse response = client.execute(request)) {
                String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                ObjectMapper mapper = new ObjectMapper();

                JsonNode root = mapper.readTree(json);
                JsonNode node = root.get("filings").get("recent");

                List<FilingRecent> filings = parseJSON(node.toString());

                FilingRecent lastAnnual = filings.stream().filter(f -> f.getForm().equals("10-K")).findFirst().orElseGet(() -> null);
                if (lastAnnual == null) {
                    lastAnnual = filings.stream().filter(f -> f.getForm().equals("20-F")).findFirst().orElseGet(() -> filings.get(0));
                }
                String dateString = lastAnnual.getFilingDate();

                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                // mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                Submission sub = mapper.readValue(json, Submission.class);
                sub.setFilings(filings);
                sub.setLastModified(dateStringToEpochEST(dateString));

                if (sub.getTickers().isEmpty()) {
                    String ticker = this.ndx.entrySet().stream().filter(e -> e.getValue().equals(cik)).findFirst().get().getKey();
                    sub.setTickers(Collections.singletonList(ticker.toUpperCase()));
                }

                return sub;
            }
        } catch(Exception e) {
            logger.error("{}(): " + e.getMessage(), new Object(){}.getClass().getEnclosingMethod().getName(), e);
        }
        return new Submission();
    }

    private long dateStringToEpochEST(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        Date date = sdf.parse(dateString);
        long epoch = date.toInstant().toEpochMilli();
        return epoch;
    }
}
