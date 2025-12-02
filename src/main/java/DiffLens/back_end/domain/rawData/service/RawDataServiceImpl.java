package DiffLens.back_end.domain.rawData.service;

import DiffLens.back_end.domain.rawData.converter.RawDataConverter;
import DiffLens.back_end.domain.rawData.dto.PanelDTO;
import DiffLens.back_end.domain.rawData.entity.RawData;
import DiffLens.back_end.domain.rawData.repository.RawDataRepository;
import DiffLens.back_end.global.responses.code.status.error.RawDataStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RawDataServiceImpl implements RawDataService {

    private final RawDataRepository rawDataRepository;
    private final RawDataConverter<PanelDTO, Object> rawDataConverter;

    /**
     *
     * RawData를 pane_id로 조회하여 매핑된 DTO 객체를 반환합니다.
     *
     * @param panelId Panel 식별자
     * @return Json -> 클래스 매핑된 DTO
     */
    @Override
    @Transactional(readOnly = true)
    public PanelDTO getRawDataDTO(String panelId) {

        RawData rawData = rawDataRepository.findById(panelId)
                .orElseThrow(() -> new ErrorHandler(RawDataStatus.RAW_DATA_NOT_FOUND));

        Object json = rawData.getJson();

        return rawDataConverter.convert(json);
    }

    /**
     *
     * panelId 목록으로 List<PanelDTO>를 반환합니다.
     *
     */
    @Override
    @Transactional(readOnly = true)
    public List<PanelDTO> getRawDataDTOList(List<String> panelIds) {
        List<RawData> rawDataList = rawDataRepository.findAllByIdIn(panelIds);
        return getPanelDTOS(rawDataList);
    }

    /**
     *
     * panelId 목록으로 Page<PanelDTO>를 반환합니다.
     *
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PanelDTO> getRawDataDTOList(List<String> panelIds, Pageable pageable) {
        Page<RawData> rawDataPage = rawDataRepository.findAllByIdIn(panelIds, pageable);
        List<PanelDTO> panelDTOList = getPanelDTOS(rawDataPage.getContent());
        return new PageImpl<>(panelDTOList, pageable, rawDataPage.getTotalElements());
    }

    /**
     *
     * List<RawData> 목록을 List<PanelDTO>로 변환하여 반환합니다.
     *
     * @param rawDataList
     * @return
     */
    private List<PanelDTO> getPanelDTOS(List<RawData> rawDataList) {
        if (rawDataList.isEmpty()) {
            throw new ErrorHandler(RawDataStatus.RAW_DATA_NOT_FOUND);
        }
        return rawDataList.stream()
                .map(RawData::getJson)
                .map(rawDataConverter::convert)
                .toList();
    }

}
