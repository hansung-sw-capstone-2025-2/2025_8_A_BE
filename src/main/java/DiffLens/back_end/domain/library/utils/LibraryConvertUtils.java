package DiffLens.back_end.domain.library.utils;

import DiffLens.back_end.domain.library.dto.LibraryCompareResponseDTO;
import DiffLens.back_end.domain.library.dto.LibraryResponseDTO;
import DiffLens.back_end.domain.library.entity.Library;
import DiffLens.back_end.domain.library.entity.SearchHistoryLibrary;
import DiffLens.back_end.domain.library.repository.SearchHistoryLibraryRepository;
import DiffLens.back_end.domain.search.entity.Filter;
import DiffLens.back_end.domain.search.entity.SearchFilter;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import DiffLens.back_end.domain.search.repository.FilterRepository;
import DiffLens.back_end.domain.search.repository.SearchFilterRepository;
import DiffLens.back_end.global.fastapi.dto.response.FastChartResponseDTO;
import DiffLens.back_end.global.fastapi.dto.response.FastLibraryCompareResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


/**
 *
 * LibraryConvertUtils
 * -------------------
 * Library 관련 DTO 및 FastAPI 응답 데이터를 변환하는 유틸리티 클래스.
 * LibraryAnalysisServiceImpl 등에서 재사용 가능하도록 변환 로직을 통합.
 *
 * convertKeyInsights(): KeyInsights를 LibraryCompareResponseDTO.Insights로 변환 (nullable 처리)
 * convertToChartData(): FastAPI ChartData를 LibraryResponseDTO.LibraryDashboard.ChartData로 변환
 * convertToChartDataPoint(): FastAPI ChartDataPoint를 LibraryResponseDTO.LibraryDashboard.ChartDataPoint로 변환
 * convertGroupInfo(): CohortBasicInfo를 LibraryCompareResponseDTO.GroupInfo로 변환
 * convertFilters(): Library 객체의 SearchFilter 및 Filter 데이터를 LibraryCompareResponseDTO.Filter 리스트로 변환
 * convertCharacteristics(): CharacteristicComparison 리스트를 LibraryCompareResponseDTO.KeyCharacteristic 리스트로 변환
 * convertBasicInfoComparisons(): BasicInfoComparison, RegionDistribution, GenderDistribution 데이터를 LibraryCompareResponseDTO.Comparisons로 변환
 *
 */
@Component
@RequiredArgsConstructor
public class LibraryConvertUtils {

    private final FilterRepository filterRepository;
    private final SearchFilterRepository searchFilterRepository;
    private final SearchHistoryLibraryRepository searchHistoryLibraryRepository;

    /**
     * KeyInsights를 Insights로 변환 (nullable 처리)
     */
    public LibraryCompareResponseDTO.Insights convertKeyInsights(FastLibraryCompareResponseDTO.KeyInsights keyInsights) {
        if (keyInsights == null) return null;
        return LibraryCompareResponseDTO.Insights.builder()
                .difference(keyInsights.getMainDifferences())
                .common(keyInsights.getCommonalities())
                .implication(keyInsights.getImplications())
                .build();
    }

    /**
     * FastAPI ChartData를 LibraryDashboard ChartData로 변환
     */
    public LibraryResponseDTO.LibraryDashboard.ChartData convertToChartData(FastChartResponseDTO.ChartData chart) {
        if (chart == null) return null;

        List<LibraryResponseDTO.LibraryDashboard.ChartDataPoint> points = chart.getData().stream()
                .map(this::convertToChartDataPoint)
                .toList();

        return LibraryResponseDTO.LibraryDashboard.ChartData.builder()
                .chartType(chart.getChartType())
                .metric(chart.getMetric())
                .title(chart.getTitle())
                .reasoning(chart.getReasoning())
                .data(points)
                .build();
    }

    /**
     * FastAPI ChartDataPoint를 LibraryDashboard ChartDataPoint로 변환
     */
    public LibraryResponseDTO.LibraryDashboard.ChartDataPoint convertToChartDataPoint(FastChartResponseDTO.ChartDataPoint point) {
        if (point == null) return null;

        return LibraryResponseDTO.LibraryDashboard.ChartDataPoint.builder()
                .category(point.getCategory())
                .value(point.getValue())
                .male(point.getMale())
                .maleMax(point.getMaleMax())
                .female(point.getFemale())
                .femaleMax(point.getFemaleMax())
                .id(point.getId())
                .name(point.getName())
                .build();
    }

    /**
     * CohortBasicInfo를 GroupInfo로 변환
     */
    public LibraryCompareResponseDTO.GroupInfo convertGroupInfo(FastLibraryCompareResponseDTO.CohortBasicInfo cohortInfo, Library library) {
        return LibraryCompareResponseDTO.GroupInfo.builder()
                .libraryId(Long.parseLong(cohortInfo.getCohortId()))
                .libraryName(cohortInfo.getCohortName())
                .totalCount(cohortInfo.getPanelCount())
                .filters(convertFilters(library))
                .build();
    }

    public List<LibraryCompareResponseDTO.Filter> convertFilters(Library library) {
        List<SearchHistoryLibrary> searchHistoryLibraries = searchHistoryLibraryRepository
                .findByLibraryId(library.getId());

        List<SearchHistory> histories = searchHistoryLibraries.stream()
                .map(SearchHistoryLibrary::getHistory)
                .toList();

        List<SearchFilter> searchFilters = searchFilterRepository.findBySearchHistory(histories);

        Set<Long> filterIds = new HashSet<>();
        searchFilters.forEach(sf -> filterIds.addAll(sf.getFilters()));

        List<Filter> filters = filterRepository.findByIds(filterIds);

        Map<String, List<String>> grouped = filters.stream()
                .collect(Collectors.groupingBy(
                        Filter::getType,
                        Collectors.mapping(Filter::getDisplayValue, Collectors.toList())
                ));

        return grouped.entrySet().stream()
                .map(e -> LibraryCompareResponseDTO.Filter.builder()
                        .key(e.getKey())
                        .values(e.getValue())
                        .build())
                .toList();
    }

    /**
     * CharacteristicComparison 리스트를 KeyCharacteristic 리스트로 변환
     */
    public List<LibraryCompareResponseDTO.KeyCharacteristic> convertCharacteristics(
            List<FastLibraryCompareResponseDTO.CharacteristicComparison> characteristics) {

        if (characteristics == null || characteristics.isEmpty()) return List.of();

        return characteristics.stream()
                .map(f -> LibraryCompareResponseDTO.KeyCharacteristic.builder()
                        .characteristic(f.getCharacteristic())
                        .description(null)
                        .group1Percentage(Optional.ofNullable(f.getCohort1Percentage()).map(Number::intValue).orElse(0))
                        .group2Percentage(Optional.ofNullable(f.getCohort2Percentage()).map(Number::intValue).orElse(0))
                        .difference(Optional.ofNullable(f.getDifferencePercentage()).map(Number::intValue).orElse(0))
                        .build())
                .toList();
    }

    /**
     * BasicInfoComparison 리스트를 Comparisons로 변환
     * basic_info에서 메트릭별로 값을 추출하여 GroupMetrics 구성
     * region_distribution 데이터를 group1, group2의 지역 필드에 매핑
     * gender_distribution 데이터를 group1, group2의 성별 필드에 매핑
     */
    public LibraryCompareResponseDTO.Comparisons convertBasicInfoComparisons(
            List<FastLibraryCompareResponseDTO.BasicInfoComparison> basicInfo,
            FastLibraryCompareResponseDTO.RegionDistribution region,
            FastLibraryCompareResponseDTO.GenderDistribution gender) {

        // basic_info에서 메트릭별로 값 추출
        LibraryCompareResponseDTO.GroupMetrics.GroupMetricsBuilder g1 = LibraryCompareResponseDTO.GroupMetrics.builder();
        LibraryCompareResponseDTO.GroupMetrics.GroupMetricsBuilder g2 = LibraryCompareResponseDTO.GroupMetrics.builder();

        if (basicInfo != null) {
            basicInfo.forEach(info -> {
                String metric = info.getMetricName();
                Double c1 = info.getCohort1Value();
                Double c2 = info.getCohort2Value();
                switch (metric) {
                    case "age" -> { if (c1 != null) g1.avgAge(c1); if (c2 != null) g2.avgAge(c2); }
                    case "family_size" -> { if (c1 != null) g1.avgFamily(c1); if (c2 != null) g2.avgFamily(c2); }
                    case "children_count" -> { if (c1 != null) g1.avgChildren(c1); if (c2 != null) g2.avgChildren(c2); }
                    case "personal_income" -> { if (c1 != null) g1.avgPersonalIncome(c1.intValue()); if (c2 != null) g2.avgPersonalIncome(c2.intValue()); }
                    case "household_income" -> { if (c1 != null) g1.avgFamilyIncome(c1.intValue()); if (c2 != null) g2.avgFamilyIncome(c2.intValue()); }
                    case "car_ownership" -> { if (c1 != null) g1.ratePossessingCar(c1.intValue()); if (c2 != null) g2.ratePossessingCar(c2.intValue()); }
                }
            });
        }

        // region_distribution 데이터를 group1, group2의 지역 필드에 매핑
        if (region != null) {
            Map<String, Double> r1 = Optional.ofNullable(region.getCohort1()).orElse(Map.of());
            Map<String, Double> r2 = Optional.ofNullable(region.getCohort2()).orElse(Map.of());
            g1.seoul(r1.getOrDefault("서울", 0.0).intValue());
            g1.gyeonggi(r1.getOrDefault("경기", 0.0).intValue());
            g1.busan(r1.getOrDefault("부산", 0.0).intValue());
            g1.regionEtc(r1.getOrDefault("기타", 0.0).intValue());
            g2.seoul(r2.getOrDefault("서울", 0.0).intValue());
            g2.gyeonggi(r2.getOrDefault("경기", 0.0).intValue());
            g2.busan(r2.getOrDefault("부산", 0.0).intValue());
            g2.regionEtc(r2.getOrDefault("기타", 0.0).intValue());
        }

        // gender_distribution 데이터를 group1, group2의 성별 필드에 매핑
        if (gender != null) {
            Map<String, Double> gend1 = Optional.ofNullable(gender.getCohort1()).orElse(Map.of());
            Map<String, Double> gend2 = Optional.ofNullable(gender.getCohort2()).orElse(Map.of());
            g1.male(gend1.getOrDefault("남성", 0.0).intValue());
            g1.female(gend1.getOrDefault("여성", 0.0).intValue());
            g2.male(gend2.getOrDefault("남성", 0.0).intValue());
            g2.female(gend2.getOrDefault("여성", 0.0).intValue());
        }

        return LibraryCompareResponseDTO.Comparisons.builder()
                .group1(g1.build())
                .group2(g2.build())
                .build();
    }
}