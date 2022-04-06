package com.example.pieceart.auction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users/auction")
@RequiredArgsConstructor
@Log4j2
@Tag(name="auction", description="auction controller")
public class AuctionController {
    private final AuctionService auctionService;

    @Operation(summary="유저별 입찰내역 가져오기")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(implementation = AuctionDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAuction(Authentication authentication) {
        String email = authentication.getPrincipal().toString();
        List<AuctionDTO> list = auctionService.findAuctionByUser(email);
        Map<String, Object> map = new HashMap<>();
        map.put("auction", list);
        return ResponseEntity.ok().body(map);
    }

    @Operation(summary="응찰하기")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description="successful", content = @Content(schema = @Schema(hidden=true)))
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createNewBid(@RequestBody AuctionDTO auctionDTO,  Authentication authentication) {
        String email = authentication.getPrincipal().toString();
        Long worksId = auctionDTO.getWorksId();
        int currentPrice = auctionDTO.getCurrentPrice();

        boolean result = auctionService.createAuction(email, worksId, currentPrice);
        Map<String, Object> map = new HashMap<>();
        map.put("status", result);

        if(!result) return ResponseEntity.badRequest().body(map);

        return ResponseEntity.ok().body(map);
    }

    @Operation(summary="입찰 취소")
    @ApiResponses(value={
            @ApiResponse(responseCode = "204", description="successful", content = @Content(schema = @Schema(hidden=true)))
    })
    @DeleteMapping("/{auctionId}")
    public ResponseEntity<Map<String, Object>> deleteBid(@PathVariable("auctionId") Long auctionId, Authentication authentication) {
        String email = authentication.getPrincipal().toString();
        boolean result = auctionService.deleteAuction(email, auctionId);

        if (result) return ResponseEntity.noContent().build();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
