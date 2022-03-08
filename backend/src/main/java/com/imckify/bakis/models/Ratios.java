package com.imckify.bakis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ratios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "formula")
    private String formula;

    @Column(name = "type")
    private String type;

    @Column(name = "AnalystsID")
    private Integer analystsID;

    @Column(name = "ReportsID")
    private Integer reportsID;
}
