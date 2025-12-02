package DiffLens.back_end.global.fastapi.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FastReSearchResponseDTO {

    @Getter
    @Setter
    public static class ReSearch {
        private Boolean success;
        private Data data;
    }

    @Getter
    @Setter
    public static class Data{
        private FastSearchChart mainChart;
        private List<FastSearchChart> subCharts;
        private List<String> matchedPanelIds;
        private List<Float> matchScores;
        private Integer totalCount;
        private FastNaturalLanguageResponseDTO.ProcessingInfo processingInfo;
    }

}
