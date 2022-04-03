package com.example.pieceart.auction;

import com.example.pieceart.entity.Auction;

import java.util.List;
import java.util.stream.Collectors;

public interface AuctionService {
    List<AuctionDTO> findAuctionByUser(String email);
    boolean createAuction(String email, Long worksId, int currentPrice);
    boolean deleteAuction(String email, Long auctionId);

    default AuctionDTO entityToDto(Auction auction){
        AuctionDTO auctionDTO = AuctionDTO.builder()
                .id(auction.getId())
                .myPrice(auction.getCurrentPrice())
                .bidDate(auction.getBidDate())
                .worksId(auction.getWorks().getId())
                .worksTitle(auction.getWorks().getName())
                .auctionEndDate(auction.getWorks().getAuctionEndDate())
                .imgUrl(auction.getWorks().getImages().stream().filter(i->i.getType().equals("ma")).collect(Collectors.toList()).get(0))
                .artistName(auction.getWorks().getArtist().getName())
                .build();
        return auctionDTO;
    }
}
