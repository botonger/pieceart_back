package com.example.pieceart.wishlist;

import com.example.pieceart.entity.Image;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WishlistDTO {
    private Long id;

    private Long worksId;
    private String worksTitle;
    private int initialPrice;

    private String artistName;
    private int piecesLeft;
    private int currentPrice;
    private Image imgUrl;
}
