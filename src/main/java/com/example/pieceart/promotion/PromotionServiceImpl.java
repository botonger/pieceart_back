package com.example.pieceart.promotion;

import com.example.pieceart.entity.Promotion;
import com.example.pieceart.entity.Works;
import com.example.pieceart.works.WorksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final WorksRepository worksRepository;

    //프로모션 전체 가져오기
    @Override
    public List<PromotionDTO> findAllPromotion(){
        List<Promotion> promotions = promotionRepository.findAll();
        List<PromotionDTO> list = new ArrayList<>();
        promotions.forEach(promotion -> list.add(entityToDTO(promotion)));
        return list;
    }

    //특정 프로모션 가져오기
    @Override
    public PromotionDTO findPromotionById(Long id){
        Optional<Promotion> promotion = promotionRepository.findById(id);
        if(promotion.isPresent()){
            Promotion promotion1 = promotion.get();
            return entityToDTO(promotionRepository.save(promotion1));
        }
        return null;
    }

    //프로모션 등록하기
    @Override
    @Transactional
    public PromotionDTO createPromotion(PromotionDTO promotionDTO){
        Works works = worksRepository.getById(promotionDTO.getWorksId());
        Promotion created  = promotionRepository.save(dtoToEntity(promotionDTO, works));
        return entityToDTO(created);
    }

    //프로모션 삭제하기
    public void deletePromotion(Long id){
        promotionRepository.deleteById(id);
    }

    //프로모션 수정하기
    @Override
    @Transactional
    public PromotionDTO updatePromotion(Long id, PromotionDTO promotionDTO){
        return promotionRepository.findById(id)
                .map(promotion->{
                    Promotion updated = Promotion.builder()
                            .id(promotion.getId())
                            .eventDescription(promotionDTO.getEventDescription())
                            .eventTitle(promotionDTO.getEventTitle())
                            .works(promotion.getWorks())
                            .build();
                    return entityToDTO(promotionRepository.save(updated));
                }).get();
    }
}
