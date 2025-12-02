package DiffLens.back_end.domain.rawData.controller;

import DiffLens.back_end.domain.rawData.service.RawDataUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "관리자용 - 문제 시 문의바람")
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class RawDataController {

    private final RawDataUploadService rawDataPanelService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/raw-data/upload", consumes = "multipart/form-data")
    @Operation(summary = "원천데이터로.json 으로 Panel과 RawData 저장")
    public ResponseEntity<String> uploadJsonFile(@RequestParam("file") MultipartFile file) {
        try {
            // 파일 이름 확인
            String fileName = file.getOriginalFilename();
            log.info("Uploaded file: " + fileName);

            // JSON 내용 읽기
            String json = new String(file.getBytes());

            // rawData 저장
            rawDataPanelService.uploadRawData(json);

            // 필요하면 파싱해서 객체로 변환
            // ObjectMapper mapper = new ObjectMapper();
            // Map<String, Object> data = mapper.readValue(json, Map.class);

            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error reading file: " + e.getMessage());
        }
    }

}
