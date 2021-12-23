package com.imckify.bakis.jackson.my;

public class Filing {

    public String accessionNumber;
    public String filingDate;
    public String reportDate;
    public String acceptanceDateTime;
    public String act;
    public String form;
    public String fileNumber;
    public String filmNumber;
    public String items;
    public String size;
    public boolean isXBRL;
    public boolean isInlineXBRL;
    public String primaryDocument;
    public String primaryDocDescription;

    public Filing() {}

    public Filing(String accessionNumber, String filingDate, String reportDate, String acceptanceDateTime, String act, String form, String fileNumber, String filmNumber, String items, String size, boolean isXBRL, boolean isInlineXBRL, String primaryDocument, String primaryDocDescription) {
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
        return "Filing{" +
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