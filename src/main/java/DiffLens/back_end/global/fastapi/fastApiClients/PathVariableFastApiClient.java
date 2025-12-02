package DiffLens.back_end.global.fastapi.fastApiClients;

import DiffLens.back_end.global.fastapi.FastApiRequestType;
import DiffLens.back_end.global.fastapi.dto.FastApiErrorDto;
import DiffLens.back_end.global.logger.annotations.SubServerExecutionTime;
import DiffLens.back_end.global.responses.code.status.error.ErrorStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * path variable에 대한 FastApiClient
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PathVariableFastApiClient implements FastApiClient {

    private final WebClient fastApiWebClient;

    @Override
    @SubServerExecutionTime("서브서버 호출 소요시간")
    public <T, R> R sendRequest(FastApiRequestType type, T requestBody, Object... pathVariables) {
        try {
            return fastApiWebClient.get()
                    .uri(type.getUri(), pathVariables)
                    .exchangeToMono(response -> {
                        HttpStatusCode status = response.statusCode();

                        if (status.is4xxClientError() || status.is5xxServerError()) {
                            return response.bodyToMono(FastApiErrorDto.class)
                                    .flatMap(errorDto -> {
                                        String detail = errorDto.getDetail();

                                        log.warn("🐞 [서브서버 호출 예외 발생] errorCode : {}, detail : {}", status.value(), detail);

                                        return Mono.just(null);
                                    });
                        }

                        // 정상 응답
                        return response.bodyToMono((Class<R>) type.getResponseType());
                    })
                    .block();

        } catch (ErrorHandler e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorHandler(ErrorStatus.SUB_SERVER_ERROR);
        }
    }
}
