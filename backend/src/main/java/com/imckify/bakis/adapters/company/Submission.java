package com.imckify.bakis.adapters.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    private int cik;
    private int sic;
    private String sicDescription;
    private String name;
    private List<String> tickers;
    private List<String> exchanges;
    private String fiscalYearEnd;
    private List<FilingRecent> filings;
}
