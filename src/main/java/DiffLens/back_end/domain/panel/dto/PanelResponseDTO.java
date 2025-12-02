package DiffLens.back_end.domain.panel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PanelResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PanelDetails {

        @JsonProperty("panel_detail")
        private PanelDetail panelDetail;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PanelDetail {

            @JsonProperty("panel_id")
            private String panelId;

            private String summary;

            @JsonProperty("basic_info")
            private BasicInfo basicInfo;

            private List<Attribute> attributes;

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class BasicInfo {
                private String gender;

                @JsonProperty("birth_year")
                private Integer birthYear;

                private String residence;

                @JsonProperty("marital_status")
                private String maritalStatus;

                @JsonProperty("children_count")
                private Integer childrenCount;

                private String occupation;

                @JsonProperty("income_level")
                private String incomeLevel;
            }

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Attribute {
                private String key;
                private String value; // 문자열 or 배열 모두 가능

                public static Attribute of(String key, Object value) {
                    return Attribute.builder()
                            .key(key)
                            .value(value != null ? value.toString() : null)
                            .build();
                }


            }
        }

    }
}
