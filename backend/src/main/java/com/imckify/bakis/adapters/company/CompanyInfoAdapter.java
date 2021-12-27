package com.imckify.bakis.adapters.company;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static com.imckify.bakis.adapters.company.FilingRecent.parseJSON;

@Component
public class CompanyInfoAdapter {

    public static final Logger logger = LoggerFactory.getLogger(CompanyInfoAdapter.class);

    @Scheduled(fixedRate = 1000 * 5)
    private void getCompanyInfo() {
        logger.info("Executing scheduled task {}()", new Object(){}.getClass().getEnclosingMethod().getName());

        String cik = "0000320193";
        String cikFormatted = ("0000000000" + cik).substring(cik.length());

        Submission submission = getSubmission(cikFormatted);

        logger.info("Received {} company info", submission.getTickers().get(0));
        System.out.println("==============================================");
    }

    private long dateStringToEpochEST(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        Date date = sdf.parse(dateString);
        long epoch = date.toInstant().toEpochMilli();
        return epoch;
    }

    public Submission getSubmission(String cik) {
        logger.info("Executing {}() {}", new Object(){}.getClass().getEnclosingMethod().getName(), cik);

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

                FilingRecent last10K = filings.stream().filter(f -> f.getForm().equals("10-K")).collect(Collectors.toList()).get(0);
                String dateString = last10K.getFilingDate();

                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                // mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                ObjectReader reader = mapper.readerFor(new TypeReference<Submission>() {});
                Submission sub = reader.readValue(root);
                sub.setFilings(filings);
                sub.setLastModified(dateStringToEpochEST(dateString));

                return sub;
            }
        } catch(IOException | ParseException e) {
            e.printStackTrace();
        }
        return new Submission();
    }
}
