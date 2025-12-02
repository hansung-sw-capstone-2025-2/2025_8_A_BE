package DiffLens.back_end.global.security;

import DiffLens.back_end.domain.members.service.auth.TokenBlackListService;
import DiffLens.back_end.global.responses.code.status.error.AuthStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlackListService blackListService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);
        String uri = request.getRequestURI();

        if (uri.equals("/auth/reissue")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (token != null) {

                if (blackListService.isContainToken(token)) {
                    throw new ErrorHandler(AuthStatus.EXPIRED_TOKEN);
                }

                if (jwtTokenProvider.validateToken(token)) {
                    // 단일 Role 기반 Authentication 생성
                    Authentication authentication = getAuthenticationFromToken(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid or expired token.");
                    return;
                }

            } else {
                Authentication anonymousAuth =
                        new AnonymousAuthenticationToken(
                                "anonymousUser",
                                "anonymousUser",
                                Collections.singletonList(
                                        new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
                SecurityContextHolder.getContext().setAuthentication(anonymousAuth);
            }

        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("\uD83D\uDEA8 {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * JWT에서 단일 Role 기반 Authentication 생성
     */
    private Authentication getAuthenticationFromToken(String token) {
        var claims = jwtTokenProvider.parseClaims(token);
        String email = (String) claims.get("email");
        if (email == null) email = (String) claims.get("sub");

        var member = jwtTokenProvider.getMemberByEmail(email);

        String roleStr = (String) claims.get("role");
        var authority = new SimpleGrantedAuthority(roleStr);

        return new UsernamePasswordAuthenticationToken(
                member, null, Collections.singletonList(authority)
        );
    }


    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
