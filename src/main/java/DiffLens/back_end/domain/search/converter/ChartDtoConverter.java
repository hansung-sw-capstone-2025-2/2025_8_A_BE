package DiffLens.back_end.domain.search.converter;

import DiffLens.back_end.domain.search.dto.ChartDTO;
import DiffLens.back_end.domain.search.entity.Chart;
import DiffLens.back_end.domain.search.enums.chart.ChartType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChartDtoConverter implements SearchDtoConverter<Void , ChartDTO.Graph, Chart> {

    @Override
    public ChartDTO.Graph requestToDto(Void response, Chart chart) {
        return ChartDTO.Graph.builder()
                .chartId(null)
                .reason(chart.getReason())
                .chartType(chart.getChartType().name())
                .title(chart.getTitle())
                .xAxis(chart.getXAxis())
                .yAxis(chart.getYAxis())
                .dataPoints(getDataPoints(chart))
                .build();
    }

    private List<ChartDTO.DataPoint> getDataPoints(Chart chart) {
        if (chart.getChartType() == ChartType.PIE) {
            return getPiePoints(chart);
        }
        return getGraphPoints(chart);
    }


    private List<ChartDTO.DataPoint> getPiePoints(Chart chart) {
        List<ChartDTO.DataPoint> dataPoints = new ArrayList<>();
        List<String> labels = chart.getLabels();
        List<Integer> values = chart.getValues();

        int total = values.stream().mapToInt(Integer::intValue).sum();
        if (total == 0) return dataPoints;

        for (int i = 0; i < labels.size(); i++) {
            double percentage = (values.get(i) * 100.0) / total;
            int roundedPercentage = (int) Math.round(percentage);
            dataPoints.add(getDataPoint(labels.get(i), roundedPercentage));
        }

        return dataPoints;
    }

    private List<ChartDTO.DataPoint> getGraphPoints(Chart chart) {
        List<ChartDTO.DataPoint>  dataPoints = new ArrayList<>();
        List<String> labels = chart.getLabels();
        List<Integer> values = chart.getValues();
        for(int i = 0 ; i < labels.size() ; i++) {
            dataPoints.add(getDataPoint(labels.get(i), values.get(i)));
        }
        return dataPoints;
    }

    private ChartDTO.DataPoint getDataPoint(String label, Integer value) {
        return ChartDTO.DataPoint.builder()
                .label(label)
                .value(value)
                .build();
    }

}
