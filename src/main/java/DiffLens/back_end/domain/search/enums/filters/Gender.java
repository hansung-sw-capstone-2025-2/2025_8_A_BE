package DiffLens.back_end.domain.search.enums.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Gender {

    MALE(1L, "남성", "남성"),
    FEMALE(2L, "여성", "여성"),
    NONE(3L, "기타", null)

    ;

    private final Long id; // 내부 시드 ID ( 상대적 )
    private final String displayValue; // 화면에 띄울 값
    private final String rawDataValue; // 원천데이터 값

    public static Gender fromRawDataValue(String rawDataValue) {
        if (rawDataValue == null) {
            return NONE;
        }
        return Arrays.stream(values())
                .filter(g -> rawDataValue.equals(g.getRawDataValue()))
                .findFirst()
                .orElse(NONE);
    }

}
