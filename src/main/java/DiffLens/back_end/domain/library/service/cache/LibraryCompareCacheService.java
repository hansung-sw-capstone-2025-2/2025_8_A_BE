package DiffLens.back_end.domain.library.service.cache;

import DiffLens.back_end.domain.library.dto.LibraryCompareRedisKeySuffix;
import DiffLens.back_end.global.redis.CacheService;

public interface LibraryCompareCacheService<T> extends CacheService<T, LibraryCompareRedisKeySuffix> {
}
