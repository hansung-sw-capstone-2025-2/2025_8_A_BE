package DiffLens.back_end.domain.search.service.implement;

import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.search.repository.cache.RedisObjectCacheRepository;
import DiffLens.back_end.domain.search.service.interfaces.RecommendSearchCacheService;
import DiffLens.back_end.global.fastapi.dto.response.FastHomeResponseDTO;
import DiffLens.back_end.global.redis.CacheInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendSearchCacheServiceImpl implements RecommendSearchCacheService<FastHomeResponseDTO.HomeRecommend> {

    // 캐시 정보를 관리하는 repository
    private final RedisObjectCacheRepository cacheRepository;

    // 캐싱 정보
    private final CacheInfo cacheInfo = CacheInfo.Search_Recommendation;

    // 저장되어있는 추천정보를 꺼냄
    @Override
    public FastHomeResponseDTO.HomeRecommend getCacheInfo(Member member) {

        // 캐시에서 조회할 키를 생성
        String key = getKey(member);

        // key로 캐시 조회 및 반환. null일 수 있음
        return cacheRepository.findByKey(key);
    }

    // 추천정보를 캐시에 저장함
    @Override
    public void saveCacheInfo(Member member, FastHomeResponseDTO.HomeRecommend recommend) {
        cacheRepository.save(getKey(member), recommend, cacheInfo);
    }

    private String getKey(Member member) {
        return cacheInfo.getPrefix() + member.getId();
    }

}
