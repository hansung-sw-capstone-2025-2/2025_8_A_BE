package DiffLens.back_end.domain.members.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanEnum {

    PERSONAL(1L, "개인", 100000),
    BUSINESS(2L, "비즈니스", 500000)
    ;

    private final Long id;
    private final String name;
    private final Integer price;

}
