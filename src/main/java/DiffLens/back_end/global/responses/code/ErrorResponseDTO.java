package DiffLens.back_end.global.responses.code;

import DiffLens.back_end.global.responses.code.status.error.ErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponseDTO extends ErrorReasonDto {

    private ErrorResponseDTO(HttpStatus httpStatus, String code, String message) {
        super(httpStatus, false, code, message);
    }

    public static ErrorResponseDTO of(ErrorStatus errorStatus) {
        return new ErrorResponseDTO(
                errorStatus.getHttpStatus(), errorStatus.getCode(), errorStatus.getMessage());
    }

    public static ErrorResponseDTO of(ErrorStatus errorStatus, String message) {
        return new ErrorResponseDTO(
                errorStatus.getHttpStatus(),
                errorStatus.getCode(),
                errorStatus.getMessage() + " - " + message);
    }

    public static ErrorResponseDTO of(ErrorStatus errorStatus, Exception e) {
        return new ErrorResponseDTO(
                errorStatus.getHttpStatus(), errorStatus.getCode(), errorStatus.getMessage());
    }
}
