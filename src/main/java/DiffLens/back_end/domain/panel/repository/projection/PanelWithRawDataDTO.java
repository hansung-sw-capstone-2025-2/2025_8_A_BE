package DiffLens.back_end.domain.panel.repository.projection;

import DiffLens.back_end.domain.panel.entity.Panel;
import DiffLens.back_end.domain.rawData.entity.RawData;
import DiffLens.back_end.domain.search.enums.filters.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 패널 전체 조회 시 Hibernate와 PostgreSQL의 vector 타입 파싱 문제로 인해
 * Panel의 embedding만 제외하고 조회하도록 하는 DTO
 */
@Getter
@AllArgsConstructor
@ToString
public class PanelWithRawDataDTO {
    private String id;
    private Gender gender;
    private Integer age;
    private String ageGroup;
    private Integer birthYear;
    private String region;
    private String residence;
    private String maritalStatus;
    private Integer childrenCount;
    private String familySize;
    private String education;
    private String occupation;
    private String job;
    private String personalIncome;
    private String householdIncome;
    private List<String> electronicDevices;
    private String phoneBrand;
    private String phoneModel;
    private String carOwnership;
    private String carBrand;
    private String carModel;
    private List<String> smokingExperience;
    private List<String> cigaretteBrands;
    private List<String> eCigarette;
    private List<String> drinkingExperience;
    private String profileSummary;
    private List<String> hashTags;
    private RawData rawData;

    public static PanelWithRawDataDTO fromEntity(Panel panel) {
        return new PanelWithRawDataDTO(
                panel.getId(),
                panel.getGender(),
                panel.getAge(),
                panel.getAgeGroup(),
                panel.getBirthYear(),
                panel.getRegion(),
                panel.getResidence(),
                panel.getMaritalStatus(),
                panel.getChildrenCount(),
                panel.getFamilySize(),
                panel.getEducation(),
                panel.getOccupation(),
                panel.getJob(),
                panel.getPersonalIncome(),
                panel.getHouseholdIncome(),
                panel.getElectronicDevices(),
                panel.getPhoneBrand(),
                panel.getPhoneModel(),
                panel.getCarOwnership(),
                panel.getCarBrand(),
                panel.getCarModel(),
                panel.getSmokingExperience(),
                panel.getCigaretteBrands(),
                panel.getECigarette(),
                panel.getDrinkingExperience(),
                panel.getProfileSummary(),
                panel.getHashtags(),
                panel.getRawData()
        );
    }
}