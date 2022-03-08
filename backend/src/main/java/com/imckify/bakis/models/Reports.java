package com.imckify.bakis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "cik")
    private String cik;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "form")
    private String form;

    @Column(name = "date")
    private String date;

    @Column(name = "location", columnDefinition="TEXT")
    @Type(type="text")
    private String location;
}
