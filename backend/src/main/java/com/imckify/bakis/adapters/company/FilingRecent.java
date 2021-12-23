package com.imckify.bakis.adapters.company;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Jackson uses methods that start with get, otherwise class attributes needs @JsonProperty attribute before declaration
 */

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

    public FilingRecent() {}

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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isXBRL() {
        return isXBRL;
    }

    public void setXBRL(boolean XBRL) {
        isXBRL = XBRL;
    }

    public boolean isInlineXBRL() {
        return isInlineXBRL;
    }

    public void setInlineXBRL(boolean inlineXBRL) {
        isInlineXBRL = inlineXBRL;
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

    @Override
    public String toString() {
        return "FilingRecent{" +
                "accessionNumber='" + accessionNumber + '\'' +
                ", filingDate='" + filingDate + '\'' +
                ", reportDate='" + reportDate + '\'' +
                ", acceptanceDateTime='" + acceptanceDateTime + '\'' +
                ", act='" + act + '\'' +
                ", form='" + form + '\'' +
                ", fileNumber='" + fileNumber + '\'' +
                ", filmNumber='" + filmNumber + '\'' +
                ", items='" + items + '\'' +
                ", size='" + size + '\'' +
                ", isXBRL='" + isXBRL + '\'' +
                ", isInlineXBRL='" + isInlineXBRL + '\'' +
                ", primaryDocument='" + primaryDocument + '\'' +
                ", primaryDocDescription='" + primaryDocDescription + '\'' +
                '}';
    }
}