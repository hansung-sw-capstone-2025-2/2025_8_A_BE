package DiffLens.back_end.domain.search.enums.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 필터 모음
@Getter
@AllArgsConstructor
public enum FilterKey {

    AGE_GROUP(100L, "age_group", "연령대", AgeGroup.class),
    CHILDREN(200L, "children", "자녀수", Children.class),
    GENDER(300L, "gender", "성별", Gender.class),
    MARTIAL_STATUS(400L, "martial_status", "결혼상태", MartialStatus.class),
    OCCUPATION(500L, "occupation", "직업", Occupation.class),
    RESIDENCE(600L, "residence", "거주지", Residence.class),
    RESPONDENT(700L, "respondent", "응답자수", Respondent.class),

    // 추가예정
    ;

    private final Long baseId; // 각 그룹에 배정하는 시드 ID의 Base. Base + Enum.id -> DB의 ID로 저장
    private final String keyEn;
    private final String keyKr;
    private final Class<? extends Enum<?>> valueEnum;

    public static List<FilterKey> getFilterList(){
        return Arrays.asList(FilterKey.values());
    }

    public static FilterKey findByKeyEn(String keyEn){
        return Arrays.stream(values())
                .filter(f -> f.getKeyEn().equals(keyEn))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid keyEn: " + keyEn));
    }


    public static List<? extends Enum<?>> getEnumList(){
        return new ArrayList<Enum<?>>(Arrays.asList(FilterKey.values()));
    }

    public static FilterKey findByEnumValue(String enumNameOrRawValue) {
        for (FilterKey filterKey : FilterKey.values()) {
            Class<? extends Enum<?>> enumClass = filterKey.getValueEnum();
            for (Enum<?> enumConst : enumClass.getEnumConstants()) {
                // enum 이름 또는 rawDataValue 비교
                try {
                    // "name()" 비교
                    if (enumConst.name().equals(enumNameOrRawValue)) {
                        return filterKey;
                    }
                    // "rawDataValue" 비교 (만약 enum이 getRawDataValue() 메서드를 가지고 있다면)
                    Object rawDataValue = enumClass.getMethod("getRawDataValue").invoke(enumConst);
                    if (rawDataValue != null && rawDataValue.toString().equals(enumNameOrRawValue)) {
                        return filterKey;
                    }
                } catch (Exception e) {
                    // getRawDataValue 없으면 무시
                }
            }
        }
        throw new IllegalArgumentException("No FilterKey found for enum value: " + enumNameOrRawValue);
    }


}
