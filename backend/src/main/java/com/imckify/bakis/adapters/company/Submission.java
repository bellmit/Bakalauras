package com.imckify.bakis.adapters.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore // @JsonIgnoreProperties would ignore only undeclared props
    private List<FilingRecent> filings;
}
