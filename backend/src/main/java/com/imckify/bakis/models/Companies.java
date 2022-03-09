package com.imckify.bakis.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Companies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy="companies")
    private List<Watchlists> watchlists = new ArrayList<Watchlists>();
}
