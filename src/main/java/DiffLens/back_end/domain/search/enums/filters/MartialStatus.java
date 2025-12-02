package DiffLens.back_end.domain.search.enums.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MartialStatus {

    NOT_MARRIED(1L, "미혼", "미혼"),
    MARRIED(2L, "기혼", "기혼"),
    DIVORCE(3L, "이혼", "이혼"),
    DEATH(4L, "사별", "사별"),
    NONE(5L, "기타", null)

    ;

    private final Long id; // 내부 시드 ID ( 상대적 )
    private final String displayValue; // 화면에 띄울 값
    private final String rawDataValue; // 원천데이터 값rivate final String value;

    public static MartialStatus fromRawDataValue(String rawDataValue) {
        if (rawDataValue == null) {
            return NONE;
        }
        return Arrays.stream(values())
                .filter(m -> rawDataValue.equals(m.getRawDataValue()))
                .findFirst()
                .orElse(NONE);
    }

}
