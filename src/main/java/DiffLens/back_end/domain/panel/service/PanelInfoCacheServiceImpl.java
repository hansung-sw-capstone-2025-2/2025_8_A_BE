package DiffLens.back_end.domain.panel.service;

import DiffLens.back_end.domain.panel.entity.Panel;
import DiffLens.back_end.domain.search.repository.cache.RedisObjectCacheRepository;
import lombok.RequiredArgsConstructor;

/**
 * 패널 정보 캐싱 전략
 *
 * - 패널 정보는 유저 당 캐싱하지 않고, 서비스 전반적으로 저장합니다.
 *
 */
@RequiredArgsConstructor
public class PanelInfoCacheServiceImpl extends PanelInfoCacheService<Panel>{

    private static final Integer TTL = 10;

    // 캐시 정보를 관리하는 repository
    private final RedisObjectCacheRepository cacheRepository;

    // 캐시 Key 접두사
    private static final String CACHE_KEY_PREFIX = "search:recommend:"; // search:recommend:{memberId} 형식으로 key 지정할 예정

    @Override
    public Panel getCacheInfo(Panel key) {

        return null;

    }

    @Override
    public void saveCacheInfo(Panel data, Panel cacheInfo) {

    }



}
