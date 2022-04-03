package com.example.pieceart.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Table(name="artist")
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude = "works")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length=50)
    private String name;

    @Column(length = 4000)
    private String degree;

    @Column(length = 4000)
    private String achieve;

    @Column(length = 4000)
    private String description;

    //제거하기.. 무한 참조 발생
//    @OneToMany(mappedBy= "artist", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Works> works = new HashSet<>();
}
