package DiffLens.back_end.domain.members.service.auth;

import DiffLens.back_end.domain.members.auth.StrategyFactory;
import DiffLens.back_end.domain.members.auth.strategy.interfaces.AuthStrategy;
import DiffLens.back_end.domain.members.dto.auth.AuthRequestDTO;
import DiffLens.back_end.domain.members.dto.auth.AuthResponseDTO;
import DiffLens.back_end.domain.members.dto.auth.TokenResponseDTO;
import DiffLens.back_end.domain.members.enums.LoginType;
import DiffLens.back_end.global.responses.code.status.error.AuthStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import DiffLens.back_end.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그인과 회원가입 처리
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final StrategyFactory strategyFactory;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponseDTO.SignUpDto signUp(AuthRequestDTO.SignUp request) {

        // 요청 정보에서 loginType 추출
        LoginType loginType = request.getLoginType();

        // 타입에 맞는 전략 클래스 선택
        AuthStrategy strategy = strategyFactory.getStrategy(loginType);

        // 전략 클래스의 signUp() 메서드 호출하여 회원가입 진행
        // 회원가입 성공 유무 반환
        Boolean isSuccess = strategy.signUp(request);

        // DTO 생성
        return AuthResponseDTO.SignUpDto.builder()
                .isSuccess(isSuccess)
                .build();
    }

    @Transactional
    public <T extends AuthRequestDTO.LoginRequest> AuthResponseDTO.LoginDto login(T request) {

        // 패턴 가져옴
        AuthStrategy strategy = strategyFactory.getStrategy(request.getLoginType());

        // 로그인 로직 처리
        TokenResponseDTO tokenDto = strategy.login(request);

        // 응답 dto 생성 및 반환
        return AuthResponseDTO.LoginDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
    }

    // 토큰 재발급
    @Transactional
    public AuthResponseDTO.LoginDto reIssue(HttpServletRequest request) {

        // 헤더 추출
        String header = request.getHeader("Authorization");

        // 헤더 없으면
        if (header == null)
            throw new ErrorHandler(AuthStatus.TOKEN_NOT_FOUND);

        // 헤더에서 토큰 추출
        String refreshToken = header.substring(7);

        // 재발급
        TokenResponseDTO tokenDTO = jwtTokenProvider.reissueTokens(refreshToken);

        // 응답 반환
        return AuthResponseDTO.LoginDto.builder()
                .accessToken(tokenDTO.getAccessToken())
                .refreshToken(tokenDTO.getRefreshToken())
                .build();
    }


}
