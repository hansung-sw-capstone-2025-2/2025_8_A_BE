package DiffLens.back_end.domain.search.enums.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 자녀유무
@Getter
@AllArgsConstructor
public enum Children {

    EXIST(1L, "있음", "true"),
    NOT_EXIST(2L, "없음", "false")
    ;

    private final Long id; // 내부 시드 ID ( 상대적 )
    private final String displayValue; // 화면에 띄울 값
    private final String rawDataValue; // 원천데이터 값

}