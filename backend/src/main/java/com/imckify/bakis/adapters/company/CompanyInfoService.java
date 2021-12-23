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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.imckify.bakis.adapters.company.FilingRecent.parseJSON;

@Service
public class CompanyInfoService {
    public static final Logger logger = LoggerFactory.getLogger(CompanyInfoService.class);

    @Cacheable(value="getSubmission") // keyGenerator="keygen"
    public Submission getSubmission() {
        logger.info("Executing Cacheable {}()", new Object(){}.getClass().getEnclosingMethod().getName());

        String url = "https://data.sec.gov/submissions/CIK0000320193.json";

        try (CloseableHttpClient client = HttpClients.createMinimal()) {
            HttpUriRequest request = new HttpGet(url);
            request.addHeader("User-Agent", "iMckify imckify@gmail.com");
            try (CloseableHttpResponse response = client.execute(request)) {
                String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                ObjectMapper mapper = new ObjectMapper();

                JsonNode root = mapper.readTree(json);
                JsonNode node = root.get("filings").get("recent");

                List<FilingRecent> filings = parseJSON(node.toString());

                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                // mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                ObjectReader reader = mapper.readerFor(new TypeReference<Submission>() {});
                Submission sub = reader.readValue(root);
                sub.setFilings(filings);

                return sub;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return new Submission();
    }
}
