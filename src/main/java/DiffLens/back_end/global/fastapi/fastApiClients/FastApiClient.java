package DiffLens.back_end.global.fastapi.fastApiClients;

import DiffLens.back_end.global.fastapi.FastApiRequestType;

public interface FastApiClient {

    /**
     * FastAPI 서버와의 모든 종류의 요청을 하나의 메서드로 처리하기 위한 통합 인터페이스입니다.
     *
     * 이 메서드는 기존 FastApiClient에서 분리되어 있던 다음 세 가지 요청 방식을 모두 지원합니다:
     *
     * 1) POST 요청 + RequestBody 전송
     *    - 일반적인 FastAPI POST/PUT 요청을 처리합니다.
     *    - JSON Body를 전송하고, 지정된 타입의 응답을 반환받습니다.
     *
     * 2) GET 요청 + PathVariable 전달
     *    - 경로에 값을 포함하여 요청하는 형태를 처리합니다.
     *    - 예: /api/item/{id}
     *
     * 3) GET 요청 + Query Parameter 전달
     *    - key=value 형태의 쿼리 파라미터를 전달하는 GET 요청을 처리합니다.
     *    - 예: /api/items?keyword=abc&page=3
     *
     * 위 세 가지 기능을 하나의 sendRequest 메서드로 통합하여,
     * 요청 타입(FastApiRequestType)에 따라 적절한 방식으로 FastAPI 서버와 통신합니다.
     *
     * @param type FastAPI 요청 메타데이터 (URI, HTTP Method, 응답 타입 등)
     * @param requestBody POST/PUT 요청 시 전송할 Body (GET 계열에서는 무시)
     * @param pathVariables PathVariable 또는 QueryParam 등 요청 시 필요한 추가 매개변수
     * @param <T> 요청 Body 타입
     * @param <R> 응답 Body 타입
     * @return FastAPI 서버로부터 받은 응답 데이터
     */
    <T, R> R sendRequest(
            FastApiRequestType type,
            T requestBody,
            Object... pathVariables
    );

}
