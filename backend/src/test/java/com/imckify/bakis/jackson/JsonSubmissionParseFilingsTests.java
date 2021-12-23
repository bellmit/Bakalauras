package com.imckify.bakis.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imckify.bakis.adapters.company.Filing;
import org.junit.jupiter.api.Test;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import java.io.File;
import java.io.IOException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonSubmissionParseFilingsTests {

    @Test
    public void myStreaming() throws IOException {
        final String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("my.json")).getPath();
        final File jsonFile = new File(path);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonFile);

        String jsonString = root.toString();
        List<Filing> list = parseJSON(jsonString);

        System.out.println("Filing List\n\n");
        list.forEach(System.out::println);
    }

    @Test
    public void submissionStreaming() throws IOException {
        final String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("submission.json")).getPath();
        final File jsonFile = new File(path);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonFile).get("filings").get("recent");

        String jsonString = root.toString();
        List<Filing> result = parseJSON(jsonString);

        System.out.println("Filing List " + result.size() + "\n\n");
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
        List<Filing> result = parseJSON(jsonString);

        System.out.println("Filing List " + result.size() + "\n\n");
        result.forEach(System.out::println);

        assertEquals(result.size(), 3);
    }



    private static List<Filing> parseJSON(String jsonString) throws IOException {
        List<Filing> container = new ArrayList<>();
        JsonParser jsonParser = new JsonFactory().createParser(jsonString);
        while(jsonParser.nextToken() != JsonToken.END_OBJECT){
            String arrayName = jsonParser.getCurrentName();
            if(arrayName == null || arrayName.length() == 0) {
                continue;
            }
            jsonParser.nextToken();
            for (int i = 0; jsonParser.nextToken() != JsonToken.END_ARRAY; i++) {
                if (i > container.size() - 1) {
                    container.add(new Filing());
                }

                Filing f = container.get(i);
                ConfigurablePropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(f);

                if (arrayName.startsWith("is")) {
                    accessor.setPropertyValue(arrayName, jsonParser.getValueAsBoolean());
                } else if (arrayName.equals("size")) {
                    accessor.setPropertyValue(arrayName, jsonParser.getLongValue());
                } else {
                    accessor.setPropertyValue(arrayName, jsonParser.getValueAsString());
                }
            }
        }
        jsonParser.close();
        return container;
    }
}
