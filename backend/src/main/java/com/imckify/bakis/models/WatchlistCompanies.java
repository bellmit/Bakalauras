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


}
