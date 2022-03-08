package com.imckify.bakis.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistCompanies implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "CompaniesID")
    private Integer companiesID;

    @ManyToOne
    @JoinColumn(name="WatchlistsID", nullable=false)
    private Watchlists watchlists;

}
