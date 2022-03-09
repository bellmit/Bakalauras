package com.imckify.bakis.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WatchlistsResponse {
    private Integer ID;
    private String name;
    private Integer investorsID;
    private List<CompaniesResponse> companies = new ArrayList<CompaniesResponse>();
}
