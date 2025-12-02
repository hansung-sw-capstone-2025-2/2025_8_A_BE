package DiffLens.back_end.global.fastapi.fastApiClients;

import DiffLens.back_end.global.fastapi.FastApiRequestType;
import DiffLens.back_end.global.logger.annotations.SubServerExecutionTime;
import DiffLens.back_end.global.responses.code.status.error.ErrorStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

/**
 * Query Parameter에 대한 FastApiService
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QueryParamFastApiClient implements FastApiClient {

    private final WebClient fastApiWebClient;

    @Override
    @SubServerExecutionTime("서브서버 호출 소요시간")
    public <T, R> R sendRequest(FastApiRequestType type, T requestBody, Object... params) {

        Map<String, Object> queryParams = (Map<String, Object>) params[0];

        try {
            return fastApiWebClient.post()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder.path(type.getUri());
                        queryParams.forEach((key, value) -> {
                            if (value != null) {
                                builder.queryParam(key, value);
                            }
                        });
                        return builder.build();
                    })
                    .retrieve()
                    .bodyToMono((Class<R>) type.getResponseType())
                    .block();

        } catch (WebClientResponseException e) {

            StringBuilder sb = new StringBuilder();
            sb.append("\n=== 서브서버 응답 오류 ===\n")
                    .append("URI: ").append(type.getUri()).append("\n")
                    .append("Query Params: ").append(queryParams).append("\n")
                    .append("Status Code: ").append(e.getStatusCode()).append("\n")
                    .append("Response Body: ").append(e.getResponseBodyAsString()).append("\n")
                    .append("Error Message: ").append(e.getMessage()).append("\n");

            log.error(sb.toString(), e);

            throw new ErrorHandler(ErrorStatus.SUB_SERVER_ERROR);

        } catch (Exception e) {

            StringBuilder sb = new StringBuilder();
            sb.append("\n=== 서브서버 요청 오류 ===\n")
                    .append("URI: ").append(type.getUri()).append("\n")
                    .append("Query Params: ").append(queryParams).append("\n")
                    .append("Error Type: ").append(e.getClass().getName()).append("\n")
                    .append("Error Message: ").append(e.getMessage()).append("\n");

            log.error(sb.toString(), e);

            throw new ErrorHandler(ErrorStatus.SUB_SERVER_ERROR);
        }
    }
}
