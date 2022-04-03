package com.example.pieceart.wishlist;

import com.example.pieceart.entity.Wishlist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    @EntityGraph(attributePaths = "member", type=EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT w from Wishlist w where w.member.email=:email")
    List<Wishlist> getWorksInWishList(@Param("email") String email);

    @EntityGraph(attributePaths = "member", type=EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT w from Wishlist w where w.member.email=:email and w.works.id=:worksId")
    Optional<Wishlist> getWorksInWishList(@Param("email") String email, @Param("worksId") Long worksId);

    @Query(value="SELECT count(*) from Wishlist w WHERE w.works.id=:worksId")
    int findSumOfWishlist(@Param("worksId") Long worksId);
//    @EntityGraph(attributePaths = "member", type=EntityGraph.EntityGraphType.LOAD)
//    @Query(value = "SELECT wish.id, a.name as artist_name, w.name as works_title, w.initial_price, i.image_url," +
//            " (select sum(piece_num) from pieces group by works_id having works_id=wish.works_id)," +
//            " (select max(current_price) from auction group by works_id having works_id=wish.works_id)"+
//            " from Wishlist wish, Artist a, Works w, Works_image i" +
//            " where wish.member_id = (select id from Member where email = :email)" +
//            " and wish.works_id = w.id" +
//            " and wish.works_id = i.works_id" +
//            " and w.artist_id = a.id" +
//            " and i.type='ma'"
//            , nativeQuery = true
//    )
//    ArrayList<Object[]> getWishlistByMember(@Param("email") String email);
//
//    @Query(value = "SELECT wish.id, a.name as artist_name, w.name as works_title, w.initial_price, i.image_url," +
//            " (select sum(piece_num) from pieces group by works_id having works_id=wish.works_id)," +
//            " (select max(current_price) from auction group by works_id having works_id=wish.works_id)"+
//            " from Wishlist wish, Artist a, Works w, Works_image i" +
//            " where wish.member_id = (select id from Member where email = :email)" +
//            " and wish.works_id = w.id" +
//            " and wish.works_id = i.works_id" +
//            " and w.artist_id = a.id" +
//            " and i.type='ma'" +
//            " and wish.works_id = :worksId"
//            , nativeQuery = true
//    )
//    ArrayList<Object[]> getWishlistById(@Param("worksId") Long worksId, @Param("email") String email);



}

