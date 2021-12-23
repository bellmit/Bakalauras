package com.imckify.bakis;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class SubmissionJsonTests {

    @Test
    public void testMyDto()
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<MyDto> listOfDtos = Lists.newArrayList(
                new MyDto("a", 1, true),
                new MyDto("bc", 3, false)
        );
        String jsonArray = mapper.writeValueAsString(listOfDtos);

        // [{"stringValue":"a","intValue":1,"booleanValue":true},
        // {"stringValue":"bc","intValue":3,"booleanValue":false}]

        MyDto[] asArray = mapper.readerFor(MyDto[].class).readValue(jsonArray);
        assertTrue(asArray instanceof MyDto[]);
    }

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

        // Todo i need latest 10-K. if there was one, update company info. Save last 10-K date like in db!!!!!
        List nodesList = StreamSupport.stream(node.spliterator(), false).collect(Collectors.toList()); // not eq to node

        HashMap<String,Object> filings = mapper.readValue(node.toString(), new TypeReference<HashMap<String,Object>>() {});
        List<List> arrayContainer = filings.entrySet().stream().map(e -> {
            return (List) e.getValue(); // Todo Entry
        }).collect(Collectors.toList()); // jis isbarsto eiliskuma

        List e0List = arrayContainer.stream().map(list -> {
            return list.get(0);
        }).collect(Collectors.toList());

        assertNotNull(result);
        assertEquals(result.accessionNumber, accList);
        assertEquals(result.accessionNumber, result2.accessionNumber);
    }

    private static class RecentFilings {

        @JsonProperty()
        private List<String> accessionNumber;
        @JsonProperty()
        private List<String> filingDate;
        @JsonProperty()
        private List<String> reportDate;
        @JsonProperty()
        private List<String> acceptanceDateTime;
        @JsonProperty()
        private List<String> act;
        @JsonProperty()
        private List<String> form;
        @JsonProperty()
        private List<String> fileNumber;
        @JsonProperty()
        private List<String> filmNumber;
        @JsonProperty()
        private List<String> items;
        @JsonProperty()
        private List<String> size;
        @JsonProperty()
        private List<String> isXBRL;
        @JsonProperty()
        private List<String> isInlineXBRL;
        @JsonProperty()
        private List<String> primaryDocument;
        @JsonProperty()
        private List<String> primaryDocDescription;

    }

    private static class MyDto {
//        @JsonProperty
        String stringValue;
//        @JsonProperty
        int intValue;
//        @JsonProperty
        boolean booleanValue;

        public MyDto() {}

        public MyDto(String stringValue, int intValue, boolean booleanValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
            this.booleanValue = booleanValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public boolean isBooleanValue() {
            return booleanValue;
        }
    }
}
