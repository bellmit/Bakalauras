package com.imckify.bakis.adapters.company;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
public class CompanyInfoService {
    public static final Logger logger = LoggerFactory.getLogger(CompanyInfoService.class);

    @Cacheable(value="feed") // keyGenerator="keygen"
    public Object getSubmission() throws Exception {
        logger.info("Executing Cacheable {}()", new Object(){}.getClass().getEnclosingMethod().getName());

        String url = "https://data.sec.gov/submissions/CIK0000320193.json";


        try (CloseableHttpClient client = HttpClients.createMinimal()) {
            HttpUriRequest request = new HttpGet(url);
            request.addHeader("User-Agent", "iMckify imckify@gmail.com");
            try (CloseableHttpResponse response = client.execute(request)) {
                String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                HashMap<String,Object> submission = new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>() {});

                JsonNode node = new ObjectMapper().readTree(json);
                JsonNode recentFilings = node.get("filings").get("recent");
                // todo merge node arrays
                System.out.println(json);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return new Object();
    }
}
