package com.imckify.bakis.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistCompanies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "CompaniesID")
    private Integer companiesID;

    @Column(name="WatchlistsID")
    private Integer watchlistsID;

//    @ManyToOne
//    @JoinColumn(name="WatchlistsID", nullable=true)
//    private Watchlists watchlist = null;

    public static WatchlistCompaniesVM toViewModel(WatchlistCompanies wc) {
        WatchlistCompaniesVM vm = new WatchlistCompaniesVM();
        vm.setID(wc.getID());
        vm.setCompaniesID(wc.getCompaniesID());
//        vm.setWatchlistsID(wc.getWatchlist().getID());

        return vm;
    }
}
