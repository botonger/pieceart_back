package com.example.pieceart.artist;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDTO {
    private Long id;
    private String achieve;
    private String degree;
    private String name;
    private String description;
}