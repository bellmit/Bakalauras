package com.imckify.bakis.adapters.company;

import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.HashMap;
import java.util.List;

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
                ObjectReader reader = new ObjectMapper().readerFor(new TypeReference<List<String>>() {});
                List<String> list = reader.readValue(recentFilings.get("accessionNumber"));
                //https://www.google.com/search?q=Map%3CString%2CList%3CString%3E%3E+to+list+of+objects&oq=Map%3CString%2CList%3CString%3E%3E+to+list+of+objects&aqs=chrome..69i57j0i22i30.25728j0j7&sourceid=chrome&ie=UTF-8
                // https://stackoverflow.com/questions/50348166/java-8-list-of-objects-to-mapstring-list-of-values
//                //https://www.baeldung.com/json-multiple-fields-single-java-field

//                ObjectNode objectNode = mapper.convertValue(hashMap, ObjectNode.class);
//                Map<String,List<String>> hashMap2 = mapper.convertValue(objectNode, new TypeReference<Map<String, List<String>>>() {});
                new ObjectMapper().readerFor(new TypeReference<List<RecentFilings>>() {}).readValue(recentFilings);
                System.out.println(json);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return new Object();
    }

    public static class RecentFilings {
        private String accessionNumber;
        private String filingDate;
        private String reportDate;
        private String acceptanceDateTime;
        private String act;
        private String form;
        private String fileNumber;
        private String filmNumber;
        private String items;
        private String size;
        private String isXBRL;
        private String isInlineXBRL;
        private String primaryDocument;
        private String primaryDocDescription;

        public RecentFilings(String accessionNumber, String filingDate, String reportDate, String acceptanceDateTime,
                             String act, String form, String fileNumber, String filmNumber, String items, String size,
                             String isXBRL, String isInlineXBRL, String primaryDocument, String primaryDocDescription) {
            this.accessionNumber = accessionNumber;
            this.filingDate = filingDate;
            this.reportDate = reportDate;
            this.acceptanceDateTime = acceptanceDateTime;
            this.act = act;
            this.form = form;
            this.fileNumber = fileNumber;
            this.filmNumber = filmNumber;
            this.items = items;
            this.size = size;
            this.isXBRL = isXBRL;
            this.isInlineXBRL = isInlineXBRL;
            this.primaryDocument = primaryDocument;
            this.primaryDocDescription = primaryDocDescription;
        }

        public String getAccessionNumber() {
            return accessionNumber;
        }

        public void setAccessionNumber(String accessionNumber) {
            this.accessionNumber = accessionNumber;
        }

        public String getFilingDate() {
            return filingDate;
        }

        public void setFilingDate(String filingDate) {
            this.filingDate = filingDate;
        }

        public String getReportDate() {
            return reportDate;
        }

        public void setReportDate(String reportDate) {
            this.reportDate = reportDate;
        }

        public String getAcceptanceDateTime() {
            return acceptanceDateTime;
        }

        public void setAcceptanceDateTime(String acceptanceDateTime) {
            this.acceptanceDateTime = acceptanceDateTime;
        }

        public String getAct() {
            return act;
        }

        public void setAct(String act) {
            this.act = act;
        }

        public String getForm() {
            return form;
        }

        public void setForm(String form) {
            this.form = form;
        }

        public String getFileNumber() {
            return fileNumber;
        }

        public void setFileNumber(String fileNumber) {
            this.fileNumber = fileNumber;
        }

        public String getFilmNumber() {
            return filmNumber;
        }

        public void setFilmNumber(String filmNumber) {
            this.filmNumber = filmNumber;
        }

        public String getItems() {
            return items;
        }

        public void setItems(String items) {
            this.items = items;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getIsXBRL() {
            return isXBRL;
        }

        public void setIsXBRL(String isXBRL) {
            this.isXBRL = isXBRL;
        }

        public String getIsInlineXBRL() {
            return isInlineXBRL;
        }

        public void setIsInlineXBRL(String isInlineXBRL) {
            this.isInlineXBRL = isInlineXBRL;
        }

        public String getPrimaryDocument() {
            return primaryDocument;
        }

        public void setPrimaryDocument(String primaryDocument) {
            this.primaryDocument = primaryDocument;
        }

        public String getPrimaryDocDescription() {
            return primaryDocDescription;
        }

        public void setPrimaryDocDescription(String primaryDocDescription) {
            this.primaryDocDescription = primaryDocDescription;
        }
    }
}
