package DiffLens.back_end.domain.search.enums.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Respondent {

    T5(1L, "50명", "50"),
    H1(2L, "100명", "100"),
    H3(3L, "300명", "300"),
    H5(3L, "500명", "500"),
    TH1(3L, "1000명", "1000"),

    ;

    private final Long id; // 내부 시드 ID ( 상대적 )
    private final String displayValue; // 화면에 띄울 값
    private final String rawDataValue; // 원천데이터 값rivate final String value;

}
