package com.imckify.bakis.adapters.company;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Jackson uses methods that start with get prefix, otherwise class attributes need @JsonProperty attribute before
 * declaration.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilingRecent {

    private String accessionNumber;
    private String filingDate;
    private String reportDate;
    private String acceptanceDateTime;
    private String act;
    private String form;
    private String fileNumber;
    private String filmNumber;
    private String items;
    private long size;
    private boolean isXBRL;
    private boolean isInlineXBRL;
    private String primaryDocument;
    private String primaryDocDescription;

    public static List<FilingRecent> parseJSON(String jsonString) throws IOException {
        List<FilingRecent> container = new ArrayList<>();
        JsonParser jsonParser = new JsonFactory().createParser(jsonString);
        while(jsonParser.nextToken() != JsonToken.END_OBJECT){
            String arrayName = jsonParser.getCurrentName();
            if(arrayName == null || arrayName.length() == 0) {
                continue;
            }
            jsonParser.nextToken();
            for (int i = 0; jsonParser.nextToken() != JsonToken.END_ARRAY; i++) {
                if (i > container.size() - 1) {
                    container.add(new FilingRecent());
                }

                FilingRecent f = container.get(i);
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