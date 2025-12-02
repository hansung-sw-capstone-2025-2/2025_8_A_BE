package DiffLens.back_end.domain.members.controller;

import DiffLens.back_end.domain.members.dto.auth.AuthRequestDTO;
import DiffLens.back_end.domain.members.dto.auth.AuthResponseDTO;
import DiffLens.back_end.domain.members.service.auth.AuthService;
import DiffLens.back_end.global.responses.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup/local")
    @Operation(summary = "회원가입 ( 로컬 )",
                description = """
                            
                            ## 개요
                            일반 회원가입 API입니다.
                            
                            ## Request Body
                            회원가입 정보 담아서 요청
                            
                            ## 응답
                            로그인 성공 여부를 반환합니다.

                            """)
    public ApiResponse<AuthResponseDTO.SignUpDto> signUp(@RequestBody @Valid AuthRequestDTO.SignUp request){
        AuthResponseDTO.SignUpDto signUpDto = authService.signUp(request);
        return ApiResponse.onSuccess(signUpDto);
    }

    @PostMapping("/login/local")
    @Operation(summary = "로그인 ( 로컬 )",
                description = """
                            
                            ## 개요
                            일반 회원가입 API입니다.
                            
                            ## Request Body
                            - 일반 회원가입에 담아 요청한 email과 paassword 를 담아 요청하세요.
                            - loginType : GENERAL
                            
                            ## 응답
                            인증에 필요한 토큰정보를 포함합니다.
                            
                            """)
    public ApiResponse<AuthResponseDTO.LoginDto> localLogin(@RequestBody @Valid AuthRequestDTO.Login request){
        AuthResponseDTO.LoginDto login = authService.login(request);
        return ApiResponse.onSuccess(login);
    }

    @PostMapping("/login/google")
    @Operation(summary = "로그인 ( 구글 )", hidden = true,
                description = """
                            
                            ## 개요
                            구글 로그인 API 입니다.
                            
                            ## Request Body
                            - code : 구글 로그인 진행 후 리다이렉트 되는 주소의 파라미터 중 code값
                            - loginType : GOOGLE
                            
                            ## 응답
                            인증에 필요한 토큰정보를 포함합니다.
                            
                            ## 기타
                            - 구글 로그인 링크는 노션 API 명세서의 구글 로그인 페이지에 첨부해놓았습니다.
                            - !! 추후 해당 API는 기획에 따라 구글 전용이 아닌 소셜로그인 통합으로 변경될 수 있습니다. ( google -> social )

                            
                            """)
    public ApiResponse<AuthResponseDTO.LoginDto> googleLogin(@RequestBody @Valid AuthRequestDTO.SocialLogin request){
        AuthResponseDTO.LoginDto login = authService.login(request);
        return ApiResponse.onSuccess(login);
    }

    @PostMapping("/reissue")
    @Operation(summary = "인증 토큰 재발급",
                description = """
                            
                            ## 개요
                            기존 토큰 만료 시 해당 API를 호출하여 새로운 인증토큰을 발급 받습니다.<br>
                            재발급 시 기존 토큰은 만료됩니다.
                            
                            ## Request Header
                            Authorization 헤더에 refresh token 을 담아 호출하세요.<br>
                            ex) Bearer eyJhbGciOi...re7neDrYl9gJM6c
                            
                            ## 응답
                            AccessToken과 RefreshToken 이 반환됩니다.
                            - 재발급 시 기존 AccessToken은 사용 불가능합니다.
                            - Refresh Token은 계정마다 14일 유효합니다.
                            
                            """)
    public ApiResponse<AuthResponseDTO.LoginDto> reIssue(HttpServletRequest request) {
        AuthResponseDTO.LoginDto loginDto = authService.reIssue(request);
        return ApiResponse.onSuccess(loginDto);
    }


}
