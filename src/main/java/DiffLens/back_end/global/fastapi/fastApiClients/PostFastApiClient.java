package DiffLens.back_end.global.fastapi.fastApiClients;

import DiffLens.back_end.global.fastapi.FastApiRequestType;
import DiffLens.back_end.global.logger.annotations.SubServerExecutionTime;
import DiffLens.back_end.global.responses.code.status.error.ErrorStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostFastApiClient implements FastApiClient {

    private final WebClient fastApiWebClient;

    @Override
    @SubServerExecutionTime("서브서버 호출 소요시간")
    public <T, R> R sendRequest(FastApiRequestType type, T requestBody, Object... ignore) {

        // 요청 타입이 맞지 않을 경우에 대한 예외
        if (!type.getRequestBody().isInstance(requestBody)) {
            throw new IllegalArgumentException("올바르지 않은 요청 타입 " + type.name()); // TODO : 에외처리...
        }

        R block = null;
        try {
            block = fastApiWebClient.post()
                    .uri(type.getUri())
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono((Class<R>) type.getResponseType())
                    .block();
        } catch (Exception e) { // fast api 호출 중 에러 발생시 예외 발생
            log.warn("[{} 호출 중 예외 발생] {} : ", type.getName(), e.getMessage());
            throw new ErrorHandler(ErrorStatus.SUB_SERVER_ERROR);
        }

        return block;
    }
}
