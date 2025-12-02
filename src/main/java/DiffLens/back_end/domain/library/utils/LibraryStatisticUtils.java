package DiffLens.back_end.domain.library.utils;

import DiffLens.back_end.domain.library.dto.LibraryResponseDTO;
import DiffLens.back_end.domain.panel.entity.Panel;

import java.util.List;

public class LibraryStatisticUtils {

    // 추후 혹시 라이브러리 상세 페이지에서 간단한 통계 디자인 생길 것 대비해 작성해둠
    public static LibraryResponseDTO.LibraryDetail.Statistics createStatistics(List<Panel> panels) {
        // 성별 분포
        long maleCount = panels.stream()
                .filter(p -> p.getGender() != null && p.getGender().toString().equals("MALE")).count();
        long femaleCount = panels.stream()
                .filter(p -> p.getGender() != null && p.getGender().toString().equals("FEMALE"))
                .count();
        long noneCount = panels.stream()
                .filter(p -> p.getGender() != null && p.getGender().toString().equals("NONE")).count();

        LibraryResponseDTO.LibraryDetail.Statistics.GenderDistribution genderDistribution = LibraryResponseDTO.LibraryDetail.Statistics.GenderDistribution
                .builder()
                .male((int) maleCount)
                .female((int) femaleCount)
                .none((int) noneCount)
                .build();

        // 연령대 분포
        long twenties = panels.stream().filter(p -> "20대".equals(p.getAgeGroup())).count();
        long thirties = panels.stream().filter(p -> "30대".equals(p.getAgeGroup())).count();
        long forties = panels.stream().filter(p -> "40대".equals(p.getAgeGroup())).count();
        long fifties = panels.stream().filter(p -> "50대".equals(p.getAgeGroup())).count();
        long sixtiesPlus = panels.stream().filter(p -> p.getAgeGroup() != null &&
                        (p.getAgeGroup().contains("60") || p.getAgeGroup().contains("70")
                                || p.getAgeGroup().contains("80")))
                .count();

        LibraryResponseDTO.LibraryDetail.Statistics.AgeGroupDistribution ageGroupDistribution = LibraryResponseDTO.LibraryDetail.Statistics.AgeGroupDistribution
                .builder()
                .twenties((int) twenties)
                .thirties((int) thirties)
                .forties((int) forties)
                .fifties((int) fifties)
                .sixtiesPlus((int) sixtiesPlus)
                .build();

        // 거주지 분포
        long seoul = panels.stream().filter(p -> p.getRegion() != null && p.getRegion().contains("서울"))
                .count();
        long gyeonggi = panels.stream().filter(p -> p.getRegion() != null && p.getRegion().contains("경기"))
                .count();
        long busan = panels.stream().filter(p -> p.getRegion() != null && p.getRegion().contains("부산"))
                .count();
        long other = panels.size() - seoul - gyeonggi - busan;

        LibraryResponseDTO.LibraryDetail.Statistics.ResidenceDistribution residenceDistribution = LibraryResponseDTO.LibraryDetail.Statistics.ResidenceDistribution
                .builder()
                .seoul((int) seoul)
                .gyeonggi((int) gyeonggi)
                .busan((int) busan)
                .other((int) other)
                .build();

        return LibraryResponseDTO.LibraryDetail.Statistics.builder()
                .totalPanels(panels.size())
                .genderDistribution(genderDistribution)
                .ageGroupDistribution(ageGroupDistribution)
                .residenceDistribution(residenceDistribution)
                .build();
    }


}
