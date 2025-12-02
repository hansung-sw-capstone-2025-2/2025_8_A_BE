package DiffLens.back_end.domain.members.auth.strategy.implement;

import DiffLens.back_end.domain.members.auth.strategy.interfaces.AuthStrategy;
import DiffLens.back_end.domain.members.dto.auth.AuthRequestDTO;
import DiffLens.back_end.domain.members.dto.auth.TokenResponseDTO;
import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.enums.LoginType;
import DiffLens.back_end.domain.members.repository.MemberRepository;
import DiffLens.back_end.global.responses.code.status.error.AuthStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import DiffLens.back_end.global.security.JwtTokenProvider;
import DiffLens.back_end.global.security.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractSocialAuthStrategy implements AuthStrategy {

    protected final MemberRepository memberRepository;
    protected final JwtTokenProvider jwtTokenProvider;

    @Override
    public Boolean signUp(AuthRequestDTO.SignUp request) {
        // 소셜 로그인은 보통 signUp 단독 호출 필요 없음
        return true;
    }

    @Override
    public TokenResponseDTO login(Object request) {

        AuthRequestDTO.SocialLogin body = (AuthRequestDTO.SocialLogin) request;
        String code = body.getCode();
        String decoded = URLDecoder.decode(code, StandardCharsets.UTF_8);

        String loginTypeEn =  body.getLoginType().getEn();

        String accessToken;
        SocialUserInfo userInfo;

        // google 로부터 token 불러옴
        try {
            accessToken = getAccessToken(decoded);
        } catch (WebClientResponseException e) {
            log.error("{} Token API 오류: {}", loginTypeEn, e.getResponseBodyAsString());
            throw new ErrorHandler(AuthStatus.SOCIAL_TOKEN_API_ERROR);
        } catch (WebClientRequestException e) {
            log.error("{} Google Token API 네트워크 오류: {}", loginTypeEn, e.getMessage());
            throw new ErrorHandler(AuthStatus.SOCIAL_TOKEN_NETWORK_ERROR);
        } catch (Exception e) {
            log.error("{} Token 처리 중 예상치 못한 오류", loginTypeEn, e);
            throw new ErrorHandler(AuthStatus.SOCIAL_TOKEN_UNKNOWN_ERROR);
        }

        // google 로부터 유저 정보 가져옴
        try {
            userInfo = getUserInfo(accessToken);
        } catch (WebClientResponseException e) {
            log.error("{} UserInfo API 오류: {}", loginTypeEn, e.getResponseBodyAsString());
            throw new ErrorHandler(AuthStatus.SOCIAL_USERINFO_API_ERROR);
        } catch (WebClientRequestException e) {
            log.error("{} UserInfo API 네트워크 오류: {}", loginTypeEn, e.getMessage());
            throw new ErrorHandler(AuthStatus.SOCIAL_USERINFO_NETWORK_ERROR);
        } catch (Exception e) {
            log.error("{} UserInfo 처리 중 예상치 못한 오류", loginTypeEn, e);
            throw new ErrorHandler(AuthStatus.SOCIAL_USERINFO_UNKNOWN_ERROR);
        }

        // 멤버 유무 판단 후 create 메서드 호출
        Member member = memberRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> createMember(userInfo, body));

        return jwtTokenProvider.createToken(member);
    }

    protected abstract String getAccessToken(String code);

    protected abstract SocialUserInfo getUserInfo(String accessToken);

    // member 생성 후 저장
    protected Member createMember(SocialUserInfo userInfo, AuthRequestDTO.SocialLogin request) {
        Member member = Member.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .loginType(getLoginType())
                .password(null)
                .loginType(userInfo.getLoginType())
                .role(Role.ROLE_USER)
//                .roles(Set.of(UserType.USER))
                .plan(request.getPlan())
                .build();
        return memberRepository.save(member);
    }

    protected abstract LoginType getLoginType();

    // 공통 DTO
    @Getter
    @Builder
    public static class SocialUserInfo {
        private String email;
        private String name;
        private LoginType loginType;
    }


}
