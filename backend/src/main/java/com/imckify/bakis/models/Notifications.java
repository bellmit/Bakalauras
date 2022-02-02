package com.imckify.bakis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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

    @Column(name = "seen")
    private Boolean seen;

    @Column(name = "InvestorsID")
    private Integer investorsID;

    // ===================  IMPORTANT! without ID  =========================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notifications that = (Notifications) o;
        return getName().equals(that.getName()) && getType().equals(that.getType()) && Objects.equals(getPeriod(), that.getPeriod()) && getInvestorsID().equals(that.getInvestorsID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getType(), getPeriod(), getInvestorsID());
    }
}
