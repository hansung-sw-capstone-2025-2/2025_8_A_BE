package DiffLens.back_end.domain.members.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 업종
 */
@Getter
@AllArgsConstructor
public enum Industry {

    IT_SOFTWARE("IT·인터넷·소프트웨어"),
    ELECTRONICS_MANUFACTURING("전자·제조·기계"),
    FINANCE_INSURANCE("금융·보험·핀테크"),
    DISTRIBUTION_FOOD("유통·소비재·식품"),
    CULTURE_MEDIA("문화·미디어·엔터테인먼트"),
    MEDICAL_BIO("의료·제약·바이오"),
    EDUCATION_EDUTECH("교육·에듀테크"),
    PUBLIC_ADMIN("공공·비영리·행정"),
    CONSTRUCTION_INFRA("건설·부동산·인프라"),
    ENERGY_ENVIRONMENT("에너지·환경·화학"),
    TOURISM_TRAVEL("관광·여행·항공"),
    ETC("기타 산업군");

    private final String krValue;
}

