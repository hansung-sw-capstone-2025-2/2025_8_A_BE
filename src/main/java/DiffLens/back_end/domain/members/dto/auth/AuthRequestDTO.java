package DiffLens.back_end.domain.members.dto.auth;

import DiffLens.back_end.domain.members.enums.Industry;
import DiffLens.back_end.domain.members.enums.Job;
import DiffLens.back_end.domain.members.enums.LoginType;
import DiffLens.back_end.domain.members.enums.PlanEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {

        // 로그인 정보
        @Schema(description = "email 형식으로 입력해야 합니다.")
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private String email;

        @Schema(description = "이름. 최대 20글자")
        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        @Size(max = 20, message = "이름은 20자 이하로 작성해주세요.")
        private String name;

        @Schema(description = "비밀번호. 8~20 글자")
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
        private String password;

        @Schema(description = "로그인 유형")
        @NotNull(message = "로그인 타입은 필수 항목입니다.")
        private LoginType loginType;

        // 플랜
        @Schema(description = "플랜 유형")
        @NotNull(message = "플랜 유형은 필수 항목입니다.")
        private PlanEnum plan;

        // 온보딩
        @Schema(description = "직무")
        @NotNull(message = "직무는 필수 항목입니다.")
        private Job job;

        @Schema(description = "엄종")
        @NotNull(message = "엄종은 필수 항목입니다.")
        private Industry industry;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login implements LoginRequest {

        @Schema(description = "이메일", example = "example@gmail.com")
        @NotBlank
        @Email
        private String email;

        @Schema(description = "비밀번호", example = "password123")
        @NotBlank
        @Size(min = 8, max = 20)
        private String password;

        @Schema(description = "로그인 타입", example = "GENERAL")
        @NotNull
        private LoginType loginType;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SocialLogin implements LoginRequest {

        @NotBlank(message = "code 는 필수 입력 항목입니다.")
        private String code;

        @NotNull(message = "로그인 타입은 필수 항목입니다.")
        private LoginType loginType;

        @NotNull(message = "플랜 유형은 필수 항목입니다.")
        private PlanEnum plan;

    }

    public interface LoginRequest {
        LoginType getLoginType();
    }

}
