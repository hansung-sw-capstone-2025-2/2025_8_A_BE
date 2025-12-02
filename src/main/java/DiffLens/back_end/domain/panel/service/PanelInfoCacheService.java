package DiffLens.back_end.domain.panel.service;

import DiffLens.back_end.domain.panel.entity.Panel;
import DiffLens.back_end.global.redis.CacheService;

/**
 *
 * 패널 정보에 대한 캐시 정보를 다루는 service interface
 *
 * @param <T> 캐시로 다룰 데이터
 */
public class PanelInfoCacheService<T> implements CacheService<T, Panel> {

    @Override
    public T getCacheInfo(Panel key) {
        return null;
    }

    @Override
    public void saveCacheInfo(Panel data, T cacheInfo) {

    }
}
