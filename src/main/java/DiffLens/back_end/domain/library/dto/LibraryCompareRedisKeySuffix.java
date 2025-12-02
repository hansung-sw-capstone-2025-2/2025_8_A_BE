package DiffLens.back_end.domain.library.dto;

import DiffLens.back_end.domain.library.entity.Library;

public record LibraryCompareRedisKeySuffix(
        Long lib1Id,
        Long lib2Id
) {

    public static LibraryCompareRedisKeySuffix of(Library lib1, Library lib2) {
        return new LibraryCompareRedisKeySuffix(lib1.getId(), lib2.getId());
    }

    public static LibraryCompareRedisKeySuffix of(Long lib1, Long lib2) {
        return new LibraryCompareRedisKeySuffix(lib1, lib2);
    }

}
