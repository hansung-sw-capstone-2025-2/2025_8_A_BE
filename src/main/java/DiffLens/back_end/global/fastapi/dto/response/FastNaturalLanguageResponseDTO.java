package DiffLens.back_end.global.fastapi.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 *
 * 자연어 응답
 * Fast API -> Spring Boot 응답 형태
 *
 */
public class FastNaturalLanguageResponseDTO {

    /**
     * fast api에서 패널 식별자 목록을 불러와 db에서 패널 데이터를 불러옵니다.
     */
    @Getter
    @Setter
    @ToString
    public static class NaturalSearch {

        private Boolean success;

        private Data data;


        // 매칭된 패널 Id 배열


        // 개별응답 종류
//        private List<String> panelColumns; // SearchResponseDTO.EachResponses로 분리

    }

    // ------------ NaturalSearch에서 필요한 클래스들 ------
    @Getter
    @Setter
    public static class Data{
        // 일반적인 데이터
        private Float accuracy;

        private List<String> matchedPanelIds; // 매칭된 패널 ID 목록
        private List<Float> matchScores; // 일치율

        // 차트 관련
        private FastSearchChart mainChart; // 메인차트

        private List<FastSearchChart> subCharts; // 서브차트

        private Integer totalCount;

        private ProcessingInfo processingInfo;


    }

    @Getter
    @Setter
    public static class ProcessingInfo{
        private Double embeddingTime;
        private Double searchTime;
        private Double chartGenerationTime;
        private Double totalTime;

    }


}
