package DiffLens.back_end.domain.search.enums.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

// 직업
@Getter
@AllArgsConstructor
public enum Occupation {

    STUDENT(1L, "학생", "학생"),
    BUSINESS(2L, "직장인", "직장인"),
    CEO(3L, "자영업", "자영업"), // 자영업자
    JUBU(4L, "주부", "주부"), // 주부
    BAEKSU(5L, "무직", "무직"), // 무직
    ETC(6L, "기타", "기타"),
    NONE(7L, "알수없음", null)

    ;

    private final Long id; // 내부 시드 ID ( 상대적 )
    private final String displayValue; // 화면에 띄울 값
    private final String rawDataValue; // 원천데이터 값

    public static Occupation fromRawDataValue(String rawDataValue) {
        if (rawDataValue == null) {
            return NONE;
        }
        return Arrays.stream(values())
                .filter(g -> rawDataValue.equals(g.getRawDataValue()))
                .findFirst()
                .orElse(NONE);
    }

}
