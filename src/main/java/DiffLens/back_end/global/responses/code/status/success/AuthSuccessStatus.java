package DiffLens.back_end.global.responses.code.status.success;

import DiffLens.back_end.global.responses.code.BaseCode;
import DiffLens.back_end.global.responses.code.ReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthSuccessStatus implements BaseCode {

    LOGIN_SUCCESS(HttpStatus.OK, "AUTH200", "로그인 성공입니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "AUTH205", "로그아웃 성공입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder().message(message).code(code).isSuccess(true).build();
    }

    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }

}
