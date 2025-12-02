package DiffLens.back_end.global.responses.code.status.error;

import DiffLens.back_end.global.responses.code.BaseErrorCode;
import DiffLens.back_end.global.responses.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON404", "입력값이 유효하지 않습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON405", "code 누락 또는 공백 등 잘못된 파라미터입니다."),
    PAGE_NO_INVALID(HttpStatus.BAD_REQUEST, "COMMON406", "페이지 번호는 1 이상 이어야 합니다."),
    PAGE_NO_EXCEED(HttpStatus.BAD_REQUEST, "COMMON407", "최대 페이지를 벗어났습니다."),

    SUB_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON502", "서브서버 요청 중 오류가 발생했습니다.")

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
