package DiffLens.back_end.domain.library.controller;

import DiffLens.back_end.domain.library.dto.LibraryCreateResult;
import DiffLens.back_end.domain.library.dto.LibraryRequestDto;
import DiffLens.back_end.domain.library.dto.LibraryResponseDTO;
import DiffLens.back_end.domain.library.dto.LibraryCompareRequestDTO;
import DiffLens.back_end.domain.library.dto.LibraryCompareResponseDTO;
import DiffLens.back_end.domain.library.service.analysis.LibraryAnalysisService;
import DiffLens.back_end.domain.library.service.command.LibraryCommandService;
import DiffLens.back_end.domain.library.service.query.LibraryQueryService;
import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.service.auth.CurrentUserService;
import DiffLens.back_end.global.responses.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "라이브러리 API")
@RestController
@RequestMapping("/libraries")
@RequiredArgsConstructor
public class LibraryController {

        private final LibraryAnalysisService libraryAnalysisService;
        private final LibraryQueryService libraryQueryService;
        private final LibraryCommandService libraryCommandService;

        @GetMapping
        @Operation(summary = "라이브러리 목록 조회", description = """

                        ## 개요
                        인증된 사용자가 생성한 라이브러리 목록을 조회하는 API입니다.

                        ## 응답
                        - libraries : 라이브러리 목록 배열
                          - library_id : 라이브러리 ID
                          - library_name : 라이브러리 이름
                          - tags : 라이브러리 분류 태그 리스트
                          - panel_count : 저장된 패널 개수
                          - created_at : 생성 시간
                        - page_info : 페이징 정보 (현재는 null, 추후 구현 예정)

                        ## 권한
                        본인이 생성한 라이브러리만 조회할 수 있습니다.

                        ## 정렬
                        최근 생성된 순서로 정렬됩니다.

                        """)
        public ApiResponse<LibraryResponseDTO.ListResult> libraryList() {
                LibraryResponseDTO.ListResult result = libraryQueryService.getLibrariesByMember();
                return ApiResponse.onSuccess(result);
        }

        @PostMapping
        @Operation(summary = "라이브러리 생성", description = """

                        ## 개요
                        검색 기록을 기반으로 라이브러리를 생성하는 API입니다.

                        ## Request Body
                        - search_history_id : 저장할 검색 기록의 ID (필수)
                        - library_name : 라이브러리 이름 (필수)
                        - tags : 라이브러리 분류 태그 리스트 (필수)
                        - panel_ids : 저장할 패널 ID 리스트 (선택, null이면 검색 기록의 모든 패널 저장)

                        ## 응답
                        - library_id : 생성된 라이브러리 ID
                        - library_name : 라이브러리 이름
                        - search_history_id : 연결된 검색 기록 ID
                        - panel_count : 저장된 패널 개수
                        - created_at : 생성 시간

                        ## 권한
                        본인의 검색 기록만 라이브러리로 저장할 수 있습니다.

                        """)
        public ApiResponse<LibraryResponseDTO.CreateResult> createLibrary(
                        @RequestBody @Valid LibraryRequestDto.Create request) {
                LibraryCreateResult createResult = libraryCommandService.createLibrary(request);

                LibraryResponseDTO.CreateResult result = LibraryResponseDTO.CreateResult.from(
                                createResult.library(),
                                request.getSearchHistoryId(),
                                createResult.panelCount());
                return ApiResponse.onSuccess(result);
        }

        @GetMapping("/{libraryId}")
        @Operation(summary = "특정 라이브러리 상세 조회", description = """
                        ## 개요
                        특정 라이브러리의 상세 정보를 조회하는 API입니다.

                        ## Path Parameters
                        - libraryId : 조회할 라이브러리 ID

                        ## 응답 데이터
                        - 라이브러리 기본 정보 (이름, 태그, 생성일, 수정일)
                        - 포함된 패널들의 상세 정보
                        - 연결된 검색기록들 -> 혹시 몰라 추가한 로직으로 필요 없다고 판단될 시 안쓰셔도 됩니다.
                        - 패널 통계 정보 (성별, 연령대, 거주지 분포) -> 혹시 몰라 추가한 로직으로 필요 없다고 판단될 시 안쓰셔도 됩니다.

                        ## 권한
                        본인이 생성한 라이브러리만 조회할 수 있습니다.
                        """)
        public ApiResponse<LibraryResponseDTO.LibraryDetail> getLibraryDetail(@PathVariable Long libraryId) {
                LibraryResponseDTO.LibraryDetail result = libraryQueryService.getLibraryDetail(libraryId);
                return ApiResponse.onSuccess(result);
        }

        @PutMapping("/{libraryId}/search-histories/{searchHistoryId}")
        @Operation(summary = "기존 라이브러리에 새로운 검색기록 추가(병합)", description = """

                        ## 개요
                        기존 라이브러리에 검색기록을 추가하는 API입니다.
                        라이브러리의 패널 ID와 검색기록의 패널 ID를 병합하여 중복을 제거합니다.

                        ## Path Parameters
                        - libraryId : 라이브러리 ID
                        - searchHistoryId : 추가할 검색기록 ID

                        ## 응답
                        - library_id : 라이브러리 ID
                        - library_name : 라이브러리 이름
                        - search_history_id : 추가된 검색기록 ID
                        - panel_count : 새로 추가된 패널 개수
                        - panel_ids : 병합된 전체 패널 ID 리스트
                        - created_at : 생성 시간

                        ## 권한
                        본인의 라이브러리와 검색기록만 사용할 수 있습니다.

                        ## 예시
                        - 라이브러리 1번에 패널 ID [1, 2, 3]이 있음
                        - 검색기록 2번에 패널 ID [3, 4, 5]가 있음
                        - 결과: 라이브러리 1번에 패널 ID [1, 2, 3, 4, 5]가 됨

                        """)
        public ApiResponse<LibraryResponseDTO.CreateResult> addSearchHistoryToLibrary(
                        @PathVariable Long libraryId,
                        @PathVariable Long searchHistoryId) {
                LibraryCreateResult createResult = libraryCommandService.addSearchHistoryToLibrary(libraryId, searchHistoryId);

                LibraryResponseDTO.CreateResult result = LibraryResponseDTO.CreateResult.from(
                                createResult.library(),
                                searchHistoryId,
                                createResult.panelCount()
                );
                return ApiResponse.onSuccess(result);
        }

        @PostMapping("/compare")
        @Operation(summary = "라이브러리 비교", description = """
                        ## 개요
                        두 개의 라이브러리를 비교하여 특성, 차트, 기본 통계를 제공하는 API입니다.

                        ## Request Body
                        - libraryId1 : 첫 번째 라이브러리 ID (필수)
                        - libraryId2 : 두 번째 라이브러리 ID (필수)

                        ## 응답 데이터
                        - group1, group2 : 각 라이브러리 기본 정보
                        - keyCharacteristics : 주요 특성 비교 (차이점 포함)
                        - comparisonCharts : 비교 차트 데이터 (Chart.js 형식)
                        - comparisons : 기본 통계 비교

                        ## 권한
                        본인이 생성한 라이브러리만 비교할 수 있습니다.

                        ## 제약사항
                        - 동일한 라이브러리는 비교할 수 없습니다.
                        - 두 라이브러리 모두 존재해야 합니다.

                        """)
        public ApiResponse<LibraryCompareResponseDTO.CompareResult> compareLibraries(
                        @RequestBody @Valid LibraryCompareRequestDTO.Compare request) {
                LibraryCompareResponseDTO.CompareResult result = libraryAnalysisService.compareLibraries(request);
                return ApiResponse.onSuccess(result);
        }

        @GetMapping("/{libraryId}/dashboard")
        @Operation(summary = "라이브러리 대시보드 조회", description = """
                        ## 개요
                        라이브러리 ID를 입력받아 해당 라이브러리의 패널 배열을 조회하고, 서브서버 API를 호출하여 차트 데이터를 반환합니다.

                        ## 응답
                        - library_id: 라이브러리 ID
                        - library_name: 라이브러리 이름
                        - panel_count: 패널 개수
                        - main_chart: 메인 차트 데이터 (amCharts 형식)
                        - sub_charts: 서브 차트 데이터 배열 (최대 2개, amCharts 형식)

                        ## 차트 구조
                        - chart_type: 차트 타입 (pie, donut, column, bar, map, stacked-bar, infographic 등)
                        - metric: 차트를 생성한 메트릭
                        - title: 차트 제목
                        - reasoning: 차트 선택 이유 (메인 차트에만 제공)
                        - data: 차트 데이터 포인트 배열

                        ## 권한
                        본인이 생성한 라이브러리만 조회할 수 있습니다.
                        """)
        public ApiResponse<LibraryResponseDTO.LibraryDashboard> getLibraryDashboard(
                        @PathVariable("libraryId") Long libraryId) {
                LibraryResponseDTO.LibraryDashboard result = libraryAnalysisService.getLibraryDashboard(libraryId);
                return ApiResponse.onSuccess(result);
        }

        @GetMapping("/{libraryId}/panels")
        @Operation(summary = "라이브러리 패널 목록 조회 (페이징)", description = """
                        ## 개요
                        라이브러리 ID와 페이징 정보를 입력받아 해당 라이브러리의 패널 목록을 조회합니다.

                        ## 쿼리 파라미터
                        - page: 페이지 번호 (1부터 시작, 기본값: 1)
                        - size: 페이지 크기 (기본값: 20)

                        ## 응답
                        - keys: 컬럼명 배열 (respondent_id, gender, age, residence, personal_income)
                        - values: 패널 데이터 배열
                        - page_info: 페이징 정보

                        ## 주의사항
                        - 일치율(concordance_rate)은 반환하지 않습니다.
                        - 검색 API와 달리 유사도 정렬을 하지 않습니다.

                        ## 권한
                        본인이 생성한 라이브러리만 조회할 수 있습니다.
                        """)
        public ApiResponse<LibraryResponseDTO.LibraryPanels> getLibraryPanels(
                        @PathVariable("libraryId") Long libraryId,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "20") Integer size) {
                LibraryResponseDTO.LibraryPanels result = libraryQueryService.getLibraryPanels(libraryId, page, size);
                return ApiResponse.onSuccess(result);
        }
}
