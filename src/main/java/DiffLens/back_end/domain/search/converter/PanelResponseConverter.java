package DiffLens.back_end.domain.search.converter;

import DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO;
import DiffLens.back_end.global.fastapi.dto.response.FastNaturalLanguageResponseDTO;
import DiffLens.back_end.domain.search.dto.SearchPanelDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PanelResponseConverter implements SearchDtoConverter<FastNaturalLanguageResponseDTO.NaturalSearch, SearchPanelDTO.PanelData, List<PanelWithRawDataDTO>>{
    @Override
    public SearchPanelDTO.PanelData requestToDto(FastNaturalLanguageResponseDTO.NaturalSearch response, List<PanelWithRawDataDTO> info) {
        // TODO : 개별 응답데이터 처리 로직 작성
        return new SearchPanelDTO.PanelData();
    }
}
