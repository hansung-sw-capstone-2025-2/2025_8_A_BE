package DiffLens.back_end.domain.search.service.interfaces;

import DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchPanelService {

    Page<PanelWithRawDataDTO> getPanelDtoList(List<String> panelIds, Pageable pageable);

}
