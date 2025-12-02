package DiffLens.back_end.domain.search.service.interfaces;

import DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO;
import DiffLens.back_end.global.fastapi.dto.response.FastNaturalLanguageResponseDTO;
import DiffLens.back_end.domain.search.entity.Chart;
import DiffLens.back_end.domain.search.entity.SearchHistory;

import java.util.List;

public interface ChartService {
    public List<Chart> makeChart(FastNaturalLanguageResponseDTO.Data fastPanelResponseDTO, SearchHistory searchHistory, List<PanelWithRawDataDTO> foundPanels);
}
