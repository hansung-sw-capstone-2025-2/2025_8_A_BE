package DiffLens.back_end.domain.library.service.cache;

import DiffLens.back_end.domain.library.dto.LibraryCompareRedisKeySuffix;
import DiffLens.back_end.domain.library.dto.LibraryCompareResponseDTO;
import DiffLens.back_end.domain.search.repository.cache.RedisObjectCacheRepository;
import DiffLens.back_end.global.redis.CacheInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryCompareCacheServiceImpl<T> implements LibraryCompareCacheService<LibraryCompareResponseDTO.CompareResult> {

    // 캐시 정보를 관리하는 repository
    private final RedisObjectCacheRepository cacheRepository;

    // 캐싱 정보
    private final CacheInfo cacheInfo = CacheInfo.Library_Compare;

    @Override
    public LibraryCompareResponseDTO.CompareResult getCacheInfo(LibraryCompareRedisKeySuffix key) {
        return cacheRepository.findByKey(getKey(key));
    }

    @Override
    public void saveCacheInfo(LibraryCompareRedisKeySuffix key, LibraryCompareResponseDTO.CompareResult compareData) {

        String redisKey = getKey(key);
        cacheRepository.save(redisKey, compareData, cacheInfo);

    }

    private String getKey(LibraryCompareRedisKeySuffix suffix){

        Long lib1Id = suffix.lib1Id();
        Long lib2Id = suffix.lib2Id();

        // 18-17 -> X
        // 17-18 -> O
        return cacheInfo.getPrefix() + Math.min(lib1Id, lib2Id) + "-" + Math.max(lib1Id, lib2Id);
    }

}
