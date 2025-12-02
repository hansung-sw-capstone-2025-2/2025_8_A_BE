package DiffLens.back_end.domain.library.service.analysis;

import DiffLens.back_end.domain.library.dto.LibraryCompareRedisKeySuffix;
import DiffLens.back_end.domain.library.dto.LibraryCompareRequestDTO;
import DiffLens.back_end.domain.library.dto.LibraryCompareResponseDTO;
import DiffLens.back_end.domain.library.dto.LibraryResponseDTO;
import DiffLens.back_end.domain.library.entity.Library;
import DiffLens.back_end.domain.library.repository.LibraryRepository;
import DiffLens.back_end.domain.library.service.cache.LibraryCompareCacheService;
import DiffLens.back_end.domain.library.utils.LibraryConvertUtils;
import DiffLens.back_end.domain.library.utils.LibraryValidation;
import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.service.auth.CurrentUserService;
import DiffLens.back_end.global.fastapi.FastApiService;
import DiffLens.back_end.global.fastapi.dto.request.FastLibraryChartRequestDTO;
import DiffLens.back_end.global.fastapi.dto.response.FastLibraryChartResponseDTO;
import DiffLens.back_end.global.fastapi.dto.response.FastLibraryCompareResponseDTO;
import DiffLens.back_end.global.responses.code.status.error.ErrorStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryAnalysisServiceImpl implements LibraryAnalysisService {

    private final LibraryRepository libraryRepository;
    private final FastApiService fastApiService;
    private final LibraryConvertUtils convertUtils;
    private final LibraryCompareCacheService<LibraryCompareResponseDTO.CompareResult> compareCacheService;
    private final CurrentUserService currentUserService;

    @Transactional(readOnly = true)
    public LibraryCompareResponseDTO.CompareResult compareLibraries(LibraryCompareRequestDTO.Compare request) {

        Member member = currentUserService.getCurrentUser();

        // 1. 두 라이브러리의 id가 같으면 예외 발생
        if (request.getLibraryId1().equals(request.getLibraryId2()))
            throw new ErrorHandler(ErrorStatus.BAD_REQUEST);

        // 1-1. 라이브러리 객체 2개 생성
        Library lib1 = libraryRepository.findById(request.getLibraryId1()).orElseThrow(() -> new ErrorHandler(ErrorStatus.BAD_REQUEST));
        Library lib2 = libraryRepository.findById(request.getLibraryId2()).orElseThrow(() -> new ErrorHandler(ErrorStatus.BAD_REQUEST));

        // 2. 캐시 조회
        // 2-1. redis Key에 쓰이는 정보 생성
        LibraryCompareRedisKeySuffix suffix = LibraryCompareRedisKeySuffix.of(request.getLibraryId1(), request.getLibraryId2());

        // 2-2. redis에서 조회
        LibraryCompareResponseDTO.CompareResult cacheInfo = compareCacheService.getCacheInfo(suffix);

        // 2-3. redis에 데이터가 있으면 조회한거를 반환
        if( cacheInfo != null ){
            log.info("[API 호출중] 라이브러리 비교 정보를 정보를 캐시에서 조회");
            return cacheInfo;
        }

        log.info("[API 호출중] 검색 추천 정보를 조회하기 위해 AI 로직 호출");

        // 3. 조회된 라이브러리가 내꺼가 아니면 예외 발생
        if (!lib1.getMember().getId().equals(member.getId()) || !lib2.getMember().getId().equals(member.getId()))
            throw new ErrorHandler(ErrorStatus.FORBIDDEN);

        // 4. 서브서버 요청해서
        var apiResp = fastApiService.compareLibraries(lib1.getId(), lib2.getId());
        LibraryCompareResponseDTO.CompareResult compareResult = makeCompareResult(apiResp, lib1, lib2);

        // 5. 캐시 저장
        compareCacheService.saveCacheInfo(suffix, compareResult);

        return compareResult;
    }

    /**
     * 라이브러리 대시보드 조회 (차트 포함)
     */
    @Transactional(readOnly = true)
    public LibraryResponseDTO.LibraryDashboard getLibraryDashboard(Long libraryId) {

        Member member = currentUserService.getCurrentUser();

        // 1. 라이브러리 조회 및 권한 검증
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BAD_REQUEST));

        // 1-1. 라이브러리가 나의 소유인지 확인. 아니라면 예외처리
        LibraryValidation.checkIsMyLibrary(library, member);

        // 2. 패널 ID 배열 조회
        List<String> panelIds = library.getPanelIds();
        if (panelIds == null || panelIds.isEmpty()) {
            throw new ErrorHandler(ErrorStatus.BAD_REQUEST);
        }

        // 3. 서브서버 API 호출
        FastLibraryChartRequestDTO request = FastLibraryChartRequestDTO.builder()
                .panelIds(panelIds)
                .libraryName(library.getLibraryName())
                .build();
        FastLibraryChartResponseDTO.LibraryChartResponse chartResponse = fastApiService.getChartsFromLibrary(request);

        // 4. 차트 데이터 변환
        LibraryResponseDTO.LibraryDashboard.ChartData mainChart = convertUtils.convertToChartData(chartResponse.getMainChart());
        List<LibraryResponseDTO.LibraryDashboard.ChartData> subCharts = chartResponse.getSubCharts()
                .stream()
                .map(convertUtils::convertToChartData)
                .toList();

        // 5. 응답 구성
        return LibraryResponseDTO.LibraryDashboard.builder()
                .libraryId(library.getId())
                .libraryName(library.getLibraryName())
                .panelCount(panelIds.size())
                .mainChart(mainChart)
                .subCharts(subCharts)
                .build();

    }

    private LibraryCompareResponseDTO.CompareResult makeCompareResult(FastLibraryCompareResponseDTO.CompareResult apiResp, Library lib1,  Library lib2) {
        return LibraryCompareResponseDTO.CompareResult.builder()
                .group1(convertUtils.convertGroupInfo(apiResp.getCohort1(), lib1))
                .group2(convertUtils.convertGroupInfo(apiResp.getCohort2(), lib2))
                .keyCharacteristics(convertUtils.convertCharacteristics(apiResp.getCharacteristics()))
                .comparisons(convertUtils.convertBasicInfoComparisons(
                        apiResp.getBasicInfo(),
                        apiResp.getRegionDistribution(),
                        apiResp.getGenderDistribution()))
                .insights(convertUtils.convertKeyInsights(apiResp.getKeyInsights()))
                .build();
    }


}
