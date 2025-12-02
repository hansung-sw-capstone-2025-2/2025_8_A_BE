package DiffLens.back_end.domain.search.enums.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgeGroup{

    TWENTY(1L, "20-29세","20대"),
    THIRTY(2L, "30-39세", "30대"),
    FORTY(3L, "40-49세","40대"),
    FIFTY(4L, "50-59세","50대"),
    SIXTY_PLUS(5L, "60세 이상", "60대 이상"),
    ;

    private final Long id; // 내부 시드 ID ( 상대적 )
    private final String displayValue; // 화면에 띄울 값
    private final String rawDataValue; // 원천데이터 값

}
