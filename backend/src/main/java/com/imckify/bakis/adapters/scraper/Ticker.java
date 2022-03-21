package com.imckify.bakis.adapters.scraper;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "Symbol","Name","LastSale","MarketCap","IPOyear","Sector","industry","Summary Quote", })
public class Ticker {

    public String Symbol;
    public String Name;
    public String LastSale;
    public String MarketCap;
    public String IPOyear;
    public String Sector;
    public String industry;
    public String summaryQuote;
}
