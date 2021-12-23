package com.imckify.bakis.jackson.my;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import java.io.File;
import java.io.IOException;

import java.util.*;

public class MyTests {

    private final String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("my.json")).getPath();
    private final File jsonFile = new File(path);

    @Test
    public void myStreaming() throws IOException, NoSuchFieldException, IllegalAccessException {
        //create JsonParser object
        JsonParser jsonParser = new JsonFactory().createParser(jsonFile);

        //loop through the tokens
        List<Filing> list = new ArrayList<Filing>();

        parseJSON(jsonParser, list);

        jsonParser.close();

        System.out.println("Filing List\n\n");
        list.forEach(System.out::println);
    }

    private static void parseJSON(JsonParser jsonParser, List<Filing> container) throws JsonParseException, IOException, NoSuchFieldException, IllegalAccessException {

        // Todo set con size 3
        for (int i = 0; i < 3; i++) {
            container.add(new Filing());
        }
        while(jsonParser.nextToken() != JsonToken.END_OBJECT){
            String arrName = jsonParser.getCurrentName();
            if(arrName == null || arrName.length() == 0) {
                continue;
            }
            jsonParser.nextToken();
            for (int i = 0; jsonParser.nextToken() != JsonToken.END_ARRAY; i++) {
                Filing f = container.get(i);
                ConfigurablePropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(f);

                if (arrName.startsWith("is")) {
                    accessor.setPropertyValue(arrName, jsonParser.getValueAsBoolean());
                } else if (arrName.equals("size")) {
                    accessor.setPropertyValue(arrName, jsonParser.getLongValue());
                } else {
                    accessor.setPropertyValue(arrName, jsonParser.getValueAsString());
                }
            }
        }
    }
}
