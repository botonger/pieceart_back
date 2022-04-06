package com.example.pieceart.pieces;

import com.example.pieceart.auction.AuctionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users/pieces")
@Tag(name="pieces", description="pieces controller")
public class PiecesController {
    private final PiecesService piecesService;

    @Operation(summary="유저별 미술품 조각구매 리스트")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(implementation = PiecesDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getPieces(Authentication authentication){
        String email = authentication.getPrincipal().toString();
        List<PiecesDTO> list = piecesService.findPiecesByUser(email);
        Map<String, Object> map = new HashMap<>();
        map.put("pieces", list);
        return ResponseEntity.ok().body(map);
    }

    @Operation(summary="미술품 조각구매 취소")
    @ApiResponses(value={
            @ApiResponse(responseCode = "204", description="successful", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description="service not found", content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{piecesId}")
    public ResponseEntity<Map<String, Object>> cancelPieces(@PathVariable("piecesId") Long piecedId, Authentication authentication){
        String email = authentication.getPrincipal().toString();
        boolean result = piecesService.cancelPieces(email, piecedId);
        if(result) return ResponseEntity.noContent().build();
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary="미술품 조각구매")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description="successful", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description="piecesNum should be less than current number of pieces left, bigger than zero")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> purchasePieces(@RequestBody PiecesDTO piecesDTO, Authentication authentication){
        String email = authentication.getPrincipal().toString();
        Long worksId = piecesDTO.getWorksId();
        int pieceNum = piecesDTO.getPiecesPurchased();

        boolean result = piecesService.purchasePieces(email, worksId, pieceNum);
        Map<String, Object> map= new HashMap<>();
        map.put("status", result);

        if(!result) return ResponseEntity.badRequest().body(map);
        return ResponseEntity.ok().body(map);
    }
}
