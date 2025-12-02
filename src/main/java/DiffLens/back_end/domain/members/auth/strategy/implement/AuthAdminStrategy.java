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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthAdminStrategy implements AuthStrategy {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Boolean signUp(AuthRequestDTO.SignUp request) {

        // 요청 정보 추출
        String email = request.getEmail();
        LoginType loginType = request.getLoginType();

        // 이미 존재하는 사용자
        if(memberRepository.existsByEmail(email)) {
            throw new ErrorHandler(AuthStatus.ALREADY_EXISTS);
        }

        // 유저 객체 생성
        Member member = Member.builder()
                .email(email)
                .name(request.getName())
                .loginType(loginType)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_ADMIN)
                .plan(request.getPlan())
                .build();

        // 유저 저장
        memberRepository.save(member);
        return true;

    }

    @Override
    public TokenResponseDTO login(Object request) {
        throw new ErrorHandler(AuthStatus.NOT_SUPPLYING);
    }
}
