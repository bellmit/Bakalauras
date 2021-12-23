package com.imckify.bakis;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class SubmissionJsonTests {

    @Test
    public void testUnwantedResult() throws IOException {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("submission.json")).getPath();
        File json = new File(path);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode node = root.get("filings").get("recent");
        ObjectReader arrayReader = mapper.readerFor(String[].class); //String[].class // new TypeReference<String[]>(){}

        List<String> accList = Arrays.asList(arrayReader.readValue(node.get("accessionNumber")));

        RecentFilings result = mapper.treeToValue(node, RecentFilings.class);
        RecentFilings result2 = mapper.readValue(node.toString(), new TypeReference<RecentFilings>() {});

        // Todo
//        RecentFilings mylist = mapper.readValue(node.toString(), new TypeReference<RecentFilings>() {});


        assertNotNull(result);
        assertEquals(result.accessionNumber, accList);
        assertEquals(result.accessionNumber, result2.accessionNumber);
    }

    private static class RecentFilings {

        @JsonProperty(value="accessionNumber")
        private List<String> accessionNumber;
        @JsonProperty(value="filingDate")
        private List<String> filingDate;
        @JsonProperty(value="reportDate")
        private List<String> reportDate;
        @JsonProperty(value="acceptanceDateTime")
        private List<String> acceptanceDateTime;
        @JsonProperty(value="act")
        private List<String> act;
        @JsonProperty(value="form")
        private List<String> form;
        @JsonProperty(value="fileNumber")
        private List<String> fileNumber;
        @JsonProperty(value="filmNumber")
        private List<String> filmNumber;
        @JsonProperty(value="items")
        private List<String> items;
        @JsonProperty(value="size")
        private List<String> size;
        @JsonProperty(value="isXBRL")
        private List<String> isXBRL;
        @JsonProperty(value="isInlineXBRL")
        private List<String> isInlineXBRL;
        @JsonProperty(value="primaryDocument")
        private List<String> primaryDocument;
        @JsonProperty(value="primaryDocDescription")
        private List<String> primaryDocDescription;

    }
}
