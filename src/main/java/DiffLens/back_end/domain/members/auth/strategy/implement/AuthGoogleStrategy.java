package DiffLens.back_end.domain.members.auth.strategy.implement;

import DiffLens.back_end.domain.members.dto.auth.GoogleResponseDTO;
import DiffLens.back_end.domain.members.enums.LoginType;
import DiffLens.back_end.domain.members.repository.MemberRepository;
import DiffLens.back_end.global.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuthGoogleStrategy extends AbstractSocialAuthStrategy {

    @Value("${security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final WebClient googleTokenWebClient;
    private final WebClient googleUserInfoWebClient;

    public AuthGoogleStrategy(
            MemberRepository memberRepository,
            JwtTokenProvider jwtTokenProvider,
            WebClient googleTokenWebClient,
            WebClient googleUserInfoWebClient
    ) {
        super(memberRepository, jwtTokenProvider);
        this.googleTokenWebClient = googleTokenWebClient;
        this.googleUserInfoWebClient = googleUserInfoWebClient;
    }

    @Override // google 로부터 access token 가져옴
    protected String getAccessToken(String code) {

        GoogleResponseDTO.GoogleTokenResponseDto tokenDto =
                googleTokenWebClient.post()
                        .uri("/token")
                        .body(BodyInserters.fromFormData("code", code)
                                .with("client_id", clientId)
                                .with("client_secret", clientSecret)
                                .with("redirect_uri", redirectUri)
                                .with("grant_type", "authorization_code"))
                        .retrieve()
                        .bodyToMono(GoogleResponseDTO.GoogleTokenResponseDto.class)
                        .block();

        return tokenDto.getAccessToken();
    }

    @Override // google 의 access token 으로 유저 정보 가져옴
    protected SocialUserInfo getUserInfo(String accessToken) {
        GoogleResponseDTO.GoogleUserInfoResponseDTO userInfo =
                googleUserInfoWebClient.get()
                        .uri("https://www.googleapis.com/oauth2/v2/userinfo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .retrieve()
                        .bodyToMono(GoogleResponseDTO.GoogleUserInfoResponseDTO.class)
                        .block();

        return SocialUserInfo.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .loginType(LoginType.GOOGLE)
                .build();
    }

    @Override
    protected LoginType getLoginType() {
        return LoginType.GOOGLE;
    }

}
