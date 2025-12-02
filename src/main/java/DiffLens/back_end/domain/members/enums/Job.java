package DiffLens.back_end.domain.members.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 직무
 */
@Getter
@AllArgsConstructor
public enum Job {

    MANAGEMENT("경영/기획/전략"),
    MARKETING("마케팅/광고/홍보"),
    SALES("영업/고객관리"),
    IT("IT/개발/데이터"),
    DESIGN("디자인/미디어"),
    PRODUCTION("생산/제조/품질"),
    RESEARCH("연구/R&D"),
    EDUCATION("교육/컨설팅"),
    MEDICAL("의료/보건/복지"),
    FINANCE("금융/회계/법률"),
    SERVICE("서비스/유통"),
    ETC_FREELANCER("기타/프리랜서")
    ;

    private final String krValue;

}
