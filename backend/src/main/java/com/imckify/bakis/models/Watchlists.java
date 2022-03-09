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

//    @OneToMany(mappedBy="watchlist")
//    private List<WatchlistCompanies> watchlistCompanies;

    public static WatchlistsVM toViewModel(Watchlists dto) {
        WatchlistsVM vm = new WatchlistsVM();
        vm.setID(dto.getID());
        vm.setName(dto.getName());
        vm.setInvestorsID(dto.getInvestorsID());
        vm.setCompanies(dto.getCompanies().stream().map(Companies::toViewModel).collect(Collectors.toList()));
//        vm.setWatchlistCompanies(dto.getWatchlistCompanies().stream().map(WatchlistCompanies::toViewModel).collect(Collectors.toList()));
        
        return vm;
    }
}