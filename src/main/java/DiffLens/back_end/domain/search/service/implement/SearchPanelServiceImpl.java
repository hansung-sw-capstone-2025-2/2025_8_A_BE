package DiffLens.back_end.domain.search.service.implement;

import DiffLens.back_end.domain.panel.repository.PanelRepository;
import DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO;
import DiffLens.back_end.domain.search.service.interfaces.SearchPanelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchPanelServiceImpl implements SearchPanelService {

    private final PanelRepository panelRepository;

    @Override
    public Page<PanelWithRawDataDTO> getPanelDtoList(List<String> panelIds, Pageable pageable) {
        return panelRepository.findPanelsWithRawDataByIdsInPage(panelIds, pageable);
    }

}
