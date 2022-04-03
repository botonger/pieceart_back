package com.example.pieceart.notice;

import com.example.pieceart.entity.Notice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @EntityGraph(attributePaths = "member", type=EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT n from Notice n where n.id=:id")
    Optional<Notice> getNoticeWithWriter(Long id);

    @EntityGraph(attributePaths = "member", type=EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT n from Notice n where n.member.email=:email")
    List<Notice> getNoticesByWriter(String email);
}
