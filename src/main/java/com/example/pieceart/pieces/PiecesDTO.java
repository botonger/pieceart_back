package com.example.pieceart.pieces;

import com.example.pieceart.entity.Image;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PiecesDTO {
    private Long id;
    private int piecesPurchased;
    //piecesLeft = piecesLeft = 1000 - sum(piecesNum)
    private int piecesLeft;
    private LocalDate purchaseDate;

    private Long worksId;
    private String worksTitle;
    //piece_price = initialPrice / 1000
    private int initialPrice;
    private LocalDate auctionEndDate;

    private Image imgUrl;
    private int currentPrice;
    private String artistName;
}
