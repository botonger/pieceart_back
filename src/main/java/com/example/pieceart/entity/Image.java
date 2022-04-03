package com.example.pieceart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Table(name="works_image")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "works")
public class Image {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2)
    private String type;

    @Column
    private String imageUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "works_id")
    private Works works;

}
