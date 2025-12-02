package DiffLens.back_end.global.util.data.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Initializer를 구현하는 모든 Bean을 찾아 List에 넣은 후
 * 각각 initialize() 메서드 실행 -> 기본 데이터 초기화
 * 서버 시작 시기에 초기화할 데이터가 있다면 Initializer 를 구현하면 됩니다.
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServerDataInitializer {

    private final List<Initializer> initializers;

    @PostConstruct
    @Transactional
    public void initialize() {
        log.info("[서버 초기 데이터 저장] 시작");
        for (Initializer initializer : initializers) {
            initializer.initialize();
        }
        log.info("[서버 초기 데이터 저장] 종료");
    }
}
