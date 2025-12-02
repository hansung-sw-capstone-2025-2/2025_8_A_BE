package DiffLens.back_end.global.responses.code.status.error;

import DiffLens.back_end.global.responses.code.BaseErrorCode;
import DiffLens.back_end.global.responses.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SearchStatus implements BaseErrorCode {

    SEARCH_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "SEARCH404", "검색기록을 찾을 수 없습니다."),

    PANEL_NOT_FOUND(HttpStatus.NOT_FOUND, "SEARCH405", "패널을 찾을 수 없습니다."),

    RECOMMENDED_SEARCH_NOT_FOUND(HttpStatus.NOT_FOUND, "SEARCH406", "잘못된 recommendId 입니다."),
    NO_RESULT(HttpStatus.NOT_FOUND, "SEARCH407", "검색 결과가 없습니다."),
    RECOMMENDED_EXPIRED(HttpStatus.BAD_REQUEST, "SEARCH407", "만료된 추천 검색어입니다."),

    INVALID_COLUMN(HttpStatus.INTERNAL_SERVER_ERROR, "SEARCH501", "Panel의 column을 찾을 수 없습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder().message(message).code(code).isSuccess(false).build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
