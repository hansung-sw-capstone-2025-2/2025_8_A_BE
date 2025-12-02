package DiffLens.back_end.domain.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ChartDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Graph{
        @JsonProperty("chart_id")
        private String chartId;

        private String reason;

        @JsonProperty("chart_type")
        private String chartType;

        private String title;

        @JsonProperty("x_axis")
        private String xAxis;

        @JsonProperty("y_axis")
        private String yAxis;

        @JsonProperty("data_points")
        private List<DataPoint> dataPoints;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataPoint {
        private String label;
        private Integer value;
    }


}