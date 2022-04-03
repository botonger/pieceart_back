package com.example.pieceart.member;

import com.example.pieceart.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.isGoogle = :social AND m.email = :email")
    Optional<Member> findByEmail(@Param("email")String email, @Param("social") boolean social);

    @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.name = :name")
    Optional<Member> findByName(@Param("name")String name);

    @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Optional<Member> findByEmail(@Param("email")String email);

    @Modifying
    @Query(value="UPDATE member SET password=:password WHERE email=:email", nativeQuery = true)
    int editMember(@Param("password") String password, @Param("email") String email);
}
