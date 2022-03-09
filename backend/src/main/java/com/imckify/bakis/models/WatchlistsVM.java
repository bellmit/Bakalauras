package com.imckify.bakis.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WatchlistsVM {
    private Integer ID;
    private String name;
    private Integer investorsID;
    private List<CompaniesVM> companies = new ArrayList<CompaniesVM>();
//    private List<WatchlistCompaniesVM> watchlistCompanies = new ArrayList<WatchlistCompaniesVM>();
}