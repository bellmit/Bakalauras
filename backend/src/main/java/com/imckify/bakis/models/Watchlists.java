package com.imckify.bakis.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Watchlists implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "name")
    private String name;

    @Column(name = "InvestorsID")
    private Integer investorsID;

    @OneToMany(mappedBy="watchlists")
    private List<WatchlistCompanies> watchlistCompanies;
}
