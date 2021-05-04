package com.imckify.bakis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "period")
    private String period;

    @Column(name = "InvestorsID")
    private Integer investorsID;
}
