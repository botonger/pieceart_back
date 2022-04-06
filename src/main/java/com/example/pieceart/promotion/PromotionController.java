package com.example.pieceart.promotion;

import com.example.pieceart.works.WorksDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Tag(name="promotion", description="promotion controller")
@RestController
@RequestMapping("/api/promotion")
public class PromotionController {

    private final PromotionService promotionService;

    @Operation(summary="프로모션 전체 불러오기")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(implementation = PromotionDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPromotion(){
        List<PromotionDTO> promotions = promotionService.findAllPromotion();
        Map<String, Object> map = new HashMap<>();

        map.put("promotion", promotions);
        map.put("totalCount", promotions.size());

        return ResponseEntity.ok().body(map);
    }

    @Operation(summary="특정 프로모션 가져오기")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(implementation = PromotionDTO.class))),
            @ApiResponse(responseCode = "404", description = "service not found", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPromotionById(@PathVariable("id") Long id){
        PromotionDTO promotionDTO = promotionService.findPromotionById(id);
        Map<String, Object> map = new HashMap<>();

        if(promotionDTO == null){
            map.put("errorCode", "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
        map.put("promotion", promotionDTO);

        return ResponseEntity.ok().body(map);
    }

    @Operation(summary="프로모션 등록")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description="successful", content = @Content(schema = @Schema(implementation = PromotionDTO.class)))
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPromotion(@Valid @RequestBody PromotionDTO promotionDTO){
        PromotionDTO created = promotionService.createPromotion(promotionDTO);
        Map<String, Object> map = new HashMap<>();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        map.put("data", created);
        return ResponseEntity.created(location).body(map);
    }

    @Operation(summary="프로모션 삭제")
    @ApiResponses(value={
            @ApiResponse(responseCode = "204", description="successful", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "service not found", content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePromotion(@PathVariable("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        if(promotionService.findPromotionById(id) == null){
            map.put("errorCode", "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary="프로모션 수정")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description="successful", content = @Content(schema = @Schema(implementation = PromotionDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePromotion(@PathVariable("id") Long id, @Valid @RequestBody PromotionDTO updated){
        PromotionDTO updatedPromotion = promotionService.updatePromotion(id, updated);
        Map<String, Object> map = new HashMap<>();
        if(updatedPromotion == null){
            return createPromotion(updated);
        } else{
            map.put("data", updatedPromotion);
            return ResponseEntity.ok().body(map);
        }
    }
}