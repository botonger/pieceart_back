package com.example.pieceart.repository;

import com.example.pieceart.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query(value="SELECT i from Image i WHERE i.works.id=:worksId")
    List<Image> findImagesByWorks(@Param("worksId") Long worksId);
}
