package DiffLens.back_end.domain.search.util.member;

import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.entity.Onboarding;
import DiffLens.back_end.domain.members.enums.Industry;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import DiffLens.back_end.domain.search.repository.SearchHistoryRepository;
import DiffLens.back_end.global.fastapi.dto.request.FastHomeRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchMemberUtil {

    private static final int LIMIT = 6;
    private static final int HISTORY_LIMIT = 50;

    private final SearchHistoryRepository searchHistoryRepository;


    /**
     *
     * 추천 검색에 필요한 요청 DTO 를 생성하여 반환합니다.
     *
     * 담긴 정보
     * - 검색기록
     * - 직업군
     * - 제한
     *
     *
     * @param member 현재 로그인한 유저
     * @return Fast API에 요청할 DTO 객체
     */
    public FastHomeRequestDTO.HomeRecommendRequest makeRequest(Member member){

        // 1. 온보딩 정보 조회 ( 직업군 )
        Industry industry = getIndustry(member);

        // 2. 검색기록 조회
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findByMember(member);
        List<String> searchContentList = searchHistoryList.stream()
                .map(SearchHistory::getContent)
                .toList();

        return FastHomeRequestDTO.HomeRecommendRequest.builder()
                .recentSearches(searchContentList)
                .limit(LIMIT)
                .industry(industry.getKrValue())
                .build();
    }

    /**
     *
     * 유저 정보를 조회해서 추천 검색어 조회
     *
     * 담긴 정보
     * - 유저 ID
     * - 검색기록 제한
     * - 결과 제한
     * - 직업군
     *
     * @param member 현래 로그인한 유저
     * @return Fast API에 요청할 DTO
     */
    public FastHomeRequestDTO.HomeRecommendByMemberRequest makeRequestByMember(Member member){

        return FastHomeRequestDTO.HomeRecommendByMemberRequest.builder()
                .memberId(member.getId())
                .limit(LIMIT)
                .historyLimit(HISTORY_LIMIT)
                .industry(getIndustry(member).getKrValue())
                .build();

    }

    private static Industry getIndustry(Member member) {
        // 1. 온보딩 정보 조회
        Onboarding onboarding = member.getOnboarding();
        Industry industry = onboarding.getIndustry();
        return industry;
    }


}
