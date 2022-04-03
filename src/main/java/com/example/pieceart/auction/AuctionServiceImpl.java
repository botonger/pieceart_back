package com.example.pieceart.auction;

import com.example.pieceart.entity.Auction;
import com.example.pieceart.entity.Member;
import com.example.pieceart.entity.Works;
import com.example.pieceart.member.MemberRepository;
import com.example.pieceart.works.WorksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService{
    private final AuctionRepository repository;
    private final MemberRepository memberRepository;
    private final WorksRepository worksRepository;

    //유저별 입찰내역 가져오기
    @Override
    public List<AuctionDTO> findAuctionByUser(String email) {
        List<Auction> auctions = repository.findAuctionByUser(email);
        List<AuctionDTO> list = new ArrayList<>();

        auctions.forEach(auction-> {
            int currentPrice = 0;
            List<Auction> auctionList = repository.findAuctionByWorks(auction.getWorks().getId());
            if(auctionList.size()!=0) currentPrice = repository.findCurrentPrice(auction.getWorks().getId());

            AuctionDTO auctionDTO = entityToDto(auction);
            auctionDTO.setCurrentPrice(currentPrice);

            list.add(auctionDTO);
        });
        return list;
    }

    //입찰하기
    @Transactional
    @Override
    public boolean createAuction(String email, Long worksId, int currentPrice) {
        Member member = memberRepository.findByEmail(email, false).get();
        Works works = worksRepository.findById(worksId).get();

        if (repository.findAuctionByWorks(worksId).size()!=0){
            int currentMaxPrice = repository.findCurrentPrice(worksId);

            if(currentMaxPrice<currentPrice){
                Auction auction = Auction.builder().member(member).works(works).currentPrice(currentPrice).build();
                repository.save(auction);
                return true;
            }
        } else{
            Auction auction = Auction.builder().member(member).works(works).currentPrice(currentPrice).build();
            repository.save(auction);
            return true;
        }
        return false;
    }

    //입찰 취소하기
    @Transactional
    @Override
    public boolean deleteAuction(String email, Long auctionId) {
        List<Auction> auctions = repository.findAuctionByUser(email);

        List<Auction> filtered = auctions.stream().filter(auc -> auc.getId() == auctionId).collect(Collectors.toList());

        if(filtered.size() != 0 ) {
            repository.deleteById(auctionId);
            return true;
        }
        return false;
    }
}

