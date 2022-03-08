package com.imckify.bakis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Modifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "HTML")
    private String html;

    @Column(name = "date")
    private String date;

    @Column(name = "AnalystsID")
    private Integer analystsID;

    @Column(name = "ReportsID")
    private Integer reportsID;
}
