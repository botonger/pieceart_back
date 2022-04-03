package com.example.pieceart.wishlist;

import com.example.pieceart.entity.Member;
import com.example.pieceart.entity.Pieces;
import com.example.pieceart.entity.Wishlist;
import com.example.pieceart.entity.Works;
import com.example.pieceart.member.MemberRepository;
import com.example.pieceart.pieces.PiecesRepository;
import com.example.pieceart.works.WorksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService{
    private final WishlistRepository repository;
    private final MemberRepository memberRepository;
    private final WorksRepository worksRepository;
    private final PiecesRepository piecesRepository;

    @Override
    public List<WishlistDTO> getWishList(String email) {
        List<Wishlist> Wishlist = repository.getWorksInWishList(email);
        List<WishlistDTO> list = new ArrayList<>();
        Wishlist.forEach(wish->{
            int piecesLeft = 0;
            List<Pieces> pieces = piecesRepository.findPiecesByWorks(wish.getWorks().getId());
            if(pieces.size()!=0) piecesLeft = 1000-piecesRepository.findSumOfPieces(wish.getWorks().getId());

            WishlistDTO wishlistDTO = entityToDto(wish);
            wishlistDTO.setPiecesLeft(piecesLeft);

            list.add(wishlistDTO);
        });

        return list;
    }

    public Long getWishOrNot(String email, Long worksId){
        Optional<Wishlist> wishlist = repository.getWorksInWishList(email, worksId);

        if(wishlist.isPresent()) return wishlist.get().getId();
        return (long)0;
    }

    @Override
    public boolean createWishlist(String email, Long worksId) {
        Member member = memberRepository.findByEmail(email, false).get();
        Works works = worksRepository.findById(worksId).get();

        Wishlist wishlist = Wishlist.builder().member(member).works(works).build();
        Wishlist result = repository.save(wishlist);
        if(result!=null) return true;

        return false;
    }

    @Transactional
    @Override
    public void deleteWishList(Long wishlistId) {
        repository.deleteById(wishlistId);
    }






}
