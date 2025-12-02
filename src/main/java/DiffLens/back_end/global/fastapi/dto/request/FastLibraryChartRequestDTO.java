package DiffLens.back_end.global.fastapi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 라이브러리로부터 차트 생성 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FastLibraryChartRequestDTO {

    @JsonProperty("panel_ids")
    private List<String> panelIds;

    @JsonProperty("library_name")
    private String libraryName;
}

