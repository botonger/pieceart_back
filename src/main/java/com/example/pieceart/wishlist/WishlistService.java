package com.example.pieceart.wishlist;

import com.example.pieceart.entity.Wishlist;

import java.util.List;
import java.util.stream.Collectors;

public interface WishlistService {
    List<WishlistDTO> getWishList(String email);
    boolean createWishlist(String email, Long worksId);
    void deleteWishList(Long wishlistId);
    Long getWishOrNot(String email, Long worksId);

    default WishlistDTO entityToDto(Wishlist wishlist){
        WishlistDTO wishlistDTO = WishlistDTO.builder()
                .id(wishlist.getId())
                .worksId(wishlist.getWorks().getId())
                .worksTitle(wishlist.getWorks().getName())
                .initialPrice(wishlist.getWorks().getInitialPrice())
                .artistName(wishlist.getWorks().getArtist().getName())
                .imgUrl(wishlist.getWorks().getImages().stream().filter(i->i.getType().equals("ma")).collect(Collectors.toList()).get(0))
                .build();
        return wishlistDTO;
    }
}
