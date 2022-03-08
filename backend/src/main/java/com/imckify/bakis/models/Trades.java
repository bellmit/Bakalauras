package com.imckify.bakis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "type")
    private String type;

    @Column(name = "price")
    private double price;

    @Column(name = "currentPrice")
    private double currentPrice;

    @Column(name = "quantity")
    private double quantity;

    @Column(name = "currency")
    private String currency;

    @Column(name = "dateValid")
    private String dateValid;

    @Column(name = "datePlaced")
    private String datePlaced;

    @Column(name = "InvestorsID")
    private Integer investorsID;
}
