package DiffLens.back_end.domain.panel.service;

import DiffLens.back_end.domain.panel.dto.PanelResponseDTO;
import DiffLens.back_end.domain.panel.entity.Panel;
import DiffLens.back_end.domain.panel.repository.PanelRepository;
import DiffLens.back_end.domain.rawData.entity.RawData;
import DiffLens.back_end.global.responses.code.status.error.ErrorStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PanelService {

        private final PanelRepository panelRepository;

        @Transactional(readOnly = true)
        public PanelResponseDTO.PanelDetails getPanelDetails(String panelId) {
                // 1. 패널 조회
                Panel panel = panelRepository.findById(panelId)
                                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BAD_REQUEST));

                // 2. RawData 조회 (OneToOne 관계로 자동 로딩)
                RawData rawData = panel.getRawData();

                // 3. PanelDetail 생성
                PanelResponseDTO.PanelDetails.PanelDetail panelDetail = PanelResponseDTO.PanelDetails.PanelDetail
                                .builder()
                                .panelId(panel.getId())
                                .summary(panel.getProfileSummary())
                                .basicInfo(makeBasicInfo(panel))
                                .attributes(buildAttributes(panel, rawData))
                                .build();

                return PanelResponseDTO.PanelDetails.builder()
                                .panelDetail(panelDetail)
                                .build();
        }

        private PanelResponseDTO.PanelDetails.PanelDetail.BasicInfo makeBasicInfo(Panel panel){
                return PanelResponseDTO.PanelDetails.PanelDetail.BasicInfo.builder()
                        .gender(panel.getGender() != null ? panel.getGender().toString() : null)
                        .residence(panel.getRegion())
                        .maritalStatus(panel.getMaritalStatus())
                        .childrenCount(panel.getChildrenCount())
                        .occupation(panel.getOccupation())
                        .build();
        }

        private List<PanelResponseDTO.PanelDetails.PanelDetail.Attribute> buildAttributes(Panel panel, RawData rawData) {

                Map<String, Object> attrMap = new HashMap<>();
                attrMap.put("age", panel.getAge());
                attrMap.put("age_group", panel.getAgeGroup());
                attrMap.put("birth_year", panel.getBirthYear());
                attrMap.put("residence", panel.getRegion());
                attrMap.put("marital_status", panel.getMaritalStatus());
                attrMap.put("children_count", panel.getChildrenCount());
                attrMap.put("family_size", panel.getFamilySize());
                attrMap.put("education", panel.getEducation());
                attrMap.put("occupation", panel.getOccupation());
                attrMap.put("job", panel.getJob());
                attrMap.put("personal_income", panel.getPersonalIncome());
                attrMap.put("household_income", panel.getHouseholdIncome());
                attrMap.put("electronic_devices", panel.getElectronicDevices());
                attrMap.put("phone_brand", panel.getPhoneBrand());
                attrMap.put("phone_model", panel.getPhoneModel());
                attrMap.put("car_ownership", panel.getCarOwnership());
                attrMap.put("car_brand", panel.getCarBrand());
                attrMap.put("car_model", panel.getCarModel());
                attrMap.put("smoking_experience", panel.getSmokingExperience());
                attrMap.put("cigarette_brands", panel.getCigaretteBrands());
                attrMap.put("e_cigarette", panel.getECigarette());
                attrMap.put("drinking_experience", panel.getDrinkingExperience());
                attrMap.put("hash_tags", panel.getHashtags());
                attrMap.put("raw_data", rawData != null ? rawData.getJson() : "No raw data available");
                attrMap.put("embedding_dimension", panel.getEmbedding() != null ? panel.getEmbedding().length : "No embedding");

                return attrMap.entrySet().stream()
                        .map(e -> PanelResponseDTO.PanelDetails.PanelDetail.Attribute.of(e.getKey(), e.getValue()))
                        .toList();
        }


}
