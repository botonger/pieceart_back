package com.example.pieceart.pieces;

import com.example.pieceart.auction.AuctionRepository;
import com.example.pieceart.entity.Auction;
import com.example.pieceart.entity.Member;
import com.example.pieceart.entity.Pieces;
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
public class PiecesServiceImpl implements PiecesService{
    private final PiecesRepository repository;
    private final MemberRepository memberRepository;
    private final WorksRepository worksRepository;
    private final AuctionRepository auctionRepository;

    //유저별 미술품 조각구매 불러오기
    @Override
    public List<PiecesDTO> findPiecesByUser(String email) {
        List<Pieces> pieces= repository.findPiecesByMember(email);
        List<PiecesDTO> list = new ArrayList<>();

        pieces.forEach(p ->{
            int piecesSum = 0;
            int currentPrice = 0;
            List<Pieces> result = repository.findPiecesByWorks(p.getWorks().getId());
            if(result.size()!=0) {
                piecesSum = repository.findSumOfPieces(p.getWorks().getId());
            }
            List<Auction> auctions = auctionRepository.findAuctionByWorks(p.getWorks().getId());
            if(auctions.size()!=0){
                currentPrice = auctionRepository.findCurrentPrice(p.getWorks().getId());
            }
            PiecesDTO piecesDTO = entityToDTO(p);
            piecesDTO.setPiecesLeft(1000-piecesSum);
            piecesDTO.setCurrentPrice(currentPrice);

            list.add(piecesDTO);
        });
        return list;
    }

    //조각 구매 취소하기
    @Transactional
    @Override
    public boolean cancelPieces(String email, Long piecesId) {

        List<Pieces> pieces = repository.findPiecesByMember(email);
        List<Pieces> filtered = pieces.stream().filter(p -> p.getId() == piecesId).collect(Collectors.toList());

        if(filtered.size() != 0 ) {
            repository.deleteById(piecesId);
            return true;
        }
        return false;
    }

    //조각 구매하기
    @Transactional
    @Override
    public boolean purchasePieces(String email, Long worksId, int pieceNum) {
        Member member = memberRepository.findByEmail(email, false).get();
        Works works = worksRepository.getById(worksId);

        if(repository.findPiecesByWorks(worksId).size()!=0){
            int sumOfPieces = repository.findSumOfPieces(worksId);

            if(1000 - sumOfPieces>=pieceNum){
                Pieces pieces = Pieces.builder().member(member).works(works).pieceNum(pieceNum).build();
                repository.save(pieces);
                return true;
            }
        } else{
            if(1000>=pieceNum){
                Pieces pieces = Pieces.builder().member(member).works(works).pieceNum(pieceNum).build();
                repository.save(pieces);
                return true;
            }
        }
        return false;
    }
}
