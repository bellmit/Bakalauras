package com.imckify.bakis.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Watchlists {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "name")
    private String name;

    @Column(name = "InvestorsID")
    private Integer investorsID;

    @ManyToMany
    @JoinTable(name = "WatchlistCompanies",
            joinColumns = { @JoinColumn(name = "WatchlistsID") },
            inverseJoinColumns = { @JoinColumn(name = "CompaniesID") })
    private List<Companies> companies = new ArrayList<Companies>();

    public static WatchlistsVM toViewModel(Watchlists dto) {
        WatchlistsVM r = new WatchlistsVM();
        r.setID(dto.getID());
        r.setName(dto.getName());
        r.setInvestorsID(dto.getInvestorsID());
        r.setCompanies(dto.getCompanies().stream().map(Companies::toViewModel).collect(Collectors.toList()));
        return r;
    }
}