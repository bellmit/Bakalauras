package com.imckify.bakis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Portfolios {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;

    @Column(name = "value")
    private double value;

    @Column(name = "changeValue")
    private String changeValue;

    @Column(name = "date")
    private String date;
}
