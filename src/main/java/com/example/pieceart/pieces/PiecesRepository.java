package com.example.pieceart.pieces;

import com.example.pieceart.entity.Pieces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PiecesRepository extends JpaRepository<Pieces, Long> {
    //1000 - sumOfPieces = piecesLeft
    @Query(value="SELECT sum(p.pieceNum) from Pieces p WHERE p.works.id=:worksId")
    int findSumOfPieces(@Param("worksId") Long worksId);

    @Query(value="SELECT p from Pieces p WHERE p.works.id=:worksId")
    List<Pieces> findPiecesByWorks(@Param("worksId") Long worksId);

    @Query(value="SELECT p from Pieces p WHERE p.member.email=:email")
    List<Pieces> findPiecesByMember(@Param("email") String email);

//    @Query(value = "SELECT DISTINCT p.id, p.purchase_date, p.works_id, p.piece_num, s.piece_sum," +
//            " w.name as works_title, w.initial_price, w.auction_end_date, i.image_url, a.name as artist_name" +
//            " from pieces as p, Member as m, works as w, works_image as i, artist as a," +
//            " (select works_id, sum(piece_num) as piece_sum from pieces group by works_id) as s" +
//            " where p.works_id=w.id and p.works_id=i.works_id and w.artist_id=a.id and i.type='ma'" +
//            " and p.member_id= (select id from Member where email = :email) and s.works_id=p.works_id"
//            , nativeQuery = true)
//    ArrayList<Object[]> getPiecesList(@Param("email") String email);

//    @Query(value="SELECT sum(piece_num) from pieces where works_id=:worksId", nativeQuery = true)
//    int findPieces(@Param("worksId") Long worksId);


}


