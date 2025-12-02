package DiffLens.back_end.global.responses.code.status.error;

import DiffLens.back_end.global.responses.code.BaseErrorCode;
import DiffLens.back_end.global.responses.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthStatus implements BaseErrorCode {

    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH400", "인증에 실패했습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH401", "존재하지 않는 사용자입니다."),
    ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "AUTH402", "이미 존재하는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH403", "올바르지 않은 비밀번호입니다."),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH404", "인증 헤더가 존재하지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "AUTH405", "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH406", "유효하지 않은 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH407", "올바르지 않은 refresh token 입니다."),
    NOT_SUPPLYING(HttpStatus.BAD_REQUEST, "AUTH408", "제공하지 않는 기능입니다."),

    ERROR_IN_SIGNUP(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH500", "회원가입 중 알 수 없는 오류가 발생했습니다."),

    SOCIAL_TOKEN_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH501", "소셜 로그인 중 인증 코드 교환에 실패했습니다."),
    SOCIAL_TOKEN_NETWORK_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "AUTH502", "소셜 로그인 서버에 연결할 수 없습니다."),
    SOCIAL_TOKEN_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH503", "소셜 로그인 처리 중 오류가 발생했습니다."),

    SOCIAL_USERINFO_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH504", "소셜 사용자 정보를 가져오지 못했습니다."),
    SOCIAL_USERINFO_NETWORK_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "AUTH505", "소셜 사용자 정보 요청 중 네트워크 오류가 발생했습니다."),
    SOCIAL_USERINFO_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH506", "소셜 사용자 정보 처리 중 오류가 발생했습니다."),

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
