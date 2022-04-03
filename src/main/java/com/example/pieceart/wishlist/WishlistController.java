package com.example.pieceart.wishlist;

import com.example.pieceart.auction.AuctionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Log4j2
@RequiredArgsConstructor
@Tag(name="wishlist", description="wishlist controller")
@RequestMapping("/api/users/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    @Operation(summary="유저별 위시리스트 전체 가져오기")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(implementation = WishlistDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getWishlist(Authentication authentication){
        String email = authentication.getPrincipal().toString();
        log.info(email);
        List<WishlistDTO> list = wishlistService.getWishList(email);
        Map<String, Object> map = new HashMap<>();
        map.put("wishlist", list);
        return ResponseEntity.ok().body(map);
    }
    @Operation(summary="위시리스트 삭제")
    @DeleteMapping("/{wishlistId}")    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Map<String, Object>> deleteWishlist(@PathVariable("wishlistId") Long wishlistId, Authentication authentication){
        String email = authentication.getPrincipal().toString();
        Map<String, Object> map = new HashMap<>();
        if(wishlistService.getWishList(email).size()==0){
            map.put("errorCode", "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
        wishlistService.deleteWishList(wishlistId);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary="위시리스트 추가")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/works/{worksId}")
    public ResponseEntity<Map<String, Object>> createWishlist(@PathVariable("worksId") Long worksId, Authentication authentication){
        String email = authentication.getPrincipal().toString();
        boolean created = wishlistService.createWishlist(email, worksId);
        Map<String, Object> map = new HashMap<>();
        map.put("status", created);
        return ResponseEntity.ok().body(map);
    }
}