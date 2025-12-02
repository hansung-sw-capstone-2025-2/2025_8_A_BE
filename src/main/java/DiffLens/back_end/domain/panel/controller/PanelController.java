package DiffLens.back_end.domain.panel.controller;

import DiffLens.back_end.domain.panel.dto.PanelResponseDTO;
import DiffLens.back_end.domain.panel.service.PanelService;
import DiffLens.back_end.global.responses.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "패널 API")
@RestController
@RequestMapping("/panels")
@RequiredArgsConstructor
public class PanelController {

    private final PanelService panelService;

    @GetMapping("/{panelId}")
    @Operation(summary = "특정 패널 상세 조회", description = """
            ## 개요
            특정 패널의 상세 정보를 조회하는 API입니다.

            ## 응답 데이터
            - 패널의 기본 정보 (성별, 나이, 거주지 등)
            - 패널의 속성 정보 (기기, 해시태그 등)
            - 연결된 RawData 정보

            ## 권한
            인증된 사용자만 접근 가능합니다.
            """)
    public ApiResponse<PanelResponseDTO.PanelDetails> details(@PathVariable("panelId") String panelId) {
        PanelResponseDTO.PanelDetails result = panelService.getPanelDetails(panelId);
        return ApiResponse.onSuccess(result);
    }

}
