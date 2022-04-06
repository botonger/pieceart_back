package com.example.pieceart.notice;

import com.example.pieceart.auction.AuctionDTO;
import com.example.pieceart.entity.Notice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("/api")
@Tag(name="notice", description="notice controller")
public class NoticeController {
    private final NoticeService service;

    @Operation(summary="공지사항 불러오기")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(implementation = NoticeDTO.class)))
    })
    @GetMapping("/notices")
    public ResponseEntity<Map<String, Object>> getNotices(@RequestParam(value="page", required = false, defaultValue = "1") int page,
                                                          @RequestParam(value="search", required = false, defaultValue = "") String search){
        int totalCount = service.findAll().size();
        List<NoticeDTO> notices = service.findByPage(page);

        Map<String, Object> map = new HashMap<>();
        if(notices.isEmpty()){
            map.put("errorCode", "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
        notices = notices.stream().filter(notice -> notice.getTitle().contains(search)).collect(Collectors.toList());
        map.put("count", notices.size());
        map.put("totalCount", totalCount);
        map.put("data", notices);

        return ResponseEntity.ok().body(map);
    }

    @Operation(summary="특정 공지사항 파일 불러오기 및 조회수 증가")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description="successful", content = @Content(schema = @Schema(implementation = NoticeDTO.class)))
    })
    @GetMapping("notices/{id}")
    public ResponseEntity<byte[]> getNoticeById(@PathVariable("id") Long id) throws UnsupportedEncodingException {
        String filename = service.findById(id).getFileName();
        if(filename == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        log.info("filename: "+filename);
        ResponseEntity<byte[]> result = null;
        HttpHeaders header = new org.springframework.http.HttpHeaders();

        try{
            String srcFileName = URLDecoder.decode(filename, "UTF-8");
            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/upload";
            File file = new File(projectPath+File.separator+srcFileName);

            header.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

        } catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    @Operation(summary="공지사항 생성")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description="successful", content = @Content(schema = @Schema(implementation = NoticeDTO.class)))
    })
    @PostMapping(value="/admin/notices", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Map<String, Object>> createNotice(@RequestPart(value="notice") NoticeDTO noticeDTO,
                                                            @RequestPart(value="file", required=false) MultipartFile file,
                                                            Authentication authentication) throws IOException {

        String email = authentication.getPrincipal().toString();
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/upload";
        UUID uuid = UUID.randomUUID();

        String fileName = null;
        if(file != null){
            fileName = uuid+ "_" + file.getOriginalFilename();

            log.info("fileName: "+fileName);

            File saveFile = new File(projectPath, fileName);

            try {
                file.transferTo(saveFile);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        NoticeDTO created = service.create(noticeDTO, fileName, email);
        Map<String, Object> map = new HashMap<>();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        map.put("data", created);
        return ResponseEntity.created(location).body(map);
    }

    @Operation(summary="공지사항 수정")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description="successful", content = @Content(schema = @Schema(implementation = NoticeDTO.class)))
    })
    @PutMapping("admin/notices/{id}")
    public ResponseEntity<Map<String, Object>> updateNotice(@PathVariable("id") Long id, @RequestPart(value="notice") NoticeDTO noticeDTO,
                                                            @RequestPart(value="file", required=false) MultipartFile file,
                                                            Authentication authentication) throws IOException{
        String email = authentication.getPrincipal().toString();
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/upload";
        UUID uuid = UUID.randomUUID();

        String fileName = null;
        if(file != null){
            fileName = uuid+ "_" + file.getOriginalFilename();

            log.info("fileName: "+fileName);

            File saveFile = new File(projectPath, fileName);

            try {
                file.transferTo(saveFile);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        NoticeDTO updatedNotice = service.update(noticeDTO, fileName, email);
        Map<String, Object> map = new HashMap<>();
        map.put("data", updatedNotice);

        return ResponseEntity.ok().body(map);
    }

    @Operation(summary="공지사항 삭제")
    @ApiResponses(value={
            @ApiResponse(responseCode = "204", description="successful", content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("admin/notices/{id}")
    public ResponseEntity<Map<String, Object>> deleteNotice(@PathVariable("id") Long id, @RequestParam(value="pass", required = false, defaultValue = "") String pass) {
        Map<String, Object> map = new HashMap<>();
        if(service.findById(id) == null){
            map.put("errorCode", "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
        boolean result = service.delete(id, pass);
        if(result) return ResponseEntity.noContent().build();
        else {
            map.put("status", "false");
            return ResponseEntity.ok().body(map);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidExceptions(MethodArgumentNotValidException e){
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        Map<String, Object> map = new HashMap<>(errors.size());
        errors.forEach((error)->{
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return ResponseEntity.badRequest().body(map);
    }
}
