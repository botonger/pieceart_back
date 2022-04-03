package com.example.pieceart.works;

import com.example.pieceart.auction.AuctionRepository;
import com.example.pieceart.entity.Auction;
import com.example.pieceart.entity.Pieces;
import com.example.pieceart.entity.Works;
import com.example.pieceart.pieces.PiecesRepository;
import com.example.pieceart.wishlist.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorksServiceImpl implements WorksService{
    private final WorksRepository worksRepository;
    private final PiecesRepository piecesRepository;
    private final AuctionRepository auctionRepository;
    private final WishlistRepository wishlistRepository;

    //전체 작품 목록 가져오기
    @Transactional(readOnly = true)
    public List<WorksDTO> getAllWorks(){
        List<Works> works = worksRepository.findAll();
        List<WorksDTO> list = new ArrayList<>();

        works.forEach(w->{
            int sumOfPieces = 0;
            int currentPrice = 0;
            int sumOfWishlist = 0;
            List<Pieces> pieces= piecesRepository.findPiecesByWorks(w.getId());
            if(pieces.size()!=0) sumOfPieces = piecesRepository.findSumOfPieces(w.getId());
            List<Auction> auctions = auctionRepository.findAuctionByWorks(w.getId());
            if(auctions.size()!=0) currentPrice = auctionRepository.findCurrentPrice(w.getId());
            sumOfWishlist = wishlistRepository.findSumOfWishlist(w.getId());
            WorksDTO worksDTO = entityToDto(w);
            worksDTO.setCurrentPrice(currentPrice);
            worksDTO.setSumOfPieces(sumOfPieces);
            worksDTO.setSumOfWishlist(sumOfWishlist);

            list.add(worksDTO);
        });

        return list;
    }

    //특정 작품 가져오기
    public WorksDTO getSpecificWork(Long id){
        Optional<Works> works = worksRepository.findById(id);
        if(works.isPresent()){
            int sumOfPieces = 0;
            int currentPrice = 0;

            List<Pieces> pieces = piecesRepository.findPiecesByWorks(id);
            List<Auction> auctions = auctionRepository.findAuctionByWorks(id);
            if(pieces.size()!=0) sumOfPieces = piecesRepository.findSumOfPieces(id);
            if(auctions.size()!=0) currentPrice = auctionRepository.findCurrentPrice(id);
            WorksDTO worksDTO = entityToDto(works.get());
            worksDTO.setCurrentPrice(currentPrice);
            worksDTO.setSumOfPieces(sumOfPieces);

            return worksDTO;
        }
        return null;
    }

    //아티스트별 작품 가져오기(검색)
    @Override
    @Transactional
    public List<WorksDTO> findWorksByArtistName(String name){
        List<Works> works = worksRepository.findWorksByArtist(name);
        List<WorksDTO> list = new ArrayList<>();

        works.forEach(work->{
            WorksDTO worksDTO = entityToDto(work);
            list.add(worksDTO);
        });
        return list;
    }

    //작품 타이틀별 작품 가져오기(검색)
    @Override
    @Transactional
    public List<WorksDTO> findWorksByTitle(String name){
        List<Works> works = worksRepository.findWorksByTitle(name);
        List<WorksDTO> list = new ArrayList<>();

        works.forEach(work -> {
            WorksDTO worksDTO = entityToDto(work);
            list.add(worksDTO);
        });
        return list;
    }

    //작품 등록하기
    @Override
    @Transactional
    public WorksDTO createWorks(WorksDTO worksDTO){
        Works created  = worksRepository.save(dtoToEntity(worksDTO));
        return entityToDto(created);
    }

    //작품 삭제하기
    @Transactional
    @Override
    public void deleteWorks(Long id){
        worksRepository.deleteById(id);
    }

    //작품 수정하기
    @Override
    @Transactional
    public WorksDTO updateWorks(Long id, WorksDTO worksDTO){
        return worksRepository.findById(id)
                .map(works->{
                    Works updated = Works.builder()
                            .id(works.getId())
                            .name(worksDTO.getName())
                            .description(worksDTO.getDescription())
                            .size(worksDTO.getSize())
                            .createdYear(worksDTO.getCreatedYear())
                            .auctionStartDate(worksDTO.getAuctionStartDate())
                            .auctionEndDate(worksDTO.getAuctionEndDate())
                            .initialPrice(worksDTO.getInitialPrice())
                            .artist(works.getArtist())
                            .images(works.getImages())
                            .build();
                    return entityToDto(worksRepository.save(updated));
                }).get();
    }
}
