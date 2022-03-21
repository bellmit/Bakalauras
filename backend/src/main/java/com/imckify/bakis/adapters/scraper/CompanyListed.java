package com.imckify.bakis.adapters.scraper;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "symbol","name","lastSale","marketCap","ipoYear","sector","industry","summaryQuote", })
public class CompanyListed {

    public String symbol;
    public String name;
    public String lastSale;
    public String marketCap;
    public String ipoYear;
    public String sector;
    public String industry;
    public String summaryQuote;

//    public String cik;
//    CsvSchema schema = CsvSchema.builder().setEscapeChar(',')
//        .addColumn("symbol")
//        .addColumn("name")
//        .addColumn("lastSale")
//        .addColumn("marketCap")
//        .addColumn("ipoYear")
//        .addColumn("sector")
//        .addColumn("industry")
//        .addColumn("summaryQuote")
//        .build();
//    MappingIterator<CompanyListed> companiesIter = new CsvMapper().readerFor(CompanyListed.class).with(schema).readValues(inputStream);
}
