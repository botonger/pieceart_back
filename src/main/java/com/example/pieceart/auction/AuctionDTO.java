package com.example.pieceart.auction;

import com.example.pieceart.entity.Image;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuctionDTO {
    private Long id;
    private int myPrice;
    private int currentPrice;
    private LocalDateTime bidDate;

    private Long worksId;
    private String worksTitle;
    private LocalDate auctionEndDate;

    private Image imgUrl;

    private String artistName;
}
