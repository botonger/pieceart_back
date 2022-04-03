package com.example.pieceart.works;
import com.example.pieceart.entity.Works;

import java.util.List;

public interface WorksService {
    List<WorksDTO> getAllWorks();
    WorksDTO getSpecificWork(Long id);
    List<WorksDTO> findWorksByArtistName(String name);
    List<WorksDTO> findWorksByTitle(String name);
    WorksDTO createWorks(WorksDTO worksDTO);
    void deleteWorks(Long id);
    WorksDTO updateWorks(Long id, WorksDTO worksDTO);

    default Works dtoToEntity(WorksDTO dto){
        Works works = Works.builder()
                .name((dto.getName()))
                .description(dto.getDescription())
                .size(dto.getSize())
                .createdYear(dto.getCreatedYear())
                .auctionStartDate(dto.getAuctionStartDate())
                .auctionEndDate(dto.getAuctionEndDate())
                .initialPrice(dto.getInitialPrice())
                .artist(dto.getArtist())
                .images(dto.getImage())
                .build();
        return works;
    }

    default WorksDTO entityToDto(Works works){
        WorksDTO worksDTO = WorksDTO.builder()
                .id(works.getId())
                .name(works.getName())
                .description(works.getDescription())
                .size(works.getSize())
                .createdYear(works.getCreatedYear())
                .auctionStartDate(works.getAuctionStartDate())
                .auctionEndDate(works.getAuctionEndDate())
                .initialPrice(works.getInitialPrice())
                .artist(works.getArtist())
                .image(works.getImages())
                .build();

        return worksDTO;
    }
}
