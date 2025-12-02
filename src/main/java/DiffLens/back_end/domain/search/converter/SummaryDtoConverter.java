package DiffLens.back_end.domain.search.converter;


import DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO;
import DiffLens.back_end.domain.search.dto.SearchResponseDTO;
import DiffLens.back_end.global.fastapi.dto.response.MainSearchResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Component
public class SummaryDtoConverter implements SearchDtoConverter<MainSearchResponse, SearchResponseDTO.SearchResult.Summary, List<PanelWithRawDataDTO>> {

    @Override
    public SearchResponseDTO.SearchResult.Summary requestToDto(MainSearchResponse response, List<PanelWithRawDataDTO> panelList) {

        return SearchResponseDTO.SearchResult.Summary.builder()
                .totalRespondents(panelList.size())
                .averageAge(getAgeAvg(response))
                .dataCaptureDate(getCurrentDate())
                .confidenceLevel(null)
                .confidenceLevel(getConfidencePercent(response))
                .build();
    }

    private int getConfidencePercent(MainSearchResponse response) {
        List<MainSearchResponse.PanelInfo> panels = response.getPanels();
        if (panels.isEmpty()) return 0;

        double sum = panels.stream()
                .map(MainSearchResponse.PanelInfo::getSimilarity)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();

        double avg = sum / panels.size();

        return (int) Math.round(avg * 100); // 소수점 반올림 후 int로 변환
    }



    private Double getAgeAvg(MainSearchResponse response) {

        List<MainSearchResponse.PanelInfo> panels = response.getPanels();
        return panels.stream()
                .filter(p -> p.getAge() != null)  // null 제외
                .mapToInt(MainSearchResponse.PanelInfo::getAge)
                .average()
                .orElse(0.0);
    }

    private String getCurrentDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return now.format(formatter);
    }
}
