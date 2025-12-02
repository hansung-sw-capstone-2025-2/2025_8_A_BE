package DiffLens.back_end.domain.search.dto;

import DiffLens.back_end.global.dto.ResponsePageDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SearchPanelDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PanelData {
        private List<Field> fields;
        private List<Record> records;

        @JsonProperty("page_info")
        private ResponsePageDTO.CursorPageInfo cursorPageInfo;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Field {
        private String key;
        private String label;
        private String type;
        private Boolean sortable;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Record {
        @JsonProperty("respondent_id")
        private String respondentId;

        private Integer age;

        private String residence;

        @JsonProperty("final_education")
        private String finalEducation;

        @JsonProperty("net_income")
        private String netIncome;

        @JsonProperty("car_brand")
        private String carBrand;

        @JsonProperty("match_rate")
        private Integer matchRate;
    }



}
