package com.example.pieceart.repository;

import com.example.pieceart.entity.File;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @EntityGraph(attributePaths = "notice", type=EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT f from File f where f.notice.id=:id")
    Optional<File> getFileOfNotice(@Param("id") Long noticeId);

    @Modifying
    @Query("DELETE from File f where f.notice.id=:id")
    void deleteFile(@Param("id") Long noticeId);
}
