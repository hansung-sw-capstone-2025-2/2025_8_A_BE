package DiffLens.back_end.domain.rawData.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import static java.util.Map.entry;

@Getter
@Setter
public class PanelDTO {
    @JsonProperty("panel_id")
    private String panelId;

    @JsonProperty("성별")
    private String gender;

    @JsonProperty("나이")
    private Integer age;

    @JsonProperty("연령대")
    private String ageGroup;

    @JsonProperty("출생년도")
    private Integer birthYear;

    @JsonProperty("거주지역")
    private String residence;

    @JsonProperty("결혼여부")
    private String maritalStatus;

    @JsonProperty("자녀수")
    private Integer childrenCount;

    @JsonProperty("가족수")
    private String familySize;

    @JsonProperty("최종학력")
    private String education;

    @JsonProperty("직업")
    private String occupation;

    @JsonProperty("직무")
    private String job;

    @JsonProperty("개인소득")
    private String personalIncome;

    @JsonProperty("가구소득")
    private String householdIncome;

    @JsonProperty("보유전자제품")
    private List<String> ownedElectronics;

    @JsonProperty("휴대폰브랜드")
    private String phoneBrand;

    @JsonProperty("휴대폰모델")
    private String phoneModel;

    @JsonProperty("차량보유")
    private String hasCar;

    @JsonProperty("차량브랜드")
    private String carBrand;

    @JsonProperty("차량모델")
    private String carModel;

    @JsonProperty("흡연경험")
    private List<String> smokingExperience;

    @JsonProperty("담배브랜드")
    private List<String> cigaretteBrands;

    @JsonProperty("전자담배")
    private List<String> eCigarettes;

    @JsonProperty("음주경험")
    private List<String> drinkingExperience;

    @JsonProperty("설문응답")
    private SurveyResponseDTO surveyResponse;

    // 한글 컬럼명 → 실제 필드명 매핑
    public static final Map<String, String> columnMapping = Map.ofEntries(
            entry("panel_id", "id"),
            entry("성별", "gender"),
            entry("나이", "age"),
            entry("연령대", "ageGroup"),
            entry("출생년도", "birthYear"),
            entry("거주지역", "residence"),
            entry("결혼여부", "maritalStatus"),
            entry("자녀수", "childrenCount"),
            entry("가족수", "familySize"),
            entry("최종학력", "education"),
            entry("직업", "occupation"),
            entry("직무", "job"),
            entry("개인소득", "personalIncome"),
            entry("가구소득", "householdIncome"),
            entry("보유전자제품", "electronicDevices"),
            entry("휴대폰브랜드", "phoneBrand"),
            entry("휴대폰모델", "phoneModel"),
            entry("차량보유", "carOwnership"),
            entry("차량브랜드", "carBrand"),
            entry("차량모델", "carModel"),
            entry("흡연경험", "smokingExperience"),
            entry("담배브랜드", "cigaretteBrands"),
            entry("전자담배", "eCigarette"),
            entry("음주경험", "drinkingExperience"),
            entry("설문응답", "profileSummary")
    );

}
