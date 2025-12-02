package DiffLens.back_end.domain.members.auth.strategy.interfaces;

import DiffLens.back_end.domain.members.dto.auth.AuthRequestDTO;
import DiffLens.back_end.domain.members.dto.auth.TokenResponseDTO;

public interface AuthStrategy {

    /**
     *
     * @param request 회원가입 시 클라이언트에서 보내는 Request Body
     * @return 회원가입 성공 여부
     */
    public Boolean signUp(AuthRequestDTO.SignUp request);


    /**
     *
     * @param request 로그인 시 클라이언트에서 보내는 Request Body 혹은 소셜 서비스에서 반환받은 code
     * @return 회원의 로그인 인증 정보 ( Jwt 토큰 )
     */
    public TokenResponseDTO login(Object request);


}
