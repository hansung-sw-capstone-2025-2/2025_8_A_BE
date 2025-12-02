package DiffLens.back_end.global.security;

import DiffLens.back_end.domain.members.repository.AccessTokenRepository;
import DiffLens.back_end.domain.members.service.auth.TokenBlackListService;
import DiffLens.back_end.global.responses.code.status.error.AuthStatus;
import DiffLens.back_end.domain.members.dto.auth.TokenResponseDTO;
import DiffLens.back_end.domain.members.dto.auth.TokenWithRolesResponseDTO;
import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.repository.MemberRepository;
import DiffLens.back_end.domain.members.repository.RefreshTokenRepository;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final MemberRepository memberRepository;
    private final TokenBlackListService tokenBlackListService;

    private Key key;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = JwtTokenExpirationTime.ACCESS_TOKEN.getExpirationMillis();
    private static final long REFRESH_TOKEN_EXPIRE_TIME = JwtTokenExpirationTime.REFRESH_TOKEN.getExpirationMillis();

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public TokenResponseDTO createToken(Member member) {

        // 항상 새로운 access token 발급
        String accessToken = createAccessToken(member);
        String refreshToken;

        Long memberId = member.getId();
        Optional<Long> existing = refreshTokenRepository.getExpirationByMemberId(memberId); // memberId로 refresh token 조회

        // 새로운 유저이거나 만료가 얼마 남지 않았으면 새로 발급
        if (existing.isEmpty() || existing.get() < (REFRESH_TOKEN_EXPIRE_TIME / 3)){
            refreshToken = createRefreshToken(member);
            refreshTokenRepository.saveToken(memberId, refreshToken, getExpiration(refreshToken));
        } else {
            refreshToken = refreshTokenRepository.getTokenByMemberId(memberId);
        }

        // accessToken Redis 저장
        accessTokenRepository.saveToken(memberId, accessToken, ACCESS_TOKEN_EXPIRE_TIME);

        return TokenResponseDTO.of(accessToken, refreshToken);
    }

    public TokenResponseDTO reissueTokens(String refreshToken) {

        if (!validateToken(refreshToken)) {
            throw new ErrorHandler(AuthStatus.INVALID_TOKEN);
        }

        Long memberId = refreshTokenRepository.getMemberIdByToken(refreshToken) // memberId 조회
                .orElseThrow(() -> new ErrorHandler(AuthStatus.REFRESH_TOKEN_NOT_FOUND));

        Member member = memberRepository.findById(memberId) // member 조회
                .orElseThrow(() -> new ErrorHandler(AuthStatus.USER_NOT_FOUND));

        Long remainingTime = refreshTokenRepository.getExpirationByMemberId(memberId) // 남은기간 조회
                .orElseThrow(() -> new ErrorHandler(AuthStatus.REFRESH_TOKEN_NOT_FOUND));

        accessTokenRepository.getCurrentToken(memberId)
                .ifPresent(oldToken -> tokenBlackListService.addTokenToList(oldToken, remainingTime));

        accessTokenRepository.getCurrentToken(memberId)
                .ifPresent(accessTokenRepository::deleteToken);


        String newAccessToken = createAccessToken(member);
        accessTokenRepository.saveToken(memberId, newAccessToken, ACCESS_TOKEN_EXPIRE_TIME);

        String newRefreshToken;

        // RefreshToken 남은 기간이 1/3 이하이면 새로 발급
        if (remainingTime < (REFRESH_TOKEN_EXPIRE_TIME / 3)) {
            newRefreshToken = createRefreshToken(member);
            refreshTokenRepository.saveToken(memberId, newRefreshToken, getExpiration(newRefreshToken));
        } else {
            newRefreshToken = refreshToken;
        }

        return TokenResponseDTO.of(newAccessToken, newRefreshToken);
    }


    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims =
                    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.warn("JWT Token expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {}", e.getMessage());
        } catch (SignatureException e) {
            log.warn("Signature exception: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Illegal JWT token: {}", e.getMessage());
        }
        return false;
    }

    public String createAccessToken(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.put("role", member.getRole().name()); // 권한 설정
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .claim("random", UUID.randomUUID().toString())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration =
                    Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody()
                            .getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public String getSubjectFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /*public Authentication getAuthentication(String token) {
        String email = getSubjectFromToken(token); // sub에서 email 꺼냄
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }*/

    public Authentication getAuthentication(String token) {
        // 1. 토큰 파싱 및 클레임 추출
        Claims claims =
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        // 2. 이메일(혹은 userId) 꺼내기
        String email = claims.get("email", String.class);
        if (email == null) {
            // 기존 방식처럼 subject를 email로 쓰는 경우
            email = claims.getSubject();
        }

        Member member =
                memberRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new ErrorHandler(AuthStatus.USER_NOT_FOUND));

        // 3. roles 클레임 꺼내기
        Object rolesObj = claims.get("roles");
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (rolesObj instanceof List<?> rolesList) {
            authorities =
                    rolesList.stream()
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
        }

        // 4. Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(member, null, authorities);
    }

    public long getExpiration(String token) {
        try {
            Claims claims =
                    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return claims.getExpiration().getTime() - System.currentTimeMillis();
        } catch (ExpiredJwtException e) {
            return 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Invalid JWT Token");
        }
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorHandler(AuthStatus.USER_NOT_FOUND));
    }


    public TokenWithRolesResponseDTO createTokenWithRoles(Member member) {
        // subject는 가급적 userId 기반 권장 (이메일 변경 이슈 방지). 기존 스타일을 그대로 쓰고 싶다면 이메일 유지도 가능.
        Claims claims = Jwts.claims().setSubject(String.valueOf(member.getId()));
        Date now = new Date();

//        List<String> roles = extractRoles(member); // ["ROLE_OWNER", "ROLE_USER"] 형태

        String accessToken =
                Jwts.builder()
                        .setClaims(claims)
                        .claim("uid", member.getId())
                        .claim("email", member.getEmail())
//                        .claim("roles", roles)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact();

        String refreshToken =
                Jwts.builder()
                        .setClaims(claims)
                        .claim("rand", UUID.randomUUID().toString()) // 재사용 방지용 랜덤 값
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact();

        long expiration = getExpiration(refreshToken);
        refreshTokenRepository.saveToken(member.getId(), refreshToken, expiration);

        return TokenWithRolesResponseDTO.of(accessToken, refreshToken);
    }

//    /** UserType → "ROLE_*" 문자열 리스트로 변환 */
//    private List<String> extractRoles(Member user) {
//        return user.getRoles().stream()
//                .map(UserType::getRoleName) // 예: UserType.OWNER -> "ROLE_OWNER"
//                .distinct()
//                .toList();
//    }

    public String issueSignupToken(String email, String jti, Duration ttl) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(email)
                .setId(jti)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttl.toMillis()))
                .claim("purpose", "signup")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Map<String, Object> parseClaims(String token) {
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return jws.getBody();
    }
}
