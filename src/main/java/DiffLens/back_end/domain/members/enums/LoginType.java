package DiffLens.back_end.domain.members.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {

    GENERAL("로컬", "LOCAL" ),
    GOOGLE("구글", "GOOGLE" ),
//    KAKAO,
    ;

    private final String kr;
    private final String en;

}
