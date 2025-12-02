package DiffLens.back_end.global.fastapi.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FastSearchChart {

    private String chartType;
    private String title;
    private String reason;
    private String panelColumn;
    private Data data;
    private Option options;
    private MetaData metadata;

    @Getter
    @Setter
    public static class Data {
        private List<String> labels;
        private List<DataSet> dataSets;
    }

    @Getter
    @Setter
    public static class DataSet {
        private String label;
        private List<Integer> data;
        private List<String> backgroundColor;
    }

    @Getter
    @Setter
    public static class Option {
        private Plugins plugins;
    }

    @Getter
    @Setter
    public static class Plugins {
        private Legend legend;
    }

    @Getter
    @Setter
    public static class Legend {
        private String position;
    }

    @Getter
    @Setter
    public static class MetaData {
        private Integer totalPanels;
        private String queryIntent;
        private Double confidence;
        private String relevance;
    }
}
