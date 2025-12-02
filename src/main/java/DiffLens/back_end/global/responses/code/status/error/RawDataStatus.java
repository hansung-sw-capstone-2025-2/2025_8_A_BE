package DiffLens.back_end.global.responses.code.status.error;

import DiffLens.back_end.global.responses.code.BaseErrorCode;
import DiffLens.back_end.global.responses.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RawDataStatus implements BaseErrorCode {

    RAW_DATA_ERROR(HttpStatus.BAD_REQUEST, "RAW400", "원천데이터 오류입니다. 관리자에게 문의하세요"),
    RAW_DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "RAW401", "존재하지 않는 원천데이터입니다."),

    RAW_DATA_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "RAW500", "원천데이터 오류입니다. 관리자에게 문의하세요."),

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
