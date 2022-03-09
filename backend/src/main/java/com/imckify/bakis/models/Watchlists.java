package com.imckify.bakis.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
}
