package com.example.pieceart.works;

import com.example.pieceart.auction.AuctionDTO;
import com.example.pieceart.wishlist.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/works")
@Log4j2
@Tag(name="works", description="works controller")
public class WorksController {
    private final WorksService worksService;
    private final WishlistService wishlistService;

    @Operation(summary="작품 목록 가져오기")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(implementation = WorksDTO.class))),
            @ApiResponse(responseCode = "404", description = "service not found", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllWorks(@RequestParam(value = "artist", required = false, defaultValue = "") String artistName,
                                                           @RequestParam(value="title", required = false, defaultValue = "") String title){
        List<WorksDTO> works = null;
        if(!artistName.equals("") && title.equals("")) {works = worksService.findWorksByArtistName(artistName);}
        else if(!title.equals("") && artistName.equals("")) {works = worksService.findWorksByTitle(title);}
        else if(artistName.equals("") && title.equals("")){ works = worksService.getAllWorks();}

        Map<String, Object> map = new HashMap<>();

        if(works.isEmpty()){
            map.put("errorCode", "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
        map.put("count", works.size());
        map.put("works", works);

        return ResponseEntity.ok().body(map);
    }

    @Operation(summary="특정 작품 가져오기")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(implementation = WorksDTO.class)))
    })
    @GetMapping("/{worksId}")
    public ResponseEntity<Map<String, Object>> getWorksById(@PathVariable("worksId") Long worksId, Authentication authentication){
        WorksDTO worksDTO= worksService.getSpecificWork(worksId);

        Map<String, Object> map = new HashMap<>();
        map.put("works", worksDTO);

        if(authentication!=null){
            Long wishlistId= wishlistService.getWishOrNot(authentication.getPrincipal().toString(), worksId);
            map.put("wishlistCheck", wishlistId);
        } else{
            map.put("wishlistCheck", false);
        }

        return ResponseEntity.ok().body(map);
    }

    @Operation(summary="작품 등록")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description="successful", content = @Content(schema = @Schema(implementation = WorksDTO.class)))
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createWorks(@Valid @RequestBody WorksDTO worksDTO){
        WorksDTO created = worksService.createWorks(worksDTO);
        Map<String, Object> map = new HashMap<>();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        map.put("data", created);
        return ResponseEntity.created(location).body(map);
    }


    @Operation(summary="작품 삭제")
    @ApiResponses(value={
            @ApiResponse(responseCode = "204", description="successful", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "service not found", content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteWorks(@PathVariable("id") Long id){
        Map<String, Object> map = new HashMap<>();
        if(worksService.getSpecificWork(id) == null){
            map.put("errorCode", "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
        worksService.deleteWorks(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary="작품 수정")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description="successful", content = @Content(schema = @Schema(implementation = WorksDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateWorks(@PathVariable("id") Long id, @Valid @RequestBody WorksDTO updated){
        WorksDTO updatedWorks = worksService.updateWorks(id, updated);
        Map<String, Object> map = new HashMap<>();
        if(updatedWorks == null){
            return createWorks(updated);
        } else{
            map.put("data", updatedWorks);
            return ResponseEntity.ok().body(map);
        }
    }

}
