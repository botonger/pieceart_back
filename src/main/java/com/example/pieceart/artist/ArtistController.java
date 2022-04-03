package com.example.pieceart.artist;

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
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/artist")
@Tag(name="artist", description="artist controller")
public class ArtistController {

    private final ArtistService artistService;

    @Operation(summary="특정 작가 가져오기")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(implementation = ArtistDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getArtistById(@PathVariable("id") Long id){
        ArtistDTO artistDTO = artistService.findArtistById(id);
        Map<String, Object> map = new HashMap<>();

        if(artistDTO == null){
            map.put("errorCode", "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
        map.put("data", artistDTO);

        return ResponseEntity.ok().body(map);
    }



    @Operation(summary="작가 등록")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(implementation = ArtistDTO.class)))
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createArtist(@Valid @RequestBody ArtistDTO artistDTO){
        ArtistDTO created = artistService.createArtist(artistDTO);
        Map<String, Object> map = new HashMap<>();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        map.put("data", created);
        return ResponseEntity.created(location).body(map);
    }

    @Operation(summary="작가 삭제")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteArtist(@PathVariable("id") Long id){
        Map<String, Object> map = new HashMap<>();
        if(artistService.findArtistById(id) == null){
            map.put("errorCode", "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
        artistService.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary="작가 수정")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(implementation = ArtistDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateArtist(@PathVariable("id") Long id, @Valid @RequestBody ArtistDTO updated){
        ArtistDTO updatedWorks = artistService.updateArtist(id, updated);
        Map<String, Object> map = new HashMap<>();
        if(updatedWorks == null){
            return createArtist(updated);
        } else{
            map.put("data", updatedWorks);
            return ResponseEntity.ok().body(map);
        }
    }
}
