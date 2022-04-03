package com.example.pieceart.member;

import com.example.pieceart.auction.AuctionDTO;
import com.example.pieceart.security.dto.MemberDTO;
import com.example.pieceart.security.service.MemberUserDetailsService;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name="member", description="member controller")
public class MemberController {
    private final MemberUserDetailsService service;

    @Operation(summary="회원가입")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> join(@RequestBody MemberDTO member) throws SQLException {
        log.info("member: "+member);
        boolean result = service.saveMember(member);

        Map<String, Object> map = new HashMap<>();
        map.put("status", result);

        if(!result) return ResponseEntity.badRequest().body(map);
        return ResponseEntity.ok().body(map);
    }

    @Operation(summary="회원정보수정")
    @PutMapping("users/edit")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Map<String, Object>> edit(@RequestBody MemberDTO member, Authentication authentication) throws IOException {
        String result = service.editMember(member, authentication);

        Map<String, Object> map = new HashMap<>();
        map.put("token", result);
        if(result==null) return ResponseEntity.badRequest().body(map);
        else return ResponseEntity.ok().body(map);
    }
    @Operation(summary="회원탈퇴")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/users/delete")
    public ResponseEntity<Map<String, Object>> delete(Authentication authentication){
        boolean result = service.deleteMember(authentication);
        if(result) return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
