package com.example.pieceart.pieces;

import com.example.pieceart.entity.Pieces;

import java.util.List;
import java.util.stream.Collectors;

public interface PiecesService {
    List<PiecesDTO> findPiecesByUser(String email);
    boolean cancelPieces(String email, Long piecesId);
    boolean purchasePieces(String email, Long worksId, int pieceNum);

    default PiecesDTO entityToDTO(Pieces pieces){
        PiecesDTO piecesDTO = PiecesDTO.builder()
                .id(pieces.getId())
                .piecesPurchased(pieces.getPieceNum())
                .purchaseDate(pieces.getPurchaseDate())
                .worksId(pieces.getWorks().getId())
                .worksTitle(pieces.getWorks().getName())
                .initialPrice(pieces.getWorks().getInitialPrice())
                .auctionEndDate(pieces.getWorks().getAuctionEndDate())
                .imgUrl(pieces.getWorks().getImages().stream().filter(i->i.getType().equals("ma")).collect(Collectors.toList()).get(0))
                .artistName(pieces.getWorks().getArtist().getName())
                .build();

        return piecesDTO;
    }

//    default ArrayList<PiecesDTO> objectToDto(ArrayList<Object[]> pieces){
//        ArrayList<PiecesDTO> piecesList = new ArrayList<>();
//
//        pieces.forEach(el->{
//            Long id = Long.parseLong(String.valueOf(el[0]));
//            Long worksId = Long.parseLong(String.valueOf(el[2]));
//            String piecesPurchased = String.valueOf(el[3])=="null"? "0":String.valueOf(el[3]);
//            String piecesSum = String.valueOf(el[4])=="null"? "0":String.valueOf(el[4]);
//            String initialPrice = String.valueOf(el[6])=="null"? "0":String.valueOf(el[6]);
//
//
//            PiecesDTO piecesDTO = PiecesDTO.builder()
//                    .id(id)
//                    .worksId(worksId)
//                    .purchaseDate(new java.sql.Date(((Date)el[1]).getTime()).toLocalDate())
//                    .piecesPurchased(Integer.parseInt(piecesPurchased))
//                    .piecesLeft(1000-Integer.parseInt(piecesSum))
//                    .worksTitle((String)el[5])
//                    .initialPrice(Integer.parseInt(initialPrice))
//                    .auctionEndDate(new java.sql.Date(((Date)el[7]).getTime()).toLocalDate())
//                    .imgUrl((String)el[8])
//                    .artistName((String)el[9])
//                    .build();
//
//            piecesList.add(piecesDTO);
//        });
//        return piecesList;
//    }


}
