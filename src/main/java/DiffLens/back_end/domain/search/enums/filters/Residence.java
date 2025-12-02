package DiffLens.back_end.domain.search.enums.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Residence {

    SEOUL(1L, "서울",  "서울"),
    GYEONGGI(2L, "제주",  "제주"),
    INCHEON(3L, "인천",  "인천"),
    BUSAN(4L, "부산",  "부산"),
    DAEGU(5L, "대구",  "대구"),
    GWANGJU(6L, "광주",  "광주"),
    DAEJEON(7L, "대전",  "대전"),
    ULSAN(8L, "울산",  "울산"),
    SEJONG(9L, "세종",  "세종"),
    GANGWON(10L, "강원",  "강원"),
    CHUNG_NORTH(11L, "충북",  "충북"),
    CHUNG_SOUTH(12L, "충남",  "충남"),
    JUN_NORTH(13L, "전북",  "전북"),
    JUN_SOUTH(14L, "전남",  "전남"),
    GYEONG_NORTH(15L, "경북",  "경북"),
    GYEONG_SOUTH(16L, "경남",  "경남"),
    JEJU(17L, "제주",  "제주"),

    ;

    private final Long id; // 내부 시드 ID ( 상대적 )
    private final String displayValue; // 화면에 띄울 값
    private final String rawDataValue; // 원천데이터 값

}
