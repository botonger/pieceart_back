package com.example.pieceart.works;

import com.example.pieceart.entity.Works;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorksRepository extends JpaRepository<Works, Long> {

    // 작가이름으로 찾기
    @Query("Select a from Works a where a.artist.name like %:name% order by a.artist.name desc ")
    List<Works> findWorksByArtist(@Param("name")String name);

    // 작품이름으로 찾기
    @Query("Select a from Works a where a.name like %:name% order by a.name desc")
    List<Works>findWorksByTitle(@Param("name")String name);
}