package DiffLens.back_end.global.responses.code;

public interface BaseErrorCode {

    String getMessage();

    ErrorReasonDto getReason();

    ErrorReasonDto getReasonHttpStatus();
}
