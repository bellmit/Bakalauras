package com.imckify.bakis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "name")
    private String name;

    @Column(name = "ref")
    private String ref;

    @Column(name = "date")
    private String date;

    @Column(name = "form")
    private String form;

    @Column(name = "cik")
    private String cik;

    @Column(name = "accno")
    private String accno;

    @Transient
    private String messageGroupID;

    @Transient
    private boolean isMessageGroupComplete;

    @Column(name = "CompaniesID")
    private Integer companiesID;
}
