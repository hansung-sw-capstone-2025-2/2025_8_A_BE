package DiffLens.back_end.domain.rawData.service;

import DiffLens.back_end.domain.rawData.dto.PanelDTO;
import DiffLens.back_end.domain.search.enums.filters.Gender;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RawDataUploadServiceImpl implements RawDataUploadService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void uploadRawData(String json) throws IOException {

        objectMapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());

        // 1. 기존 RawData 삭제
        jdbcTemplate.update("TRUNCATE TABLE raw_data CASCADE");

        // 2. 기존 Panel ID 조회
        List<String> existingPanelIds = jdbcTemplate.queryForList("SELECT id FROM panel", String.class);
        Set<String> existingIds = new HashSet<>(existingPanelIds);

        // 3. JSON -> PanelDTO
        List<PanelDTO> panelDTOList = objectMapper.readValue(json, new TypeReference<List<PanelDTO>>() {});

        // 4. RawData bulk insert (jsonb)
        String rawDataSql = "INSERT INTO raw_data (id, json) VALUES (?, ?::jsonb)";
        List<Object[]> rawDataBatch = new ArrayList<>();
        for (PanelDTO dto : panelDTOList) {
            rawDataBatch.add(new Object[]{
                    dto.getPanelId(),
                    objectMapper.writeValueAsString(dto)
            });
        }
        jdbcTemplate.batchUpdate(rawDataSql, rawDataBatch);

        // 5. Panel bulk insert
        String panelSql = "INSERT INTO panel (" +
                "id, gender, age, age_group, birth_year, residence, marital_status, children_count, family_size, education, occupation, job, " +
                "personal_income, household_income, electronic_devices, phone_brand, phone_model, car_ownership, car_brand, car_model, " +
                "smoking_experience, cigarette_brands, e_cigarette, drinking_experience, profile_summary, embedding, hash_tags" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> panelBatch = new ArrayList<>();
        for (PanelDTO dto : panelDTOList) {
            if (!existingIds.contains(dto.getPanelId())) {

                String[] devicesArray = dto.getOwnedElectronics() != null
                        ? dto.getOwnedElectronics().toArray(new String[0])
                        : new String[0];

                String[] smokingArray = dto.getSmokingExperience() != null
                        ? dto.getSmokingExperience().toArray(new String[0])
                        : new String[0];

                String[] cigBrands = dto.getCigaretteBrands() != null
                        ? dto.getCigaretteBrands().toArray(new String[0])
                        : new String[0];

                String[] eCig = dto.getECigarettes() != null
                        ? dto.getECigarettes().toArray(new String[0])
                        : new String[0];

                String[] drinking = dto.getDrinkingExperience() != null
                        ? dto.getDrinkingExperience().toArray(new String[0])
                        : new String[0];

                String profileJson = dto.getSurveyResponse() != null
                        ? objectMapper.writeValueAsString(dto.getSurveyResponse())
                        : null;

                float[] embedding = new float[4096]; // 기본값 0.0f
                String[] hashTags = new String[0];

                panelBatch.add(new Object[]{
                        dto.getPanelId(),
                        Gender.fromRawDataValue(dto.getGender()).name(),
                        dto.getAge(),
                        dto.getAgeGroup(),
                        dto.getBirthYear(),
                        dto.getResidence(),
                        dto.getMaritalStatus(),
                        dto.getChildrenCount(),
                        dto.getFamilySize(),
                        dto.getEducation(),
                        dto.getOccupation(),
                        dto.getJob(),
                        dto.getPersonalIncome(),
                        dto.getHouseholdIncome(),
                        devicesArray,
                        dto.getPhoneBrand(),
                        dto.getPhoneModel(),
                        dto.getHasCar(),
                        dto.getCarBrand(),
                        dto.getCarModel(),
                        smokingArray,
                        cigBrands,
                        eCig,
                        drinking,
                        profileJson,
                        embedding,
                        hashTags
                });
            }
        }

        jdbcTemplate.batchUpdate(panelSql, panelBatch);
    }
}
