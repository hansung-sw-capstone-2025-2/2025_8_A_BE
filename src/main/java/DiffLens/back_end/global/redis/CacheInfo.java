package DiffLens.back_end.global.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Getter
public enum CacheInfo {

    Search_Recommendation("search:recommend:", 10, TimeUnit.MINUTES),

    Library_Compare("library:compare:", 20, TimeUnit.MINUTES),
    ;

    private String prefix;
    private int TTL;
    private TimeUnit timeUnit;

}
