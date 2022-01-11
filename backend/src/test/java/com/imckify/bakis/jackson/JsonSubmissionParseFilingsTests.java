package com.imckify.bakis.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imckify.bakis.adapters.company.FilingRecent;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import java.util.*;

import static com.imckify.bakis.adapters.company.FilingRecent.parseJSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonSubmissionParseFilingsTests {

    @Test
    public void testMyDto() throws IOException {
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
    public void submissionStreaming() throws IOException {
        final String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("submission.json")).getPath();
        final File jsonFile = new File(path);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonFile).get("filings").get("recent");

        String jsonString = root.toString();
        List<FilingRecent> result = parseJSON(jsonString);

        System.out.println("FilingRecent List " + result.size() + "\n\n");
        result.forEach(System.out::println);

        assertEquals(result.size(), 1003);
    }

    @Test
    public void submission3Streaming() throws IOException {
        final String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("submission3.json")).getPath();
        final File jsonFile = new File(path);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonFile).get("recent");

        String jsonString = root.toString();
        List<FilingRecent> result = parseJSON(jsonString);

        System.out.println("FilingRecent List " + result.size() + "\n\n");
        result.forEach(System.out::println);

        assertEquals(result.size(), 3);
    }

    @Test
    public void testUnwantedResult() throws IOException {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("submission.json")).getPath();
        File json = new File(path);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode node = root.get("filings").get("recent");


        // Todo i need latest 10-K. if there was one, update company info. Save last 10-K date like in db!!!!!
        // Todo use JsonSubmissionParseFilingsTests.java

        List<FilingRecent> filings = parseJSON(node.toString());
        System.out.println(filings);

//          Todo extract other info from root, like sic, name, fiscalYearEnd
//        ObjectReader arrayReader = mapper.readerFor(String[].class); //String[].class // new TypeReference<String[]>(){}
//        List<String> accList = Arrays.asList(arrayReader.readValue(node.get("accessionNumber")));
//        RecentFilings result = mapper.treeToValue(node, RecentFilings.class);
//        RecentFilings result2 = mapper.readValue(node.toString(), new TypeReference<RecentFilings>() {});
//        List nodesList = StreamSupport.stream(node.spliterator(), false).collect(Collectors.toList()); // not eq to node
//        HashMap<String,Object> filings = mapper.readValue(node.toString(), new TypeReference<HashMap<String,Object>>() {});
//        List<List> arrayContainer = filings.entrySet().stream().map(e -> {
//            return (List) e.getValue();
//        }).collect(Collectors.toList()); // jis isbarsto eiliskuma
//        List e0List = arrayContainer.stream().map(list -> {
//            return list.get(0);
//        }).collect(Collectors.toList());
//        assertNotNull(result);
//        assertEquals(result.accessionNumber, accList);
//        assertEquals(result.accessionNumber, result2.accessionNumber);
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
