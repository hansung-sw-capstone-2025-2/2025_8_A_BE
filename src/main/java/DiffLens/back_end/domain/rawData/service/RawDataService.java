package DiffLens.back_end.domain.rawData.service;

import DiffLens.back_end.domain.rawData.dto.PanelDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RawDataService {
    PanelDTO getRawDataDTO(String panelId);
    List<PanelDTO> getRawDataDTOList(List<String> panelId);
    Page<PanelDTO> getRawDataDTOList(List<String> panelId, Pageable pageable);
}
