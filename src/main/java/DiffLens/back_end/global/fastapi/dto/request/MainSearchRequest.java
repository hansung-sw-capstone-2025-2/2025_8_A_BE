package DiffLens.back_end.global.fastapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "메인 검색 요청 DTO")
public class MainSearchRequest {

    @Schema(description = "자연어 검색 쿼리 (예: '20대 남성 서울 거주 100명')", example = "30대 여성 ChatGPT 사용자 서울 거주")
    private String query;

    @Schema(description = "추천 쿼리 선택 시 제공되는 구조화된 검색 파라미터", example = "{\"age_group\": \"30대\", \"gender\": \"여성\", \"region\": \"서울\", \"limit\": 150}")
    private Map<String, Object> searchParams;

    @Schema(description = "UI에서 필터를 직접 선택한 경우", example = "{\"age_group\": \"20대\", \"occupation\": [\"전문직\", \"사무직\"], \"region\": \"경기\"}")
    private Map<String, Object> structuredFilters;

    @Schema(description = "검색 모드: 'strict' (엄격) 또는 'flexible' (유연)", example = "flexible", defaultValue = "strict")
    private String searchMode = "strict";

    @Min(1)
    @Max(1000)
    @Schema(description = "반환할 최대 결과 수", example = "100", defaultValue = "100")
    private int limit = 100;
}
