package com.imckify.bakis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Courses {
    @Id
    @Column(name = "ID")
    private String ID;

    @Column(name = "title")
    private String title;

    @Column(name = "length")
    private String length;

    @Column(name = "category")
    private String category;

    @Column(name = "watchHref")
    private String watchHref;

    @Column(name = "authorId")
    private String authorId;
}
