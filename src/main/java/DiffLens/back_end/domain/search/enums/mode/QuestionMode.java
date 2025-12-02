package DiffLens.back_end.domain.search.enums.mode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionMode {

    FLEXIBLE("유연모드"),
    STRICT("엄격모드");

    private final String kr;

}