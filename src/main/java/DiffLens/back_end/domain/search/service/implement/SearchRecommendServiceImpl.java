package DiffLens.back_end.domain.search.service.implement;

import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.service.auth.CurrentUserService;
import DiffLens.back_end.domain.search.dto.SearchResponseDTO;
import DiffLens.back_end.domain.search.service.interfaces.RecommendSearchCacheService;
import DiffLens.back_end.domain.search.service.interfaces.SearchRecommendService;
import DiffLens.back_end.domain.search.util.member.SearchMemberUtil;
import DiffLens.back_end.global.fastapi.FastApiService;
import DiffLens.back_end.global.fastapi.dto.request.FastHomeRequestDTO;
import DiffLens.back_end.global.fastapi.dto.response.FastHomeResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchRecommendServiceImpl implements SearchRecommendService {

    private final SearchMemberUtil searchMemberUtil;

    private final FastApiService fastApiService;
    private final CurrentUserService currentUserService;
    private final RecommendSearchCacheService<FastHomeResponseDTO.HomeRecommend> recommendCacheService;

    /**
     *
     * user의 온보딩, 검색기록을 기반으로 추천 검색 정보를 조회합니다.
     * 홈화면에서 조회하는 기능 특성상 다량 호출이 예상되어 불필요한 ai 로직 호출을 줄이기 위해 redis 통한 캐싱 처리를 합니다.
     *
     * redis에 member에 해당하는 추천 캐시 정보가 있다면 캐시에서 데이터를 조회하여 반환하고,
     * 캐시에 없다면 ai 로직을 호출하여 클라이언트에 반환합니다.
     *
     * @return 추천 검색 정보 - SearchResponseDTO.Recommends
     */
    @Override
    public SearchResponseDTO.Recommends getRecommendations() {

        // 0. 현재 로그인한 유저 조회
        Member member = currentUserService.getCurrentUser();

        // 1. 캐시에 존재하면 캐시에서 꺼내서 반환
        FastHomeResponseDTO.HomeRecommend cacheInfo = recommendCacheService.getCacheInfo(member);
        if (cacheInfo != null) { // redis에 값이 있다면 ai 로직을 호출하지 않고 바로 return
            log.info("[API 호출중] 검색 추천 정보를 캐시에서 조회");
            return fastDtoToResponseList(cacheInfo);
        }

        log.info("[API 호출중] 검색 추천 정보를 조회하기 위해 AI 로직 호출");

        // 2. member 기반 요청 데이터 준비
        FastHomeRequestDTO.HomeRecommendRequest fastRequestDTO = searchMemberUtil.makeRequest(member);

        // 3. fast api 요청 후, 응답결과를 저장
        FastHomeResponseDTO.HomeRecommend fastResponse = fastApiService.recommend(fastRequestDTO);

        // 4. 클라이언트에게 보낼 DTO 생성 후
        SearchResponseDTO.Recommends result = fastDtoToResponseList(fastResponse);
        recommendCacheService.saveCacheInfo(member, fastResponse); // 이후에 조회할 때 캐시(Redis)에서 조회하기 위해 캐시에 저장

        // 5. 반환
        return result;
    }

    // Fast api에서 받은 응답 객체를 클라이언트에게 응답보낼 dto로 변환
    // FastHomeResponseDTO.Data -> SearchResponseDTO.Recommends
    private SearchResponseDTO.Recommends fastDtoToResponseList(FastHomeResponseDTO.HomeRecommend recommendationResponse){

        // 응답받은 객체에서 추천 객체만 뽑아냄
        List<FastHomeResponseDTO.Recommendation> recommendations = recommendationResponse.getRecommendations();

        // 추천 객체제를 순회하며 Recommend( 클라이언트 DTO에 담기는 ) 목록 생성
        List<SearchResponseDTO.Recommend> recommendList = recommendations.stream()
                .map(recommendation -> SearchResponseDTO.Recommend.builder()
                        .id(recommendation.getId())
                        .title(recommendation.getQuery())
                        .description(recommendation.getDescription())
                        .build()
                ).toList();

        return SearchResponseDTO.Recommends.builder()
                .recommendations(recommendList)
                .build();
    }

}
