package DiffLens.back_end.global.security;

import DiffLens.back_end.domain.members.repository.RefreshTokenRepository;
import DiffLens.back_end.domain.members.service.auth.CurrentUserService;
import DiffLens.back_end.domain.members.service.auth.TokenBlackListService;
import DiffLens.back_end.global.responses.code.status.error.AuthStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenBlackListService tokenBlackListService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CurrentUserService currentUserService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        // 요청 정보에서 토큰 추출
        String headerToken = request.getHeader("Authorization");

        // 토큰이 없을 경우 처리
        if(headerToken == null) {
            throw new ErrorHandler(AuthStatus.TOKEN_NOT_FOUND);
        }

        // 헤더에서 토큰 추출
        String token = headerToken.substring(7);

        // Redis 내에 토큰이 존재하지 않는 경우
        if (!tokenBlackListService.isContainToken(token)) {
            tokenBlackListService.addTokenToList(token, JwtTokenExpirationTime.ACCESS_TOKEN.getExpirationMillis()); // BlackList 추가
        }

        Long memberId = currentUserService.getCurrentUserId();
        refreshTokenRepository.deleteByMemberId(memberId);

    }

}
