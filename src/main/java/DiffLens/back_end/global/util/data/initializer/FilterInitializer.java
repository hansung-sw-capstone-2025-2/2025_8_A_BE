package DiffLens.back_end.global.util.data.initializer;

import DiffLens.back_end.domain.search.entity.Filter;
import DiffLens.back_end.domain.search.enums.filters.FilterKey;
import DiffLens.back_end.domain.search.repository.FilterRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 서버 시작 시 모든 FilterKey Enum 데이터를 DB Filter 테이블에 저장
 * 기존 데이터가 있어도 덮어쓰기
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FilterInitializer implements Initializer {

    private final FilterRepository filterRepository;

    @PostConstruct
    @Transactional
    public void initialize() {

        log.info("[초기 필터 데이터 저장] 시작");

        List<Filter> filters = new ArrayList<>();

        for (FilterKey filterKey : FilterKey.getFilterList()) {
            filters.addAll(convertEnumToFilters(filterKey));
        }

        filterRepository.saveAll(filters);

        log.info("[초기 필터 데이터 저장] 종료");
    }

    /**
     * FilterKey에 매핑된 Enum 값을 Filter 엔티티 리스트로 변환
     */
    private List<Filter> convertEnumToFilters(FilterKey filterKey) {
        List<Filter> filters = new ArrayList<>();
        Class<? extends Enum<?>> enumClass = filterKey.getValueEnum();

        try {
            Enum<?>[] enumValues = getEnumValues(enumClass);

            for (Enum<?> enumValue : enumValues) {
                filters.add(Filter.builder()
                        .id(filterKey.getBaseId() + getEnumId(enumValue))
                        .type(filterKey.getKeyKr())
                        .displayValue(getDisplayDataValue(enumValue))
                        .rawDataValue(getRawDataValue(enumValue))
                        .build());
            }

        } catch (Exception e) {
            throw new RuntimeException("Enum 초기화 실패: " + enumClass.getSimpleName(), e);
        }

        return filters;
    }

    /**
     * Enum 클래스에서 values() 호출
     */
    private Enum<?>[] getEnumValues(Class<? extends Enum<?>> enumClass) throws Exception {
        Method valuesMethod = enumClass.getMethod("values");
        return (Enum<?>[]) valuesMethod.invoke(null);
    }

    /**
     * Enum에서 getId() 호출
     */
    private Long getEnumId(Enum<?> enumValue) {
        try {
            Method idMethod = enumValue.getClass().getMethod("getId");
            return (Long) idMethod.invoke(enumValue);
        } catch (Exception e) {
            throw new RuntimeException("Enum id 조회 실패: " + enumValue.name(), e);
        }
    }

    /**
     * Enum에서 getDisplayDataValue() 호출
     */
    private String getDisplayDataValue(Enum<?> enumValue) {
        try {
            Method valueMethod = enumValue.getClass().getMethod("getDisplayValue");
            return (String) valueMethod.invoke(enumValue);
        } catch (Exception e) {
            throw new RuntimeException("Enum DisplayDataValue 조회 실패: " + enumValue.name(), e);
        }
    }

    /**
     * Enum에서 getRawDataValue() 호출
     */
    private String getRawDataValue(Enum<?> enumValue) {
        try {
            Method valueMethod = enumValue.getClass().getMethod("getRawDataValue");
            String rawDataValue = (String) valueMethod.invoke(enumValue);
            // null 값인 경우 기본값 설정
            return rawDataValue != null ? rawDataValue : "UNKNOWN";
        } catch (Exception e) {
            throw new RuntimeException("Enum RawDataValue 조회 실패: " + enumValue.name(), e);
        }
    }

}
