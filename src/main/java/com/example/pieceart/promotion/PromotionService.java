package com.example.pieceart.promotion;

import com.example.pieceart.entity.Promotion;
import com.example.pieceart.entity.Works;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public interface PromotionService {
    PromotionDTO findPromotionById(Long id);
    List<PromotionDTO> findAllPromotion();
    PromotionDTO createPromotion(PromotionDTO promotionDTO);
    void deletePromotion(Long id);
    PromotionDTO updatePromotion(Long id, PromotionDTO promotionDTO);

    default Promotion dtoToEntity(PromotionDTO promotionDTO, Works works){
        Promotion promotion = Promotion.builder()
                .eventTitle(promotionDTO.getEventTitle())
                .eventDescription(promotionDTO.getEventDescription())
                .works(works)
                .build();
        return promotion;
    }

    default PromotionDTO entityToDTO(Promotion promotion){
        PromotionDTO promotionDTO = PromotionDTO.builder()
                .id(promotion.getId())
                .worksId(promotion.getWorks().getId())
                .eventTitle(promotion.getEventTitle())
                .eventDescription(promotion.getEventDescription())
                .worksTitle(promotion.getWorks().getName())
                .artistName(promotion.getWorks().getArtist().getName())
                .worksImg(promotion.getWorks().getImages().stream().filter(el -> el.getType().equals("ma")).collect(Collectors.toList()).get(0))
                .build();
        return promotionDTO;
    }

}
