package com.example.pieceart.auction;

import com.example.pieceart.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    @Query(value="SELECT max(a.currentPrice) from Auction a where a.works.id=:worksId")
    int findCurrentPrice(@Param("worksId") Long worksId);

    @Query(value="SELECT a from Auction a WHERE a.works.id=:worksId")
    List<Auction> findAuctionByWorks(@Param("worksId") Long worksId);

    @Query(value="SELECT a from Auction a WHERE a.member.email=:email")
    List<Auction> findAuctionByUser(@Param("email") String email);
}

