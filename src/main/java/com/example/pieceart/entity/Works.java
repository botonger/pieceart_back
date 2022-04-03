package com.example.pieceart.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@Table(name="works")
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@ToString(exclude ="artist")
public class Works {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length=100)
    private String name;

    @Column(nullable = false, length=4000)
    private String description;

    @Column(length=200)
    private String size;

    @Column(length=4)
    private String createdYear; //2020

    @Column
    private LocalDate auctionStartDate;

    @Column
    private LocalDate auctionEndDate;

    @PositiveOrZero
    @Column
    private int initialPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="artist_id")
    private Artist artist;

    @OneToMany(mappedBy= "works", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Image> images = new HashSet<>();
}